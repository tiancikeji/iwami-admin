package com.iwami.iwami.app.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.dao.TaskDao;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TreasureConfig;
import com.iwami.iwami.app.service.TaskService;
import com.iwami.iwami.app.util.LocalCaches;

public class TaskServiceImpl implements TaskService {

	private TaskDao taskDao;
	
	private long expireTime;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Task> getAllAvailableTasks() {
		List<Task> tasks = (List<Task>)LocalCaches.get(IWamiConstants.CACHE_TASKS_KEY, System.currentTimeMillis(), expireTime);
		
		if(tasks == null){
			tasks = new ArrayList<Task>();
			List<Task> tmp = taskDao.getAllTasks();
			Date now = new Date();
			if(tmp != null && tmp.size() > 0)
				for(Task task : tmp)
					if(task != null && task.getStartTime() != null && task.getStartTime().before(now)
							&& (task.getEndTime() == null || (task.getEndTime() != null && task.getEndTime().after(now)))){
						if(task.getMaxPrize() >= 0 && task.getCurrentPrize() >= task.getMaxPrize())
							task.setAvailable(1);
						
						tasks.add(task);
					}
			
			LocalCaches.set(IWamiConstants.CACHE_TASKS_KEY, tasks, System.currentTimeMillis());
		}
		return tasks;
	}

	@Override
	public TreasureConfig getTreasureConfig() {
		TreasureConfig config = (TreasureConfig) LocalCaches.get(IWamiConstants.CACHE_TREASURE_CONFIG_KEY, System.currentTimeMillis(), expireTime);
		
		if(config == null){
			config = taskDao.getTreasureConfig();
			
			LocalCaches.set(IWamiConstants.CACHE_TREASURE_CONFIG_KEY, config, System.currentTimeMillis());
		}
		
		return config;
	}

	@Override
	public Task getTaskById(long taskid) {
		return taskDao.getTaskById(taskid);
	}

	@Override
	public List<Task> getTasksByIds(List<Long> taskIds) {
		return taskDao.getTasksByIds(taskIds);
	}

	@Override
	public void incrTaskCurrentPrize(long taskid) {
		taskDao.incrTaskCurrentPrize(taskid);
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
