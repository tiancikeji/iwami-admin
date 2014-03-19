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
		if(flag){
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

	public void testGenTaskIdReport() throws FileNotFoundException, IOException{
		if(flag){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_TASK_ID);
			param.setKey("20");
			long adminid = 11;
			
			HSSFWorkbook book = reportBiz.genReport(param, adminid);
			book.write(new FileOutputStream("C:\\material\\tmp\\20140317\\" + System.currentTimeMillis() + ".xls"));
		}
	}

	public void testGenTaskWamiReport() throws FileNotFoundException, IOException{
		if(flag){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_TASK_WAMI);
			param.setStart("2014-02-01");
			param.setEnd("2014-03-31");
			param.setKey("");
			long adminid = 11;
			
			HSSFWorkbook book = reportBiz.genReport(param, adminid);
			book.write(new FileOutputStream("C:\\material\\tmp\\20140317\\" + System.currentTimeMillis() + ".xls"));
		}
	}

	public void testGenTaskHistoryReport() throws FileNotFoundException, IOException{
		if(flag){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_TASK_HISTORY);
			param.setStart("2014-02-01");
			param.setEnd("2014-03-31");
			param.setKey("");
			long adminid = 11;
			
			HSSFWorkbook book = reportBiz.genReport(param, adminid);
			book.write(new FileOutputStream("C:\\material\\tmp\\20140317\\" + System.currentTimeMillis() + ".xls"));
		}
	}

	public void testGenExchangeReport() throws FileNotFoundException, IOException{
		if(flag){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_EXCHANGE);
			param.setStart("2014-02-01");
			param.setEnd("2014-03-31");
			long adminid = 11;
			
			HSSFWorkbook book = reportBiz.genReport(param, adminid);
			book.write(new FileOutputStream("C:\\material\\tmp\\20140317\\" + System.currentTimeMillis() + ".xls"));
		}
	}

	public void testGenGiftsReport() throws FileNotFoundException, IOException{
		if(flag){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_GIFT);
			param.setStart("2014-02-01");
			param.setEnd("2014-03-31");
			long adminid = 11;
			
			HSSFWorkbook book = reportBiz.genReport(param, adminid);
			book.write(new FileOutputStream("C:\\material\\tmp\\20140317\\" + System.currentTimeMillis() + ".xls"));
		}
	}

	public void testGenUserInfosReport() throws FileNotFoundException, IOException{
		if(flag){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_USER_INFO);
			param.setStart("2014-02-01");
			param.setEnd("2014-03-31");
			long adminid = 11;
			
			HSSFWorkbook book = reportBiz.genReport(param, adminid);
			book.write(new FileOutputStream("C:\\material\\tmp\\20140317\\" + System.currentTimeMillis() + ".xls"));
		}
	}
	
	public void testGenUserLoginsReport() throws FileNotFoundException, IOException{
		if(flag){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_USER_LOGIN);
			param.setStart("2014-02-01");
			param.setEnd("2014-03-31");
			long adminid = 11;
			
			HSSFWorkbook book = reportBiz.genReport(param, adminid);
			book.write(new FileOutputStream("C:\\material\\tmp\\20140317\\" + System.currentTimeMillis() + ".xls"));
		}
	}
	
	public void testGenPresentSummaryReport() throws FileNotFoundException, IOException{
		if(flag){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_PRESENT_SUMMARY);
			param.setStart("2014-02");
			param.setEnd("2014-03");
			long adminid = 11;
			
			HSSFWorkbook book = reportBiz.genReport(param, adminid);
			book.write(new FileOutputStream("C:\\material\\tmp\\20140317\\" + System.currentTimeMillis() + ".xls"));
		}
	}
	
	public void testGenPresentHistoryReport() throws FileNotFoundException, IOException{
		if(flag){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_PRESENT_HISTORY);
			param.setStart("2014-02-01");
			param.setEnd("2014-03-31");
			long adminid = 11;
			
			HSSFWorkbook book = reportBiz.genReport(param, adminid);
			book.write(new FileOutputStream("C:\\material\\tmp\\20140317\\" + System.currentTimeMillis() + ".xls"));
		}
	}
	
	public void testGenPresentOfflineReport() throws FileNotFoundException, IOException{
		if(flag){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_PRESENT_OFFLINE);
			param.setStart("2014-02-01");
			param.setEnd("2014-03-31");
			long adminid = 11;
			
			HSSFWorkbook book = reportBiz.genReport(param, adminid);
			book.write(new FileOutputStream("C:\\material\\tmp\\20140317\\" + System.currentTimeMillis() + ".xls"));
		}
	}

	public void testGenShareReport() throws FileNotFoundException, IOException{
		if(flag){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_SHARE);
			param.setStart("2014-02-01");
			param.setEnd("2014-03-31");
			long adminid = 11;
			
			HSSFWorkbook book = reportBiz.genReport(param, adminid);
			book.write(new FileOutputStream("C:\\material\\tmp\\20140317\\" + System.currentTimeMillis() + ".xls"));
		}
	}

	public void testGenDownloadReport() throws FileNotFoundException, IOException{
		if(flag){
			ReportBiz reportBiz = getBiz();
			
			ReportParam param = new ReportParam();
			param.setType(ReportParam.TYPE_APK_DOWNLOAD);
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
