// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   XmlRequest.java
package org.cj.http.protocol;

import java.io.UnsupportedEncodingException;

import org.cj.MyApplication;
import org.cj.androidexception.EncodeMessageException;
import org.cj.config._Config;

// Referenced classes of package com.h.util.http.protocol:
//			AbstractProtocol
public abstract class XmlRequest extends AbstractProtocol
{
	@Override
	public String getRequestUrl()
	{
		return _Config.get().getServer_Url() + getSubUrl();
	}

	protected abstract String getSubUrl();

	@Override
	public byte[] getRequestXMLData() throws UnsupportedEncodingException,
	        EncodeMessageException
	{
		byte data[] = null;
		try
		{
			String XmlData = getRequestXML();
			if (XmlData == null) return null;
			byte[] xml = XmlData.getBytes("utf-8");
			logUtil.d((new StringBuilder(String.valueOf(getClass()
			        .getSimpleName()))).append(":").append(XmlData).toString());
			byte[] file = getUploadFileData();
			if (file != null)
			{
				data = new byte[xml.length + file.length];
				System.arraycopy(xml, 0, data, 0, xml.length);
				System.arraycopy(file, 0, data, xml.length, file.length);
			} else
				data = xml;
		} catch (Exception e)
		{
			MyApplication.get().getLogUtil().e(e);
			throw new EncodeMessageException(e.getMessage());
		}
		return data;
	}
}
