package com.iwami.iwami.app.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class TaskNotification {
	
	public static final int STATUS_NEW = 0;
	
	public static final int STATUS_SMS = 1;
	
	private int id;
	
	private long taskId;
	
	private long cellPhone;
	
	private String sms;
	
	private int status;
	
	private Date addTime;
	
	private Date lastModTime;

	private String smsName;
	
	private String smsReason;
	
	private Date smsStartDate;
	
	private Date smsEndDate;
	
	private int smsTotal;
	
	private int smsCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(Date lastModTime) {
		this.lastModTime = lastModTime;
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

	public long getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(long cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public String getSmsName() {
		return smsName;
	}

	public void setSmsName(String smsName) {
		this.smsName = smsName;
	}

	public String getSmsReason() {
		return smsReason;
	}

	public void setSmsReason(String smsReason) {
		this.smsReason = smsReason;
	}

	public Date getSmsStartDate() {
		return smsStartDate;
	}

	public void setSmsStartDate(Date smsStartDate) {
		this.smsStartDate = smsStartDate;
	}

	public Date getSmsEndDate() {
		return smsEndDate;
	}

	public void setSmsEndDate(Date smsEndDate) {
		this.smsEndDate = smsEndDate;
	}

	public int getSmsTotal() {
		return smsTotal;
	}

	public void setSmsTotal(int smsTotal) {
		this.smsTotal = smsTotal;
	}

	public int getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(int smsCount) {
		this.smsCount = smsCount;
	}

}
