package org.cj.message;

import android.os.Handler;

/**
 * 短信验证
 * 
 * @author HR
 * 
 *         2013-12-23 上午11:14:19
 */
public class MessageVerify
{
	Handler	                handler;
	public static final int	FINISH	= 2;
	public static final int	ONTICK	= 1;
	public static final int	CODE	= 0;
	long	                seconds;	 // 时间长度
	MyCount	                count;	     // 倒计时
	String	                patter;	 // 匹配字符串
	int	                    len;	     // 验证码长度
	static MessageVerify	verify;

	public MessageVerify()
	{
		// TODO Auto-generated constructor stub
	}

	public static MessageVerify get()
	{
		if (verify == null) verify = new MessageVerify();
		return verify;
	}

	/**
	 * 
	 * @param seconds
	 *            单位秒
	 */
	public void init(long seconds)
	{
		this.seconds = seconds > 0 ? seconds : 60;
		count = new MyCount(seconds * 1000, 1000);
	}

	@Deprecated
	public void init(Handler handler, long seconds)
	{
		this.seconds = seconds > 0 ? seconds : 60;
		this.handler = handler;
		count = new MyCount(seconds * 1000, 1000);
		count.setHandler(handler);
	}

	public void start()
	{
		if (count == null) throw new NullPointerException("MessageVerify should init first");
		count.reset();
		count.start();
	}

	public void setHandler(Handler handler)
	{
		if (count == null) throw new NullPointerException("MessageVerify should init first");
		this.handler = handler;
		count.setHandler(handler);
	}

	/**
	 * 当前页面关闭,后台倒计时
	 */
	public void onDismiss()
	{
		count.setHandler(null);
	}

	/**
	 * 销毁
	 */
	public void onDestory()
	{
		if (count != null) count.cancel();
		count = null;
	}

	/* 定义一个倒计时的内部类 */
	public class MyCount extends CountDownTimer
	{
		Handler	handler;

		public MyCount(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);
		}

		public void setHandler(Handler handler)
		{
			this.handler = handler;
		}

		@Override
		public void onFinish()
		{
			if (handler != null) handler.sendEmptyMessage(FINISH);
			isFinish = true;
		}

		@Override
		public void onTick(long millisUntilFinished)
		{
			if (handler != null) handler
			        .sendMessage(handler.obtainMessage(ONTICK, String
			                .valueOf(millisUntilFinished)));
		}
	}
}
