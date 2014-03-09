package com.iwami.iwami.app.comparator;

import java.util.Comparator;

import com.iwami.iwami.app.model.Exchange;

public class ExchangeHistoryComparator implements Comparator<Exchange> {

	@Override
	public int compare(Exchange e1, Exchange e2) {
		long t1 = Long.MAX_VALUE;
		long t2 = Long.MAX_VALUE;
		
		if(e1 != null){
			t1 = e1.getPresentType();
		}
		if(e2 != null){
			t2 = e2.getPresentType();
		}
		
		int count = (int)(t1 - t2);
		return count;
	}

}
