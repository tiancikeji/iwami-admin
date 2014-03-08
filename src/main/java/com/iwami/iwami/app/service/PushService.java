package com.iwami.iwami.app.service;

import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.model.Push;
import com.iwami.iwami.app.model.PushTask;

public interface PushService {

	public boolean pushUserMsg(String alias, String msg);

	public List<Push> getUnFinishedPushTasks();

	public Map<Long, Map<Integer, Integer>> getAllCntsByIds(List<Long> ids);

	public boolean addPush(Push push);

	public void addPushTasks(List<PushTask> tasks);

	public boolean updatePush(int status, long id, long adminid);

}
