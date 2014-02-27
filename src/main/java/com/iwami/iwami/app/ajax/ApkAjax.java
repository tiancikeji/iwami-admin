package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.ApkBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.model.Apk;
import com.iwami.iwami.app.util.IWamiUtils;

@AjaxClass
public class ApkAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private ApkBiz apkBiz;

	@AjaxMethod(path = "download.ajax")
	public Map<Object, Object> download(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			String url = apkBiz.getApkURL();
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
			result.put("url", url);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in download", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	@AjaxMethod(path = "checkupdate.ajax")
	public Map<Object, Object> checkUpdate(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			Apk apk = apkBiz.getApk();
			if(apk != null){
				Map<String, String> data = new HashMap<String, String>();
				int update = IWamiUtils.calcNew(apk.getVersion(), params.get("version"));
				if(update > 0){
					data.put("version", apk.getVersion());
					data.put("update", "" + update);
					data.put("force", "" + apk.getForce());
					data.put("url", apk.getUrl());
					data.put("desc", apk.getDesc());
				}
				
				result.put("data", data);
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
			} else{
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
			}
				
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in download", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	public ApkBiz getApkBiz() {
		return apkBiz;
	}

	public void setApkBiz(ApkBiz apkBiz) {
		this.apkBiz = apkBiz;
	}

}
