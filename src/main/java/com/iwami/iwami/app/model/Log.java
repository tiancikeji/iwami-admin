package com.iwami.iwami.app.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Log {
	
	public static final int TYPE_STRATEGY_LIST = 1;
	
	public static final int TYPE_STRATEGY_DETAIL = 2;
	
	public static final int TYPE_TASK_TOP = 3;
	
	public static final int TYPE_TASK_TASK = 4;
	
	public static final int TYPE_TASK_TREASURE = 5;
	
	public static final int TYPE_APP_DOWNLOAD = 6;
	
	public static final int TYPE_LOGIN = 7;
	
	private long userid;
	
	private int type;
	
	private String msg;
	
	private Date addTime;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
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

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
