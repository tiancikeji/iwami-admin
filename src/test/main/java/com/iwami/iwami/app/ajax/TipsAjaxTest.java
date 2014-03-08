package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TipsAjaxTest extends TestCase {

	private boolean flag = false;

	public void testGetTips() {
		if (flag) {
			TipsAjax ajax = getAjax();

			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.getTips(params));

			try {
				params.put("adminid", "10");
				System.out.println(ajax.getTips(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

			try {
				params.put("adminid", "11");
				System.out.println(ajax.getTips(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

		}
	}

	public void testModTip() {
		if (flag) {
			TipsAjax ajax = getAjax();

			Map<String, String> params = new HashMap<String, String>();
			System.out.println(ajax.modTip(params));

			try {
				params.put("adminid", "10");

				params.put("type", "2");
				params.put("content", "contentcontentcontent");
				System.out.println(ajax.modTip(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

			try {
				params.put("adminid", "11");

				params.put("type", "2");
				params.put("content", "contentcontentcontent");
				System.out.println(ajax.modTip(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

			try {
				params.put("adminid", "11");

				params.put("type", "2");
				params.put("content", "dfdfd");
				System.out.println(ajax.modTip(params));
			} catch (Throwable t) {
				t.printStackTrace();
			}

		}
	}

	private TipsAjax getAjax() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		return ac.getBean("tipsAjax", TipsAjax.class);
	}
}
