package com.iwami.iwami.app.service.impl;

import com.iwami.iwami.app.dao.OnstartDao;
import com.iwami.iwami.app.model.Onstart;
import com.iwami.iwami.app.service.OnstartService;

public class OnstartServiceImpl implements OnstartService {
	
	private OnstartDao onstartDao;

	@Override
	public boolean logOnstart(Onstart onstart) {
		if(onstart != null)
			return onstartDao.logOnstart(onstart);
		else
			return false;
	}

	public OnstartDao getOnstartDao() {
		return onstartDao;
	}

	public void setOnstartDao(OnstartDao onstartDao) {
		this.onstartDao = onstartDao;
	}
}
