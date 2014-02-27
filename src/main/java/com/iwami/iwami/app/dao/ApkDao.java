package com.iwami.iwami.app.dao;

import com.iwami.iwami.app.model.Apk;

public interface ApkDao {

	public Apk getApk();
	
	public boolean delApk(long id);
	
	public boolean addApk(Apk apk);
}
