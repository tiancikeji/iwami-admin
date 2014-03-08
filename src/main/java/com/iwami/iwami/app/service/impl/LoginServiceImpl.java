package com.iwami.iwami.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.dao.UserDao;
import com.iwami.iwami.app.model.Login;
import com.iwami.iwami.app.model.UserRole;
import com.iwami.iwami.app.service.LoginService;
import com.iwami.iwami.app.util.LocalCaches;

public class LoginServiceImpl implements LoginService {
	
	private UserDao userDao;
	
	private long expireTime = 1200000;

	@Override
	public boolean checkLogin(long adminid) {
		Login login = (Login)LocalCaches.get(IWamiConstants.CACHE_LOGIN_KEY + adminid, System.currentTimeMillis(), expireTime);
		
		if(login == null){
			login = userDao.getLogin(adminid);
			
			if(login != null){
				long left = expireTime - (System.currentTimeMillis() - login.getAddTime().getTime());
				if(login != null && left > 0){
					LocalCaches.set(IWamiConstants.CACHE_LOGIN_KEY + login.getUserid(), login, left);
					return true;
				} else
					return false;
			} else
				return false;
		} else
			return true;
	}

	@Override
	public boolean checkRole(long adminid, long type) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(adminid);
		
		Map<Long, UserRole> roles = userDao.getUserRoles(ids);
		if(roles != null && roles.containsKey(adminid)){
			UserRole role = roles.get(adminid);
			
			return (role.getRole() & type) > 0;
		}
		
		return false;
	}

	@Override
	public UserRole login(String loginname, String password) {
		UserRole role = userDao.getUserRoleByLoginNameNPwd(loginname, password);
		
		if(role != null){
			Login login = new Login();
			login.setUserid(role.getUserid());
			
			userDao.addLogin(login);
			
			LocalCaches.set(IWamiConstants.CACHE_LOGIN_KEY + role.getUserid(), login, expireTime);
		}
		
		return role;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

}
