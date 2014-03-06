package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TaskAjaxTest extends TestCase {

	private boolean flag = false;
	
	public void testGetTreasureConfig(){
		if(flag){
			TaskAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getTreasureConfig(params));
			
			try{
				params.put("adminid", "10");
				System.out.println(ajax.getTreasureConfig(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
			try{
				params.put("adminid", "11");
				System.out.println(ajax.getTreasureConfig(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testModTreasureConfig(){
		if(flag){
			TaskAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modTreasureConfig(params));
			
			try{
				params.put("adminid", "10");
				System.out.println(ajax.modTreasureConfig(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
			try{
				params.put("adminid", "11");
				params.put("days", "10");
				params.put("count", "10");
				System.out.println(ajax.modTreasureConfig(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	private TaskAjax getAjax(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ac.getBean("taskAjax", TaskAjax.class);
	}
}
