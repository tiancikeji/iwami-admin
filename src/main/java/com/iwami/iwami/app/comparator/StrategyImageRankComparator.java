package com.iwami.iwami.app.comparator;

import java.util.Comparator;

import com.iwami.iwami.app.model.StrategyImage;

public class StrategyImageRankComparator implements Comparator<StrategyImage> {

	@Override
	public int compare(StrategyImage i1, StrategyImage i2) {
		int rank1 = Integer.MAX_VALUE;
		int rank2 = Integer.MAX_VALUE;
		
		if(i1 != null)
			rank1 = i1.getRank();
		if(i2 != null)
			rank2 = i2.getRank();
		
		return rank1 - rank2;
	}

}
