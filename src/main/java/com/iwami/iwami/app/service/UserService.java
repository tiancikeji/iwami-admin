package com.iwami.iwami.app.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.model.UserRole;

public interface UserService {

	public User getUserById(long id);

	public User getUserByCellPhone(long cellPhone);

	public List<User> getUserByIdOCellPhone(long key);

	public boolean modifyUser(User user);

	public boolean modifyUserinfo(User user);

	public User getAdminById(long adminid);

	public List<User> getAdminUsers();

	public Map<Long, UserRole> getUserRoles(List<Long> ids);

	public boolean addAdminUser(User user);

	public boolean addAdminUserInfo(User user);

	public boolean addAdminRole(UserRole role);

	public boolean modAdminUserInfo(User user);

	public boolean modAdminUser(User user);

	public boolean modAdminRole(UserRole role);

	public boolean delAdminUser(long userid, long adminid);

	public boolean delAdminUserInfo(long userid, long adminid);

	public boolean delAdminRole(long userid, long adminid);

	public Map<Long, String> getAllAlias();

	public List<User> getUsers(Date start, Date end);
}
