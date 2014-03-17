package com.iwami.iwami.app.biz.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.iwami.iwami.app.biz.ReportBiz;
import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.model.Exchange;
import com.iwami.iwami.app.model.Log;
import com.iwami.iwami.app.model.Onstart;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.model.ReportParam;
import com.iwami.iwami.app.model.Share;
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
		
		if(adminid > 0 && loginService.checkLogin(adminid) && loginService.checkRole(adminid, IWamiConstants.DOWNLOAD_MANAGEMENT)){
			if(param.getType() == ReportParam.TYPE_OVERVIEW_REPORT){
				Date start = IWamiUtils.getMonthDate(param.getStart());
				Date end = IWamiUtils.getMonthDate(param.getEnd());
				
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
				
			} else if(param.getType() == ReportParam.TYPE_TASK_WAMI){
				
			} else if(param.getType() == ReportParam.TYPE_TASK_HISTORY){
				
			} else if(param.getType() == ReportParam.TYPE_EXCHANGE){
				
			} else if(param.getType() == ReportParam.TYPE_GIFT){
				
			} else if(param.getType() == ReportParam.TYPE_USER_INFO){
				
			} else if(param.getType() == ReportParam.TYPE_USER_LOGIN){
				
			} else if(param.getType() == ReportParam.TYPE_PRESENT_SUMMARY){
				
			} else if(param.getType() == ReportParam.TYPE_PRESENT_HISTORY){
				
			} else if(param.getType() == ReportParam.TYPE_PRESENT_OFFLINE){
				
			} else if(param.getType() == ReportParam.TYPE_SHARE){
				
			} else if(param.getType() == ReportParam.TYPE_APK_DOWNLOAD){
				
			} else
				throw new RuntimeException("not recognized param >> " + param);
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
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_OVERVIEW_REPORT));
		
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
		HSSFSheet sheet = book.createSheet(ReportParam.TYPE_TITLES.get(ReportParam.TYPE_OVERVIEW_REPORT));
		
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
