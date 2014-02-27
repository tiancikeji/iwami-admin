package com.iwami.iwami.app.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.model.Exchange;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.model.Share;

public interface PresentDao {
	
	public List<Present> getAllPresents();

	public long addExchange(Exchange exchange);

	public void updateExchangeStatus(long id, int status);

	public boolean addShareExchange(Share share);

	public Map<Long, Present> getPresentsByIds(List<Long> ids);

	public void updateExchangeStatus(List<Long> ids, int status);

	public int getLuckyExchangeCount(long presentid, Date date);

	public List<Exchange> getAllExchanges(long userid);

}
