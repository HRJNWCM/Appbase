// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LogUtil.java
package org.cj.logUtil;

import java.io.IOException;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.cj.config._Config;

import android.util.Log;

public class LogUtil
{
	private static class TextFormatter extends Formatter
	{
		public String format(LogRecord record)
		{
			return (new StringBuilder("[")).append(LogUtil.getCurrentTime())
			        .append("]").append(" ").append(record.getMessage())
			        .append("\r\n").toString();
		}

		public TextFormatter()
		{
		}
	}
	private static boolean	   LOGFLAG;
	public static String	   TAG;
	private String	           mClassName;
	private static LogUtil	   HLOG;
	public static final Logger	LOGGER	= Logger.getLogger("App");
	private static String	   APPLOG;

	public static void initAppLogger(String dir, String appName)
	{
		LOGFLAG = _Config.get().isPrint();
		TAG = "[" + appName + "]";
		APPLOG = (new StringBuilder(String.valueOf(dir))).append("app.log")
		        .toString();
		try
		{
			FileHandler appHandler = new FileHandler(APPLOG, 0xfa000, 1, true);
			appHandler.setFormatter(new TextFormatter());
			LOGGER.setLevel(Level.ALL);
			LOGGER.addHandler(appHandler);
		} catch (IOException ioexception)
		{
			ioexception.printStackTrace();
		}
	}

	public void setPrint(boolean flag)
	{
		LOGFLAG = flag;
	}

	private LogUtil(String name)
	{
		mClassName = name;
	}

	public static LogUtil HLog()
	{
		return HLOG != null ? HLOG : (HLOG = new LogUtil("@hr "));
	}

	private String getFunctionName()
	{
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null)
		{
			return null;
		}
		for ( StackTraceElement st : sts)
		{
			if (st.isNativeMethod())
			{
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName()))
			{
				continue;
			}
			if (st.getClassName().equals(this.getClass().getName()))
			{
				continue;
			}
			return mClassName + "[ " + Thread.currentThread().getName() + ": "
			        + st.getFileName() + ":" + st.getLineNumber() + " "
			        + st.getMethodName() + " ]";
		}
		return null;
	}

	public void i(Object str)
	{
		if (LOGFLAG && str != null) writeAndPrintLog(str.toString(), false);
	}

	public void d(Object str)
	{
		if (LOGFLAG && str != null) writeAndPrintLog(str.toString(), false);
	}

	public void v(Object str)
	{
		if (LOGFLAG && str != null) writeAndPrintLog(str.toString(), false);
	}

	public void w(Object str)
	{
		if (str != null) writeAndPrintLog(str.toString(), true);
	}

	public void w(Throwable e)
	{
		if (e != null) writeAndPrintError(e);
	}

	public void e(Object str)
	{
		if (LOGFLAG && str != null) writeAndPrintLog(str.toString(), true);
	}

	public void e(Exception ex)
	{
		if (LOGFLAG && ex != null) writeAndPrintError(ex);
	}

	public void e(String log, Throwable tr)
	{
		if (LOGFLAG && tr != null && log != null)
		{
			String line = getFunctionName();
			Log.e(TAG, (new StringBuilder("{Thread:"))
			        .append(Thread.currentThread().getName()).append("}")
			        .append("[").append(mClassName).append(line).append(":] ")
			        .append(log).append("\n").toString(), tr);
		}
	}

	void writeAndPrintError(Throwable e)
	{
		String name = getFunctionName();
		StackTraceElement stackTraceElements[] = e.getStackTrace();
		StringBuffer sb = new StringBuffer();
		sb.append(e.getMessage() + "\n");
		for ( int i = 0; i < stackTraceElements.length; i++)
			sb.append((new StringBuilder()).append(stackTraceElements[i])
			        .append("\n").toString());
		if (LOGGER != null) LOGGER.warning((new StringBuilder(String
		        .valueOf(TAG))).append(name).append("-").append(sb.toString())
		        .toString());
	}

	void writeAndPrintLog(String str, boolean waring)
	{
		String name = getFunctionName();
		if (name != null)
		{
			int maxLogSize = 1000;
			for ( int i = 0; i <= str.toString().length() / maxLogSize; i++)
			{
				int start = i * maxLogSize;
				int end = (i + 1) * maxLogSize;
				end = end <= str.toString().length() ? end : str.toString()
				        .length();
				if (LOGGER != null) if (waring) LOGGER.warning(TAG + name + "-"
				        + str.toString().substring(start, end));
				else
					LOGGER.info(TAG + name + "-"
					        + str.toString().substring(start, end));
			}
		} else
		{
			int maxLogSize = 1000;
			for ( int i = 0; i <= str.toString().length() / maxLogSize; i++)
			{
				int start = i * maxLogSize;
				int end = (i + 1) * maxLogSize;
				end = end <= str.toString().length() ? end : str.toString()
				        .length();
				if (LOGGER != null) if (waring) LOGGER.warning(TAG + "-"
				        + str.toString().substring(start, end));
				else
					LOGGER.info(TAG + "-"
					        + str.toString().substring(start, end));
			}
		}
	}

	private static String getCurrentTime()
	{
		try
		{
			return MessageFormat
			        .format("{0,date,yyyy-MM-dd HH:mm:ss}", new Object[] { new Date(System
			                .currentTimeMillis()) });
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return "";
	}
}
