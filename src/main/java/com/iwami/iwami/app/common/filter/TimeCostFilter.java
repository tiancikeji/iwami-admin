package com.iwami.iwami.app.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TimeCostFilter implements Filter {
	
    private static Log log = LogFactory.getLog(TimeCostFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
    	long start = System.currentTimeMillis();
    	boolean result = false;
    	try{
    		filterChain.doFilter(servletRequest, servletResponse);
    		result = true;
    	} finally{
    		log.info("[TimeCost] [" + (System.currentTimeMillis() - start) + " ms] [" + result + "] " + ((HttpServletRequest)servletRequest).getRequestURI() + " - " + ((HttpServletRequest)servletRequest).getQueryString());
    	}
    }

    @Override
    public void destroy() {
    }

}
