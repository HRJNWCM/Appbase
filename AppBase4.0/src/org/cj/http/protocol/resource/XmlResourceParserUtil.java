package org.cj.http.protocol.resource;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;

/**
 * xml 资源解析
 * 
 * @author HR
 * 
 *         2014-6-21 下午8:23:28
 */
public abstract class XmlResourceParserUtil
{
	protected abstract void parserContent(Context context,
	        XmlResourceParser parser);

	public void parserFromResource(Context context, String resource,
	        String fileName)
	{
		XmlResourceParser parser = context
		        .getResources()
		        .getXml(ResourceId.getResourceIdByReflect(context, resource, fileName));
		try
		{
			int eventType = parser.next();
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				switch (eventType)
				{
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						parserContent(context, parser);
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
