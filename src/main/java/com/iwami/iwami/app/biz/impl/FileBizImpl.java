package com.iwami.iwami.app.biz.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.iwami.iwami.app.biz.FileBiz;
import com.iwami.iwami.app.util.AnalysisApk;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class FileBizImpl implements FileBiz {
	
	private String localDir = "/var/iwami/upload/file/";
	
	private String url = "http://yy.iwami.cn/file/";

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
				
				File _file = new File(tmp);
				fs = new FileOutputStream(_file);
			    fs.write(Base64.decode(data/*splits[1].split(",")[1]*/)); 
			    try{
				if(StringUtils.endsWith(name, ".apk"))
					_file.renameTo(new File(localDir + "." + System.currentTimeMillis() + "." + AnalysisApk.unZip(tmp)[1] + ".apk"));
			    } catch(Throwable t){
			    	t.printStackTrace();
			    }
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
}
