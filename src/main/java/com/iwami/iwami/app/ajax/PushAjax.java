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

import com.iwami.iwami.app.biz.UserBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.model.Push;
import com.iwami.iwami.app.util.IWamiUtils;

@AjaxClass
public class PushAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private UserBiz userBiz;

	@AjaxMethod(path = "PUSH/user.ajax")
	public Map<Object, Object> pushSingleUserMsg(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("userid") && params.containsKey("msg") ){
				// TODO check admin id
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

	@AjaxMethod(path = "PUSH/all.ajax")
	public Map<Object, Object> pushAllUserMsg(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("msg") && params.containsKey("interval")){
				// TODO check admin id
				long adminid = NumberUtils.toLong(params.get("adminid"));
				String msg = StringUtils.trimToEmpty(params.get("msg"));
				if(adminid > 0 && StringUtils.isNotBlank(msg)){
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

	@AjaxMethod(path = "PUSH/white.ajax")
	public Map<Object, Object> pushwhiteUserMsg(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("msg") && params.containsKey("interval") && params.containsKey("file")){
				// TODO check admin id
				long adminid = NumberUtils.toLong(params.get("adminid"));
				String msg = StringUtils.trimToEmpty(params.get("msg"));
				String file = StringUtils.trimToEmpty(params.get("file"));
				if(adminid > 0 && StringUtils.isNotBlank(msg) && StringUtils.isNotBlank(file)){
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

	@AjaxMethod(path = "PUSH/black.ajax")
	public Map<Object, Object> pushBlackUserMsg(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("msg") && params.containsKey("interval") && params.containsKey("file")){
				// TODO check admin id
				long adminid = NumberUtils.toLong(params.get("adminid"));
				String msg = StringUtils.trimToEmpty(params.get("msg"));
				String file = StringUtils.trimToEmpty(params.get("file"));
				if(adminid > 0 && StringUtils.isNotBlank(msg) && StringUtils.isNotBlank(file)){
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

	@AjaxMethod(path = "PUSH/list.ajax")
	public Map<Object, Object> pushList(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid")){
				// TODO check admin id
				long adminid = NumberUtils.toLong(params.get("adminid"));
				if(adminid > 0){
					List<Push> pushes = null;
					result.put("data", parsePush(pushes));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
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

	private List<Map<String, Object>> parsePush(List<Push> pushes) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		BigDecimal bd = new BigDecimal(1000);
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
				// TODO check admin id
				long adminid = NumberUtils.toLong(params.get("adminid"));
				long id = NumberUtils.toLong(params.get("id"));
				if(adminid > 0 && id > 0){
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
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

	@AjaxMethod(path = "PUSH/continue.ajax")
	public Map<Object, Object> continuePush(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				// TODO check admin id
				long adminid = NumberUtils.toLong(params.get("adminid"));
				long id = NumberUtils.toLong(params.get("id"));
				if(adminid > 0 && id > 0){
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
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

	@AjaxMethod(path = "PUSH/stop.ajax")
	public Map<Object, Object> stopPush(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				// TODO check admin id
				long adminid = NumberUtils.toLong(params.get("adminid"));
				long id = NumberUtils.toLong(params.get("id"));
				if(adminid > 0 && id > 0){
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
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
