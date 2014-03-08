package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public class APKAjaxTest extends TestCase {

	private boolean flag = false;
	
	public void testGetApk(){
		if(flag){
			ApkAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getApks(params));

			try{
				params.put("adminid", "10");
				System.out.println(ajax.getApks(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.getApks(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testAddApk(){
		if(flag){
			ApkAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.addApk(params));

			try{
				params.put("adminid", "10");

				params.put("version", "1.1.0");
				params.put("url", "10");
				params.put("force", "0");
				params.put("desc", "1121210");
				System.out.println(ajax.addApk(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");

				params.put("version", "1.1.0");
				params.put("url", "10");
				params.put("force", "0");
				params.put("desc", "1121210");
				System.out.println(ajax.addApk(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testModApk(){
		if(flag){
			ApkAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modApk(params));

			try{
				params.put("adminid", "10");

				params.put("id", "2");
				params.put("version", "1.2.0");
				params.put("url", "20");
				params.put("force", "1");
				params.put("desc", "222");
				params.put("isdel", "0");
				System.out.println(ajax.modApk(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");

				params.put("id", "2");
				params.put("version", "1.2.0");
				params.put("url", "20");
				params.put("force", "1");
				params.put("desc", "222");
				params.put("isdel", "0");
				System.out.println(ajax.modApk(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");

				params.put("id", "1");
				params.put("version", "1.2.0");
				params.put("url", "20");
				params.put("force", "1");
				params.put("desc", "222");
				params.put("isdel", "0");
				System.out.println(ajax.modApk(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	private ApkAjax getAjax(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ac.getBean("apkAjax", ApkAjax.class);
	}
}
