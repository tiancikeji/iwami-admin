package com.iwami.iwami.app.biz.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.iwami.iwami.app.biz.TipsBiz;
import com.iwami.iwami.app.model.Tips;
import com.iwami.iwami.app.service.TipsService;

public class TipsBizImpl implements TipsBiz {

	private TipsService tipsService;

	@Override
	public List<Tips> getTips() {
		return tipsService.getTips();
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean addTip(Tips tip) {
		Tips tmp = null;
		List<Tips> tips = getTips();
		if(tips != null && tips.size() > 0)
			for(Tips _tip : tips)
				if(_tip != null && _tip.getType() == tip.getType()){
					tmp = _tip;
					break;
				}
		
		if(tmp == null || tipsService.delTipsByType(tip.getType()))
			if(tipsService.addTip(tip))
				return true;
			else
				throw new RuntimeException("Failed in addTip, so rollback");
		else
			return false;
	}

	public TipsService getTipsService() {
		return tipsService;
	}

	public void setTipsService(TipsService tipsService) {
		this.tipsService = tipsService;
	}

}
