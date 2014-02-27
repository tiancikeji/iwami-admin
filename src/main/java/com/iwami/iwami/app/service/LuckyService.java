package com.iwami.iwami.app.service;

import java.util.List;

import com.iwami.iwami.app.model.LuckyConfig;
import com.iwami.iwami.app.model.LuckyRule;

public interface LuckyService {

	public List<LuckyRule> getLuckyRules();

	public LuckyConfig getLuckyConfig();
}
