package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AdminAjaxTest extends TestCase {

	private boolean flag = false;

	public void testGetAdmin() {
		if (flag) {
			AdminAjax ajax = getAjax();

			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getAdmin(params));

			try {
				params.put("adminid", "10");
				System.out.println(ajax.getAdmin(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

			try {
				params.put("adminid", "11");
				System.out.println(ajax.getAdmin(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

		}
	}

	public void testAddAdmin() {
		if (flag) {
			AdminAjax ajax = getAjax();

			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.addAdmin(params));

			try {
				params.put("adminid", "10");

				params.put("username", "10");
				params.put("loginname", "10");
				params.put("cellPhone", "18277887651");
				params.put("password", "1220");
				params.put("roles", "3");
				System.out.println(ajax.addAdmin(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

			try {
				params.put("adminid", "11");
				System.out.println(ajax.addAdmin(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

		}
	}

	public void testModAdmin() {
		if (flag) {
			AdminAjax ajax = getAjax();

			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modAdmin(params));

			try {
				params.put("adminid", "10");

				params.put("userid", "14");
				params.put("username", "20");
				params.put("loginname", "20");
				params.put("cellPhone", "18277887652");
				params.put("password", "2020");
				params.put("roles", "7");
				params.put("isdel", "0");
				System.out.println(ajax.modAdmin(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

			try {
				params.put("adminid", "11");
				System.out.println(ajax.modAdmin(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

			try {
				params.put("adminid", "11");
				params.put("loginname", "200");
				System.out.println(ajax.modAdmin(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

		}
	}
	public void testDelAdmin() {
		if (flag) {
			AdminAjax ajax = getAjax();

			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.delAdmin(params));

			try {
				params.put("adminid", "10");

				params.put("userid", "14");
				System.out.println(ajax.delAdmin(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

			try {
				params.put("adminid", "11");
				System.out.println(ajax.delAdmin(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	private AdminAjax getAjax() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ac.getBean("adminAjax", AdminAjax.class);
	}
}
