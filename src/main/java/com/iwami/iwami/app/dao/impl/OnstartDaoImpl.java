package com.iwami.iwami.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.OnstartDao;
import com.iwami.iwami.app.model.Onstart;

public class OnstartDaoImpl extends JdbcDaoSupport implements OnstartDao{

	@Override
	public boolean logOnstart(Onstart onstart) {
		boolean result = false;
		if(onstart != null){
			int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_ONSTART + "(`userid`,`cell_phone`,`uuid`,`type`,`gps`,`alias`,`version`,`add_time`,`lastmod_time`) values(?,?,?,?,?,?,?,?,now())", 
					new Object[]{onstart.getUserid(), onstart.getCellPhone(), onstart.getUuid(), onstart.getType(), onstart.getGps(),onstart.getAlias(), onstart.getVersion(), onstart.getAddTime()});
			if(count > 0)
				result = true;
		}
		return result;
	}

	@Override
	public List<Onstart> getAllOnstarts() {
		List<Onstart> result = getJdbcTemplate().query("", new RowMapper<Onstart>() {

			@Override
			public Onstart mapRow(ResultSet rs, int rowNum) throws SQLException {
				Onstart os = new Onstart();
				os.setUserid(rs.getLong("userid"));
				os.setCellPhone(rs.getLong("cell_phone"));
				os.setUuid(rs.getString("uuid"));
				os.setGps(rs.getString("gps"));
				os.setAlias(rs.getString("alias"));
				os.setVersion(rs.getString("vesion"));
				os.setType(rs.getInt("type"));
				Timestamp ts = rs.getTimestamp("add_time");
				if(ts != null)
					os.setAddTime(new Date(ts.getTime()));
				ts = rs.getTimestamp("lastmod_time");
				if(ts != null)
					os.setLastModTime(new Date(ts.getTime()));
				return os;
			}
		});
		return result;
	}
}
