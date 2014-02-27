package com.iwami.iwami.app.comparator;

import java.util.Comparator;

import com.iwami.iwami.app.model.LuckyRule;

public class LuckyRuleComparator implements Comparator<LuckyRule> {

	@Override
	public int compare(LuckyRule rule1, LuckyRule rule2) {
		if(rule1 != null && rule2 != null){
			int lev1 = rule1.getIndexLevel();
			int lev2 = rule2.getIndexLevel();
			return lev2 - lev1;
		}
		return 0;
	}

}
