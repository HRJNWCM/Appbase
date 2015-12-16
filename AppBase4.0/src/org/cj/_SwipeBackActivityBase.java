package org.cj;

import org.cj.layout.SwipeBackLayout;

public interface _SwipeBackActivityBase
{
	/**
	 * @return the SwipeBackLayout associated with this activity.
	 */
	public abstract SwipeBackLayout getSwipeBackLayout();

	public abstract void setSwipeBackEnable(boolean enable);

	/**
	 * Scroll out contentView and finish the activity
	 */
	public abstract void scrollToFinishActivity();
}
