package com.iwami.iwami.app.biz;

import java.util.List;

import com.iwami.iwami.app.exception.LuckyExceedLimitException;
import com.iwami.iwami.app.exception.NotEnoughPrizeException;
import com.iwami.iwami.app.model.LuckyConfig;
import com.iwami.iwami.app.model.LuckyRule;
import com.iwami.iwami.app.model.User;

public interface LuckyBiz {

	public List<LuckyRule> getLuckyRules();

	public LuckyConfig getLuckyConfig();

	public LuckyRule draw(User user, LuckyConfig config) throws LuckyExceedLimitException, NotEnoughPrizeException;
}
