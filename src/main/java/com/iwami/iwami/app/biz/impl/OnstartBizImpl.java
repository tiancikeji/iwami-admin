package com.iwami.iwami.app.biz.impl;

import com.iwami.iwami.app.biz.OnstartBiz;
import com.iwami.iwami.app.model.Onstart;
import com.iwami.iwami.app.service.OnstartService;

public class OnstartBizImpl implements OnstartBiz {

	private OnstartService onstartService;

	@Override
	public boolean logOnstart(Onstart onstart) {
		if(onstart != null)
			return onstartService.logOnstart(onstart);
		else
			return false;
	}

	public OnstartService getOnstartService() {
		return onstartService;
	}

	public void setOnstartService(OnstartService onstartService) {
		this.onstartService = onstartService;
	}
	
}
