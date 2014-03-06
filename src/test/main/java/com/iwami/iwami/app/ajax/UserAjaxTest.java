package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserAjaxTest extends TestCase {

	private boolean flag = false;
	
	public void testLogin(){
		if(flag){
			UserAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getUserinfo(params));

			params.put("adminid", "10");
			System.out.println(ajax.getUserinfo(params));

			params.put("key", "10");
			System.out.println(ajax.getUserinfo(params));

			params.put("adminid", "11");
			System.out.println(ajax.getUserinfo(params));

			params.put("key", "11");
			System.out.println(ajax.getUserinfo(params));
		}
	}
	
	private UserAjax getAjax(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ac.getBean("userAjax", UserAjax.class);
	}
}
