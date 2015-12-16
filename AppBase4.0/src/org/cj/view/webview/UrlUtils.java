/*
 * Zirco Browser for Android
 * 
 * Copyright (C) 2010 J. Devauchelle and contributors.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package org.cj.view.webview;

import android.content.Context;

/**
 * Url management utils.
 */
public class UrlUtils
{

	/**
	 * Check if a string is an url. For now, just consider that if a string
	 * contains a dot, it is an url.
	 * 
	 * @param url
	 *            The url to check.
	 * @return True if the string is an url.
	 */
	public static boolean isUrl(String url)
	{
		return url.contains(".");
	}

	/**
	 * Check en url. Add http:// before if missing.
	 * 
	 * @param url
	 *            The url to check.
	 * @return The modified url if necessary.
	 */
	public static String checkUrl(String url)
	{
		if ((url != null) && (url.length() > 0))
		{

			if ((!url.startsWith("http://")) && (!url.startsWith("https://"))
					&& (!url.startsWith("file://")))
			{

				url = "http://" + url;

			}
		}

		return url;
	}

	/**
	 * Check if there is an item in the mobile view url list that match a given
	 * url.
	 * 
	 * @param context
	 *            The current context.
	 * @param url
	 *            The url to check.
	 * @return True if an item in the list match the given url.
	 */
	public static boolean checkInMobileViewUrlList(Context context, String url)
	{

		return false;
	}

}
