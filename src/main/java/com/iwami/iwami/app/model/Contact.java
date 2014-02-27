package com.iwami.iwami.app.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Contact {
	
	private long id;
	
	private String phone1;
	
	private String email1;
	
	private String domain;
	
	private long qq;
	
	private String qqgroup;
	
	private String phone2;
	
	private String email2;
	
	private Date lastmodTime;
	
	private long lastmodUserid;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public long getQq() {
		return qq;
	}

	public void setQq(long qq) {
		this.qq = qq;
	}

	public String getQqgroup() {
		return qqgroup;
	}

	public void setQqgroup(String qqgroup) {
		this.qqgroup = qqgroup;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
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
	
}
