package com.iwami.iwami.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.UserDao;
import com.iwami.iwami.app.model.Login;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.model.UserRole;

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
	public List<User> getUserByIds(Set<Long> uids) {
		return getJdbcTemplate().query("select id, current_prize, exchange_prize, last_cell_phone_1, last_alipay_account, last_bank_account, "
				+ "last_bank_name, last_bank_no, last_address, last_cell_phone_2, last_name, name, uuid, alias, cell_phone, age, gender, job, address, add_time, b.lastmod_time as lastmod_time, b.lastmod_userid as lastmod_userid, b.isdel as isdel from " 
				+ SqlConstants.TABLE_USER + " a join " + SqlConstants.TABLE_USERINFO + " b on a.id = b.userid where a.id in (" + StringUtils.join(uids, ",") + ") and a.isdel = 0 and b.isdel = 0", new UserRowMapper());
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
				+ SqlConstants.TABLE_USER + " a join " + SqlConstants.TABLE_USERINFO + " b on a.id = b.userid where (b.cell_phone = ? or b.userid = ?) and a.isdel in (0, 1) and b.isdel in (0, 1)", new Object[]{key, key}, new UserRowMapper());
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
	public List<User> getAdminUsers(String key) {
		String sql = "select id, current_prize, exchange_prize, last_cell_phone_1, last_alipay_account, last_bank_account, "
				+ "last_bank_name, last_bank_no, last_address, last_cell_phone_2, last_name, name, uuid, alias, cell_phone, age, gender, job, address, add_time, b.lastmod_time as lastmod_time, b.lastmod_userid as lastmod_userid, b.isdel as isdel from " 
				+ SqlConstants.TABLE_USER + " a join " + SqlConstants.TABLE_USERINFO + " b on a.id = b.userid where a.isdel in (3,4) and b.isdel in (3,4) ";
		
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(key)){
			sql += " and (b.cell_phone = ? or b.name = ?)";
			params.add(key);
			params.add(key);
		}
		
		return  getJdbcTemplate().query(sql, params.toArray(), new UserRowMapper());
	
	}

	@Override
	public Map<Long, UserRole> getUserRoles(List<Long> ids) {
		final Map<Long, UserRole> roles = new HashMap<Long, UserRole>();
		List<UserRole> tmp = getJdbcTemplate().query("select userid, name, password, role, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_USERROLE + " where isdel = ? and userid in (" + StringUtils.join(ids.toArray(), ",") + ")",
				new Object[]{IWamiConstants.ACTIVE}, new UserRoleMapper());
		
		if(tmp != null && tmp.size() > 0)
			for(UserRole role : tmp)
				roles.put(role.getUserid(), role);
		
		return roles;
	}

	@Override
	public boolean addAdminUser(final User user) {
		KeyHolder holder = new GeneratedKeyHolder();
	    int result = getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				return con.prepareStatement("insert into " + SqlConstants.TABLE_USER + "(current_prize, exchange_prize, lastmod_time, lastmod_userid, isdel) values(0, 0, now(), " + user.getLastmodUserid() + ", 3)", Statement.RETURN_GENERATED_KEYS);
			}
		}, holder);
	    
		if(result >= 0){
			user.setId(holder.getKey().longValue());
			return true;
		}
		return false;
	}

	@Override
	public boolean addAdminUserInfo(User user) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_USERINFO + "(userid, name, uuid, alias, cell_phone, add_time, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, ?, now(), now(), ?, ?)", 
				new Object[]{user.getId(), user.getName(), StringUtils.EMPTY, StringUtils.EMPTY, user.getCellPhone(), user.getLastmodUserid(), IWamiConstants.ADMIN});
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean addAdminRole(UserRole role) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_USERROLE + "(userid, name, password, role, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, now(), ?, ?)", 
				new Object[]{role.getUserid(), role.getName(), role.getPassword(), role.getRole(), role.getLastModUserid(), IWamiConstants.ACTIVE});
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean modAdminUser(User user) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_USER + " set lastmod_time = now(), lastmod_userid = ?, isdel = ? where id = ?", 
				new Object[]{user.getLastmodUserid(), user.getIsdel(), user.getId()});
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean modAdminRole(UserRole role) {
		String sql = "update " + SqlConstants.TABLE_USERROLE + " set name = ?, role = ?, lastmod_time = now(), lastmod_userid = ?, isdel = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(role.getName());
		params.add(role.getRole());
		params.add(role.getLastModUserid());
		params.add(role.getIsdel());
		if(StringUtils.isNotBlank(role.getPassword())){
			sql += ", password = ?";
			params.add(role.getPassword());
		}
		sql += " where userid = ?";
		params.add(role.getUserid());
		
		int count = getJdbcTemplate().update(sql, params.toArray());
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean modAdminUserInfo(User user) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_USERINFO + " set name = ?, cell_phone = ?, lastmod_time = now(), lastmod_userid = ?, isdel = ? where userid = ?", 
				new Object[]{user.getName(), user.getCellPhone(), user.getLastmodUserid(), user.getIsdel(), user.getId()});
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean delAdminUser(long userid, long adminid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_USER + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{4, adminid, userid});
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean delAdminUserInfo(long userid, long adminid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_USERINFO + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where userid = ?", 
				new Object[]{4, adminid, userid});
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean delAdminRole(long userid, long adminid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_USERROLE + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where userid = ?", 
				new Object[]{IWamiConstants.INACTIVE, adminid, userid});
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public UserRole getUserRoleByLoginNameNPwd(String loginname, String password) {
		List<UserRole> roles = getJdbcTemplate().query("select userid, name, password, role, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_USERROLE + " where isdel = ? and name = ? and password = ?",
				new Object[]{IWamiConstants.ACTIVE, loginname, password}, new UserRoleMapper());
		
		if(roles != null && roles.size() > 0)
			return roles.get(0);
		else
			return null;
	}

	@Override
	public void addLogin(Login login) {
		getJdbcTemplate().update("insert into " + SqlConstants.TABLE_LOGIN + "(userid, add_time) values(?, now())", new Object[]{login.getUserid()});
	}

	@Override
	public Login getLogin(long adminid) {
		List<Login> data = getJdbcTemplate().query("select userid, add_time from " + SqlConstants.TABLE_LOGIN + " where userid = ? order by add_time desc limit 1", new Object[]{adminid}, new RowMapper<Login>(){

			@Override
			public Login mapRow(ResultSet rs, int rowNum) throws SQLException {
				Login login = new Login();
				login.setUserid(rs.getLong("userid"));
				Timestamp ts = rs.getTimestamp("add_time");
				if(ts != null)
					login.setAddTime(new Date(ts.getTime()));
				return login;
			}
			
		});
		
		if(data != null && data.size() > 0)
			return data.get(0);
		else
			return null;
	}

	@Override
	public Map<Long, String> getAllAlias() {
		final Map<Long, String> result = new HashMap<Long, String>();
		
		getJdbcTemplate().query("select userid, alias from " + SqlConstants.TABLE_USERINFO + " where alias is not null and isdel = 0", new RowMapper<Integer>(){

			@Override
			public Integer mapRow(ResultSet rs, int index) throws SQLException {
				result.put(rs.getLong("userid"), rs.getString("alias"));
				
				return index;
			}
			
		});
		
		return result;
	}

	@Override
	public List<User> getUsers(Date start, Date end) {
		return getJdbcTemplate().query("select id, current_prize, exchange_prize, last_cell_phone_1, last_alipay_account, last_bank_account, "
				+ "last_bank_name, last_bank_no, last_address, last_cell_phone_2, last_name, name, uuid, alias, cell_phone, age, gender, job, address, add_time, b.lastmod_time as lastmod_time, b.lastmod_userid as lastmod_userid, b.isdel as isdel from " 
				+ SqlConstants.TABLE_USER + " a join " + SqlConstants.TABLE_USERINFO + " b on a.id = b.userid where a.isdel = 0 and b.isdel = 0 and add_time between ? and ?", new Object[]{start, end}, new UserRowMapper());
	}

	@Override
	public List<User> getChangedUsers(Date start, Date end) {
		return getJdbcTemplate().query("select id, current_prize, exchange_prize, last_cell_phone_1, last_alipay_account, last_bank_account, "
				+ "last_bank_name, last_bank_no, last_address, last_cell_phone_2, last_name, name, uuid, alias, cell_phone, age, gender, job, address, add_time, b.lastmod_time as lastmod_time, b.lastmod_userid as lastmod_userid, b.isdel as isdel from " 
				+ SqlConstants.TABLE_USER + " a join " + SqlConstants.TABLE_USERINFO + " b on a.id = b.userid where a.isdel = 0 and b.isdel = 0 and b.lastmod_time between ? and ?", new Object[]{start, end}, new UserRowMapper());
	}

}

class UserRoleMapper implements RowMapper<UserRole>{

	@Override
	public UserRole mapRow(ResultSet rs, int index)
			throws SQLException {
		UserRole role = new UserRole();
		role.setUserid(rs.getLong("userid"));
		role.setName(rs.getString("name"));
		role.setPassword(rs.getString("password"));
		role.setRole(rs.getLong("role"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			role.setLastModTime(new Date(ts.getTime()));
		role.setLastModUserid(rs.getLong("lastmod_userid"));
		
		return role;
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
