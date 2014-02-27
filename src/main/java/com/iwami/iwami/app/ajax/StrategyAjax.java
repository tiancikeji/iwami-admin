package com.iwami.iwami.app.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.StrategyBiz;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.exception.DuplicateRateStrategyInfoException;
import com.iwami.iwami.app.model.Strategy;
import com.iwami.iwami.app.model.StrategyImage;
import com.iwami.iwami.app.model.StrategyInfo;
import com.iwami.iwami.app.model.StrategyRate;

public class StrategyAjax {
	
	private Log logger = LogFactory.getLog(getClass());

	private StrategyBiz strategyBiz;

	@AjaxMethod(path = "strategy/rate.ajax")
	public Map<Object, Object> rateStrategy(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try{
			if(params.containsKey("id") && params.containsKey("uuid")){
				long strategyId = NumberUtils.toLong(params.get("id"), -1);
				if(strategyId > 0){
					String uuid = StringUtils.trimToEmpty(params.get("uuid"));
					if(StringUtils.isNotBlank(uuid)){
						Strategy strategy = strategyBiz.getStrategyByStrategyId(strategyId);
						if(strategy != null){
							boolean success = strategyBiz.rateStrategy(strategyId, uuid);
							if(success)
								result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
							else
								result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
						} else{
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_RATE_STRATEGY_NOT_EXISTS);
							result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_RATE_STRATEGY_NOT_EXISTS));
						}
					} else{
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_RATE_STRATEGY_UUID);
						result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_RATE_STRATEGY_UUID));
					}
				} else{
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_RATE_STRATEGY_NOT_EXISTS);
					result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_RATE_STRATEGY_NOT_EXISTS));
				}
			} else{
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(DuplicateRateStrategyInfoException e){
			if(logger.isErrorEnabled())
				logger.error("Duplicate rate : ", e);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_RATE_STRATEGY_DUPLICATE);
			result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_RATE_STRATEGY_DUPLICATE));
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in rateStrategy", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	@AjaxMethod(path = "strategy/detail.ajax")
	public Map<Object, Object> getStrategyDetail(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try{
			if(params.containsKey("id") && params.containsKey("start") && params.containsKey("step")){
				long strategyId = NumberUtils.toLong(params.get("id"), -1);
				if(strategyId > 0){
					int start = NumberUtils.toInt(params.get("start"), -1);
					if(start >= 0){
						int step = NumberUtils.toInt(params.get("step"), -1);
						if(step > 0){
							Strategy strategy = strategyBiz.getStrategyByStrategyId(strategyId);
							if(strategy != null){
								List<StrategyInfo> infos = strategyBiz.getStrategyInfoByStrateByStrategyId(strategyId, start, step);
								StrategyRate rate = strategyBiz.getStrategyRateByStrategyId(strategyId);
								
								Map<String, Object> data = new HashMap<String, Object>();
								int skimc = 0;
								int ratec = 0;
								if(rate != null){
									skimc = rate.getSkim();
									ratec = rate.getRate();
								}
								data.put("skim", skimc);
								data.put("rate", ratec);
								data.put("strategy", parseStrategyInfo(infos));
								
								result.put("data", data);
								result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
							} else{
								result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_STRATEGY_NOT_EXISTS);
								result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_STRATEGY_NOT_EXISTS));
							}
						} else{
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_STRATEGY_STEP);
							result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_STRATEGY_STEP));
						}
					} else{
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_STRATEGY_START);
						result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_STRATEGY_START));
					}
				} else{
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_STRATEGY_NOT_EXISTS);
					result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_STRATEGY_NOT_EXISTS));
				}
			} else{
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getStrategyDetail", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	private List<Map<String, Object>> parseStrategyInfo(List<StrategyInfo> infos) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		if(infos != null && infos.size() > 0)
			for(StrategyInfo info : infos){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", info.getId());
				tmp.put("rank", info.getRank());
				tmp.put("title", info.getTitle());
				tmp.put("content", info.getContent());
				tmp.put("url", info.getUrl());
				
				data.add(tmp);
			}
		
		return data;
	}

	@AjaxMethod(path = "strategy/list.ajax")
	public Map<Object, Object> getStrategyList(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			List<Strategy> strategies = strategyBiz.getAllStrategies();
			List<StrategyImage> images = strategyBiz.getAllStragtegyImages();
			
			Map<String, List<Map<String, Object>>> data = new HashMap<String, List<Map<String, Object>>>();
			data.put("images", parseStrategyImages(images));
			data.put("list", parseStrategies(strategies));
			
			result.put("data", data);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getStrategyList", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	private List<Map<String, Object>> parseStrategies(List<Strategy> strategies) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		if(strategies != null && strategies.size() > 0)
			for(Strategy strategy : strategies){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", strategy.getId());
				tmp.put("name", strategy.getName());
				tmp.put("subname", strategy.getSubName());
				tmp.put("intr", strategy.getIntr());
				tmp.put("rank", strategy.getRank());
				tmp.put("iconSmall", strategy.getIconSmall());
				tmp.put("iconBig", strategy.getIconBig());
				
				list.add(tmp);
			}
		
		return list;
	}

	private List<Map<String, Object>> parseStrategyImages(List<StrategyImage> images) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		if(images != null && images.size() > 0)
			for(StrategyImage image : images){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", image.getId());
				tmp.put("rank", image.getRank());
				tmp.put("url", image.getIconUrl());
				
				data.add(tmp);
			}
		
		return data;
	}

	public StrategyBiz getStrategyBiz() {
		return strategyBiz;
	}

	public void setStrategyBiz(StrategyBiz strategyBiz) {
		this.strategyBiz = strategyBiz;
	}
	
}
