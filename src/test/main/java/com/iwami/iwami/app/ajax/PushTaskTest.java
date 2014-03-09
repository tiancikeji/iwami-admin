package com.iwami.iwami.app.ajax;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iwami.iwami.task.PushTask;

import junit.framework.TestCase;

public class PushTaskTest extends TestCase {
	
	private boolean flag = false;

	public void testRun(){
		if(flag){
			ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
			PushTask task = ac.getBean("pushTask", PushTask.class);
			
			task.run();
		}
	}
}
