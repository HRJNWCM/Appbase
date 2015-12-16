package org.cj.comm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;

public class ObjectUtil
{
	/**
	 * 对象转换字符串
	 * 
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static String object2String(Object object) throws IOException
	{
		if (object == null) throw new NullPointerException("object can not be null");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		String result = new String(Base64.encodeBase64(baos.toByteArray()));
		result = DES.encryptDES(result, "43215768");
		return result;
	}

	/**
	 * string转换对象
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static Object string2Object(String str) throws Exception
	{
		if (str == null || str.equals("")) throw new NullPointerException("str can not be null ");
		str = DES.decryptDES(str, "43215768");
		byte[] data = Base64.decodeBase64(str.getBytes());
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object object = ois.readObject();
		return object;
	}
}
