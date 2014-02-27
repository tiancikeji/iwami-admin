package com.iwami.iwami.app.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.model.Wami;


public interface WamiDao {
	
	public void newWami(Wami wami);

	public Wami getLatestWami(long userid, long taskid);

	public Map<Long, Wami> getLatestWami(long userid, List<Long> taskids);

	public Map<Long, Wami> getDoneTaskIds(long userid, Date start);

	public Map<Long, Wami> getOngoingWami(long userid);
	
}
