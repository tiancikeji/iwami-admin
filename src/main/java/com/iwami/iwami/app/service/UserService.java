package com.iwami.iwami.app.service;

import java.util.List;

import com.iwami.iwami.app.model.User;

public interface UserService {

	public User getUserById(long id);

	public User getUserByCellPhone(long cellPhone);

	public List<User> getUserByIdOCellPhone(long key);

	public boolean modifyUser(User user);

	public boolean modifyUserinfo(User user);

	public User getAdminById(long adminid);
}
