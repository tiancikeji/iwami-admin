package com.iwami.iwami.app.biz;

import java.util.List;

import com.iwami.iwami.app.model.Push;

public interface PushBiz {

	public boolean pushUserMsg(String alias, String msg);

	public boolean pushAllMsgs(String msg, long interval, long adminid);

	public boolean pushWhiteMsgs(String file, String msg, long interval, long adminid);

	public boolean pushBlackMsgs(String file, String msg, long interval, long adminid);

	public List<Push> getUnFinishedPushTasks();

	public boolean pauseTask(long id, long adminid);

	public boolean continueTask(long id, long adminid);

	public boolean stopTask(long id, long adminid);

}
