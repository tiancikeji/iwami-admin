package com.iwami.iwami.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.ContactDao;
import com.iwami.iwami.app.model.Contact;

public class ContactDaoImpl extends JdbcDaoSupport implements ContactDao{

	@Override
	public Contact getContact() {
		List<Contact> contacts = getJdbcTemplate().query("select id, phone1, email1, domain, qq, qqgroup, phone2, email2, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_CONTACT + " where isdel = 0 order by lastmod_time desc limit 1", new RowMapper<Contact>() {

			@Override
			public Contact mapRow(ResultSet rs, int index) throws SQLException {
				Contact contact = new Contact();
				contact.setId(rs.getLong("id"));
				contact.setPhone1(rs.getString("phone1"));
				contact.setEmail1(rs.getString("email1"));
				contact.setDomain(rs.getString("domain"));
				contact.setQq(rs.getLong("qq"));
				contact.setQqgroup(rs.getString("qqgroup"));
				contact.setPhone2(rs.getString("phone2"));
				contact.setEmail2(rs.getString("email2"));
				contact.setLastmodTime(rs.getDate("lastmod_time"));
				contact.setLastmodUserid(rs.getLong("lastmod_userid"));
				return contact;
			}
		});
		
		if(contacts != null && contacts.size() > 0)
			return contacts.get(0);
		else 
			return null;
	}

	@Override
	public boolean delContact(long id) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_CONTACT + " set isdel = 1 where id = ?", new Object[]{id});
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean addContact(Contact contact) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_CONTACT + "(phone1, email1, domain,qq, qqgroup, phone2, email2,lastmod_time, lastmod_userid, isdel) values(?,?,?,?,?,?,?,now(),?,0)", 
				new Object[]{contact.getPhone1(), contact.getEmail1(), contact.getDomain(), contact.getQq(), contact.getQqgroup(), contact.getPhone2(), contact.getEmail2(), contact.getLastmodUserid()});
		if(count > 0)
			return true;
		else
			return false;
	}

}
