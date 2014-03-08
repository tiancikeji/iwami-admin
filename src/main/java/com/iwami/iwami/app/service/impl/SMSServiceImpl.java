package com.iwami.iwami.app.service.impl;

import com.iwami.iwami.app.sal.SMSSAL;
import com.iwami.iwami.app.service.SMSService;

public class SMSServiceImpl implements SMSService {
	
	private SMSSAL smsSAL;

	@Override
	public boolean sendSMS(String cellPhone, String msg) {
		return smsSAL.sendSMS(cellPhone, msg);
	}

	public SMSSAL getSmsSAL() {
		return smsSAL;
	}

	public void setSmsSAL(SMSSAL smsSAL) {
		this.smsSAL = smsSAL;
	}

}
