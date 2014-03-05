package com.iwami.iwami.app.biz;

import java.util.List;

import com.iwami.iwami.app.model.Tips;

public interface TipsBiz {
	
	public List<Tips> getTips();

	public boolean addTip(Tips tip);

}
