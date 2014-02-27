package com.iwami.iwami.app.biz.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.iwami.iwami.app.biz.TaskBiz;
import com.iwami.iwami.app.comparator.TaskDoneComparator;
import com.iwami.iwami.app.comparator.TaskNewComparator;
import com.iwami.iwami.app.comparator.TaskOngoingComparator;
import com.iwami.iwami.app.comparator.TaskRankComparator;
import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TreasureConfig;
import com.iwami.iwami.app.model.Wami;
import com.iwami.iwami.app.service.TaskService;
import com.iwami.iwami.app.service.WamiService;
import com.iwami.iwami.app.util.IWamiUtils;

public class TaskBizImpl implements TaskBiz {
	
	private TaskService taskService;
	
	private WamiService wamiService;

	@Override
	public List<Task> getShareTasks() {
		List<Task> tasks = new ArrayList<Task>();
		
		List<Task> tmp = taskService.getAllAvailableTasks();
		if(tmp != null && tmp.size() > 0){
			List<Task> ttt = new ArrayList<Task>();
			for(Task task : tmp)
				if((task.getType() & 4) == 4){
					ttt.add(task);
				}
					
			Collections.sort(ttt, new TaskRankComparator());
			
			for(int i = 0; i < ttt.size(); i ++){
				Task _task = new Task();
				BeanUtils.copyProperties(ttt.get(i), _task);
				_task.setRank(i);
				tasks.add(_task);
			}
		}
		
		return tasks;
	}

	@Override
	public List<Task> getTreasureTasks(long userid) {
		List<Task> tasks = new ArrayList<Task>();
		
		List<Task> tmp = taskService.getAllAvailableTasks();
		if(tmp != null && tmp.size() > 0){
			List<Task> ttt = new ArrayList<Task>();
			for(Task task : tmp)
				if((task.getType() & 2) == 2){
					ttt.add(task);
				}
					
			Collections.sort(ttt, new TaskRankComparator());
			
			List<Long> ids = new ArrayList<Long>();
			for(int i = 0; i < ttt.size(); i ++){
				Task _task = new Task();
				BeanUtils.copyProperties(ttt.get(i), _task);
				_task.setRank(i);
				tasks.add(_task);
				ids.add(_task.getId());
			}
			
			if(ids != null && ids.size() > 0 && userid > 0){
				Map<Long, Wami> wamis = wamiService.getLatestWamis(userid, ids);
				
				if(wamis != null && wamis.size() > 0)
					for(Task _task : tasks)
						if(wamis.containsKey(_task.getId()))
							_task.setStatus(wamis.get(_task.getId()).getType());
						else
							_task.setStatus(Task.STATUS_READY);
			}
		}
		
		return tasks;
	}

	@Override
	public List<Task> getTopTasks(long userid) {
		List<Task> tasks = new ArrayList<Task>();
		
		List<Task> tmp = taskService.getAllAvailableTasks();
		if(tmp != null && tmp.size() > 0){
			List<Task> ttt = new ArrayList<Task>();
			for(Task task : tmp)
				if((task.getType() & 8) == 8){
					ttt.add(task);
				}
					
			Collections.sort(ttt, new TaskRankComparator());
			
			List<Long> ids = new ArrayList<Long>();
			for(int i = 0; i < ttt.size() && i < IWamiConstants.TOP_COUNT; i ++){
				Task _task = new Task();
				BeanUtils.copyProperties(ttt.get(i), _task);
				_task.setRank(i);
				_task.setStatus(Task.STATUS_READY);
				tasks.add(_task);
				ids.add(_task.getId());
			}
			
			if(ids != null && ids.size() > 0 && userid > 0){
				Map<Long, Wami> wamis = wamiService.getLatestWamis(userid, ids);
				
				if(wamis != null && wamis.size() > 0)
					for(Task _task : tasks)
						if(wamis.containsKey(_task.getId()))
							_task.setStatus(wamis.get(_task.getId()).getType());
			}
		}
		
		return tasks;
	}

