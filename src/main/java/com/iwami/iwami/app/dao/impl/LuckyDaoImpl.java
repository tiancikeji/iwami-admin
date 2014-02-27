package com.iwami.iwami.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.LuckyDao;
import com.iwami.iwami.app.model.LuckyConfig;
import com.iwami.iwami.app.model.LuckyRule;
import com.iwami.iwami.app.model.Present;

public class LuckyDaoImpl extends JdbcDaoSupport implements LuckyDao {

	@Override
	public List<LuckyRule> getLuckyRules() {
		List<LuckyRule> result = getJdbcTemplate().query("select id, index_lev, gift, prob, count, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_LUCKY_RULE + " where isdel = 0", new RowMapper<LuckyRule>() {

			@Override
			public LuckyRule mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				LuckyRule rule = new LuckyRule();
				rule.setId(rs.getLong("id"));
				rule.setIndexLevel(rs.getInt("index_lev"));
				rule.setGift(rs.getString("gift"));
				rule.setProb(rs.getInt("prob"));
				rule.setCount(rs.getInt("count"));
				rule.setLastmodTime(rs.getDate("lastmod_time"));
				rule.setLastmodUserid(rs.getLong("lastmod_userid"));
				return rule;
			}
		});
		return result;
	}

	@Override
	public boolean delLuckyRules(long id) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_LUCKY_RULE + " set isdel = 1 where id = ?", new Object[]{id});
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean addLuckyRule(LuckyRule rule) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_LUCKY_RULE + "(index_lev, gift, prob, lastmod_time, lastmod_userid, isdel) values(?,?,?,now(),?,0)", 
				new Object[]{rule.getIndexLevel(), rule.getGift(), rule.getProb(), rule.getLastmodUserid()});
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public LuckyConfig getLuckyConfig() {
		List<LuckyConfig> configs = getJdbcTemplate().query("select id, name, prize, `count`, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_PRESENT + " where isdel = ? and type = ? order by lastmod_time desc limit 1", new Object[]{0, Present.TYPE_LUCK}, new RowMapper<LuckyConfig>(){

			@Override
			public LuckyConfig mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				LuckyConfig config = new LuckyConfig();
				config.setId(rs.getLong("id"));
				config.setCount(rs.getInt("count"));
				config.setPrize(rs.getInt("prize"));
				config.setLastmodTime(rs.getDate("lastmod_time"));
				config.setLastmodUserid(rs.getLong("lastmod_userid"));
				return config;
			}
			
		});
		if(configs != null && configs.size() > 0)
			return configs.get(0);
		else
			return null;
	}

}
