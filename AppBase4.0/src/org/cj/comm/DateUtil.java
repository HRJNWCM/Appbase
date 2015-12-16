package org.cj.comm;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

public class DateUtil
{
	/**
	 * 
	 * @param time
	 *            字符串时间
	 * @param format
	 *            time 格式 ex yyyyMMdd HH:mm:ss
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime(String time, String format, String des)
	{
		try
		{
			long now = new Date().getTime();
			Date d = CommonUtils.strToDate(time, format);
			long t = d.getTime();
			long seconds = (now - t) / 1000;
			long minutes = seconds / 60;
			long hours = minutes / 60;
			seconds = seconds - minutes * 60;
			minutes = minutes - hours * 60;
			if (hours > 0 && hours < 24) return String.valueOf(hours + "小时前");
			else if (hours >= 24) return new SimpleDateFormat(des).format(d);
			if (minutes > 0) return String.valueOf(minutes + "分钟前");
			if (seconds > 0) return String.valueOf(seconds + "秒前");
			if (seconds == 0) return String.valueOf("1秒前");
		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
			return new SimpleDateFormat(des).format(new Date());
		}
		return new SimpleDateFormat(des).format(new Date());
	}
}
