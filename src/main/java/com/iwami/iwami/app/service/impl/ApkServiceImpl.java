package com.iwami.iwami.app.service.impl;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.dao.ApkDao;
import com.iwami.iwami.app.model.Apk;
import com.iwami.iwami.app.service.ApkService;
import com.iwami.iwami.app.util.LocalCaches;

public class ApkServiceImpl implements ApkService {

	private ApkDao apkDao;
	
	private long expireTime;
	
	@Override
	public Apk getApk() {
		Apk apk = (Apk)LocalCaches.get(IWamiConstants.CACHE_APK_KEY, System.currentTimeMillis(), expireTime);
		if(apk == null){
			apk = apkDao.getApk();
			LocalCaches.set(IWamiConstants.CACHE_APK_KEY, apk, System.currentTimeMillis());
		}
		
		return apk;
	}

	public ApkDao getApkDao() {
		return apkDao;
	}

	public void setApkDao(ApkDao apkDao) {
		this.apkDao = apkDao;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

}
