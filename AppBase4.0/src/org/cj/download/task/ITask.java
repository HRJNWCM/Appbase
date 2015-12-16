package org.cj.download.task;

import org.cj.download.info.DownInfo;

public interface ITask
{
	DownInfo getInfo();

	Runnable getThread();
}
