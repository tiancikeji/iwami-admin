package com.iwami.iwami.app.service.impl;

import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.dao.PresentDao;
import com.iwami.iwami.app.model.Exchange;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.service.PresentService;

public class PresentServiceImpl implements PresentService {
	
	private PresentDao presentDao;

	@Override
	public List<Present> getPresentsByTypeNStatus(int type, List<Integer> status) {
		return presentDao.getPresentsByTypeNStatus(type, status);
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
	public Exchange getExchangeById(long id) {
		return presentDao.getExchangeById(id);
	}

	@Override
	public boolean modExchange(String name, String no, long id, long adminid) {
		return presentDao.modExchange(name, no, id, adminid);
	}

	public PresentDao getPresentDao() {
		return presentDao;
	}

	public void setPresentDao(PresentDao presentDao) {
		this.presentDao = presentDao;
	}

}
