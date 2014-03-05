package com.iwami.iwami.app.biz.impl;

import org.springframework.transaction.annotation.Transactional;

import com.iwami.iwami.app.biz.ContactBiz;
import com.iwami.iwami.app.model.Contact;
import com.iwami.iwami.app.service.ContactService;

public class ContactBizImpl implements ContactBiz {

	private ContactService contactService;
	
	@Override
	public Contact getContact() {
		return contactService.getContact();
	}

	@Override
	@Transactional(rollbackFor=Exception.class, value="txManager")
	public boolean modContact(Contact contact) {
		Contact tmp = getContact();
		if(tmp == null || contactService.delAllContacts())
			if(contactService.addContact(contact))
				return true;
			else
				throw new RuntimeException("Failed in add contact, so rollback...");
		else
			return false;
	}

	public ContactService getContactService() {
		return contactService;
	}

	public void setContactService(ContactService contactService) {
		this.contactService = contactService;
	}

}
