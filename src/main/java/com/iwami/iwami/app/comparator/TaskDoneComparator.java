package com.iwami.iwami.app.comparator;

import java.util.Comparator;

import com.iwami.iwami.app.model.Task;

public class TaskDoneComparator implements Comparator<Task> {

	@Override
	public int compare(Task t1, Task t2) {
		long time1 = Long.MAX_VALUE;
		long time2 = Long.MAX_VALUE;
		
		if(t1 != null){
			time1 = t1.getLastModTime().getTime();
		}
		if(t2 != null){
			 time2 = t2.getLastModTime().getTime();
		}
		
		return (int)(time2 - time1);
	}

}
