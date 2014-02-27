package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;


@AjaxClass
public class MonitorAjax {
	
	@AjaxMethod(path = "monitor.ajax")
	public Map<Object, Object> monitor(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
		return result;
	}

}
