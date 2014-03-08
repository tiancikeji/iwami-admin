package com.iwami.iwami.app.service;

public interface SMSService {
	public boolean sendSMS(String cellPhone, String msg);
}
