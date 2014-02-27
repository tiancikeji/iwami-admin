package com.iwami.iwami.app.dao;

import java.util.List;

import com.iwami.iwami.app.model.Onstart;

public interface OnstartDao {

	public boolean logOnstart(Onstart onstart);
	
	public List<Onstart> getAllOnstarts();
}
