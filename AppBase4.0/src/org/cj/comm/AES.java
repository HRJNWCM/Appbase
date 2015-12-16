// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AES.java

package org.cj.comm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES
{

	public static String encrypt(String seed, String cleartext)
			throws Exception
	{
		byte rawKey[] = getRawKey(seed.getBytes());
		byte result[] = encrypt(rawKey, cleartext.getBytes());
		return toHex(result);
	}

	public static String decrypt(String seed, String encrypted)
			throws Exception
	{
		byte rawKey[] = getRawKey(seed.getBytes());
		byte enc[] = toByte(encrypted);
		byte result[] = decrypt(rawKey, enc);
		return new String(result);
	}

	private static byte[] getRawKey(byte seed[]) throws Exception
	{
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		kgen.init(128, sr);
		SecretKey skey = kgen.generateKey();
		byte raw[] = skey.getEncoded();
		return raw;
	}

	private static byte[] encrypt(byte raw[], byte clear[]) throws Exception
	{
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(1, skeySpec);
		byte encrypted[] = cipher.doFinal(clear);
		return encrypted;
	}

	private static byte[] decrypt(byte raw[], byte encrypted[])
			throws Exception
	{
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(2, skeySpec);
		byte decrypted[] = cipher.doFinal(encrypted);
		return decrypted;
	}

	public static String toHex(String txt)
	{
		return toHex(txt.getBytes());
	}

	public static String fromHex(String hex)
	{
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString)
	{
		int len = hexString.length() / 2;
		byte result[] = new byte[len];
		for ( int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();

		return result;
	}

	public static String toHex(byte buf[])
	{
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for ( int i = 0; i < buf.length; i++)
			appendHex(result, buf[i]);

		return result.toString();
	}

	public static void EncryptFile(String pwd, File fileIn, File fileOut)
			throws Exception
	{
		try
		{
			FileInputStream fis = new FileInputStream(fileIn);
			byte bytIn[] = readbyte(fis);
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(pwd.getBytes()));
			SecretKey skey = kgen.generateKey();
			byte raw[] = skey.getEncoded();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(1, skeySpec);
			byte bytOut[] = cipher.doFinal(bytIn);
			FileOutputStream fos = new FileOutputStream(fileOut);
			InputStream sbs = new ByteArrayInputStream(bytOut);
			fos.write(readbyte(sbs));
			fos.close();
			fis.close();
		} catch (Exception e)
		{
			throw new Exception(e.getMessage());
		}
	}

	public static File DecryptFile(String pwd, File fileIn, File fileOut)
			throws Exception
	{
		try
		{
			FileInputStream fis = new FileInputStream(fileIn);
			byte bytIn[] = readbyte(fis);
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(pwd.getBytes()));
			SecretKey skey = kgen.generateKey();
			byte raw[] = skey.getEncoded();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(2, skeySpec);
			byte bytOut[] = cipher.doFinal(bytIn);
			FileOutputStream fos = new FileOutputStream(fileOut);
			InputStream sbs = new ByteArrayInputStream(bytOut);
			fos.write(readbyte(sbs));
			fos.close();
			fis.close();
		} catch (Exception e)
		{
			throw new Exception(e.getMessage());
		}
		return fileOut;
	}

	protected static byte[] readbyte(InputStream stream)
	{
		ByteArrayOutputStream baos;
		baos = new ByteArrayOutputStream(8196);
		byte buffer[] = new byte[1024];
		try
		{
			for ( int length = 0; (length = stream.read(buffer)) > 0;)
				baos.write(buffer, 0, length);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return baos.toByteArray();
	}

	private static void appendHex(StringBuffer sb, byte b)
	{
		sb.append("0123456789ABCDEF".charAt(b >> 4 & 0xf)).append(
				"0123456789ABCDEF".charAt(b & 0xf));
	}
}
