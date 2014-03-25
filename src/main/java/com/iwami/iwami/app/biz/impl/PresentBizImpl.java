package com.iwami.iwami.app.biz.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.iwami.iwami.app.biz.PresentBiz;
import com.iwami.iwami.app.comparator.ExchangeHistoryComparator;
import com.iwami.iwami.app.model.Exchange;
import com.iwami.iwami.app.model.ExchangeHistory;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.service.PresentService;

public class PresentBizImpl implements PresentBiz {
	
	private PresentService presentService;
	
	@Override
	public List<Present> getPresentsByTypeNStatus(int type, List<Integer> status) {
		return presentService.getPresentsByTypeNStatus(type, status);
	}

	@Override
	public boolean modPresent(Present present) {
		return presentService.modPresent(present);
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean addPresent(Present present) {
		if(presentService.addPresent(present)){
			return true;
		} else
			return false;
	}

	@Override
	public boolean delPresent(long id, long adminid) {
		return presentService.delPresent(id, adminid);
	}

	@Override
	public boolean seqPresent(Map<Long, Integer> data, long adminid) {
		return presentService.seqPresent(data, adminid);
	}

	@Override
	public List<ExchangeHistory> getExchangeHistory(List<Integer> types, List<Integer> status) {
		List<Exchange> exchanges = presentService.getExchangeHistory(types, status);
		return transform(exchanges);
	}

	@Override
	public List<ExchangeHistory> getExchangeHistoryByUser(List<Integer> types, long type) {
		List<Exchange> exchanges = presentService.getExchangeHistoryByUser(types, type);
		return transform(exchanges);
	}

	@Override
	public List<ExchangeHistory> getExchangeHistoryByPresent(List<Integer> types, String type) {
		List<Exchange> exchanges = presentService.getExchangeHistoryByPresent(types, type);
		return transform(exchanges);
	}

	@Override
	public Exchange getExchangeById(long id) {
		return presentService.getExchangeById(id);
	}

	@Override
	public boolean modExchange(String name, String no, long id, long adminid) {
		return presentService.modExchange(name, no, id, adminid);
	}

	private List<ExchangeHistory> transform(List<Exchange> exchanges) {
		Map<Long, ExchangeHistory> result = new HashMap<Long, ExchangeHistory>();
		
		if(exchanges != null && exchanges.size() > 0)
			for(Exchange exchange : exchanges){
				int status = ExchangeHistory.STATUS_NEW;
				if(exchange.getStatus() == Exchange.STATUS_FINISH)
					status = ExchangeHistory.STATUS_SENT;
				if(exchange.getPresentType() == Present.TYPE_OFFLINE)
					status = ExchangeHistory.STATUS_OFFLINE;
				exchange.setStatus(status);
				
				ExchangeHistory history = result.get(exchange.getUserid());
				if(history == null){
					history = new ExchangeHistory();
					history.setUserid(exchange.getUserid());
					result.put(exchange.getUserid(), history);
				}
				
				List<Exchange> tmps = history.getExchange();
				if(tmps == null){
					tmps = new ArrayList<Exchange>();
					history.setExchange(tmps);
				}
				
				tmps.add(exchange);
			}
		
		List<ExchangeHistory> tmp = new ArrayList<ExchangeHistory>(result.values());
		
		for(ExchangeHistory history : tmp)
			Collections.sort(history.getExchange(), new ExchangeHistoryComparator());
		
		return tmp;
	}

	public PresentService getPresentService() {
		return presentService;
	}

	public void setPresentService(PresentService presentService) {
		this.presentService = presentService;
	}

	@Override
	public List<Present> getPresents() {
		List<Present> presents = presentService.getPresents();
		
		if(presents != null && presents.size() > 0)
			Collections.sort(presents, new Comparator<Present>() {

				@Override
				public int compare(Present o1, Present o2) {
					return o1.getRank() - o2.getRank();
				}
			});
		
		return presents;
	}
}
