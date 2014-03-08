package com.iwami.iwami.app.service;

import java.util.Date;
import java.util.List;

import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TaskNotification;
import com.iwami.iwami.app.model.TreasureConfig;

public interface TaskService {

	public TreasureConfig getTreasureConfig();

	public void deleteAllTreasureConfig(long adminid);

	public boolean addTreasureConfig(TreasureConfig config);
	
	public List<Task> getTasks(int type, int background, int register,
			int maxL, int maxR, int prizeL, int prizeR, int currL, int currR,
			int leftL, int leftR, Date startL, Date startR, Date endL, Date endR);

	public Task getTaskById(long taskid);

	public boolean delUnstartedTask(long taskid, long adminid);

	public boolean stopTask(long taskid, long adminid);

	public boolean modTask(Task task);

	public boolean updateTaskUrl(Task task);

	public boolean addTask(Task task);

	public List<Task> getFinishedTasks();

	public String getSMSNo();

	public boolean addTaskNotifications(List<TaskNotification> notis);

	public void updateTaskNotificationStatus(long taskid, long cellPhone, int status);

	public void incrTaskRankByType(int type);

}
