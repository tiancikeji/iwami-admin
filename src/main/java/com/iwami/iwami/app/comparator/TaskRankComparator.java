package com.iwami.iwami.app.comparator;

import java.util.Comparator;

import com.iwami.iwami.app.model.Task;

public class TaskRankComparator implements Comparator<Task> {

	@Override
	public int compare(Task task1, Task task2) {
		int rank1 = -1;
		int rank2 = -1;
		
		if(task1 != null)
			rank1 = task1.getRank();
		if(task2 != null)
			rank2 = task2.getRank();
		
		return rank1 - rank2;
	}

}
