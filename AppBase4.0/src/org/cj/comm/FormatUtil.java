// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FormatUtil.java
package org.cj.comm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtil
{
	public static boolean isMobileNO(String mobiles)
	{
		Pattern p = Pattern
		        .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isChineseIdcard(String idcard)
	{
		Pattern p = Pattern.compile("\\d{15}|\\d{18}");
		Matcher m = p.matcher(idcard);
		return m.matches();
	}

	public static boolean isTecentQQ(String QQ)
	{
		Pattern p = Pattern.compile("[1-9][0-9]{4,}");
		Matcher m = p.matcher(QQ);
		return m.matches();
	}

	public static boolean hasVerifyEqual(String msg, String serverVerify)
	{
		boolean isEqual = false;
		int idx = msg.indexOf("�����֤��Ϊ[") + 7;
		String target = msg.substring(idx, idx + 6);
		isEqual = target.equals(serverVerify);
		return isEqual;
	}

	public static String getVerifyFromMsg(String msg, String pattern, int verlen)
	{
		if (pattern == null || pattern.equals("")) return "";
		if (verlen <= 0)
		{
			return "";
		} else
		{
			int idx = msg.indexOf(pattern) + pattern.length();
			String target = msg.substring(idx, idx + verlen);
			return target;
		}
	}

	/**
	 * 验证邮箱
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	//	public static boolean isEmail(String str)
	//	{
	//		String regex = "^([//w-//.]+)@((//[[0-9]{1,3}//.[0-9]{1,3}//.[0-9]{1,3}//.)|(([//w-]+//.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(//]?)$";
	//		return match(regex, str);
	//	}
	public static boolean isEmail(String email)
	{
		if (null == email || "".equals(email)) return false;
		//      Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配  
		String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";//复杂匹配  
		return match(regex, email);
	}

	/**
	 * 验证IP地址
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isIP(String str)
	{
		String num = "(25[0-5]|2[0-4]//d|[0-1]//d{2}|[1-9]?//d)";
		String regex = "^" + num + "//." + num + "//." + num + "//." + num
		        + "$";
		return match(regex, str);
	}

	/**
	 * 验证网址Url
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsUrl(String str)
	{
		String regex = "http(s)?://([//w-]+//.)+[//w-]+(/[//w- ./?%&=]*)?";
		return match(regex, str);
	}

	/**
	 * 验证电话号码
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsTelephone(String str)
	{
		String regex = "^(//d{3,4}-)?//d{6,8}$";
		return match(regex, str);
	}

	/**
	 * 验证输入密码条件(字符数字组合,位数min-max)
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsPassword(String str, int min, int max)
	{
		if (str == null || str.equals("")) return false;
		String regex = "[A-Za-z]+";
		//		String regex = "[\\da-zA-Z]+";
		String numregex = "[+-]?[1-9]+[0-9]*(\\.[0-9]+)?";//只包含数字
		//		String num = ".+?\\d.+?";
		return !match(regex, str) && !match(numregex, str);
	}

	public static boolean isPassword2(String str, int min, int max)
	{
		String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{" + min + "," + max
		        + "}$";
		return match(reg, str);
	}

	/**
	 * 验证输入密码长度 (6-18位)
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsPasswLength(String str, int min, int max)
	{
		String regex = "^//d{" + min + "," + max + "}$";
		return match(regex, str);
	}

	/**
	 * 验证输入邮政编号
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsPostalcode(String str)
	{
		String regex = "^//d{6}$";
		return match(regex, str);
	}

	/**
	 * 验证输入手机号码
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsHandset(String str)
	{
		String regex = "^[1]+[3,5]+//d{9}$";
		return match(regex, str);
	}

	/**
	 * 验证输入身份证号
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsIDcard(String str)
	{
		String regex = "^((1[1-5])|(2[1-3])|(3[1-7])|(4[1-6])|(5[0-4])|(6[1-5])|71|(8[12])|91)\\d{4}((19\\d{2}(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|(19\\d{2}(0[13578]|1[02])31)|(19\\d{2}02(0[1-9]|1\\d|2[0-8]))|(19([13579][26]|[2468][048]|0[48])0229))\\d{3}(\\d|X|x)?$";
		return match(regex, str);
	}

	/**
	 * 验证输入两位小数
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsDecimal(String str)
	{
		String regex = "^[0-9]+(.[0-9]{2})?$";
		return match(regex, str);
	}

	/**
	 * 验证输入一年的12个月
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsMonth(String str)
	{
		String regex = "^(0?[[1-9]|1[0-2])$";
		return match(regex, str);
	}

	/**
	 * 验证输入一个月的31天
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsDay(String str)
	{
		String regex = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
		return match(regex, str);
	}

	/**
	 * 验证日期时间
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合网址格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isDate(String str)
	{
		//严格验证时间格式的(匹配[2002-01-31], [1997-04-30], [2004-01-01])不匹配([2002-01-32], [2003-02-29], [04-01-01])   
		//	        String regex = "^((((19|20)(([02468][048])|([13579][26]))-02-29))|((20[0-9][0-9])|(19[0-9][0-9]))-((((0[1-9])|(1[0-2]))-((0[1-9])|(1//d)|(2[0-8])))|((((0[13578])|(1[02]))-31)|(((01,3-9])|(1[0-2]))-(29|30)))))$";  
		//没加时间验证的YYYY-MM-DD  
		//	        String regex = "^((((1[6-9]|[2-9]//d)//d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]//d|3[01]))|(((1[6-9]|[2-9]//d)//d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]//d|30))|(((1[6-9]|[2-9]//d)//d{2})-0?2-(0?[1-9]|1//d|2[0-8]))|(((1[6-9]|[2-9]//d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$";  
		//加了时间验证的YYYY-MM-DD 00:00:00  
		String regex = "^((((1[6-9]|[2-9]//d)//d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]//d|3[01]))|(((1[6-9]|[2-9]//d)//d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]//d|30))|(((1[6-9]|[2-9]//d)//d{2})-0?2-(0?[1-9]|1//d|2[0-8]))|(((1[6-9]|[2-9]//d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?//d):[0-5]?//d:[0-5]?//d$";
		return match(regex, str);
	}

	/**
	 * 验证数字输入
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsNumber(String str)
	{
		String regex = "^[0-9]*$";
		return match(regex, str);
	}

	/**
	 * 验证非零的正整数
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsIntNumber(String str)
	{
		String regex = "^//+?[1-9][0-9]*$";
		return match(regex, str);
	}

	/**
	 * 验证大写字母
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsUpChar(String str)
	{
		String regex = "^[A-Z]+$";
		return match(regex, str);
	}

	/**
	 * 验证小写字母
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsLowChar(String str)
	{
		String regex = "^[a-z]+$";
		return match(regex, str);
	}

	/**
	 * 验证验证输入字母
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsLetter(String str)
	{
		String regex = "^[A-Za-z]+$";
		return match(regex, str);
	}

	/**
	 * 验证验证输入汉字
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsChinese(String str)
	{
		String regex = "^[/u4e00-/u9fa5],{0,}$";
		return match(regex, str);
	}

	/**
	 * 验证验证输入字符串
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsLength(String str)
	{
		String regex = "^.{8,}$";
		return match(regex, str);
	}

	/**
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	private static boolean match(String regex, String str)
	{
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
}
