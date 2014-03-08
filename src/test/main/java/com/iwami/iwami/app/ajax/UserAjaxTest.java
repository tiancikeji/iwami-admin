package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserAjaxTest extends TestCase {

	private boolean flag = false;
	
	public void testGetUserInfo(){
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
	
	public void testModUserInfo(){
		if(flag){
			UserAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modUserinfo(params));

			params.put("adminid", "11");
			params.put("userid", "10");
			params.put("username", "scofieldlin");
			params.put("cellPhone", "18611007601");
			params.put("age", "21");
			params.put("gender", "0");
			params.put("job", "it");
			params.put("address", "bj");
			
			params.put("isdel", "0");
			params.put("currentPrize", "11111");
			params.put("exchangePrize", "21");
			params.put("lastCellPhone1", "18611007601");
			params.put("lastAlipayAccount", "scofieldlin@hotmail.com");
			params.put("lastBankName", "dfd");
			params.put("lastBankAccount", "11111");
			params.put("lastBankNo", "212121");
			params.put("lastAddress", "2w213232");
			params.put("lastCellPhone2", "18611007601");
			params.put("lastName", "dfd");
			
			System.out.println(ajax.modUserinfo(params));
		}
	}
	
	private UserAjax getAjax(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ac.getBean("userAjax", UserAjax.class);
	}
}