	@Override
	public List<List<Task>> getWamiTasks(long userid) {
		List<List<Task>> tasks = new ArrayList<List<Task>>();
		
		// new
		List<Task> newtasks = getNewTasks();;
		List<Task> ongoingtasks = new ArrayList<Task>();
		List<Task> donetasks = new ArrayList<Task>();
		
		if(userid > 0){
			// new
			List<Task> tmpnew = newtasks;
			newtasks = new ArrayList<Task>();
			
			// ongoing
			List<Task> tmpongoing = getOngoingTasks(userid);
			
			// done
			donetasks = getDoneTasks(userid);

			// filter ongoing tasks
			Set<Long> filteredIds = new HashSet<Long>();
			
			if(donetasks != null && donetasks.size() > 0)
				for(Task task : donetasks)
					filteredIds.add(task.getId());
			
			if(tmpongoing != null && tmpongoing.size() > 0)
				for(Task task : tmpongoing)
					if(!filteredIds.contains(task.getId())){
						ongoingtasks.add(task);
						filteredIds.add(task.getId());
					}
			
			// filter new tasks
			if(tmpnew != null && tmpnew.size() > 0)
				for(Task task : tmpnew)
					if(!filteredIds.contains(task.getId()))
						newtasks.add(task);
		}
		
		// sort new
		Collections.sort(newtasks, new TaskNewComparator());
		// set rank
		for(int i = 0; i < newtasks.size(); i ++){
			newtasks.get(i).setStatus(Task.STATUS_READY);
			newtasks.get(i).setRank(i);
		}
		// add new tasks
		tasks.add(newtasks);
		
		// sort ongoing
		Collections.sort(ongoingtasks, new TaskOngoingComparator());
		// set rank
		for(int i = 0; i < ongoingtasks.size(); i ++){
			ongoingtasks.get(i).setRank(i);
		}
		// add ongoing tasks
		tasks.add(ongoingtasks);
		
		// sort done
		Collections.sort(donetasks, new TaskDoneComparator());
		// set rank
		for(int i = 0; i < donetasks.size(); i ++){
			donetasks.get(i).setRank(i);
		}
		// add done tasks
		tasks.add(donetasks);
		
		return tasks;
	}

	private List<Task> getDoneTasks(long userid) {
		Map<Long, Wami> taskIds = wamiService.getDoneTaskIds(userid, IWamiUtils.getTodayStart());
		if(taskIds != null && taskIds.size() > 0){
			List<Task> tasks = taskService.getTasksByIds(new ArrayList<Long>(taskIds.keySet()));
			if(tasks != null && tasks.size() > 0)
				for(Task task : tasks)
					if(taskIds.containsKey(task.getId())){
						task.setStatus(taskIds.get(task.getId()).getType());
						task.setLastModTime(taskIds.get(task.getId()).getLastmodTime());
					}
			return tasks;
		} else
			return null;
	}

	private List<Task> getOngoingTasks(long userid) {
		Map<Long, Wami> taskIds = wamiService.getOngoingWami(userid);
		if(taskIds != null && taskIds.size() > 0){
			List<Task> tasks = taskService.getTasksByIds(new ArrayList<Long>(taskIds.keySet()));
			if(tasks != null && tasks.size() > 0)
				for(Task task : tasks)
					if(taskIds.containsKey(task.getId())){
						task.setStatus(taskIds.get(task.getId()).getType());
						task.setLastModTime(taskIds.get(task.getId()).getLastmodTime());
					}
			return tasks;
		} else
			return null;
	}

	private List<Task> getNewTasks() {
		List<Task> newtasks = new ArrayList<Task>();
		List<Task> tmp = taskService.getAllAvailableTasks();
		if(tmp != null && tmp.size() > 0){
			for(Task task : tmp)
				if((task.getType() & 1) == 1){
					Task tmpTask = new Task();
					BeanUtils.copyProperties(task, tmpTask);
					newtasks.add(tmpTask);
				}
		}
		return newtasks;
	}

	@Override
	public TreasureConfig getTreasureConfig() {
		return taskService.getTreasureConfig();
	}

	@Override
	public Task getTaskById(long taskid) {
		return taskService.getTaskById(taskid);
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
