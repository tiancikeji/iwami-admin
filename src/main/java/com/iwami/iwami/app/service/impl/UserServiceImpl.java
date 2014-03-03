package com.iwami.iwami.app.service.impl;

import java.util.List;

import com.iwami.iwami.app.dao.UserDao;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.service.UserService;

public class UserServiceImpl implements UserService {
	
	private UserDao userDao;

	@Override
	public User getUserById(long id) {
		return userDao.getUserById(id);
	}

	@Override
	public User getAdminById(long adminid) {
		return userDao.getAdminById(adminid);
	}

	@Override
	public User getUserByCellPhone(long cellPhone) {
		return userDao.getUserByCellPhone(cellPhone);
	}

	@Override
	public List<User> getUserByIdOCellPhone(long key) {
		return userDao.getUserByIdOCellPhone(key);
	}

	@Override
	public boolean modifyUser(User user) {
		return userDao.modifyUser(user);
	}

	@Override
	public boolean modifyUserinfo(User user) {
		return userDao.modifyUserinfo(user);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
