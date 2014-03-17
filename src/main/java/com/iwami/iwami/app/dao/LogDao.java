package com.iwami.iwami.app.dao;

import java.util.Date;
import java.util.List;

import com.iwami.iwami.app.model.Log;

public interface LogDao {

	public List<Log> getLogs(Date start, Date end);

	public List<Log> getLogsByType(int type, Date start, Date end);

}
