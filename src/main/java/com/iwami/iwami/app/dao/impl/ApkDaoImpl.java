package com.iwami.iwami.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.ApkDao;
import com.iwami.iwami.app.model.Apk;

public class ApkDaoImpl extends JdbcDaoSupport implements ApkDao {

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
	public boolean addApk(Apk apk) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_APK + "(version, url, `force`, `desc`, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, now(), ?, ?)", 
				new Object[]{apk.getVersion(), apk.getUrl(), apk.getForce(), apk.getDesc(), apk.getLastmodUserid(), IWamiConstants.ACTIVE}); 
		if(count > 0)
			return true;
		else
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
	public boolean delAllApks() {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_APK + " set isdel = ? where isdel = ?", new Object[]{IWamiConstants.INACTIVE, IWamiConstants.ACTIVE}); 
		if(count > 0)
			return true;
		else
			return false;
	}

}
