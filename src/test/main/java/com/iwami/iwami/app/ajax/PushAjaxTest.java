package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PushAjaxTest extends TestCase {

	private boolean flag = false;
	
	public void testPushSingleUserMsg(){
		if(flag){
			PushAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.pushSingleUserMsg(params));

			try{
				params.put("adminid", "11");
				System.out.println(ajax.pushSingleUserMsg(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("userid", "10");
				System.out.println(ajax.pushSingleUserMsg(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("msg", "this is a test");
				System.out.println(ajax.pushSingleUserMsg(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testPushAllUserMsg(){
		if(flag){
			PushAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.pushAllUserMsg(params));

			try{
				params.put("adminid", "10");
				params.put("msg", "1110");
				params.put("interval", "0.34");
				System.out.println(ajax.pushAllUserMsg(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.pushAllUserMsg(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testPushWhiteUserMsg(){
		if(flag){
			PushAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.pushWhiteUserMsg(params));

			try{
				params.put("adminid", "10");
				params.put("msg", "1110");
				params.put("interval", "0.34");
				params.put("file", "http://115.28.232.121/file/abc.txt");
				System.out.println(ajax.pushWhiteUserMsg(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.pushWhiteUserMsg(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testPushBlackUserMsg(){
		if(flag){
			PushAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.pushBlackUserMsg(params));

			try{
				params.put("adminid", "10");
				params.put("msg", "1110");
				params.put("interval", "0.34");
				params.put("file", "http://115.28.232.121/file/abc.txt");
				System.out.println(ajax.pushBlackUserMsg(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.pushBlackUserMsg(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testGetPushList(){
		if(flag){
			PushAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.pushList(params));

			try{
				params.put("adminid", "10");
				System.out.println(ajax.pushList(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.pushList(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testPauseTask(){
		if(flag){
			PushAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.pausePush(params));

			try{
				params.put("adminid", "10");
				params.put("id", "5");
				System.out.println(ajax.pausePush(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.pausePush(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testResumeTask(){
		if(flag){
			PushAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.continuePush(params));

			try{
				params.put("adminid", "10");
				params.put("id", "5");
				System.out.println(ajax.continuePush(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.continuePush(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testStopTask(){
		if(flag){
			PushAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.stopPush(params));

			try{
				params.put("adminid", "10");
				params.put("id", "5");
				System.out.println(ajax.stopPush(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.stopPush(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	private PushAjax getAjax(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ac.getBean("pushAjax", PushAjax.class);
	}
}
