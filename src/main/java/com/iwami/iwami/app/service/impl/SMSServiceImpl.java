package com.iwami.iwami.app.service.impl;

import java.util.Date;

import com.iwami.iwami.app.sal.SMSSAL;
import com.iwami.iwami.app.service.SMSService;

public class SMSServiceImpl implements SMSService {
	
	private SMSSAL smsSAL;

	@Override
	public boolean sendJPushSMS(String cellPhone, long id, Date addTime, Date lastModTime, int count) {
		return smsSAL.sendJPushSMS(cellPhone, id, addTime, lastModTime, count);
	}

	@Override
	public boolean sendTaskSMS(String cellPhone, String name, String reason, Date startdate, Date enddate, int total, int count) {
		return smsSAL.sendTaskSMS(cellPhone, name, reason, startdate, enddate, total, count);
	}

	public SMSSAL getSmsSAL() {
		return smsSAL;
	}

	public void setSmsSAL(SMSSAL smsSAL) {
		this.smsSAL = smsSAL;
	}

}
