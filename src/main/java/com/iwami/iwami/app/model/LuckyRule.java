package com.iwami.iwami.app.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class LuckyRule {

	private long id;
	
	private int indexLevel;
	
	private String gift;
	
	private int prob;
	
	private int count;
	
	private Date lastmodTime;
	
	private long lastmodUserid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIndexLevel() {
		return indexLevel;
	}

	public void setIndexLevel(int indexLevel) {
		this.indexLevel = indexLevel;
	}

	public String getGift() {
		return gift;
	}

	public void setGift(String gift) {
		this.gift = gift;
	}

	public int getProb() {
		return prob;
	}

	public void setProb(int prob) {
		this.prob = prob;
	}

	public Date getLastmodTime() {
		return lastmodTime;
	}

	public void setLastmodTime(Date lastmodTime) {
		this.lastmodTime = lastmodTime;
	}

	public long getLastmodUserid() {
		return lastmodUserid;
	}

	public void setLastmodUserid(long lastmodUserid) {
		this.lastmodUserid = lastmodUserid;
	}

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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
