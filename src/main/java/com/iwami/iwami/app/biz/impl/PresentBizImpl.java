package com.iwami.iwami.app.biz.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.iwami.iwami.app.biz.PresentBiz;
import com.iwami.iwami.app.comparator.ExchangeHistoryComparator;
import com.iwami.iwami.app.exception.NotEnoughPrizeException;
import com.iwami.iwami.app.model.Exchange;
import com.iwami.iwami.app.model.ExchangeHistory;
import com.iwami.iwami.app.model.Gift;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.model.Share;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.service.PresentService;
import com.iwami.iwami.app.service.UserService;

public class PresentBizImpl implements PresentBiz {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private PresentService presentService;
	
	private UserService userService;

	@Override
	public List<Present> getAllPresents(long userid) {
		return presentService.getAllAvailablePresents();
	}

	@Override
	public boolean gift(User user, User user2, int prize) throws NotEnoughPrizeException {
		Exchange exchange = new Exchange();
		exchange.setUserid(user.getId());
		exchange.setPresentId(user2.getId());
		exchange.setPresentName("赠" + user2.getName());
		exchange.setPresentPrize(-1);
		exchange.setPresentType(Present.TYPE_GIFT);
		exchange.setCount(1);
		exchange.setPrize(prize);
		exchange.setStatus(Exchange.STATUS_NEW);
		exchange.setLastModUserid(user.getId());
		
		long exchangeid = presentService.addExchange(exchange);
		int status = Exchange.STATUS_FAILED;
		
		try{
			doGift(user, user2, prize);
			status = Exchange.STATUS_FINISH;
			
			// jpush TODO
			String msg = user.getName() + "|" + prize;
		} catch(NotEnoughPrizeException e){
			if(logger.isErrorEnabled())
				logger.error("exception in gift from " + user.getId() + " to " + user2.getId() + " <" + prize + ">", e);
			throw e;
		} finally{
			presentService.updateExchangeStatus(exchangeid, status);
		}
		
		return true;
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public void doGift(User user, User user2, int prize) throws NotEnoughPrizeException {
		if(userService.subUserCurrentNExchangePrize(user.getId(), prize))
			userService.addUserCurrentPrize(user2.getId(), prize);
		else
			throw new NotEnoughPrizeException();
	}

	@Override
	public boolean addShareExchange(long userid, int type, int target, String msg) {
		Share share = new Share();
		share.setUserid(userid);
		share.setType(type);
		share.setTarget(target);
		share.setMsg(msg);
		return presentService.addShareExchange(share);
	}

	@Override
	public Map<Long, Present> getPresentsByIds(List<Long> ids) {
		List<Present> presents = presentService.getAllAvailablePresents();
		Map<Long, Present> result = new HashMap<Long, Present>();
		
		if(presents != null && presents.size() > 0)
			for(Present present : presents)
				if(ids.contains(present.getId()))
					result.put(present.getId(), present);
		
		return result;
	}

	@Override
	public boolean exchangeExpress(User user, Map<Present, Integer> presentCnts, long cellPhone, String address, String name) throws NotEnoughPrizeException {
		int allPrize = 0;
		List<Exchange> exchanges = new ArrayList<Exchange>();
		for(Present present : presentCnts.keySet()){
			Exchange exchange = new Exchange();
			exchange.setUserid(user.getId());
			exchange.setPresentId(present.getId());
			exchange.setPresentName(present.getName());
			exchange.setPresentPrize(present.getPrize());
			exchange.setPresentType(present.getType());
			int count = presentCnts.get(present);
			exchange.setCount(count);
			int prize = present.getPrize() * count;
			exchange.setPrize(prize);
			allPrize += prize;
			exchange.setStatus(Exchange.STATUS_NEW);
			exchange.setCellPhone(cellPhone);
			exchange.setAddress(address);
			exchange.setName(name);
			exchange.setLastModUserid(user.getId());
			exchanges.add(exchange);
		}
		
		List<Long> ids = new ArrayList<Long>();
		for(Exchange exchange : exchanges)
			ids.add(presentService.addExchange(exchange));
		
		int status = Exchange.STATUS_FAILED;
		if(userService.updateUser4ExpressExchange(user.getId(), allPrize, cellPhone, address, name))
			status = Exchange.STATUS_READY;
		
		presentService.updateExchangesStatus(ids, status);
		
		return status == Exchange.STATUS_READY;
	}

	@Override
	public boolean exchangeAlipay(User user, Present present, int prize, String aliAccount) {
		Exchange exchange = new Exchange();
		exchange.setUserid(user.getId());
		exchange.setPresentId(present.getId());
		exchange.setPresentName(present.getName());
		exchange.setPresentType(present.getType());
		exchange.setPresentPrize(present.getPrize());
		exchange.setCount(1);
		exchange.setPrize(prize);
		exchange.setStatus(Exchange.STATUS_NEW);
		exchange.setAlipayAccount(aliAccount);
		exchange.setLastModUserid(user.getId());
		
		long id = presentService.addExchange(exchange);
		
		int status = Exchange.STATUS_FAILED;
		if(userService.updateUser4AlipayExchange(user.getId(), prize, aliAccount))
			status = Exchange.STATUS_READY;
		
		presentService.updateExchangeStatus(id, status);
		
		return status == Exchange.STATUS_READY;
	}

	@Override
	public boolean exchangeBank(User user, Present present, int prize, String bankAccount, String bankName, long bankNo) {
		Exchange exchange = new Exchange();
		exchange.setUserid(user.getId());
		exchange.setPresentId(present.getId());
		exchange.setPresentName(present.getName());
		exchange.setPresentType(present.getType());
		exchange.setPresentPrize(present.getPrize());
		exchange.setCount(1);
		exchange.setPrize(prize);
		exchange.setStatus(Exchange.STATUS_NEW);
		exchange.setBankName(bankName);
		exchange.setBankAccount(bankAccount);
		exchange.setBankNo(bankNo);
		exchange.setLastModUserid(user.getId());
		
		long id = presentService.addExchange(exchange);
		
		int status = Exchange.STATUS_FAILED;
		if(userService.updateUser4BankExchange(user.getId(), prize, bankAccount, bankName, bankNo))
			status = Exchange.STATUS_READY;
		
		presentService.updateExchangeStatus(id, status);
		
		return status == Exchange.STATUS_READY;
	}

	@Override
	public boolean exchangeMobile(User user, Present present, long cellPhone) {
		Exchange exchange = new Exchange();
		exchange.setUserid(user.getId());
		exchange.setPresentId(present.getId());
		exchange.setPresentName(present.getName());
		exchange.setPresentType(present.getType());
		exchange.setPresentPrize(present.getPrize());
		exchange.setCount(1);
		exchange.setPrize(present.getPrize());
		exchange.setStatus(Exchange.STATUS_NEW);
		exchange.setCellPhone(cellPhone);
		exchange.setLastModUserid(user.getId());
		
		long id = presentService.addExchange(exchange);
		
		int status = Exchange.STATUS_FAILED;
		if(userService.updateUser4MobileExchange(user.getId(), present.getPrize(), cellPhone))
			status = Exchange.STATUS_READY;
		
		presentService.updateExchangeStatus(id, status);
		
		return status == Exchange.STATUS_READY;
	}

	@Override
	public List<ExchangeHistory> getExchangeHistory(long userid) {
		List<Exchange> exchanges = presentService.getAllExchanges(userid);
		// time - type - status - exchangehistory
		List<ExchangeHistory> result = new ArrayList<ExchangeHistory>();
		if(exchanges != null && exchanges.size() > 0){
			Map<Long, Map<Integer, Map<Integer, ExchangeHistory>>> tmp = new HashMap<Long, Map<Integer,Map<Integer,ExchangeHistory>>>();
			
			for(Exchange exchange : exchanges)
				if(exchange != null){
					long time = DateUtils.truncate(exchange.getAddTime(), Calendar.DATE).getTime();
					int type = exchange.getPresentType();
					int status = ExchangeHistory.STATUS_NEW;
					if(type == Present.TYPE_OFFLINE)
						status = ExchangeHistory.STATUS_OFFLINE;
					else if(exchange.getStatus() == Exchange.STATUS_FINISH)
						status = ExchangeHistory.STATUS_SENT;
					
					Map<Integer, Map<Integer, ExchangeHistory>> tmp1 = tmp.get(time);
					if(tmp1 == null){
						tmp1 = new HashMap<Integer, Map<Integer,ExchangeHistory>>();
						tmp.put(time, tmp1);
					}
					
					Map<Integer, ExchangeHistory> tmp2 = tmp1.get(type);
					if(tmp2 == null){
						tmp2 = new HashMap<Integer, ExchangeHistory>();
						tmp1.put(type, tmp2);
					}
					
					ExchangeHistory tmp3 = tmp2.get(status);
					if(tmp3 == null){
						tmp3 = new ExchangeHistory();
						tmp3.setTime(time);
						tmp3.setType(type);
						tmp3.setStatus(status);
						tmp2.put(status, tmp3);
						result.add(tmp3);
					}
					
					List<Gift> gifts = tmp3.getGifts();
					if(gifts == null){
						gifts = new ArrayList<Gift>();
						tmp3.setGifts(gifts);
					}
					
					Gift gift = new Gift();
					gift.setName(exchange.getPresentName());
					gift.setCount(exchange.getCount());
					gift.setPrize(exchange.getPrize());
					
					gifts.add(gift);
				}
			
			Collections.sort(result, new ExchangeHistoryComparator());
		}
		return result;
	}

	@Override
	public boolean exchangeOffline(User user, Map<Present, Integer> presentCnts, String channel) {
		int allPrize = 0;
		List<Exchange> exchanges = new ArrayList<Exchange>();
		for(Present present : presentCnts.keySet()){
			Exchange exchange = new Exchange();
			exchange.setUserid(user.getId());
			exchange.setPresentId(present.getId());
			exchange.setPresentName(present.getName());
			exchange.setPresentPrize(present.getPrize());
			exchange.setPresentType(present.getType());
			int count = presentCnts.get(present);
			exchange.setCount(count);
			int prize = present.getPrize() * count;
			exchange.setPrize(prize);
			allPrize += prize;
			exchange.setStatus(Exchange.STATUS_NEW);
			exchange.setChannel(channel);
			exchange.setLastModUserid(user.getId());
			exchanges.add(exchange);
		}
		
		List<Long> ids = new ArrayList<Long>();
		for(Exchange exchange : exchanges)
			ids.add(presentService.addExchange(exchange));
		
		int status = Exchange.STATUS_FAILED;
		if(userService.updateUser4OfflineExchange(user.getId(), allPrize))
			status = Exchange.STATUS_FINISH;
		
		presentService.updateExchangesStatus(ids, status);
		
		return status == Exchange.STATUS_FINISH;
	}

	@Override
	public Map<Long, Present> getOfflinePresentsByIds(List<Long> ids) {
		return presentService.getPresentsByIds(ids);
	}

	public PresentService getPresentService() {
		return presentService;
	}

	public void setPresentService(PresentService presentService) {
		this.presentService = presentService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
