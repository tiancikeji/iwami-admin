package com.iwami.iwami.app.service.impl;

import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.dao.UserDao;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.model.UserRole;
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

	@Override
	public List<User> getAdminUsers() {
		return userDao.getAdminUsers();
	}

	@Override
	public Map<Long, UserRole> getUserRoles(List<Long> ids) {
		return userDao.getUserRoles(ids);
	}

	@Override
	public boolean addAdminUser(User user) {
		return userDao.addAdminUser(user);
	}

	@Override
	public boolean addAdminUserInfo(User user) {
		return userDao.addAdminUserInfo(user);
	}

	@Override
	public boolean addAdminRole(UserRole role) {
		return userDao.addAdminRole(role);
	}

	@Override
	public boolean modAdminUserInfo(User user) {
		return userDao.modAdminUserInfo(user);
	}

	@Override
	public boolean modAdminUser(User user) {
		return userDao.modAdminUser(user);
	}

	@Override
	public boolean modAdminRole(UserRole role) {
		return userDao.modAdminRole(role);
	}

	@Override
	public boolean delAdminUser(long userid, long adminid) {
		return userDao.delAdminUser(userid, adminid);
	}

	@Override
	public boolean delAdminUserInfo(long userid, long adminid) {
		return userDao.delAdminUserInfo(userid, adminid);
	}

	@Override
	public boolean delAdminRole(long userid, long adminid) {
		return userDao.delAdminRole(userid, adminid);
	}

	@Override
	public Map<Long, String> getAllAlias() {
		return userDao.getAllAlias();
	}

}
