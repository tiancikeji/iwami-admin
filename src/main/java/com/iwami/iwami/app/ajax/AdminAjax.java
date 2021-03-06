package com.iwami.iwami.app.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.LoginBiz;
import com.iwami.iwami.app.biz.UserBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.exception.UserNotLoginException;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.model.UserRole;
import com.iwami.iwami.app.util.IWamiUtils;

@AjaxClass
public class AdminAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private UserBiz userBiz;
	
	private LoginBiz loginBiz;

	@AjaxMethod(path = "DEL/admin.ajax")
	public Map<Object, Object> delAdmin(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("userid")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long userid = NumberUtils.toLong(params.get("userid"), -1);
				// check admin id
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.ADMIN_MANAGEMENT) && userid > 0){
					
					if(userBiz.delAdmin(userid, adminid))
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
				logger.error("Exception in getUserinfo", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "password.ajax")
	public Map<Object, Object> password(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("oldpassword") && params.containsKey("password1") && params.containsKey("password2")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				String oldpassword = StringUtils.trimToEmpty(params.get("oldpassword"));
				String password1 = StringUtils.trimToEmpty(params.get("password1"));
				String password2 = StringUtils.trimToEmpty(params.get("password2"));
				if(adminid > 0 && loginBiz.checkLogin(adminid) 
						&& StringUtils.isNotBlank(oldpassword)
						&& StringUtils.isNotBlank(password1) && StringUtils.isNotBlank(password2) && StringUtils.equals(password1, password2)){
					List<Long> ids = new ArrayList<Long>();
					ids.add(adminid);
					
					Map<Long, UserRole> roles = userBiz.getUserRoles(ids);
					if(roles != null && roles.containsKey(adminid)){
						UserRole role = roles.get(adminid);
						if(role != null && StringUtils.equals(role.getPassword(), oldpassword)){
							role.setPassword(password1);
							if(userBiz.modRole(role))
								result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
							else
								result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
						} else
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
					}
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
				logger.error("Exception in getUserinfo", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "MOD/admin.ajax")
	public Map<Object, Object> modAdmin(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("userid") && params.containsKey("username") && params.containsKey("loginname") && params.containsKey("cellPhone")
					 && params.containsKey("password") && params.containsKey("roles") && params.containsKey("isdel")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long userid = NumberUtils.toLong(params.get("userid"), -1);
				String username = StringUtils.trimToEmpty(params.get("username"));
				String loginname = StringUtils.trimToEmpty(params.get("loginname"));
				long cellPhone = NumberUtils.toLong(params.get("cellPhone"), -1);
				String password = StringUtils.trimToEmpty(params.get("password"));
				long roles = NumberUtils.toLong(params.get("roles"), -1);
				int isdel = NumberUtils.toInt(params.get("isdel"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.ADMIN_MANAGEMENT) && userid > 0 && IWamiUtils.validatePhone("" + cellPhone) && StringUtils.isNotBlank(username)
						&& /*StringUtils.isNotBlank(password) &&*/ roles >= 0 && isdel >= 0){
					
					User user = new User();
					user.setId(userid);
					user.setName(username);
					user.setCellPhone(cellPhone);
					user.setLastmodUserid(adminid);
					if(isdel == 0)
						user.setIsdel(3);
					else
						user.setIsdel(4);
					
					UserRole role = new UserRole();
					role.setUserid(userid);
					role.setName(loginname);
					role.setPassword(password);
					if(adminid == userid)
						roles = 268435455;
					role.setRole(roles);
					role.setLastModUserid(adminid);
					role.setIsdel(isdel);
					
					if(userBiz.modAdmin(user, role))
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
				logger.error("Exception in getUserinfo", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "ADD/admin.ajax")
	public Map<Object, Object> addAdmin(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("username") && params.containsKey("loginname") && params.containsKey("cellPhone")
					 && params.containsKey("password") && params.containsKey("roles")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				String username = StringUtils.trimToEmpty(params.get("username"));
				String loginname = StringUtils.trimToEmpty(params.get("loginname"));
				long cellPhone = NumberUtils.toLong(params.get("cellPhone"), -1);
				String password = StringUtils.trimToEmpty(params.get("password"));
				long roles = NumberUtils.toLong(params.get("roles"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.ADMIN_MANAGEMENT) && IWamiUtils.validatePhone("" + cellPhone) && StringUtils.isNotBlank(username)
						&& StringUtils.isNotBlank(password) && roles >= 0){
					
					User user = new User();
					user.setName(username);
					user.setCellPhone(cellPhone);
					user.setLastmodUserid(adminid);
					
					UserRole role = new UserRole();
					role.setName(loginname);
					role.setPassword(password);
					role.setRole(roles);
					role.setLastModUserid(adminid);
					
					if(userBiz.addAdmin(user, role))
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
				logger.error("Exception in getUserinfo", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/admin.ajax")
	public Map<Object, Object> getAdmin(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.ADMIN_MANAGEMENT)){
					List<User> users = userBiz.getAdminUsers(StringUtils.trimToEmpty(params.get("key")));
					
					Map<Long, UserRole> roles = null;
					if(users != null && users.size() > 0){
						List<Long> ids = new ArrayList<Long>();
						for(User user : users)
							ids.add(user.getId());
							
						roles = userBiz.getUserRoles(ids);
					}
					result.put("data", parseAdminInfo(users, roles));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getUserinfo", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	private Object parseAdminInfo(List<User> users, Map<Long, UserRole> roles) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		if(users != null && roles != null)
			for(User user : users)
				if(user != null && roles.containsKey(user.getId())){
					Map<String, Object> tmp = new HashMap<String, Object>();
					tmp.put("userid", user.getId());
					tmp.put("username", StringUtils.trimToEmpty(user.getName()));
					tmp.put("cellPhone", IWamiUtils.toStringL(user.getCellPhone()));
					
					UserRole role = roles.get(user.getId());
					tmp.put("loginname", StringUtils.trimToEmpty(role.getName()));
					tmp.put("password", StringUtils.EMPTY);
					tmp.put("roles", role.getRole());
					
					tmp.put("lastModTime", IWamiUtils.getDateString(user.getLastmodTime()));
					tmp.put("lastModUserid", user.getLastmodUserid());
					
					int isdel = 1;
					if(user.getIsdel() == 3)
						isdel = 0;
					tmp.put("isdel", isdel);
					
					data.add(tmp);
				}
		
		return data;
	}


	public UserBiz getUserBiz() {
		return userBiz;
	}

	public void setUserBiz(UserBiz userBiz) {
		this.userBiz = userBiz;
	}

	public LoginBiz getLoginBiz() {
		return loginBiz;
	}

	public void setLoginBiz(LoginBiz loginBiz) {
		this.loginBiz = loginBiz;
	}

}
