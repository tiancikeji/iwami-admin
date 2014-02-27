package com.iwami.iwami.app.service.impl;

import com.iwami.iwami.app.constants.IWamiConstants;
import com.iwami.iwami.app.dao.ContactDao;
import com.iwami.iwami.app.model.Contact;
import com.iwami.iwami.app.service.ContactService;
import com.iwami.iwami.app.util.LocalCaches;

public class ContactServiceImpl implements ContactService {

	private ContactDao contactDao;
	
	private long expireTime;
	
	@Override
	public Contact getContact() {
		Contact contact = (Contact)LocalCaches.get(IWamiConstants.CACHE_CONTACT_KEY, System.currentTimeMillis(), expireTime);
		
		if(contact == null){
			contact = contactDao.getContact();
			LocalCaches.set(IWamiConstants.CACHE_CONTACT_KEY, contact, System.currentTimeMillis());
		}
		
		return contact;
	}

	public ContactDao getContactDao() {
		return contactDao;
	}

	public void setContactDao(ContactDao contactDao) {
		this.contactDao = contactDao;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

}
