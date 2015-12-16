package org.cj.view;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AutoScrollerViewPager extends ViewPager implements Runnable
{
	long	              seconds	= 5;
	boolean	              auto	  = false;	       // 自动
	boolean	              manual	= false;	   // 手动
	PointF	              downP	  = new PointF();	// 按下点
	PointF	              curP	  = new PointF();	// 当前点
	OnSingleTouchListener	onSingleTouchListener;
	Handler	              handler;
	boolean	              pause	  = false;
	Thread	              thread;
	Context	              context;

	public AutoScrollerViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void startAutoScroll(long seconds, Handler handler)
	{
		if (seconds > 0) this.seconds = seconds;
		this.handler = handler;
		auto = true;
		if (thread == null)
		{
			thread = new Thread(this);
			thread.start();
		} else
			resume();
	}

	public void resume()
	{
		//		thread.start();
		//		thread.notify();
	}

	public void stopAutoScroll()
	{
		auto = false;
		if (thread != null) thread.interrupt();
	}

	public void pause()
	{
		synchronized (context)
		{
			//			try
			//			{
			//				thread.wait();
			//			} catch (InterruptedException e)
			//			{
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();
			//			}
		}
	}

	public void setManual(boolean flag)
	{
		manual = flag;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		Looper.prepare();
		while (auto && !thread.isInterrupted())
		{
			int time = 0;
			while (time < seconds * 10)
			{
				time++;
				try
				{
					Thread.sleep(100);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
				}
				if (manual) time = 0;
			}
			if (handler != null) handler.sendEmptyMessage(0);
		}
		thread = null;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0)
	{
		// TODO Auto-generated method stub
		// 当拦截触摸事件到达此位置的时候，返回true，
		// 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0)
	{
		// TODO Auto-generated method stub
		// 每次进行onTouch事件都记录当前的按下的坐标
		curP.x = arg0.getX();
		curP.y = arg0.getY();
		if (arg0.getAction() == MotionEvent.ACTION_DOWN)
		{
			setManual(true);
			// 记录按下时候的坐标
			// 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
			downP.x = arg0.getX();
			downP.y = arg0.getY();
			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		if (arg0.getAction() == MotionEvent.ACTION_MOVE)
		{
			// 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		if (arg0.getAction() == MotionEvent.ACTION_UP)
		{
			setManual(false);
			// 在up时判断是否按下和松手的坐标为一个点
			if (Math.abs(downP.x - curP.x) <= 10
			        && Math.abs(downP.y - curP.y) <= 10)
			{
				onSingleTouch();
				return true;
			}
		}
		return super.onTouchEvent(arg0);
	}

	/**
	 * 单击
	 */
	public void onSingleTouch()
	{
		if (onSingleTouchListener != null)
		{
			onSingleTouchListener.onSingleTouch();
		}
	}

	/**
	 * 创建点击事件接口
	 * 
	 * @author wanpg
	 * 
	 */
	public interface OnSingleTouchListener
	{
		public void onSingleTouch();
	}

	public void setOnSingleTouchListener(
	        OnSingleTouchListener onSingleTouchListener)
	{
		this.onSingleTouchListener = onSingleTouchListener;
	}
}
