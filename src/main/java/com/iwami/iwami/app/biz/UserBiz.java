package com.iwami.iwami.app.biz;

import java.util.List;

import com.iwami.iwami.app.model.User;

public interface UserBiz {
	
	public User getUserById(long userid);
	
	public User getAdminById(long adminid);

	public User getUserByCellPhone(long cellPhone);

	public List<User> getUserByIdOCellPhone(long key);

	public boolean canOpt(long adminid, long userid);

	public boolean modifyUser(User user);

}
