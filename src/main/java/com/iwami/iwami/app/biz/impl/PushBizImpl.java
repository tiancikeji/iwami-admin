package com.iwami.iwami.app.biz.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.transaction.annotation.Transactional;

import com.iwami.iwami.app.biz.PushBiz;
import com.iwami.iwami.app.model.Push;
import com.iwami.iwami.app.model.PushTask;
import com.iwami.iwami.app.service.PushService;
import com.iwami.iwami.app.service.UserService;

public class PushBizImpl implements PushBiz {
	
	private PushService pushService;
	
	private UserService userService;
	
	private int defaultTime = 1000;

	@Override
	public boolean pushUserMsg(String alias, String msg) {
		return pushService.pushUserMsg(alias, msg);
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean pushAllMsgs(String msg, long interval, long adminid) {
		Push push = addNewPush(msg, interval, adminid);
		
		List<PushTask> tasks = new ArrayList<PushTask>();
		Map<Long, String> alias = getAllAlias();
		if(alias != null && alias.size() > 0){
			for(Long userid : alias.keySet()){
				PushTask task = new PushTask();
				
				task.setPushId(push.getId());
				task.setUserid(userid);
				task.setAlias(alias.get(userid));
				task.setStatus(PushTask.STATUS_NEW);
				task.setLastModUserid(adminid);
			}
			
			pushService.addPushTasks(tasks);
		}
		
		return true;
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean pushWhiteMsgs(String file, String msg, long interval, long adminid) {
		Push push = addNewPush(msg, interval, adminid);
		
		List<Long> whiteList = readFile(file);
		
		List<PushTask> tasks = new ArrayList<PushTask>();
		Map<Long, String> alias = getAllAlias();
		if(alias != null && alias.size() > 0){
			for(Long userid : alias.keySet())
				if(whiteList.contains(userid)){
					PushTask task = new PushTask();
					
					task.setPushId(push.getId());
					task.setUserid(userid);
					task.setAlias(alias.get(userid));
					task.setStatus(PushTask.STATUS_NEW);
					task.setLastModUserid(adminid);
				}
			
			pushService.addPushTasks(tasks);
		}
		
		return true;
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean pushBlackMsgs(String file, String msg, long interval, long adminid) {
		Push push = addNewPush(msg, interval, adminid);
		
		List<Long> blackList = readFile(file);
		
		List<PushTask> tasks = new ArrayList<PushTask>();
		Map<Long, String> alias = getAllAlias();
		if(alias != null && alias.size() > 0){
			for(Long userid : alias.keySet())
				if(!blackList.contains(userid)){
					PushTask task = new PushTask();
					
					task.setPushId(push.getId());
					task.setUserid(userid);
					task.setAlias(alias.get(userid));
					task.setStatus(PushTask.STATUS_NEW);
					task.setLastModUserid(adminid);
				}
			
			pushService.addPushTasks(tasks);
		}
		
		return true;
	}
	
	public List<Long> readFile(String url){
		List<Long> data = new ArrayList<Long>();
		try{
			URL _url = new URL(url);
			Scanner sc = new Scanner(_url.openStream());
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				if(StringUtils.isNotBlank(line) && NumberUtils.isNumber(line))
					data.add(NumberUtils.toLong(line, -1));
			}
		} catch(Throwable t){
			t.printStackTrace();
			throw new RuntimeException(t);
		}
		return data;
	}

	private Map<Long, String> getAllAlias() {
		return userService.getAllAlias();
	}

	private Push addNewPush(String msg, long interval, long adminid) {
		Push push = new Push();
		push.setInterval(interval);
		push.setMsg(msg);
		push.setStatus(Push.STATUS_NEW);
		push.setEstimateTime(DateUtils.addMilliseconds(new Date(), defaultTime));
		push.setLastModUserid(adminid);
		
		pushService.addPush(push);
		
		return push;
	}

	@Override
	public List<Push> getUnFinishedPushTasks() {
		List<Push> pushes = pushService.getUnFinishedPushTasks();
		
		if(pushes != null && pushes.size() > 0){
			List<Long> ids = new ArrayList<Long>();
			for(Push push : pushes)
				ids.add(push.getId());
			
			Map<Long, Map<Integer, Integer>> cnts = pushService.getAllCntsByIds(ids);
			
			if(cnts != null && cnts.size() > 0)
				for(Push push : pushes)
					if(cnts.containsKey(push.getId())){
						Map<Integer, Integer> tmp = cnts.get(push.getId());
						
						int succ = 0;
						int fail = 0;
						int fre = 0;
						
						if(tmp.containsKey(PushTask.STATUS_SUCCESS))
							succ = tmp.get(PushTask.STATUS_SUCCESS);
						if(tmp.containsKey(PushTask.STATUS_FAILED))
							fail = tmp.get(PushTask.STATUS_FAILED);
						if(tmp.containsKey(PushTask.STATUS_NEW))
							fre = tmp.get(PushTask.STATUS_NEW);
						
						push.setSuccCnt(succ);
						push.setFailCnt(fail);
						push.setAllCnt(fre + succ + fail);
					}
		}
		
		return pushes;
	}

	@Override
	public boolean pauseTask(long id, long adminid) {
		return pushService.updatePush(Push.STATUS_PAUSE, id, adminid);
	}

	@Override
	public boolean continueTask(long id, long adminid) {
		return pushService.updatePush(Push.STATUS_RESUME, id, adminid);
	}

	@Override
	public boolean stopTask(long id, long adminid) {
		return pushService.updatePush(Push.STATUS_STOP, id, adminid);
	}

	public PushService getPushService() {
		return pushService;
	}

	public void setPushService(PushService pushService) {
		this.pushService = pushService;
	}

	public int getDefaultTime() {
		return defaultTime;
	}

	public void setDefaultTime(int defaultTime) {
		this.defaultTime = defaultTime;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
