package org.cj.service;

import org.cj.MyApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RestartReceiver extends BroadcastReceiver
{
	public static final String	ACTION	= "org.cj.";

	@Override
	public void onReceive(Context arg0, Intent arg1)
	{
		// TODO Auto-generated method stub
		MyApplication.get().getLogUtil().d(arg1.getAction());
		Intent intent = arg0.getPackageManager()
		        .getLaunchIntentForPackage(arg0.getPackageName());
		arg0.startActivity(intent);
	}
}
