package com.iwami.iwami.app.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.iwami.iwami.app.dao.OnstartDao;
import com.iwami.iwami.app.model.Onstart;
import com.iwami.iwami.app.model.User;
import com.iwami.iwami.app.service.OnstartService;

public class OnstartServiceImpl implements OnstartService {
	
	private OnstartDao onstartDao;

	@Override
	public boolean logOnstart(Onstart onstart) {
		if(onstart != null)
			return onstartDao.logOnstart(onstart);
		else
			return false;
	}

	@Override
	public Map<Long, Date> getLastLogins(List<Long> userids) {
		return onstartDao.getLastLogins(userids);
	}

	@Override
	public Map<Long, Date> getCreateTimes(List<User> users) {
		Map<Long, Date> result = new HashMap<Long, Date>();
		if(users != null && users.size() > 0){
			List<Long> cellPhones = new ArrayList<Long>();
			List<String> uuids = new ArrayList<String>();
			List<String> alias = new ArrayList<String>();
			
			for(User user : users)
				if(user != null){
					if(user.getCellPhone() > 0)
						cellPhones.add(user.getCellPhone());
					if(StringUtils.isNotBlank(user.getUuid()))
						uuids.add(user.getUuid());
					if(StringUtils.isNotBlank(user.getAlias()))
						alias.add(user.getAlias());
				}
			
			if(cellPhones.size() > 0 || uuids.size() > 0 || alias.size() > 0){
				List<Onstart> starts = onstartDao.getOnstartsByUser(cellPhones, uuids, alias);
				
				if(starts != null && starts.size() > 0)
					for(Onstart start : starts)
						if(start != null){
							Long cellPhone = start.getCellPhone();
							String uuid = start.getUuid();
							String alia = start.getAlias();
							
							// inefficient to find user...
							for(User user : users)
								if((cellPhone > 0 && user.getCellPhone() == cellPhone)
										|| (StringUtils.isNotBlank(uuid) && StringUtils.equals(uuid, user.getUuid()))
										|| (StringUtils.isNotBlank(alia) && StringUtils.equals(alia, user.getAlias()))){
									Date tmp = result.get(user.getId());
									if(tmp == null || tmp.after(start.getLastModTime()))
										result.put(user.getId(), start.getLastModTime());
									
									break;
								}
						}
			}
		}
		
		return result;
	}

	@Override
	public List<Onstart> getOnstarts(Date start, Date end) {
		return onstartDao.getOnstarts(start, end);
	}

	public OnstartDao getOnstartDao() {
		return onstartDao;
	}

	public void setOnstartDao(OnstartDao onstartDao) {
		this.onstartDao = onstartDao;
	}
}
