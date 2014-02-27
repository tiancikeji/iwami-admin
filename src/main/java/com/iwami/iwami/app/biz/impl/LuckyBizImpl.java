package com.iwami.iwami.app.biz.impl;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.iwami.iwami.app.biz.LuckyBiz;
import com.iwami.iwami.app.comparator.LuckyRuleComparator;
import com.iwami.iwami.app.exception.LuckyExceedLimitException;
import com.iwami.iwami.app.exception.NotEnoughPrizeException;
import com.iwami.iwami.app.model.Exchange;
import com.iwami.iwami.app.model.LuckyConfig;
import com.iwami.iwami.app.model.LuckyRule;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.service.LuckyService;
import com.iwami.iwami.app.service.PresentService;
import com.iwami.iwami.app.service.UserService;
import com.iwami.iwami.app.util.IWamiUtils;

public class LuckyBizImpl implements LuckyBiz {
	
	private LuckyService luckyService;
	
	private UserService userService;
	
	private PresentService presentService;

	@Override
	public List<LuckyRule> getLuckyRules() {
		List<LuckyRule> rules = luckyService.getLuckyRules();
		if(rules != null && rules.size() > 0)
			Collections.sort(rules, new LuckyRuleComparator());
		return rules;
	}

	@Override
	public LuckyConfig getLuckyConfig() {
		return luckyService.getLuckyConfig();
	}

	@Override
	public LuckyRule draw(User user, LuckyConfig config) throws LuckyExceedLimitException, NotEnoughPrizeException {
		/*if(config.getCount() >= 0){
			int count = luckyService.getLuckyCountByUserid(user.getId());
			if(count >= config.getCount())
				throw new LuckyExceedLimitException();
		}*/
		
		List<LuckyRule> rules = luckyService.getLuckyRules();
		if(rules == null || rules.size() <= 0)
			throw new RuntimeException("no lucky rules at all...");
		Collections.sort(rules, new LuckyRuleComparator());
		
		LuckyRule frule = null;
		int prob = new Random(System.currentTimeMillis()).nextInt(10000);
		for(LuckyRule rule : rules){
			prob = prob - rule.getProb();
			if(prob < 0)
				frule = rule;
		}
		
		Exchange exchange = new Exchange();
		exchange.setUserid(user.getId());
		exchange.setPresentId(-1);
		exchange.setPresentPrize(-1);
		exchange.setPresentName("未中奖");
		exchange.setPresentType(Present.TYPE_LUCK);
		exchange.setCount(1);
		exchange.setPrize(config.getPrize());
		exchange.setStatus(Exchange.STATUS_NEW);
		if(frule != null && presentService.getLuckyExchangeCount(frule.getId(), IWamiUtils.getTodayStart()) <= frule.getCount()){
			exchange.setPresentId(frule.getId());
			exchange.setPresentName(frule.getGift());
			exchange.setPresentPrize(frule.getIndexLevel());
		} else
			frule = null;
		
		long presentId = presentService.addExchange(exchange);
		
		int status = Exchange.STATUS_FAILED;
		
		// substract from user.current_price
		boolean result = userService.subUserCurrentNExchangePrize(user.getId(), config.getPrize());
		
		if(result)
			status = Exchange.STATUS_READY;
		
		presentService.updateExchangeStatus(presentId, status);
		
		if(!result)
			throw new NotEnoughPrizeException();
		else
			return frule;
	}

	public LuckyService getLuckyService() {
		return luckyService;
	}

	public void setLuckyService(LuckyService luckyService) {
		this.luckyService = luckyService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public PresentService getPresentService() {
		return presentService;
	}

	public void setPresentService(PresentService presentService) {
		this.presentService = presentService;
	}

}
