package com.iwami.iwami.app.sal;

public interface JPushSAL {

	public int sendCustomMessageWithAlias(int sendNo, String alias, String title, String content);
}
