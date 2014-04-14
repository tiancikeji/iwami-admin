package com.iwami.iwami.app.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.LoginBiz;
import com.iwami.iwami.app.biz.LuckyBiz;
import com.iwami.iwami.app.biz.PresentBiz;
import com.iwami.iwami.app.biz.UserBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.exception.UserNotLoginException;
import com.iwami.iwami.app.model.Exchange;
import com.iwami.iwami.app.model.ExchangeHistory;
import com.iwami.iwami.app.model.LuckyRule;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.model.UserRole;
import com.iwami.iwami.app.util.IWamiUtils;

@AjaxClass
public class PresentAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private PresentBiz presentBiz;
	
	private LuckyBiz luckyBiz;
	
	private UserBiz userBiz;
	
	private LoginBiz loginBiz;

	@AjaxMethod(path = "MOD/exch.ajax")
	public Map<Object, Object> modExchange(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("ids")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				String[] id = StringUtils.split(params.get("ids"), ",");
				List<Long> eids = new ArrayList<Long>();
				if(id != null && id.length > 0)
					for(String tmp : id)
						if(NumberUtils.isNumber(tmp))
							eids.add(NumberUtils.toLong(tmp, -1));
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && eids.size() > 0){
					List<Long> ids = new ArrayList<Long>();
					ids.add(adminid);
					Map<Long, UserRole> roles = userBiz.getUserRoles(ids);
					UserRole role = roles.get(adminid);
					
					if(role != null){
						List<Exchange> exchanges = presentBiz.getExchangeByIds(eids);
						
						String name = StringUtils.trimToEmpty(params.get("name"));
						String no = StringUtils.trimToEmpty(params.get("no"));
						if(exchanges != null && exchanges.size() > 0){
							for(Exchange exchange : exchanges)
								if(!((StringUtils.isNotBlank(name) && StringUtils.isNotBlank(no) && exchange.getPresentType() == Present.TYPE_ONLINE_EMS && (role.getRole() & IWamiConstants.EXCHANGE_ONLINE_MANAGEMENT) > 0)
										|| (exchange.getPresentType() == Present.TYPE_ONLINE_RECHARGE_MOBILE && (role.getRole() & IWamiConstants.EXCHANGE_MOBILE_MANAGEMENT) > 0)
										|| (exchange.getPresentType() == Present.TYPE_ONLINE_RECHARGE_ALIPAY && (role.getRole() & IWamiConstants.EXCHANGE_ALIPAY_MANAGEMENT) > 0)
										|| (exchange.getPresentType() == Present.TYPE_ONLINE_RECHARGE_BANK && (role.getRole() & IWamiConstants.EXCHANGE_BANK_MANAGEMENT) > 0)
										|| (exchange.getPresentType() == Present.TYPE_LUCK && (role.getRole() & IWamiConstants.EXCHANGE_LUCKY_MANAGEMENT) > 0))){
									result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
									return result;
								}
							
								if(presentBiz.modExchange(name, no, eids, adminid))
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
				logger.error("Exception in exchangePresents", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/exchpre.ajax")
	public Map<Object, Object> getExchangeHistoryByPresent(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("key")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				String key = StringUtils.trimToEmpty(params.get("key"));
				if(adminid > 0 && loginBiz.checkLogin(adminid) && StringUtils.isNotBlank(key)){
					List<Long> ids = new ArrayList<Long>();
					ids.add(adminid);
					Map<Long, UserRole> roles = userBiz.getUserRoles(ids);
					UserRole role = roles.get(adminid);
					
					if(role != null){
						List<Integer> types = new ArrayList<Integer>();
						
						if((role.getRole() & IWamiConstants.EXCHANGE_ONLINE_MANAGEMENT) > 0)
							types.add(Present.TYPE_ONLINE_EMS);
						if((role.getRole() & IWamiConstants.EXCHANGE_MOBILE_MANAGEMENT) > 0)
							types.add(Present.TYPE_ONLINE_RECHARGE_MOBILE);
						if((role.getRole() & IWamiConstants.EXCHANGE_ALIPAY_MANAGEMENT) > 0)
							types.add(Present.TYPE_ONLINE_RECHARGE_ALIPAY);
						if((role.getRole() & IWamiConstants.EXCHANGE_BANK_MANAGEMENT) > 0)
							types.add(Present.TYPE_ONLINE_RECHARGE_BANK);
						if((role.getRole() & IWamiConstants.EXCHANGE_OFFLINE_MANAGEMENT) > 0)
							types.add(Present.TYPE_OFFLINE);
						if((role.getRole() & IWamiConstants.EXCHANGE_LUCKY_MANAGEMENT) > 0)
							types.add(Present.TYPE_LUCK);
						
						if(types != null && types.size() > 0){
							int start = NumberUtils.toInt(params.get("start"), 0);
							int step = NumberUtils.toInt(params.get("step"), 20);
							List<ExchangeHistory> history = presentBiz.getExchangeHistoryByPresent(types, key);
							List<ExchangeHistory> htmps = new ArrayList<ExchangeHistory>();
							for(int i = start; i < history.size() && i < (start + step); i ++)
								htmps.add(history.get(i));

							Map<Long, User> users = new HashMap<Long, User>();
							if(htmps != null && htmps.size() > 0){
								Set<Long> uids = new HashSet<Long>();

								for(ExchangeHistory eh : htmps)
									uids.add(eh.getUserid());
								
								List<User> tmps = userBiz.getUserByIds(uids);
								if(tmps != null && tmps.size() > 0)
									for(User tmp : tmps)
										users.put(tmp.getId(), tmp);
							}
							
							Map<String, Object> data = new HashMap<String, Object>();
							data.put("list", parseExchangeHistory(htmps, users));
							data.put("count", history == null ? 0 : history.size());
							result.put("data", data);
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
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
				logger.error("Exception in exchangePresents", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/exchuser.ajax")
	public Map<Object, Object> getExchangeHistoryByUser(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("key")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long key = NumberUtils.toLong(params.get("key"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid) && key > 0){
					List<Long> ids = new ArrayList<Long>();
					ids.add(adminid);
					Map<Long, UserRole> roles = userBiz.getUserRoles(ids);
					UserRole role = roles.get(adminid);
					
					if(role != null){
						List<Integer> types = new ArrayList<Integer>();
						
						if((role.getRole() & IWamiConstants.EXCHANGE_ONLINE_MANAGEMENT) > 0)
							types.add(Present.TYPE_ONLINE_EMS);
						if((role.getRole() & IWamiConstants.EXCHANGE_MOBILE_MANAGEMENT) > 0)
							types.add(Present.TYPE_ONLINE_RECHARGE_MOBILE);
						if((role.getRole() & IWamiConstants.EXCHANGE_ALIPAY_MANAGEMENT) > 0)
							types.add(Present.TYPE_ONLINE_RECHARGE_ALIPAY);
						if((role.getRole() & IWamiConstants.EXCHANGE_BANK_MANAGEMENT) > 0)
							types.add(Present.TYPE_ONLINE_RECHARGE_BANK);
						if((role.getRole() & IWamiConstants.EXCHANGE_OFFLINE_MANAGEMENT) > 0)
							types.add(Present.TYPE_OFFLINE);
						if((role.getRole() & IWamiConstants.EXCHANGE_LUCKY_MANAGEMENT) > 0)
							types.add(Present.TYPE_LUCK);
						
						if(types != null && types.size() > 0){
							User user = userBiz.getUserByCellPhone(key);
							if(user != null)
								key = user.getId();
							int start = NumberUtils.toInt(params.get("start"), 0);
							int step = NumberUtils.toInt(params.get("step"), 20);
							List<ExchangeHistory> history = presentBiz.getExchangeHistoryByUser(types, key);
							List<ExchangeHistory> htmps = new ArrayList<ExchangeHistory>();
							for(int i = start; i < history.size() && i < (start + step); i ++)
								htmps.add(history.get(i));

							Map<Long, User> users = new HashMap<Long, User>();
							if(htmps != null && htmps.size() > 0){
								Set<Long> uids = new HashSet<Long>();

								for(ExchangeHistory eh : htmps)
									uids.add(eh.getUserid());
								
								List<User> tmps = userBiz.getUserByIds(uids);
								if(tmps != null && tmps.size() > 0)
									for(User tmp : tmps)
										users.put(tmp.getId(), tmp);
							}
							
							Map<String, Object> data = new HashMap<String, Object>();
							data.put("list", parseExchangeHistory(htmps, users));
							data.put("count", history == null ? 0 : history.size());
							result.put("data", data);
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
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
				logger.error("Exception in exchangePresents", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/exch.ajax")
	public Map<Object, Object> getExchangeHistory(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("type") && params.containsKey("status")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				int status = NumberUtils.toInt(params.get("status"), -1);
				int type = NumberUtils.toInt(params.get("type"), -1);
				if(adminid > 0 && loginBiz.checkLogin(adminid)
						&& status >= 0 && status <= 3
						&& type >= 0 && type <= 6){
					List<Long> ids = new ArrayList<Long>();
					ids.add(adminid);
					Map<Long, UserRole> roles = userBiz.getUserRoles(ids);
					UserRole role = roles.get(adminid);
					
					if(role != null){
						List<Integer> types = new ArrayList<Integer>();
						
						if(type == 1 || type == 0)
							if((role.getRole() & IWamiConstants.EXCHANGE_ONLINE_MANAGEMENT) > 0)
								types.add(Present.TYPE_ONLINE_EMS);
						if(type == 2 || type == 0)
							if((role.getRole() & IWamiConstants.EXCHANGE_MOBILE_MANAGEMENT) > 0)
								types.add(Present.TYPE_ONLINE_RECHARGE_MOBILE);
						if(type == 3 || type == 0)
							if((role.getRole() & IWamiConstants.EXCHANGE_ALIPAY_MANAGEMENT) > 0)
								types.add(Present.TYPE_ONLINE_RECHARGE_ALIPAY);
						if(type == 4 || type == 0)
							if((role.getRole() & IWamiConstants.EXCHANGE_BANK_MANAGEMENT) > 0)
								types.add(Present.TYPE_ONLINE_RECHARGE_BANK);
						if(type == 5 || type == 0)
							if((role.getRole() & IWamiConstants.EXCHANGE_OFFLINE_MANAGEMENT) > 0)
								types.add(Present.TYPE_OFFLINE);
						if(type == 6 || type == 0)
							if((role.getRole() & IWamiConstants.EXCHANGE_LUCKY_MANAGEMENT) > 0)
								types.add(Present.TYPE_LUCK);
						
						if(types != null && types.size() > 0){
							List<Integer> stats = new ArrayList<Integer>();
							if(status == 1 || status == 0)
								stats.add(Exchange.STATUS_READY);
							if(status == 2 || status == 3 || status == 0)
								stats.add(Exchange.STATUS_FINISH);

							int start = NumberUtils.toInt(params.get("start"), 0);
							int step = NumberUtils.toInt(params.get("step"), 20);
							List<ExchangeHistory> history = presentBiz.getExchangeHistory(types, stats);
							List<ExchangeHistory> htmps = new ArrayList<ExchangeHistory>();
							for(int i = start; i < history.size() && i < (start + step); i ++)
								htmps.add(history.get(i));
							
							Map<Long, User> users = new HashMap<Long, User>();
							if(htmps != null && htmps.size() > 0){
								Set<Long> uids = new HashSet<Long>();

								for(ExchangeHistory eh : htmps)
									uids.add(eh.getUserid());
								
								List<User> tmps = userBiz.getUserByIds(uids);
								if(tmps != null && tmps.size() > 0)
									for(User user : tmps)
										users.put(user.getId(), user);
							}
							
							Map<String, Object> data = new HashMap<String, Object>();
							data.put("list", parseExchangeHistory(htmps, users));
							data.put("count", history == null ? 0 : history.size());
							result.put("data", data);
							result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
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
				logger.error("Exception in exchangePresents", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	private Object parseExchangeHistory(List<ExchangeHistory> history, Map<Long, User> users) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		if(history != null && history.size() > 0)
			for(ExchangeHistory tmp : history)
				if(tmp != null && tmp.getExchange() != null){
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("userid", tmp.getUserid());
					data.put("presents", parseExchange(tmp.getExchange(), users.get(tmp.getUserid()).getCellPhone()));
					
					list.add(data);
				}
		return list;
	}

	private List<Map<String, Object>> parseExchange(List<Exchange> exchanges, long cellPhone) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		if(exchanges != null && exchanges.size() > 0)
			for(Exchange exchange : exchanges){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", exchange.getId());
				tmp.put("userCellPhone", cellPhone);
				tmp.put("presentId", exchange.getPresentId());
				tmp.put("presentName", exchange.getPresentName());
				tmp.put("presentPrize", exchange.getPresentId());
				tmp.put("presentType", exchange.getPresentType());
				tmp.put("count", exchange.getCount());
				tmp.put("prize", exchange.getPrize());
				tmp.put("status", exchange.getStatus());
				if(exchange.getCellPhone() > 0)
					tmp.put("cellPhone", exchange.getCellPhone());
				else
					tmp.put("cellPhone", StringUtils.EMPTY);
				tmp.put("alipayAccount", StringUtils.trimToEmpty(exchange.getAlipayAccount()));
				tmp.put("bankAccount", StringUtils.trimToEmpty(exchange.getBankAccount()));
				tmp.put("bankName", StringUtils.trimToEmpty(exchange.getBankName()));
				if(exchange.getBankNo() > 0)
					tmp.put("bankNo", exchange.getBankNo());
				else
					tmp.put("bankNo", StringUtils.EMPTY);
				tmp.put("address", StringUtils.trimToEmpty(exchange.getAddress()));
				tmp.put("name", StringUtils.trimToEmpty(exchange.getName()));
				tmp.put("expressName", StringUtils.trimToEmpty(exchange.getExpressName()));
				tmp.put("expressNo", StringUtils.trimToEmpty(exchange.getExpressNo()));
				tmp.put("channel", StringUtils.trimToEmpty(exchange.getChannel()));

				tmp.put("addTime", IWamiUtils.getDateString(exchange.getAddTime()));
				tmp.put("lastModTime", IWamiUtils.getDateString(exchange.getLastModTime()));
				tmp.put("lastModUserid", exchange.getLastModUserid());
				
				if(exchange.getPresentType() == Present.TYPE_ONLINE_RECHARGE_ALIPAY || exchange.getPresentType() == Present.TYPE_ONLINE_RECHARGE_BANK)
					tmp.put("money", exchange.getPrize() / exchange.getPresentId());
				else
					tmp.put("money", StringUtils.EMPTY);
				
				result.add(tmp);
			}
		
		return result;
	}
	
	@AjaxMethod(path = "SEQ/present.ajax")
	public Map<Object, Object> seqPresent(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("ids") && params.containsKey("ranks")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				String[] ids = StringUtils.split(params.get("ids"), IWamiConstants.SEPARATOR_PRESENT);
				String[] ranks = StringUtils.split(params.get("ranks"), IWamiConstants.SEPARATOR_PRESENT);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& ids != null && ranks != null && ids.length > 0 && ids.length == ranks.length){
					Map<Long, Integer> data = new HashMap<Long, Integer>();
					
					for(int i = 0; i < ids.length; i ++){
						long id = NumberUtils.toLong(ids[i], -1);
						int rank = NumberUtils.toInt(ranks[i], Integer.MAX_VALUE);
						if(id > 0)
							data.put(id, rank);
					}
					
					if(data != null && data.size() > 0 && data.size() == ids.length){
						if(presentBiz.seqPresent(data, adminid))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "DEL/luck.ajax")
	public Map<Object, Object> delLuck(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& id > 0){
					if(presentBiz.delPresent(id, adminid) && luckyBiz.delRules(adminid))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "MOD/luck.ajax")
	public Map<Object, Object> modLuck(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try{
			if(params.containsKey("adminid") && params.containsKey("id")
					 && params.containsKey("rank") && params.containsKey("prize") && params.containsKey("gifts") && params.containsKey("probs") && params.containsKey("counts")
					  && params.containsKey("iconSmall") && params.containsKey("isdel")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				int prize = NumberUtils.toInt(params.get("prize"), -1);
				int rank = NumberUtils.toInt(params.get("rank"), -1);
				int isdel = NumberUtils.toInt(params.get("isdel"), -1);
				String iconSmall = StringUtils.trimToEmpty(params.get("iconSmall"));
				
				
				String[] gifts = StringUtils.split(StringUtils.trimToEmpty(params.get("gifts")), IWamiConstants.SEPARATOR_PRESENT);
				String[] probs = StringUtils.split(StringUtils.trimToEmpty(params.get("probs")), IWamiConstants.SEPARATOR_PRESENT);
				String[] counts = StringUtils.split(StringUtils.trimToEmpty(params.get("counts")), IWamiConstants.SEPARATOR_PRESENT);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& id > 0 && prize > 0 && (isdel == 0 || isdel == 1)
						 && StringUtils.isNotBlank(iconSmall) 
						 && gifts != null && gifts.length == 4 && probs != null && probs.length == 4 &&  counts != null && counts.length == 4){
					
					List<LuckyRule> rules = new ArrayList<LuckyRule>();
					for(int i = 0; i < gifts.length; i ++){
						String gift = StringUtils.trimToEmpty(gifts[i]);
						int prob = NumberUtils.toInt(probs[i], -1);
						int count = NumberUtils.toInt(counts[i], -1);
						
						if(StringUtils.isNotBlank(gift) && prob > 0){
							LuckyRule rule = new LuckyRule();
							rule.setIndexLevel(i + 1);
							rule.setGift(gift);
							rule.setCount(count);
							rule.setProb(prob);
							rule.setLastmodUserid(adminid);
							rule.setIsdel(isdel);
							
							rules.add(rule);
						}
					}
					
					if(rules != null && rules.size() == 4){
						Present present = new Present();
						present.setId(id);
						present.setName("抽奖");
						present.setPrize(prize);
						present.setCount(-1);
						present.setType(Present.TYPE_LUCK);
						present.setRank(rank);
						present.setIconSmall(iconSmall);
						
						present.setIsdel(isdel);
						present.setLastModUserid(adminid);
					
						if(presentBiz.modPresent(present) && luckyBiz.modRules(rules))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/luck.ajax")
	public Map<Object, Object> getLuck(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("status")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				int status = NumberUtils.toInt(params.get("status"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& status >= 0 && status <= 2){
					List<Integer> stat = new ArrayList<Integer>();
					if(status == 0 || status == 1)
						stat.add(IWamiConstants.ACTIVE);
					if(status == 0 || status == 2)
						stat.add(IWamiConstants.INACTIVE);
							
					List<Present> presents = presentBiz.getPresentsByTypeNStatus(Present.TYPE_LUCK, stat, 0, Integer.MAX_VALUE);
					List<LuckyRule> rules = luckyBiz.getLuckyRules();
					
					List<Map<String, Object>> data = parsePresents(presents);
					if(data != null && data.size() > 0){
						Map<String, Object> tmp = data.get(0);
						List<Map<String, Object>> ruleTmp = new ArrayList<Map<String,Object>>();
						if(tmp != null && rules != null && rules.size() > 0)
							for(LuckyRule rule : rules){
								Map<String, Object> rtmp = new HashMap<String, Object>();
								
								rtmp.put("index", rule.getIndexLevel());
								rtmp.put("gift", rule.getGift());
								rtmp.put("prob", rule.getProb());
								rtmp.put("count", rule.getCount());
								
								ruleTmp.add(rtmp);
							}
						
						tmp.put("rules", ruleTmp);
					}
					
					result.put("data", data);
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getLuck", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "DEL/offline.ajax")
	public Map<Object, Object> delOffline(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& id > 0){
					if(presentBiz.delPresent(id, adminid))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "ADD/offline.ajax")
	public Map<Object, Object> addOffline(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("name")
					 && params.containsKey("rank") && params.containsKey("prize")
					  && params.containsKey("iconSmall") && params.containsKey("isdel")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				int prize = NumberUtils.toInt(params.get("prize"), -1);
				int rank = NumberUtils.toInt(params.get("rank"), -1);
				int isdel = NumberUtils.toInt(params.get("isdel"), -1);
				String name = StringUtils.trimToEmpty(params.get("name"));
				String iconSmall = StringUtils.trimToEmpty(params.get("iconSmall"));
				
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& prize > 0 && StringUtils.isNotBlank(name) && (isdel == 0 || isdel == 1)
						 && StringUtils.isNotBlank(iconSmall) ){
					Present present = new Present();
					present.setName(name);
					present.setPrize(prize);
					present.setCount(1);
					present.setType(Present.TYPE_OFFLINE);
					present.setRank(rank);
					present.setIconSmall(iconSmall);
					present.setChannel(StringUtils.trimToEmpty(params.get("channel")));
					
					present.setIsdel(isdel);
					present.setLastModUserid(adminid);
					
					if(presentBiz.addPresent(present))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "MOD/offline.ajax")
	public Map<Object, Object> modOffline(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id") && params.containsKey("name")
					 && params.containsKey("rank") && params.containsKey("prize")
					  && params.containsKey("iconSmall") && params.containsKey("isdel")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				int prize = NumberUtils.toInt(params.get("prize"), -1);
				int rank = NumberUtils.toInt(params.get("rank"), -1);
				int isdel = NumberUtils.toInt(params.get("isdel"), -1);
				String name = StringUtils.trimToEmpty(params.get("name"));
				String iconSmall = StringUtils.trimToEmpty(params.get("iconSmall"));
				
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& id > 0 && prize > 0 && StringUtils.isNotBlank(name) && (isdel == 0 || isdel == 1)
						 && StringUtils.isNotBlank(iconSmall) ){
					Present present = new Present();
					present.setId(id);
					present.setName(name);
					present.setPrize(prize);
					present.setCount(1);
					present.setType(Present.TYPE_OFFLINE);
					present.setRank(rank);
					present.setIconSmall(iconSmall);
					present.setChannel(StringUtils.trimToEmpty(params.get("channel")));
					
					present.setIsdel(isdel);
					present.setLastModUserid(adminid);
					
					if(presentBiz.modPresent(present))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/offline.ajax")
	public Map<Object, Object> getOffline(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("status")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				int status = NumberUtils.toInt(params.get("status"), -1);
				String channel = StringUtils.trimToEmpty(params.get("channel"));
				int start = NumberUtils.toInt(params.get("start"), 0);
				int step = NumberUtils.toInt(params.get("step"), 20);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& status >= 0 && status <= 2 && start >= 0 && step > 0){
					List<Integer> stat = new ArrayList<Integer>();
					if(status == 0 || status == 1)
						stat.add(IWamiConstants.ACTIVE);
					if(status == 0 || status == 2)
						stat.add(IWamiConstants.INACTIVE);
							
					List<Present> presents = presentBiz.getPresentsByTypeNStatus(Present.TYPE_OFFLINE, channel, stat, start, step);
					result.put("data", parsePresents(presents));
					result.put("count", presentBiz.getPresentCountByTypeNStatus(Present.TYPE_OFFLINE, channel, stat));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getLuck", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/offchannel.ajax")
	public Map<Object, Object> getOfflineByChannel(Map<String, String> params) {
		return getOffline(params);
	}

	@AjaxMethod(path = "DEL/online.ajax")
	public Map<Object, Object> delOnline(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& id > 0){
					if(presentBiz.delPresent(id, adminid))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "ADD/online.ajax")
	public Map<Object, Object> addOnline(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("name")
					 && params.containsKey("rank") && params.containsKey("prize")
					  && params.containsKey("iconSmall") && params.containsKey("isdel")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				int prize = NumberUtils.toInt(params.get("prize"), -1);
				int rank = NumberUtils.toInt(params.get("rank"), -1);
				int isdel = NumberUtils.toInt(params.get("isdel"), -1);
				String name = StringUtils.trimToEmpty(params.get("name"));
				String iconSmall = StringUtils.trimToEmpty(params.get("iconSmall"));
				
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& prize > 0 && StringUtils.isNotBlank(name) && (isdel == 0 || isdel == 1)
						 && StringUtils.isNotBlank(iconSmall) ){
					Present present = new Present();
					present.setName(name);
					present.setPrize(prize);
					present.setCount(1);
					present.setType(Present.TYPE_ONLINE_EMS);
					present.setRank(rank);
					present.setIconSmall(iconSmall);
					
					present.setIsdel(isdel);
					present.setLastModUserid(adminid);
					
					if(presentBiz.addPresent(present))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "MOD/online.ajax")
	public Map<Object, Object> modOnline(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id") && params.containsKey("name")
					 && params.containsKey("rank") && params.containsKey("prize")
					  && params.containsKey("iconSmall") && params.containsKey("isdel")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				int prize = NumberUtils.toInt(params.get("prize"), -1);
				int rank = NumberUtils.toInt(params.get("rank"), -1);
				int isdel = NumberUtils.toInt(params.get("isdel"), -1);
				String name = StringUtils.trimToEmpty(params.get("name"));
				String iconSmall = StringUtils.trimToEmpty(params.get("iconSmall"));
				
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& id > 0 && prize > 0 && StringUtils.isNotBlank(name) && (isdel == 0 || isdel == 1)
						 && StringUtils.isNotBlank(iconSmall) ){
					Present present = new Present();
					present.setId(id);
					present.setName(name);
					present.setPrize(prize);
					present.setCount(1);
					present.setType(Present.TYPE_ONLINE_EMS);
					present.setRank(rank);
					present.setIconSmall(iconSmall);
					
					present.setIsdel(isdel);
					present.setLastModUserid(adminid);
					
					if(presentBiz.modPresent(present))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/presents.ajax")
	public Map<Object, Object> getPresents(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)){
					List<Present> presents = presentBiz.getPresents();
					
					List<Present> tmps = new ArrayList<Present>();
					if(presents != null && presents.size() > 0)
						for(Present present : presents)
							if(present.getType() != Present.TYPE_OFFLINE)
								tmps.add(present);
					
					result.put("data", parsePresents(tmps));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getOnline", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/online.ajax")
	public Map<Object, Object> getOnline(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("status")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				int status = NumberUtils.toInt(params.get("status"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& status >= 0 && status <= 2){
					List<Integer> stat = new ArrayList<Integer>();
					if(status == 0 || status == 1)
						stat.add(IWamiConstants.ACTIVE);
					if(status == 0 || status == 2)
						stat.add(IWamiConstants.INACTIVE);
							
					List<Present> presents = presentBiz.getPresentsByTypeNStatus(Present.TYPE_ONLINE_EMS, stat, 0, Integer.MAX_VALUE);
					result.put("data", parsePresents(presents));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getOnline", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "DEL/mobile.ajax")
	public Map<Object, Object> delMobile(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& id > 0){
					if(presentBiz.delPresent(id, adminid))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "ADD/mobile.ajax")
	public Map<Object, Object> addMobile(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("name")
					 && params.containsKey("rank") && params.containsKey("prize") && params.containsKey("count")
					  && params.containsKey("iconSmall") && params.containsKey("isdel")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				int prize = NumberUtils.toInt(params.get("prize"), -1);
				int count = NumberUtils.toInt(params.get("count"), -1);
				int rank = NumberUtils.toInt(params.get("rank"), -1);
				int isdel = NumberUtils.toInt(params.get("isdel"), -1);
				String name = StringUtils.trimToEmpty(params.get("name"));
				String iconSmall = StringUtils.trimToEmpty(params.get("iconSmall"));
				
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& prize > 0 && /*count >= prize &&*/ StringUtils.isNotBlank(name) && (isdel == 0 || isdel == 1)
						 && StringUtils.isNotBlank(iconSmall) ){
					Present present = new Present();
					present.setName(name);
					present.setPrize(prize);
					present.setCount(count);
					present.setType(Present.TYPE_ONLINE_RECHARGE_MOBILE);
					present.setRank(rank);
					present.setIconSmall(iconSmall);
					
					present.setIsdel(isdel);
					present.setLastModUserid(adminid);
					
					if(presentBiz.addPresent(present))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "MOD/mobile.ajax")
	public Map<Object, Object> modMobile(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id") && params.containsKey("name")
					 && params.containsKey("rank") && params.containsKey("prize") && params.containsKey("count")
					  && params.containsKey("iconSmall") && params.containsKey("isdel")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				int prize = NumberUtils.toInt(params.get("prize"), -1);
				int count = NumberUtils.toInt(params.get("count"), -1);
				int rank = NumberUtils.toInt(params.get("rank"), -1);
				int isdel = NumberUtils.toInt(params.get("isdel"), -1);
				String name = StringUtils.trimToEmpty(params.get("name"));
				String iconSmall = StringUtils.trimToEmpty(params.get("iconSmall"));
				
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& id > 0 && prize > 0 && /*count >= prize &&*/ StringUtils.isNotBlank(name) && (isdel == 0 || isdel == 1)
						 && StringUtils.isNotBlank(iconSmall) ){
					Present present = new Present();
					present.setId(id);
					present.setName(name);
					present.setPrize(prize);
					present.setCount(count);
					present.setType(Present.TYPE_ONLINE_RECHARGE_MOBILE);
					present.setRank(rank);
					present.setIconSmall(iconSmall);
					
					present.setIsdel(isdel);
					present.setLastModUserid(adminid);
					
					if(presentBiz.modPresent(present))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/mobile.ajax")
	public Map<Object, Object> getMobile(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("status")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				int status = NumberUtils.toInt(params.get("status"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& status >= 0 && status <= 2){
					List<Integer> stat = new ArrayList<Integer>();
					if(status == 0 || status == 1)
						stat.add(IWamiConstants.ACTIVE);
					if(status == 0 || status == 2)
						stat.add(IWamiConstants.INACTIVE);
							
					List<Present> presents = presentBiz.getPresentsByTypeNStatus(Present.TYPE_ONLINE_RECHARGE_MOBILE, stat, 0, Integer.MAX_VALUE);
					result.put("data", parsePresents(presents));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getMobile", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "DEL/ali.ajax")
	public Map<Object, Object> delAli(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& id > 0){
					if(presentBiz.delPresent(id, adminid))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "MOD/ali.ajax")
	public Map<Object, Object> modAli(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id") && params.containsKey("name")
					 && params.containsKey("rank") && params.containsKey("prize") && params.containsKey("count")
					  && params.containsKey("iconSmall") && params.containsKey("isdel")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				int prize = NumberUtils.toInt(params.get("prize"), -1);
				int count = NumberUtils.toInt(params.get("count"), -1);
				int rank = NumberUtils.toInt(params.get("rank"), -1);
				int isdel = NumberUtils.toInt(params.get("isdel"), -1);
				String name = StringUtils.trimToEmpty(params.get("name"));
				String iconSmall = StringUtils.trimToEmpty(params.get("iconSmall"));
				
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& id > 0 && prize > 0 && count >= prize && StringUtils.isNotBlank(name) && (isdel == 0 || isdel == 1)
						 && StringUtils.isNotBlank(iconSmall) ){
					Present present = new Present();
					present.setId(id);
					present.setName(name);
					present.setPrize(prize);
					present.setCount(count);
					present.setType(Present.TYPE_ONLINE_RECHARGE_ALIPAY);
					present.setRank(rank);
					present.setIconSmall(iconSmall);
					
					present.setIsdel(isdel);
					present.setLastModUserid(adminid);
					
					if(presentBiz.modPresent(present))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/ali.ajax")
	public Map<Object, Object> getAli(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("status")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				int status = NumberUtils.toInt(params.get("status"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& status >= 0 && status <= 2){
					List<Integer> stat = new ArrayList<Integer>();
					if(status == 0 || status == 1)
						stat.add(IWamiConstants.ACTIVE);
					if(status == 0 || status == 2)
						stat.add(IWamiConstants.INACTIVE);
							
					List<Present> presents = presentBiz.getPresentsByTypeNStatus(Present.TYPE_ONLINE_RECHARGE_ALIPAY, stat, 0, Integer.MAX_VALUE);
					result.put("data", parsePresents(presents));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getAlis", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "DEL/bank.ajax")
	public Map<Object, Object> delBank(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& id > 0){
					if(presentBiz.delPresent(id, adminid))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}
	
	@AjaxMethod(path = "MOD/bank.ajax")
	public Map<Object, Object> modBank(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("id") && params.containsKey("name")
					 && params.containsKey("rank") && params.containsKey("prize") && params.containsKey("count")
					  && params.containsKey("iconSmall") && params.containsKey("isdel")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				long id = NumberUtils.toLong(params.get("id"), -1);
				int prize = NumberUtils.toInt(params.get("prize"), -1);
				int count = NumberUtils.toInt(params.get("count"), -1);
				int rank = NumberUtils.toInt(params.get("rank"), -1);
				int isdel = NumberUtils.toInt(params.get("isdel"), -1);
				String name = StringUtils.trimToEmpty(params.get("name"));
				String iconSmall = StringUtils.trimToEmpty(params.get("iconSmall"));
				
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& id > 0 && prize > 0 && count >= prize && StringUtils.isNotBlank(name) && (isdel == 0 || isdel == 1)
						 && StringUtils.isNotBlank(iconSmall) ){
					Present present = new Present();
					present.setId(id);
					present.setName(name);
					present.setPrize(prize);
					present.setCount(count);
					present.setType(Present.TYPE_ONLINE_RECHARGE_BANK);
					present.setRank(rank);
					present.setIconSmall(iconSmall);
					
					present.setIsdel(isdel);
					present.setLastModUserid(adminid);
					
					if(presentBiz.modPresent(present))
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
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "GET/bank.ajax")
	public Map<Object, Object> getBank(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid") && params.containsKey("status")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				int status = NumberUtils.toInt(params.get("status"), -1);
				
				if(adminid > 0 && loginBiz.checkLogin(adminid) && loginBiz.checkRole(adminid, IWamiConstants.PRESENT_MANAGEMENT)
						&& status >= 0 && status <= 2){
					List<Integer> stat = new ArrayList<Integer>();
					if(status == 0 || status == 1)
						stat.add(IWamiConstants.ACTIVE);
					if(status == 0 || status == 2)
						stat.add(IWamiConstants.INACTIVE);
							
					List<Present> presents = presentBiz.getPresentsByTypeNStatus(Present.TYPE_ONLINE_RECHARGE_BANK, stat, 0, Integer.MAX_VALUE);
					result.put("data", parsePresents(presents));
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(UserNotLoginException e){
			result.put(ErrorCodeConstants.STATUS_KEY, 500);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in getBanks", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	private List<Map<String, Object>> parsePresents(List<Present> presents) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		
		if(presents != null && presents.size() > 0)
			for(Present present : presents){
				Map<String, Object> tmp = new HashMap<String, Object>();
				
				tmp.put("id", present.getId());
				tmp.put("name", present.getName());
				tmp.put("prize", present.getPrize());
				tmp.put("count", present.getCount());
				tmp.put("rank", present.getRank());
				tmp.put("type", present.getType());
				tmp.put("iconSmall", present.getIconSmall());
				tmp.put("isdel", present.getIsdel());
				tmp.put("channel", present.getChannel());

				tmp.put("lastModTime", IWamiUtils.getDateString(present.getLastModTime()));
				tmp.put("lastModUserid", present.getLastModUserid());
				
				data.add(tmp);
			}
		
		return data;
	}

	public PresentBiz getPresentBiz() {
		return presentBiz;
	}

	public void setPresentBiz(PresentBiz presentBiz) {
		this.presentBiz = presentBiz;
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

	public LuckyBiz getLuckyBiz() {
		return luckyBiz;
	}

	public void setLuckyBiz(LuckyBiz luckyBiz) {
		this.luckyBiz = luckyBiz;
	}

}
