package com.iwami.iwami.app.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.model.Onstart;
import com.iwami.iwami.app.model.User;

public interface OnstartService {

	public boolean logOnstart(Onstart onstart);

	public Map<Long, Date> getLastLogins(List<Long> userids);

	public Map<Long, Date> getCreateTimes(List<User> users);

}
