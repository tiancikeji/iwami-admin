package com.iwami.iwami.app.biz.impl;

import com.iwami.iwami.app.biz.TipsBiz;
import com.iwami.iwami.app.model.Tips;
import com.iwami.iwami.app.service.TipsService;

public class TipsBizImpl implements TipsBiz {

	private TipsService tipsService;

	@Override
	public Tips getTips(int type) {
		return tipsService.getTips(type);
	}

	public TipsService getTipsService() {
		return tipsService;
	}

	public void setTipsService(TipsService tipsService) {
		this.tipsService = tipsService;
	}

}
