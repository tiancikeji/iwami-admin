package com.iwami.iwami.app.service.impl;

import java.util.Date;
import java.util.List;

import com.iwami.iwami.app.dao.LogDao;
import com.iwami.iwami.app.model.Log;
import com.iwami.iwami.app.service.LogService;

public class LogServiceImpl implements LogService {
	
	private LogDao logDao;

	@Override
	public List<Log> getLogs(Date start, Date end){
		return logDao.getLogs(start, end);
	}

	@Override
	public List<Log> getLogsByType(int type, Date start, Date end) {
		return logDao.getLogsByType(type, start, end);
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

}
