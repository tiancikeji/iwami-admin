package com.iwami.iwami.app.service;

import java.util.Date;

public interface SMSService {

	boolean sendJPushSMS(String cellPhone, long id, Date addTime, Date lastModTime, int count);

	boolean sendTaskSMS(String cellPhone, String name, String reason, Date startdate, Date enddate, int total, int count);
}
