package com.iwami.iwami.app.ajax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.LoginBiz;
import com.iwami.iwami.app.biz.PushBiz;
import com.iwami.iwami.app.biz.UserBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.exception.UserNotLoginException;
import com.iwami.iwami.app.model.Push;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.util.IWamiUtils;

@AjaxClass
public class PushAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private UserBiz userBiz;
	
	private PushBiz pushBiz;
	
	private LoginBiz loginBiz;
	
	private static BigDecimal bd = new BigDecimal(1000);

	@AjaxMethod(path = "PUSH/user.ajax")
	public Map<Object, Object> pushSingleUserMsg(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("userid") && params.containsKey("msg") ){
				long adminid = NumberUtils.toLong(params.get("adminid"));
				long userid = NumberUtils.toLong(params.get("userid"));
				String msg = StringUtils.trimToEmpty(params.get("msg"));
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PUSH_MANAGEMENT) 
						&& userid > 0 && StringUtils.isNotBlank(msg)){
					User user = userBiz.getUserById(userid);
					if(user != null && StringUtils.isNotBlank(user.getAlias())){
						if(pushBiz.pushUserMsg(user.getAlias(), msg))
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
						else
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
					} else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(UserNotLoginException e){
			throw e;
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getUserinfo", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "PUSH/all.ajax")
	public Map<Object, Object> pushAllUserMsg(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("msg") && params.containsKey("interval")){
				long adminid = NumberUtils.toLong(params.get("adminid"));
				String msg = StringUtils.trimToEmpty(params.get("msg"));
				double interval = NumberUtils.toDouble(params.get("interval"));
				if(adminid > 0 && interval >= 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PUSH_MANAGEMENT) && StringUtils.isNotBlank(msg)){
					if(pushBiz.pushAllMsgs(msg, new BigDecimal(interval).multiply(bd).longValue(), adminid))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK); 
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(UserNotLoginException e){
			throw e;
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getUserinfo", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "PUSH/white.ajax")
	public Map<Object, Object> pushWhiteUserMsg(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("msg") && params.containsKey("interval") && params.containsKey("file")){
				long adminid = NumberUtils.toLong(params.get("adminid"));
				String msg = StringUtils.trimToEmpty(params.get("msg"));
				String file = StringUtils.trimToEmpty(params.get("file"));
				double interval = NumberUtils.toDouble(params.get("interval"));
				if(adminid > 0 && interval >= 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PUSH_MANAGEMENT) && StringUtils.isNotBlank(msg) && StringUtils.isNotBlank(file)){
					if(pushBiz.pushWhiteMsgs(file, msg, new BigDecimal(interval).multiply(bd).longValue(), adminid))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(UserNotLoginException e){
			throw e;
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getUserinfo", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "PUSH/black.ajax")
	public Map<Object, Object> pushBlackUserMsg(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("msg") && params.containsKey("interval") && params.containsKey("file")){
				long adminid = NumberUtils.toLong(params.get("adminid"));
				String msg = StringUtils.trimToEmpty(params.get("msg"));
				String file = StringUtils.trimToEmpty(params.get("file"));
				double interval = NumberUtils.toDouble(params.get("interval"));
				if(adminid > 0 && interval >= 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PUSH_MANAGEMENT) && StringUtils.isNotBlank(msg) && StringUtils.isNotBlank(file)){
					if(pushBiz.pushBlackMsgs(file, msg, new BigDecimal(interval).multiply(bd).longValue(), adminid))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(UserNotLoginException e){
			throw e;
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getUserinfo", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "PUSH/list.ajax")
	public Map<Object, Object> pushList(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid")){
				long adminid = NumberUtils.toLong(params.get("adminid"));
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PUSH_MANAGEMENT)){
					List<Push> pushes = pushBiz.getUnFinishedPushTasks();
					result.put("data", parsePush(pushes));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(UserNotLoginException e){
			throw e;
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getUserinfo", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	private List<Map<String, Object>> parsePush(List<Push> pushes) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		if(pushes != null && pushes.size() > 0)
			for(Push push : pushes){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", push.getId());
				tmp.put("interval", new BigDecimal(push.getInterval()).divide(bd).doubleValue());
				tmp.put("msg", push.getMsg());
				tmp.put("status", push.getStatus());
				tmp.put("cellPhone", push.getCellPhone());
				tmp.put("allCnt", push.getAllCnt());
				tmp.put("succCnt", push.getSuccCnt());
				tmp.put("failCnt", push.getFailCnt());
				tmp.put("addTime", IWamiUtils.getDateString(push.getAddTime()));
				tmp.put("estimateTime", IWamiUtils.getDateString(push.getEstimateTime()));
				tmp.put("lastModTime", IWamiUtils.getDateString(push.getLastModTime()));
				tmp.put("lastModUserid", push.getLastModUserid());
				
				data.add(tmp);
			}
		
		return data;
	}

	@AjaxMethod(path = "PUSH/pause.ajax")
	public Map<Object, Object> pausePush(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				long adminid = NumberUtils.toLong(params.get("adminid"));
				long id = NumberUtils.toLong(params.get("id"));
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PUSH_MANAGEMENT) && id > 0){
					if(pushBiz.pauseTask(id, adminid))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(UserNotLoginException e){
			throw e;
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getUserinfo", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "PUSH/continue.ajax")
	public Map<Object, Object> continuePush(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				long adminid = NumberUtils.toLong(params.get("adminid"));
				long id = NumberUtils.toLong(params.get("id"));
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PUSH_MANAGEMENT) && id > 0){
					if(pushBiz.continueTask(id, adminid))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(UserNotLoginException e){
			throw e;
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getUserinfo", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "PUSH/stop.ajax")
	public Map<Object, Object> stopPush(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				long adminid = NumberUtils.toLong(params.get("adminid"));
				long id = NumberUtils.toLong(params.get("id"));
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PUSH_MANAGEMENT) && id > 0){
					if(pushBiz.stopTask(id, adminid))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(UserNotLoginException e){
			throw e;
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

	public PushBiz getPushBiz() {
		return pushBiz;
	}

	public void setPushBiz(PushBiz pushBiz) {
		this.pushBiz = pushBiz;
	}

	public LoginBiz getLoginBiz() {
		return loginBiz;
	}

	public void setLoginBiz(LoginBiz loginBiz) {
		this.loginBiz = loginBiz;
	}

}
