package com.iwami.iwami.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TaskNotification;
import com.iwami.iwami.app.service.SMSService;
import com.iwami.iwami.app.service.TaskService;
import com.iwami.iwami.app.util.IWamiUtils;

public class TaskNotificationTask implements Runnable {

	private Log logger = LogFactory.getLog(getClass());
	
	private TaskService taskService;
	
	private SMSService smsService;
	
	private static Lock lock = new ReentrantLock();
	
	@Override
	public void run() {
		if(lock.tryLock()){
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

							noti.setSmsName(task.getName());
							noti.setSmsStartDate(task.getStartTime());
							noti.setSmsEndDate(task.getEndTime());
							noti.setSmsCount(task.getCurrentPrize());
							noti.setSmsTotal(task.getMaxPrize());
							
							if(task.getEndTime() != null && now.after(task.getEndTime()))
								noti.setSmsReason("到期");
							else if(task.getMaxPrize() != -1 && task.getCurrentPrize() >= task.getMaxPrize())
								noti.setSmsReason("完成");
							
							noti.setSms(noti.getSmsName() + "#" + noti.getSmsReason() + "#" + noti.getSmsStartDate() + "#" + noti.getSmsEndDate() + "#" + noti.getSmsTotal() + "#" + noti.getSmsCount());
							
							notis.add(noti);
						}
						
						if(cellPhones.size() > 1){
							List<TaskNotification> tmps = new ArrayList<TaskNotification>();
							for(long cellPhone : cellPhones)
								for(TaskNotification tn : notis){
									TaskNotification tmp = new TaskNotification();
									BeanUtils.copyProperties(tn, tmp);
									tmp.setCellPhone(cellPhone);
									tmps.add(tmp);
								}
							
							notis.clear();
							notis.addAll(tmps);
						}
						
						if(taskService.addTaskNotifications(notis)){
							for(TaskNotification noti : notis){
								try{
									if(smsService.sendTaskSMS("" + noti.getCellPhone(), noti.getSmsName(), noti.getSmsReason(), noti.getSmsStartDate(), noti.getSmsEndDate(), noti.getSmsTotal(), noti.getSmsCount()))
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
			} finally{
				lock.unlock();
			}
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

	public SMSService getSmsService() {
		return smsService;
	}

	public void setSmsService(SMSService smsService) {
		this.smsService = smsService;
	}
}