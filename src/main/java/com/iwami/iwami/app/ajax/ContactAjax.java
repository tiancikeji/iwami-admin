package com.iwami.iwami.app.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iwami.iwami.app.biz.ContactBiz;
import com.iwami.iwami.app.common.dispatch.AjaxClass;
import com.iwami.iwami.app.common.dispatch.AjaxMethod;
import com.iwami.iwami.app.constants.ErrorCodeConstants;
import com.iwami.iwami.app.model.Contact;

@AjaxClass
public class ContactAjax {
	
	private Log logger = LogFactory.getLog(getClass());
	
	private ContactBiz contactBiz;

	@AjaxMethod(path = "contact.ajax")
	public Map<Object, Object> getContact(Map<String, String> params) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try{
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
			}
			result.put("data", data);
			result.put(ErrorCodeConstants.STATUS_KEY, ErrorCodeConstants.STATUS_OK);
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
