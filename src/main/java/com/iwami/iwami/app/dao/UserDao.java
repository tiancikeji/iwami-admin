package com.iwami.iwami.app.dao;

import java.util.Date;

import com.iwami.iwami.app.model.Code;
import com.iwami.iwami.app.model.User;

public interface UserDao {
	
	public User getUserById(long id);

	public boolean subUserCurrentNExchangePrize(long userid, int prize);

	public boolean addCode(Code code);

	public Code getCode(long cellPhone, String code, Date start);

	public User getUserByCellPhone(long cellPhone);

	public boolean newUser4Register(User user);

	public boolean addUserInfo4Register(User user);

	public boolean updateUser4Register(User user);

	public boolean modifyUserInfo4Register(User user);

	public void addUserCurrentPrize(long userid, int prize);

	public boolean updateUser4ExpressExchange(long userid, int allPrize, long cellPhone, String address, String name);

	public boolean updateUser4AlipayExchange(long id, int prize, String aliAccount);

	public boolean updateUser4BankExchange(long id, int prize, String bankAccount, String bankName, long bankNo);

	public boolean updateUser4MobileExchange(long id, int prize, long cellPhone);

	public boolean updateUser4OfflineExchange(long id, int prize);

}
