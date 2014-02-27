package com.iwami.iwami.app.biz;

import com.iwami.iwami.app.exception.TaskFinishedException;
import com.iwami.iwami.app.exception.TaskNotExistsException;
import com.iwami.iwami.app.exception.TaskRepeatStartException;
import com.iwami.iwami.app.exception.TaskUnavailableException;
import com.iwami.iwami.app.exception.TaskWamiedException;
import com.iwami.iwami.app.model.Task;
import com.iwami.iwami.app.model.User;

public interface WamiBiz {

	public void wami(User user, Task task, int type, long time, String channel) throws TaskRepeatStartException, TaskNotExistsException, TaskFinishedException, TaskUnavailableException, TaskWamiedException;

	public void share(User user, Task task, int type, long time, String channel);
}
