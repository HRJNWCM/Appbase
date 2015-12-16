package org.cj.http.protocol;

import java.io.InputStream;

import android.content.Context;

public class ParserFactory
{
	static ParserFactory	instance;

	private ParserFactory()
	{
	}

	public static ParserFactory newInstance()
	{
		if (instance == null) instance = new ParserFactory();
		return instance;
	}

	public _IParser newParser(Mode mode)
	{
		switch (mode)
		{
			default:
			case DOC:
				return null;
			case JSON:
				return null;
			case RESOURCE:
				return new XmlResourceParserUtil();
			case XMLPULL:
				return null;
		}
	}

	class XmlResourceParserUtil implements _IParser
	{
		@Override
		public void parser(Context context, String name)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void parser(Context context, InputStream inputStream)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void parser(Context context, byte[] data)
		{
			// TODO Auto-generated method stub
		}
	}

	public enum Mode
	{
		RESOURCE, XMLPULL, DOC, JSON
	}
}
