package com.iwami.iwami.app.report.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.HttpRequestHandler;

import com.iwami.iwami.app.biz.ApkBiz;
import com.iwami.iwami.app.model.Apk;
import com.iwami.iwami.app.service.LogService;

public class DownloadServlet implements HttpRequestHandler {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private static final String DOWNLOAD_ERROR = "/error.html";
	
	private ApkBiz apkBiz;
	
	private LogService logService;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			try{
				String msg = request.getQueryString();
				com.iwami.iwami.app.model.Log log = new com.iwami.iwami.app.model.Log();
				log.setType(com.iwami.iwami.app.model.Log.TYPE_APP_DOWNLOAD);
				if(StringUtils.isNotBlank(msg) && StringUtils.contains(msg, "target=") && StringUtils.contains(msg, "type=") && StringUtils.contains(msg, "userid="))
					log.setMsg(msg);
				logService.log(log);
			} catch(Throwable t){
				logger.error("Error in logging >> ", t);
			}
			Apk apk = apkBiz.getApk();
			if(apk != null && StringUtils.isNotBlank(apk.getUrl()))
				response.sendRedirect(apk.getUrl());
			else
				response.sendRedirect(request.getContextPath() + "/" + DOWNLOAD_ERROR);
		} catch (Throwable t) {
			logger.warn("Download redirect error. Reason:", t);
			response.sendRedirect(request.getContextPath() + "/" + DOWNLOAD_ERROR);
		}
	}

	public void handleRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}

	public ApkBiz getApkBiz() {
		return apkBiz;
	}

	public void setApkBiz(ApkBiz apkBiz) {
		this.apkBiz = apkBiz;
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

}
