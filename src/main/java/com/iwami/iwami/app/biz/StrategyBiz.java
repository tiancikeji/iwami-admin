package com.iwami.iwami.app.biz;

import java.util.List;

import com.iwami.iwami.app.exception.DuplicateRateStrategyInfoException;
import com.iwami.iwami.app.model.Strategy;
import com.iwami.iwami.app.model.StrategyImage;
import com.iwami.iwami.app.model.StrategyInfo;
import com.iwami.iwami.app.model.StrategyRate;

public interface StrategyBiz {

	public List<Strategy> getAllStrategies();
	
	public List<StrategyImage> getAllStragtegyImages();
	
	public List<StrategyInfo> getStrategyInfoByStrateByStrategyId(long strategyId, int start, int step);
	
	public StrategyRate getStrategyRateByStrategyId(long strategyId);
	
	public boolean rateStrategy(long strategyId, String uuid) throws DuplicateRateStrategyInfoException;

	public Strategy getStrategyByStrategyId(long strategyId);
}
