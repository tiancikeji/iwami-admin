package com.iwami.iwami.app.service.impl;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.iwami.iwami.app.dao.UserDao;
import com.iwami.iwami.app.model.Code;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.service.UserService;

public class UserServiceImpl implements UserService {
	
	private UserDao userDao;

	@Override
	public User getUserById(long id) {
		return userDao.getUserById(id);
	}

	@Override
	public boolean subUserCurrentNExchangePrize(long userid, int prize) {
		return userDao.subUserCurrentNExchangePrize(userid, prize);
	}

	@Override
	public boolean addCode(Code code) {
		return userDao.addCode(code);
	}

	@Override
	public Code getCode(long cellPhone, String code, Date start) {
		return userDao.getCode(cellPhone, code, start);
	}

	@Override
	public User getUserByCellPhone(long cellPhone) {
		return userDao.getUserByCellPhone(cellPhone);
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean addUser4Register(User user) {
		if(userDao.newUser4Register(user) && userDao.addUserInfo4Register(user))
			return true;
		else
			throw new RuntimeException("Error in saving user into db.");
	}

	@Override
	public boolean updateUser4Register(User user) {
		if(userDao.updateUser4Register(user))
			return true;
		else
			throw new RuntimeException("Error in updating user into db.");
	}

	@Override
	public boolean modifyUserInfo4Register(User user) {
		return userDao.modifyUserInfo4Register(user);
	}

	@Override
	public void addUserCurrentPrize(long userid, int prize) {
		userDao.addUserCurrentPrize(userid, prize);
	}

	@Override
	public boolean updateUser4ExpressExchange(long userid, int allPrize, long cellPhone, String address, String name) {
		return userDao.updateUser4ExpressExchange(userid, allPrize, cellPhone, address, name);
	}

	@Override
	public boolean updateUser4AlipayExchange(long id, int prize, String aliAccount) {
		return userDao.updateUser4AlipayExchange(id, prize, aliAccount);
	}

	@Override
	public boolean updateUser4BankExchange(long id, int prize, String bankAccount, String bankName, long bankNo) {
		return userDao.updateUser4BankExchange(id, prize, bankAccount, bankName, bankNo);
	}

	@Override
	public boolean updateUser4MobileExchange(long id, int prize, long cellPhone) {
		return userDao.updateUser4MobileExchange(id, prize, cellPhone);
	}

	@Override
	public boolean updateUser4OfflineExchange(long id, int prize) {
		return userDao.updateUser4OfflineExchange(id, prize);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
