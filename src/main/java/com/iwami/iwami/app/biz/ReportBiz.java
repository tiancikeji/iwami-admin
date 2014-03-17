package com.iwami.iwami.app.biz;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.iwami.iwami.app.model.ReportParam;

public interface ReportBiz {

	public HSSFWorkbook genReport(ReportParam param, long adminid);

}
