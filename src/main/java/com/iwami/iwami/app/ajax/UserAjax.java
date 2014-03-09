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
import com.iwami.iwami.app.util.IWamiUtils;

@AjaxClass
public class UserAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private UserBiz userBiz;
	
	private LoginBiz loginBiz;

	@AjaxMethod(path = "GET/user.ajax")
	public Map<Object, Object> getUserinfo(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("key")){
				List<User> users = null;
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long key = NumberUtils.toLong(params.get("key"), -1);
				
				if(adminid > 0 && key > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.USER_MANAGEMENT)){
					users = userBiz.getUserByIdOCellPhone(key);

					result.put("data", parseUserInfo(users));
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

	private Object parseUserInfo(List<User> users) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		if(users != null)
			for(User user : users){
				Map<String, Object> tmp = new HashMap<String, Object>();
				tmp.put("userid", user.getId());
				tmp.put("username", StringUtils.trimToEmpty(user.getName()));
				tmp.put("uuid", StringUtils.trimToEmpty(user.getUuid()));
				tmp.put("alias", StringUtils.trimToEmpty(user.getAlias()));
				tmp.put("cellPhone", IWamiUtils.toStringL(user.getCellPhone()));
				tmp.put("age", user.getAge());
				tmp.put("gender", user.getGender());
				tmp.put("job", StringUtils.trimToEmpty(user.getJob()));
				tmp.put("address", StringUtils.trimToEmpty(user.getAddress()));
				tmp.put("addTime", IWamiUtils.getDateString(user.getAddTime()));
				tmp.put("lastModTime", IWamiUtils.getDateString(user.getLastmodTime()));
				tmp.put("lastModUserid", user.getLastmodUserid());
				tmp.put("isdel", user.getIsdel());
				
				if(user.getCreateTime() != null)
					tmp.put("createTime", IWamiUtils.getDateString(user.getCreateTime()));
				else
					tmp.put("createTime", IWamiUtils.getDateString(user.getAddTime()));
				tmp.put("lastLoginTime", IWamiUtils.getDateString(user.getLastLoginTime()));
				
				tmp.put("currentPrize", user.getCurrentPrize());
				tmp.put("exchangePrize", user.getExchangePrize());
				tmp.put("lastCellPhone1", IWamiUtils.toStringL(user.getLastCellPhone1()));
				tmp.put("lastAlipayAccount", StringUtils.trimToEmpty(user.getLastAlipayAccount()));
				tmp.put("lastBankAccount", StringUtils.trimToEmpty(user.getLastBankAccount()));
				tmp.put("lastBankNo", IWamiUtils.toStringL(user.getLastBankNo()));
				tmp.put("lastBankName", StringUtils.trimToEmpty(user.getLastBankName()));
				tmp.put("lastAddress", StringUtils.trimToEmpty(user.getLastAddres()));
				tmp.put("lastCellPhone2", IWamiUtils.toStringL(user.getLastCellPhone2()));
				tmp.put("lastName", StringUtils.trimToEmpty(user.getLastName()));
				
				data.add(tmp);
			}
		
		return data;
	}

	@AjaxMethod(path = "MOD/user.ajax")
	public Map<Object, Object> modUserinfo(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") 
					&& params.containsKey("userid") && params.containsKey("username") && params.containsKey("cellPhone") && params.containsKey("age") && params.containsKey("gender") && params.containsKey("job") && params.containsKey("address") && params.containsKey("isdel")
					 && params.containsKey("currentPrize") && params.containsKey("exchangePrize") && params.containsKey("lastCellPhone1") && params.containsKey("lastAlipayAccount") && params.containsKey("lastBankName") && params.containsKey("lastBankAccount") && params.containsKey("lastBankNo") && params.containsKey("lastAddress") && params.containsKey("lastCellPhone2") && params.containsKey("lastName")){
				long adminid = NumberUtils.toLong(params.get("adminid"));
				long userid = NumberUtils.toLong(params.get("userid"));
				if(adminid > 0 && userid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.USER_MANAGEMENT)){
					User user = new User();
					user.setId(userid);
					
					user.setName(StringUtils.trimToEmpty(params.get("username")));
					user.setCellPhone(NumberUtils.toLong(params.get("celllPhone"), -1));
					user.setAge(NumberUtils.toInt(params.get("age"), -1));
					user.setGender(NumberUtils.toInt(params.get("gender"), -1));
					user.setJob(StringUtils.trimToEmpty(params.get("job")));
					user.setAddress(StringUtils.trimToEmpty(params.get("address")));
					user.setIsdel(NumberUtils.toInt(params.get("isdel"), IWamiConstants.INACTIVE));
					if(user.getIsdel() == IWamiConstants.ADMIN)
						user.setIsdel(IWamiConstants.INACTIVE);
					
					user.setCurrentPrize(NumberUtils.toInt(params.get("currentPrize"), -1));
					user.setExchangePrize(NumberUtils.toInt(params.get("exchangePrize"), -1));
					user.setLastCellPhone1(NumberUtils.toLong(params.get("lastCellPhone1"), -1));
					user.setLastAlipayAccount(StringUtils.trimToEmpty(params.get("lastAlipayAccount")));
					user.setLastBankName(StringUtils.trimToEmpty(params.get("lastBankName")));
					user.setLastBankAccount(StringUtils.trimToEmpty(params.get("lastBankAccount")));
					user.setLastBankNo(NumberUtils.toLong("lastBankNo", -1));
					user.setLastAddres(StringUtils.trimToEmpty(params.get("lastAddress")));
					user.setLastCellPhone2(NumberUtils.toLong(params.get("lastCellPhone2"), -1));
					user.setLastName(StringUtils.trimToEmpty(params.get("lastName")));
					
					user.setLastmodUserid(adminid);
					
					if(userBiz.modifyUser(user))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
			}
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
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

	public LoginBiz getLoginBiz() {
		return loginBiz;
	}

	public void setLoginBiz(LoginBiz loginBiz) {
		this.loginBiz = loginBiz;
	}

}
