package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.TaskBiz;
import com.iwami.iwami.app.biz.UserBiz;
import com.iwami.iwami.app.biz.WamiBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.exception.TaskFinishedException;
import com.iwami.iwami.app.exception.TaskNotExistsException;
import com.iwami.iwami.app.exception.TaskRepeatStartException;
import com.iwami.iwami.app.exception.TaskUnavailableException;
import com.iwami.iwami.app.exception.TaskWamiedException;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.User;

@AjaxClass
public class WamiAjax {

	private Log logger = LogFactory.getLog(getClass());

	private WamiBiz wamiBiz;
	
	private UserBiz userBiz;
	
	private TaskBiz taskBiz;

	@AjaxMethod(path = "sharetask.ajax")
	public Map<Object, Object> shareUpload(Map<String,String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			if(params.containsKey("userid") && params.containsKey("taskid") && params.containsKey("time")){
				long userid = NumberUtils.toLong(params.get("userid"), -1);
				if(userid > 0){
					long taskid = NumberUtils.toLong(params.get("taskid"), -1);
					if(taskid > 0){
						int type = Task.STATUS_FINISH;
						if(type >= 0){
							long time = NumberUtils.toLong(params.get("time"), -1);
							if(time > 0){
								User user = userBiz.getUserById(userid);
								if(user != null && user.getId() == userid){
									Task task = taskBiz.getTaskById(taskid);
									if(task != null && task.getId() == taskid){
										wamiBiz.share(user, task, type, time, StringUtils.trimToEmpty(params.get("channel")));
										result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
									} else {
										result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_WAMI_TASKID);
										result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_WAMI_TASKID));
									}
								} else {
									result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_WAMI_USERID);
									result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_WAMI_USERID));
								}
							} else{
								result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_WAMI_TIME);
								result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_WAMI_TIME));
							}
						} else{
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_WAMI_TYPE);
							result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_WAMI_TYPE));
						}
					} else {
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_WAMI_TASKID);
						result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_WAMI_TASKID));
					}
				} else {
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_WAMI_USERID);
					result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_WAMI_USERID));
				}
			} else
				result.put(ErrorCodeConstants.STATUS_KEY,ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch (Throwable t) {
			if (logger.isErrorEnabled()) 
				logger.error("Exception in wami", t);
			result.put(ErrorCodeConstants.STATUS_KEY,ErrorCodeConstants.STATUS_ERROR);
		}
		return result;
	}

	@AjaxMethod(path = "wami.ajax")
	public Map<Object, Object> statusUpload(Map<String,String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			if(params.containsKey("userid") && params.containsKey("taskid") && params.containsKey("type") && params.containsKey("time")){
				long userid = NumberUtils.toLong(params.get("userid"), -1);
				if(userid > 0){
					long taskid = NumberUtils.toLong(params.get("taskid"), -1);
					if(taskid > 0){
						int type = NumberUtils.toInt(params.get("type"), -1);
						if(type >= 0){
							long time = NumberUtils.toLong(params.get("time"), -1);
							if(time > 0){
								User user = userBiz.getUserById(userid);
								if(user != null && user.getId() == userid){
									Task task = taskBiz.getTaskById(taskid);
									if(task != null && task.getId() == taskid){
										wamiBiz.wami(user, task, type, time, StringUtils.trimToEmpty(params.get("channel")));
										result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
									} else {
										result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_WAMI_TASKID);
										result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_WAMI_TASKID));
									}
								} else {
									result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_WAMI_USERID);
									result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_WAMI_USERID));
								}
							} else{
								result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_WAMI_TIME);
								result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_WAMI_TIME));
							}
						} else{
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_WAMI_TYPE);
							result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_WAMI_TYPE));
						}
					} else {
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_WAMI_TASKID);
						result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_WAMI_TASKID));
					}
				} else {
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_WAMI_USERID);
					result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_WAMI_USERID));
				}
			} else
				result.put(ErrorCodeConstants.STATUS_KEY,ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch (TaskRepeatStartException t) {
			if (logger.isErrorEnabled()) 
				logger.error("Exception in wami", t);
			result.put(ErrorCodeConstants.STATUS_KEY,ErrorCodeConstants.STATUS_WAMI_REPEAT_START);
			result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_WAMI_REPEAT_START));
		} catch (TaskNotExistsException t) {
			if (logger.isErrorEnabled()) 
				logger.error("Exception in wami", t);
			result.put(ErrorCodeConstants.STATUS_KEY,ErrorCodeConstants.STATUS_TASK_NOT_EXISTS);
			result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_TASK_NOT_EXISTS));
		} catch (TaskFinishedException t) {
			if (logger.isErrorEnabled()) 
				logger.error("Exception in wami", t);
			result.put(ErrorCodeConstants.STATUS_KEY,ErrorCodeConstants.STATUS_TASK_FINISHED);
			result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_TASK_FINISHED));
		} catch (TaskUnavailableException t) {
			if (logger.isErrorEnabled()) 
				logger.error("Exception in wami", t);
			result.put(ErrorCodeConstants.STATUS_KEY,ErrorCodeConstants.STATUS_TASK_UNAVAILABLE);
			result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_TASK_UNAVAILABLE));
		} catch (TaskWamiedException t) {
			if (logger.isErrorEnabled()) 
				logger.error("Exception in wami", t);
			result.put(ErrorCodeConstants.STATUS_KEY,ErrorCodeConstants.STATUS_TASK_WAMIED);
			result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_TASK_WAMIED));
		} catch (Throwable t) {
			if (logger.isErrorEnabled()) 
				logger.error("Exception in wami", t);
			result.put(ErrorCodeConstants.STATUS_KEY,ErrorCodeConstants.STATUS_ERROR);
		}
		return result;
	}

	public WamiBiz getWamiBiz() {
		return wamiBiz;
	}

	public void setWamiBiz(WamiBiz wamiBiz) {
		this.wamiBiz = wamiBiz;
	}

	public UserBiz getUserBiz() {
		return userBiz;
	}

	public void setUserBiz(UserBiz userBiz) {
		this.userBiz = userBiz;
	}

	public TaskBiz getTaskBiz() {
		return taskBiz;
	}

	public void setTaskBiz(TaskBiz taskBiz) {
		this.taskBiz = taskBiz;
	}
}
