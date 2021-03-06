package com.iwami.iwami.app.biz.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.iwami.iwami.app.biz.TaskBiz;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TreasureConfig;
import com.iwami.iwami.app.service.TaskService;
import com.iwami.iwami.app.service.WamiService;

public class TaskBizImpl implements TaskBiz {
	
	private TaskService taskService;
	
	private WamiService wamiService;
	
	@Override
	public TreasureConfig getTreasureConfig() {
		return taskService.getTreasureConfig();
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean modTreasureConfig(TreasureConfig config) {
		taskService.deleteAllTreasureConfig(config.getLastModUserid());
		return taskService.addTreasureConfig(config);
	}

	@Override
	public List<Task> getTasks(int type, int attr, int maxL, int maxR,
			int prizeL, int prizeR, int currL, int currR, int leftL, int leftR,
			Date startL, Date startR, Date endL, Date endR, int status) {
		int ttype = 0;
		if(type == 0){
			if(attr == 0)
				ttype = 31;
			else if(attr == 4)
				ttype = 4;
			else
				ttype = 27;
		} if(type == 1)
			ttype = 1;
		else if(type == 2)
			ttype = 2;
		else if(type == 3)
			ttype = 8;
		else if(type == 4)
			ttype = 4;
		else if(type == 5)
			ttype = 16;
		
		int background = -1;
		int register = -1;
		
		if(ttype != 4){
			if(attr == 1){
				background = 1;
				register = 0;
			} else if(attr == 2){
				background = 0;
				register = 0;
			} else if(attr == 3){
				background = 0;
				register = 1;
			} else if(attr == 4){
				background = 2;
				register = 2;
			}
		}
		
		List<Task> tasks = taskService.getTasks(ttype, background, register, maxL, maxR, prizeL, prizeR, currL, currR, leftL, leftR, startL, startR, endL, endR, status);
		
		if(tasks != null && tasks.size() > 0){
			Collections.sort(tasks, new Comparator<Task>() {

				@Override
				public int compare(Task t1, Task t2) {
					int result = t1.getType() - t2.getType();
					if(result == 0){
						if(t1.getEndTime() == null)
							return -1;
						if(t2.getEndTime() == null)
							return 1;
						return t1.getEndTime().after(t2.getEndTime()) ? -1 : 1;
					} else
						return result;
				}
			});
			
			Date now = new Date();
			for(Task task : tasks){
				int _status = 2;
				if(now.before(task.getStartTime()))
					_status = 1;
				else if(task.getCurrentPrize() >= task.getMaxPrize() 
						|| (task.getEndTime() != null && task.getEndTime().before(now)))
					_status = 3;
				
				task.setStatus(_status);
			}
				
		}
		
		return tasks;
	}

	@Override
	public int getTaskCount(int type, int attr, int maxL, int maxR,
			int prizeL, int prizeR, int currL, int currR, int leftL, int leftR,
			Date startL, Date startR, Date endL, Date endR, int status) {
		int ttype = 31;
		if(type == 1)
			ttype = 1;
		else if(type == 2)
			ttype = 2;
		else if(type == 3)
			ttype = 8;
		else if(type == 4)
			ttype = 4;
		else if(type == 5)
			ttype = 16;
		
		int background = -1;
		int register = -1;
		if(attr == 1)
			background = 1;
		else if(attr == 2)
			background = 0;
		else if(attr == 3){
			background = 1;
			register = 1;
		}
		
		return taskService.getTaskCount(ttype, background, register, maxL, maxR, prizeL, prizeR, currL, currR, leftL, leftR, startL, startR, endL, endR, status);
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean modTask(Task task) {
		if(task.getType() == Task.TYPE_GOLD && task.getRank() == Task.RANK_GOLD_DEFAULT)
			taskService.incrTaskRankByType(Task.TYPE_GOLD);
		return taskService.modTask(task);
	}

	@Override
	public Task getTaskById(long taskid) {
		return taskService.getTaskById(taskid);
	}

	@Override
	public boolean delUnstartedTask(long taskid, long adminid) {
		return taskService.delUnstartedTask(taskid, adminid);
	}

	@Override
	public boolean stopTask(long taskid, long adminid) {
		return taskService.stopTask(taskid, adminid);
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean addTask(Task task) {
		return taskService.addTask(task);
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public WamiService getWamiService() {
		return wamiService;
	}

	public void setWamiService(WamiService wamiService) {
		this.wamiService = wamiService;
	}
}
