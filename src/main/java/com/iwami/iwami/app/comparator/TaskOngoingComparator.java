package com.iwami.iwami.app.comparator;

import java.util.Comparator;

import com.iwami.iwami.app.model.Task;

public class TaskOngoingComparator implements Comparator<Task> {

	@Override
	public int compare(Task t1, Task t2) {
		int type1 = Integer.MAX_VALUE;
		int type2 = Integer.MAX_VALUE;
		long time1 = Long.MAX_VALUE;
		long time2 = Long.MAX_VALUE;
		
		if(t1 != null){
			type1 = t1.getType();
			time1 = t1.getLastModTime().getTime();
		}
		if(t2 != null){
			 type2 = t2.getType();
			 time2 = t2.getLastModTime().getTime();
		}
		
		int count = type2 - type1;
		if(count == 0)
			return (int)(time2 - time1);
		else
			return count;
	}

}
