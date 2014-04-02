package com.iwami.iwami.app.biz;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.model.UserRole;

public interface UserBiz {
	
	public User getUserById(long userid);

	public List<User> getUserByIds(Set<Long> uids);
	
	public User getAdminById(long adminid);

	public User getUserByCellPhone(long cellPhone);

	public List<User> getUserByIdOCellPhone(long key);

	public boolean canOpt(long adminid, long userid);

	public boolean modifyUser(User user);

	public List<User> getAdminUsers(String key);

	public Map<Long, UserRole> getUserRoles(List<Long> ids);

	public boolean addAdmin(User user, UserRole role);

	public boolean modAdmin(User user, UserRole role);

	public boolean delAdmin(long userid, long adminid);

	public boolean modRole(UserRole role);

}
