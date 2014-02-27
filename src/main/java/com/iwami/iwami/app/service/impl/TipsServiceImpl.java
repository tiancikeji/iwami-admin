package com.iwami.iwami.app.service.impl;

import java.util.List;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.dao.TipsDao;
import com.iwami.iwami.app.model.Tips;
import com.iwami.iwami.app.service.TipsService;
import com.iwami.iwami.app.util.LocalCaches;

public class TipsServiceImpl implements TipsService {

	private TipsDao tipsDao;
	
	private long expireTime;

	@Override
	@SuppressWarnings("unchecked")
	public Tips getTips(int type) {
		List<Tips> tips = (List<Tips>)LocalCaches.get(IWamiConstants.CACHE_TIPS_KEY, System.currentTimeMillis(), expireTime);
		if(tips == null){
			tips = tipsDao.getAllTips();
			LocalCaches.set(IWamiConstants.CACHE_TIPS_KEY, tips, System.currentTimeMillis());
		}
		
		if(tips != null)
			for(Tips tip : tips)
				if(tip.getType() == type)
					return tip;
		
		return null;
	}

	public TipsDao getTipsDao() {
		return tipsDao;
	}

	public void setTipsDao(TipsDao tipsDao) {
		this.tipsDao = tipsDao;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

}
