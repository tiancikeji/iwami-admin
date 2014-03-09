package com.iwami.iwami.app.biz;

import com.iwami.iwami.app.model.Apk;
import com.iwami.iwami.app.model.Present;
import com.iwami.iwami.app.model.Strategy;
import com.iwami.iwami.app.model.StrategyImage;
import com.iwami.iwami.app.model.StrategyInfo;
import com.iwami.iwami.app.model.Task;

public interface FileBiz {

	public String addFile(String data, String name);

	public boolean uploadTaskResource(Task task);

	public boolean uploadImageResource(StrategyImage image);

	public boolean uploadStrategyResource(Strategy strategy);

	public boolean uploadStrategyInfoResource(StrategyInfo info);

	public boolean uploadApkResource(Apk apk);

	public boolean uploadPresentResource(Present present);

}
