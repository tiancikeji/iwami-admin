package com.iwami.iwami.app.biz.impl;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.iwami.iwami.app.biz.WamiBiz;
import com.iwami.iwami.app.exception.TaskFinishedException;
import com.iwami.iwami.app.exception.TaskNotExistsException;
import com.iwami.iwami.app.exception.TaskRepeatStartException;
import com.iwami.iwami.app.exception.TaskUnavailableException;
import com.iwami.iwami.app.exception.TaskWamiedException;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.model.Wami;
import com.iwami.iwami.app.service.TaskService;
import com.iwami.iwami.app.service.UserService;
import com.iwami.iwami.app.service.WamiService;

public class WamiBizImpl  implements WamiBiz {
	
	private WamiService wamiService;
	
	private TaskService taskService;
	
	private UserService userService;

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public void share(User user, Task task, int type, long time, String channel) {
		Wami lastestWami = wamiService.getLatestWami(user.getId(), task.getId());
		
		Wami wami = new Wami();
		wami.setUserid(user.getId());
		wami.setTaskId(task.getId());
		wami.setType(type);
		wami.setPrize(task.getPrize());
		wami.setChannel(channel);
		wami.setAddTime(new Date(time));
		wami.setLastmodUserid(user.getId());
		wamiService.newWami(wami);
		
		if(lastestWami == null && type == Task.STATUS_FINISH){
			// finish task
			taskService.incrTaskCurrentPrize(task.getId());
			userService.addUserCurrentPrize(user.getId(), task.getPrize());
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public void wami(User user, Task task, int type, long time, String channel) throws TaskRepeatStartException, TaskNotExistsException, TaskFinishedException, TaskUnavailableException, TaskWamiedException {
		Wami lastestWami = wamiService.getLatestWami(user.getId(), task.getId());
		
		if(type == Task.STATUS_START){
			// start task, check
			if(lastestWami != null)
				throw new TaskRepeatStartException();
			
			Date now = new Date();
			if(task.getStartTime() != null && now.before(task.getStartTime()))
				throw new TaskNotExistsException();
			
			if(task.getEndTime() != null && now.after(task.getEndTime()))
				throw new TaskFinishedException();
			
			if(task.getCurrentPrize() >= task.getMaxPrize())
				throw new TaskUnavailableException();
		}
		
		if(lastestWami != null && lastestWami.getType() == Task.STATUS_FINISH)
			throw new TaskWamiedException();
		
		Wami wami = new Wami();
		wami.setUserid(user.getId());
		wami.setTaskId(task.getId());
		wami.setType(type);
		wami.setPrize(task.getPrize());
		wami.setChannel(channel);
		wami.setAddTime(new Date(time));
		wami.setLastmodUserid(user.getId());
		wamiService.newWami(wami);
		
		if(type == Task.STATUS_FINISH){
			// finish task
			taskService.incrTaskCurrentPrize(task.getId());
			userService.addUserCurrentPrize(user.getId(), task.getPrize());
		}
	}
	
	public WamiService getWamiService() {
		return wamiService;
	}

	public void setWamiService(WamiService wamiService) {
		this.wamiService = wamiService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
