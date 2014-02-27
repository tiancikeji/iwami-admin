package com.iwami.iwami.app.comparator;

import java.util.Comparator;

import com.iwami.iwami.app.model.Present;

public class PresentRankComparator implements Comparator<Present> {

	@Override
	public int compare(Present p1, Present p2) {
		int rank1 = Integer.MAX_VALUE;
		int rank2 = Integer.MAX_VALUE;
		
		if(p1 != null)
			rank1 = p1.getRank();
		if(p2 != null)
			rank2 = p2.getRank();
		
		return rank1 - rank2;
	}

}
