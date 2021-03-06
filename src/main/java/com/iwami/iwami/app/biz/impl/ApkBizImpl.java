package com.iwami.iwami.app.biz.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.iwami.iwami.app.biz.ApkBiz;
import com.iwami.iwami.app.model.Apk;
import com.iwami.iwami.app.service.ApkService;

public class ApkBizImpl implements ApkBiz {

	private ApkService apkService;

	@Override
	public Apk getApk() {
		return apkService.getApk();
	}

	@Override
	public List<Apk> getApks() {
		return apkService.getApks();
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean addApk(Apk apk) {
		apkService.delAllApks();
		if(apkService.addApk(apk)){
			return true;
		} else
			throw new RuntimeException("failed in addApk, so rollback.");
	}

	@Override
	public boolean modApk(Apk apk) {
		if(apk.getIsdel() == 0)
			apkService.delAllApks();
		return apkService.modApk(apk);
	}
	
	public ApkService getApkService() {
		return apkService;
	}

	public void setApkService(ApkService apkService) {
		this.apkService = apkService;
	}
}
