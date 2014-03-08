package com.iwami.iwami.app.biz.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.iwami.iwami.app.biz.FileBiz;
import com.iwami.iwami.app.biz.TaskBiz;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TreasureConfig;
import com.iwami.iwami.app.service.TaskService;
import com.iwami.iwami.app.service.WamiService;

public class TaskBizImpl implements TaskBiz {
	
	private TaskService taskService;
	
	private WamiService wamiService;
	
	private FileBiz fileBiz;
	
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
			Date startL, Date startR, Date endL, Date endR) {
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
		
		List<Task> tasks = taskService.getTasks(ttype, background, register, maxL, maxR, prizeL, prizeR, currL, currR, leftL, leftR, startL, startR, endL, endR);
		
		if(tasks != null && tasks.size() > 0){
			Collections.sort(tasks, new Comparator<Task>() {

				@Override
				public int compare(Task t1, Task t2) {
					return (int)(t1.getId() - t2.getId());
				}
			});
			
			Date now = new Date();
			for(Task task : tasks){
				int status = 2;
				if(now.before(task.getStartTime()))
					status = 1;
				else if(task.getCurrentPrize() >= task.getMaxPrize() 
						|| (task.getEndTime() != null && task.getEndTime().before(now)))
					status = 3;
				
				task.setStatus(status);
			}
				
		}
		
		return tasks;
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean modTask(Task task) {
		fileBiz.uploadTaskResource(task);
		
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
		if(taskService.addTask(task)){
			fileBiz.uploadTaskResource(task);
			return taskService.updateTaskUrl(task);
		} else
			return false;
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

	public FileBiz getFileBiz() {
		return fileBiz;
	}

	public void setFileBiz(FileBiz fileBiz) {
		this.fileBiz = fileBiz;
	}
}
