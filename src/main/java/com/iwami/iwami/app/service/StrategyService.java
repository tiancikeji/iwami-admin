package com.iwami.iwami.app.service;

import java.util.List;

import com.iwami.iwami.app.model.Strategy;
import com.iwami.iwami.app.model.StrategyImage;
import com.iwami.iwami.app.model.StrategyInfo;
import com.iwami.iwami.app.model.StrategyRate;

public interface StrategyService {

	public List<Strategy> getAllStrategies();
	
	public List<StrategyImage> getAllStragtegyImages();
	
	public List<StrategyInfo> getStrategyInfoByStrateByStrategyId(long strategyId, int start, int step);
	
	public StrategyRate getStrategyRateByStrategyId(long strategyId);
	
	public boolean incrStrategyRateSkim(long strategyId);

	public boolean incrStrategyRateRate(long strategyId);
	
	public boolean rateStrategy(long strategyId, String uuid);
}
