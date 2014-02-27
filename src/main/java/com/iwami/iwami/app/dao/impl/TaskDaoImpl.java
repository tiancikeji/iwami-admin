package com.iwami.iwami.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.nuxeo.common.utils.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.TaskDao;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.TreasureConfig;

public class TaskDaoImpl extends JdbcDaoSupport implements TaskDao {

	@Override
	public List<Task> getAllTasks() {
		return getJdbcTemplate().query("select id, name, rank, size, intr, appintr, prize, type, background, time, register, reputation, star, start_time, end_time, current_prize, max_prize, icon_gray, icon_small, icon_big, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_TASK + " where isdel = ?", 
				new Object[]{0}, new TaskRowMapper());
	}

	@Override
	public TreasureConfig getTreasureConfig() {
		List<TreasureConfig> configs = getJdbcTemplate().query("select `id`, `days`, `count`, `lastmod_time`, `lastmod_userid` from " + SqlConstants.TABLE_TREASURE_CONFIG + " where isdel = 0 order by lastmod_time desc limit 1", new TreasureConfigRowMapper());
		
		if(configs != null && configs.size() > 0)
			return configs.get(0);
		else
			return null;
	}

	@Override
	public Task getTaskById(long taskid) {
		List<Task> tasks = getJdbcTemplate().query("select id, name, rank, size, intr, appintr, prize, type, background, time, register, reputation, star, start_time, end_time, current_prize, max_prize, icon_gray, icon_small, icon_big, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_TASK + " where id = ?", 
				new Object[]{taskid}, new TaskRowMapper());
		
		if(tasks != null && tasks.size() > 0)
			return tasks.get(0);
		else
			return null;
	}

	@Override
	public List<Task> getTasksByIds(List<Long> taskIds) {
		return getJdbcTemplate().query("select id, name, rank, size, intr, appintr, prize, type, background, time, register, reputation, star, start_time, end_time, current_prize, max_prize, icon_gray, icon_small, icon_big, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_TASK + " where id in (" + StringUtils.join(taskIds.toArray(), ",") + ")", new TaskRowMapper());
	}

	@Override
	public void incrTaskCurrentPrize(long taskid) {
		getJdbcTemplate().update("update " + SqlConstants.TABLE_TASK + " set current_prize = current_prize + prize where id = ?", new Object[]{taskid});
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
		task.setIconGray(rs.getString("icon_gray"));
		task.setIconSmall(rs.getString("icon_small"));
		task.setIconBig(rs.getString("icon_big"));
		ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			task.setLastModTime(new Date(ts.getTime()));
		task.setLastModUserid(rs.getLong("lastmod_userid"));
		return task;
	}
	
}