package org.cj.comm;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加密
 * 
 * @author HR
 * 
 */
public class DES
{
	private static final String	DEFAULT_KEY	= "12345678";
	private static byte[]		iv			= { 1, 2, 3, 4, 5, 6, 7, 8 };

	public static String encryptDES(String encryptString)
	{
		return encryptDES(encryptString, null);
	}

	public static String encryptDES(String encryptString, String encryptKey)
	{
		String mykey = (encryptKey == null || encryptKey.equals("")) ? DEFAULT_KEY
				: encryptKey;
		byte[] encryptedData = null;
		try
		{
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(mykey.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			encryptedData = cipher.doFinal(encryptString.getBytes());
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Base64.encode(encryptedData);
	}

	public static String decryptDES(String decrypString)
	{
		return decryptDES(decrypString, DEFAULT_KEY);
	}

	public static String decryptDES(String decryptString, String decryptKey)
	{
		String mykey = (decryptKey == null || decryptKey.equals("")) ? DEFAULT_KEY
				: decryptKey;
		byte decryptedData[] = null;
		try
		{
			byte[] byteMi = Base64.decode(decryptString);
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(mykey.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			decryptedData = cipher.doFinal(byteMi);
		} catch (Exception e)
		{
		}

		return new String(decryptedData);
	}

}
