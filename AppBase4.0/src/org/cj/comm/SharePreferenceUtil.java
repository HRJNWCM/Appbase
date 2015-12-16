package org.cj.comm;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class SharePreferenceUtil
{
	static SharePreferenceUtil	util	= new SharePreferenceUtil();
	Context	                   mContext;
	SharedPreferences	       sp;

	private SharePreferenceUtil()
	{
	}

	public static SharePreferenceUtil get()
	{
		if (util == null) util = new SharePreferenceUtil();
		return util;
	}

	public void init(Context context, String name)
	{
		mContext = context;
		sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public void set(String key, Object value) throws IOException
	{
		if (value instanceof Boolean) sp.edit()
		        .putBoolean(key, (Boolean) value).commit();
		else if (value instanceof String) sp.edit()
		        .putString(key, (String) value).commit();
		else if (value instanceof Integer) sp.edit()
		        .putInt(key, (Integer) value).commit();
		else if (value instanceof Float) sp.edit().putFloat(key, (Float) value)
		        .commit();
		else if (value instanceof Long) sp.edit().putLong(key, (Long) value)
		        .commit();
		else
			sp.edit().putString(key, ObjectUtil.object2String(value)).commit();
	}

	public Object get(String key, Object object) throws Exception
	{
		if (object instanceof Boolean) return getBoolean(key);
		if (object instanceof String) return getString(key);
		if (object instanceof Integer) return getInt(key);
		if (object instanceof Float) return getFloat(key);
		if (object instanceof Long) return getLong(key);
		if (object instanceof String) return getString(key);
		String str = getString(key);
		return TextUtils.isEmpty(str) ? object : ObjectUtil.string2Object(str);
	}

	public void clear()
	{
		sp.edit().clear().commit();
	}

	public boolean remove(String key)
	{
		return sp.edit().remove(key).commit();
	}

	public int getInt(String key)
	{
		return sp.getInt(key, 0);
	}

	public float getFloat(String key)
	{
		return sp.getFloat(key, 0);
	}

	public String getString(String key)
	{
		return sp.getString(key, "");
	}

	public boolean getBoolean(String key)
	{
		return sp.getBoolean(key, false);
	}

	public long getLong(String key)
	{
		return sp.getLong(key, 0);
	}
}
