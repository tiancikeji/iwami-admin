package com.iwami.iwami.app.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class User {

	private long id;
	
	private int currentPrize;
	
	private int exchangePrize;
	
	private long lastCellPhone1;
	
	private String lastAlipayAccount;
	
	private String lastBankAccount;
	
	private String lastBankName;
	
	private long lastBankNo;
	
	private String lastAddres;
	
	private long lastCellPhone2;
	
	private String lastName;
	
	private Date lastmodTime;
	
	private long lastmodUserid;
	
	// userinfo table
	private String name;
	
	private String uuid;
	
	private String alias;
	
	private long cellPhone;
	
	private int age;
	
	private int gender;
	
	private String job;
	
	private String address;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCurrentPrize() {
		return currentPrize;
	}

	public void setCurrentPrize(int currentPrize) {
		this.currentPrize = currentPrize;
	}

	public int getExchangePrize() {
		return exchangePrize;
	}

	public void setExchangePrize(int exchangePrize) {
		this.exchangePrize = exchangePrize;
	}

	public long getLastCellPhone1() {
		return lastCellPhone1;
	}

	public void setLastCellPhone1(long lastCellPhone1) {
		this.lastCellPhone1 = lastCellPhone1;
	}

	public String getLastAlipayAccount() {
		return lastAlipayAccount;
	}

	public void setLastAlipayAccount(String lastAlipayAccount) {
		this.lastAlipayAccount = lastAlipayAccount;
	}

	public String getLastBankName() {
		return lastBankName;
	}

	public void setLastBankName(String lastBankName) {
		this.lastBankName = lastBankName;
	}

	public String getLastAddres() {
		return lastAddres;
	}

	public void setLastAddres(String lastAddres) {
		this.lastAddres = lastAddres;
	}

	public long getLastCellPhone2() {
		return lastCellPhone2;
	}

	public void setLastCellPhone2(long lastCellPhone2) {
		this.lastCellPhone2 = lastCellPhone2;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public long getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(long cellPhone) {
		this.cellPhone = cellPhone;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public long getLastBankNo() {
		return lastBankNo;
	}

	public void setLastBankNo(long lastBankNo) {
		this.lastBankNo = lastBankNo;
	}

	public void setLastBankAccount(String lastBankAccount) {
		this.lastBankAccount = lastBankAccount;
	}

	public String getLastBankAccount() {
		return lastBankAccount;
	}
}
