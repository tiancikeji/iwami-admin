package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.LoginBiz;
import com.iwami.iwami.app.biz.UserBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.model.UserRole;
import com.iwami.iwami.app.util.IWamiUtils;

@AjaxClass
public class LoginAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private LoginBiz loginBiz;
	
	private UserBiz userBiz;

	@AjaxMethod(path = "login.ajax")
	public Map<Object, Object> login(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("loginname") && params.containsKey("password")){
				String loginname = StringUtils.trimToEmpty(params.get("loginname"));
				String password = StringUtils.trimToEmpty(params.get("password"));
				if(StringUtils.isNotBlank(loginname) && StringUtils.isNotBlank(password)){
					UserRole role = loginBiz.login(loginname, password);
					User user = null;
					
					if(role != null)
						user = userBiz.getAdminById(role.getUserid());
					
					result.put("data", parseAdminInfo(user, role));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getApks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	private Map<String, Object> parseAdminInfo(User user, UserRole role) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		if(user != null && role != null){
			data.put("userid", user.getId());
			data.put("username", StringUtils.trimToEmpty(user.getName()));
			data.put("cellPhone", IWamiUtils.toStringL(user.getCellPhone()));
			
			data.put("loginname", StringUtils.trimToEmpty(role.getName()));
			data.put("roles", role.getRole());
			
			data.put("lastModTime", IWamiUtils.getDateString(user.getLastmodTime()));
			data.put("lastModUserid", user.getLastmodUserid());
		}
					
		
		return data;
	}

	public LoginBiz getLoginBiz() {
		return loginBiz;
	}

	public void setLoginBiz(LoginBiz loginBiz) {
		this.loginBiz = loginBiz;
	}

	public UserBiz getUserBiz() {
		return userBiz;
	}

	public void setUserBiz(UserBiz userBiz) {
		this.userBiz = userBiz;
	}

}
