package com.iwami.iwami.app.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.iwami.iwami.app.dao.PushDao;
import com.iwami.iwami.app.model.Push;
import com.iwami.iwami.app.model.PushTask;
import com.iwami.iwami.app.sal.JPushSAL;
import com.iwami.iwami.app.service.PushService;

public class PushServiceImpl implements PushService {
	
	private JPushSAL pushSAL;
	
	private PushDao pushDao;

	// dao
	@Override
	public List<Push> getUnFinishedPushTasks() {
		return pushDao.getUnFinishedPushTasks();
	}

	@Override
	public List<Push> getTodoPushTasks() {
		return pushDao.getTodoPushTasks();
	}

	@Override
	public Map<Long, Map<Integer, Integer>> getAllCntsByIds(List<Long> ids) {
		return pushDao.getAllCntsByIds(ids);
	}

	@Override
	public boolean addPush(Push push) {
		return pushDao.addPush(push);
	}

	@Override
	public void addPushTasks(List<PushTask> tasks) {
		pushDao.addPushTasks(tasks);
	}

	@Override
	public boolean updatePush(int status, long id, long adminid) {
		return pushDao.updatePush(status, id, adminid);
	}

	@Override
	public boolean updatePush(int status, long id) {
		return pushDao.updatePush(status, id);
	}

	@Override
	public boolean updatePush(String cellPhone, int status, long id) {
		return pushDao.updatePush(cellPhone, status, id);
	}

	@Override
	public List<PushTask> getLimitedPushTaskById(long pushid, int limit) {
		return pushDao.getLimitedPushTaskById(pushid, limit);
	}

	@Override
	public boolean updatePushTask(int status, long id) {
		return pushDao.updatePushTask(status, id);
	}

	@Override
	public Push getPushById(long id) {
		return pushDao.getPushById(id);
	}

	@Override
	public int getAllCntsById(long id) {
		return pushDao.getAllCntsById(id);
	}

	// real jpush
	@Override
	public boolean pushUserMsg(String alias, String msg) {
		int status = pushSAL.sendCustomMessageWithAlias(genSendNo(), alias, msg, msg);
		
		return checkStatus(status);
	}
	
	private boolean checkStatus(int status) {
		return status == 0;
	}

	private int genSendNo(){
		return NumberUtils.toInt(new SimpleDateFormat("HHmmssS").format(new Date()));
	}

	public JPushSAL getPushSAL() {
		return pushSAL;
	}

	public void setPushSAL(JPushSAL pushSAL) {
		this.pushSAL = pushSAL;
	}

	public PushDao getPushDao() {
		return pushDao;
	}

	public void setPushDao(PushDao pushDao) {
		this.pushDao = pushDao;
	}

}
