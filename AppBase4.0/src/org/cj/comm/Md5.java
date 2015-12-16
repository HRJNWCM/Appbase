package org.cj.comm;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5的算法在RFC1321 中定在RFC 1321中，给出了Test suite用来你的实现是否正确MD5 ("")
 * =d41d8cd98f00b204e9800998ecf8427e MD5 ("a") =0cc175b9c0f1b6a831c399e269772661
 * MD5 ("abc") =900150983cd24fb0d6963f7d28e17f72 MD5 ("message digest")
 * =f96b697d7cb7938d525a2f31aaf161d0 MD5 ("abcdefghijklmnopqrstuvwxyz")
 * =c3fcd3d76192e4007dfb496cca67e13b
 */
public class Md5
{
	static char	hexDigits[]	= { // 用来将字节转换成 16 进制表示的字
	                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	        'a', 'b', 'c', 'd', 'e', 'f' };

	public static String getMD5(String val) throws NoSuchAlgorithmException
	{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(val.getBytes());
		byte[] m = md5.digest();// 加密
		char str[] = new char[16 * 2];
		int k = 0; // 表示转换结果中对应的字符位置
		for ( int i = 0; i < 16; i++)
		{
			byte byte0 = m[i]; //
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		String s = new String(str);
		return s.substring(8, 24);
	}

	public static String Md5_16(String plainText)
	{
		String result = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for ( int offset = 0; offset < b.length; offset++)
			{
				i = b[offset];
				if (i < 0) i += 256;
				if (i < 16) buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString().substring(8, 24);
			System.out.println("mdt 16bit: " + buf.toString().substring(8, 24));
			System.out.println("md5 32bit: " + buf.toString());
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public final static String MD5_32(String str)
	{
		MessageDigest messageDigest = null;
		try
		{
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e)
		{
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for ( int i = 0; i < byteArray.length; i++)
		{
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) md5StrBuff
			        .append("0")
			        .append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}
}
