package com.iwami.iwami.app.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


public class ReportParam {
	
	public static final int TYPE_OVERVIEW_REPORT = 1;
	
	public static final int TYPE_TASK_TOP = 2;
	
	public static final int TYPE_TASK_TREASURE = 3;
	
	public static final int TYPE_TASK_ID = 4;
	
	public static final int TYPE_TASK_WAMI = 5;
	
	public static final int TYPE_TASK_HISTORY = 6;
	
	public static final int TYPE_EXCHANGE = 7;
	
	public static final int TYPE_GIFT = 8;
	
	public static final int TYPE_USER_INFO = 9;
	
	public static final int TYPE_USER_LOGIN = 10;
	
	public static final int TYPE_PRESENT_SUMMARY = 11;
	
	public static final int TYPE_PRESENT_HISTORY = 12;
	
	public static final int TYPE_PRESENT_OFFLINE = 13;
	
	public static final int TYPE_SHARE = 14;
	
	public static final int TYPE_APK_DOWNLOAD = 15;
	
	public static final Map<Integer, String> TYPE_TITLES = new HashMap<Integer, String>();
	
	static {
		TYPE_TITLES.put(TYPE_OVERVIEW_REPORT, "运营月报");
		TYPE_TITLES.put(TYPE_TASK_TOP, "金榜下载");
		TYPE_TITLES.put(TYPE_TASK_TREASURE, "红包");
		TYPE_TITLES.put(TYPE_TASK_ID, "APP结算");
		TYPE_TITLES.put(TYPE_TASK_WAMI, "挖米清单");
		TYPE_TITLES.put(TYPE_TASK_HISTORY, "运行历史");
		TYPE_TITLES.put(TYPE_EXCHANGE, "兑换详情");
		TYPE_TITLES.put(TYPE_GIFT, "米粒赠送");
		TYPE_TITLES.put(TYPE_USER_INFO, "用户资料");
		TYPE_TITLES.put(TYPE_USER_LOGIN, "用户登录情况");
		TYPE_TITLES.put(TYPE_PRESENT_SUMMARY, "发货月汇总");
		TYPE_TITLES.put(TYPE_PRESENT_HISTORY, "发货记录");
		TYPE_TITLES.put(TYPE_PRESENT_OFFLINE, "现场活动");
		TYPE_TITLES.put(TYPE_SHARE, "分享详情");
		TYPE_TITLES.put(TYPE_APK_DOWNLOAD, "官网APP下载");
	}
	
	private int type;
	
	private String start;
	
	private String end;
	
	private String key;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
