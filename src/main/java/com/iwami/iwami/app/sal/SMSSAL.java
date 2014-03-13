package com.iwami.iwami.app.sal;

import java.util.Date;

public interface SMSSAL {

	public boolean sendJPushSMS(String cellPhone, long id, Date addTime, Date lastModTime, int count);

	public boolean sendTaskSMS(String cellPhone, String name, String reason, Date startdate, Date enddate, int total, int count);
}
