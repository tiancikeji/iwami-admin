package com.iwami.iwami.app.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.model.Exchange;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.model.Share;

public interface PresentDao {
	
	public List<Present> getPresentsByTypeNStatus(int type, String channel,List<Integer> status, int start, int step);

	public int getPresentCountByTypeNStatus(int type, String channel, List<Integer> status);

	public boolean modPresent(Present present);

	public boolean delPresent(long id, long adminid);

	public boolean addPresent(Present present);

	public boolean updatePresentURL(Present present);

	public boolean seqPresent(Map<Long, Integer> data, long adminid);

	public List<Exchange> getExchangeHistory(List<Integer> types, List<Integer> status);

	public List<Exchange> getExchangeHistoryByUser(List<Integer> types, long key);

	public List<Exchange> getExchangeHistoryByPresent(List<Integer> types, String key);

	public List<Exchange> getExchangeByIds(List<Long> ids);

	public boolean modExchange(String name, String no, List<Long> ids, long adminid);

	public List<Exchange> getExchanges(Date start, Date end);

	public List<Share> getShares(Date start, Date end);

	public List<Exchange> getGifts(Date start, Date end);

	public List<Present> getPresents();

}
