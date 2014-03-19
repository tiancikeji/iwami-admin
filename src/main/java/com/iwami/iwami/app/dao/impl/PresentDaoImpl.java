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
import com.iwami.iwami.app.dao.PresentDao;
import com.iwami.iwami.app.model.Exchange;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.model.Share;

public class PresentDaoImpl extends JdbcDaoSupport implements PresentDao {

	@Override
	public List<Present> getPresentsByTypeNStatus(int type, List<Integer> status) {
		return getJdbcTemplate().query("select id, name, prize, `count`, rank, type, icon_small, icon_big, lastmod_time, lastmod_userid, isdel from " + SqlConstants.TABLE_PRESENT + " where type = ? and isdel in (" + StringUtils.join(status.toArray(), ",") + ")", 
				new Object[]{type}, new PresentRowMapper());
	}

	@Override
	public boolean modPresent(Present present) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_PRESENT + " set name = ?, prize = ?, `count` = ?, type = ?, rank =?, lastmod_time = now(), lastmod_userid = ?, isdel = ?, icon_small = ?, icon_big = ? where id = ?", 
				new Object[]{present.getName(), present.getPrize(), present.getCount(), present.getType(), present.getRank(), present.getLastModUserid(), present.getIsdel(), present.getIconSmall(), present.getIconBig(), present.getId()});
		
		return count > 0;
	}

	@Override
	public boolean delPresent(long id, long adminid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_PRESENT + " set isdel = ?, lastmod_time = now(), lastmod_userid = ? where id = ?", 
				new Object[]{IWamiConstants.INACTIVE, adminid, id});
		
		return count > 0;
	}

	@Override
	public boolean addPresent(final Present present) {
		KeyHolder holder = new GeneratedKeyHolder();
		int count = getJdbcTemplate().update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement("insert into " + SqlConstants.TABLE_PRESENT + "(name, prize, `count`, rank, type, icon_small, icon_big, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, ?, ?, ?, now(), ?, ?)", Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, present.getName());
				ps.setObject(2, present.getPrize());
				ps.setObject(3, present.getCount());
				ps.setObject(4, present.getRank());
				ps.setObject(5, present.getType());
				ps.setObject(6, present.getIconSmall());
				ps.setObject(7, present.getIconBig());
				ps.setObject(8, present.getLastModUserid());
				ps.setObject(9, present.getIsdel());
				return ps;
			}
		}, holder);
		
		if(count > 0 && holder.getKey() != null){
			present.setId(holder.getKey().longValue());
			return true;
		} else
			return false;
	}

	@Override
	public boolean updatePresentURL(Present present) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_PRESENT + " set lastmod_time = now(), lastmod_userid = ?, icon_small = ?, icon_big = ? where id = ?", 
				new Object[]{present.getLastModUserid(), present.getIconSmall(), present.getIconBig(), present.getId()});
		
		return count > 0;
	}

	@Override
	public boolean seqPresent(final Map<Long, Integer> data, final long adminid) {
		final List<Long> keys = new ArrayList<Long>(data.keySet());
		getJdbcTemplate().batchUpdate("update " + SqlConstants.TABLE_PRESENT + " set lastmod_time = now(), lastmod_userid = ?, rank = ? where id = ?", new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				if(keys.size() > i){
					long key = keys.get(i);
					ps.setObject(1, adminid);
					ps.setObject(2, data.get(key));
					ps.setObject(3, key);
				}
			}
			
			@Override
			public int getBatchSize() {
				return keys.size();
			}
		});
		return true;
	}

	@Override
	public List<Exchange> getExchangeHistory(List<Integer> types, List<Integer> status) {
		return getJdbcTemplate().query("select id, userid, presentid, present_name, present_prize, present_type, `count`, prize, status, cell_phone, alipay_account, bank_account, bank_no, bank_name, address, name, express_name, express_no, channel, add_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_EXCHANGE + " where isdel = 0 and presentid > 0 and status in (" + StringUtils.join(status.toArray(), ",") + ") and present_type in (" + StringUtils.join(types.toArray(), ",") + ")", 
				new ExchangeRowMapper());
	}

	@Override
	public List<Exchange> getExchangeHistoryByUser(List<Integer> types, long key) {
		return getJdbcTemplate().query("select id, userid, presentid, present_name, present_prize, present_type, `count`, prize, status, cell_phone, alipay_account, bank_account, bank_no, bank_name, address, name, express_name, express_no, channel, add_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_EXCHANGE + " where isdel = 0 and presentid > 0 and present_type in (" + StringUtils.join(types.toArray(), ",") + ") and (userid = ? or cell_phone = ?)", 
				new Object[]{key, key}, new ExchangeRowMapper());
	}

	@Override
	public List<Exchange> getExchangeHistoryByPresent(List<Integer> types, String key) {
		return getJdbcTemplate().query("select id, userid, presentid, present_name, present_prize, present_type, `count`, prize, status, cell_phone, alipay_account, bank_account, bank_no, bank_name, address, name, express_name, express_no, channel, add_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_EXCHANGE + " where isdel = 0 and presentid > 0 and present_type in (" + StringUtils.join(types.toArray(), ",") + ") and (presentid = ? or present_name = ?)", 
				new Object[]{key, key}, new ExchangeRowMapper());
	}

	@Override
	public Exchange getExchangeById(long id) {
		List<Exchange> result = getJdbcTemplate().query("select id, userid, presentid, present_name, present_prize, present_type, `count`, prize, status, cell_phone, alipay_account, bank_account, bank_no, bank_name, address, name, express_name, express_no, channel, add_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_EXCHANGE + " where isdel = 0 and id = ?", 
				new Object[]{id}, new ExchangeRowMapper());
		
		if(result != null && result.size() > 0)
			return result.get(0);
		else
			return null;
	}

	@Override
	public boolean modExchange(String name, String no, long id, long adminid) {
		int count = getJdbcTemplate().update("update " + SqlConstants.TABLE_EXCHANGE + " set status = ?, express_name = ?, express_no = ?, lastmod_time = now(), lastmod_userid = ? where id = ?",
				new Object[]{Exchange.STATUS_FINISH, name, no, adminid, id});
		return count > 0;
	}

	@Override
	public List<Exchange> getExchanges(Date start, Date end) {
		return getJdbcTemplate().query("select id, userid, presentid, present_name, present_prize, present_type, `count`, prize, status, cell_phone, alipay_account, bank_account, bank_no, bank_name, address, name, express_name, express_no, channel, add_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_EXCHANGE + " where isdel = 0 and (status = ? or status = ?) and add_time between ? and ?", 
				new Object[]{Exchange.STATUS_READY, Exchange.STATUS_FINISH, start, end}, new ExchangeRowMapper());
	}

	@Override
	public List<Exchange> getGifts(Date start, Date end) {
		return getJdbcTemplate().query("select id, userid, presentid, present_name, present_prize, present_type, `count`, prize, status, cell_phone, alipay_account, bank_account, bank_no, bank_name, address, name, express_name, express_no, channel, add_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_EXCHANGE + " where isdel = 0 and (status = ? or status = ?) and add_time between ? and ?", 
				new Object[]{Exchange.STATUS_FAILED, Exchange.STATUS_FINISH, start, end}, new ExchangeRowMapper());
	}

	@Override
	public List<Share> getShares(Date start, Date end) {
		return getJdbcTemplate().query("select id, userid, type, target, msg, lastmod_time from " + SqlConstants.TABLE_SHARE + " where lastmod_time between ? and ?",
				new Object[]{start, end}, new RowMapper<Share>(){

					@Override
					public Share mapRow(ResultSet rs, int rowNum) throws SQLException {
						Share share = new Share();
						
						share.setId(rs.getLong("id"));
						share.setUserid(rs.getLong("userid"));
						share.setType(rs.getInt("type"));
						share.setTarget(rs.getInt("target"));
						share.setMsg(rs.getString("msg"));
						Timestamp ts = rs.getTimestamp("lastmod_time");
						if(ts != null)
							share.setLastModTime(new Date(ts.getTime()));
						
						return share;
					}
			
		});
	}
}

