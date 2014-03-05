package com.iwami.iwami.app.dao;

import java.util.List;

import com.iwami.iwami.app.model.Tips;

public interface TipsDao {
	
	public List<Tips> getAllTips();

	public boolean addTip(Tips tip);

	public boolean delTipsByType(int type);
	
}
