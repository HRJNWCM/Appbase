package org.cj.image;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.ImageView;

public final class RoundedTransformationBuilder
{
	//private final Resources mResources;
	private final DisplayMetrics	mDisplayMetrics;

	public RoundedTransformationBuilder()
	{
		mDisplayMetrics = Resources.getSystem().getDisplayMetrics();
	}

	public RoundedTransformationBuilder scaleType(ImageView.ScaleType scaleType)
	{
		return this;
	}

	/**
	 * set corner radius in px
	 */
	public RoundedTransformationBuilder cornerRadius(float radiusPx)
	{
		return this;
	}

	/**
	 * set corner radius in dip
	 */
	public RoundedTransformationBuilder cornerRadiusDp(float radiusDp)
	{
		TypedValue
		        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, radiusDp, mDisplayMetrics);
		return this;
	}

	/**
	 * set border width in px
	 */
	public RoundedTransformationBuilder borderWidth(float widthPx)
	{
		return this;
	}

	/**
	 * set border width in dip
	 */
	public RoundedTransformationBuilder borderWidthDp(float widthDp)
	{
		TypedValue
		        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthDp, mDisplayMetrics);
		return this;
	}

	/**
	 * set border color
	 */
	public RoundedTransformationBuilder borderColor(int color)
	{
		ColorStateList.valueOf(color);
		return this;
	}

	public RoundedTransformationBuilder borderColor(ColorStateList colors)
	{
		return this;
	}

	public RoundedTransformationBuilder oval(boolean oval)
	{
		return this;
	}
	//	public Transformation build1()
	//	{
	//		return new Transformation()
	//		{
	//			@Override
	//			public Bitmap transform(Bitmap source)
	//			{
	//				Bitmap transformed = RoundedDrawable.fromBitmap(source)
	//				        .setScaleType(mScaleType)
	//				        .setCornerRadius(mCornerRadius)
	//				        .setBorderWidth(mBorderWidth)
	//				        .setBorderColor(mBorderColor).setOval(mOval).toBitmap();
	//				if (!source.equals(transformed))
	//				{
	//					source.recycle();
	//				}
	//				return transformed;
	//			}
	//
	//			@Override
	//			public String key()
	//			{
	//				return "r:" + mCornerRadius + "b:" + mBorderWidth + "c:"
	//				        + mBorderColor + "o:" + mOval;
	//			}
	//		};
	//	}
}
