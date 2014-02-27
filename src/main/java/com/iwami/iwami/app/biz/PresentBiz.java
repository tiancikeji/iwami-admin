package com.iwami.iwami.app.biz;

import java.util.List;
import java.util.Map;

import com.iwami.iwami.app.exception.NotEnoughPrizeException;
import com.iwami.iwami.app.model.ExchangeHistory;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.model.User;

public interface PresentBiz {

	public List<Present> getAllPresents(long userid);

	public boolean gift(User user, User user2, int prize) throws NotEnoughPrizeException;
	
	// only for transaction control, not open for other callers.
	public void doGift(User user, User user2, int prize) throws NotEnoughPrizeException;
	// end of transaction control

	public boolean addShareExchange(long userid, int type, int target, String msg);

	public Map<Long, Present> getPresentsByIds(List<Long> ids);

	public boolean exchangeExpress(User user, Map<Present, Integer> presentCnts, long cellPhone, String address, String name) throws NotEnoughPrizeException;

	public boolean exchangeAlipay(User user, Present present, int prize, String aliAccount);

	public boolean exchangeBank(User user, Present present, int prize, String bankAccount, String bankName, long bankNo);

	public boolean exchangeMobile(User user, Present present, long cellPhone);

	public List<ExchangeHistory> getExchangeHistory(long userid);

	public Map<Long, Present> getOfflinePresentsByIds(List<Long> ids);

	public boolean exchangeOffline(User user, Map<Present, Integer> presentCnts, String channel);
}
