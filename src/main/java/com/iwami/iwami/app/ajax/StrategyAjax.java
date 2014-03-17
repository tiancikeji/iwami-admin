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
import com.iwami.iwami.app.biz.StrategyBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.exception.UserNotLoginException;
import com.iwami.iwami.app.model.Strategy;
import com.iwami.iwami.app.model.StrategyImage;
import com.iwami.iwami.app.model.StrategyInfo;
import com.iwami.iwami.app.model.StrategyRate;
import com.iwami.iwami.app.util.IWamiUtils;

@AjaxClass
public class StrategyAjax {
	
	private Log logger = LogFactory.getLog(getClass());

	private StrategyBiz strategyBiz;
	
	private LoginBiz loginBiz;

	@AjaxMethod(path = "DEL/infos.ajax")
	public Map<Object, Object> delInfos(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT) && id > 0){
					if(strategyBiz.delInfos(id, adminid))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else{
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_STRATEGY_NOT_EXISTS);
					result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_STRATEGY_NOT_EXISTS));
				}
			} else{
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getStrategyDetail", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "DEL/info.ajax")
	public Map<Object, Object> delInfo(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT) && id > 0){
					if(strategyBiz.delInfo(id, adminid))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else{
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_STRATEGY_NOT_EXISTS);
					result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_STRATEGY_NOT_EXISTS));
				}
			} else{
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getStrategyDetail", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "MOD/info.ajax")
	public Map<Object, Object> modInfo(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try{
			if(params.containsKey("strategyid") && params.containsKey("id")
					 && params.containsKey("rank") && params.containsKey("title")
					 && params.containsKey("content") && params.containsKey("url")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				int rank = NumberUtils.toInt(params.get("rank"));
				String title = StringUtils.trimToEmpty(params.get("title"));
				String content = StringUtils.trimToEmpty(params.get("content"));
				String url = StringUtils.trimToEmpty(params.get("url"));
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT) && id > 0 && rank >= 0 && StringUtils.isNotBlank(title)
						&& StringUtils.isNotBlank(url) && StringUtils.isNotBlank(content)){
					StrategyInfo info = new StrategyInfo();
					info.setId(id);
					info.setRank(rank);
					info.setTitle(title);
					info.setContent(content);
					info.setLastModUserid(adminid);
					info.setUrl(url);
					
					if(strategyBiz.modInfo(info))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else{
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_STRATEGY_NOT_EXISTS);
					result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_STRATEGY_NOT_EXISTS));
				}
			} else{
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getStrategyDetail", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "ADD/info.ajax")
	public Map<Object, Object> addInfo(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try{
			if(params.containsKey("strategyid") && params.containsKey("adminid")
					 && params.containsKey("rank") && params.containsKey("title")
					 && params.containsKey("content") && params.containsKey("url")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long strategyid = NumberUtils.toLong(params.get("strategyid"), -1);
				int rank = NumberUtils.toInt(params.get("rank"));
				String title = StringUtils.trimToEmpty(params.get("title"));
				String content = StringUtils.trimToEmpty(params.get("content"));
				String url = StringUtils.trimToEmpty(params.get("url"));
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT) && strategyid > 0 && rank >= 0 && StringUtils.isNotBlank(title)
						&& StringUtils.isNotBlank(url) && StringUtils.isNotBlank(content)){
					StrategyInfo info = new StrategyInfo();
					info.setStrategyId(strategyid);
					info.setRank(rank);
					info.setTitle(title);
					info.setContent(content);
					info.setLastModUserid(adminid);
					info.setUrl(url);
					
					if(strategyBiz.addInfo(info))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else{
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_STRATEGY_NOT_EXISTS);
					result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_STRATEGY_NOT_EXISTS));
				}
			} else{
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getStrategyDetail", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/info.ajax")
	public Map<Object, Object> getInfos(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try{
			if(params.containsKey("strategyid") && params.containsKey("adminid")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long strategyid = NumberUtils.toLong(params.get("strategyid"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT) && strategyid > 0){
					List<StrategyInfo> infos = strategyBiz.getInfos(strategyid);
					
					result.put("data", parseStrategyInfo(infos));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else{
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_STRATEGY_NOT_EXISTS);
					result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_STRATEGY_NOT_EXISTS));
				}
			} else{
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
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
				
				tmp.put("lastModTime", IWamiUtils.getDateString(info.getLastModTime()));
				tmp.put("lastModUserid", info.getLastModUserid());
				
				data.add(tmp);
			}
		
		return data;
	}

	@AjaxMethod(path = "SEQ/strategy.ajax")
	public Map<Object, Object> seqStrategy(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("ranks") && params.containsKey("ids")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT)){
					String ids = StringUtils.trimToEmpty(params.get("ids"));
					String ranks = StringUtils.trimToEmpty(params.get("ranks"));
					if(StringUtils.isNotBlank(ids) && StringUtils.isNotBlank(ranks)){
						String[] tmp = StringUtils.split(ids, IWamiConstants.SEPARATOR_STRATEGY);
						List<Long> lIds = new ArrayList<Long>();
						if(tmp != null && tmp.length > 0)
							for(String _tmp : tmp){
								long _id = NumberUtils.toLong(_tmp, -1);
								if(_id > 0)
									lIds.add(_id);
							}
						
						tmp = StringUtils.split(ranks, IWamiConstants.SEPARATOR_STRATEGY);
						List<Integer> lRanks = new ArrayList<Integer>();
						if(tmp != null && tmp.length > 0)
							for(String _tmp : tmp){
								int _rank = NumberUtils.toInt(_tmp, -1);
								if(_rank >= 0)
									lRanks.add(_rank);
							}
						
						if(lIds.size() > 0 && lRanks.size() > 0 && lIds.size() == lRanks.size()){
							if(strategyBiz.modStrategySeqs(lIds, lRanks, adminid))
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
				logger.error("Exception in getStrategyList", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "DEL/strategy.ajax")
	public Map<Object, Object> delStrategy(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT) && id > 0){
					if(strategyBiz.delStrategy(id, adminid))
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
				logger.error("Exception in getStrategyList", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "ADD/strategy.ajax")
	public Map<Object, Object> addStrategy(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("name")
					 && params.containsKey("subname") && params.containsKey("intr") && params.containsKey("rank")
					 && params.containsKey("iconSmall") && params.containsKey("iconBig") && params.containsKey("isdel")
					 && params.containsKey("skim") && params.containsKey("rate")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				String name = StringUtils.trimToEmpty(params.get("name"));
				String subname = StringUtils.trimToEmpty(params.get("subname"));
				String intr = StringUtils.trimToEmpty(params.get("intr"));
				String iconSmall = StringUtils.trimToEmpty(params.get("iconSmall"));
				String iconBig = StringUtils.trimToEmpty(params.get("iconBig"));
				int isdel = NumberUtils.toInt(params.get("isdel"), -1);
				int rank = NumberUtils.toInt(params.get("rank"), -1);
				int skim = NumberUtils.toInt(params.get("skim"), -1);
				int rate = NumberUtils.toInt(params.get("rate"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT) && StringUtils.isNotBlank(name) && StringUtils.isNotBlank(intr)
						&& StringUtils.isNotBlank(iconSmall) && StringUtils.isNotBlank(iconBig)
						&& rank >= 0 && skim >= 0 && rate >= 0 && (isdel == 0 || isdel == 1)){
					Strategy strategy = new Strategy();
					strategy.setName(name);
					strategy.setSubName(subname);
					strategy.setIntr(intr);
					strategy.setIconSmall(iconSmall);
					strategy.setIconBig(iconBig);
					strategy.setRank(rank);
					strategy.setLastModUserid(adminid);
					strategy.setIsdel(isdel);
					
					StrategyRate srate = new StrategyRate();
					srate.setRate(rate);
					srate.setSkim(skim);
					
					long id = strategyBiz.addStrategy(strategy, srate);
					if(id > 0){
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
						result.put("data", id);
					}else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getStrategyList", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "MOD/strategy.ajax")
	public Map<Object, Object> modStrategy(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id") && params.containsKey("name")
					 && params.containsKey("subname") && params.containsKey("intr") && params.containsKey("rank")
					 && params.containsKey("iconSmall") && params.containsKey("iconBig") && params.containsKey("isdel")
					 && params.containsKey("skim") && params.containsKey("rate")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				String name = StringUtils.trimToEmpty(params.get("name"));
				String subname = StringUtils.trimToEmpty(params.get("subname"));
				String intr = StringUtils.trimToEmpty(params.get("intr"));
				String iconSmall = StringUtils.trimToEmpty(params.get("iconSmall"));
				String iconBig = StringUtils.trimToEmpty(params.get("iconBig"));
				int isdel = NumberUtils.toInt(params.get("isdel"), -1);
				int rank = NumberUtils.toInt(params.get("rank"), -1);
				int skim = NumberUtils.toInt(params.get("skim"), -1);
				int rate = NumberUtils.toInt(params.get("rate"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT) && id > 0 && StringUtils.isNotBlank(name) && StringUtils.isNotBlank(intr)
						&& StringUtils.isNotBlank(iconSmall) && StringUtils.isNotBlank(iconBig)
						&& rank >= 0 && skim >= 0 && rate >= 0 && (isdel == 0 || isdel == 1)){
					Strategy strategy = new Strategy();
					strategy.setId(id);
					strategy.setName(name);
					strategy.setSubName(subname);
					strategy.setIntr(intr);
					strategy.setIconSmall(iconSmall);
					strategy.setIconBig(iconBig);
					strategy.setRank(rank);
					strategy.setLastModUserid(adminid);
					strategy.setIsdel(isdel);
					
					StrategyRate srate = new StrategyRate();
					srate.setStrategyId(id);
					srate.setRate(rate);
					srate.setSkim(skim);
					
					if(strategyBiz.modStrategy(strategy, srate))
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
				logger.error("Exception in getStrategyList", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/strategy.ajax")
	public Map<Object, Object> getStrategyList(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				String key = StringUtils.trimToEmpty(params.get("key"));
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT)){
					List<Strategy> strategies = strategyBiz.getStrategies(key);
					
					Map<Long, StrategyRate> rates = null;
					List<Long> ids = new ArrayList<Long>();
					if(strategies != null && strategies.size() > 0){
						for(Strategy st : strategies)
							ids.add(st.getId());
						
						rates = strategyBiz.getRatesByIds(ids);
					}
					
					result.put("data", parseStrategies(strategies, rates));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getStrategyList", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	private List<Map<String, Object>> parseStrategies(List<Strategy> strategies, Map<Long, StrategyRate> rates) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		if(strategies != null && strategies.size() > 0)
			for(Strategy strategy : strategies){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", strategy.getId());
				tmp.put("name", StringUtils.trimToEmpty(strategy.getName()));
				tmp.put("subname", StringUtils.trimToEmpty(strategy.getSubName()));
				tmp.put("intr", StringUtils.trimToEmpty(strategy.getIntr()));
				tmp.put("rank", strategy.getRank());
				tmp.put("iconSmall", StringUtils.trimToEmpty(strategy.getIconSmall()));
				tmp.put("iconBig", StringUtils.trimToEmpty(strategy.getIconBig()));
				tmp.put("isdel", strategy.getIsdel());
				int skim = 0;
				int rate = 0;
				if(rates != null && rates.containsKey(strategy.getId())){
					skim = rates.get(strategy.getId()).getSkim();
					rate = rates.get(strategy.getId()).getRate();
				}
				tmp.put("skim", skim);
				tmp.put("rate", rate);
				
				tmp.put("lastModTime", IWamiUtils.getDateString(strategy.getLastModTime()));
				tmp.put("lastModUserid", strategy.getLastModUserid());
				
				list.add(tmp);
			}
		
		return list;
	}

	@AjaxMethod(path = "SEQ/image.ajax")
	public Map<Object, Object> seqImage(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("ranks") && params.containsKey("ids")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT)){
					String ids = StringUtils.trimToEmpty(params.get("ids"));
					String ranks = StringUtils.trimToEmpty(params.get("ranks"));
					if(StringUtils.isNotBlank(ids) && StringUtils.isNotBlank(ranks)){
						String[] tmp = StringUtils.split(ids, IWamiConstants.SEPARATOR_STRATEGY);
						List<Long> lIds = new ArrayList<Long>();
						if(tmp != null && tmp.length > 0)
							for(String _tmp : tmp){
								long _id = NumberUtils.toLong(_tmp, -1);
								if(_id > 0)
									lIds.add(_id);
							}
						
						tmp = StringUtils.split(ranks, IWamiConstants.SEPARATOR_STRATEGY);
						List<Integer> lRanks = new ArrayList<Integer>();
						if(tmp != null && tmp.length > 0)
							for(String _tmp : tmp){
								int _rank = NumberUtils.toInt(_tmp, -1);
								if(_rank >= 0)
									lRanks.add(_rank);
							}
						
						if(lIds.size() > 0 && lRanks.size() > 0 && lIds.size() == lRanks.size()){
							if(strategyBiz.modImageSeqs(lIds, lRanks, adminid))
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
				logger.error("Exception in getStrategyList", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "DEL/image.ajax")
	public Map<Object, Object> delImage(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT)){
					int id = NumberUtils.toInt(params.get("id"), -1);
					if(id > 0 && strategyBiz.delImage(id, adminid))
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
				logger.error("Exception in getStrategyList", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "MOD/image.ajax")
	public Map<Object, Object> modImage(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("rank") && params.containsKey("url") && params.containsKey("id") && params.containsKey("isdel")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT)){
					int id = NumberUtils.toInt(params.get("id"), -1);
					int rank = NumberUtils.toInt(params.get("rank"), -2);
					String url = StringUtils.trimToEmpty(params.get("url"));
					int isdel = NumberUtils.toInt(params.get("isdel"), -1);
					if(id > 0 && rank > -2 && StringUtils.isNotBlank(url) && (isdel == 0 || isdel == 1)){
						StrategyImage image = new StrategyImage();
						image.setId(id);
						image.setRank(rank);
						image.setIconUrl(url);
						image.setLastModUserid(adminid);
						image.setIsdel(isdel);
						
						if(strategyBiz.modImage(image))
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
				logger.error("Exception in getStrategyList", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "ADD/image.ajax")
	public Map<Object, Object> addImage(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("rank") && params.containsKey("url") && params.containsKey("isdel")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT)){
					int rank = NumberUtils.toInt(params.get("rank"), -2);
					String url = StringUtils.trimToEmpty(params.get("url"));
					int isdel = NumberUtils.toInt(params.get("isdel"), -1);
					if(rank > -2 && StringUtils.isNotBlank(url) && (isdel == 0 || isdel == 1)){
						StrategyImage image = new StrategyImage();
						image.setRank(rank);
						image.setIconUrl(url);
						image.setLastModUserid(adminid);
						image.setIsdel(isdel);
						
						if(strategyBiz.addImage(image))
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
				logger.error("Exception in getStrategyList", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/images.ajax")
	public Map<Object, Object> getImages(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.STRATEGY_MANAGEMENT)){
					List<StrategyImage> images = strategyBiz.getAllImages();
					
					result.put("data", parseStrategyImages(images));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getStrategyImages", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	private List<Map<String, Object>> parseStrategyImages(List<StrategyImage> images) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		if(images != null && images.size() > 0)
			for(StrategyImage image : images){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", image.getId());
				tmp.put("rank", image.getRank());
				tmp.put("url", StringUtils.trimToEmpty(image.getIconUrl()));
				tmp.put("isdel", image.getIsdel());
				tmp.put("lastModTime", IWamiUtils.getDateString(image.getLastModTime()));
				tmp.put("lastModUserid", image.getLastModUserid());
				
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

	public LoginBiz getLoginBiz() {
		return loginBiz;
	}

	public void setLoginBiz(LoginBiz loginBiz) {
		this.loginBiz = loginBiz;
	}
	
}
