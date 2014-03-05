package com.iwami.iwami.app.service.impl;

import java.util.List;

import com.iwami.iwami.app.dao.TipsDao;
import com.iwami.iwami.app.model.Tips;
import com.iwami.iwami.app.service.TipsService;

public class TipsServiceImpl implements TipsService {

	private TipsDao tipsDao;
	
	@Override
	public List<Tips> getTips() {
		return tipsDao.getAllTips();
	}

	@Override
	public boolean delTipsByType(int type) {
		return tipsDao.delTipsByType(type);
	}

	@Override
	public boolean addTip(Tips tip) {
		return tipsDao.addTip(tip);
	}

	public TipsDao getTipsDao() {
		return tipsDao;
	}

	public void setTipsDao(TipsDao tipsDao) {
		this.tipsDao = tipsDao;
	}

}
