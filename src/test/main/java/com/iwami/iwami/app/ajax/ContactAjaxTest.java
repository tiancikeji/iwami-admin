package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ContactAjaxTest extends TestCase {

	private boolean flag = false;

	public void testGetContact() {
		if (flag) {
			ContactAjax ajax = getAjax();

			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getContact(params));

			try {
				params.put("adminid", "10");
				System.out.println(ajax.getContact(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

			try {
				params.put("adminid", "11");
				System.out.println(ajax.getContact(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

		}
	}

	public void testModContact() {
		if (flag) {
			ContactAjax ajax = getAjax();

			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modContact(params));

			try {
				params.put("adminid", "10");

				params.put("phone1", "10");
				params.put("email1", "10");
				params.put("domain", "10");
				params.put("qq", "10");
				params.put("qqgroup", "10");
				params.put("phone2", "10");
				params.put("email2", "10");
				System.out.println(ajax.modContact(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

			try {
				params.put("adminid", "11");
				System.out.println(ajax.modContact(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

		}
	}

	private ContactAjax getAjax() {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		return ac.getBean("contactAjax", ContactAjax.class);
	}
}
