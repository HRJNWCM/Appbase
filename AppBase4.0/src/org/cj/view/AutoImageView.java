package org.cj.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * bitmap自适应imageview宽高
 * 
 * @author HR
 * 
 *         2014-2-7 下午10:55:30
 */
public class AutoImageView extends ImageView
{
	private int	imageWidth;
	private int	imageHeight;
	Context	    context;
	int	        tarWidth;
	int	        tarHeight;

	public AutoImageView(Context context)
	{
		super(context);
		this.context = context;
		getImageBitmapSize();
	}

	public AutoImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		getImageBitmapSize();
	}

	public void setSize(int width, int height)
	{
		this.tarHeight = height;
		this.tarWidth = width;
	}

	private void getImageBitmapSize()
	{
		Drawable drawable = getDrawable();
		if (drawable == null) return;
		Bitmap image = ((BitmapDrawable) drawable).getBitmap();
		if (image == null) return;
		imageWidth = image.getWidth();
		imageHeight = image.getHeight();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		getImageBitmapSize();
		if (imageWidth != 0)
		{
			int width = MeasureSpec.getSize(widthMeasureSpec);
			int height = width * imageHeight / imageWidth;
			this.setMeasuredDimension(width, height);
		} else
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
