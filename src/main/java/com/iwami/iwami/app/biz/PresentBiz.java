package com.iwami.iwami.app.biz;

import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.model.Exchange;
import com.iwami.iwami.app.model.ExchangeHistory;
import com.iwami.iwami.app.model.Present;

public interface PresentBiz {

	public List<Present> getPresentsByTypeNStatus(int type, List<Integer> status, int start, int step);

	public int getPresentCountByTypeNStatus(int type, List<Integer> status);

	public List<Present> getPresentsByTypeNStatus(int type, String channel, List<Integer> status, int start, int step);

	public int getPresentCountByTypeNStatus(int type, String channel, List<Integer> status);

	public boolean modPresent(Present present);

	public boolean delPresent(long id, long adminid);

	public boolean addPresent(Present present);

	public boolean seqPresent(Map<Long, Integer> data, long adminid);

	public List<ExchangeHistory> getExchangeHistory(List<Integer> types, List<Integer> status);

	public List<ExchangeHistory> getExchangeHistoryByUser(List<Integer> types, long key);

	public List<ExchangeHistory> getExchangeHistoryByPresent( List<Integer> types, String key);

	public boolean modExchange(String name, String no, List<Long> ids, long adminid);

	public List<Present> getPresents();

	public List<Exchange> getExchangeByIds(List<Long> ids);
}
