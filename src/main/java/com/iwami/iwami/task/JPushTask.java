package com.iwami.iwami.task;

import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.model.Push;
import com.iwami.iwami.app.model.PushTask;
import com.iwami.iwami.app.service.PushService;
import com.iwami.iwami.app.service.SMSService;

public class JPushTask implements Callable<Integer>{
	
	private PushService pushService;
	
	private SMSService smsService;
	
	private Push push;
	
	private List<Long> cellPhones;
	
	private int batchSize = 10;

	public JPushTask(PushService pushService, SMSService smsService, Push push, List<Long> cellPhones) {
		this.pushService = pushService;
		this.smsService = smsService;
		this.push = push;
		this.cellPhones = cellPhones;
	}

	@Override
	public Integer call() {
		int status = 0;
		
		if(push != null && pushService != null && smsService != null){
			List<PushTask> tasks = pushService.getLimitedPushTaskById(push.getId(), batchSize);
			
			if(tasks != null && tasks.size() > 0)
				for(PushTask task : tasks)
					try{
						int tmp = PushTask.STATUS_FAILED;
						if(pushService.pushUserMsg(task.getAlias(), push.getMsg()))
							tmp = PushTask.STATUS_SUCCESS;
						
						pushService.updatePushTask(tmp, task.getId());
						
						Thread.sleep(push.getInterval());
					} catch(Throwable t){
						t.printStackTrace();
					}
			
			if(tasks == null || tasks.size() < batchSize){
				// 1. update status
				pushService.updatePush(Push.STATUS_PUSHED, push.getId());
				Push tmp = pushService.getPushById(push.getId());
				int count = pushService.getAllCntsById(push.getId());
				// 2. send sms
				if(cellPhones != null && cellPhones.size() > 0)
					for(Long cellPhone : cellPhones)
						smsService.sendJPushSMS("" + cellPhone, tmp.getId(), tmp.getAddTime(), tmp.getLastModTime(), count);
				// 3. update status
				pushService.updatePush(StringUtils.join(cellPhones.toArray(), IWamiConstants.SEPARATOR_CELLPHONE), Push.STATUS_SENT, push.getId());
			}
		}
		
		return status;
	}

}
