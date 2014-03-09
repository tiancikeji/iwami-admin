package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public class PresentAjaxTest extends TestCase {

	private boolean flag = false;
	
	public void testModExchange(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modExchange(params));

			try{
				params.put("adminid", "10");
				params.put("id", "25");
				System.out.println(ajax.modExchange(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.modExchange(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				params.put("id", "27");
				System.out.println(ajax.modExchange(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				params.put("name", "dfdfdf");
				params.put("no", "dfdfdf");
				System.out.println(ajax.modExchange(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testGetExchPresent(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getExchangeHistoryByPresent(params));

			try{
				params.put("adminid", "10");
				params.put("key", "2");
				System.out.println(ajax.getExchangeHistoryByPresent(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.getExchangeHistoryByPresent(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				params.put("key", "p1");
				System.out.println(ajax.getExchangeHistoryByPresent(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testGetExchUser(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getExchangeHistoryByUser(params));

			try{
				params.put("adminid", "10");
				params.put("key", "10");
				System.out.println(ajax.getExchangeHistoryByUser(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.getExchangeHistoryByUser(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				params.put("key", "121212312");
				System.out.println(ajax.getExchangeHistoryByUser(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testGetExch(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getExchangeHistory(params));

			try{
				params.put("adminid", "10");
				params.put("status", "0");
				params.put("type", "0");
				System.out.println(ajax.getExchangeHistory(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.getExchangeHistory(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				params.put("type", "6");
				System.out.println(ajax.getExchangeHistory(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testGetLuck(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getLuck(params));

			try{
				params.put("adminid", "10");
				params.put("status", "0");
				System.out.println(ajax.getLuck(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.getLuck(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testDelLuck(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.delLuck(params));

			try{
				params.put("adminid", "10");
				params.put("id", "1");
				System.out.println(ajax.delLuck(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.delLuck(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testModLuck(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modLuck(params));

			try{
				params.put("adminid", "10");
				params.put("id", "1");
				params.put("isdel", "0");
				params.put("prize", "110");
				params.put("rank", "1");
				params.put("iconSmall", "1123213210");
				params.put("iconBig", "0");

				params.put("gifts", "0,1,2,3");
				params.put("probs", "1,2,3,4");
				params.put("counts", "2,2,2,3");
				System.out.println(ajax.modLuck(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.modLuck(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testGetMobile(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getAli(params));

			try{
				params.put("adminid", "10");
				params.put("status", "0");
				System.out.println(ajax.getMobile(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.getMobile(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testDelMobile(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.delMobile(params));

			try{
				params.put("adminid", "10");
				params.put("id", "4");
				System.out.println(ajax.delMobile(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.delMobile(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testAddMobile(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.addMobile(params));

			try{
				params.put("adminid", "10");
				params.put("isdel", "0");
				params.put("name", "nandmfd");
				params.put("prize", "110");
				params.put("count", "2000");
				params.put("rank", "1");
				params.put("iconSmall", "1123213210");
				params.put("iconBig", "1123213210");
				System.out.println(ajax.addMobile(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.addMobile(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testModMobile(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modMobile(params));

			try{
				params.put("adminid", "10");
				params.put("id", "4");
				params.put("isdel", "0");
				params.put("name", "nandmfd");
				params.put("prize", "110");
				params.put("count", "2000");
				params.put("rank", "1");
				params.put("iconSmall", "1123213210");
				params.put("iconBig", "1123213210");
				System.out.println(ajax.modMobile(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.modMobile(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testGetAli(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getAli(params));

			try{
				params.put("adminid", "10");
				params.put("status", "0");
				System.out.println(ajax.getAli(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.getAli(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testDelAli(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.delAli(params));

			try{
				params.put("adminid", "10");
				params.put("id", "6");
				System.out.println(ajax.delAli(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.delAli(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testModAli(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modAli(params));

			try{
				params.put("adminid", "10");
				params.put("id", "6");
				params.put("isdel", "0");
				params.put("name", "nandmfd");
				params.put("prize", "110");
				params.put("count", "2000");
				params.put("rank", "1");
				params.put("iconSmall", "1123213210");
				params.put("iconBig", "0");
				System.out.println(ajax.modAli(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.modAli(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testGetBank(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getBank(params));

			try{
				params.put("adminid", "10");
				params.put("status", "0");
				System.out.println(ajax.getBank(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.getBank(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testDelBank(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.delBank(params));

			try{
				params.put("adminid", "10");
				params.put("id", "8");
				System.out.println(ajax.delBank(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.delBank(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testModBank(){
		if(flag){
			PresentAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modBank(params));

			try{
				params.put("adminid", "10");
				params.put("id", "8");
				params.put("isdel", "0");
				params.put("name", "nandmfd");
				params.put("prize", "110");
				params.put("count", "2000");
				params.put("rank", "1");
				params.put("iconSmall", "1123213210");
				params.put("iconBig", "0");
				System.out.println(ajax.modBank(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.modBank(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	private PresentAjax getAjax(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ac.getBean("presentAjax", PresentAjax.class);
	}
}
