package com.iwami.iwami.app.biz.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.iwami.iwami.app.biz.FileBiz;
import com.iwami.iwami.app.biz.StrategyBiz;
import com.iwami.iwami.app.comparator.StrategyImageRankComparator;
import com.iwami.iwami.app.comparator.StrategyInfoComparator;
import com.iwami.iwami.app.comparator.StrategyRankComparator;
import com.iwami.iwami.app.model.Strategy;
import com.iwami.iwami.app.model.StrategyImage;
import com.iwami.iwami.app.model.StrategyInfo;
import com.iwami.iwami.app.model.StrategyRate;
import com.iwami.iwami.app.service.StrategyService;

public class StrategyBizImpl implements StrategyBiz {
	
	private StrategyService strategyService;
	
	private FileBiz fileBiz;

	// image
	@Override
	public boolean addImage(StrategyImage image) {
		if(strategyService.addImage(image)){
			fileBiz.uploadImageResource(image);
			return strategyService.updateImageUrl(image);
		} else
			return false;
	}

	@Override
	public boolean modImage(StrategyImage image) {
		fileBiz.uploadImageResource(image);
		return strategyService.modImage(image);
	}

	@Override
	public boolean delImage(int id, long adminid) {
		return strategyService.delImage(id, adminid);
	}

	@Override
	public boolean modImageSeqs(List<Long> lIds, List<Integer> lRanks, long adminid) {
		return strategyService.modImageSeqs(lIds, lRanks, adminid);
	}

	@Override
	public List<StrategyImage> getAllImages() {
		List<StrategyImage> images = strategyService.getAllImages();
		
		if(images != null && images.size() > 0){
			Collections.sort(images, new StrategyImageRankComparator());
			
			for(int i = 0; i < images.size(); i ++)
				images.get(i).setRank(i);
		}
		
		return images;
	}

	// strategy
	@Override
	public List<Strategy> getStrategies(String key) {
		List<Strategy> strategies = strategyService.getStrategies(key);
		
		if(strategies != null && strategies.size() > 0){
			Collections.sort(strategies, new StrategyRankComparator());
			
			for(int i = 0; i < strategies.size(); i ++)
				strategies.get(i).setRank(i);
		}
		
		return strategies;
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public long addStrategy(Strategy strategy, StrategyRate rate) {
		long id = strategyService.addStrategy(strategy);
		if(id > 0){
			strategy.setId(id);
			fileBiz.uploadStrategyResource(strategy);
			strategyService.updateStrategyUrl(strategy);
			rate.setStrategyId(id);
			if(!strategyService.addRate(rate))
				throw new RuntimeException("exception in modrate, so rollback");
			else
				return id;
		} else
			return 0;
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean modStrategy(Strategy strategy, StrategyRate rate) {
		fileBiz.uploadStrategyResource(strategy);
		if(strategyService.modStrategy(strategy)){
			if(!strategyService.modRate(rate))
				throw new RuntimeException("exception in modrate, so rollback");
			else
				return true;
		} else
			return false;
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean delStrategy(long id, long adminid) {
		if(strategyService.delStrategy(id, adminid))
			if(strategyService.delInfos(id, adminid))
				return true;
			else
				throw new RuntimeException("exception in delStrategy, so rollback");
		else
			return false;
	}

	@Override
	public boolean modStrategySeqs(List<Long> lIds, List<Integer> lRanks, long adminid) {
		return strategyService.modStrategySeqs(lIds, lRanks, adminid);
	}

	// rate
	@Override
	public Map<Long, StrategyRate> getRatesByIds(List<Long> ids) {
		return strategyService.getRatesByIds(ids);
	}

	// info
	@Override
	public List<StrategyInfo> getInfos(long id) {
		List<StrategyInfo> infos = strategyService.getInfos(id);
		
		if(infos != null && infos.size() > 0){
			Collections.sort(infos, new StrategyInfoComparator());
			
			for(int i = 0; i < infos.size(); i ++)
				infos.get(i).setRank(i);
		}
		return infos;
	}

	@Override
	public boolean addInfo(StrategyInfo info) {
		if(strategyService.addInfo(info)){
			fileBiz.uploadStrategyInfoResource(info);
			return strategyService.updateInfoUrl(info);
		} else
			return false;
	}

	@Override
	public boolean modInfo(StrategyInfo info) {
		fileBiz.uploadStrategyInfoResource(info);
		return strategyService.modInfo(info);
	}

	@Override
	public boolean delInfo(long id, long adminid) {
		return strategyService.delInfo(id, adminid);
	}

	@Override
	public boolean delInfos(long id, long adminid) {
		return strategyService.delInfos(id, adminid);
	}

	public StrategyService getStrategyService() {
		return strategyService;
	}

	public void setStrategyService(StrategyService strategyService) {
		this.strategyService = strategyService;
	}

	public FileBiz getFileBiz() {
		return fileBiz;
	}

	public void setFileBiz(FileBiz fileBiz) {
		this.fileBiz = fileBiz;
	}

}
