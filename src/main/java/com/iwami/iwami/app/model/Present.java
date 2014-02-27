package com.iwami.iwami.app.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Present {
	
	public static final int TYPE_ONLINE_EMS = 0;
	
	public static final int TYPE_ONLINE_RECHARGE_MOBILE = 1;
	
	public static final int TYPE_ONLINE_RECHARGE_ALIPAY = 2;
	
	public static final int TYPE_ONLINE_RECHARGE_BANK = 3;
	
	public static final int TYPE_OFFLINE = 4;
	
	public static final int TYPE_LUCK = 5;
	
	public static final int TYPE_GIFT = 6;
	
	private long id;
	
	private String name;
	
	private int prize;
	
	private int count;
	
	private int rank;
	
	private int type;

	private String iconSmall;
	
	private String iconBig;
	
	private Date lastModTime;
	
	private long lastModUserid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrize() {
		return prize;
	}

	public void setPrize(int prize) {
		this.prize = prize;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIconSmall() {
		return iconSmall;
	}

	public void setIconSmall(String iconSmall) {
		this.iconSmall = iconSmall;
	}

	public String getIconBig() {
		return iconBig;
	}

	public void setIconBig(String iconBig) {
		this.iconBig = iconBig;
	}

	public Date getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(Date lastModTime) {
		this.lastModTime = lastModTime;
	}

	public long getLastModUserid() {
		return lastModUserid;
	}

	public void setLastModUserid(long lastModUserid) {
		this.lastModUserid = lastModUserid;
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
