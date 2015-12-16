/*
 * Zirco Browser for Android
 * 
 * Copyright (C) 2010 - 2012 J. Devauchelle and contributors.
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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.cj.BaseAppCompatFragmentActivity;

/**
 * Convenient extension of WebViewClient.
 */
public class CustomWebViewClient extends WebViewClient
{
	private BaseAppCompatFragmentActivity context;

	public CustomWebViewClient(BaseAppCompatFragmentActivity context)
	{
		super();
		this.context = context;
	}

	@Override
	public void onPageFinished(WebView view, String url)
	{
		((CustomWebView) view).notifyPageFinished();
		super.onPageFinished(view, url);
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon)
	{
		((CustomWebView) view).notifyPageStarted();
		super.onPageStarted(view, url, favicon);
	}
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url)
	{
		if (isExternalApplicationUrl(url))
		{
			onExternalApplicationUrl(url);
			return true;
		} else
		{
			((CustomWebView) view).resetLoadedUrl();
			return false;
		}
	}

	private boolean isExternalApplicationUrl(String url)
	{
		return url.startsWith("vnd.") || url.startsWith("rtsp://")
		        || url.startsWith("itms://") || url.startsWith("itpc://");
	}

	void onExternalApplicationUrl(String url)
	{
		try
		{
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		} catch (Exception e)
		{
			// Notify user that the vnd url cannot be viewed.
			new AlertDialog.Builder(context)
			        .setTitle("提示")
			        .setMessage("本程序暂不支持这个网址:" + url)
			        .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
			        {
				        public void onClick(DialogInterface dialog, int which)
				        {
				        }
			        }).setCancelable(true).create().show();
		}
	}
}
