package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;

@AjaxClass
public class FileAjax {
	
	private Log logger = LogFactory.getLog(getClass());

	@AjaxMethod(path = "ADD/file.ajax")
	public Map<Object, Object> addFile(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("data")){
				String data = StringUtils.trimToEmpty(params.get("data"));
				if(StringUtils.isNotBlank(data)){
					String url = null;
					//TODO upload & set url here...
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					result.put("url", url);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in download", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		
		return result;
	}

}
