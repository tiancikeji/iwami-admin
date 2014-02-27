package com.iwami.iwami.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.StrategyDao;
import com.iwami.iwami.app.model.Strategy;
import com.iwami.iwami.app.model.StrategyImage;
import com.iwami.iwami.app.model.StrategyInfo;
import com.iwami.iwami.app.model.StrategyRate;

public class StrategyDaoImpl extends JdbcDaoSupport implements StrategyDao {

	@Override
	public List<StrategyImage> getAllStrategyImages() {
		return getJdbcTemplate().query("select id, rank, icon_url, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_STRATEGY_IMAGES + " where isdel = ?", 
				new Object[]{0}, new StrategyImageRowMapper());
	}

	@Override
	public List<Strategy> getAllStrategies() {
		return getJdbcTemplate().query("select id, name, subname, intr, rank, icon_small, icon_big, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_STRATEGY_LIST + " where isdel = ?", 
				new Object[]{0}, new StrategyRowMapper());
	}

	@Override
	public StrategyRate getStrategyRateByStrategyId(long strategyId) {
		List<StrategyRate> rates = getJdbcTemplate().query("select strategy_id, skim, rate from " + SqlConstants.TABLE_STRATEGY_RATE + " where isdel = ? and strategy_id = ?",
				new Object[]{0, strategyId}, new StrategyRateRowMapper());
		if(rates != null && rates.size() > 0)
			return rates.get(0);
		else
			return null;
	}

	@Override
	public List<StrategyInfo> getStrategyInfosByStrategyId(long strategyId, int start, int step) {
		return getJdbcTemplate().query("select id, strategy_id, rank, title, content, url, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_STRATEGY_INFO + " where isdel = ? and strategy_id = ? order by rank, id limit ?, ?", 
				new Object[]{0, strategyId, start, step}, new StrategyInfoRowMapper());
	}

	@Override
	public boolean rateStrategy(long strategyId, String uuid) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_RATE_INFO + "(strategy_id, uuid, lastmod_time, isdel) values(?, ?, now(), ?)", 
				new Object[]{strategyId, uuid, 0});
		return count > 0;
	}

	@Override
	public boolean incrStrategyRateSkim(long strategyId) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_STRATEGY_RATE + " set skim = skim + 1 where strategy_id = ? and isdel = ?", 
				new Object[]{strategyId, 0});
		return count > 0;
	}

	@Override
	public boolean incrStrategyRateRate(long strategyId) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_STRATEGY_RATE + " set rate = rate + 1 where strategy_id = ? and isdel = ?", 
				new Object[]{strategyId, 0});
		return count > 0;
	}
}

class StrategyInfoRowMapper implements RowMapper<StrategyInfo>{

	@Override
	public StrategyInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		StrategyInfo info = new StrategyInfo();
		info.setId(rs.getInt("id"));
		info.setStrategyId(rs.getLong("strategy_id"));
		info.setRank(rs.getInt("rank"));
		info.setTitle(rs.getString("title"));
		info.setContent(rs.getString("content"));
		info.setUrl(rs.getString("url"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			info.setLastModTime(new Date(ts.getTime()));
		info.setLastModUserid(rs.getLong("lastmod_userid"));
		return info;
	}
	
}

class StrategyRateRowMapper implements RowMapper<StrategyRate>{

	@Override
	public StrategyRate mapRow(ResultSet rs, int rowNum) throws SQLException {
		StrategyRate rate = new StrategyRate();
		rate.setStrategyId(rs.getLong("strategy_id"));
		rate.setSkim(rs.getInt("skim"));
		rate.setRate(rs.getInt("rate"));
		return rate;
	}
	
}

class StrategyRowMapper implements RowMapper<Strategy>{

	@Override
	public Strategy mapRow(ResultSet rs, int rowNum) throws SQLException {
		Strategy strategy = new Strategy();
		strategy.setId(rs.getLong("id"));
		strategy.setName(rs.getString("name"));
		strategy.setSubName(rs.getString("subname"));
		strategy.setIntr(rs.getString("intr"));
		strategy.setRank(rs.getInt("rank"));
		strategy.setIconSmall(rs.getString("icon_small"));
		strategy.setIconBig(rs.getString("icon_big"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			strategy.setLastModTime(new Date(ts.getTime()));
		strategy.setLastModUserid(rs.getLong("lastmod_userid"));
		return strategy;
	}
	
}

class StrategyImageRowMapper implements RowMapper<StrategyImage>{

	@Override
	public StrategyImage mapRow(ResultSet rs, int index) throws SQLException {
		StrategyImage image = new StrategyImage();
		image.setId(rs.getLong("id"));
		image.setRank(rs.getInt("rank"));
		image.setIconUrl(rs.getString("icon_url"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			image.setLastModTime(new Date(ts.getTime()));
		image.setLastModUserid(rs.getLong("lastmod_userid"));
		return image;
	}
	
}
