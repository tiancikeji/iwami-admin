package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.FileBiz;
import com.iwami.iwami.app.biz.LoginBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.exception.UserNotLoginException;

@AjaxClass
public class FileAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private FileBiz fileBiz;
	
	private LoginBiz loginBiz;

	@AjaxMethod(path = "ADD/file.ajax")
	public Map<Object, Object> addFile(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("data") && params.containsKey("name") && params.containsKey("adminid")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				
				String data = StringUtils.trimToEmpty(params.get("data"));
				String name = StringUtils.trimToEmpty(params.get("name"));
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.FILE_MANAGEMENT) && StringUtils.isNotBlank(data) && StringUtils.isNotBlank(name)){
					String url = fileBiz.addFile(data, name);
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					result.put("url", url);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in download", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	public FileBiz getFileBiz() {
		return fileBiz;
	}

	public void setFileBiz(FileBiz fileBiz) {
		this.fileBiz = fileBiz;
	}

	public LoginBiz getLoginBiz() {
		return loginBiz;
	}

	public void setLoginBiz(LoginBiz loginBiz) {
		this.loginBiz = loginBiz;
	}

}
