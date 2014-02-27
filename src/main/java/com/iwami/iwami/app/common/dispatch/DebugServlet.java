package com.iwami.iwami.app.common.dispatch;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

@SuppressWarnings("rawtypes")
public class DebugServlet extends HttpServlet {

	private static final long serialVersionUID = 1314152162114573313L;
	
	private ServletConfig servletConfig;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // for unit test
        String debug = "";
        if (this.servletConfig != null) {
            debug = this.servletConfig.getInitParameter("debug");
        } else {
            debug = getServletConfig().getInitParameter("debug");
        }

        if ("true".equals(debug)) {//debug
            String debugClass = req.getParameter("debugClass");
            if (debugClass != null && !"".equals(debugClass)) {
                showDetailedDebugPage(req, resp);
            } else {
                showBriefDebugPage(req, resp);
            }
        } else {// do nothing

        }
    }

    static class NM implements Comparable<NM> {
        String name;
        Method m;

        public String getName() {
            return name;
        }

        public Method getM() {
            return m;
        }

        public NM(String name, Method m) {
            this.name = name;
            this.m = m;
        }

        public int compareTo(NM o) {
            return this.name.compareTo(o.name);
        }

    }

    private void showBriefDebugPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        Map<Class, List<NM>> entryMap = initMethodMap();

        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream(), "utf-8"));

        for (Map.Entry<Class, List<NM>> entry : entryMap.entrySet()) {
            Class clazz = entry.getKey();
            w.write("<a href='?debugClass=" + clazz.getName() + "'>" + clazz.getName() + "</a><br/>");
        }
        w.flush();
    }

    private void showDetailedDebugPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String debugClass = req.getParameter("debugClass");
        Map<Class, List<NM>> entryMap = initMethodMap();
        String ctxPath = req.getContextPath();

        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream(), "utf-8"));
        w.write("<html><head><meta charset='utf-8'/><style type='text/css'>" + ".cname{color: #000000;}"
                + ".mname{color: #FF0000;}" + "</style></head><body><script src='ajaxdemo.js'></script>");

        boolean hasOutput = false;
        for (Map.Entry<Class, List<NM>> entry : entryMap.entrySet()) {
            Class clazz = entry.getKey();
            if (!clazz.getName().equals(debugClass)) {
                continue;
            }
            hasOutput = true;
            List<NM> nms = entry.getValue();
            w.write("<p class='cname'>" + clazz.getName() + "</p>");
            w.write("<br/>");
            int idx = 0;
            for (NM nm : nms) {
                w.write("<span class='mname'>" + nm.getName() + "</span>");
                Method m = nm.getM();
                Class[] paras = m.getParameterTypes();
                w.write("<script>var callback" + idx + " = function(req) {document.getElementById('Return" + idx
                        + "').innerHTML = req.responseText;};</script>");
                w.write("(");
                for (int i = 0; i < paras.length; i++) {
                    w.write(i == 0 ? "" : ",");
                    Class paraClass = paras[i];
                    String hint = paramHint(paraClass);
                    w.write("<input value='" + (i == 0 ? 658 : hint) + "' id='input" + idx + "_" + i + "' size='"
                            + (i == 0 ? 10 : 100) + "'/>");
                }
                w.write(")");
                String url = ctxPath + "/request.ajax";

                String ajaxName = nm.getName();
                w.write("<script>" + "var url = '" + url + "';" + "var invoke" + idx + " = function(){var path='"
                        + ajaxName + "';" + "data = 'userid=' + document.getElementById('input" + idx
                        + "_0').value + '&path=' + path + " + "'&params=' + document.getElementById('input" + idx
                        + "_1').value;" + "baidu.ajax.post(url, data, callback" + idx + ");}</script>");
                w.write("<button onclick='invoke" + idx + "()'>run</button>");
                w.write("<p id='Return" + (idx++) + "'></p>");
                w.write("<br/>");
            }
        }

        if (!hasOutput) {
            w.write("Oops... no class found for " + debugClass + ", try <a href='request.ajax'>list</a> classes first");
        }

        w.write("</body></html>");
        w.flush();
    }

    private Map<Class, List<NM>> initMethodMap() {
        Map<String, Method> dispatchMap = AjaxDispatcher.getDispatchMap();
        Map<Class, List<NM>> entryMap = new HashMap<Class, List<NM>>();
        for (Map.Entry<String, Method> entry : dispatchMap.entrySet()) {
            NM nm = new NM(entry.getKey(), entry.getValue());
            Class decClass = nm.getM().getDeclaringClass();
            if (entryMap.containsKey(decClass)) {
                entryMap.get(decClass).add(nm);
            } else {
                List<NM> nms = new ArrayList<NM>();
                nms.add(nm);
                entryMap.put(decClass, nms);
            }
        }

        for (Map.Entry<Class, List<NM>> entry : entryMap.entrySet()) {
            Collections.sort(entry.getValue());
        }
        return entryMap;
    }

    private String paramHint(Class paraClass) {
        if (paraClass == Map.class) {
            return "{}";
        }
        try {
            return new ObjectMapper().writeValueAsString(paraClass.newInstance());
        } catch (Exception e) {
            return paraClass.getSimpleName();
        }
    }

    // for unit test
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }
}
