package org.cj.service;

import org.cj.sender.GMailSender;

import android.content.Intent;
import android.os.IBinder;

public class CrashService extends AbstraceService
{
	ExObj	exObj;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// TODO Auto-generated method stub
		try
		{
			exObj = (ExObj) intent.getSerializableExtra(ExObj.TAG);
		} catch (Exception e)
		{
			// TODO: handle exception
			stopSelf();
		}
		sendMsg();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	void sendMsg()
	{
		if (exObj != null) new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				try
				{
					new GMailSender().sendMail(exObj.getName(), exObj
					        .getError());
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally
				{
					stopSelf();
				}
			}
		}).start();
		else
			stopSelf();
	}
}
