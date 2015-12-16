package org.cj.download;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.cj.download.info.DownInfo;
import org.cj.download.task.DownloadTask;

import android.content.Context;

/**
 * 下载管理器
 * 
 * @author HR
 * 
 */
public class DownLoadMananger
{
	public static DownLoadMananger	mInstance	   = new DownLoadMananger();
	public Map<String,DownloadTask>	mDownloadTasks	= new HashMap<String,DownloadTask>();
	public DownObserve	            mObserve;
	public static Context	        mContext;

	private DownLoadMananger()
	{
		// TODO Auto-generated constructor stub
		mObserve = new DownObserve();
	}

	public static DownLoadMananger getInstance(Context context)
	{
		mContext = context;
		if (mInstance == null) mInstance = new DownLoadMananger();
		return mInstance;
	}

	public synchronized void execute(DownInfo info, IDownloadListener l)
	{
		if (isTaskExit(info.getUrl()))
		{
			resetCallback(info.getUrl(), l);
			return;
		}
		DownloadThread thread = new DownloadThread(info, Status.UNKNOW, mContext);
		thread.addObserver(new DownObserve());
		DownloadTask task = new DownloadTask(info.getUrl(), thread, l);
		mDownloadTasks.put(info.getUrl(), task);
		((DownloadThread) task.getThread()).onStart();
	}

	boolean isTaskExit(String id)
	{
		return mDownloadTasks.containsKey(id);
	}

	public void stop(String id)
	{
		if (isTaskExit(id))
		{
			((DownloadThread) mDownloadTasks.get(id).getThread()).stop();
			mDownloadTasks.remove(id);
		}
	}

	/**
	 * 设置回调
	 * 
	 * @param id
	 * @param l
	 */
	public void resetCallback(String id, IDownloadListener l)
	{
		DownloadTask task = mDownloadTasks.get(id);
		if (task == null) return;
		task.setListener(l);
	}

	public void shutDown()
	{
		Iterator<String> iterator = mDownloadTasks.keySet().iterator();
		while (iterator.hasNext())
		{
			String id = iterator.next();
			DownloadTask task = mDownloadTasks.get(id);
			((DownloadThread) task.getThread()).stop();
		}
		mDownloadTasks.clear();
	}

	/**
	 * 
	 * @author Eway
	 * 
	 */
	public class DownObserve implements Observer
	{
		public void addObserve(DownloadThread thread)
		{
			thread.addObserver(this);
		}

		@Override
		public void update(Observable arg0, Object arg1)
		{
			// TODO Auto-generated method stub
			DownloadThread thread = (DownloadThread) arg0;
			DownloadTask task;
			RespObj obj;
			String url = thread.info.getUrl();
			switch (thread.status)
			{
				case Status.UNKNOW:
					task = mDownloadTasks.get(url);
					if (task == null) return;
					if (task.getListener() == null) return;
					obj = new RespObj(url, Status.UNKNOW);
					obj.setName(thread.info.getName());
					task.getListener().onResponse(obj);
					break;
				case Status.DOWNLOADING:
					task = mDownloadTasks.get(url);
					if (task == null) return;
					if (task.getListener() == null) return;
					obj = new RespObj(url, Status.DOWNLOADING);
					obj.setName(thread.info.getName());
					obj.progress = thread.getProgress();
					task.getListener().onResponse(obj);
					break;
				case Status.PAUSE:
				case Status.STOP:
					break;
				case Status.COMPLETE:
					task = mDownloadTasks.remove(url);
					if (task == null) return;
					if (task.getListener() == null) return;
					obj = new RespObj(url, Status.COMPLETE);
					obj.setName(thread.info.getName());
					obj.progress = thread.getProgress();
					task.getListener().onResponse(obj);
					break;
				case Status.ERROR:
					task = mDownloadTasks.remove(url);
					if (task == null) return;
					if (task.getListener() == null) return;
					obj = new RespObj(url, Status.ERROR);
					obj.setName(thread.info.getName());
					task.getListener().onResponse(obj);
					break;
				default:
					break;
			}
		}
	}
}
