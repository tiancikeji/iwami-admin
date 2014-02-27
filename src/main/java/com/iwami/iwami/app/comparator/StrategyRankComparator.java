package com.iwami.iwami.app.comparator;

import java.util.Comparator;

import com.iwami.iwami.app.model.Strategy;

public class StrategyRankComparator implements Comparator<Strategy> {

	@Override
	public int compare(Strategy s1, Strategy s2) {
		int rank1 = Integer.MAX_VALUE;
		int rank2 = Integer.MAX_VALUE;
		
		if(s1 != null)
			rank1 = s1.getRank();
		if(s2 != null)
			rank2 = s2.getRank();
		
		return rank1 - rank2;
	}

}
