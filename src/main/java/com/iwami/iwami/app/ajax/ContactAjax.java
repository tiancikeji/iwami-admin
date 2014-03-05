package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.ContactBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.model.Contact;
import com.iwami.iwami.app.util.IWamiUtils;

@AjaxClass
public class ContactAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private ContactBiz contactBiz;

	@AjaxMethod(path = "GET/contact.ajax")
	public Map<Object, Object> getContact(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				//TODO check admin id
				
				if(adminid > 0){
					Contact contact = contactBiz.getContact();
					Map<String, Object> data = new HashMap<String, Object>();
					if(contact != null){
						data.put("phone1", contact.getPhone1());
						data.put("email1", contact.getEmail1());
						data.put("domain", contact.getDomain());
						data.put("qq", contact.getQq());
						data.put("qqgroup", contact.getQqgroup());
						data.put("phone2", contact.getPhone2());
						data.put("email2", contact.getEmail2());
						
						data.put("lastModTime", IWamiUtils.getDateString(contact.getLastmodTime()));
						data.put("lastModUserid", contact.getLastmodUserid());
					}
					result.put("data", data);
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in contact", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	@AjaxMethod(path = "MOD/contact.ajax")
	public Map<Object, Object> modContact(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
			if(params.containsKey("adminid")
					&& params.containsKey("phone1") && params.containsKey("email1") && params.containsKey("domain")
					&& params.containsKey("qq") && params.containsKey("qqgroup") && params.containsKey("phone2")
					&& params.containsKey("email2")){
				long adminid = NumberUtils.toLong(params.get("adminid"), -1);
				//TODO check admin id
				
				String phone1 = StringUtils.trimToEmpty(params.get("phone1"));
				String email1 = StringUtils.trimToEmpty(params.get("email1"));
				String domain = StringUtils.trimToEmpty(params.get("domain"));
				String qq = StringUtils.trimToEmpty(params.get("qq"));
				String qqgroup = StringUtils.trimToEmpty(params.get("qqgroup"));
				String phone2 = StringUtils.trimToEmpty(params.get("phone2"));
				String email2 = StringUtils.trimToEmpty(params.get("email2"));
				if(adminid > 0 && StringUtils.isNotBlank(phone1)
						 && StringUtils.isNotBlank(email1) && StringUtils.isNotBlank(domain) && StringUtils.isNotBlank(qq)
						 && StringUtils.isNotBlank(qqgroup) && StringUtils.isNotBlank(phone2) && StringUtils.isNotBlank(email2)){
					Contact contact = new Contact();
					contact.setPhone1(phone1);
					contact.setEmail1(email1);
					contact.setDomain(domain);
					contact.setQq(qq);
					contact.setQqgroup(qqgroup);
					contact.setPhone2(phone2);
					contact.setEmail2(email2);
					contact.setLastmodUserid(adminid);
					
					if(contactBiz.modContact(contact))
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
					else
						result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
				} else
					result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
			} else
				result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_PARAM_ERROR);
		} catch(Throwable t){
			if(logger.isErrorEnabled())
				logger.error("Exception in contact", t);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_ERROR);
		}
		
		return result;
	}

	public ContactBiz getContactBiz() {
		return contactBiz;
	}

	public void setContactBiz(ContactBiz contactBiz) {
		this.contactBiz = contactBiz;
	}

}
