package com.iwami.iwami.app.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iwami.iwami.app.model.Login;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.model.UserRole;

public interface UserDao {
	
	public User getUserById(long id);

	public List<User> getUserByIds(Set<Long> uids);

	public User getUserByCellPhone(long cellPhone);

	public List<User> getUserByIdOCellPhone(long key);

	public boolean modifyUser(User user);

	public boolean modifyUserinfo(User user);

	public User getAdminById(long adminid);

	public List<User> getAdminUsers(String key);

	public Map<Long, UserRole> getUserRoles(List<Long> ids);

	public boolean addAdminUser(User user);

	public boolean addAdminUserInfo(User user);

	public boolean addAdminRole(UserRole role);

	public boolean modAdminRole(UserRole role);

	public boolean modAdminUserInfo(User user);

	public boolean modAdminUser(User user);

	public boolean delAdminUser(long userid, long adminid);

	public boolean delAdminUserInfo(long userid, long adminid);

	public boolean delAdminRole(long userid, long adminid);

	public UserRole getUserRoleByLoginNameNPwd(String loginname, String password);

	public void addLogin(Login login);

	public Login getLogin(long adminid);

	public Map<Long, String> getAllAlias();

	public List<User> getUsers(Date start, Date end);

	List<User> getChangedUsers(Date start, Date end);

}
