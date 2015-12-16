package org.cj.comm;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 公共工具类
 * 
 * @author Eway
 * 
 */
@SuppressLint("SimpleDateFormat")
public final class CommonUtils
{
	/**
	 * 检测SDcard是否存在
	 * 
	 * @return true:存在、false:不存在
	 */
	public static boolean isSdcardExist()
	{
		if (android.os.Environment.getExternalStorageState()
		        .equals(android.os.Environment.MEDIA_MOUNTED))
		{
			return true;
		}
		return false;
	}

	public static String getNumeric(Context ctx)
	{
		TelephonyManager tm = (TelephonyManager) ctx
		        .getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSimOperator();
	}

	/**
	 * 发短信
	 * 
	 * @param number
	 * @param message
	 * @return
	 */
	public static boolean sendSMSMsg(String number, String message)
	{
		boolean flag = true;
		try
		{
			// 应判断下短信的长度
			SmsManager smsMgr = SmsManager.getDefault();
			// 按照短信，最大容量拆分成多条短信
			List<String> divideContents = smsMgr.divideMessage(message);
			for ( String text : divideContents)
			{
				smsMgr.sendTextMessage(number, null, text, null, null);
			}
		} catch (Exception e)
		{
			flag = false;
		}
		return flag;
	}

	/**
	 * 判断App是否在前台运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isRunningForeground(Context context)
	{
		ActivityManager am = (ActivityManager) context
		        .getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		if (!TextUtils.isEmpty(currentPackageName)
		        && currentPackageName.equals(context.getPackageName()))
		{
			return true;
		}
		return false;
	}

	/**
	 * 判断ac是否在前台
	 * 
	 * @param context
	 * @param className
	 * @return
	 */
	public static boolean isActivityForeground(Context context, String className)
	{
		if (context == null || TextUtils.isEmpty(className))
		{
			return false;
		}
		ActivityManager am = (ActivityManager) context
		        .getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0)
		{
			ComponentName cpn = list.get(0).topActivity;
			if (className.equals(cpn.getClassName())) return true;
		}
		return false;
	}

	/**
	 * 判断当前App是否后台运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isBackground(Context context)
	{
		ActivityManager activityManager = (ActivityManager) context
		        .getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
		        .getRunningAppProcesses();
		for ( RunningAppProcessInfo appProcess : appProcesses)
		{
			if (appProcess.processName.equals(context.getPackageName()))
			{
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND)
				{
					return true;
				} else
				{
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 判断应用是否运行
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAppRuning(Context context, String packageName)
	{
		boolean flag = false;
		ActivityManager activityManager = (ActivityManager) context
		        .getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> TaskList = activityManager.getRunningTasks(100);
		for ( int i = 0; i < TaskList.size(); i++)
		{
			RunningTaskInfo rti = TaskList.get(i);
			if (rti.topActivity.getPackageName().equals(packageName))
			{
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 判断SIM卡是否正确安装
	 */
	public static boolean isNormalSIMCard(Context context)
	{
		TelephonyManager tm = (TelephonyManager) context
		        .getSystemService(Context.TELEPHONY_SERVICE);// 取得相关系统服务
		switch (tm.getSimState())
		{ // getSimState()取得sim的状态 有下面6中状态
			default:
				// 无卡
			case TelephonyManager.SIM_STATE_ABSENT:
				return false;
				// 未知状态
			case TelephonyManager.SIM_STATE_UNKNOWN:
				return false;
				// 需要NetworkPIN解锁
			case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
				return false;
				// 需要PIN解锁
			case TelephonyManager.SIM_STATE_PIN_REQUIRED:
				return false;
				// 需要PUK解锁
			case TelephonyManager.SIM_STATE_PUK_REQUIRED:
				return false;
				// 良好
			case TelephonyManager.SIM_STATE_READY:
				return true;
		}
	}

	/**
	 * 获取sim卡号
	 * 
	 * @param context
	 * @return
	 */
	public static String getImsiID(Context context)
	{
		TelephonyManager tm = (TelephonyManager) context
		        .getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSubscriberId();
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @param format
	 *            时间格式
	 * @return
	 */
	public static String getCurrentTime(String format)
	{
		SimpleDateFormat df = new SimpleDateFormat(format);// 设置日期格式
		return df.format(new Date());// new Date()为获取当前系统时间
	}
	static String[]	weekDays	= { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
	        "星期六"	         };

	/**
	 * 将短时间格式字符串转换为时间 yyyy年MM月dd日
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	public static Date strToDate(String strDate, String format)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	public static Date StrToDate(String str, String format)
	{
		SimpleDateFormat f = new SimpleDateFormat(format);
		Date date = null;
		try
		{
			date = f.parse(str);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 字符串转换成星期
	 * 
	 * @param date
	 * @return date
	 */
	public static String getWeekOfDate(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int weekday = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (weekday < 0)
		{
			weekday = 0;
		}
		return weekDays[weekday];
	}

	/***
	 * 1则代表的是对年份操作 2是对月份操作 3是对星期操作 5是对日期操作 11是对小时操作 12是对分钟操作 13是对秒操作，14是对毫秒操作
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	static Date addYears(Date date, int amount)
	{
		return add(date, 1, amount);
	}

	static Date addMonths(Date date, int amount)
	{
		return add(date, 2, amount);
	}

	static Date addWeeks(Date date, int amount)
	{
		return add(date, 3, amount);
	}

	static Date addDays(Date date, int amount)
	{
		return add(date, 5, amount);
	}

	static Date addHours(Date date, int amount)
	{
		return add(date, 11, amount);
	}

	static Date addMinutes(Date date, int amount)
	{
		return add(date, 12, amount);
	}

	static Date addSeconds(Date date, int amount)
	{
		return add(date, 13, amount);
	}

	static Date addMilliseconds(Date date, int amount)
	{
		return add(date, 14, amount);
	}

	static Date add(Date date, int calendarField, int amount)
	{
		if (date == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		} else
		{
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(calendarField, amount);
			return c.getTime();
		}
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @param pattern
	 *            str 的格式
	 * @param desp
	 *            得到的格式
	 * @return yyyy年MM月dd日
	 */
	public static String formateDate(String str, String pattern, String desp)
	{
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;
		try
		{
			date = format.parse(str);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return str;
		}
		SimpleDateFormat format2 = new SimpleDateFormat(desp);
		String strDate = format2.format(date);
		return strDate;
	}

	/**
	 * @param con
	 *            启动应用
	 */
	public static void startActivity(Context context, String packageName)
	{
		PackageManager pm = context.getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(packageName);
		if (intent != null) context.startActivity(intent);
	}

	public static String getStandardPhone(String phoneNum)
	{
		String newNum = null;
		newNum = phoneNum.replaceAll(" ", "");
		if (newNum.startsWith("+86"))
		{
			newNum = newNum.substring(3);
		}
		if (newNum.startsWith("17951"))
		{
			newNum = newNum.substring(5);
		}
		return newNum;
	}

	/**
	 * 打开设置网络界面
	 */
	public static void setNetworkMethod(final Activity context)
	{
		// 提示对话框
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("网络设置提示")
		        .setMessage("网络连接不可用，是否进行设置?")
		        .setPositiveButton("设置", new DialogInterface.OnClickListener()
		        {
			        @SuppressLint("NewApi")
			        @Override
			        public void onClick(DialogInterface dialog, int which)
			        {
				        // TODO Auto-generated method stub
				        Intent intent = null;
				        // 判断手机系统的版即API大于10 就是3.0或以上版
				        if (android.os.Build.VERSION.SDK_INT > 10)
				        {
					        intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				        } else
				        {
					        intent = new Intent();
					        ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
					        intent.setComponent(component);
					        intent.setAction("android.intent.action.VIEW");
				        }
				        context.startActivity(intent);
			        }
		        })
		        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
		        {
			        @Override
			        public void onClick(DialogInterface dialog, int which)
			        {
				        // TODO Auto-generated method stub
				        dialog.dismiss();
				        context.finish();
			        }
		        }).show();
	}
}
