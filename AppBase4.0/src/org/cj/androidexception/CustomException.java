package org.cj.androidexception;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import org.cj.logUtil.LogUtil;
import org.cj.sender.GMailSender;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.view.WindowManager;


/**
 * uncaughtException singleTone
 * 
 * @author Eway
 * 
 */
public class CustomException implements UncaughtExceptionHandler
{

	private Context							mContext;
	private Thread.UncaughtExceptionHandler	defaultExceptionHandler;
	private static CustomException			customException	= null;
	String									name;
	int										logo			= -1;

	private CustomException()
	{
	}

	public static CustomException getInstance()
	{
		if (customException == null)
		{
			customException = new CustomException();
		}
		return customException;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable exception)
	{
		// TODO Auto-generated method stub
		if (defaultExceptionHandler != null && !handleException(exception))
		{
			defaultExceptionHandler.uncaughtException(thread, exception);
		}
	}

	public void init(Context context, String AppName)
	{
		mContext = context;
		name = AppName;
		defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);

	}

	public void init(Context context, String AppName, int logo)
	{
		mContext = context;
		name = AppName;
		this.logo = logo;
		defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);

	}

	private boolean handleException(Throwable e)
	{
		if (e == null)
			return false;
		final StringBuffer sb = new StringBuffer();
		String msg = "ERROR ----------\n" + e.getLocalizedMessage();
		Throwable cause = e.getCause();
		sb.append(msg + "\n");
		StackTraceElement[] stackTraceElements = e.getStackTrace();
		StackTraceElement[] causeElements = cause.getStackTrace();
		try
		{
			for ( StackTraceElement element : causeElements)
			{
				sb.append(element.toString() + "\n");
			}

			for ( StackTraceElement element : stackTraceElements)
			{
				sb.append(element.toString() + "\n");
			}
			final String info = "DeviceInfo:\n" + collectDeviceInfo() + "\n"
					+ sb.toString();
			LogUtil.HLog().setPrint(true);
			LogUtil.HLog().w(info);
			new Thread(new Runnable() {

				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					Looper.prepare();
					exit();
					Looper.loop();
				}
			}).start();
			new Thread(new Runnable() {

				@Override
				public void run()
				{
					// TODO Auto-generated method stub
					Looper.prepare();

					try
					{
						new GMailSender().sendMail(name, info);
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Looper.loop();
				}
			}).start();
		} catch (Exception exception)
		{
			// TODO: handle exception
			android.os.Process.killProcess(android.os.Process.myPid());
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
		PackageInfo pi = pmManager.getPackageInfo(mContext.getPackageName(),
				PackageManager.GET_ACTIVITIES);
		if (pi != null)
		{
			deviceInfo.append("versionName:" + pi.versionName + "\n");
			deviceInfo.append("versionCode: " + pi.versionCode + "\n");
		}
		Field[] fields = Build.class.getDeclaredFields();
		for ( Field field : fields)
		{
			field.setAccessible(true);
			deviceInfo.append(field.getName() + ":" + field.get(null) + "\n");
		}
		return deviceInfo.toString();
	}

	protected void exit()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		if (logo != -1)
			builder.setIcon(mContext.getResources().getDrawable(logo));
		builder.setTitle(name)
				.setMessage("应用程序运行异常")
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1)
							{
								// TODO Auto-generated method stub
								arg0.dismiss();
								android.os.Process
										.killProcess(android.os.Process.myPid());
								// System.exit(10);
							}
						});
		AlertDialog dialog = builder.create();
		dialog.getWindow()
				.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);// 设定为系统级警告
		dialog.show();
	}
}
