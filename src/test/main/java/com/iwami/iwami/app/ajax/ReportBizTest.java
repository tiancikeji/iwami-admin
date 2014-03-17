package com.iwami.iwami.app.ajax;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.iwami.iwami.app.biz.ReportBiz;
import com.iwami.iwami.app.model.ReportParam;

import junit.framework.TestCase;

public class ReportBizTest extends TestCase {
	
	private boolean flag = false;

	public void testGenOverviewReport() throws FileNotFoundException, IOException{
		if(flag){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_OVERVIEW_REPORT);
			param.setStart("2014-02");
			param.setEnd("2014-03");
			long adminid = 11;
			
			HSSFWorkbook book = reportBiz.genReport(param, adminid);
			book.write(new FileOutputStream("C:\\material\\tmp\\20140317\\" + System.currentTimeMillis() + ".xls"));
		}
	}

	public void testGenTopTaskReport() throws FileNotFoundException, IOException{
		if(flag){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_TASK_TOP);
			param.setStart("2014-02-01");
			param.setEnd("2014-03-31");
			long adminid = 11;
			
			HSSFWorkbook book = reportBiz.genReport(param, adminid);
			book.write(new FileOutputStream("C:\\material\\tmp\\20140317\\" + System.currentTimeMillis() + ".xls"));
		}
	}

	public void testGenTreasureTaskReport() throws FileNotFoundException, IOException{
		if(true){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_TASK_TREASURE);
			param.setStart("2014-02-01");
			param.setEnd("2014-03-31");
			long adminid = 11;
			
			HSSFWorkbook book = reportBiz.genReport(param, adminid);
			book.write(new FileOutputStream("C:\\material\\tmp\\20140317\\" + System.currentTimeMillis() + ".xls"));
		}
	}
	
	private ReportBiz getBiz(){
		return new ClassPathXmlApplicationContext("classpath:applicationContext.xml").getBean("reportBiz", ReportBiz.class);
	}
}
