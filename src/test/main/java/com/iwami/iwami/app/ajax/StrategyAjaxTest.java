package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public class StrategyAjaxTest extends TestCase {

	private boolean flag = false;
	
	public void testGetImages(){
		if(flag){
			StrategyAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getImages(params));

			try{
				params.put("adminid", "10");
				System.out.println(ajax.getImages(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.getImages(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testAddImage(){
		if(flag){
			StrategyAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.addImage(params));

			try{
				params.put("adminid", "10");
				params.put("rank", "0");
				params.put("url", "dfdfdfd");
				params.put("isdel", "0");
				System.out.println(ajax.addImage(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				params.put("rank", "0");
				params.put("url", "dfdfdfd");
				params.put("isdel", "0");
				System.out.println(ajax.addImage(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testModImage(){
		if(flag){
			StrategyAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modImage(params));

			try{
				params.put("adminid", "10");
				params.put("id", "8");
				params.put("rank", "10");
				params.put("url", "2222");
				params.put("isdel", "0");
				System.out.println(ajax.modImage(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				params.put("id", "8");
				params.put("rank", "10");
				params.put("url", "2222");
				params.put("isdel", "0");
				System.out.println(ajax.modImage(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testSeqImage(){
		if(flag){
			StrategyAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.seqImage(params));

			try{
				params.put("adminid", "10");
				params.put("ids", "8,7");
				params.put("ranks", "10,20");
				System.out.println(ajax.modImage(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				params.put("ids", "8,7");
				params.put("ranks", "10,20");
				System.out.println(ajax.seqImage(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testdelImage(){
		if(flag){
			StrategyAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.delImage(params));

			try{
				params.put("adminid", "10");
				params.put("id", "7");
				System.out.println(ajax.delImage(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				params.put("id", "7");
				System.out.println(ajax.delImage(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testGetStrategies(){
		if(flag){
			StrategyAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getStrategyList(params));

			try{
				params.put("adminid", "10");
				params.put("key", "7");
				System.out.println(ajax.getStrategyList(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				params.put("key", "name11");
				System.out.println(ajax.getStrategyList(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testAddStrategy(){
		if(flag){
			StrategyAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.addStrategy(params));

			try{
				params.put("adminid", "10");
				params.put("name", "sss7");
				params.put("subname", "sss7");
				params.put("intr", "sss7");
				params.put("rank", "7");
				params.put("iconSmall", "sss7");
				params.put("iconBig", "sss7");
				params.put("isdel", "0");
				params.put("skim", "7");
				params.put("rate", "7");
				System.out.println(ajax.addStrategy(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.addStrategy(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testModStrategy(){
		if(flag){
			StrategyAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modStrategy(params));

			try{
				params.put("adminid", "10");
				params.put("name", "ttt8");
				params.put("subname", "ttt8");
				params.put("intr", "ttt8");
				params.put("rank", "7");
				params.put("iconSmall", "ttt8");
				params.put("iconBig", "ttt8");
				params.put("isdel", "0");
				params.put("skim", "8");
				params.put("rate", "8");
				System.out.println(ajax.modStrategy(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.modStrategy(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("id", "16");
				System.out.println(ajax.modStrategy(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testSeqStrategy(){
		if(flag){
			StrategyAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.seqStrategy(params));

			try{
				params.put("adminid", "10");
				params.put("ids", "15,16");
				params.put("ranks", "7,8");
				System.out.println(ajax.seqStrategy(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				params.put("ids", "15,16");
				params.put("ranks", "7,8");
				System.out.println(ajax.seqStrategy(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testDelStrategy(){
		if(flag){
			StrategyAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.delStrategy(params));

			try{
				params.put("adminid", "10");
				params.put("id", "16");
				System.out.println(ajax.delStrategy(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				params.put("id", "16");
				System.out.println(ajax.delStrategy(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testGetInfo(){
		if(flag){
			StrategyAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getInfos(params));

			try{
				params.put("adminid", "10");
				params.put("strategyid", "5");
				System.out.println(ajax.getInfos(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				params.put("strategyid", "5");
				System.out.println(ajax.getInfos(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testAddInfo(){
		if(flag){
			StrategyAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.addInfo(params));

			try{
				params.put("adminid", "10");
				params.put("strategyid", "5555");
				params.put("rank", "5555");
				params.put("title", "5555");
				params.put("content", "5555");
				params.put("url", "5555");
				System.out.println(ajax.addInfo(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.addInfo(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testModInfo(){
		if(flag){
			StrategyAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modInfo(params));

			try{
				params.put("adminid", "10");
				params.put("rank", "6666");
				params.put("title", "6666");
				params.put("content", "6666");
				params.put("url", "6666");
				System.out.println(ajax.modInfo(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.modInfo(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				params.put("id", "11");
				System.out.println(ajax.modInfo(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testDelInfo(){
		if(flag){
			StrategyAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.delInfo(params));

			try{
				params.put("adminid", "10");
				params.put("id", "11");
				System.out.println(ajax.delInfo(params));
			}catch(Throwable t){
				t.printStackTrace();
			}

			try{
				params.put("adminid", "11");
				System.out.println(ajax.delInfo(params));
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	private StrategyAjax getAjax(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ac.getBean("strategyAjax", StrategyAjax.class);
	}
}
