package com.iwami.iwami.app.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;

public class IWamiUtils {
	
    private static final char[] CODE_ARRAY_NUMBER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    public static String getRandInt(int length) {
	    Random rand = new Random();
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < length; i++) {
	        sb.append(CODE_ARRAY_NUMBER[rand.nextInt(CODE_ARRAY_NUMBER.length)]);
	    }
	    return sb.toString();
    }
    
	public static boolean validatePhone(String phone) {
        if (phone.length() != 11) {
            return false;
        }
        if (!phone.startsWith("1")) {
            return false;
        }
        for (int i = 0; i < phone.length(); i++) {
            char c = phone.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

	public static int calcNew(String newV, String oldV) {
		if(StringUtils.isNotBlank(newV)){
			String[] v1s = StringUtils.split(newV, ".");
			String[] v2s = StringUtils.split(oldV, ".");
			
			for(int i = 0; i < v1s.length; i ++){
				int v1i = NumberUtils.toInt(v1s[i], 0);
				
				if(v2s != null && v2s.length > i){
					int v2i = NumberUtils.toInt(v2s[i], -1);
					
					if(v1i > v2i)
						return 1;
					else if(v1i < v2i)
						return 0;
					else
						continue;
				} else
					return 1;
			}
		}
		return 0;
	}

	public static Date getTodayStart() {
		return DateUtils.truncate(new Date(), Calendar.DATE);
	}
}
