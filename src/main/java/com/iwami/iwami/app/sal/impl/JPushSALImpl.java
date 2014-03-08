package com.iwami.iwami.app.sal.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.CustomMessageParams;
import cn.jpush.api.push.MessageResult;
import cn.jpush.api.push.ReceiverTypeEnum;

import com.iwami.iwami.app.sal.JPushSAL;

public class JPushSALImpl implements JPushSAL {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private JPushClient jpush;
	
	@Override
	public int sendCustomMessageWithAlias(int sendNo, String alias, String title, String content) {
		if(logger.isDebugEnabled())
			logger.debug("sendCustomMessageWithAlias : <" + sendNo + "," + alias + "," + title + "," + content + ">");
		
		CustomMessageParams params = new CustomMessageParams();
		params.setReceiverType(ReceiverTypeEnum.ALIAS);
		params.setReceiverValue(alias);
		
		MessageResult result = jpush.sendCustomMessage(title, content, params, null);
		
		if(logger.isDebugEnabled())
			logger.debug("received >> " + result.responseResult.responseContent);
		
		int status = -1;
		if (result.isResultOK()) {
			logger.info("msgResult - " + result);
			logger.info("messageId - " + result.getMessageId());
			status = 0;
		} else {
		    if (result.getErrorCode() > 0) {
		    	status = result.getErrorCode();
		        // 业务异常
		    	logger.warn("Service error - ErrorCode: " + result.getErrorCode() + ", ErrorMessage: " + result.getErrorMessage());
		    } else {
		        // 未到达 JPush 
		    	logger.error("Other excepitons - " + result.responseResult.exceptionString);
		    	status = -1;
		    }
		}
		
		return status;
	}

	public JPushClient getJpush() {
		return jpush;
	}

	public void setJpush(JPushClient jpush) {
		this.jpush = jpush;
	}
}
