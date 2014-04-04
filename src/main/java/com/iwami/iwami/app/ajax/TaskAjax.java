package com.iwami.iwami.app.ajax;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.LoginBiz;
import com.iwami.iwami.app.biz.TaskBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.exception.UserNotLoginException;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TreasureConfig;
import com.iwami.iwami.app.util.IWamiUtils;

@AjaxClass
public class TaskAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private TaskBiz taskBiz;
	
	private LoginBiz loginBiz;
	
	@AjaxMethod(path = "GET/treasureconfig.ajax")
	public Map<Object, Object> getTreasureConfig(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.TASK_MANAGEMENT)){
					TreasureConfig config = taskBiz.getTreasureConfig();
					int days = -1;
					int count = -1;
					if(config != null){
						days = config.getDays();
						count = config.getCount();
					}
					
					Map<String, String> data = new HashMap<String, String>();
					data.put("days", IWamiUtils.toStringI(days));
					data.put("count", IWamiUtils.toStringI(count));
					
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					result.put("data", data);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getTreasureConfig", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}
	
	@AjaxMethod(path = "MOD/treasureconfig.ajax")
	public Map<Object, Object> modTreasureConfig(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("days") && params.containsKey("count")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.TASK_MANAGEMENT)){
					TreasureConfig config = new TreasureConfig();
					config.setCount(NumberUtils.toInt(params.get("count"), -1));
					config.setDays(NumberUtils.toInt(params.get("days"), -1));
					config.setLastModUserid(adminid);
					if(taskBiz.modTreasureConfig(config))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in modTreasureConfig", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "ADD/task.ajax")
	public Map<Object, Object> addTask(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("url") && params.containsKey("packageName")
					 && params.containsKey("name") && params.containsKey("rank") && params.containsKey("intr")
					 && params.containsKey("appintr") && params.containsKey("prize") && params.containsKey("type")
					 && params.containsKey("attr") && params.containsKey("time") && params.containsKey("startTime")
					 && params.containsKey("endTime") && params.containsKey("currentPrize") && params.containsKey("maxPrize")
					 && params.containsKey("iconSmall") && params.containsKey("iconBig") && params.containsKey("isdel")){
				
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.TASK_MANAGEMENT)){
					String name = StringUtils.trimToEmpty(params.get("name"));
					int rank = NumberUtils.toInt(params.get("rank"), -1);
					String intr = StringUtils.trimToEmpty(params.get("intr"));
					String appintr = StringUtils.trimToEmpty(params.get("appintr"));
					String packageName = StringUtils.trimToEmpty(params.get("packageName"));
					int prize = NumberUtils.toInt(params.get("prize"), -1);
					int type = NumberUtils.toInt(params.get("type"), -1);
					int attr = NumberUtils.toInt(params.get("attr"), -1);
					int time = NumberUtils.toInt(params.get("time"), -1);
					Date startTime = IWamiUtils.getDate(StringUtils.trimToEmpty(params.get("startTime")));
					Date endTime = IWamiUtils.getDate(StringUtils.trimToEmpty(params.get("endTime")));
					int currentPrize = NumberUtils.toInt(params.get("currentPrize"), -1);
					int maxPrize = NumberUtils.toInt(params.get("maxPrize"), -2);
					String iconSmall = StringUtils.trimToEmpty(params.get("iconSmall"));
					String iconBig = StringUtils.trimToEmpty(params.get("iconBig"));
					int isdel = NumberUtils.toInt(params.get("isdel"), -1);
					String url = StringUtils.trimToEmpty(params.get("url"));
					
					int defApp = NumberUtils.toInt(params.get("default"), -1);
					
					if(StringUtils.isNotBlank(name) && rank >= 0 && StringUtils.isNotBlank(intr) && StringUtils.isNotBlank(appintr) && StringUtils.isNotBlank(packageName)
						&& prize > 0 && maxPrize > -2 && currentPrize > -2
						&& (type == 1 || type == 2 || (type == 3 && StringUtils.isNotBlank(iconBig)) || type == 5)
						&& (attr == 1 || attr == 2 || attr == 3)
						&& time >= 0 && StringUtils.isNotBlank(iconSmall)
						&& (isdel == 0 || isdel == 1)
						&& StringUtils.isNotBlank(url)){

						Task task = new Task();
						task.setName(name);
						if(type == 3 && defApp == 1)
							task.setRank(-1);
						else
							task.setRank(rank);
						task.setIntr(intr);
						task.setAppIntr(appintr);
						task.setPackageName(packageName);
						task.setPrize(prize);
						
						task.setType(type);
						if(type == 3)
							task.setType(8);
						else if(type == 5)
							task.setType(16);
						
						int background = 1;
						int register = 0;
						if(attr == 2)
							background = 0;
						else if(attr == 3)
							register = 1;
						task.setBackground(background);
						task.setRegister(register);
						
						task.setTime(time);
						if(startTime == null)
							startTime = new Date();
						task.setStartTime(startTime);
						task.setEndTime(endTime);
						task.setCurrentPrize(currentPrize);
						task.setMaxPrize(maxPrize);
						task.setIconSmall(iconSmall);
						task.setIconBig(iconBig);
						task.setUrl(url);
						task.setLastModUserid(adminid);
						task.setIsdel(isdel);
					
						if(taskBiz.addTask(task))
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
						else
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
					} else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in delTasks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	@AjaxMethod(path = "MOD/task.ajax")
	public Map<Object, Object> modTask(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("taskid") && params.containsKey("url")
					 && params.containsKey("name") && params.containsKey("rank") && params.containsKey("intr") && params.containsKey("packageName") 
					 && params.containsKey("appintr") && params.containsKey("prize") && params.containsKey("type")
					 && params.containsKey("attr") && params.containsKey("time") && params.containsKey("startTime")
					 && params.containsKey("endTime") && params.containsKey("addCurrentPrize") && params.containsKey("maxPrize")
					 && params.containsKey("iconSmall") && params.containsKey("iconBig") && params.containsKey("isdel")){
				
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long taskid = NumberUtils.toLong(params.get("taskid"), -1);
				
				if(adminid > 0 && taskid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.TASK_MANAGEMENT)){
					Task task = taskBiz.getTaskById(taskid);

					int type = NumberUtils.toInt(params.get("type"), -1);
					if(task != null && (type > 0 && ((type == 4 && task.getType() == 4) || (type == 1 || type == 2 || type == 3 || type == 5)))){
						String name = StringUtils.trimToEmpty(params.get("name"));
						int rank = NumberUtils.toInt(params.get("rank"), -1);
						String intr = StringUtils.trimToEmpty(params.get("intr"));
						String appintr = StringUtils.trimToEmpty(params.get("appintr"));
						String packageName = StringUtils.trimToEmpty(params.get("packageName"));
						int prize = NumberUtils.toInt(params.get("prize"), -1);
						int attr = NumberUtils.toInt(params.get("attr"), -1);
						int time = NumberUtils.toInt(params.get("time"), -1);
						Date startTime = IWamiUtils.getDate(StringUtils.trimToEmpty(params.get("startTime")));
						Date endTime = IWamiUtils.getDate(StringUtils.trimToEmpty(params.get("endTime")));
						int addCurrentPrize = NumberUtils.toInt(params.get("addCurrentPrize"), Integer.MAX_VALUE);
						int maxPrize = NumberUtils.toInt(params.get("maxPrize"), -2);
						String iconSmall = StringUtils.trimToEmpty(params.get("iconSmall"));
						String iconBig = StringUtils.trimToEmpty(params.get("iconBig"));
						int isdel = NumberUtils.toInt(params.get("isdel"), -1);
						String url = StringUtils.trimToEmpty(params.get("url"));
						int defApp = NumberUtils.toInt(params.get("default"), -1);
						
						if(StringUtils.isNotBlank(name) && rank >= 0 && StringUtils.isNotBlank(intr) && StringUtils.isNotBlank(appintr)
							&& prize > 0 && maxPrize > -2 && addCurrentPrize != Integer.MAX_VALUE && StringUtils.isNotBlank(packageName)
							&& (type == 1 || type == 2 || (type == 3 && StringUtils.isNotBlank(iconBig)) || type == 4 || type == 5)
							&& (attr == 1 || attr == 2 || attr == 3 || attr == 4)
							&& time >= 0 && startTime != null
							&& StringUtils.isNotBlank(iconSmall)
							&& (isdel == 0 || isdel == 1)
							&& StringUtils.isNotBlank(url)){
							
							Task tmp = taskBiz.getTaskById(taskid);
							
							task.setName(name);
							if(type == 3 && defApp == 1)
								task.setRank(-1);
							else
								task.setRank(rank);
							task.setIntr(intr);
							task.setAppIntr(appintr);
							task.setPackageName(packageName);
							task.setPrize(prize);
							
							task.setType(type);
							if(type == 3)
								task.setType(8);
							else if(type == 5)
								task.setType(16);
							
							// share task
							if(tmp.getType() == 4)
								task.setType(4);
							
							int background = 1;
							int register = 0;
							if(attr == 2)
								background = 0;
							else if(attr == 3)
								register = 1;
							task.setBackground(background);
							task.setRegister(register);
							
							task.setTime(time);
							task.setStartTime(startTime);
							task.setEndTime(endTime);
							task.setCurrentPrize(addCurrentPrize);
							task.setMaxPrize(maxPrize);
							task.setIconSmall(iconSmall);
							task.setIconBig(iconBig);
							task.setUrl(url);
							task.setIsdel(isdel);
							task.setLastModUserid(adminid);
						
							if(taskBiz.modTask(task))
								result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
							else
								result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
						} else
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
					} else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in delTasks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "STOP/task.ajax")
	public Map<Object, Object> stopTask(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("taskid")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long taskid = NumberUtils.toLong(params.get("taskid"), -1);
				
				//direct check in db
				if(adminid > 0 && taskid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.TASK_MANAGEMENT)){
					if(taskBiz.stopTask(taskid, adminid))
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
				logger.error("Exception in stopTasks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	@AjaxMethod(path = "DEL/task.ajax")
	public Map<Object, Object> delTask(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("taskid")){
				
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long taskid = NumberUtils.toLong(params.get("taskid"), -1);
				
				//direct check in db
				if(adminid > 0 && taskid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.TASK_MANAGEMENT)){
					if(taskBiz.delUnstartedTask(taskid, adminid))
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
				logger.error("Exception in delTasks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	@AjaxMethod(path = "GET/task.ajax")
	public Map<Object, Object> getTasks(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid")
					&& params.containsKey("type") && params.containsKey("attr")
					/*&& params.containsKey("startL")&& params.containsKey("startR")
					&& params.containsKey("endL") && params.containsKey("endR")
					&& params.containsKey("maxL") && params.containsKey("maxR")
					&& params.containsKey("prizeL") && params.containsKey("prizeR")
					&& params.containsKey("currL") && params.containsKey("currR")
					&& params.containsKey("leftL") && params.containsKey("leftR")*/){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.TASK_MANAGEMENT)){
					int type = NumberUtils.toInt(params.get("type"), -1);
					int attr = NumberUtils.toInt(params.get("attr"), -1);
					Date startL = IWamiUtils.getDate(StringUtils.trimToEmpty(params.get("startL")));
					Date startR = IWamiUtils.getDate(StringUtils.trimToEmpty(params.get("startR")));
					Date endL = IWamiUtils.getDate(StringUtils.trimToEmpty(params.get("endL")));
					Date endR = IWamiUtils.getDate(StringUtils.trimToEmpty(params.get("endR")));
					int maxL = NumberUtils.toInt(params.get("maxL"), -1);
					int maxR = NumberUtils.toInt(params.get("maxR"), -1);
					int prizeL = NumberUtils.toInt(params.get("prizeL"), -1);
					int prizeR = NumberUtils.toInt(params.get("prizeR"), -1);
					int currL = NumberUtils.toInt(params.get("currL"), -1);
					int currR = NumberUtils.toInt(params.get("currR"), -1);
					int leftL = NumberUtils.toInt(params.get("leftL"), -1);
					int leftR = NumberUtils.toInt(params.get("leftR"), -1);
					int status = NumberUtils.toInt(params.get("status"), 0);
					int start = NumberUtils.toInt(params.get("start"), 0);
					int step = NumberUtils.toInt(params.get("step"), 20);
					
					if((type == 0 || type == 1 || type == 2 || type == 3 || type == 4 || type == 5) && start >= 0 && step > 0
							&& (attr == 0 || attr == 1 || attr == 2 || attr == 3 || attr == 4) 
							&& maxL >= -1 && maxR >= -1 && maxR >= maxL
							&& prizeL >= -1 && prizeR >= -1 && prizeR >=prizeL
							&& currL >= -1 && currR >= -1 && currR >= currL
							&& leftL >= -1 && leftR >= -1 && leftR >= leftL && (status == 0 || status == 1 || status == 2 || status == 3)){
						List<Task> tasks = taskBiz.getTasks(type, attr, maxL, maxR, prizeL, prizeR, currL, currR, leftL, leftR, startL, startR, endL, endR, status);
						
						List<Task> tmps = new ArrayList<Task>();
						if(tasks != null && tasks.size() > 0)
							for(int i = start; i < tasks.size() && i < (start + step); i ++)
								tmps.add(tasks.get(i));
						
						result.put("data", parseTasks(tmps));
						result.put("count", tasks == null ? 0 : tasks.size());
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					} else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getTasks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}
	
	private List<Map<String, Object>> parseTasks(List<Task> tasks) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		if(tasks != null && tasks.size() > 0)
			for(Task task : tasks)
				if(task != null){
					Map<String, Object> tmp = new HashMap<String, Object>();
					
					tmp.put("taskid", task.getId());
					tmp.put("name", StringUtils.trimToEmpty(task.getName()));
					tmp.put("rank", task.getRank());
					tmp.put("intr", StringUtils.trimToEmpty(task.getIntr()));
					tmp.put("appintr", StringUtils.trimToEmpty(task.getAppIntr()));
					tmp.put("packageName", StringUtils.trimToEmpty(task.getPackageName()));
					tmp.put("prize", task.getPrize());
					tmp.put("url", StringUtils.trimToEmpty(task.getUrl()));
					
					int type = 1;
					if((task.getType() & 2) > 0)
						type = 2;
					else if((task.getType() & 8) > 0)
						type = 3;
					else if((task.getType() & 4) > 0)
						type = 4;
					else if((task.getType() & 16) > 0)
						type = 5;
					tmp.put("type", type);
					
					int attr = 1;
					if(task.getBackground() == 0)
						attr = 2;
					if(task.getRegister() == 1)
						attr = 3;
					if(type == 4)
						attr = 4;
					tmp.put("attr", attr);
					
					tmp.put("time", task.getTime());
					tmp.put("startTime", IWamiUtils.getDateString(task.getStartTime()));
					tmp.put("endTime", IWamiUtils.getDateString(task.getEndTime()));
					tmp.put("currentPrize", task.getCurrentPrize());
					tmp.put("maxPrize", task.getMaxPrize());
					
					int leftPrize = task.getMaxPrize();
					if(leftPrize >= 0)
						leftPrize -= task.getCurrentPrize();
					tmp.put("leftPrize", leftPrize);
					
					tmp.put("iconSmall", StringUtils.trimToEmpty(task.getIconSmall()));
					tmp.put("iconBig", StringUtils.trimToEmpty(task.getIconBig()));
					tmp.put("lastModTime", IWamiUtils.getDateString(task.getLastModTime()));
					tmp.put("lastModUserid", task.getLastModUserid());
					tmp.put("isdel", task.getIsdel());

					tmp.put("status", task.getStatus());
					
					if(type == 3 && task.getRank() == -1)
						tmp.put("default", 1);
					else
						tmp.put("default", 0);
					
					data.add(tmp);
				}
		
		return data;
	}

	public TaskBiz getTaskBiz() {
		return taskBiz;
	}

	public void setTaskBiz(TaskBiz taskBiz) {
		this.taskBiz = taskBiz;
	}

	public LoginBiz getLoginBiz() {
		return loginBiz;
	}

	public void setLoginBiz(LoginBiz loginBiz) {
		this.loginBiz = loginBiz;
	}

}
