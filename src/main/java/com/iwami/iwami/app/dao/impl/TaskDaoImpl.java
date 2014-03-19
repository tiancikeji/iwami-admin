package com.iwami.iwami.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.TaskDao;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TaskNotification;
import com.iwami.iwami.app.model.TreasureConfig;

public class TaskDaoImpl extends JdbcDaoSupport implements TaskDao {

	@Override
	public TreasureConfig getTreasureConfig() {
		List<TreasureConfig> configs = getJdbcTemplate().query("select `id`, `days`, `count`, `lastmod_time`, `lastmod_userid` from " + SqlConstants.TABLE_TREASURE_CONFIG + " where isdel = 0 order by lastmod_time desc limit 1", new TreasureConfigRowMapper());
		
		if(configs != null && configs.size() > 0)
			return configs.get(0);
		else
			return null;
	}

	@Override
	public void deleteAllTreasureConfig(long adminid) {
		getJdbcTemplate().update("update " + SqlConstants.TABLE_TREASURE_CONFIG + " set isdel = ?, lastmod_time = now(), lastmod_userid = ?", new Object[]{IWamiConstants.INACTIVE, adminid});
	}

	@Override
	public boolean addTreasureConfig(TreasureConfig config) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_TREASURE_CONFIG + "(days, `count`, lastmod_time, lastmod_userid) values(?, ?, now(), ?)", 
				new Object[]{config.getDays(), config.getCount(), config.getLastModUserid()});
		return count > 0;
	}

	@Override
	public List<Task> getTasks(int type, int background, int register,
			int maxL, int maxR, int prizeL, int prizeR, int currL, int currR,
			int leftL, int leftR, Date startL, Date startR, Date endL, Date endR) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("select id, name, rank, size, intr, appintr, prize, type, background, time, register, reputation, star, start_time, end_time, current_prize, max_prize, url, icon_gray, icon_small, icon_big, lastmod_time, lastmod_userid, isdel from ");
		sql.append(SqlConstants.TABLE_TASK);
		sql.append(" where type & ? > 0");
		params.add(type);
		sql.append(" and isdel in (?, ?) ");
		params.add(0);
		params.add(1);
		
		if(background >= 0){
			sql.append(" and background = ?");
			params.add(background);
		}
		if(register >= 0){
			sql.append(" and register = ?");
			params.add(register);
		}
		
		if(maxL >= 0){
			sql.append(" and (max_prize = -1 or max_prize >= ?)");
			params.add(maxL);
		}
		if(maxR >= 0 && maxR >= maxL){
			sql.append(" and (max_prize = -1 or max_prize <= ?)");
			params.add(maxR);
		}
		
		if(prizeL >= 0){
			sql.append(" and prize >= ?");
			params.add(prizeL);
		}
		if(prizeR >= 0 && prizeR >= prizeL){
			sql.append(" and prize <= ?");
			params.add(prizeR);
		}
		
		if(currL >= 0){
			sql.append(" and current_prize >= ?");
			params.add(currL);
		}
		if(currR >= 0 && currR >= currL){
			sql.append(" and current_prize <= ?");
			params.add(currR);
		}
		
		if(leftL >= 0){
			sql.append(" and (max_prize = -1 or (max_prize - current_prize) >= ?)");
			params.add(leftL);
		}
		if(leftR >= 0 && leftR >= leftL){
			sql.append(" and (max_prize = -1 or (max_prize - current_prize) <= ?)");
			params.add(leftR);
		}
		
		if(startL != null){
			sql.append(" and start_time >= ?");
			params.add(startL);
		}
		if(startR != null){
			sql.append(" and start_time <= ?");
			params.add(startR);
		}
		
		if(endL != null){
			sql.append(" and (end_time is null or end_time >= ?)");
			params.add(endL);
		}
		if(endR != null){
			sql.append(" and (end_time is null or end_time <= ?)");
			params.add(endR);
		}
		
		return getJdbcTemplate().query(sql.toString(), params.toArray(),new TaskRowMapper());
	}

	@Override
	public Task getTaskById(long taskid) {
		List<Task> tasks = getJdbcTemplate().query("select id, name, rank, size, intr, appintr, prize, type, background, time, register, reputation, star, start_time, end_time, current_prize, max_prize, url, icon_gray, icon_small, icon_big, lastmod_time, lastmod_userid, isdel from " + SqlConstants.TABLE_TASK + " where id = ?", 
				new Object[]{taskid}, new TaskRowMapper());
		if(tasks != null && tasks.size() > 0)
			return tasks.get(0);
		else
			return null;
	}

	@Override
	public boolean delUnstartedTask(long taskid, long adminid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_TASK + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where id = ? and isdel = ? and start_time > now()", new Object[]{IWamiConstants.INACTIVE, adminid, taskid, IWamiConstants.ACTIVE});
		return count > 0;
	}

	@Override
	public boolean stopTask(long taskid, long adminid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_TASK + " set end_time = now(), lastmod_time = now(), lastmod_userid = ? where id = ? and isdel = ? and start_time < now() and (end_time >= now() or end_time is null) and (current_prize <= max_prize or max_prize = -1)", new Object[]{adminid, taskid, IWamiConstants.ACTIVE});
		return count > 0;
	}

	@Override
	public boolean modTask(Task task) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_TASK + " set name = ?, rank = ?, size = ?, intr = ?, appintr = ?, prize = ?, type = ?, background = ?, time = ?, register = ?, reputation = ?, star = ?, start_time = ?, end_time = ?, current_prize = current_prize + ?, max_prize = ?, url = ?, icon_gray = ?, icon_small = ?, icon_big = ?, lastmod_time = now(), lastmod_userid = ?, isdel = ? where id = ?",
				new Object[]{task.getName(), task.getRank(), task.getSize(), task.getIntr(), task.getAppIntr(), task.getPrize(), task.getType(), task.getBackground(), task.getTime(), task.getRegister(), task.getReputation(), task.getStar(), task.getStartTime(), task.getEndTime(), task.getCurrentPrize(), task.getMaxPrize(), task.getUrl(), task.getIconGray(), task.getIconSmall(), task.getIconBig(), task.getLastModUserid(), task.getIsdel(), task.getId()});
		return count > 0;
	}

	@Override
	public boolean updateTaskUrl(Task task) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_TASK + " set url = ?, icon_gray = ?, icon_small = ?, icon_big = ?, lastmod_time = now(), lastmod_userid = ? where id = ?",
				new Object[]{task.getUrl(), task.getIconGray(), task.getIconSmall(), task.getIconBig(), task.getLastModUserid(), task.getId()});
		return count > 0;
	}

	@Override
	public void incrTaskRankByType(int type) {
		getJdbcTemplate().update("update " + SqlConstants.TABLE_TASK + " set rank = rank + 1 where type = ?", new Object[]{type});
	}

	@Override
	public boolean addTask(final Task task) {
		KeyHolder holder = new GeneratedKeyHolder();
		int count = getJdbcTemplate().update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement("insert into " + SqlConstants.TABLE_TASK + "(name, rank, size, intr, appintr, prize, type, background, time, register, reputation, star, start_time, end_time, current_prize, max_prize, url, icon_gray, icon_small, icon_big, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, ?, ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?, ?, ?,  ?, now(), ?,  ?)", Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, task.getName());
				ps.setObject(2, task.getRank());
				ps.setObject(3, task.getSize());
				ps.setObject(4, task.getIntr());
				ps.setObject(5, task.getAppIntr());
				ps.setObject(6, task.getPrize());
				ps.setObject(7, task.getType());
				ps.setObject(8, task.getBackground());
				ps.setObject(9, task.getTime());
				ps.setObject(10, task.getRegister());
				ps.setObject(11, task.getReputation());
				ps.setObject(12, task.getStar());
				ps.setObject(13, task.getStartTime());
				ps.setObject(14, task.getEndTime());
				ps.setObject(15, task.getCurrentPrize());
				ps.setObject(16, task.getMaxPrize());
				ps.setObject(17, task.getUrl());
				ps.setObject(18, task.getIconGray());
				ps.setObject(19, task.getIconSmall());
				ps.setObject(20, task.getIconBig());
				ps.setObject(21, task.getLastModUserid());
				ps.setObject(22, task.getIsdel());
				return ps;
			}
		}, holder);
		
		if(count > 0 && holder.getKey() != null){
			task.setId(holder.getKey().longValue());
			return true;
		} else
			return false;
	}

	@Override
	public List<Task> getFinishedTasks() {
		return getJdbcTemplate().query("select id, name, rank, size, intr, appintr, prize, type, background, time, register, reputation, star, start_time, end_time, current_prize, max_prize, url, icon_gray, icon_small, icon_big, lastmod_time, lastmod_userid, isdel from " + SqlConstants.TABLE_TASK + " where isdel = ? and ((max_prize > -1 and current_prize >= max_prize) or (end_time is not null and end_time <= now())) and id not in (select distinct task_id from " + SqlConstants.TABLE_TASK_NOTIFICATION + " where status = ?)", 
				new Object[]{IWamiConstants.ACTIVE, TaskNotification.STATUS_SMS}, new TaskRowMapper());
	}

	@Override
	public void updateTaskNotificationStatus(long taskid, long cellPhone, int status) {
		getJdbcTemplate().update("update " + SqlConstants.TABLE_TASK_NOTIFICATION + " set status = ? where task_id = ? and cell_phone = ?", new Object[]{status, taskid, cellPhone});
	}

	@Override
	public boolean addTaskNotifications(final List<TaskNotification> notis) {
		getJdbcTemplate().batchUpdate("insert into " + SqlConstants.TABLE_TASK_NOTIFICATION + "(task_id, cell_phone, sms, status, add_time, lastmod_time) values(?, ?, ?, ?, now(), now())", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				if(notis.size() > index){
					TaskNotification tn = notis.get(index);
					if(tn != null){
						ps.setObject(1, tn.getTaskId());
						ps.setObject(2, tn.getCellPhone());
						ps.setObject(3, tn.getSms());
						ps.setObject(4, tn.getStatus());
					}
				}
			}
			
			@Override
			public int getBatchSize() {
				return notis.size();
			}
		});
		return true;
	}

	@Override
	public String getSMSNo() {
		List<String> nos = getJdbcTemplate().query("select content from " + SqlConstants.TABLE_TIPS + " where type = ? and isdel = ?", 
				new Object[]{4, 0}, new RowMapper<String>(){

					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getString("content");
					}
			
		});
		
		if(nos != null && nos.size() > 0)
			return nos.get(0);
		else
			return "";
	}

	@Override
	public List<Long> getTopTaskIds() {
		return getJdbcTemplate().query("select id from " + SqlConstants.TABLE_TASK + " where type & 8 > 0", new RowMapper<Long>(){

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("id");
			}
			
		});
	}

	@Override
	public List<Long> getTreasureTaskIds() {
		return getJdbcTemplate().query("select id from " + SqlConstants.TABLE_TASK + " where type & 2 > 0", new RowMapper<Long>(){

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong("id");
			}
			
		});
	}

	@Override
	public List<Task> getTasksByIds(Set<Long> taskIds) {
		return getJdbcTemplate().query("select id, name, rank, size, intr, appintr, prize, type, background, time, register, reputation, star, start_time, end_time, current_prize, max_prize, url, icon_gray, icon_small, icon_big, lastmod_time, lastmod_userid, isdel from " + SqlConstants.TABLE_TASK + " where id in (" + StringUtils.join(taskIds, ",") + ")", new TaskRowMapper());
	}
}

