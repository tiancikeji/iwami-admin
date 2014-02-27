package com.iwami.iwami.app.service.impl;

import java.util.List;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.dao.LuckyDao;
import com.iwami.iwami.app.model.LuckyConfig;
import com.iwami.iwami.app.model.LuckyRule;
import com.iwami.iwami.app.service.LuckyService;
import com.iwami.iwami.app.util.LocalCaches;

public class LuckyServiceImpl implements LuckyService {

	private LuckyDao luckyDao;
	
	private long expireTime;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<LuckyRule> getLuckyRules() {
		List<LuckyRule> rules = (List<LuckyRule>) LocalCaches.get(IWamiConstants.CACHE_LUCKY_RULE_KEY, System.currentTimeMillis(), expireTime);
		
		if(rules == null || rules.size() <= 0){
			rules = luckyDao.getLuckyRules();
			LocalCaches.set(IWamiConstants.CACHE_LUCKY_RULE_KEY, rules, System.currentTimeMillis());
		}
		
		return rules;
	}

	@Override
	public LuckyConfig getLuckyConfig() {
		LuckyConfig config = (LuckyConfig) LocalCaches.get(IWamiConstants.CACHE_LUCKY_CONFIG_KEY, System.currentTimeMillis(), expireTime);
		
		if(config == null){
			config = luckyDao.getLuckyConfig();
			LocalCaches.set(IWamiConstants.CACHE_LUCKY_CONFIG_KEY, config, System.currentTimeMillis());
		}
		
		return config;
	}

	public LuckyDao getLuckyDao() {
		return luckyDao;
	}

	public void setLuckyDao(LuckyDao luckyDao) {
		this.luckyDao = luckyDao;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
}
