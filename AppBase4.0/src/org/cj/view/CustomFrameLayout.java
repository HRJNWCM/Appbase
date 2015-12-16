package org.cj.view;

import org.cj.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

public class CustomFrameLayout extends FrameLayout
{
	View	mErrorView;
	View	mEmptyView;
	View	view;

	public CustomFrameLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mErrorView = LayoutInflater.from(context)
		        .inflate(R.layout.view_error, null);
		mEmptyView = LayoutInflater.from(context)
		        .inflate(R.layout.view_empty, null);
	}

	@Override
	protected void onFinishInflate()
	{
		// TODO Auto-generated method stub
		super.onFinishInflate();
		onLoading();
	}

	public void onLoading()
	{
		for ( int i = 0; i < getChildCount(); i++)
			getChildAt(i).setVisibility(View.INVISIBLE);
	}

	public void onSuccess()
	{
		for ( int i = 0; i < getChildCount(); i++)
			getChildAt(i).setVisibility(View.VISIBLE);
		if (view != null)
		{
			ViewParent parent = view.getParent();
			if (parent != null) view.setVisibility(View.GONE);
		}
	}

	public void onException(ExceptionMode mode,
	        final OnExceptionTouchListener listener)
	{
		switch (mode)
		{
			default:
			case EMPTY:
				view = mEmptyView;
				break;
			case ERROR:
				view = mErrorView;
				break;
		}
		for ( int i = 0; i < getChildCount(); i++)
			getChildAt(i).setVisibility(View.INVISIBLE);
		ViewParent parent = view.getParent();
		if (parent == null) addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		view.setVisibility(View.VISIBLE);
		view.findViewById(R.id.textViewMessage)
		        .setOnTouchListener(new OnTouchListener()
		        {
			        @Override
			        public boolean onTouch(View v, MotionEvent event)
			        {
				        // TODO Auto-generated method stub
				        view.setVisibility(View.GONE);
				        if (listener != null) listener.onTouch();
				        return false;
			        }
		        });
	}

	public enum ExceptionMode
	{
		ERROR, EMPTY
	}

	public interface OnExceptionTouchListener
	{
		void onTouch();
	}
}
