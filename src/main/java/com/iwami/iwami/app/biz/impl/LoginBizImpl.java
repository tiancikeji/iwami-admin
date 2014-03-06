package com.iwami.iwami.app.biz.impl;

import com.iwami.iwami.app.biz.LoginBiz;
import com.iwami.iwami.app.exception.UserNotLoginException;
import com.iwami.iwami.app.model.UserRole;
import com.iwami.iwami.app.service.LoginService;

public class LoginBizImpl implements LoginBiz {
	
	private LoginService loginService;

	@Override
	public boolean checkLogin(long adminid) {
		if(loginService.checkLogin(adminid))
			return true;
		else
			throw new UserNotLoginException();
	}

	@Override
	public boolean checkRole(long adminid, long type) {
		return loginService.checkRole(adminid, type);
	}

	@Override
	public UserRole login(String loginname, String password) {
		return loginService.login(loginname, password);
	}

	public LoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	
}
