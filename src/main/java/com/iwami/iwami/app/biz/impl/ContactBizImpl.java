package com.iwami.iwami.app.biz.impl;

import com.iwami.iwami.app.biz.ContactBiz;
import com.iwami.iwami.app.model.Contact;
import com.iwami.iwami.app.service.ContactService;

public class ContactBizImpl implements ContactBiz {

	private ContactService contactService;
	
	@Override
	public Contact getContact() {
		return contactService.getContact();
	}

	public ContactService getContactService() {
		return contactService;
	}

	public void setContactService(ContactService contactService) {
		this.contactService = contactService;
	}

}
