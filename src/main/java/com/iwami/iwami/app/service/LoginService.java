package com.iwami.iwami.app.service;

import com.iwami.iwami.app.model.UserRole;

public interface LoginService {

	public boolean checkLogin(long adminid);

	public boolean checkRole(long adminid, long type);

	public UserRole login(String loginname, String password);

}
