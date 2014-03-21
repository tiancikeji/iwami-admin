package com.iwami.iwami.app.dao;

import java.util.List;

import com.iwami.iwami.app.model.Apk;

public interface ApkDao {

	public Apk getApk();

	public List<Apk> getApks();
	
	public boolean addApk(Apk apk);

	public boolean delAllApks();

	public boolean modApk(Apk apk);

	public boolean updateApkUrl(Apk apk);
}
