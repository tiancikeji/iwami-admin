package com.iwami.iwami.app.service;

import com.iwami.iwami.app.model.Contact;

public interface ContactService {

	public Contact getContact();

	public boolean delAllContacts();

	public boolean addContact(Contact contact);
}
