package org.cj.http;

import java.util.ArrayList;

import org.cj.bean._AbstractObject;

/**
 * protocol状态
 * 
 * @author HR
 * 
 *         2014-8-5 上午10:19:11
 */
public class _Status extends _AbstractObject
{
	private static final long	     serialVersionUID	= 1L;
	public static int	             ERROR	          = 500;
	public static int	             SUCCESS	      = 0;
	public static int	             TIMEOUT	      = 10;
	public static String	         DES	          = "des";
	public static String	         RECORD	          = "rc";
	public static ArrayList<Integer>	STATUS	      = new ArrayList<Integer>();

	@Deprecated
	public static void initStatus(int error, int success, int timeout,
	        int... arr)
	{
		ERROR = error;
		SUCCESS = success;
		TIMEOUT = timeout;
		for ( int i = 0; i < arr.length; i++)
			STATUS.add(arr[i]);
	}

	public static void initStatus(String des, String recode, int error,
	        int success, int timeout, int... arr)
	{
		initStatus(error, success, timeout, arr);
		DES = des;
		RECORD = recode;
	}
}
