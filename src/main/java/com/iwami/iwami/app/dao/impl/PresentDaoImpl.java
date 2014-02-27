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

import org.nuxeo.common.utils.StringUtils;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.iwami.iwami.app.constants.SqlConstants;
import com.iwami.iwami.app.dao.PresentDao;
import com.iwami.iwami.app.model.Exchange;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.model.Share;

public class PresentDaoImpl extends JdbcDaoSupport implements PresentDao {

	@Override
	public List<Present> getAllPresents() {
		return getJdbcTemplate().query("select id, name, prize, `count`, rank, type, icon_small, icon_big, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_PRESENT + " where isdel = ? and type <> ?", new Object[]{0, Present.TYPE_OFFLINE}, new PresentRowMapper());
	}

	@Override
	public long addExchange(final Exchange exchange) {
		KeyHolder holder = new GeneratedKeyHolder();
		int count = getJdbcTemplate().update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement("insert into " + SqlConstants.TABLE_EXCHANGE + "(userid, presentid, present_name, present_prize, present_type, count, prize, status, cell_phone, alipay_account, bank_account, bank_no, bank_name, address, name, express_name, express_no, channel, add_time, lastmod_time, lastmod_userid, isdel) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now(), ?, ?)", Statement.RETURN_GENERATED_KEYS);
				ps.setObject(1, exchange.getUserid());
				ps.setObject(2, exchange.getPresentId());
				ps.setObject(3, exchange.getPresentName());
				ps.setObject(4, exchange.getPresentPrize());
				ps.setObject(5, exchange.getPresentType());
				ps.setObject(6, exchange.getCount());
				ps.setObject(7, exchange.getPrize());
				ps.setObject(8, exchange.getStatus());
				ps.setObject(9, exchange.getCellPhone());
				ps.setObject(10, exchange.getAlipayAccount());
				ps.setObject(11, exchange.getBankAccount());
				ps.setObject(12, exchange.getBankNo());
				ps.setObject(13, exchange.getBankName());
				ps.setObject(14, exchange.getAddress());
				ps.setObject(15, exchange.getName());
				ps.setObject(16, exchange.getExpressName());
				ps.setObject(17, exchange.getExpressNo());
				ps.setObject(18, exchange.getChannel());
				ps.setObject(19, exchange.getLastModUserid());
				ps.setObject(20, 0);
				return ps;
			}
		}, holder);
		
		if(count > 0 && holder.getKey() != null)
			return holder.getKey().longValue();
		
		return 0;
	}

	@Override
	public void updateExchangeStatus(long id, int status) {
		getJdbcTemplate().update("update " + SqlConstants.TABLE_EXCHANGE + " set status = ?, lastmod_time = now() where id = ?", new Object[]{status, id});
	}

	@Override
	public void updateExchangeStatus(List<Long> ids, int status) {
		getJdbcTemplate().update("update " + SqlConstants.TABLE_EXCHANGE + " set status = ?, lastmod_time = now() where id in (" + StringUtils.join(ids.toArray(), ",") + ")", new Object[]{status});
		}

	@Override
	public boolean addShareExchange(Share share) {
		int count = getJdbcTemplate().update("insert into " + SqlConstants.TABLE_SHARE + "(userid, type, target, msg, lastmod_time) values(?, ?, ?, ?, now())",
				new Object[]{share.getUserid(), share.getType(), share.getTarget(), share.getMsg()});
		return count > 0;
	}

	@Override
	public Map<Long, Present> getPresentsByIds(List<Long> ids) {
		List<Present> tmp = getJdbcTemplate().query("select id, name, prize, `count`, rank, type, icon_small, icon_big, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_PRESENT + " where isdel = ? and id in (" + StringUtils.join(ids.toArray(), ",") + ")", new Object[]{0}, new PresentRowMapper());
		Map<Long, Present> presents = new HashMap<Long, Present>();
		
		if(tmp != null && tmp.size() > 0)
			for(Present present : tmp)
				presents.put(present.getId(), present);
		
		return presents;
	}

	@Override
	public int getLuckyExchangeCount(long presentid, Date date) {
		return getJdbcTemplate().queryForInt("select count(1) from " + SqlConstants.TABLE_EXCHANGE + " where presentid = ? and add_time >= ? and present_type = ?", new Object[]{presentid, date, Present.TYPE_LUCK});
	}

	@Override
	public List<Exchange> getAllExchanges(long userid) {
		return getJdbcTemplate().query("select id, userid, presentid, present_name, present_prize, present_type, count, prize, status, cell_phone, alipay_account, bank_account, bank_no, bank_name, address, name, express_name, express_no, channel, add_time, lastmod_time, lastmod_userid from " + SqlConstants.TABLE_EXCHANGE + " where isdel = ? and userid = ? and presentid > 0 and status in (" + Exchange.STATUS_FINISH + "," + Exchange.STATUS_READY + ")", new Object[]{0, userid}, new RowMapper<Exchange>(){

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
		
		return present;
	}
}