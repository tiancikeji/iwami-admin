package com.iwami.iwami.app.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Task {

	public static final int STATUS_READY = -1;

	public static final int STATUS_START = 0;

	public static final int STATUS_DOWNLOADING = 1;

	public static final int STATUS_DOWNLOADED = 2;

	public static final int STATUS_INSTALLING = 3;

	public static final int STATUS_INSTALLED = 4;

	public static final int STATUS_FINISH = 5;

	private long id;
	
	private String name;
	
	private int rank;
	
	private double size;
	
	private String intr;
	
	private String appIntr;
	
	private int prize;
	
	private int type;
	
	private int background;
	
	private int time;
	
	private int register;
	
	private int reputation;
	
	private int star;
	
	private Date startTime;
	
	private Date endTime;
	
	private int currentPrize;
	
	private int maxPrize;
	
	private int available;
	
	private String iconGray;
	
	private String iconSmall;
	
	private String iconBig;
	
	private int status;
	
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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public String getIntr() {
		return intr;
	}

	public void setIntr(String intr) {
		this.intr = intr;
	}

	public String getAppIntr() {
		return appIntr;
	}

	public void setAppIntr(String appIntr) {
		this.appIntr = appIntr;
	}

	public int getPrize() {
		return prize;
	}

	public void setPrize(int prize) {
		this.prize = prize;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBackground() {
		return background;
	}

	public void setBackground(int background) {
		this.background = background;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getRegister() {
		return register;
	}

	public void setRegister(int register) {
		this.register = register;
	}

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getCurrentPrize() {
		return currentPrize;
	}

	public void setCurrentPrize(int currentPrize) {
		this.currentPrize = currentPrize;
	}

	public int getMaxPrize() {
		return maxPrize;
	}

	public void setMaxPrize(int maxPrize) {
		this.maxPrize = maxPrize;
	}

	public String getIconGray() {
		return iconGray;
	}

	public void setIconGray(String iconGray) {
		this.iconGray = iconGray;
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

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
