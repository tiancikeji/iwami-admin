package com.iwami.iwami.app.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class StrategyRate {

	private long strategyId;
	
	private int skim;
	
	private int rate;

	public long getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(long strategyId) {
		this.strategyId = strategyId;
	}

	public int getSkim() {
		return skim;
	}

	public void setSkim(int skim) {
		this.skim = skim;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
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
	
}
