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
import com.iwami.iwami.app.dao.TaskDao;
import com.iwami.iwami.app.model.Task;
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
		StringBuilder sql = new StringBuilder("select id, name, rank, size, intr, appintr, prize, type, background, time, register, reputation, star, start_time, end_time, current_prize, max_prize, icon_gray, icon_small, icon_big, lastmod_time, lastmod_userid, isdel from ");
		sql.append(SqlConstants.TABLE_TASK);
		sql.append(" where type & ");
		sql.append(type);
		sql.append(" > 0 ");
		sql.append(" and isdel in (0, 1) ");
		
		if(type != 4){
			sql.append(" and background = ");
			sql.append(background);
			sql.append(" and register = ");
			sql.append(register);
		}
		
		if(maxL >= 0){
			sql.append(" and (max_prize = -1 or max_prize >= ");
			sql.append(maxL);
			sql.append(")");
		}
		if(maxR >= 0 && maxR >= maxL){
			sql.append(" and (max_prize = -1 or max_prize <= ");
			sql.append(maxR);
			sql.append(")");
		}
		
		if(prizeL >= 0){
			sql.append(" and prize >= ");
			sql.append(prizeL);
		}
		if(prizeR >= 0 && prizeR >= prizeL){
			sql.append(" and prize <= ");
			sql.append(prizeR);
		}
		
		if(currL >= 0){
			sql.append(" and current_prize >= ");
			sql.append(currL);
		}
		if(currR >= 0 && currR >= currL){
			sql.append(" and current_prize <= ");
			sql.append(currR);
		}
		
		if(leftL >= 0){
			sql.append(" and (max_prize = -1 or (max_prize - current_prize) >= ");
			sql.append(leftL);
			sql.append(")");
		}
		if(leftR >= 0 && leftR >= leftL){
			sql.append(" and (max_prize = -1 or (max_prize - current_prize) <= ");
			sql.append(leftR);
			sql.append(")");
		}
		
		if(startL != null){
			sql.append(" and start_time >= ");
			sql.append(startL);
		}
		if(startR != null){
			sql.append(" and start_time <= ");
			sql.append(startR);
		}
		
		if(endL != null){
			sql.append(" and (end_time is null or end_time >= ");
			sql.append(endL);
			sql.append(")");
		}
		if(endR != null){
			sql.append(" and (end_time is null or end_time <= ");
			sql.append(endR);
			sql.append(")");
		}
		
		return getJdbcTemplate().query(sql.toString(), new TaskRowMapper());
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
		task.setIsdel(rs.getInt("isdel"));
		return task;
	}
	
}