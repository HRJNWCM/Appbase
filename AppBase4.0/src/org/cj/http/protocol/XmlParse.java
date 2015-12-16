// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   XmlParse.java
package org.cj.http.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cj.androidexception.DecodeMessageException;
import org.cj.androidexception.ServerErrorException;
import org.cj.androidexception.SessionTimeoutException;
import org.cj.http.Const;
import org.cj.http._Status;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public abstract class XmlParse extends AbstractProtocol
{
	public int	  recode;
	public String	error;
	public int	  index;

	public XmlParse()
	{
		recode = -1;
		error = "";
	}

	public XmlParse(String content) throws DecodeMessageException,
	        SessionTimeoutException, ServerErrorException,
	        ParserConfigurationException, SAXException, IOException
	{
		this();
		parseResponseData(content);
	}

	@Override
	public void parseResponseData(String str)
	        throws ParserConfigurationException, SAXException, IOException,
	        DecodeMessageException, SessionTimeoutException,
	        ServerErrorException
	{
		parseResponseData(str.getBytes("utf-8"));
	}

	@Override
	public void parseResponseData(byte data[])
	        throws ParserConfigurationException, SAXException, IOException,
	        DecodeMessageException, SessionTimeoutException,
	        ServerErrorException
	{
		parseResponseData(byteToInputStream(data));
	}

	InputStream byteToInputStream(byte data[])
	        throws UnsupportedEncodingException
	{
		String xmlString = new String(data, "utf-8");
		logUtil.i(getClass().getSimpleName());
		logUtil.i((new StringBuilder("response--length:")).append(data.length)
		        .toString());
		logUtil.i((new StringBuilder("response--content:")).append(xmlString)
		        .toString());
		return new ByteArrayInputStream(data);
	}

	@Override
	public void parseResponseData(InputStream inputstream)
	        throws ParserConfigurationException, SAXException, IOException,
	        DecodeMessageException, SessionTimeoutException,
	        ServerErrorException
	{
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(inputstream);
			Element element = document.getDocumentElement();
			String result = element.getAttribute(_Status.RECORD);
			if (result != null && !result.equals("")) recode = Integer
			        .parseInt(result);
			if (recode == _Status.ERROR) parseResponseError(element);
			else if (recode == _Status.TIMEOUT) parseResponseTimeout(element);
			else if (recode == _Status.SUCCESS
			        || _Status.STATUS.indexOf(recode) != -1) parseResponseXML(element);
			else
				throw new DecodeMessageException((new StringBuilder("decode error"))
				        .toString());
		} catch (DecodeMessageException e)
		{
			throw new DecodeMessageException((new StringBuilder("decode error:"))
			        .append(e.getMessage()).toString());
		}
	}

	@Override
	public void parseResponseError(Element element)
	        throws DecodeMessageException, ServerErrorException
	{
		try
		{
			NodeList nodeList = element.getElementsByTagName(_Status.DES);
			Node node = nodeList.item(0);
			error = node.getTextContent();
		} catch (Exception e)
		{
			throw new DecodeMessageException(e.getMessage());
		}
		throw new ServerErrorException(error, Const.MessageStatus.ERROR);
	}

	public String getDataFromCData1(String msg)
	{
		StringBuffer buffer = new StringBuffer();
		String resx1 = "(?<=<!\\[CDATA\\[).*(?=\\]\\]>)";
		Pattern p = Pattern.compile(resx1);
		for ( Matcher matcher = p.matcher(msg); matcher.find(); buffer
		        .append(matcher.group()))
			;
		return buffer.toString();
	}

	@Override
	public void parseResponseDataByXmlPull(byte[] b)
	        throws XmlPullParserException, DecodeMessageException,
	        SessionTimeoutException, ServerErrorException,
	        UnsupportedEncodingException, IOException
	{
		parseResponseDataByXmlPull(byteToInputStream(b));
	}

	@Override
	public void parseResponseDataByXmlPull(InputStream inputstream)
	        throws XmlPullParserException, DecodeMessageException,
	        SessionTimeoutException, ServerErrorException,
	        XmlPullParserException, IOException
	{
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		parser.setInput(inputstream, "UTF-8");
		parser.next();
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT)
		{
			switch (eventType)
			{
			//解析开始
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					break;
				case XmlPullParser.END_TAG:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
			}
			eventType = parser.next();
		}
	}

	@Override
	public void parseResponseDataByXmlPull(String str)
	        throws XmlPullParserException, DecodeMessageException,
	        SessionTimeoutException, ServerErrorException
	{
	}
}
