package com.iwami.iwami.app.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.LuckyBiz;
import com.iwami.iwami.app.biz.UserBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.exception.LuckyExceedLimitException;
import com.iwami.iwami.app.exception.NotEnoughPrizeException;
import com.iwami.iwami.app.model.LuckyConfig;
import com.iwami.iwami.app.model.LuckyRule;
import com.iwami.iwami.app.model.User;

@AjaxClass
public class LuckyAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private LuckyBiz luckyBiz;
	
	private UserBiz userBiz;

	@AjaxMethod(path = "luck/rule.ajax")
	public Map<Object, Object> getRules(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			List<LuckyRule> rules = luckyBiz.getLuckyRules();
			List<Map<String, Object>> tmp = new ArrayList<Map<String,Object>>();
			if(rules != null)
				for(LuckyRule rule : rules){
					Map<String, Object> rtmp = new HashMap<String, Object>();
					rtmp.put("level", rule.getIndexLevel());
					rtmp.put("gift", rule.getGift());
					tmp.add(rtmp);
				}
				
			result.put("data", tmp);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in lucky rules", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	@AjaxMethod(path = "luck/draw.ajax")
	public Map<Object, Object> draw(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			long userid = -1;
			
			if(params.containsKey("userid")){
				userid = NumberUtils.toLong(params.get("userid"));
				User user = userBiz.getUserById(userid);
				
				if(user != null && user.getId() == userid){
					LuckyConfig config = luckyBiz.getLuckyConfig();
					if(config != null && user.getCurrentPrize() >= config.getPrize()){
						LuckyRule rule = luckyBiz.draw(user, config);
						
						Map<String, Object> tmp = new HashMap<String, Object>();
						if(rule != null){
							tmp.put("level", rule.getIndexLevel());
							tmp.put("gift", rule.getGift());
						} else{
							tmp.put("level", -1);
							tmp.put("gift", "未中奖");
						}
						
						user = userBiz.getUserById(userid);
						tmp.put("prize", user.getCurrentPrize());
						if(config.getPrize() > 0)
							tmp.put("count", user.getCurrentPrize() / config.getPrize());
						else
							tmp.put("count", 0);
						
						result.put("data", tmp);
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					} else{
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_LUCKY_DRAW_PRIZE);
						result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_LUCKY_DRAW_PRIZE));
					}
				} else{
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_LUCKY_DRAW_USERID);
					result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_LUCKY_DRAW_USERID));
				}
			} else {
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_LUCKY_DRAW_USERID);
				result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_LUCKY_DRAW_USERID));
			}
				
		} catch(LuckyExceedLimitException e){
			if(logger.isErrorEnabled())
				logger.error("Exceed lucky limit", e);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_LUCKY_DRAW_COUNT);
			result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_LUCKY_DRAW_COUNT));
		} catch(NotEnoughPrizeException e){
			if(logger.isErrorEnabled())
				logger.error("Not enough prize", e);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR_LUCKY_DRAW_PRIZE);
			result.put(ErrorCodeConstants.MSG_KEY, ErrorCodeConstants.ERROR_MSG_MAP.get(ErrorCodeConstants.STATUS_ERROR_LUCKY_DRAW_PRIZE));
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in lucky draw", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

	public LuckyBiz getLuckyBiz() {
		return luckyBiz;
	}

	public void setLuckyBiz(LuckyBiz luckyBiz) {
		this.luckyBiz = luckyBiz;
	}

	public UserBiz getUserBiz() {
		return userBiz;
	}

	public void setUserBiz(UserBiz userBiz) {
		this.userBiz = userBiz;
	}
}
