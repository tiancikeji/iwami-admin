package com.iwami.iwami.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.common.utils.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.WamiDao;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.Wami;

public class WamiDaoImpl extends JdbcDaoSupport implements WamiDao {

	@Override
	public void newWami(Wami wami) {
		getJdbcTemplate().update("insert into " + SqlConstants.TABLE_WAMI + "(userid, task_id, type, prize, channel, add_time, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, ?, ?, now(), ?, ?)", 
				new Object[]{wami.getUserid(), wami.getTaskId(), wami.getType(), wami.getPrize(), wami.getChannel(), wami.getAddTime(), wami.getLastmodUserid(), 0});
	}

	@Override
	public Wami getLatestWami(long userid, long taskid) {
		List<Wami> wamis = getJdbcTemplate().query("select id, userid, task_id, type, prize, channel, add_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_WAMI + " where isdel = ? and userid = ? and task_id = ? order by lastmod_time, id desc limit 1", 
				new Object[]{0, userid, taskid}, new WamiRowMapper());
		
		if(wamis != null && wamis.size() > 0)
			return wamis.get(0);
		else
			return null;
	}

	@Override
	public Map<Long, Wami> getLatestWami(long userid, List<Long> taskids) {
		Map<Long, Wami> wamis = new HashMap<Long, Wami>();
		
		List<Wami> tmp = getJdbcTemplate().query("select id, userid, task_id, type, prize, channel, add_time, lastmod_time, lastmod_userid from (select id, userid, task_id, type, prize, channel, add_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_WAMI + " where isdel = ? and userid = ? and task_id in (" + StringUtils.join(taskids.toArray(), ",") + ") order by lastmod_time, id desc) tmp group by task_id", 
				new Object[]{0, userid}, new WamiRowMapper());
		if(tmp != null && tmp.size() > 0)
			for(Wami wami : tmp)
				wamis.put(wami.getTaskId(), wami);
		
		return wamis;
	}

	@Override
	public Map<Long, Wami> getDoneTaskIds(long userid, Date start) {
		final Map<Long, Wami> wamis = new HashMap<Long, Wami>();
		List<Wami> tmp = getJdbcTemplate().query("select id, userid, task_id, type, prize, channel, add_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_WAMI + " where isdel = ? and userid = ? and type = ? and lastmod_time > ?", new Object[]{0, userid, Task.STATUS_FINISH, start}, new WamiRowMapper());
		if(tmp != null && tmp.size() > 0)
			for(Wami wami : tmp)
				wamis.put(wami.getTaskId(), wami);
		return wamis;
	}

	@Override
	public Map<Long, Wami> getOngoingWami(long userid) {
		final Map<Long, Wami> wamis = new HashMap<Long, Wami>();
		List<Wami> tmp = getJdbcTemplate().query("select id, userid, task_id, type, prize, channel, add_time, lastmod_time, lastmod_userid from (select id, userid, task_id, type, prize, channel, add_time, lastmod_time, lastmod_userid from (select id, userid, task_id, type, prize, channel, add_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_WAMI + " where isdel = ? and userid = ? order by lastmod_time, id desc) tmp group by task_id) tmp2 where type <> ?", 
				new Object[]{0, userid, Task.STATUS_FINISH}, new WamiRowMapper());
		if(tmp != null && tmp.size() > 0)
			for(Wami wami : tmp)
				wamis.put(wami.getTaskId(), wami);
		return wamis;
	}

}

class WamiRowMapper implements RowMapper<Wami>{

	@Override
	public Wami mapRow(ResultSet rs, int rowNum) throws SQLException {
		Wami wami = new Wami();
		wami.setId(rs.getLong("id"));
		wami.setUserid(rs.getLong("userid"));
		wami.setTaskId(rs.getLong("task_id"));
		wami.setType(rs.getInt("type"));
		wami.setPrize(rs.getInt("type"));
		wami.setChannel(rs.getString("channel"));
		Timestamp ts = rs.getTimestamp("add_time");
		if(ts != null)
			wami.setAddTime(new Date(ts.getTime()));
		ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			wami.setLastmodTime(new Date(ts.getTime()));
		wami.setLastmodUserid(rs.getLong("lastmod_userid"));
		return wami;
	}
	
}
