package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.TipsBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.model.Tips;

@AjaxClass
public class TipsAjax {

	private Log logger = LogFactory.getLog(getClass());

	private TipsBiz tipsBiz;

	@AjaxMethod(path = "tips.ajax")
	public Map<Object, Object> getTips(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			int type = -1;
			if (params.containsKey("type")) {
				type = Integer.parseInt(params.get("type"));
				if (type >= 0) {
					Tips tips = tipsBiz.getTips(type);
					String content = StringUtils.EMPTY;
					if(tips != null)
						content = tips.getContent();
					result.put("data", content);
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
