package com.iwami.iwami.app.common.dispatch;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DispatchServlet extends HttpServlet {
	private static final long serialVersionUID = 5607686507908533512L;
	
	private static Log logger = LogFactory.getLog(DispatchServlet.class);

	@Override
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, String> params = new HashMap<String, String>();
		
		Enumeration<String> eu = req.getParameterNames();
		while(eu.hasMoreElements()){
			String key = eu.nextElement();
			params.put(key, req.getParameter(key));
		}
		
		String path = StringUtils.substringAfter(req.getRequestURI(), "/client/");
		map.put(AjaxDispatcher.KEY_PARAM, params);
		map.put(AjaxDispatcher.KEY_PATH, path);

		logger.info("ajax-dispather" + " : " +  "Receving request path: " + path + ", param: " + params);

		Writer writer = new OutputStreamWriter(resp.getOutputStream(), "utf-8");

		try {
			String result = AjaxDispatcher.dispatch(map);
			writer.write(result);
		} catch (Throwable e) {
			logger.error("Something wrong when invoke ajax method", e);
		} finally {
			writer.flush();
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

}
