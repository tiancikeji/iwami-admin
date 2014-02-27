package com.iwami.iwami.app.dao;

import java.util.List;

import com.iwami.iwami.app.model.Strategy;
import com.iwami.iwami.app.model.StrategyImage;
import com.iwami.iwami.app.model.StrategyInfo;
import com.iwami.iwami.app.model.StrategyRate;

public interface StrategyDao {

	public List<StrategyImage> getAllStrategyImages();
	
	public List<Strategy> getAllStrategies();
	
	public StrategyRate getStrategyRateByStrategyId(long strategyId);
	
	public List<StrategyInfo> getStrategyInfosByStrategyId(long strategyId, int start, int step);
	
	public boolean rateStrategy(long strategyId, String uuid);
	
	public boolean incrStrategyRateSkim(long strategyId);
	
	public boolean incrStrategyRateRate(long strategyId);
}
