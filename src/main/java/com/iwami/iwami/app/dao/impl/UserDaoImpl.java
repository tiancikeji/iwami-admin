package com.iwami.iwami.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.UserDao;
import com.iwami.iwami.app.model.User;

public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

	@Override
	public User getUserById(long id) {
		List<User> users = getJdbcTemplate().query("select id, current_prize, exchange_prize, last_cell_phone_1, last_alipay_account, last_bank_account, "
				+ "last_bank_name, last_bank_no, last_address, last_cell_phone_2, last_name, name, uuid, alias, cell_phone, age, gender, job, address, add_time, b.lastmod_time as lastmod_time, b.lastmod_userid as lastmod_userid, b.isdel as isdel from " 
				+ SqlConstants.TABLE_USER + " a join " + SqlConstants.TABLE_USERINFO + " b on a.id = b.userid where a.id = ? and a.isdel = 0 and b.isdel = 0", new Object[]{id}, new UserRowMapper());
		if(users != null && users.size() > 0)
			return users.get(0);
		else
			return null;
	}

	@Override
	public User getAdminById(long adminid) {
		List<User> users = getJdbcTemplate().query("select id, current_prize, exchange_prize, last_cell_phone_1, last_alipay_account, last_bank_account, "
				+ "last_bank_name, last_bank_no, last_address, last_cell_phone_2, last_name, name, uuid, alias, cell_phone, age, gender, job, address, add_time, b.lastmod_time as lastmod_time, b.lastmod_userid as lastmod_userid, b.isdel as isdel from " 
				+ SqlConstants.TABLE_USER + " a join " + SqlConstants.TABLE_USERINFO + " b on a.id = b.userid where a.id = ? and a.isdel = 3 and b.isdel = 3", new Object[]{adminid}, new UserRowMapper());
		if(users != null && users.size() > 0)
			return users.get(0);
		else
			return null;
	}

	@Override
	public User getUserByCellPhone(long cellPhone) {
		List<User> users = getJdbcTemplate().query("select id, current_prize, exchange_prize, last_cell_phone_1, last_alipay_account, last_bank_account, "
				+ "last_bank_name, last_bank_no, last_address, last_cell_phone_2, last_name, name, uuid, alias, cell_phone, age, gender, job, address, add_time, b.lastmod_time as lastmod_time, b.lastmod_userid as lastmod_userid, b.isdel as isdel from " 
				+ SqlConstants.TABLE_USER + " a join " + SqlConstants.TABLE_USERINFO + " b on a.id = b.userid where b.cell_phone = ? and a.isdel = 0 and b.isdel = 0", new Object[]{cellPhone}, new UserRowMapper());
		if(users != null && users.size() > 0)
			return users.get(0);
		else
			return null;
	}

	@Override
	public List<User> getUserByIdOCellPhone(long key) {
		return getJdbcTemplate().query("select id, current_prize, exchange_prize, last_cell_phone_1, last_alipay_account, last_bank_account, "
				+ "last_bank_name, last_bank_no, last_address, last_cell_phone_2, last_name, name, uuid, alias, cell_phone, age, gender, job, address, add_time, b.lastmod_time as lastmod_time, b.lastmod_userid as lastmod_userid, b.isdel as isdel from " 
				+ SqlConstants.TABLE_USER + " a join " + SqlConstants.TABLE_USERINFO + " b on a.id = b.userid where (b.cell_phone = ? or b.userid = ?) and a.isdel in (0, 1) and b.isdel = in (0, 1)", new Object[]{key, key}, new UserRowMapper());
	}

	@Override
	public boolean modifyUser(User user) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_USER + " set current_prize = ?, exchange_prize = ?, last_cell_phone_1 = ?, last_alipay_account = ?, last_bank_name = ?, last_bank_account = ?, last_bank_no = ?, last_address = ?, last_cell_phone_2 = ?, last_name = ?, lastmod_time = now(), lastmod_userid = ?, isdel = ? where id = ?", 
				new Object[]{user.getCurrentPrize(), user.getExchangePrize(), user.getLastCellPhone1(), user.getLastAlipayAccount(), user.getLastBankName(), user.getLastBankAccount(), user.getLastBankNo(), user.getLastAddres(), user.getLastCellPhone2(), user.getLastName(), user.getLastmodUserid(), user.getIsdel(), user.getId()});
		return count > 0;
	}

	@Override
	public boolean modifyUserinfo(User user) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_USERINFO + " set name = ?, cell_phone = ?, age = ?, gender = ?, job = ?, address = ?, lastmod_time = now(), lastmod_userid = ?, isdel = ? where userid = ?", 
				new Object[]{user.getName(), user.getCellPhone(), user.getAge(), user.getGender(), user.getJob(), user.getAddress(), user.getLastmodUserid(), user.getIsdel(), user.getId()});
		return count > 0;
	}

}

class UserRowMapper implements RowMapper<User>{

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setAddress(rs.getString("address"));
		user.setAge(rs.getInt("age"));
		user.setCellPhone(rs.getLong("cell_phone"));
		user.setCurrentPrize(rs.getInt("current_prize"));
		user.setExchangePrize(rs.getInt("exchange_prize"));
		user.setGender(rs.getInt("gender"));
		user.setId(rs.getLong("id"));
		user.setJob(rs.getString("job"));
		user.setLastAddres(rs.getString("last_address"));
		user.setLastAlipayAccount(rs.getString("last_alipay_account"));
		user.setLastBankAccount(rs.getString("last_bank_account"));
		user.setLastBankNo(rs.getLong("last_bank_no"));
		user.setLastBankName(rs.getString("last_bank_name"));
		user.setLastCellPhone1(rs.getLong("last_cell_phone_1"));
		user.setLastCellPhone2(rs.getLong("last_cell_phone_2"));
		user.setLastName(rs.getString("last_name"));
		user.setName(rs.getString("name"));
		user.setUuid(rs.getString("uuid"));
		user.setAlias(rs.getString("alias"));
		Timestamp ts = rs.getTimestamp("add_time");
		if(ts != null)
			user.setAddTime(new Date(ts.getTime()));
		ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			user.setLastmodTime(new Date(ts.getTime()));
		user.setLastmodUserid(rs.getLong("lastmod_userid"));
		user.setIsdel(rs.getInt("isdel"));
		return user;
	}
	
}
