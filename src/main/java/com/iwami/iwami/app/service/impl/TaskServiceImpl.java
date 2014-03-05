package com.iwami.iwami.app.service.impl;

import java.util.Date;
import java.util.List;

import com.iwami.iwami.app.dao.TaskDao;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TaskNotification;
import com.iwami.iwami.app.model.TreasureConfig;
import com.iwami.iwami.app.service.TaskService;

public class TaskServiceImpl implements TaskService {

	private TaskDao taskDao;
	
	private long expireTime;

	@Override
	public TreasureConfig getTreasureConfig() {
		return taskDao.getTreasureConfig();
	}

	@Override
	public void deleteAllTreasureConfig(long adminid) {
		taskDao.deleteAllTreasureConfig(adminid);
	}

	@Override
	public boolean addTreasureConfig(TreasureConfig config) {
		return taskDao.addTreasureConfig(config);
	}

	@Override
	public List<Task> getTasks(int type, int background, int register,
			int maxL, int maxR, int prizeL, int prizeR, int currL, int currR,
			int leftL, int leftR, Date startL, Date startR, Date endL, Date endR) {
		return taskDao.getTasks(type, background, register, maxL, maxR, prizeL, prizeR, currL, currR, leftL, leftR, startL, startR, endL, endR);
	}

	@Override
	public Task getTaskById(long taskid) {
		return taskDao.getTaskById(taskid);
	}

	@Override
	public boolean delUnstartedTask(long taskid, long adminid) {
		return taskDao.delUnstartedTask(taskid, adminid);
	}

	@Override
	public boolean stopTask(long taskid, long adminid) {
		return taskDao.stopTask(taskid, adminid);
	}

	@Override
	public boolean modTask(Task task) {
		return taskDao.modTask(task);
	}

	@Override
	public boolean addTask(Task task) {
		return taskDao.addTask(task);
	}

	@Override
	public List<Task> getFinishedTasks() {
		return taskDao.getFinishedTasks();
	}

	@Override
	public String getSMSNo() {
		return taskDao.getSMSNo();
	}

	@Override
	public boolean addTaskNotifications(List<TaskNotification> notis) {
		return taskDao.addTaskNotifications(notis);
	}

	@Override
	public void updateTaskNotificationStatus(long taskid, long cellPhone, int status) {
		taskDao.updateTaskNotificationStatus(taskid, cellPhone, status);
	}

	@Override
	public void incrTaskRankByType(int type) {
		taskDao.incrTaskRankByType(type);
	}

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
}