class TreasureConfigRowMapper implements RowMapper<TreasureConfig>{

	@Override
	public TreasureConfig mapRow(ResultSet rs, int index) throws SQLException {
		TreasureConfig config = new TreasureConfig();
		config.setId(rs.getInt("id"));
		config.setDays(rs.getInt("days"));
		config.setCount(rs.getInt("count"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			config.setLastModTime(new Date(ts.getTime()));
		config.setLastModUserid(rs.getLong("lastmod_userid"));
		return config;
	}
	
}

class TaskRowMapper implements RowMapper<Task>{

	@Override
	public Task mapRow(ResultSet rs, int index) throws SQLException {
		Task task = new Task();
		task.setId(rs.getLong("id"));
		task.setName(rs.getString("name"));
		task.setRank(rs.getInt("rank"));
		task.setSize(rs.getDouble("size"));
		task.setIntr(rs.getString("intr"));
		task.setAppIntr(rs.getString("appintr"));
		task.setPrize(rs.getInt("prize"));
		task.setType(rs.getInt("type"));
		task.setBackground(rs.getInt("background"));
		task.setTime(rs.getInt("time"));
		task.setRegister(rs.getInt("register"));
		task.setReputation(rs.getInt("reputation"));
		task.setStar(rs.getInt("star"));
		Timestamp ts = rs.getTimestamp("start_time");
		if(ts != null)
			task.setStartTime(new Date(ts.getTime()));
		ts = rs.getTimestamp("end_time");
		if(ts != null)
			task.setEndTime(new Date(ts.getTime()));
		task.setCurrentPrize(rs.getInt("current_prize"));
		task.setMaxPrize(rs.getInt("max_prize"));
		task.setUrl(rs.getString("url"));
		task.setIconGray(rs.getString("icon_gray"));
		task.setIconSmall(rs.getString("icon_small"));
		task.setIconBig(rs.getString("icon_big"));
		ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			task.setLastModTime(new Date(ts.getTime()));
		task.setLastModUserid(rs.getLong("lastmod_userid"));
		task.setIsdel(rs.getInt("isdel"));
		return task;
	}
	
}