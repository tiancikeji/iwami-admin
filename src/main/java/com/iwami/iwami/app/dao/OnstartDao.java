package com.iwami.iwami.app.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.model.Onstart;

public interface OnstartDao {

	public boolean logOnstart(Onstart onstart);
	
	public Map<Long, Date> getLastLogins(List<Long> userids);

	public List<Onstart> getOnstartsByUser(List<Long> cellPhones,List<String> uuids, List<String> alias);
}
