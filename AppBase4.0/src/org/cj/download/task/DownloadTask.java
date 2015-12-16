package org.cj.download.task;

import org.cj.download.DownloadThread;
import org.cj.download.IDownloadListener;
import org.cj.download.info.DownInfo;

public class DownloadTask implements ITask
{

	String				id;
	IDownloadListener	listener;
	DownloadThread		thread;
	DownInfo			info;

	public DownloadTask(String id)
	{
		this.id = id;
	}

	public DownloadTask(String id, DownloadThread thread, IDownloadListener l)
	{
		// TODO Auto-generated constructor stub
		this.id = id;
		this.thread = thread;
		this.listener = l;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the listener
	 */
	public IDownloadListener getListener()
	{
		return listener;
	}

	/**
	 * @param listener
	 *            the listener to set
	 */
	public void setListener(IDownloadListener listener)
	{
		this.listener = listener;
	}

	/**
	 * @return the thread
	 */
	public Runnable getThread()
	{
		return this.thread;
	}

	/**
	 * @param thread
	 *            the thread to set
	 */
	public void setThread(DownloadThread thread)
	{
		this.thread = thread;
	}

	@Override
	public DownInfo getInfo()
	{
		// TODO Auto-generated method stub
		return this.info;
	}

}
