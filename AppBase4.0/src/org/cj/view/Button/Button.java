package org.cj.view.Button;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class Button extends android.widget.Button
{
	public Button(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO Auto-generated method stub
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) getBackground()
				        .setAlpha(190);
				else
					setAlpha(0.75f);
				break;
			case MotionEvent.ACTION_UP:
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) getBackground()
				        .setAlpha(255);
				else
					setAlpha(1f);
				break;
		}
		return super.onTouchEvent(event);
	}
}
