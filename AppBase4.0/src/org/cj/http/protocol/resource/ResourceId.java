package org.cj.http.protocol.resource;

import java.lang.reflect.Field;

import android.content.Context;

/**
 * 获取资源id
 * 
 * @author HR
 * 
 *         2015-1-6 上午11:30:06
 */
public class ResourceId
{
	public static int getResourceIdByIdentifier(Context context,
	        String resource, String paramString)
	{
		return context.getResources()
		        .getIdentifier(paramString, resource, context.getPackageName());
	}

	public static int getResourceIdByReflect(Context context, String resource,
	        String fileName)
	{
		int id = 0;
		Class<?> c = null;
		try
		{
			c = Class.forName(context.getPackageName() + ".R$" + resource);
			Field field = c.getDeclaredField(fileName);
			if (field != null) id = field.getInt(c);
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return id;
	}
}
