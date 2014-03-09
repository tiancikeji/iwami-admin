package com.iwami.iwami.app.service;

import java.util.List;

import com.iwami.iwami.app.model.LuckyRule;

public interface LuckyService {

	public List<LuckyRule> getLuckyRules();

	public boolean delRules(long adminid);

	public boolean modRules(List<LuckyRule> rules);
}
