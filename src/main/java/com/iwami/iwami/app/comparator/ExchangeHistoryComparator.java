package com.iwami.iwami.app.comparator;

import java.util.Comparator;

import com.iwami.iwami.app.model.ExchangeHistory;

public class ExchangeHistoryComparator implements Comparator<ExchangeHistory> {

	@Override
	public int compare(ExchangeHistory e1, ExchangeHistory e2) {
		long t1 = Long.MAX_VALUE;
		long t2 = Long.MAX_VALUE;
		int s1 = Integer.MAX_VALUE;
		int s2 = Integer.MAX_VALUE;
		int p1 = Integer.MAX_VALUE;
		int p2 = Integer.MAX_VALUE;
		
		if(e1 != null){
			t1 = e1.getTime();
			s1 = e1.getStatus();
			p1 = e1.getType();
		}
		if(e2 != null){
			t2 = e2.getTime();
			s2 = e2.getStatus();
			p2 = e2.getType();
		}
		
		int count = (int)(t1 - t2);
		if(count == 0)
			count = s1 - s2;
		if(count == 0)
			count = p1 - p2;
		
		return count;
	}

}
