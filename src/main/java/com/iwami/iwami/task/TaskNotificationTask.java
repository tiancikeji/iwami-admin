package com.iwami.iwami.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TaskNotification;
import com.iwami.iwami.app.service.TaskService;
import com.iwami.iwami.app.util.IWamiUtils;
import com.iwami.iwami.app.util.SMSUtils;

public class TaskNotificationTask implements Runnable {

	private Log logger = LogFactory.getLog(getClass());
	
	private TaskService taskService;
	
	@Override
	public void run() {
		try{
			List<Long> cellPhones = splitCellPhones(taskService.getSMSNo());
			if(cellPhones != null && cellPhones.size() > 0){
				List<Task> tasks = taskService.getFinishedTasks();
				
				if(tasks != null && tasks.size() > 0){
					Date now = new Date();
					
					List<TaskNotification> notis = new ArrayList<TaskNotification>();
					for(Task task : tasks){
						TaskNotification noti = new TaskNotification();
						noti.setTaskId(task.getId());
						noti.setStatus(TaskNotification.STATUS_NEW);
						noti.setCellPhone(cellPhones.get(0));
						
						StringBuilder sms = new StringBuilder("任务");
						if(task.getEndTime() != null && now.after(task.getEndTime()))
							sms.append("\"").append(task.getName()).append("\"").append("已经到期（")
							.append(IWamiUtils.getDateString(task.getStartTime())).append("~").append(task.getEndTime()).append("），")
							.append("设定数量").append(task.getMaxPrize()).append("，实际完成").append(task.getCurrentPrize());
						else if(task.getMaxPrize() != -1 && task.getCurrentPrize() >= task.getMaxPrize())
							sms.append("\"").append(task.getName()).append("\"").append("已于")
							.append(IWamiUtils.getDateString(task.getLastModTime())).append("完成，原定时间为（")
							.append(IWamiUtils.getDateString(task.getStartTime())).append("~").append(task.getEndTime()).append("）");
						
						notis.add(noti);
					}
					
					if(cellPhones.size() > 1){
						List<TaskNotification> tmps = new ArrayList<TaskNotification>();
						for(long cellPhone : cellPhones)
							for(TaskNotification tn : notis){
								TaskNotification tmp = new TaskNotification();
								BeanUtils.copyProperties(tn, tmp);
								tn.setCellPhone(cellPhone);
								tmps.add(tn);
							}
						
						notis.addAll(tmps);
					}
					
					if(taskService.addTaskNotifications(notis)){
						for(TaskNotification noti : notis){
							try{
								if(SMSUtils.sendLuosiMao(noti.getSms(), "" + noti.getCellPhone()))
									taskService.updateTaskNotificationStatus(noti.getTaskId(), noti.getCellPhone(), TaskNotification.STATUS_SMS);
							} catch(Throwable t){
								if(logger.isErrorEnabled())
									logger.error("Error in sending sms >> ", t);
							}
						}
					}
				}
			}
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Error in task notification >> ", t);
		}
	}

	private List<Long> splitCellPhones(String smsNo) {
		String[] nos = StringUtils.split(smsNo, IWamiConstants.SEPARATOR_CELLPHONE);
		
		List<Long> cellPhones = new ArrayList<Long>();
		if(nos != null && nos.length > 0)
			for(String no : nos)
				if(IWamiUtils.validatePhone(no))
					cellPhones.add(NumberUtils.toLong(no));
		
		return cellPhones;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

}
