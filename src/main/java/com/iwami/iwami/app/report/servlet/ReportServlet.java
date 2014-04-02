package com.iwami.iwami.app.report.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.HttpRequestHandler;

import com.iwami.iwami.app.biz.ReportBiz;
import com.iwami.iwami.app.model.ReportParam;

public class ReportServlet implements HttpRequestHandler {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private static final String DOWNLOAD_ERROR = "/error.html";
	
	private ReportBiz reportBiz;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OutputStream out = null;
		try {
			long adminid = NumberUtils.toLong(request.getParameter("adminid"), -1);
			
			int type = NumberUtils.toInt(request.getParameter("type"), 0);
			if(type > 0 && type < 16){
				response.setHeader("Content-Type", "application/msexcel");
				
				String oriFileName = ReportParam.TYPE_TITLES.get(type) + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				oriFileName = oriFileName.replaceAll("\\s+", ""); 
				oriFileName += ".xls";
				String agent = request.getHeader("USER-AGENT");
				if (null != agent && -1 != agent.indexOf("Firefox")) {
					response.setHeader("Content-disposition", "attachment;filename=" + new String(oriFileName.getBytes("UTF-8"),"iso-8859-1"));
				} else {
					response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(oriFileName, "UTF-8"));
				}
	
				ReportParam param = new ReportParam();
				param.setType(type);
				param.setStart(request.getParameter("start"));
				param.setEnd(request.getParameter("end"));
				param.setKey(request.getParameter("key"));
				
				logger.info("[Report] " + param);
				
				out = response.getOutputStream();
				reportBiz.genReport(param, adminid).write(out);
			} else
				response.sendRedirect(request.getContextPath() + "/" + DOWNLOAD_ERROR);
		} catch (Exception e) {
			logger.warn("Download error. Reason:" + e, e);
			response.sendRedirect(request.getContextPath() + "/" + DOWNLOAD_ERROR);
			return;
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void handleRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}

	public ReportBiz getReportBiz() {
		return reportBiz;
	}

	public void setReportBiz(ReportBiz reportBiz) {
		this.reportBiz = reportBiz;
	}

}
