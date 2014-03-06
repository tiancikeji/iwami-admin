package com.iwami.iwami.app.service.impl;

import java.util.List;

import com.iwami.iwami.app.dao.ApkDao;
import com.iwami.iwami.app.model.Apk;
import com.iwami.iwami.app.service.ApkService;

public class ApkServiceImpl implements ApkService {

	private ApkDao apkDao;

	@Override
	public List<Apk> getApks() {
		return apkDao.getApks();
	}

	@Override
	public boolean delAllApks() {
		return apkDao.delAllApks();
	}

	@Override
	public boolean addApk(Apk apk) {
		return apkDao.addApk(apk);
	}

	@Override
	public boolean modApk(Apk apk) {
		return apkDao.modApk(apk);
	}

	public ApkDao getApkDao() {
		return apkDao;
	}

	public void setApkDao(ApkDao apkDao) {
		this.apkDao = apkDao;
	}

}
