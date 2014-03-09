package com.iwami.iwami.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.PushDao;
import com.iwami.iwami.app.model.Push;
import com.iwami.iwami.app.model.PushTask;

public class PushDaoImpl extends JdbcDaoSupport implements PushDao {

	@Override
	public List<Push> getUnFinishedPushTasks() {
		return getJdbcTemplate().query("select id, `interval`, msg, status, cell_phone, add_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_PUSH + " where isdel = 0 and status in (0, 3, 4)", 
				new PushRowMapper());
	}

	@Override
	public List<Push> getTodoPushTasks() {
		return getJdbcTemplate().query("select id, `interval`, msg, status, cell_phone, add_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_PUSH + " where isdel = 0 and status in (0, 1, 3)", 
				new PushRowMapper());
	}

	@Override
	public Map<Long, Map<Integer, Integer>> getAllCntsByIds(List<Long> ids) {
		final Map<Long, Map<Integer, Integer>> result = new HashMap<Long, Map<Integer,Integer>>();
		
		getJdbcTemplate().query("select push_id, status, count(1) as cnt from " + SqlConstants.TABLE_PUSH_TASK + " where push_id in (" + StringUtils.join(ids.toArray(), ",") + ") group by push_id, status", new RowMapper<Integer>(){

			@Override
			public Integer mapRow(ResultSet rs, int index) throws SQLException {
				long id = rs.getLong("push_id");
				int status = rs.getInt("status");
				int cnt = rs.getInt("cnt");
				
				Map<Integer, Integer> tmp = result.get(id);
				if(tmp == null){
					tmp = new HashMap<Integer, Integer>();
					result.put(id, tmp);
				}
				
				tmp.put(status, cnt);
				
				return index;
			}
			
		});
		
		return result;
	}

	@Override
	public boolean addPush(final Push push) {
		KeyHolder holder = new GeneratedKeyHolder();
		int count = getJdbcTemplate().update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement("insert into " + SqlConstants.TABLE_PUSH + "(`interval`, msg, status, cell_phone, add_time, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, now(), now(), ?, ?)", Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, push.getInterval());
				ps.setObject(2, push.getMsg());
				ps.setObject(3, push.getStatus());
				ps.setObject(4, push.getCellPhone());
				ps.setObject(5, push.getLastModUserid());
				ps.setObject(6, IWamiConstants.ACTIVE);
				return ps;
			}
		}, holder);
		
		if(count > 0 && holder.getKey() != null){
			push.setId(holder.getKey().longValue());
			return true;
		} else
			return false;
	}

	@Override
	public boolean updatePush(int status, long id, long adminid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_PUSH + " set status = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", new Object[]{status, adminid, id});
		return count > 0;
	}

	@Override
	public boolean updatePush(int status, long id) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_PUSH + " set status = ?, lastmod_time = now() where id = ?", new Object[]{status, id});
		return count > 0;
	}

	@Override
	public boolean updatePush(String cellPhone, int status, long id) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_PUSH + " set cell_phone = ?, status = ?, lastmod_time = now() where id = ?", new Object[]{cellPhone, status, id});
		return count > 0;
	}

	@Override
	public void addPushTasks(final List<PushTask> tasks) {
		getJdbcTemplate().batchUpdate("insert into " + SqlConstants.TABLE_PUSH_TASK + "(push_id, userid, alias, status, add_time, lastmod_time, lastmod_userid) values(?, ?, ?, ?, now(), now(), ?)", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				if(tasks.size() > index){
					PushTask task = tasks.get(index);
					if(task != null){
						ps.setObject(1, task.getPushId());
						ps.setObject(2, task.getUserid());
						ps.setObject(3, task.getAlias());
						ps.setObject(4, task.getStatus());
						ps.setObject(5, task.getLastModUserid());
					}
				}
			}
			
			@Override
			public int getBatchSize() {
				return tasks.size();
			}
		});
	}

	@Override
	public boolean updatePushTask(int status, long id) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_PUSH_TASK + " set status = ?, lastmod_time = now() where id = ?", new Object[]{status, id});
		return count > 0;
	}

	@Override
	public List<PushTask> getLimitedPushTaskById(long pushid, int limit) {
		return getJdbcTemplate().query("select id, push_id, userid, alias, status, add_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_PUSH_TASK + " where push_id = ? and status = ? limit ?", 
				new Object[]{pushid, PushTask.STATUS_NEW, limit}, new PushTaskRowMapper());
	}

}

class PushTaskRowMapper implements RowMapper<PushTask>{

	@Override
	public PushTask mapRow(ResultSet rs, int index) throws SQLException {
		PushTask task = new PushTask();
		
		task.setId(rs.getLong("id"));
		task.setPushId(rs.getLong("push_id"));
		task.setUserid(rs.getLong("userid"));
		task.setAlias(rs.getString("alias"));
		task.setStatus(rs.getInt("status"));
		Timestamp ts = rs.getTimestamp("add_time");
		if(ts != null)
			task.setAddTime(new Date(ts.getTime()));
		ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			task.setLastModTime(new Date(ts.getTime()));
		task.setLastModUserid(rs.getLong("lastmod_userid"));
		
		return task;
	}
	
}

class PushRowMapper implements RowMapper<Push>{

	@Override
	public Push mapRow(ResultSet rs, int index) throws SQLException {
		Push push = new Push();
		
		push.setId(rs.getLong("id"));
		push.setInterval(rs.getLong("interval"));
		push.setMsg(rs.getString("msg"));
		push.setStatus(rs.getInt("status"));
		push.setCellPhone(rs.getString("cell_phone"));
		Timestamp ts = rs.getTimestamp("add_time");
		if(ts != null)
			push.setAddTime(new Date(ts.getTime()));
		ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			push.setLastModTime(new Date(ts.getTime()));
		push.setLastModUserid(rs.getLong("lastmod_userid"));
		
		return push;
	}
	
}