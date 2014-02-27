package com.iwami.iwami.app.biz.impl;

import org.apache.commons.lang.StringUtils;

import com.iwami.iwami.app.biz.ApkBiz;
import com.iwami.iwami.app.model.Apk;
import com.iwami.iwami.app.service.ApkService;

public class ApkBizImpl implements ApkBiz {

	private ApkService apkService;
	
	private String defaultURL;
	
	@Override
	public String getApkURL() {
		Apk apk = apkService.getApk();
		
		if(apk != null && StringUtils.isNotBlank(apk.getUrl()))
			return apk.getUrl();
		else
			return defaultURL;
	}

	@Override
	public Apk getApk() {
		Apk apk = apkService.getApk();
		
		if(apk != null && StringUtils.isNotBlank(apk.getUrl()))
			return apk;
		else
			return null;
	}

	public ApkService getApkService() {
		return apkService;
	}

	public void setApkService(ApkService apkService) {
		this.apkService = apkService;
	}

	public String getDefaultURL() {
		return defaultURL;
	}

	public void setDefaultURL(String defaultURL) {
		this.defaultURL = defaultURL;
	}

}
