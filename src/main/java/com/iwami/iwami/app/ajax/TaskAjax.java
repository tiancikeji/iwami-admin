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

import com.iwami.iwami.app.biz.TaskBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TreasureConfig;
import com.iwami.iwami.app.util.IWamiUtils;

@AjaxClass
public class TaskAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private TaskBiz taskBiz;
	
	@AjaxMethod(path = "GET/treasureconfig.ajax")
	public Map<Object, Object> getTreasureConfig(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid")){
				// TODO check admin
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
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in top.ajax", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}
	
	@AjaxMethod(path = "MOD/treasureconfig.ajax")
	public Map<Object, Object> modTreasureConfig(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("days") && params.containsKey("count")){
				// TODO check admin
				long adminid = NumberUtils.toLong(params.get("admin"), -1);
				if(adminid > 0){
					TreasureConfig config = new TreasureConfig();
					config.setCount(NumberUtils.toInt("count", -1));
					config.setDays(NumberUtils.toInt("days", -1));
					config.setLastModUserid(adminid);
					if(taskBiz.modTreasureConfig(config))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in top.ajax", t);
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
					&& params.containsKey("startL")&& params.containsKey("startR")
					&& params.containsKey("endL") && params.containsKey("endR")
					&& params.containsKey("maxL") && params.containsKey("maxR")
					&& params.containsKey("prizeL") && params.containsKey("prizeR")
					&& params.containsKey("currL") && params.containsKey("currR")
					&& params.containsKey("leftL") && params.containsKey("leftR")){
				//TODO check admin user
				
				int type = NumberUtils.toInt(params.get("type"), -1);
				int attr = NumberUtils.toInt(params.get("attr"), -1);
				Date startL = IWamiUtils.getDate(StringUtils.trimToEmpty(params.get("startL")));
				Date startR = IWamiUtils.getDate(StringUtils.trimToEmpty(params.get("startR")));
				Date endL = IWamiUtils.getDate(StringUtils.trimToEmpty(params.get("endL")));
				Date endR = IWamiUtils.getDate(StringUtils.trimToEmpty(params.get("endR")));
				int maxL = NumberUtils.toInt(params.get("maxL"), -2);
				int maxR = NumberUtils.toInt(params.get("maxR"), -2);
				int prizeL = NumberUtils.toInt(params.get("prizeL"), -2);
				int prizeR = NumberUtils.toInt(params.get("prizeR"), -2);
				int currL = NumberUtils.toInt(params.get("currL"), -2);
				int currR = NumberUtils.toInt(params.get("currR"), -2);
				int leftL = NumberUtils.toInt(params.get("leftL"), -2);
				int leftR = NumberUtils.toInt(params.get("leftR"), -2);
				
				if((type == 0 || type == 1 || type == 2 || type == 3 || type == 4 || type == 5)
						&& (attr == 0 || attr == 1 || attr == 2 || attr == 3) 
						&& maxL >= -1 && maxR >= -1 && maxR >= maxL
						&& prizeL >= -1 && prizeR >= -1 && prizeR >=prizeL
						&& currL >= -1 && currR >= -1 && currR >= currL
						&& leftL >= -1 && leftR >= -1 && leftR >= leftL){
					List<Task> tasks = taskBiz.getTasks(type, attr, maxL, maxR, prizeL, prizeR, currL, currR, leftL, leftR, startL, startR, endL, endR);
					result.put("data", parseTasks(tasks));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in top.ajax", t);
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
					tmp.put("name", task.getName());
					tmp.put("rank", task.getRank());
					tmp.put("intr", task.getIntr());
					tmp.put("appintr", task.getAppIntr());
					tmp.put("prize", task.getPrize());
					
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
					tmp.put("icomSmall", task.getIconSmall());
					tmp.put("iconBig", task.getIconBig());
					tmp.put("lastModTime", IWamiUtils.getDateString(task.getLastModTime()));
					tmp.put("lastModUserid", task.getLastModUserid());
					tmp.put("isdel", task.getIsdel());

					tmp.put("status", task.getStatus());
					
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

}
