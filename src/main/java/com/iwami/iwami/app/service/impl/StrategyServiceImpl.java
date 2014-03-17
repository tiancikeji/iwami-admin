package com.iwami.iwami.app.service.impl;

import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.dao.StrategyDao;
import com.iwami.iwami.app.model.Strategy;
import com.iwami.iwami.app.model.StrategyImage;
import com.iwami.iwami.app.model.StrategyInfo;
import com.iwami.iwami.app.model.StrategyRate;
import com.iwami.iwami.app.service.StrategyService;

public class StrategyServiceImpl implements StrategyService {

	private StrategyDao strategyDao;

	// rate
	@Override
	public Map<Long, StrategyRate> getRatesByIds(List<Long> ids) {
		return strategyDao.getRatesByIds(ids);
	}

	@Override
	public boolean delRateInfo(long id, long adminid) {
		return strategyDao.delRateInfo(id, adminid);
	}

	@Override
	public boolean delRate(long id, long adminid) {
		return strategyDao.delRate(id, adminid);
	}

	@Override
	public boolean modRate(StrategyRate rate) {
		return strategyDao.modRate(rate);
	}

	@Override
	public boolean addRate(StrategyRate rate) {
		return strategyDao.addRate(rate);
	}
	
	// strategy
	@Override
	public List<Strategy> getStrategies(String key) {
		return strategyDao.getStrategies(key);
	}

	@Override
	public boolean delStrategy(long id, long adminid) {
		return strategyDao.delStrategy(id, adminid);
	}
	
	@Override
	public boolean modStrategySeqs(List<Long> lIds, List<Integer> lRanks, long adminid) {
		return strategyDao.modStrategySeqls(lIds, lRanks, adminid);
	}

	@Override
	public boolean modStrategy(Strategy strategy) {
		return strategyDao.modStrategy(strategy);
	}

	@Override
	public boolean updateStrategyUrl(Strategy strategy) {
		return strategyDao.updateStrategyUrl(strategy);
	}

	@Override
	public long addStrategy(Strategy strategy) {
		return strategyDao.addStrategy(strategy);
	}

	// image
	@Override
	public boolean addImage(StrategyImage image) {
		return strategyDao.addImage(image);
	}

	@Override
	public boolean modImage(StrategyImage image) {
		return strategyDao.modImage(image);
	}

	@Override
	public boolean updateImageUrl(StrategyImage image) {
		return strategyDao.updateImageUrl(image);
	}

	@Override
	public boolean delImage(int id, long adminid) {
		return strategyDao.delImage(id, adminid);
	}

	@Override
	public List<StrategyImage> getAllImages() {
		return strategyDao.getAllImages();
	}

	@Override
	public boolean modImageSeqs(List<Long> lIds, List<Integer> lRanks, long adminid) {
		return strategyDao.modImageSeqs(lIds, lRanks, adminid);
	}

	// info
	@Override
	public List<StrategyInfo> getInfos(long id) {
		return strategyDao.getInfos(id);
	}

	@Override
	public boolean addInfo(StrategyInfo info) {
		return strategyDao.addInfo(info);
	}

	@Override
	public boolean modInfo(StrategyInfo info) {
		return strategyDao.modInfo(info);
	}

	@Override
	public boolean updateInfoUrl(StrategyInfo info) {
		return strategyDao.updateInfoUrl(info);
	}

	@Override
	public boolean delInfo(long id, long adminid) {
		return strategyDao.delInfo(id, adminid);
	}

	@Override
	public boolean delInfos(long id, long adminid) {
		return strategyDao.delInfos(id, adminid);
	}

	public StrategyDao getStrategyDao() {
		return strategyDao;
	}

	public void setStrategyDao(StrategyDao strategyDao) {
		this.strategyDao = strategyDao;
	}

}
