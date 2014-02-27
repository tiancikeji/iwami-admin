package com.iwami.iwami.app.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class LocalCaches {
	
	/**
	 * Expire time: 1 hour
	 */
	private static final int CACHE_INVALID_TIME = 60 * 60;

	private static CacheManager manager = null;

	private static Cache cache = null;

	static {
		manager = CacheManager.create();
		cache = new Cache("iwamicache", 10000, false, false, CACHE_INVALID_TIME, CACHE_INVALID_TIME);
		cache.setCacheManager(manager);
		cache.initialise();
	}

	public void finalize() {
		manager.shutdown();
		try {
			super.finalize();
		} catch (Throwable e) {
		}
	}

	public static Object get(String key, long time, long expire) {
		Element cacheElement = cache.get(key);
		if (cacheElement == null) {
			return null;
		}
		if((time - cacheElement.getVersion()) > expire)
			return null;
		else
			return cacheElement.getObjectValue();
	}

	public static void set(String key, Object value, long time) {
		if (key == null || value == null) {
			return;
		}
		cache.put(new Element(key, value, time));
	}
}
