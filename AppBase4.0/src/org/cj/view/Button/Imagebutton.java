package org.cj.view.Button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

public class Imagebutton extends ImageButton
{
	public Imagebutton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO Auto-generated method stub
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) setImageAlpha(190);
				else
					setAlpha(190);
				break;
			case MotionEvent.ACTION_UP:
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) setImageAlpha(255);
				else
					setAlpha(255);
				break;
		}
		return super.onTouchEvent(event);
	}
}
