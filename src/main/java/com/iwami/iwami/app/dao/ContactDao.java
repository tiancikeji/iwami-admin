package com.iwami.iwami.app.dao;

import com.iwami.iwami.app.model.Contact;

public interface ContactDao {

	public Contact getContact();
	
	public boolean delAllContacts();
	
	public boolean addContact(Contact contact);
	
}
