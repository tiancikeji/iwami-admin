package com.iwami.iwami.app.biz.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.iwami.iwami.app.biz.FileBiz;
import com.iwami.iwami.app.model.Apk;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.model.Strategy;
import com.iwami.iwami.app.model.StrategyImage;
import com.iwami.iwami.app.model.StrategyInfo;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.service.CDNService;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class FileBizImpl implements FileBiz {
	
	private String localDir = "/var/iwami/upload/file/";
	
	private String url = "http://115.28.232.121/file/";
	
	private CDNService cdnService;

	@Override
	public String addFile(String data, String name) {
		String result = StringUtils.EMPTY;
		if(StringUtils.isNotBlank(data)){
			FileOutputStream fs = null;
			try {
//				String[] splits = StringUtils.split(data, ";");
				result = System.currentTimeMillis() + "." + name;
				String tmp = localDir + result;
				
				File dir = new File(localDir);
				if(!(dir.exists() && dir.isDirectory()))
					dir.mkdirs();
				
				fs = new FileOutputStream(tmp);
			    fs.write(Base64.decode(data/*splits[1].split(",")[1]*/)); 
			} catch (Throwable t) {
				t.printStackTrace();
			} finally{
				if(fs != null)
					try {
						fs.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		
		return url + result;
	}

	@Override
	public boolean uploadTaskResource(Task task) {
		// TODO upload to CDN
		if(StringUtils.startsWith(task.getIconSmall(), url))
			task.setIconSmall(cdnService.uploadFile(task.getIconSmall()));

		if(StringUtils.startsWith(task.getIconBig(), url))
			task.setIconBig(cdnService.uploadFile(task.getIconBig()));

		if(StringUtils.startsWith(task.getIconGray(), url))
			task.setIconGray(cdnService.uploadFile(task.getIconGray()));

		if(StringUtils.startsWith(task.getUrl(), url))
			task.setUrl(cdnService.uploadFile(task.getUrl()));
		return true;
	}

	@Override
	public boolean uploadImageResource(StrategyImage image) {
		// TODO Auto-generated method stub
		if(StringUtils.startsWith(image.getIconUrl(), url))
			image.setIconUrl(cdnService.uploadFile(image.getIconUrl()));
		return true;
	}

	@Override
	public boolean uploadStrategyResource(Strategy strategy) {
		// TODO Auto-generated method stub
		if(StringUtils.startsWith(strategy.getIconSmall(), url))
			strategy.setIconSmall(cdnService.uploadFile(strategy.getIconSmall()));
		
		if(StringUtils.startsWith(strategy.getIconBig(), url))
			strategy.setIconBig(cdnService.uploadFile(strategy.getIconBig()));
		
		return true;
	}

	@Override
	public boolean uploadStrategyInfoResource(StrategyInfo info) {
		// TODO Auto-generated method stub
		if(StringUtils.startsWith(info.getUrl(), url))
			info.setUrl(cdnService.uploadFile(info.getUrl()));
		
		return true;
	}

	@Override
	public boolean uploadApkResource(Apk apk) {
		// TODO Auto-generated method stub
		if(StringUtils.startsWith(apk.getUrl(), url))
			apk.setUrl(cdnService.uploadFile(apk.getUrl()));
		
		return true;
	}

	@Override
	public boolean uploadPresentResource(Present present) {
		// TODO Auto-generated method stub
		if(StringUtils.startsWith(present.getIconSmall(), url))
			present.setIconSmall(cdnService.uploadFile(present.getIconSmall()));
		
		if(StringUtils.startsWith(present.getIconBig(), url))
			present.setIconBig(cdnService.uploadFile(present.getIconBig()));
		
		return true;
	}

	public String getLocalDir() {
		return localDir;
	}

	public void setLocalDir(String localDir) {
		this.localDir = localDir;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public CDNService getCdnService() {
		return cdnService;
	}

	public void setCdnService(CDNService cdnService) {
		this.cdnService = cdnService;
	}

}
