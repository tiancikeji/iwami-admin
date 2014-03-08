package com.iwami.iwami.app.biz;

import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.model.Strategy;
import com.iwami.iwami.app.model.StrategyImage;
import com.iwami.iwami.app.model.StrategyInfo;
import com.iwami.iwami.app.model.StrategyRate;

public interface StrategyBiz {

	// rates
	public Map<Long, StrategyRate> getRatesByIds(List<Long> ids);

	// strategies
	public List<Strategy> getStrategies(String key);

	public boolean delStrategy(long id, long adminid);

	public boolean modStrategySeqs(List<Long> lIds, List<Integer> lRanks, long adminid);

	public boolean modStrategy(Strategy strategy, StrategyRate rate);

	public long addStrategy(Strategy strategy, StrategyRate srate);
	
	// info
	public List<StrategyInfo> getInfos(long id);

	public boolean addInfo(StrategyInfo info);

	public boolean modInfo(StrategyInfo info);

	public boolean delInfo(long id, long adminid);
	
	// images
	public List<StrategyImage> getAllImages();

	public boolean addImage(StrategyImage image);

	public boolean modImage(StrategyImage image);

	public boolean modImageSeqs(List<Long> lIds, List<Integer> lRanks, long adminid);

	public boolean delImage(int id, long adminid);
}
