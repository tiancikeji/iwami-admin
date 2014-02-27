package com.iwami.iwami.app.biz.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import com.iwami.iwami.app.biz.StrategyBiz;
import com.iwami.iwami.app.comparator.StrategyImageRankComparator;
import com.iwami.iwami.app.comparator.StrategyInfoComparator;
import com.iwami.iwami.app.comparator.StrategyRankComparator;
import com.iwami.iwami.app.exception.DuplicateRateStrategyInfoException;
import com.iwami.iwami.app.model.Strategy;
import com.iwami.iwami.app.model.StrategyImage;
import com.iwami.iwami.app.model.StrategyInfo;
import com.iwami.iwami.app.model.StrategyRate;
import com.iwami.iwami.app.service.StrategyService;

public class StrategyBizImpl implements StrategyBiz {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private StrategyService strategyService;

	@Override
	public List<Strategy> getAllStrategies() {
		List<Strategy> strategies = strategyService.getAllStrategies();
		
		if(strategies != null && strategies.size() > 0){
			Collections.sort(strategies, new StrategyRankComparator());
			
			for(int i = 0; i < strategies.size(); i ++)
				strategies.get(i).setRank(i);
		}
		
		return strategies;
	}

	@Override
	public List<StrategyImage> getAllStragtegyImages() {
		List<StrategyImage> images = strategyService.getAllStragtegyImages();
		
		if(images != null && images.size() > 0){
			Collections.sort(images, new StrategyImageRankComparator());
			
			for(int i = 0; i < images.size(); i ++)
				images.get(i).setRank(i);
		}
		
		return images;
	}

	@Override
	public List<StrategyInfo> getStrategyInfoByStrateByStrategyId(long strategyId, int start, int step) {
		List<StrategyInfo> infos = strategyService.getStrategyInfoByStrateByStrategyId(strategyId, start, step);
		
		if(infos != null && infos.size() > 0){
			Collections.sort(infos, new StrategyInfoComparator());
			
			for(int i = 0; i < infos.size(); i ++)
				infos.get(i).setRank(start + i);
		}
		
		if(start == 0)
			try{
				strategyService.incrStrategyRateSkim(strategyId);
			} catch(Throwable t){
				if(logger.isErrorEnabled())
					logger.error("Error in incr skim for <" + strategyId + "> : ", t);
			}
		
		return infos;
	}

	@Override
	public StrategyRate getStrategyRateByStrategyId(long strategyId) {
		return strategyService.getStrategyRateByStrategyId(strategyId);
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean rateStrategy(long strategyId, String uuid) throws DuplicateRateStrategyInfoException {
		try{
			strategyService.rateStrategy(strategyId, uuid);
			strategyService.incrStrategyRateRate(strategyId);
		} catch(DuplicateKeyException e){
			if(logger.isErrorEnabled())
				logger.error("DuplicateKeyException in rateStrategy for <" + strategyId + "," + uuid + "> : ", e);
			throw new DuplicateRateStrategyInfoException();
		}
		return true;
	}

	@Override
	public Strategy getStrategyByStrategyId(long strategyId) {
		List<Strategy> strategies = getAllStrategies();
		
		if(strategies != null && strategies.size() > 0)
			for(Strategy strategy : strategies)
				if(strategy != null && strategy.getId() == strategyId)
					return strategy;
		
		return null;
	}

	public StrategyService getStrategyService() {
		return strategyService;
	}

	public void setStrategyService(StrategyService strategyService) {
		this.strategyService = strategyService;
	}

}
