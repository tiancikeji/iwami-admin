package com.iwami.iwami.app.biz;

import java.util.Date;
import java.util.List;

import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TreasureConfig;

public interface TaskBiz {

	public TreasureConfig getTreasureConfig();

	public boolean modTreasureConfig(TreasureConfig config);
	
	public List<Task> getTasks(int type, int attr, int maxL, int maxR, int prizeL, int prizeR, int currL, int currR, int leftL, int leftR, Date startL, Date startR, Date endL, Date endR);

	public Task getTaskById(long taskid);

	public boolean delUnstartedTask(long taskid, long adminid);

	public boolean stopTask(long taskid, long adminid);

	public boolean modTask(Task task);

	public boolean addTask(Task task);

}
