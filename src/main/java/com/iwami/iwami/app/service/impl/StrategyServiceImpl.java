package com.iwami.iwami.app.service.impl;

import java.util.List;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.dao.StrategyDao;
import com.iwami.iwami.app.model.Strategy;
import com.iwami.iwami.app.model.StrategyImage;
import com.iwami.iwami.app.model.StrategyInfo;
import com.iwami.iwami.app.model.StrategyRate;
import com.iwami.iwami.app.service.StrategyService;
import com.iwami.iwami.app.util.LocalCaches;

@SuppressWarnings("unchecked")
public class StrategyServiceImpl implements StrategyService {

	private StrategyDao strategyDao;
	
	private long expireTime;
	
	@Override
	public List<Strategy> getAllStrategies() {
		List<Strategy> strategies = (List<Strategy>)LocalCaches.get(IWamiConstants.CACHE_STRATEGY_KEY, System.currentTimeMillis(), expireTime);
		if(strategies == null){
			strategies = strategyDao.getAllStrategies();
			LocalCaches.set(IWamiConstants.CACHE_STRATEGY_KEY, strategies, System.currentTimeMillis());
		}
		return strategies;
	}

	@Override
	public List<StrategyImage> getAllStragtegyImages() {
		List<StrategyImage> images = (List<StrategyImage>)LocalCaches.get(IWamiConstants.CACHE_STRATEGY_IMAGE_KEY, System.currentTimeMillis(), expireTime);
		if(images == null){
			images = strategyDao.getAllStrategyImages();
			LocalCaches.set(IWamiConstants.CACHE_STRATEGY_IMAGE_KEY, images, System.currentTimeMillis());
		}
		return images;
	}

	@Override
	public List<StrategyInfo> getStrategyInfoByStrateByStrategyId(long strategyId, int start, int step) {
		return strategyDao.getStrategyInfosByStrategyId(strategyId, start, step);
	}

	@Override
	public StrategyRate getStrategyRateByStrategyId(long strategyId) {
		return strategyDao.getStrategyRateByStrategyId(strategyId);
	}

	@Override
	public boolean rateStrategy(long strategyId, String uuid) {
		return strategyDao.rateStrategy(strategyId, uuid);
	}

	@Override
	public boolean incrStrategyRateSkim(long strategyId) {
		return strategyDao.incrStrategyRateSkim(strategyId);
	}

	@Override
	public boolean incrStrategyRateRate(long strategyId) {
		return strategyDao.incrStrategyRateRate(strategyId);
	}

	public StrategyDao getStrategyDao() {
		return strategyDao;
	}

	public void setStrategyDao(StrategyDao strategyDao) {
		this.strategyDao = strategyDao;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

}
