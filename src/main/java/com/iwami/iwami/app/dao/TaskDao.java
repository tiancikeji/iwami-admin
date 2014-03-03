package com.iwami.iwami.app.dao;

import java.util.Date;
import java.util.List;

import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TreasureConfig;

public interface TaskDao {

	public TreasureConfig getTreasureConfig();

	public void deleteAllTreasureConfig(long adminid);

	public boolean addTreasureConfig(TreasureConfig config); 

	public List<Task> getTasks(int type, int background, int register,
			int maxL, int maxR, int prizeL, int prizeR, int currL, int currR,
			int leftL, int leftR, Date startL, Date startR, Date endL, Date endR);
	
}
