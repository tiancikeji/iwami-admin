package com.iwami.iwami.app.service.impl;

import com.iwami.iwami.app.dao.ContactDao;
import com.iwami.iwami.app.model.Contact;
import com.iwami.iwami.app.service.ContactService;

public class ContactServiceImpl implements ContactService {

	private ContactDao contactDao;
	
	@Override
	public Contact getContact() {
		return contactDao.getContact();
	}

	@Override
	public boolean delAllContacts() {
		return contactDao.delAllContacts();
	}

	@Override
	public boolean addContact(Contact contact) {
		return contactDao.addContact(contact);
	}

	public ContactDao getContactDao() {
		return contactDao;
	}

	public void setContactDao(ContactDao contactDao) {
		this.contactDao = contactDao;
	}

}
