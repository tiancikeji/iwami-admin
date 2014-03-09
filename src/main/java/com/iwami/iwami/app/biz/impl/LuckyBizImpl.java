package com.iwami.iwami.app.biz.impl;

import java.util.Collections;
import java.util.List;

import com.iwami.iwami.app.biz.LuckyBiz;
import com.iwami.iwami.app.comparator.LuckyRuleComparator;
import com.iwami.iwami.app.model.LuckyRule;
import com.iwami.iwami.app.service.LuckyService;

public class LuckyBizImpl implements LuckyBiz {
	
	private LuckyService luckyService;

	@Override
	public List<LuckyRule> getLuckyRules() {
		List<LuckyRule> rules = luckyService.getLuckyRules();
		if(rules != null && rules.size() > 0)
			Collections.sort(rules, new LuckyRuleComparator());
		return rules;
	}

	@Override
	public boolean delRules(long adminid) {
		return luckyService.delRules(adminid);
	}

	@Override
	public boolean modRules(List<LuckyRule> rules) {
		return luckyService.modRules(rules);
	}

	public LuckyService getLuckyService() {
		return luckyService;
	}

	public void setLuckyService(LuckyService luckyService) {
		this.luckyService = luckyService;
	}
}
