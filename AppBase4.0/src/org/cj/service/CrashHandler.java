package org.cj.service;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import org.cj.config._Config;
import org.cj.logUtil.LogUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

public class CrashHandler implements UncaughtExceptionHandler
{
	private Thread.UncaughtExceptionHandler	defaultExceptionHandler;
	String	                                name;
	private Context	                        mContext;
	static CrashHandler	                    instance;
	boolean	                                isRestart;

	private CrashHandler()
	{
	}

	public static CrashHandler getInstance()
	{
		if (instance == null)
		{
			instance = new CrashHandler();
		}
		return instance;
	}

	public void init(Context context, String AppName)
	{
		this.init(context, AppName, true);
	}

	public void init(Context context, String AppName, int logo)
	{
		this.init(context, AppName, true);
	}

	public void init(Context context, String AppName, boolean isRestart)
	{
		this.isRestart = isRestart;
		mContext = context;
		name = AppName;
		defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable arg1)
	{
		// TODO Auto-generated method stub
		if (defaultExceptionHandler != null && !handleException(arg1)) defaultExceptionHandler
		        .uncaughtException(arg0, arg1);
		else
		{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					Looper.prepare();
					Toast.makeText(mContext, mContext.getString(org.cj.R.string.oop_error), Toast.LENGTH_LONG)
					        .show();
					Looper.loop();
				}
			}).start();
		}
		if (isRestart) restart();
		else
		{
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally
			{
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}
	}

	void restart()
	{
		Intent intent = mContext.getPackageManager()
		        .getLaunchIntentForPackage(mContext.getApplicationContext()
		                .getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	private boolean handleException(Throwable e)
	{
		if (e == null) return false;
		final StringBuffer sb = new StringBuffer();
		String msg = "-----------ERROR info----------\n" + e.getLocalizedMessage();
		Throwable cause = e.getCause();
		sb.append(msg + "\n");
		StackTraceElement[] stackTraceElements = e.getStackTrace();
		try
		{
			StackTraceElement[] causeElements = cause.getStackTrace();
			for ( StackTraceElement element : causeElements)
			{
				sb.append(element.toString() + "\n");
			}
		} catch (Exception e2)
		{
			// TODO: handle exception
		}
		try
		{
			for ( StackTraceElement element : stackTraceElements)
			{
				sb.append(element.toString() + "\n");
			}
			final String info = "DeviceInfo:\n" + collectDeviceInfo() + "\n"
			        + sb.toString();
			LogUtil.HLog().setPrint(true);
			LogUtil.HLog().w(info);
			ExObj exObj = new ExObj();
			exObj.setName(name);
			exObj.setError(info);
			mContext.startService(new Intent(_Config.get().getCrashAction())
			        .putExtra(ExObj.TAG, exObj));
		} catch (Exception exception)
		{
			// TODO: handle exception
			exception.printStackTrace();
		}
		return true;
	}

	/**
	 * 获取设备信息
	 * 
	 * @return
	 * @throws NameNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public String collectDeviceInfo() throws NameNotFoundException,
	        IllegalArgumentException, IllegalAccessException
	{
		StringBuffer deviceInfo = new StringBuffer();
		PackageManager pmManager = mContext.getPackageManager();
		PackageInfo pi = pmManager
		        .getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
		if (pi != null)
		{
			deviceInfo.append("versionName：" + pi.versionName + "\n");
			deviceInfo.append("versionCode：" + pi.versionCode + "\n");
		}
		Field[] fields = Build.class.getDeclaredFields();
		for ( Field field : fields)
		{
			field.setAccessible(true);
			deviceInfo.append(field.getName() + "：" + field.get(null) + "\n");
		}
		return deviceInfo.toString();
	}
}
