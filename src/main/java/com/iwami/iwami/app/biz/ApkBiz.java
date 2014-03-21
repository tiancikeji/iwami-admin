package com.iwami.iwami.app.biz;

import java.util.List;

import com.iwami.iwami.app.model.Apk;

public interface ApkBiz {
	
	public List<Apk> getApks();

	public boolean addApk(Apk apk);

	public boolean modApk(Apk apk);

	public Apk getApk();
}
