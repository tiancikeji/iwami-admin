package com.iwami.iwami.app.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Exchange {
	
	public static final int STATUS_NEW = 0;
	
	public static final int STATUS_FAILED = 1;
	
	public static final int STATUS_READY = 2;
	
	public static final int STATUS_FINISH = 3;

	private long id;
	
	private long userid;
	
	private long presentId;
	
	private String presentName;
	
	private int presentPrize;
	
	private int presentType;
	
	private int count;
	
	private int prize;
	
	private int status;
	
	private long cellPhone;
	
	private String alipayAccount;
	
	private String bankAccount;
	
	private long bankNo;
	
	private String bankName;
	
	private String address;
	
	private String name;
	
	private String expressName;
	
	private String expressNo;
	
	private String channel;
	
	private Date addTime;
	
	private Date lastModTime;
	
	private long lastModUserid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getPresentId() {
		return presentId;
	}

	public void setPresentId(long presentId) {
		this.presentId = presentId;
	}

	public String getPresentName() {
		return presentName;
	}

	public void setPresentName(String presentName) {
		this.presentName = presentName;
	}

	public int getPresentPrize() {
		return presentPrize;
	}

	public void setPresentPrize(int presentPrize) {
		this.presentPrize = presentPrize;
	}

	public int getPresentType() {
		return presentType;
	}

	public void setPresentType(int presentType) {
		this.presentType = presentType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPrize() {
		return prize;
	}

	public void setPrize(int prize) {
		this.prize = prize;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(long cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public long getBankNo() {
		return bankNo;
	}

	public void setBankNo(long bankNo) {
		this.bankNo = bankNo;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
}
