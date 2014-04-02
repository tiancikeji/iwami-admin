package com.iwami.iwami.app.model;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ExchangeHistory {
	
	public static final int STATUS_NEW = 0;
	
	public static final int STATUS_SENT = 1;
	
	public static final int STATUS_OFFLINE = 2;

	private long userid;
	
	private int type;
	
	private long time;
	
	private List<Exchange> exchange;
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public List<Exchange> getExchange() {
		return exchange;
	}

	public void setExchange(List<Exchange> exchange) {
		this.exchange = exchange;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
