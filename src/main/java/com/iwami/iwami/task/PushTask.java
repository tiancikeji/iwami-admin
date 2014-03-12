package com.iwami.iwami.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.model.Push;
import com.iwami.iwami.app.service.PushService;
import com.iwami.iwami.app.service.SMSService;
import com.iwami.iwami.app.service.TaskService;
import com.iwami.iwami.app.util.IWamiUtils;

public class PushTask implements Runnable {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private ExecutorService executorService;
	
	private PushService pushService;
	
	private SMSService smsService;
	
	private TaskService taskService;
	
	private int startHour;
	
	private int endHour;

	@Override
	public void run() {
		Date now = new Date();
		long hour = DateUtils.getFragmentInHours(now, Calendar.DAY_OF_YEAR);
		
		if(hour >= startHour && hour < endHour){
			List<Push> pushes = pushService.getTodoPushTasks();
			if(pushes != null && pushes.size() > 0){
				List<Long> cellPhones = splitCellPhones(taskService.getSMSNo());
				List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
				for(Push push : pushes)
					futures.add(executorService.submit(new JPushTask(pushService, smsService, push, cellPhones)));
				
				for(Future<Integer> future : futures)
					try {
						future.get();
					} catch (Throwable t) {
						t.printStackTrace();
					}
			}
		} else{
			long tmp = 0;
			if(hour < startHour)
				tmp = DateUtils.truncate(DateUtils.setHours(now, startHour), Calendar.HOUR_OF_DAY).getTime() - now.getTime();
			else if(hour >= endHour)
				tmp = DateUtils.truncate(DateUtils.setHours(DateUtils.addDays(now, 1), startHour), Calendar.HOUR_OF_DAY).getTime() - now.getTime();
			
			try {
				logger.info("slepp................ " + tmp);
				logger.info("get up.....................");
				Thread.sleep(tmp);
			} catch (Throwable t) {
				t.printStackTrace();
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
	
	public static void main(String[] args) {
		System.out.println(DateUtils.getFragmentInHours(new Date(), Calendar.DAY_OF_YEAR));
		System.out.println(DateUtils.truncate(DateUtils.setHours(DateUtils.addDays(new Date(), 1), 10), Calendar.HOUR_OF_DAY));
		System.out.println(DateUtils.truncate(DateUtils.setHours(new Date(), 10), Calendar.HOUR_OF_DAY));
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public PushService getPushService() {
		return pushService;
	}

	public void setPushService(PushService pushService) {
		this.pushService = pushService;
	}

	public SMSService getSmsService() {
		return smsService;
	}

	public void setSmsService(SMSService smsService) {
		this.smsService = smsService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

}
