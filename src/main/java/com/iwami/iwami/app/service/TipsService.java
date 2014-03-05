package com.iwami.iwami.app.service;

import java.util.List;

import com.iwami.iwami.app.model.Tips;

public interface TipsService {

	public List<Tips> getTips();

	public boolean delTipsByType(int type);

	public boolean addTip(Tips tip);
}
