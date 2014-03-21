package com.iwami.iwami.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.ApkDao;
import com.iwami.iwami.app.model.Apk;

public class ApkDaoImpl extends JdbcDaoSupport implements ApkDao {

	@Override
	public Apk getApk() {
		List<Apk> apks = getJdbcTemplate().query("select id, version, url, `force`, `desc`, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_APK + " where isdel = 0 order by lastmod_time desc limit 1", new RowMapper<Apk>() {
			@Override
			public Apk mapRow(ResultSet rs, int index) throws SQLException {
				Apk apk = new Apk();
				apk.setId(rs.getLong("id"));
				apk.setVersion(rs.getString("version"));
				apk.setUrl(rs.getString("url"));
				apk.setForce(rs.getInt("force"));
				apk.setDesc(rs.getString("desc"));
				Timestamp ts = rs.getTimestamp("lastmod_time");
				if(ts != null)
					apk.setLastmodTime(new Date(ts.getTime()));
				apk.setLastmodUserid(rs.getLong("lastmod_userid"));
				return apk;
			}
		});
		
		if(apks != null && apks.size() > 0)
			return apks.get(0);
		else
			return null;
	}

	@Override
	public List<Apk> getApks() {
		return getJdbcTemplate().query("select id, version, url, `force`, `desc`, lastmod_time, lastmod_userid, isdel from " + SqlConstants.TABLE_APK, new RowMapper<Apk>() {
			@Override
			public Apk mapRow(ResultSet rs, int index) throws SQLException {
				Apk apk = new Apk();
				apk.setId(rs.getLong("id"));
				apk.setVersion(rs.getString("version"));
				apk.setUrl(rs.getString("url"));
				apk.setForce(rs.getInt("force"));
				apk.setDesc(rs.getString("desc"));
				Timestamp ts = rs.getTimestamp("lastmod_time");
				if(ts != null)
					apk.setLastmodTime(new Date(ts.getTime()));
				apk.setLastmodUserid(rs.getLong("lastmod_userid"));
				apk.setIsdel(rs.getInt("isdel"));
				return apk;
			}
		});
	}

	@Override
	public boolean addApk(final Apk apk) {
		KeyHolder holder = new GeneratedKeyHolder();
		int count = getJdbcTemplate().update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement("insert into " + SqlConstants.TABLE_APK + "(version, url, `force`, `desc`, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, now(), ?, ?)", Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, apk.getVersion());
				ps.setObject(2, apk.getUrl());
				ps.setObject(3, apk.getForce());
				ps.setObject(4, apk.getDesc());
				ps.setObject(5, apk.getLastmodUserid());
				ps.setObject(6, IWamiConstants.ACTIVE);
				return ps;
			}
		}, holder);
		
		if(count > 0 && holder.getKey() != null){
			apk.setId(holder.getKey().longValue());
			return true;
		} else
			return false;
	}

	@Override
	public boolean modApk(Apk apk) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_APK + " set version = ?, url = ?, `force` = ?, `desc` = ?, lastmod_time = now(), lastmod_userid = ?, isdel = ? where id = ?", 
				new Object[]{apk.getVersion(), apk.getUrl(), apk.getForce(), apk.getDesc(), apk.getLastmodUserid(), apk.getIsdel(), apk.getId()}); 
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean updateApkUrl(Apk apk) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_APK + " set url = ?, lastmod_time = now(), lastmod_userid = ?, where id = ?", 
				new Object[]{apk.getUrl(), apk.getLastmodUserid(), apk.getId()}); 
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean delAllApks() {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_APK + " set isdel = ? where isdel = ?", new Object[]{IWamiConstants.INACTIVE, IWamiConstants.ACTIVE}); 
		if(count > 0)
			return true;
		else
			return false;
	}

}
