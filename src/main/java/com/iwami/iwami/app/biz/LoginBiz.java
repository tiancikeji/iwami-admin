package com.iwami.iwami.app.biz;

import com.iwami.iwami.app.model.UserRole;

public interface LoginBiz {

	public boolean checkLogin(long adminid);
	
	public boolean checkRole(long adminid, long type);

	public UserRole login(String loginname, String password);

}
