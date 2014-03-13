package com.iwami.iwami.app.dao;

import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.model.Push;
import com.iwami.iwami.app.model.PushTask;

public interface PushDao {

	public List<Push> getUnFinishedPushTasks();

	public List<Push> getTodoPushTasks();

	public Map<Long, Map<Integer, Integer>> getAllCntsByIds(List<Long> ids);

	public boolean addPush(Push push);

	public void addPushTasks(List<PushTask> tasks);

	public boolean updatePush(int status, long id, long adminid);

	public boolean updatePush(int status, long id);

	public boolean updatePush(String cellPhone, int status, long id);

	public boolean updatePushTask(int status, long id);

	public List<PushTask> getLimitedPushTaskById(long pushid, int limit);

	public Push getPushById(long id);

	public int getAllCntsById(long id);

}
