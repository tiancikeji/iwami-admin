package com.iwami.iwami.app.biz.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.iwami.iwami.app.biz.ReportBiz;
//import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.model.Exchange;
import com.iwami.iwami.app.model.Log;
import com.iwami.iwami.app.model.Onstart;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.model.ReportParam;
import com.iwami.iwami.app.model.Share;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.model.Wami;
import com.iwami.iwami.app.service.LogService;
import com.iwami.iwami.app.service.LoginService;
import com.iwami.iwami.app.service.OnstartService;
import com.iwami.iwami.app.service.PresentService;
import com.iwami.iwami.app.service.TaskService;
import com.iwami.iwami.app.service.UserService;
import com.iwami.iwami.app.service.WamiService;
import com.iwami.iwami.app.util.IWamiUtils;

public class ReportBizImpl implements ReportBiz {
	
	private LoginService loginService; 
	
	private LogService logService;
	
	private TaskService taskService;
	
	private WamiService wamiService;
	
	private PresentService presentService;
	
	private OnstartService onStartService;
	
	private UserService userService;
	
	@Override
	public HSSFWorkbook genReport(ReportParam param, long adminid) {
		HSSFWorkbook book = null;
		
		if(true/*adminid > 0 && loginService.checkLogin(adminid) && loginService.checkRole(adminid, IWamiConstants.DOWNLOAD_MANAGEMENT)*/){
			if(param.getType() == ReportParam.TYPE_OVERVIEW_REPORT){
				Date start = IWamiUtils.getMonthDate(param.getStart());
				Date end = IWamiUtils.getMonthDate(param.getEnd());
				if(end == null)
					end = start;
				
				if(start != null && end != null && !start.after(end))
					book = genOverViewReport(start, end);
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else if(param.getType() == ReportParam.TYPE_TASK_TOP){
				Date start = IWamiUtils.getDayDate(param.getStart());
				Date end = IWamiUtils.getDayDate(param.getEnd());
				
				if(start != null && end != null && !start.after(end))
					book = genTopReport(start, end);
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else if(param.getType() == ReportParam.TYPE_TASK_TREASURE){
				Date start = IWamiUtils.getDayDate(param.getStart());
				Date end = IWamiUtils.getDayDate(param.getEnd());
				
				if(start != null && end != null && !start.after(end))
					book = genTreasureReport(start, end);
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else if(param.getType() == ReportParam.TYPE_TASK_ID){
				long id = NumberUtils.toLong(param.getKey(), -1);
				if(id > 0)
					book = genTaskIdReport(id);
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else if(param.getType() == ReportParam.TYPE_TASK_WAMI){
				Date start = IWamiUtils.getDayDate(param.getStart());
				Date end = IWamiUtils.getDayDate(param.getEnd());
				
				if(start != null && end != null && !start.after(end))
					book = genTaskWamiReport(start, end, param.getKey());
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else if(param.getType() == ReportParam.TYPE_TASK_HISTORY){
				Date start = IWamiUtils.getDayDate(param.getStart());
				Date end = IWamiUtils.getDayDate(param.getEnd());
				
				if(start != null && end != null && !start.after(end))
					book = genTaskHisotryReport(start, end);
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else if(param.getType() == ReportParam.TYPE_EXCHANGE){
				Date start = IWamiUtils.getDayDate(param.getStart());
				Date end = IWamiUtils.getDayDate(param.getEnd());
				
				if(start != null && end != null && !start.after(end))
					book = genExchangeReport(start, end);
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else if(param.getType() == ReportParam.TYPE_GIFT){
				Date start = IWamiUtils.getDayDate(param.getStart());
				Date end = IWamiUtils.getDayDate(param.getEnd());
				
				if(start != null && end != null && !start.after(end))
					book = genGiftReport(start, end);
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else if(param.getType() == ReportParam.TYPE_USER_INFO){
				Date start = IWamiUtils.getDayDate(param.getStart());
				Date end = IWamiUtils.getDayDate(param.getEnd());
				
				if(start != null && end != null && !start.after(end))
					book = genUserReport(start, end);
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else if(param.getType() == ReportParam.TYPE_USER_LOGIN){
				Date start = IWamiUtils.getDayDate(param.getStart());
				Date end = IWamiUtils.getDayDate(param.getEnd());
				
				if(start != null && end != null && !start.after(end))
					book = genOnstartReport(start, end);
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else if(param.getType() == ReportParam.TYPE_PRESENT_SUMMARY){
				Date start = IWamiUtils.getMonthDate(param.getStart());
				Date end = IWamiUtils.getMonthDate(param.getEnd());
				if(end == null)
					end = start;
				
				if(start != null && end != null && !start.after(end))
					book = genPresentSummaryReport(start, end);
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else if(param.getType() == ReportParam.TYPE_PRESENT_HISTORY){
				Date start = IWamiUtils.getDayDate(param.getStart());
				Date end = IWamiUtils.getDayDate(param.getEnd());
				
				if(start != null && end != null && !start.after(end))
					book = genPresentHistoryReport(start, end);
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else if(param.getType() == ReportParam.TYPE_PRESENT_OFFLINE){
				Date start = IWamiUtils.getDayDate(param.getStart());
				Date end = IWamiUtils.getDayDate(param.getEnd());
				
				if(start != null && end != null && !start.after(end))
					book = genOfflinePresentReport(start, end);
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else if(param.getType() == ReportParam.TYPE_SHARE){
				Date start = IWamiUtils.getDayDate(param.getStart());
				Date end = IWamiUtils.getDayDate(param.getEnd());
				
				if(start != null && end != null && !start.after(end))
					book = genShareReport(start, end);
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else if(param.getType() == ReportParam.TYPE_APK_DOWNLOAD){
				Date start = IWamiUtils.getDayDate(param.getStart());
				Date end = IWamiUtils.getDayDate(param.getEnd());
				
				if(start != null && end != null && !start.after(end))
					book = genDownloadReport(start, end);
				else
					throw new RuntimeException("not recognized param >> " + param);
			} else
				throw new RuntimeException("not recognized param >> " + param);
		}
		
		return book;
	}
	
	private HSSFWorkbook genDownloadReport(Date start, Date end) {
		end = DateUtils.addSeconds(DateUtils.addDays(end, 1), -1);
		
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_APK_DOWNLOAD));
		
		List<Log> logs = logService.getLogsByType(Log.TYPE_APP_DOWNLOAD, start, end);
		
		int rowindex = 0;
		HSSFRow row = sheet.createRow(rowindex++);
		
		int columnIndex = 0;
		HSSFCell cell = row.createCell(columnIndex++);
		cell.setCellValue("开始日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(start));
		cell = row.createCell(columnIndex++);
		cell.setCellValue("结束日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(end));
		
		row = sheet.createRow(rowindex++);
		row = sheet.createRow(rowindex++);
		
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("下载开始时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("来源");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("分享者ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("分享渠道");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("分享原因");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("分享时间");
		
		for(Log log : logs){
			row = sheet.createRow(rowindex++);
			columnIndex = 0;
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(log.getAddTime()));
			cell = row.createCell(columnIndex++);
			String source = "官网";
			if(StringUtils.isNotBlank(log.getMsg()))
				source = "分享";
			cell.setCellValue(source);
			cell = row.createCell(columnIndex++);
			String userid = StringUtils.substringBetween(log.getMsg(), "userid=", "&");
			if(userid == null)
				userid = StringUtils.substringAfter(log.getMsg(), "userid=");
			cell.setCellValue(userid);
			cell = row.createCell(columnIndex++);
			String type = StringUtils.substringBetween(log.getMsg(), "type=", "&");
			if(type == null)
				type = StringUtils.substringAfter(log.getMsg(), "type=");
			cell.setCellValue(type);
			cell = row.createCell(columnIndex++);
			String target = StringUtils.substringBetween(log.getMsg(), "target=", "&");
			if(target == null)
				target = StringUtils.substringAfter(log.getMsg(), "target=");
			int _target = NumberUtils.toInt(target, -1);
			if(_target == 0)
				_target = 1;
			else if(_target == 1)
				_target = 0;
			cell.setCellValue(target);
			cell.setCellValue(target);
			cell = row.createCell(columnIndex++);
			String time = StringUtils.substringBetween(log.getMsg(), "time=", "&");
			if(time == null)
				time = StringUtils.substringAfter(log.getMsg(), "time=");
			if(StringUtils.isNotBlank(time))
				time = IWamiUtils.getDateString(new Date(NumberUtils.toLong(time)));
			else
				time = StringUtils.EMPTY;
			cell.setCellValue(time);
		}
		
		return book;
	}

	private HSSFWorkbook genShareReport(Date start, Date end) {
		end = DateUtils.addSeconds(DateUtils.addDays(end, 1), -1);
		
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_SHARE));
		
		List<Share> shares = presentService.getShares(start, end);
		
		int rowindex = 0;
		HSSFRow row = sheet.createRow(rowindex++);
		
		int columnIndex = 0;
		HSSFCell cell = row.createCell(columnIndex++);
		cell.setCellValue("开始日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(start));
		cell = row.createCell(columnIndex++);
		cell.setCellValue("结束日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(end));
		
		row = sheet.createRow(rowindex++);
		row = sheet.createRow(rowindex++);
		
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("米农ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("分享渠道");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("分享原因");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("分享话语");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("分享时间");
		
		for(Share share : shares){
			row = sheet.createRow(rowindex++);
			columnIndex = 0;
			cell = row.createCell(columnIndex++);
			cell.setCellValue(share.getUserid());
			cell = row.createCell(columnIndex++);
			int target = share.getTarget();
			if(target == 0)
				target = 1;
			else if(target == 1)
				target = 0;
			cell.setCellValue(target);
			cell = row.createCell(columnIndex++);
			cell.setCellValue(share.getType());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(share.getMsg());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(share.getLastModTime()));
		}
		
		return book;
	}

	private HSSFWorkbook genOfflinePresentReport(Date start, Date end) {
		end = DateUtils.addSeconds(DateUtils.addDays(end, 1), -1);
		
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_PRESENT_OFFLINE));
		
		List<Exchange> exchanges = presentService.getExchanges(start, end);
		if(exchanges != null && exchanges.size() > 0)
			Collections.sort(exchanges, new Comparator<Exchange>() {

				@Override
				public int compare(Exchange o1, Exchange o2) {
					return (int)(NumberUtils.toLong(o2.getChannel(), 0) - NumberUtils.toLong(o1.getChannel(), 0));
				}
			});
		
		int rowindex = 0;
		HSSFRow row = sheet.createRow(rowindex++);
		
		int columnIndex = 0;
		HSSFCell cell = row.createCell(columnIndex++);
		cell.setCellValue("开始日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(start));
		cell = row.createCell(columnIndex++);
		cell.setCellValue("结束日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(end));
		
		row = sheet.createRow(rowindex++);
		row = sheet.createRow(rowindex++);
		
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("现场ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("礼品代码");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("米粒数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("米农ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("兑换时间");
		
		for(Exchange exchange : exchanges)
			if(exchange.getStatus() == Exchange.STATUS_FINISH && exchange.getPresentType() == Present.TYPE_OFFLINE){
				row = sheet.createRow(rowindex++);
				columnIndex = 0;
				cell = row.createCell(columnIndex++);
				cell.setCellValue(exchange.getChannel());
				cell = row.createCell(columnIndex++);
				cell.setCellValue(exchange.getId());
				cell = row.createCell(columnIndex++);
				cell.setCellValue(exchange.getPrize());
				cell = row.createCell(columnIndex++);
				cell.setCellValue(exchange.getUserid());
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(exchange.getLastModTime()));
			}
		
		return book;
	}

	private HSSFWorkbook genPresentHistoryReport(Date start, Date end) {
		end = DateUtils.addSeconds(DateUtils.addDays(end, 1), -1);
		
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_PRESENT_HISTORY));
		
		List<Exchange> exchanges = presentService.getExchanges(start, end);
		if(exchanges != null && exchanges.size() > 0)
			Collections.sort(exchanges, new Comparator<Exchange>() {

				@Override
				public int compare(Exchange o1, Exchange o2) {
					return (int)(o1.getLastModUserid() - o2.getLastModUserid());
				}
			});
		
		int rowindex = 0;
		HSSFRow row = sheet.createRow(rowindex++);
		
		int columnIndex = 0;
		HSSFCell cell = row.createCell(columnIndex++);
		cell.setCellValue("开始日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(start));
		cell = row.createCell(columnIndex++);
		cell.setCellValue("结束日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(end));
		
		row = sheet.createRow(rowindex++);
		row = sheet.createRow(rowindex++);
		
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("米农ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("申请时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("确认人员ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("发货时间");
		
		for(Exchange exchange : exchanges)
			if(exchange.getStatus() == Exchange.STATUS_FINISH && exchange.getPresentType() != Present.TYPE_GIFT && exchange.getPresentType() != Present.TYPE_OFFLINE){
				row = sheet.createRow(rowindex++);
				columnIndex = 0;
				cell = row.createCell(columnIndex++);
				cell.setCellValue(exchange.getUserid());
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(exchange.getAddTime()));
				cell = row.createCell(columnIndex++);
				cell.setCellValue(exchange.getLastModUserid());
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(exchange.getLastModTime()));
			}
		
		return book;
	}

	private HSSFWorkbook genPresentSummaryReport(Date start, Date end) {
		end = DateUtils.addSeconds(DateUtils.addMonths(end, 1), -1);
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_PRESENT_SUMMARY));
		
		List<Exchange> exchanges = presentService.getExchanges(start, end);
		Map<Long, Map<String, Integer>> counts = new HashMap<Long, Map<String,Integer>>();
		if(exchanges != null && exchanges.size() > 0)
			for(Exchange exchange : exchanges)
				if(exchange.getStatus() == Exchange.STATUS_FINISH && exchange.getPresentType() != Present.TYPE_GIFT && exchange.getPresentType() != Present.TYPE_OFFLINE){
					Map<String, Integer> _counts = counts.get(exchange.getLastModUserid());
					if(_counts == null){
						_counts = new HashMap<String, Integer>();
						counts.put(exchange.getLastModUserid(), _counts);
					}
					
					String key = IWamiUtils.getDayDateString(exchange.getLastModTime());
					Integer count = _counts.get(key);
					if(count == null)
						count = 0;
					
					_counts.put(key, count + 1);
				}
		
		int rowindex = 0;
		HSSFRow row = sheet.createRow(rowindex++);
		
		int columnIndex = 0;
		HSSFCell cell = row.createCell(columnIndex++);
		cell.setCellValue("运营人员ID");
		
		for(Date tmp = start; tmp.before(end); tmp = DateUtils.addDays(tmp, 1)){
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDayDateString(tmp));
		}
		List<Long> keys = new ArrayList<Long>(counts.keySet());
		Collections.sort(keys, new Comparator<Long>(){

			@Override
			public int compare(Long o1, Long o2) {
				return (int)(o1 - o2);
			}
			
		});
		
		for(Long userid : counts.keySet()){
			row = sheet.createRow(rowindex++);
			columnIndex = 0;
			cell = row.createCell(columnIndex++);
			cell.setCellValue(userid);
			Map<String, Integer> values = counts.get(userid);
			for(Date tmp = start; tmp.before(end); tmp = DateUtils.addDays(tmp, 1)){
				cell = row.createCell(columnIndex++);
				String count = StringUtils.EMPTY;
				Integer _count = values.get(IWamiUtils.getDayDateString(tmp));
				if(_count != null)
					count = _count.toString();
				cell.setCellValue(count);
			}
		}
		
		return book;
	}

	private HSSFWorkbook genOnstartReport(Date start, Date end) {
		end = DateUtils.addSeconds(DateUtils.addDays(end, 1), -1);
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_USER_LOGIN));
		
		List<Onstart> onstarts = onStartService.getOnstarts(start, end);
		
		int rowindex = 0;
		HSSFRow row = sheet.createRow(rowindex++);
		
		int columnIndex = 0;
		HSSFCell cell = row.createCell(columnIndex++);
		cell.setCellValue("开始日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(start));
		cell = row.createCell(columnIndex++);
		cell.setCellValue("结束日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(end));

		row = sheet.createRow(rowindex++);
		
		row = sheet.createRow(rowindex++);
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("米农ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("手机号");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("机器码");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("JPush alias");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("GPS");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("登录类型");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("客户端版本号");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("登录时间");
		
		for(Onstart onstart : onstarts){
			row = sheet.createRow(rowindex++);
			columnIndex = 0;
			cell = row.createCell(columnIndex++);
			cell.setCellValue(onstart.getUserid());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(onstart.getCellPhone());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(onstart.getUuid());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(onstart.getAlias());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(onstart.getGps());
			cell = row.createCell(columnIndex++);
			String type = "未知启动类型";
			if(onstart.getType() == 0)
				type = "冷启动";
			else if(onstart.getType() == 0)
				type = "激活";
			cell.setCellValue(type);
			cell = row.createCell(columnIndex++);
			cell.setCellValue(onstart.getVersion());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(onstart.getLastModTime()));
		}
		
		return book;
	}

	private HSSFWorkbook genUserReport(Date start, Date end) {
		end = DateUtils.addSeconds(DateUtils.addDays(end, 1), -1);
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_USER_INFO));
		
		List<User> users = userService.getChangedUsers(start, end);
		
		int rowindex = 0;
		HSSFRow row = sheet.createRow(rowindex++);
		
		int columnIndex = 0;
		HSSFCell cell = row.createCell(columnIndex++);
		cell.setCellValue("开始日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(start));
		cell = row.createCell(columnIndex++);
		cell.setCellValue("结束日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(end));

		row = sheet.createRow(rowindex++);
		
		row = sheet.createRow(rowindex++);
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("米农ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("手机号");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("机器码");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("JPush alias");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("姓名");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("性别");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("年龄");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("职业");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("寄送地址");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("寄送姓名");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("寄送电话");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("银行名称");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("银行账户");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("银行卡号");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("支付宝账号");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("赠送手机号");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("注册时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("最后更新日期");
		
		for(User user : users){
			row = sheet.createRow(rowindex++);
			columnIndex = 0;
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getId());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getCellPhone());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getUuid());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getAlias());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getName());
			cell = row.createCell(columnIndex++);
			String gender = "帅哥";
			if(user.getGender() == 1)
				gender = "美女";
			cell.setCellValue(gender);
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getAge());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getJob());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getLastAddres());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getLastName());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getLastCellPhone2());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getLastBankName());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getLastBankAccount());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getLastBankNo());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getLastAlipayAccount());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(user.getLastCellPhone1());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(user.getAddTime()));;
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(user.getLastmodTime()));
		}
		
		return book;
	}

	private HSSFWorkbook genGiftReport(Date start, Date end) {
		end = DateUtils.addSeconds(DateUtils.addDays(end, 1), -1);
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_GIFT));
		
		List<Exchange> exchanges = presentService.getGifts(start, end);
		
		int rowindex = 0;
		HSSFRow row = sheet.createRow(rowindex++);
		
		int columnIndex = 0;
		HSSFCell cell = row.createCell(columnIndex++);
		cell.setCellValue("开始日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(start));
		cell = row.createCell(columnIndex++);
		cell.setCellValue("结束日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(end));

		row = sheet.createRow(rowindex++);
		
		row = sheet.createRow(rowindex++);
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("赠送者ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("受赠者ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("米粒数量");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("赠送时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("赠送结果");
		
		for(Exchange exchange : exchanges){
			row = sheet.createRow(rowindex++);
			columnIndex = 0;
			cell = row.createCell(columnIndex++);
			cell.setCellValue(exchange.getUserid());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(exchange.getPresentId());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(exchange.getPrize());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(exchange.getLastModTime()));
			cell = row.createCell(columnIndex++);
			String result = "失败";
			if(exchange.getStatus() == Exchange.STATUS_FAILED)
				result = "余额不足";
			else if(exchange.getStatus() == Exchange.STATUS_FINISH)
				result = "成功";
			cell.setCellValue(result);
		}
		
		return book;
	}
	
	private HSSFWorkbook genExchangeReport(Date start, Date end) {
		end = DateUtils.addSeconds(DateUtils.addDays(end, 1), -1);
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_EXCHANGE));
		
		List<Exchange> exchanges = presentService.getExchanges(start, end);
		if(exchanges != null && exchanges.size() > 0)
			Collections.sort(exchanges, new Comparator<Exchange>() {

				@Override
				public int compare(Exchange e1, Exchange e2) {
					int result = e2.getStatus() - e1.getStatus();
					if(result == 0)
						result = (int) (e2.getLastModUserid() - e2.getLastModUserid());
					return result;
				}
			});
		
		int rowindex = 0;
		HSSFRow row = sheet.createRow(rowindex++);
		
		int columnIndex = 0;
		HSSFCell cell = row.createCell(columnIndex++);
		cell.setCellValue("开始日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(start));
		cell = row.createCell(columnIndex++);
		cell.setCellValue("结束日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(end));

		row = sheet.createRow(rowindex++);
		
		row = sheet.createRow(rowindex++);
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("米农ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("申请时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("发货员ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("发货时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("快递公司");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("快递单号");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("礼物数量");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("米粒数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("兑换类别");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("渠道ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("申请单ID");
		
		for(Exchange exchange : exchanges){
			if(exchange.getPresentType() == Present.TYPE_GIFT)
				continue;
			row = sheet.createRow(rowindex++);
			columnIndex = 0;
			cell = row.createCell(columnIndex++);
			cell.setCellValue(exchange.getUserid());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(exchange.getAddTime()));
			cell = row.createCell(columnIndex++);
			String id = StringUtils.EMPTY;
			String time = StringUtils.EMPTY;
			if(exchange.getStatus() == Exchange.STATUS_FINISH){
				id = "" + exchange.getLastModUserid();
				time = IWamiUtils.getDateString(exchange.getLastModTime());
			}
			cell.setCellValue(id);
			cell = row.createCell(columnIndex++);
			cell.setCellValue(time);
			cell = row.createCell(columnIndex++);
			cell.setCellValue(exchange.getExpressName());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(exchange.getExpressNo());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(exchange.getCount());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(exchange.getPrize());
			cell = row.createCell(columnIndex++);
			String type = "未知";
			if(exchange.getPresentType() == Present.TYPE_OFFLINE)
				type = "线下礼品";
			else if(exchange.getPresentType() == Present.TYPE_LUCK)
				type = "抽奖";
			else if(exchange.getPresentType() == Present.TYPE_ONLINE_EMS)
				type = "线上快递";
			else if(exchange.getPresentType() == Present.TYPE_ONLINE_RECHARGE_ALIPAY)
				type = "线上支付宝充值";
			else if(exchange.getPresentType() == Present.TYPE_ONLINE_RECHARGE_BANK)
				type = "线上银行卡充值";
			else if(exchange.getPresentType() == Present.TYPE_ONLINE_RECHARGE_MOBILE)
				type = "线上手机充值";
			cell.setCellValue(type);
			cell = row.createCell(columnIndex++);
			cell.setCellValue(exchange.getChannel());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(exchange.getId());
		}
		
		return book;
	}

	private HSSFWorkbook genTaskHisotryReport(Date start, Date end) {
		end = DateUtils.addSeconds(DateUtils.addDays(end, 1), -1);
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_TASK_WAMI));
		
		// taskid - userid - historys
		Map<Long, Map<Long, List<UserWamiHistory>>> tWamis = new HashMap<Long, Map<Long, List<UserWamiHistory>>>();

		List<Wami> wamis = wamiService.getWamis(start, end);
		if(wamis != null && wamis.size() > 0)
			for(Wami wami : wamis){
				Collections.sort(wamis, new Comparator<Wami>() {

					@Override
					public int compare(Wami o1, Wami o2) {
						int result = (int)(o1.getTaskId() - o2.getTaskId());
						if(result == 0)
							result = (int)(o1.getUserid() - o2.getUserid());
						if(result == 0)
							result = o1.getType() - o2.getType();
						if(result == 0)
							result = (o1.getLastmodTime().before(o2.getLastmodTime()) ? -1 : 1);
						return result;
					}
				});
				
				if(wami.getType() == (Wami.TYPE_RUN + 100) || wami.getType() == (Wami.TYPE_FINISH + 100)){
					Map<Long, List<UserWamiHistory>> _tWamis = tWamis.get(wami.getTaskId());
					if(_tWamis == null){
						_tWamis = new HashMap<Long, List<UserWamiHistory>>();
						tWamis.put(wami.getTaskId(), _tWamis);
					}
					
					List<UserWamiHistory> _tWami = _tWamis.get(wami.getUserid());
					if(_tWami == null){
						_tWami = new ArrayList<UserWamiHistory>();
						_tWamis.put(wami.getUserid(), _tWami);
					}
					
					if(wami.getType() == (Wami.TYPE_RUN + 100)){
						UserWamiHistory _h = new UserWamiHistory();
						_h.taskid = wami.getTaskId();
						_h.userid = wami.getUserid();
						_h.run = wami.getAddTime();
						_h.cRun = wami.getLastmodTime();
						
						_tWami.add(_h);
					}
					
					if(wami.getType() == (Wami.TYPE_FINISH + 100)){
						UserWamiHistory _h = null;
						if(_tWami.size() > 0)
							_h = _tWami.get(_tWami.size() - 1);
						
						if(_h == null || _h.finish != null){
							_h = new UserWamiHistory();
							_h.taskid = wami.getTaskId();
							_h.userid = wami.getUserid();
						}
						
						_h.finish = wami.getAddTime();
						_h.cFinish = wami.getLastmodTime();
						
						_tWami.add(_h);
					}
				}
			}
		
		int rowindex = 0;
		HSSFRow row = sheet.createRow(rowindex++);
		
		int columnIndex = 0;
		HSSFCell cell = row.createCell(columnIndex++);
		cell.setCellValue("开始日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(start));
		cell = row.createCell(columnIndex++);
		cell.setCellValue("结束日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(end));

		row = sheet.createRow(rowindex++);
		
		row = sheet.createRow(rowindex++);
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("服务器时间");
		
		row = sheet.createRow(rowindex++);
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("APP ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("用户ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("启动运行时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("结束运行时间");
		cell = row.createCell(columnIndex++);
		
		for(Long appid : tWamis.keySet())
			for(Long userid : tWamis.get(appid).keySet())
				for(UserWamiHistory h : tWamis.get(appid).get(userid)){
					row = sheet.createRow(rowindex++);
					columnIndex = 0;
					cell = row.createCell(columnIndex++);
					cell.setCellValue(h.taskid);
					cell = row.createCell(columnIndex++);
					cell.setCellValue(h.userid);
					cell = row.createCell(columnIndex++);
					cell.setCellValue(IWamiUtils.getDateString(h.run));
					cell = row.createCell(columnIndex++);
					cell.setCellValue(IWamiUtils.getDateString(h.finish));
				}

		row = sheet.createRow(rowindex++);
		
		row = sheet.createRow(rowindex++);
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("客户端时间");
		
		row = sheet.createRow(rowindex++);
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("APP ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("用户ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("启动运行时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("结束运行时间");
		cell = row.createCell(columnIndex++);
		
		for(Long appid : tWamis.keySet())
			for(Long userid : tWamis.get(appid).keySet())
				for(UserWamiHistory h : tWamis.get(appid).get(userid)){
					row = sheet.createRow(rowindex++);
					columnIndex = 0;
					cell = row.createCell(columnIndex++);
					cell.setCellValue(h.taskid);
					cell = row.createCell(columnIndex++);
					cell.setCellValue(h.userid);
					cell = row.createCell(columnIndex++);
					cell.setCellValue(IWamiUtils.getDateString(h.cRun));
					cell = row.createCell(columnIndex++);
					cell.setCellValue(IWamiUtils.getDateString(h.cFinish));
				}
		
		return book;
	}

	private HSSFWorkbook genTaskWamiReport(Date start, Date end, String key) {
		end = DateUtils.addSeconds(DateUtils.addDays(end, 1), -1);
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_TASK_WAMI));
		
		Map<Long, Map<Long, UserWamiHistory>> tWamis = new HashMap<Long, Map<Long, UserWamiHistory>>();
		Set<Long> taskIds = new HashSet<Long>();

		List<Wami> wamis = wamiService.getWamis(start, end, key);
		if(wamis != null && wamis.size() > 0)
			for(Wami wami : wamis){
				Map<Long, UserWamiHistory> _tWamis = tWamis.get(wami.getUserid());
				if(_tWamis == null){
					_tWamis = new HashMap<Long, ReportBizImpl.UserWamiHistory>();
					tWamis.put(wami.getUserid(), _tWamis);
				}
				
				UserWamiHistory _tWami = _tWamis.get(wami.getTaskId());
				if(_tWami == null){
					_tWami = new UserWamiHistory();
					_tWamis.put(wami.getTaskId(), _tWami);
					
					taskIds.add(wami.getTaskId());
				}

				_tWami.taskid = wami.getTaskId();
				_tWami.userid = wami.getUserid();
				_tWami.channel = wami.getChannel();
				
				if(wami.getType() == Wami.TYPE_START){
					_tWami.start = wami.getLastmodTime();
					_tWami.cStart = wami.getAddTime();
				}
				
				if(wami.getType() == Wami.TYPE_DOWNLOAD_START){
					_tWami.downloading = wami.getLastmodTime();
					_tWami.cDownloading = wami.getAddTime();
				}
				
				if(wami.getType() == Wami.TYPE_DOWNLOAD_FINISH){
					_tWami.downloaded = wami.getLastmodTime();
					_tWami.cDownloaded = wami.getAddTime();
				}
				
				if(wami.getType() == Wami.TYPE_INSTALL){
					_tWami.install = wami.getLastmodTime();
					_tWami.cInstall = wami.getAddTime();
				}
				
				if(wami.getType() == Wami.TYPE_RUN){
					_tWami.start = wami.getLastmodTime();
					_tWami.cStart = wami.getAddTime();
				}
				
				if(wami.getType() == Wami.TYPE_FINISH){
					_tWami.finish = wami.getLastmodTime();
					_tWami.cFinish = wami.getAddTime();
					_tWami.prize = wami.getPrize();
				}
			}
		
		List<Task> tasks = taskService.getTasksByIds(taskIds);
		Map<Long, String> taskMap = new HashMap<Long, String>();
		if(tasks != null && tasks.size() > 0)
			for(Task task : tasks){
				String type = "未知任务";
				if((task.getType() & Task.TYPE_TREASURE) > 0)
					type = "宝箱任务";
				if((task.getType() & Task.TYPE_SHARE) > 0)
					type = "分享任务";
				if((task.getType() & Task.TYPE_GOLD) > 0)
					type = "金榜任务";
				if((task.getType() & Task.TYPE_OFFLINE) > 0)
					type = "线下任务";
				if((task.getType() & Task.TYPE_NORMAL) > 0)
					type = "普通任务";
				taskMap.put(task.getId(), type);
			}
		
		List<UserWamiHistory> history = new ArrayList<UserWamiHistory>();
		for(Long userid : tWamis.keySet())
			history.addAll(tWamis.get(userid).values());
		Collections.sort(history, new Comparator<UserWamiHistory>() {

			@Override
			public int compare(UserWamiHistory h1, UserWamiHistory h2) {
				int result = (int)(NumberUtils.toLong(h1.channel, 0) - NumberUtils.toLong(h2.channel, 0));
				if(result == 0){
					if(h1.finish == null)
						result = 1;
					else if(h2.finish == null)
						result = -1;
					else
						result = h1.finish.before(h2.finish) ? -1 : 1;
				}
				return result;
			}
		});
		
		int rowindex = 0;
		HSSFRow row = sheet.createRow(rowindex++);
		
		int columnIndex = 0;
		HSSFCell cell = row.createCell(columnIndex++);
		cell.setCellValue("开始日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(start));
		cell = row.createCell(columnIndex++);
		cell.setCellValue("结束日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(end));
		cell = row.createCell(columnIndex++);
		cell.setCellValue("渠道标志");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(key);

		row = sheet.createRow(rowindex++);
		
		row = sheet.createRow(rowindex++);
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("服务器时间");
		
		row = sheet.createRow(rowindex++);
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("米农ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("任务开始时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("下载开始时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("下载完成时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("安装完成时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("启动运行时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("任务完成时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("APP ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("任务类型");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("赠送米粒");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("渠道标志");
		
		for(UserWamiHistory h : history){
			row = sheet.createRow(rowindex++);
			columnIndex = 0;
			cell = row.createCell(columnIndex++);
			cell.setCellValue(h.userid);
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(h.start));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(h.downloading));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(h.downloaded));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(h.install));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(h.run));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(h.finish));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(h.taskid);
			cell = row.createCell(columnIndex++);
			cell.setCellValue(taskMap.get(h.taskid));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(h.prize);
			cell = row.createCell(columnIndex++);
			cell.setCellValue(h.channel);
		}

		row = sheet.createRow(rowindex++);
		
		row = sheet.createRow(rowindex++);
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("客户端时间");
		
		row = sheet.createRow(rowindex++);
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("米农ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("任务开始时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("下载开始时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("下载完成时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("安装完成时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("启动运行时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("任务完成时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("APP ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("任务类型");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("赠送米粒");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("渠道标志");
		
		for(UserWamiHistory h : history){
			row = sheet.createRow(rowindex++);
			columnIndex = 0;
			cell = row.createCell(columnIndex++);
			cell.setCellValue(h.userid);
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(h.cStart));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(h.cDownloading));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(h.cDownloaded));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(h.cInstall));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(h.cRun));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(h.cFinish));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(h.taskid);
			cell = row.createCell(columnIndex++);
			cell.setCellValue(taskMap.get(h.taskid));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(h.prize);
			cell = row.createCell(columnIndex++);
			cell.setCellValue(h.channel);
		}
		
		return book;
	}

	class UserWamiHistory{
		
		private long userid;
		
		private long taskid;
		
		private Date start;
		
		private Date downloading;
		
		private Date downloaded;
		
		private Date install;
		
		private Date run;
		
		private Date finish;
		
		private Date cStart;
		
		private Date cDownloading;
		
		private Date cDownloaded;
		
		private Date cInstall;
		
		private Date cRun;
		
		private Date cFinish;
		
		private int prize;
		
		private String channel;
	}

	private HSSFWorkbook genTaskIdReport(long id) {
		// 1. treasure ids
		Task task = taskService.getTaskById(id);
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_TASK_ID));
		if(task != null){
			// 2. wami status
			List<Wami> wamis = wamiService.getWamisById(task.getId());
			Map<Long, UserWamiHistory> tWamis = new HashMap<Long, UserWamiHistory>();
			int count = 0;
			
			if(wamis != null && wamis.size() > 0)
				for(Wami wami : wamis){
					
					UserWamiHistory _tWamis = tWamis.get(wami.getUserid());
					if(_tWamis == null){
						_tWamis = new UserWamiHistory();
						tWamis.put(wami.getUserid(), _tWamis);
					}

					_tWamis.channel = wami.getChannel();
					
					if(wami.getType() == Wami.TYPE_START){
						_tWamis.start = wami.getLastmodTime();
						_tWamis.cStart = wami.getAddTime();
					}
					
					if(wami.getType() == Wami.TYPE_DOWNLOAD_START){
						_tWamis.downloading = wami.getLastmodTime();
						_tWamis.cDownloading = wami.getAddTime();
					}
					
					if(wami.getType() == Wami.TYPE_DOWNLOAD_FINISH){
						_tWamis.downloaded = wami.getLastmodTime();
						_tWamis.cDownloaded = wami.getAddTime();
					}
					
					if(wami.getType() == Wami.TYPE_INSTALL){
						_tWamis.install = wami.getLastmodTime();
						_tWamis.cInstall = wami.getAddTime();
					}
					
					if(wami.getType() == Wami.TYPE_RUN){
						_tWamis.start = wami.getLastmodTime();
						_tWamis.cStart = wami.getAddTime();
					}
					
					if(wami.getType() == Wami.TYPE_FINISH){
						count ++;
						_tWamis.finish = wami.getLastmodTime();
						_tWamis.cFinish = wami.getAddTime();
					}
				}
			
			int rowindex = 0;
			HSSFRow row = sheet.createRow(rowindex++);
			
			int columnIndex = 0;
			HSSFCell cell = row.createCell(columnIndex++);
			cell.setCellValue("APP ID");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("APP名称");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("类型");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("开始日期");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("结束日期");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("设定米粒总数");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("已抢数量");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("下载成功总数");
			
			row = sheet.createRow(rowindex++);
			
			columnIndex = 0;
			cell = row.createCell(columnIndex++);
			cell.setCellValue(task.getId());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(task.getName());
			cell = row.createCell(columnIndex++);
			String type = "未知任务";
			if((task.getType() & Task.TYPE_TREASURE) > 0)
				type = "宝箱任务";
			if((task.getType() & Task.TYPE_SHARE) > 0)
				type = "分享任务";
			if((task.getType() & Task.TYPE_GOLD) > 0)
				type = "金榜任务";
			if((task.getType() & Task.TYPE_OFFLINE) > 0)
				type = "线下任务";
			if((task.getType() & Task.TYPE_NORMAL) > 0)
				type = "普通任务";
			cell.setCellValue(type);
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(task.getStartTime()));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(IWamiUtils.getDateString(task.getEndTime()));
			cell = row.createCell(columnIndex++);
			cell.setCellValue(task.getMaxPrize());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(task.getCurrentPrize());
			cell = row.createCell(columnIndex++);
			cell.setCellValue(count);

			row = sheet.createRow(rowindex++);
			
			row = sheet.createRow(rowindex++);
			columnIndex = 0;
			cell = row.createCell(columnIndex++);
			cell.setCellValue("服务器时间");
			
			row = sheet.createRow(rowindex++);
			columnIndex = 0;
			cell = row.createCell(columnIndex++);
			cell.setCellValue("米农ID");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("任务开始时间");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("下载开始时间");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("下载完成时间");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("安装完成时间");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("启动运行时间");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("任务完成时间");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("APP 来源");
			
			for(Long tmp : tWamis.keySet()){
				UserWamiHistory uwh = tWamis.get(tmp);
				
				row = sheet.createRow(rowindex++);
				columnIndex = 0;
				cell = row.createCell(columnIndex++);
				cell.setCellValue(tmp);
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(uwh.start));
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(uwh.downloading));
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(uwh.downloaded));
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(uwh.install));
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(uwh.run));
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(uwh.finish));
				cell = row.createCell(columnIndex++);
				cell.setCellValue(uwh.channel);
			}

			row = sheet.createRow(rowindex++);
			
			row = sheet.createRow(rowindex++);
			columnIndex = 0;
			cell = row.createCell(columnIndex++);
			cell.setCellValue("客户端时间");
			
			row = sheet.createRow(rowindex++);
			columnIndex = 0;
			cell = row.createCell(columnIndex++);
			cell.setCellValue("米农ID");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("任务开始时间");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("下载开始时间");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("下载完成时间");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("安装完成时间");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("启动运行时间");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("任务完成时间");
			cell = row.createCell(columnIndex++);
			cell.setCellValue("APP 来源");
			
			for(Long tmp : tWamis.keySet()){
				UserWamiHistory uwh = tWamis.get(tmp);
				
				row = sheet.createRow(rowindex++);
				columnIndex = 0;
				cell = row.createCell(columnIndex++);
				cell.setCellValue(tmp);
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(uwh.cStart));
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(uwh.cDownloading));
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(uwh.cDownloaded));
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(uwh.cInstall));
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(uwh.cRun));
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(uwh.cFinish));
				cell = row.createCell(columnIndex++);
				cell.setCellValue(uwh.channel);
			}
		}
		
		return book;
	}

	private HSSFWorkbook genTreasureReport(Date start, Date end) {
		end = DateUtils.addSeconds(DateUtils.addDays(end, 1), -1);
		
		// 1. treasure ids
		List<Long> ids = taskService.getTreasureTaskIds();
		Map<String, Map<Long, Date>> tTasks = new HashMap<String, Map<Long, Date>>();
		
		// 2. wami status
		List<Wami> wamis = wamiService.getWamisByIds(ids, start, end);
		if(wamis != null && wamis.size() > 0)
			for(Wami wami : wamis){
				String tmp = IWamiUtils.getDayDateString(wami.getLastmodTime());
				
				Map<Long, Date> _userids = tTasks.get(tmp);
				if(_userids == null){
					_userids = new HashMap<Long, Date>();
					tTasks.put(tmp, _userids);
				}
				
				_userids.put(wami.getUserid(), wami.getLastmodTime());
			}
		
		// 3. request log
		Map<String, Map<Long, Date>> rlogs = new HashMap<String, Map<Long, Date>>();
		List<Log> logs = logService.getLogsByType(Log.TYPE_TASK_TREASURE, start, end);
		if(logs != null && logs.size() > 0)
			for(Log log : logs){
				String tmpDate = IWamiUtils.getDayDateString(log.getAddTime());
				
				Map<Long, Date> _rlogs = rlogs.get(tmpDate);
				if(_rlogs == null){
					_rlogs = new HashMap<Long, Date>();
					rlogs.put(tmpDate, _rlogs);
				}
				_rlogs.put(log.getUserid(), log.getAddTime());
			}
		
		HSSFWorkbook book = new HSSFWorkbook();
		
		int rowindex = 0;
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_TASK_TREASURE));
		
		HSSFRow row = sheet.createRow(rowindex++);
		
		int columnIndex = 0;
		HSSFCell cell = row.createCell(columnIndex++);
		cell.setCellValue("开始日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(start));
		cell = row.createCell(columnIndex++);
		cell.setCellValue("结束日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue(IWamiUtils.getDayDateString(end));
		
		row = sheet.createRow(rowindex++);
		row = sheet.createRow(rowindex++);
		columnIndex = 0;
		cell = row.createCell(columnIndex++);
		cell.setCellValue("米农ID");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("红包时间");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("当天最后抢红包时间");
		
		for(String tmp : rlogs.keySet()){
			Map<Long, Date> _userids = rlogs.get(tmp);
			for(Long _userid : _userids.keySet()){
				row = sheet.createRow(rowindex++);
				columnIndex = 0;
				cell = row.createCell(columnIndex++);
				cell.setCellValue(_userid);
				cell = row.createCell(columnIndex++);
				cell.setCellValue(IWamiUtils.getDateString(_userids.get(_userid)));
				cell = row.createCell(columnIndex++);
				String time = "";
				
				if(tTasks.containsKey(tmp)){
					Date _time = tTasks.get(tmp).get(_userid);
					if(_time != null)
						time = IWamiUtils.getDateString(_time);
				}
				
				cell.setCellValue(time);
			}
		}
		
		return book;
	}

	private HSSFWorkbook genTopReport(Date start, Date end) {
		end = DateUtils.addSeconds(DateUtils.addDays(end, 1), -1);
		
		// 1. top ids
		List<Long> ids = taskService.getTopTaskIds();
		Map<String, Map<Long, Integer>> topTasks = new HashMap<String, Map<Long, Integer>>();
		
		// 2. wami status
		List<Wami> wamis = wamiService.getWamisByIds(ids, start, end);
		if(wamis != null && wamis.size() > 0)
			for(Wami wami : wamis){
				String tmp = IWamiUtils.getDayDateString(wami.getLastmodTime());
				
				Map<Long, Integer> _topTasks = topTasks.get(tmp);
				if(_topTasks == null){
					_topTasks = new HashMap<Long, Integer>();
					topTasks.put(tmp, _topTasks);
				}
				
				int count = 0;
				if(_topTasks.containsKey(wami.getTaskId()))
					count = _topTasks.get(wami.getTaskId());
				_topTasks.put(wami.getTaskId(), count + 1);
			}
		
		HSSFWorkbook book = new HSSFWorkbook();
		
		int rowindex = 0;
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_TASK_TOP));
		
		HSSFRow row = sheet.createRow(rowindex++);
		
		int columnIndex = 0;
		HSSFCell cell = row.createCell(columnIndex++);
		cell.setCellValue("日期");
		for(Long id : ids){
			cell = row.createCell(columnIndex++);
			cell.setCellValue(id);
		}
		
		for(Date tmp = start; tmp.before(end); tmp = DateUtils.addDays(tmp, 1)){
			row = sheet.createRow(rowindex++);
			String key = IWamiUtils.getDayDateString(tmp);
			
			columnIndex = 0;
			
			// date
			cell = row.createCell(columnIndex++);
			cell.setCellValue(key);
			
			for(Long id : ids){
				String count = "";
				if(topTasks.containsKey(key)){
					Integer _count = topTasks.get(key).get(id);
					if(_count != null)
						count = _count.toString();
				}
				cell = row.createCell(columnIndex++);
				cell.setCellValue(count);
			}
		}
		
		return book;
	}

	private HSSFWorkbook genOverViewReport(Date start, Date end) {
		end = DateUtils.addSeconds(DateUtils.addMonths(end, 1), -1);
		
		// 1. request log
		Map<String, Map<Integer, Integer>> rLogs = new HashMap<String, Map<Integer,Integer>>();
		List<Log> logs = logService.getLogs(start, end);
		if(logs != null && logs.size() > 0)
			for(Log log : logs){
				String tmpDate = IWamiUtils.getDayDateString(log.getAddTime());
				Map<Integer, Integer> _rLogs = rLogs.get(tmpDate);
				if(_rLogs == null){
					_rLogs = new HashMap<Integer, Integer>();
					rLogs.put(tmpDate, _rLogs);
				}
				
				int count = 0;
				if(_rLogs.containsKey(log.getType()))
					count = _rLogs.get(log.getType());
				_rLogs.put(log.getType(), count + 1);
			}
		// 2. top download
		List<Long> ids = taskService.getTopTaskIds();
		Map<String, Integer> topTasks = new HashMap<String, Integer>();
		
		// 3. wami status
		Map<String, Integer> downloading = new HashMap<String, Integer>();
		Map<String, Integer> downloaded = new HashMap<String, Integer>();
		Map<String, Integer> appRun = new HashMap<String, Integer>();
		Map<String, Integer> taskFinish = new HashMap<String, Integer>();
		Map<String, Integer> prizes = new HashMap<String, Integer>();
		
		List<Wami> wamis = wamiService.getWamis(start, end);
		if(wamis != null && wamis.size() > 0)
			for(Wami wami : wamis){
				String tmp = IWamiUtils.getDayDateString(wami.getLastmodTime());
				
				if(wami.getType() == Wami.TYPE_START && ids != null && ids.contains(wami.getTaskId())){
					int count = 0;
					if(topTasks.containsKey(tmp))
						count = topTasks.get(tmp);
					topTasks.put(tmp, count + 1);
				}
				
				if(wami.getType() == Wami.TYPE_DOWNLOAD_START){
					int count = 0;
					if(downloading.containsKey(tmp))
						count = downloading.get(tmp);
					downloading.put(tmp, count + 1);
				} else if(wami.getType() == Wami.TYPE_DOWNLOAD_FINISH){
					int count = 0;
					if(downloaded.containsKey(tmp))
						count = downloaded.get(tmp);
					downloaded.put(tmp, count + 1);
				} else if(wami.getType() == Wami.TYPE_RUN){
					int count = 0;
					if(appRun.containsKey(tmp))
						count = appRun.get(tmp);
					appRun.put(tmp, count + 1);
				} else if(wami.getType() == Wami.TYPE_FINISH){
					int count = 0;
					if(taskFinish.containsKey(tmp))
						count = taskFinish.get(tmp);
					taskFinish.put(tmp, count + 1);
					
					int prize = 0;
					if(prizes.containsKey(tmp))
						prize = prizes.get(tmp);
					prizes.put(tmp, prize + wami.getPrize());
				}
			}
		
		// 4. exchange
		Map<String, Map<Integer, Integer>> exhCnts = new HashMap<String, Map<Integer,Integer>>();
		Map<String, Map<Integer, Integer>> exhPrizes = new HashMap<String, Map<Integer,Integer>>();
		List<Exchange> exchanges = presentService.getExchanges(start, end);
		if(exchanges != null && exchanges.size() > 0)
			for(Exchange exchange : exchanges){
				int type = exchange.getPresentType();
				String tmp = IWamiUtils.getDayDateString(exchange.getAddTime());
				
				Map<Integer, Integer> _exhCnts = exhCnts.get(tmp);
				if(_exhCnts == null){
					_exhCnts = new HashMap<Integer, Integer>();
					exhCnts.put(tmp, _exhCnts);
				}
				int count = 0;
				if(_exhCnts.containsKey(type))
					count = _exhCnts.get(type);
				_exhCnts.put(type, count + 1);
				
				Map<Integer, Integer> _exhPrizes = exhPrizes.get(tmp);
				if(_exhPrizes == null){
					_exhPrizes = new HashMap<Integer, Integer>();
					exhPrizes.put(tmp, _exhPrizes);
				}
				count = 0;
				if(_exhPrizes.containsKey(type))
					count = _exhPrizes.get(type);
				if(type == Present.TYPE_LUCK){
					if(exchange.getPresentId() > 0)
						_exhPrizes.put(type, count + 1);
				} else
					_exhPrizes.put(type, count + exchange.getPrize());
			}

		// 5. onstart log
		List<Onstart> onstarts = onStartService.getOnstarts(start, end);
		Map<String, Set<String>> startMaps = new HashMap<String, Set<String>>();
		if(onstarts != null && onstarts.size() > 0)
			for(Onstart onstart : onstarts){
				String tmp = IWamiUtils.getDayDateString(onstart.getLastModTime());
				Set<String> _starts = startMaps.get(tmp);
				if(_starts == null){
					_starts = new HashSet<String>();
					startMaps.put(tmp, _starts);
				}
				_starts.add(onstart.getUuid());
			}
		
		// 6. user info
		List<User> users = userService.getUsers(start, end);
		Map<String, Integer> userMaps = new HashMap<String, Integer>();
		if(users != null && users.size() > 0)
			for(User user : users){
				String tmp = IWamiUtils.getDayDateString(user.getAddTime());
				int count = 0;
				if(userMaps.containsKey(tmp))
					count = userMaps.get(tmp);
				userMaps.put(tmp, count + 1);
			}
		
		// 7. share log
		List<Share> shares = presentService.getShares(start, end);
		Map<String, Integer> shareMaps = new HashMap<String, Integer>();
		if(shares != null && shares.size() > 0)
			for(Share share : shares){
				String tmp = IWamiUtils.getDayDateString(share.getLastModTime());
				int count = 0;
				if(shareMaps.containsKey(tmp))
					count = shareMaps.get(tmp);
				shareMaps.put(tmp, count + 1);
			}
		
		HSSFWorkbook book = new HSSFWorkbook();
		
		int rowindex = 0;
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_OVERVIEW_REPORT));
		
		HSSFRow row = sheet.createRow(rowindex++);
		
		int columnIndex = 0;
		HSSFCell cell = row.createCell(columnIndex++);
		cell.setCellValue("日期");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("攻略列表请求总数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("攻略详情请求总数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("榜单列表请求总数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("榜单下载总数");
		/*cell = row.createCell(columnIndex++);
		cell.setCellValue("榜单第2项下载总数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("榜单第3项下载总数");*/
		cell = row.createCell(columnIndex++);
		cell.setCellValue("挖米任务列表请求次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("挖米任务尝试下载次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("挖米任务下载成功次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("挖米任务APP启动次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("挖米任务成功完成次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("赠送米粒总次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("赠送米粒总数量");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("红包掉出次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("银行卡兑现次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("银行卡兑现米粒数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("支付宝兑现次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("支付宝兑现米粒数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("线上礼物兑现次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("线上礼物兑现米粒数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("线下礼物兑现次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("线下礼物兑现米粒数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("抽奖次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("中奖次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("赠送米粒给好友总次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("赠送米粒给好友米粒数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("新增总用户");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("新注册总用户");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("登录总用户");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("分享总次数");
		cell = row.createCell(columnIndex++);
		cell.setCellValue("爱挖米APP官网下载次数");
		
		for(Date tmp = start; tmp.before(end); tmp = DateUtils.addDays(tmp, 1)){
			row = sheet.createRow(rowindex++);
			String key = IWamiUtils.getDayDateString(tmp);
			
			columnIndex = 0;
			// 攻略列表请求总数
			cell = row.createCell(columnIndex++);
			cell.setCellValue(key);
			cell = row.createCell(columnIndex++);
			String count = "";
			if(rLogs.containsKey(key)){
				Integer _count = rLogs.get(key).get(Log.TYPE_STRATEGY_LIST);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 攻略详情请求总数
			cell = row.createCell(columnIndex++);
			count = "";
			if(rLogs.containsKey(key)){
				Integer _count = rLogs.get(key).get(Log.TYPE_STRATEGY_DETAIL);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 榜单列表请求总数
			cell = row.createCell(columnIndex++);
			count = "";
			if(rLogs.containsKey(key)){
				Integer _count = rLogs.get(key).get(Log.TYPE_TASK_TOP);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 榜单第1项下载总数
			cell = row.createCell(columnIndex++);
			count = "";
			if(topTasks.containsKey(key))
				count = topTasks.get(key).toString();
			cell.setCellValue(count);
			
//			// 榜单第2项下载总数
//			cell = row.createCell(columnIndex++);
//			count = "";
//			if(ids != null && ids.size() > 1 && topTasks.containsKey(key)){
//				Integer _count = topTasks.get(key).get(ids.get(1));
//				if(_count != null)
//					count = _count.toString();
//			}
//			cell.setCellValue(count);
//			
//			// 榜单第3项下载总数
//			cell = row.createCell(columnIndex++);
//			count = "";
//			if(ids != null && ids.size() > 2 && topTasks.containsKey(key)){
//				Integer _count = topTasks.get(key).get(ids.get(2));
//				if(_count != null)
//					count = _count.toString();
//			}
//			cell.setCellValue(count);
			
			// 挖米任务列表请求次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(rLogs.containsKey(key)){
				Integer _count = rLogs.get(key).get(Log.TYPE_TASK_TASK);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 挖米任务尝试下载次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(downloading.containsKey(key))
				count = downloading.get(key).toString();
			cell.setCellValue(count);
			
			// 挖米任务下载成功次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(downloaded.containsKey(key))
				count = downloaded.get(key).toString();
			cell.setCellValue(count);
			
			// 挖米任务APP启动次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(appRun.containsKey(key))
				count = appRun.get(key).toString();
			cell.setCellValue(count);
			
			// 挖米任务成功完成次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(taskFinish.containsKey(key))
				count = taskFinish.get(key).toString();
			cell.setCellValue(count);
			
			// 赠送米粒总次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(taskFinish.containsKey(key))
				count = taskFinish.get(key).toString();
			cell.setCellValue(count);
			
			// 赠送米粒总数量
			cell = row.createCell(columnIndex++);
			count = "";
			if(prizes.containsKey(key))
				count = prizes.get(key).toString();
			cell.setCellValue(count);
			
			// 红包掉出次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(rLogs.containsKey(key)){
				Integer _count = rLogs.get(key).get(Log.TYPE_TASK_TREASURE);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 银行卡兑现次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(exhCnts.containsKey(key)){
				Integer _count = exhCnts.get(key).get(Present.TYPE_ONLINE_RECHARGE_BANK);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 银行卡兑现米粒数
			cell = row.createCell(columnIndex++);
			count = "";
			if(exhPrizes.containsKey(key)){
				Integer _count = exhPrizes.get(key).get(Present.TYPE_ONLINE_RECHARGE_BANK);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 支付宝兑现次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(exhCnts.containsKey(key)){
				Integer _count = exhCnts.get(key).get(Present.TYPE_ONLINE_RECHARGE_ALIPAY);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 支付宝兑现米粒数
			cell = row.createCell(columnIndex++);
			count = "";
			if(exhPrizes.containsKey(key)){
				Integer _count = exhPrizes.get(key).get(Present.TYPE_ONLINE_RECHARGE_ALIPAY);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 线上礼物兑现次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(exhCnts.containsKey(key)){
				Integer _count = exhCnts.get(key).get(Present.TYPE_ONLINE_EMS);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 线上礼物兑现米粒数
			cell = row.createCell(columnIndex++);
			count = "";
			if(exhPrizes.containsKey(key)){
				Integer _count = exhPrizes.get(key).get(Present.TYPE_ONLINE_EMS);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 线下礼物兑现次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(exhCnts.containsKey(key)){
				Integer _count = exhCnts.get(key).get(Present.TYPE_OFFLINE);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 线下礼物兑现米粒数
			cell = row.createCell(columnIndex++);
			count = "";
			if(exhPrizes.containsKey(key)){
				Integer _count = exhPrizes.get(key).get(Present.TYPE_OFFLINE);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 抽奖次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(exhCnts.containsKey(key)){
				Integer _count = exhCnts.get(key).get(Present.TYPE_LUCK);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 中奖次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(exhPrizes.containsKey(key)){
				Integer _count = exhPrizes.get(key).get(Present.TYPE_LUCK);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 赠送米粒给好友总次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(exhCnts.containsKey(key)){
				Integer _count = exhCnts.get(key).get(Present.TYPE_GIFT);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 赠送米粒给好友米粒数
			cell = row.createCell(columnIndex++);
			count = "";
			if(exhPrizes.containsKey(key)){
				Integer _count = exhPrizes.get(key).get(Present.TYPE_GIFT);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 新增总用户
			cell = row.createCell(columnIndex++);
			count = "";
			if(startMaps.containsKey(key))
				count = "" + startMaps.get(key).size();
			cell.setCellValue(count);
			
			// 新注册总用户
			cell = row.createCell(columnIndex++);
			count = "";
			if(userMaps.containsKey(key))
				count = userMaps.get(key).toString();
			cell.setCellValue(count);
			
			// 登录总用户
			cell = row.createCell(columnIndex++);
			count = "";
			if(rLogs.containsKey(key)){
				Integer _count = rLogs.get(key).get(Log.TYPE_LOGIN);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
			
			// 分享总次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(shareMaps.containsKey(key))
				count = shareMaps.get(key).toString();
			cell.setCellValue(count);
			
			//爱挖米APP官网下载次数
			cell = row.createCell(columnIndex++);
			count = "";
			if(rLogs.containsKey(key)){
				Integer _count = rLogs.get(key).get(Log.TYPE_APP_DOWNLOAD);
				if(_count != null)
					count = _count.toString();
			}
			cell.setCellValue(count);
		}
		
		return book;
	}

	public LoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	public LogService getLogService() {
		return logService;
	}

	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public WamiService getWamiService() {
		return wamiService;
	}

	public void setWamiService(WamiService wamiService) {
		this.wamiService = wamiService;
	}

	public PresentService getPresentService() {
		return presentService;
	}

	public void setPresentService(PresentService presentService) {
		this.presentService = presentService;
	}

	public OnstartService getOnStartService() {
		return onStartService;
	}

	public void setOnStartService(OnstartService onStartService) {
		this.onStartService = onStartService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
