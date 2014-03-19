package com.iwami.iwami.app.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.dao.WamiDao;
import com.iwami.iwami.app.model.Wami;
import com.iwami.iwami.app.service.WamiService;

public class WamiServiceImpl implements WamiService{
	
	private WamiDao wamiDao;

	@Override
	public Wami getLatestWami(long userid, long taskid) {
		return wamiDao.getLatestWami(userid, taskid);
	}

	@Override
	public Map<Long, Wami> getLatestWamis(long userid, List<Long> taskids) {
		return wamiDao.getLatestWami(userid, taskids);
	}

	@Override
	public void newWami(Wami wami) {
		wamiDao.newWami(wami);
	}

	@Override
	public Map<Long, Wami> getDoneTaskIds(long userid, Date start) {
		return wamiDao.getDoneTaskIds(userid, start);
	}

	@Override
	public Map<Long, Wami> getOngoingWami(long userid) {
		return wamiDao.getOngoingWami(userid);
	}

	@Override
	public List<Wami> getWamis(Date start, Date end) {
		return wamiDao.getWamis(start, end);
	}

	@Override
	public List<Wami> getWamis(Date start, Date end, String channel) {
		return wamiDao.getWamis(start, end, channel);
	}

	@Override
	public List<Wami> getWamisByIds(List<Long> ids, Date start, Date end) {
		return wamiDao.getWamisByIds(ids, start, end);
	}

	@Override
	public List<Wami> getWamisById(long id) {
		return wamiDao.getWamisById(id);
	}

	public WamiDao getWamiDao() {
		return wamiDao;
	}

	public void setWamiDao(WamiDao wamiDao) {
		this.wamiDao = wamiDao;
	}
}
