// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AbstractProtocol.java
package org.cj.http.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;

import org.cj.MyApplication;
import org.cj.androidexception.DecodeMessageException;
import org.cj.androidexception.EncodeMessageException;
import org.cj.androidexception.ServerErrorException;
import org.cj.androidexception.SessionTimeoutException;
import org.cj.http.Const;
import org.cj.logUtil.LogUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

// Referenced classes of package com.h.util.http.protocol:
//			IProtocol
public abstract class AbstractProtocol implements _IProtocol
{
	LogUtil	logUtil;

	public AbstractProtocol()
	{
		logUtil = MyApplication.get().getLogUtil();
	}

	public AbstractProtocol(String content)
	{
		logUtil = MyApplication.get().getLogUtil();
	}

	@Override
	public byte[] getRequestXMLData() throws UnsupportedEncodingException,
	        EncodeMessageException
	{
		return null;
	}

	@Override
	public String getHeaders()
	{
		return null;
	}

	@Override
	public byte[] getUploadFileData()
	{
		return null;
	}

	@Override
	public String getRequestUrl()
	{
		return null;
	}

	@Override
	public String getRequestXML()
	{
		return null;
	}

	@Override
	public void parseResponseData(String str)
	        throws ParserConfigurationException, SAXException, IOException,
	        DecodeMessageException, SessionTimeoutException,
	        ServerErrorException
	{
	}

	@Override
	public void parseResponseData(byte data[])
	        throws ParserConfigurationException, SAXException, IOException,
	        DecodeMessageException, SessionTimeoutException,
	        ServerErrorException
	{
	}

	@Override
	public void parseResponseData(InputStream inputstream)
	        throws ParserConfigurationException, SAXException, IOException,
	        DecodeMessageException, SessionTimeoutException,
	        ServerErrorException
	{
	}

	@Override
	public void parseResponseError(Element element)
	        throws DecodeMessageException, ServerErrorException
	{
	}

	@Override
	public void parseResponseXML(Element element) throws DecodeMessageException
	{
	}

	@Override
	public void parseResponseDataByXmlPull(byte[] b)
	        throws XmlPullParserException, DecodeMessageException,
	        SessionTimeoutException, ServerErrorException,
	        UnsupportedEncodingException, IOException
	{
	}

	@Override
	public void parseResponseDataByXmlPull(InputStream inputstream)
	        throws XmlPullParserException, DecodeMessageException,
	        SessionTimeoutException, ServerErrorException, IOException
	{
	}

	@Override
	public void parseResponseDataByXmlPull(String str)
	        throws XmlPullParserException, DecodeMessageException,
	        SessionTimeoutException, ServerErrorException
	{
	}

	@SuppressWarnings("finally")
	@Override
	public void parseResponseTimeout(Element element)
	        throws SessionTimeoutException
	{
		String msg = "";
		try
		{
			NodeList nodeList = element.getElementsByTagName("des");
			if (nodeList.getLength() > 0)
			{
				Node node = nodeList.item(0);
				msg = node.getTextContent();
			}
		} catch (Exception e)
		{
		} finally
		{
			throw new SessionTimeoutException(msg.equals("") ? "session time out,try relogin" : msg, Const.MessageStatus.TIMEOUT);
		}
	}

	@Override
	public void parseResponseXMLByXmlPull(XmlPullParser parser)
	{
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null) return false;
		assert o instanceof AbstractProtocol : false;
		AbstractProtocol abstractProtocol = (AbstractProtocol) o;
		if (abstractProtocol.getClass().getName()
		        .equals(this.getClass().getName())) return true;
		return false;
	}
}
