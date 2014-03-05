package com.iwami.iwami.app.biz.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.iwami.iwami.app.biz.UserBiz;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.model.UserRole;
import com.iwami.iwami.app.service.OnstartService;
import com.iwami.iwami.app.service.UserService;

public class UserBizImpl implements UserBiz {
	
	private UserService userService;
	
	private OnstartService onstartService;
	
	private int verifyCodeLength = 4;
	
	public User getUserById(long userid){
		return userService.getUserById(userid);
	}

	public User getAdminById(long adminid) {
		return userService.getAdminById(adminid);
	}

	@Override
	public User getUserByCellPhone(long cellPhone) {
		return userService.getUserByCellPhone(cellPhone);
	}

	@Override
	public List<User> getUserByIdOCellPhone(long key) {
		List<User> users = userService.getUserByIdOCellPhone(key);
		
		if(users != null && users.size() > 0){
			Collections.sort(users, new Comparator<User>() {

				@Override
				public int compare(User u1, User u2) {
					return (int)(u1.getId() - u2.getId());
				}
			});
		
			List<Long> userids = new ArrayList<Long>();
			for(User user : users)
				userids.add(user.getId());
			
			// create time...
			Map<Long, Date> createTimes = onstartService.getCreateTimes(users);
			if(createTimes != null && createTimes.size() > 0)
				for(User user : users)
					if(createTimes.containsKey(user.getId()))
						user.setCreateTime(createTimes.get(user.getId()));
			
			// last login...
			Map<Long, Date> lastLogins = onstartService.getLastLogins(userids);
			if(lastLogins != null && lastLogins.size() > 0)
				for(User user : users)
					if(lastLogins.containsKey(user.getId()))
						user.setLastLoginTime(lastLogins.get(user.getId()));
		}
		
		return users;
	}

	@Override
	public boolean canOpt(long adminid, long userid) {
		User admin = getAdminById(adminid);
		User user = getUserById(userid);
		
		if(admin != null && user != null)
			return _canOpt();
			
		return false;
	}

	private boolean _canOpt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean modifyUser(User user) {
		if(userService.modifyUser(user))
			if(!userService.modifyUserinfo(user))
				throw new RuntimeException("error in modify user, roolback");
			else
				return true;
		else
			return false;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public int getVerifyCodeLength() {
		return verifyCodeLength;
	}

	public void setVerifyCodeLength(int verifyCodeLength) {
		this.verifyCodeLength = verifyCodeLength;
	}

	public OnstartService getOnstartService() {
		return onstartService;
	}

	public void setOnstartService(OnstartService onstartService) {
		this.onstartService = onstartService;
	}

	// admin user here...
	@Override
	public List<User> getAdminUsers() {
		return userService.getAdminUsers();
	}

	@Override
	public Map<Long, UserRole> getUserRoles(List<Long> ids) {
		return userService.getUserRoles(ids);
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean addAdmin(User user, UserRole role) {
		if(userService.addAdminUser(user)){
			role.setUserid(user.getId());
			if(userService.addAdminUserInfo(user) && userService.addAdminRole(role))
				return true;
			else
				throw new RuntimeException("failed add admin, so rollback");
		} else
			return false;
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean modAdmin(User user, UserRole role) {
		if(userService.modAdminUserInfo(user))
			if(userService.modAdminRole(role))
				return true;
			else
				throw new RuntimeException("failed mod admin, so rollback");
		else
			return false;
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean delAdmin(long userid, long adminid) {
		if(userService.delAdminUser(userid, adminid))
			if(userService.delAdminUserInfo(userid, adminid) && userService.delAdminRole(userid, adminid))
				return true;
			else
				throw new RuntimeException("failed mod admin, so rollback");
		else
			return false;
	}
}