class PresentRowMapper implements RowMapper<Present>{
	
	@Override
	public Present mapRow(ResultSet rs, int rowNum) throws SQLException {
		Present present = new Present();
		
		present.setId(rs.getLong("id"));
		present.setName(rs.getString("name"));
		present.setPrize(rs.getInt("prize"));
		present.setCount(rs.getInt("count"));
		present.setRank(rs.getInt("rank"));
		present.setType(rs.getInt("type"));
		present.setIconSmall(rs.getString("icon_small"));
		present.setIconBig(rs.getString("icon_big"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			present.setLastModTime(new Date(ts.getTime()));
		present.setLastModUserid(rs.getLong("lastmod_userid"));
		present.setIsdel(rs.getInt("isdel"));
		
		return present;
	}
}

class ExchangeRowMapper implements RowMapper<Exchange>{

	@Override
	public Exchange mapRow(ResultSet rs, int rowNum) throws SQLException {
		Exchange exchange = new Exchange();
		exchange.setId(rs.getLong("id"));
		exchange.setUserid(rs.getLong("userid"));
		exchange.setPresentId(rs.getLong("presentid"));
		exchange.setPresentName(rs.getString("present_name"));
		exchange.setPresentPrize(rs.getInt("present_prize"));
		exchange.setPresentType(rs.getInt("present_type"));
		exchange.setCount(rs.getInt("count"));
		exchange.setPrize(rs.getInt("prize"));
		exchange.setStatus(rs.getInt("status"));
		exchange.setCellPhone(rs.getLong("cell_phone"));
		exchange.setAlipayAccount(rs.getString("alipay_account"));
		exchange.setBankName(rs.getString("bank_name"));
		exchange.setBankAccount(rs.getString("bank_account"));
		exchange.setBankNo(rs.getLong("bank_no"));
		exchange.setAddress(rs.getString("address"));
		exchange.setName(rs.getString("name"));
		exchange.setExpressName(rs.getString("express_name"));
		exchange.setExpressNo(rs.getString("express_no"));
		exchange.setChannel(rs.getString("channel"));
		Timestamp ts = rs.getTimestamp("lastmod_time");
		if(ts != null)
			exchange.setLastModTime(new Date(ts.getTime()));
		ts = rs.getTimestamp("add_time");
		if(ts != null)
			exchange.setAddTime(new Date(ts.getTime()));
		exchange.setLastModUserid(rs.getLong("lastmod_userid"));
		return exchange;
	}
	
}