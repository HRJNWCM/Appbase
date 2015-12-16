package org.cj.view.ImageView;

import org.cj.view.AutoImageView;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * bitmap自适应imageview宽高
 * 
 * @author HR
 * 
 *         2014-2-7 下午10:55:30
 */
public class ColorFilterImageView extends AutoImageView
{
	public final float[]	    BT_SELECTED	    = new float[] { 1, 0, 0, 0, 99,
	        0, 1, 0, 0, 99, 0, 0, 1, 0, 99, 0, 0, 0, 1, 0 };
	public final float[]	    BT_NOT_SELECTED	= new float[] { 1, 0, 0, 0, 0,
	        0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };	 ;
	public final static float[]	BT_SELECTED1	= new float[] { 0.338f, 0.339f,
	        0.332f, 0, 0, 0.338f, 0.339f, 0.332f, 0, 0, 0.338f, 0.339f, 0.332f,
	        0, 0, 0, 0, 0, 1, 0	            };

	public ColorFilterImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO Auto-generated method stub
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				getDrawable()
				        .setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
				break;
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP:
				getDrawable()
				        .setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
				break;
		}
		return super.onTouchEvent(event);
	}
}
