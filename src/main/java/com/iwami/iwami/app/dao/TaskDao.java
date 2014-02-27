package com.iwami.iwami.app.dao;

import java.util.List;

import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TreasureConfig;

public interface TaskDao {

	public List<Task> getAllTasks();

	public TreasureConfig getTreasureConfig();

	public Task getTaskById(long taskid);

	public void incrTaskCurrentPrize(long taskid);

	public List<Task> getTasksByIds(List<Long> taskIds); 
	
}
