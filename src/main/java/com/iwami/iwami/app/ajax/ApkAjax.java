package com.iwami.iwami.app.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.ApkBiz;
import com.iwami.iwami.app.biz.LoginBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.exception.UserNotLoginException;
import com.iwami.iwami.app.model.Apk;
import com.iwami.iwami.app.util.IWamiUtils;

@AjaxClass
public class ApkAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private ApkBiz apkBiz;
	
	private LoginBiz loginBiz;

	@AjaxMethod(path = "MOD/apk.ajax")
	public Map<Object, Object> modApk(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("version") && params.containsKey("url") && params.containsKey("isdel")
					&& params.containsKey("force") && params.containsKey("desc") && params.containsKey("id")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				String version = StringUtils.trimToEmpty(params.get("version"));
				String url = StringUtils.trimToEmpty(params.get("url"));
				int force = NumberUtils.toInt(params.get("force"), -1);
				String desc = StringUtils.trimToEmpty(params.get("desc"));
				int isdel = NumberUtils.toInt(params.get("isdel"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.APK_MANAGEMENT) && id > 0 && StringUtils.isNotBlank(version) && StringUtils.isNotBlank(url)
						&& (force == 0 || force == 1) && (isdel == 0 || isdel == 1)){
					Apk apk = new Apk();
					apk.setId(id);
					apk.setVersion(version);
					apk.setUrl(url);
					apk.setDesc(desc);
					apk.setForce(force);
					apk.setLastmodUserid(adminid);
					apk.setIsdel(isdel);
					
					if(apkBiz.modApk(apk))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getApks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "ADD/apk.ajax")
	public Map<Object, Object> addApk(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("version") && params.containsKey("url")
					&& params.containsKey("force") && params.containsKey("desc")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				String version = StringUtils.trimToEmpty(params.get("version"));
				String url = StringUtils.trimToEmpty(params.get("url"));
				String desc = StringUtils.trimToEmpty(params.get("desc"));
				int force = NumberUtils.toInt(params.get("force"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.APK_MANAGEMENT) && StringUtils.isNotBlank(version) && StringUtils.isNotBlank(url)
						&& (force == 0 || force == 1)){
					Apk apk = new Apk();
					apk.setVersion(version);
					apk.setUrl(url);
					apk.setDesc(desc);
					apk.setForce(force);
					apk.setLastmodUserid(adminid);
					
					if(apkBiz.addApk(apk))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getApks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/apk.ajax")
	public Map<Object, Object> getApks(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.APK_MANAGEMENT)){
					List<Apk> apks = apkBiz.getApks();
					result.put("data", parseApk(apks));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getApks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	private List<Map<String, Object>> parseApk(List<Apk> apks) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		if(apks != null && apks.size() > 0)
			for(Apk apk : apks){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", apk.getId());
				tmp.put("version", apk.getVersion());
				tmp.put("url", apk.getUrl());
				tmp.put("force", apk.getForce());
				tmp.put("desc", apk.getDesc());
				tmp.put("isdel", apk.getIsdel());

				tmp.put("lastModTime", IWamiUtils.getDateString(apk.getLastmodTime()));
				tmp.put("lastModUserid", apk.getLastmodUserid());
				
				data.add(tmp);
			}
		
		return data;
	}

	public ApkBiz getApkBiz() {
		return apkBiz;
	}

	public void setApkBiz(ApkBiz apkBiz) {
		this.apkBiz = apkBiz;
	}

	public LoginBiz getLoginBiz() {
		return loginBiz;
	}

	public void setLoginBiz(LoginBiz loginBiz) {
		this.loginBiz = loginBiz;
	}

}
