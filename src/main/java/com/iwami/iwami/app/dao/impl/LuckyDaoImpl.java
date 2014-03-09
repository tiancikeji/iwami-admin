package com.iwami.iwami.app.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.LuckyDao;
import com.iwami.iwami.app.model.LuckyRule;

public class LuckyDaoImpl extends JdbcDaoSupport implements LuckyDao {

	@Override
	public List<LuckyRule> getLuckyRules() {
		List<LuckyRule> result = getJdbcTemplate().query("select id, index_lev, gift, prob, `count`, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_LUCKY_RULE, new RowMapper<LuckyRule>() {

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
	public boolean delRules(long adminid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_LUCKY_RULE + " set isdel = ?, lastmod_time = now(), lastmod_userid = ?", new Object[]{IWamiConstants.INACTIVE, adminid});
		if(count > 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean modRules(final List<LuckyRule> rules) {
		getJdbcTemplate().batchUpdate("update " + SqlConstants.TABLE_LUCKY_RULE + " set gift = ?, prob = ?, `count` = ?, lastmod_time = now(), lastmod_userid = ?, isdel = ? where index_lev = ?", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				if(rules.size() > index){
					LuckyRule rule = rules.get(index);
					if(rule != null){
						ps.setObject(1, rule.getGift());
						ps.setObject(2, rule.getProb());
						ps.setObject(3, rule.getCount());
						ps.setObject(4, rule.getLastmodUserid());
						ps.setObject(5, rule.getIsdel());
						ps.setObject(6, rule.getIndexLevel());
					}
				}
			}
			
			@Override
			public int getBatchSize() {
				return rules.size();
			}
		});
		return true;
	}

}
