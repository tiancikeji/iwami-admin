package com.iwami.iwami.app.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.TipsBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.model.Tips;
import com.iwami.iwami.app.util.IWamiUtils;

@AjaxClass
public class TipsAjax {

	private Log logger = LogFactory.getLog(getClass());

	private TipsBiz tipsBiz;

	@AjaxMethod(path = "MOD/tip.ajax")
	public Map<Object, Object> modTip(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			if (params.containsKey("adminid") && params.containsKey("type") && params.containsKey("content")) {
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				int type = NumberUtils.toInt(params.get("type"), -1);
				String content = StringUtils.trimToEmpty(params.get("content"));
				
				// TODO check admin id
				if (adminid > 0 && type >= 0 && StringUtils.isNotBlank(content)) {
					Tips tip = new Tips();
					tip.setContent(content);
					tip.setType(type);
					tip.setLastmodUserid(adminid);
					
					if(tipsBiz.addTip(tip))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else {
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
				}
			} else {
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch (Throwable t) {
			if (logger.isErrorEnabled())
				logger.error("Exception in tips", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		return result;
	}

	@AjaxMethod(path = "GET/tip.ajax")
	public Map<Object, Object> getTips(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			if (params.containsKey("adminid")) {
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				// TODO check admin id
				if (adminid > 0) {
					List<Tips> tips = tipsBiz.getTips();
					List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
					if(tips != null)
						for(Tips tip : tips){
							Map<String, Object> tmp = new HashMap<String, Object>();
							tmp.put("type", tip.getType());
							tmp.put("content", tip.getContent());
							
							tmp.put("lastModTime", IWamiUtils.getDateString(tip.getLastmodTime()));
							tmp.put("lastModUserid", tip.getLastmodUserid());
						}
					result.put("data", data);
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else {
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
				}
			} else {
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			}
		} catch (Throwable t) {
			if (logger.isErrorEnabled())
				logger.error("Exception in tips", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		return result;
	}

	public TipsBiz getTipsBiz() {
		return tipsBiz;
	}

	public void setTipsBiz(TipsBiz tipsBiz) {
		this.tipsBiz = tipsBiz;
	}
}
