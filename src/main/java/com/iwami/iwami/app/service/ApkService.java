package com.iwami.iwami.app.service;

import java.util.List;

import com.iwami.iwami.app.model.Apk;

public interface ApkService {

	public Apk getApk();

	public List<Apk> getApks();

	public boolean delAllApks();

	public boolean addApk(Apk apk);

	public boolean modApk(Apk apk);

	public boolean updateApkUrl(Apk apk);
}
