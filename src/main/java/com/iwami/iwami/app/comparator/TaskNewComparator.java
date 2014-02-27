package com.iwami.iwami.app.comparator;

import java.util.Comparator;

import com.iwami.iwami.app.model.Task;

public class TaskNewComparator implements Comparator<Task> {

	@Override
	public int compare(Task task1, Task task2) {
		int available1 = 10;
		int available2 = 10;
		long date1 = Long.MAX_VALUE;
		long date2 = Long.MAX_VALUE;
		
		if(task1 != null){
			available1 = task1.getAvailable();
			date1 = task1.getStartTime().getTime();
		}
		if(task2 != null){
			available2 = task2.getAvailable();
			date2 = task2.getStartTime().getTime();
		}
		
		int result = available1 - available2;
		if(result == 0){
			return (int)(date2 - date1);
		}
		
		return result;
	}

}
