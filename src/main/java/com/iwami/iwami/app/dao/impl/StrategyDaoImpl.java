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
import com.iwami.iwami.app.dao.StrategyDao;
import com.iwami.iwami.app.model.Strategy;
import com.iwami.iwami.app.model.StrategyImage;
import com.iwami.iwami.app.model.StrategyInfo;
import com.iwami.iwami.app.model.StrategyRate;

public class StrategyDaoImpl extends JdbcDaoSupport implements StrategyDao {

	// image
	@Override
	public List<StrategyImage> getAllImages() {
		return getJdbcTemplate().query("select id, strategy_id, rank, icon_url, lastmod_time, lastmod_userid, isdel from " + SqlConstants.TABLE_STRATEGY_IMAGES + " where isdel = 0", new StrategyImageRowMapper());
	}

	@Override
	public boolean addImage(final StrategyImage image) {
		KeyHolder holder = new GeneratedKeyHolder();
		int count = getJdbcTemplate().update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement("insert into " + SqlConstants.TABLE_STRATEGY_IMAGES + "(rank, strategy_id, icon_url, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, now(), ?, ?)", Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, image.getRank());
				ps.setObject(2, image.getStrategyId());
				ps.setObject(3, image.getIconUrl());
				ps.setObject(4, image.getLastModUserid());
				ps.setObject(5, image.getIsdel());
				return ps;
			}
		}, holder);
		
		if(count > 0 && holder.getKey() != null){
			image.setId(holder.getKey().longValue());
			return true;
		} else
			return false;
	}

	@Override
	public boolean modImage(StrategyImage image) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_STRATEGY_IMAGES + " set rank = ?, strategy_id = ?, icon_url = ?, lastmod_time = now(), lastmod_userid = ?, isdel = ? where id = ?", new Object[]{image.getRank(), image.getStrategyId(), image.getIconUrl(), image.getLastModUserid(), image.getIsdel(), image.getId()});
		return count > 0;
	}

	@Override
	public boolean updateImageUrl(StrategyImage image) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_STRATEGY_IMAGES + " set icon_url = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", new Object[]{image.getIconUrl(), image.getLastModUserid(), image.getId()});
		return count > 0;
	}

	@Override
	public boolean delImage(int id, long adminid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_STRATEGY_IMAGES + " set lastmod_time = now(), lastmod_userid = ?, isdel = ? where id = ?", new Object[]{adminid, IWamiConstants.INACTIVE, id});
		return count > 0;
	}

	@Override
	public boolean modImageSeqs(final List<Long> lIds, final List<Integer> lRanks, final long adminid) {
		getJdbcTemplate().batchUpdate("update " + SqlConstants.TABLE_STRATEGY_IMAGES + " set rank = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				if(lIds.size() > index){
					ps.setObject(1, lRanks.get(index));
					ps.setObject(2, adminid);
					ps.setObject(3, lIds.get(index));
				}
			}
			
			@Override
			public int getBatchSize() {
				return lIds.size();
			}
		});
		return true;
	}

	// strategy
	@Override
	public List<Strategy> getStrategies(String key) {
		String sql = "select id, name, subname, intr, rank, icon_small, icon_big, isdel, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_STRATEGY_LIST + " where isdel = 0 ";
		if(StringUtils.isNotBlank(key)){
			sql += " and (id = ? or name = ?)";
			return getJdbcTemplate().query(sql, new Object[]{key, key}, new StrategyRowMapper());
		}
		return getJdbcTemplate().query(sql, new StrategyRowMapper());
	}

	@Override
	public boolean delStrategy(long id, long adminid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_STRATEGY_LIST + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", new Object[]{IWamiConstants.INACTIVE, adminid, id});
		return count > 0;
	}

	@Override
	public boolean modStrategySeqls(final List<Long> lIds, final List<Integer> lRanks, final long adminid) {
		getJdbcTemplate().batchUpdate("update " + SqlConstants.TABLE_STRATEGY_LIST + " set rank = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				if(lIds.size() > index){
					ps.setObject(1, lRanks.get(index));
					ps.setObject(2, adminid);
					ps.setObject(3, lIds.get(index));
				}
			}
			
			@Override
			public int getBatchSize() {
				return lIds.size();
			}
		});
		return true;
	}

	@Override
	public boolean modStrategy(Strategy strategy) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_STRATEGY_LIST + " set name = ?, subname = ?, intr = ?, rank = ?, icon_small = ?, icon_big = ?, isdel = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{strategy.getName(), strategy.getSubName(), strategy.getIntr(), strategy.getRank(), strategy.getIconSmall(), strategy.getIconBig(), strategy.getIsdel(), strategy.getLastModUserid(), strategy.getId()});
		return count > 0;
	}

	@Override
	public boolean updateStrategyUrl(Strategy strategy) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_STRATEGY_LIST + " set icon_small = ?, icon_big = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{strategy.getIconSmall(), strategy.getIconBig(), strategy.getLastModUserid(), strategy.getId()});
		return count > 0;
	}

	@Override
	public long addStrategy(final Strategy strategy) {
		KeyHolder holder = new GeneratedKeyHolder();
		int count = getJdbcTemplate().update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement("insert into " + SqlConstants.TABLE_STRATEGY_LIST + "(name, subname, intr, rank, icon_small, icon_big, isdel, lastmod_time, lastmod_userid) values(?, ?, ?, ?, ?, ?, ?, now(), ?)", Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, strategy.getName());
				ps.setObject(2, strategy.getSubName());
				ps.setObject(3, strategy.getIntr());
				ps.setObject(4, strategy.getRank());
				ps.setObject(5, strategy.getIconSmall());
				ps.setObject(6, strategy.getIconBig());
				ps.setObject(7, strategy.getIsdel());
				ps.setObject(8, strategy.getLastModUserid());
				return ps;
			}
		}, holder);
		
		if(count > 0 && holder.getKey() != null)
			return holder.getKey().longValue();
		
		return 0;
	}

	// info
	@Override
	public List<StrategyInfo> getInfos(long id) {
		return getJdbcTemplate().query("select id, strategy_id, rank, title, content, url, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_STRATEGY_INFO + " where isdel = ? and strategy_id = ?", 
				new Object[]{IWamiConstants.ACTIVE, id}, new StrategyInfoRowMapper());
	}

	@Override
	public boolean delInfo(long id, long adminid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_STRATEGY_INFO + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", new Object[]{IWamiConstants.INACTIVE, adminid, id});
		return count > 0;
	}

	@Override
	public boolean delInfos(long id, long adminid) {
		getJdbcTemplate().update("update " + SqlConstants.TABLE_STRATEGY_INFO + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where strategy_id = ?", new Object[]{IWamiConstants.INACTIVE, adminid, id});
		return true;
	}

	@Override
	public boolean addInfo(final StrategyInfo info) {
		KeyHolder holder = new GeneratedKeyHolder();
		int count = getJdbcTemplate().update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement("insert into " + SqlConstants.TABLE_STRATEGY_INFO + "(strategy_id, rank, title, content, url, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, ?, now(), ?, ?)", Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, info.getStrategyId());
				ps.setObject(2, info.getRank());
				ps.setObject(3, info.getTitle());
				ps.setObject(4, info.getContent());
				ps.setObject(5, info.getUrl());
				ps.setObject(6, info.getLastModUserid());
				ps.setObject(7, IWamiConstants.ACTIVE);
				return ps;
			}
		}, holder);
		
		if(count > 0 && holder.getKey() != null){
			info.setId(holder.getKey().longValue());
			return true;
		} else
			return false;
	}

	@Override
	public boolean modInfo(StrategyInfo info) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_STRATEGY_INFO + " set rank = ?, title = ?, content = ?, url = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{info.getRank(), info.getTitle(), info.getContent(), info.getUrl(), info.getLastModUserid(), info.getId()});
		return count > 0;
	}

	@Override
	public boolean updateInfoUrl(StrategyInfo info) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_STRATEGY_INFO + " set url = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{info.getUrl(), info.getLastModUserid(), info.getId()});
		return count > 0;
	}

	// rate
	@Override
	public Map<Long, StrategyRate> getRatesByIds(List<Long> ids) {
		List<StrategyRate> rates = getJdbcTemplate().query("select strategy_id, skim, rate from " + SqlConstants.TABLE_STRATEGY_RATE + " where strategy_id in (" + StringUtils.join(ids.toArray(), ",") + ")", new StrategyRateRowMapper());
		
		Map<Long, StrategyRate> result = new HashMap<Long, StrategyRate>();
		if(rates != null && rates.size() > 0)
			for(StrategyRate rate : rates)
				result.put(rate.getStrategyId(), rate);
		
		return result;
	}

	@Override
	public boolean delRateInfo(long id, long adminid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_RATE_INFO + " set isdel = ? where strategy_id = ?", new Object[]{IWamiConstants.INACTIVE, id});
		return count > 0;
	}

	@Override
	public boolean delRate(long id, long adminid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_STRATEGY_RATE + " set isdel = ? where strategy_id = ?", new Object[]{IWamiConstants.INACTIVE, id});
		return count > 0;
	}

	@Override
	public boolean modRate(StrategyRate rate) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_STRATEGY_RATE + " set skim = ?, rate = ? where strategy_id = ?", new Object[]{rate.getSkim(), rate.getRate(), rate.getStrategyId()});
		return count > 0;
	}

	@Override
	public boolean addRate(StrategyRate rate) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_STRATEGY_RATE + "(strategy_id, skim, rate, isdel) values(?, ?, ?, ?)", new Object[]{rate.getStrategyId(), rate.getSkim(), rate.getRate(), IWamiConstants.ACTIVE});
		return count > 0;
	}
}

class StrategyInfoRowMapper implements RowMapper<StrategyInfo>{

	@Override
	public StrategyInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		StrategyInfo info = new StrategyInfo();
		info.setId(rs.getLong("id"));
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
		strategy.setIsdel(rs.getInt("isdel"));
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
		image.setStrategyId(rs.getLong("strategy_id"));
		image.setRank(rs.getInt("rank"));
		image.setIconUrl(rs.getString("icon_url"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			image.setLastModTime(new Date(ts.getTime()));
		image.setLastModUserid(rs.getLong("lastmod_userid"));
		image.setIsdel(rs.getInt("isdel"));
		return image;
	}
	
}
