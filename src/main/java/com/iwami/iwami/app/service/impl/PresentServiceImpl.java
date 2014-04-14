package com.iwami.iwami.app.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.dao.PresentDao;
import com.iwami.iwami.app.model.Exchange;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.model.Share;
import com.iwami.iwami.app.service.PresentService;

public class PresentServiceImpl implements PresentService {
	
	private PresentDao presentDao;

	@Override
	public List<Present> getPresentsByTypeNStatus(int type,String channel,  List<Integer> status, int start, int step) {
		return presentDao.getPresentsByTypeNStatus(type, channel, status, start, step);
	}

	@Override
	public int getPresentCountByTypeNStatus(int type, String channel, List<Integer> status){
		return presentDao.getPresentCountByTypeNStatus(type, channel, status);
	}

	@Override
	public boolean modPresent(Present present) {
		return presentDao.modPresent(present);
	}

	@Override
	public boolean addPresent(Present present) {
		return presentDao.addPresent(present);
	}

	@Override
	public boolean updatePresentURL(Present present) {
		return presentDao.updatePresentURL(present);
	}

	@Override
	public boolean delPresent(long id, long adminid) {
		return presentDao.delPresent(id, adminid);
	}

	@Override
	public boolean seqPresent(Map<Long, Integer> data, long adminid) {
		return presentDao.seqPresent(data, adminid);
	}

	@Override
	public List<Exchange> getExchangeHistory(List<Integer> types, List<Integer> status) {
		return presentDao.getExchangeHistory(types, status);
	}

	@Override
	public List<Exchange> getExchangeHistoryByUser(List<Integer> types, long key) {
		return presentDao.getExchangeHistoryByUser(types, key);
	}

	@Override
	public List<Exchange> getExchangeHistoryByPresent(List<Integer> types, String key) {
		return presentDao.getExchangeHistoryByPresent(types, key);
	}

	@Override
	public List<Exchange> getExchangeByIds(List<Long> ids) {
		return presentDao.getExchangeByIds(ids);
	}

	@Override
	public boolean modExchange(String name, String no, List<Long> ids, long adminid) {
		return presentDao.modExchange(name, no, ids, adminid);
	}

	@Override
	public List<Exchange> getExchanges(Date start, Date end) {
		return presentDao.getExchanges(start, end);
	}

	@Override
	public List<Exchange> getGifts(Date start, Date end) {
		return presentDao.getGifts(start, end);
	}

	@Override
	public List<Share> getShares(Date start, Date end) {
		return presentDao.getShares(start, end);
	}

	public PresentDao getPresentDao() {
		return presentDao;
	}

	public void setPresentDao(PresentDao presentDao) {
		this.presentDao = presentDao;
	}

	@Override
	public List<Present> getPresents() {
		return presentDao.getPresents();
	}

}
