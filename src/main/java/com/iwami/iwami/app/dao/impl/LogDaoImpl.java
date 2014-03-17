package com.iwami.iwami.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.LogDao;
import com.iwami.iwami.app.model.Log;

public class LogDaoImpl extends JdbcDaoSupport implements LogDao {
	
	public List<Log> getLogs(Date start, Date end){
		return getJdbcTemplate().query("select userid, type, add_time from " + SqlConstants.TABLE_REQUEST_LOG + " where add_time between ? and ?", 
				new Object[]{start, end}, new RowMapper<Log>(){

			@Override
			public Log mapRow(ResultSet rs, int index) throws SQLException {
				Log log = new Log();
				log.setType(rs.getInt("type"));
				log.setUserid(rs.getLong("userid"));
				Timestamp ts = rs.getTimestamp("add_time");
				if(ts != null)
					log.setAddTime(new Date(ts.getTime()));
				return log;
			}
			
		});
	}
	
	public List<Log> getLogsByType(int type, Date start, Date end){
		return getJdbcTemplate().query("select userid, type, add_time from " + SqlConstants.TABLE_REQUEST_LOG + " where type = ? and add_time between ? and ?", 
				new Object[]{type, start, end}, new RowMapper<Log>(){

			@Override
			public Log mapRow(ResultSet rs, int index) throws SQLException {
				Log log = new Log();
				log.setType(rs.getInt("type"));
				log.setUserid(rs.getLong("userid"));
				Timestamp ts = rs.getTimestamp("add_time");
				if(ts != null)
					log.setAddTime(new Date(ts.getTime()));
				return log;
			}
			
		});
	}

}
