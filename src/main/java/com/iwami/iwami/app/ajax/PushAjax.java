package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.UserBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;

@AjaxClass
public class PushAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private UserBiz userBiz;

	@AjaxMethod(path = "PUSH/user.ajax")
	public Map<Object, Object> pushSingleUserMsg(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("userid") && params.containsKey("msg") ){
				long adminid = NumberUtils.toLong(params.get("adminid"));
				long userid = NumberUtils.toLong(params.get("userid"));
				if(adminid > 0 && userid > 0 && userBiz.canOpt(adminid, userid)){
					// TODO push biz here...
					/*if(userBiz.modifyUser(user, adminid))*/
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
						/*else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);*/
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
			}
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getUserinfo", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	public UserBiz getUserBiz() {
		return userBiz;
	}

	public void setUserBiz(UserBiz userBiz) {
		this.userBiz = userBiz;
	}

}
