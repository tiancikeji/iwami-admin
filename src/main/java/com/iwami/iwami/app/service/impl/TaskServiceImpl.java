package com.iwami.iwami.app.service.impl;

import java.util.Date;
import java.util.List;

import com.iwami.iwami.app.dao.TaskDao;
import com.iwami.iwami.app.model.Task;
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
