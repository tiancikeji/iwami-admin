package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iwami.iwami.task.TaskNotificationTask;

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
	
	public void testGetTask(){
		if(flag){
			TaskAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getTasks(params));
			
			try{
				params.put("adminid", "10");
				System.out.println(ajax.getTasks(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
			
			try{
				params.put("adminid", "10");
				params.put("type", "0");
				params.put("attr", "0");
				params.put("startL", "2014-01-01 00:00:00");
				params.put("startR", "2014-03-10 00:00:00");
				params.put("endL", "2014-03-10 00:00:00");
				params.put("endR", "2014-03-11 00:00:00");
				params.put("maxL", "0");
				params.put("maxR", "1000000");
				params.put("prizeL", "0");
				params.put("prizeR", "1100");
				params.put("currL", "-1");
				params.put("currR", "-1");
				params.put("leftL", "-1");
				params.put("leftR", "-1");
				
				System.out.println(ajax.getTasks(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
			
			try{
				params.put("adminid", "11");
				params.put("type", "0");
				params.put("attr", "0");
				params.put("startL", "2014-01-01 00:00:00");
				params.put("startR", "2014-03-10 00:00:00");
				params.put("endL", "2014-03-10 00:00:00");
				params.put("endR", "2014-03-11 00:00:00");
				params.put("maxL", "0");
				params.put("maxR", "1000000");
				params.put("prizeL", "0");
				params.put("prizeR", "1100");
				params.put("currL", "-1");
				params.put("currR", "-1");
				params.put("leftL", "-1");
				params.put("leftR", "-1");
				
				System.out.println(ajax.getTasks(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testDelTask(){
		if(flag){
			TaskAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.delTask(params));
			
			try{
				params.put("adminid", "10");
				params.put("taskid", "1");
				System.out.println(ajax.delTask(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
			
			try{
				params.put("adminid", "11");
				params.put("taskid", "1");
				
				System.out.println(ajax.delTask(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testStopTask(){
		if(flag){
			TaskAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.stopTask(params));
			
			try{
				params.put("adminid", "10");
				params.put("taskid", "1");
				System.out.println(ajax.stopTask(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
			
			try{
				params.put("adminid", "11");
				params.put("taskid", "1");
				
				System.out.println(ajax.stopTask(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testModTask(){
		if(flag){
			TaskAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modTask(params));
			
			try{
				params.put("adminid", "10");
				params.put("taskid", "1");
				params.put("name", "task1");
				params.put("rank", "10");
				params.put("intr", "task111111");
				params.put("appintr", "apprintrrrrrr");
				params.put("prize", "1");
				params.put("url", "http://iwami.com/apk/url");
				params.put("type", "1");
				params.put("attr", "1");
				params.put("time", "20");
				params.put("startTime", "2014-03-07 23:59:59");
				params.put("endTime", "2015-03-03 23:59:59");
				params.put("addCurrentPrize", "1000");
				params.put("maxPrize", "121212121");
				params.put("iconSmall", "http://iwami.com/icon/small");
				params.put("iconBig", "http://iwami.com/icon/big");
				params.put("isdel", "0");
				System.out.println(ajax.modTask(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
			
			try{
				params.put("adminid", "11");
				params.put("taskid", "1");
				params.put("name", "task1");
				params.put("rank", "10");
				params.put("intr", "task111111");
				params.put("appintr", "apprintrrrrrr");
				params.put("prize", "1");
				params.put("url", "http://iwami.com/apk/url");
				params.put("type", "3");
				params.put("attr", "1");
				params.put("time", "20");
				params.put("startTime", "2014-03-07 23:59:59");
				params.put("endTime", null);
				params.put("addCurrentPrize", "1000");
				params.put("maxPrize", "121212121");
				params.put("iconSmall", "http://iwami.com/icon/small");
				params.put("iconBig", "http://iwami.com/icon/big");
				params.put("isdel", "0");
				
				params.put("default", "1");
				
				System.out.println(ajax.modTask(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
			
			try{
				params.put("adminid", "11");
				params.put("taskid", "9");
				params.put("name", "task1");
				params.put("rank", "10");
				params.put("intr", "task111111");
				params.put("appintr", "apprintrrrrrr");
				params.put("prize", "1");
				params.put("url", "http://iwami.com/apk/url");
				params.put("type", "2");
				params.put("attr", "1");
				params.put("time", "20");
				params.put("startTime", "2014-03-07 23:59:59");
				params.put("endTime", "2015-03-03 23:59:59");
				params.put("addCurrentPrize", "1000");
				params.put("maxPrize", "121212121");
				params.put("iconSmall", "http://iwami.com/icon/small");
				params.put("iconBig", "http://iwami.com/icon/big");
				params.put("isdel", "0");
				
				params.put("default", "1");
				
				System.out.println(ajax.modTask(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
			
			try{
				params.put("adminid", "11");
				params.put("taskid", "1");
				params.put("name", "task1");
				params.put("rank", "10");
				params.put("intr", "task111111");
				params.put("appintr", "apprintrrrrrr");
				params.put("prize", "1");
				params.put("url", "http://iwami.com/apk/url");
				params.put("type", "4");
				params.put("attr", "1");
				params.put("time", "20");
				params.put("startTime", "2014-03-07 23:59:59");
				params.put("endTime", "2015-03-03 23:59:59");
				params.put("addCurrentPrize", "1000");
				params.put("maxPrize", "121212121");
				params.put("iconSmall", "http://iwami.com/icon/small");
				params.put("iconBig", "http://iwami.com/icon/big");
				params.put("isdel", "0");
				
				params.put("default", "1");
				
				System.out.println(ajax.modTask(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testAddTask(){
		if(flag){
			TaskAjax ajax = getAjax();
			
			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.addTask(params));
			
			try{
				params.put("adminid", "10");
				params.put("name", "task1");
				params.put("rank", "10");
				params.put("intr", "task111111");
				params.put("appintr", "apprintrrrrrr");
				params.put("prize", "1");
				params.put("url", "http://iwami.com/apk/url");
				params.put("type", "1");
				params.put("attr", "1");
				params.put("time", "20");
				params.put("startTime", "2014-03-07 23:59:59");
				params.put("endTime", "2015-03-03 23:59:59");
				params.put("currentPrize", "1000");
				params.put("maxPrize", "121212121");
				params.put("iconSmall", "http://iwami.com/icon/small");
				params.put("iconBig", "http://iwami.com/icon/big");
				params.put("isdel", "0");
				System.out.println(ajax.addTask(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
			
			try{
				params.put("adminid", "11");
				params.put("name", "task1");
				params.put("rank", "10");
				params.put("intr", "task111111");
				params.put("appintr", "apprintrrrrrr");
				params.put("prize", "1");
				params.put("url", "http://iwami.com/apk/url");
				params.put("type", "3");
				params.put("attr", "1");
				params.put("time", "20");
				params.put("startTime", "2014-03-07 23:59:59");
				params.put("endTime", null);
				params.put("currentPrize", "1000");
				params.put("maxPrize", "121212121");
				params.put("iconSmall", "http://iwami.com/icon/small");
				params.put("iconBig", "http://iwami.com/icon/big");
				params.put("isdel", "0");
				
				params.put("default", "1");
				
				System.out.println(ajax.addTask(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
			
			try{
				params.put("adminid", "11");
				params.put("name", "task1");
				params.put("rank", "10");
				params.put("intr", "task111111");
				params.put("appintr", "apprintrrrrrr");
				params.put("prize", "1");
				params.put("url", "http://iwami.com/apk/url");
				params.put("type", "2");
				params.put("attr", "1");
				params.put("time", "20");
				params.put("startTime", "2014-03-07 23:59:59");
				params.put("endTime", "2015-03-03 23:59:59");
				params.put("currentPrize", "1000");
				params.put("maxPrize", "121212121");
				params.put("iconSmall", "http://iwami.com/icon/small");
				params.put("iconBig", "http://iwami.com/icon/big");
				params.put("isdel", "0");
				
				params.put("default", "1");
				
				System.out.println(ajax.addTask(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
			
			try{
				params.put("adminid", "11");
				params.put("name", "task1");
				params.put("rank", "10");
				params.put("intr", "task111111");
				params.put("appintr", "apprintrrrrrr");
				params.put("prize", "1");
				params.put("url", "http://iwami.com/apk/url");
				params.put("type", "4");
				params.put("attr", "1");
				params.put("time", "20");
				params.put("startTime", "2014-03-07 23:59:59");
				params.put("endTime", "2015-03-03 23:59:59");
				params.put("currentPrize", "1000");
				params.put("maxPrize", "121212121");
				params.put("iconSmall", "http://iwami.com/icon/small");
				params.put("iconBig", "http://iwami.com/icon/big");
				params.put("isdel", "0");
				
				params.put("default", "1");
				
				System.out.println(ajax.addTask(params));
			} catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
	
	public void testNotifyTask(){
		if(true){
			getTask().run();
		}
	}
	
	private TaskNotificationTask getTask(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ac.getBean("taskNotificationTask", TaskNotificationTask.class);
	}
	
	private TaskAjax getAjax(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ac.getBean("taskAjax", TaskAjax.class);
	}
}
