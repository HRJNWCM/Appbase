package org.cj;

import org.cj.layout.SwipeBackLayout;

import android.os.Bundle;
import android.view.View;

/**
 * 可滑动Activity基类
 * 
 * @author HR
 * 
 *         2014-8-5 上午10:51:17
 */
public abstract class BaseSwipeBackFragmentActivity extends
        BaseFragmentActivity implements _SwipeBackActivityBase
{
	private SwipeBackActivityHelper	mHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTheme(R.style.Activity_swipe_theme);
		initView(savedInstanceState);
		mHelper = new SwipeBackActivityHelper(this);
		mHelper.onActivtyCreate();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate();
	}

	@Override
	public View findViewById(int id)
	{
		View v = super.findViewById(id);
		if (v == null && mHelper != null) return mHelper.findViewById(id);
		return v;
	}

	@Override
	public SwipeBackLayout getSwipeBackLayout()
	{
		return mHelper.getSwipeBackLayout();
	}

	@Override
	public void setSwipeBackEnable(boolean enable)
	{
		getSwipeBackLayout().setEnableGesture(enable);
	}

	@Override
	public void scrollToFinishActivity()
	{
		getSwipeBackLayout().scrollToFinishActivity();
	}
}
