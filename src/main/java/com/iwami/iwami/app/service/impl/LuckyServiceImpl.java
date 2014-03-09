package com.iwami.iwami.app.service.impl;

import java.util.List;

import com.iwami.iwami.app.dao.LuckyDao;
import com.iwami.iwami.app.model.LuckyRule;
import com.iwami.iwami.app.service.LuckyService;

public class LuckyServiceImpl implements LuckyService {

	private LuckyDao luckyDao;

	@Override
	public List<LuckyRule> getLuckyRules() {
		return luckyDao.getLuckyRules();
	}

	@Override
	public boolean delRules(long adminid) {
		return luckyDao.delRules(adminid);
	}

	@Override
	public boolean modRules(List<LuckyRule> rules) {
		return luckyDao.modRules(rules);
	}

	public LuckyDao getLuckyDao() {
		return luckyDao;
	}

	public void setLuckyDao(LuckyDao luckyDao) {
		this.luckyDao = luckyDao;
	}
}
