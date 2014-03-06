package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public class LoginAjaxTest extends TestCase {

	private boolean flag = false;
	
	public void testLogin(){
		if(flag){
			LoginAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.login(params));

			params.put("loginname", "test");
			System.out.println(ajax.login(params));

			params.put("password", "test");
			System.out.println(ajax.login(params));

			params.put("loginname", "scofieldlin");
			System.out.println(ajax.login(params));

			params.put("password", "scofieldlin");
			System.out.println(ajax.login(params));
		}
	}
	
	private LoginAjax getAjax(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ac.getBean("loginAjax", LoginAjax.class);
	}
}
