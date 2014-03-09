package com.iwami.iwami.app.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Push {
	
	public static int STATUS_NEW = 0;
	
	public static int STATUS_PAUSE = 1;
	
	public static int STATUS_STOP = 2;
	
	public static int STATUS_RESUME = 3;
	
	public static int STATUS_PUSHED = 4;
	
	public static int STATUS_SENT = 5;
	
	private long id;
	
	private long interval;
	
	private String msg;
	
	private int status;
	
	private String cellPhone;
	
	private int allCnt;
	
	private int succCnt;
	
	private int failCnt;
	
	private Date addTime;
	
	private Date estimateTime;
	
	private Date lastModTime;
	
	private long lastModUserid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getEstimateTime() {
		return estimateTime;
	}

	public void setEstimateTime(Date estimateTime) {
		this.estimateTime = estimateTime;
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

	public int getAllCnt() {
		return allCnt;
	}

	public void setAllCnt(int allCnt) {
		this.allCnt = allCnt;
	}

	public int getSuccCnt() {
		return succCnt;
	}

	public void setSuccCnt(int succCnt) {
		this.succCnt = succCnt;
	}

	public int getFailCnt() {
		return failCnt;
	}

	public void setFailCnt(int failCnt) {
		this.failCnt = failCnt;
	}

}
