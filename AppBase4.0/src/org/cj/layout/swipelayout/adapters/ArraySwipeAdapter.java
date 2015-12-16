package org.cj.layout.swipelayout.adapters;

import java.util.List;

import org.cj.layout.swipelayout.SwipeLayout;
import org.cj.layout.swipelayout.implments.SwipeItemMangerImpl;
import org.cj.layout.swipelayout.interfaces.SwipeAdapterInterface;
import org.cj.layout.swipelayout.interfaces.SwipeItemMangerInterface;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

@SuppressWarnings("rawtypes")
public abstract class ArraySwipeAdapter<T> extends ArrayAdapter implements
        SwipeItemMangerInterface, SwipeAdapterInterface
{
	private SwipeItemMangerImpl	mItemManger	= new SwipeItemMangerImpl(this);
	{
	}

	public ArraySwipeAdapter(Context context, int resource)
	{
		super(context, resource);
	}

	public ArraySwipeAdapter(Context context, int resource,
	        int textViewResourceId)
	{
		super(context, resource, textViewResourceId);
	}

	@SuppressWarnings("unchecked")
	public ArraySwipeAdapter(Context context, int resource, T[] objects)
	{
		super(context, resource, objects);
	}

	@SuppressWarnings("unchecked")
	public ArraySwipeAdapter(Context context, int resource,
	        int textViewResourceId, T[] objects)
	{
		super(context, resource, textViewResourceId, objects);
	}

	@SuppressWarnings("unchecked")
	public ArraySwipeAdapter(Context context, int resource, List<T> objects)
	{
		super(context, resource, objects);
	}

	@SuppressWarnings("unchecked")
	public ArraySwipeAdapter(Context context, int resource,
	        int textViewResourceId, List<T> objects)
	{
		super(context, resource, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		boolean convertViewIsNull = convertView == null;
		View v = super.getView(position, convertView, parent);
		if (convertViewIsNull)
		{
			mItemManger.initialize(v, position);
		} else
		{
			mItemManger.updateConvertView(v, position);
		}
		return v;
	}

	@Override
	public void openItem(int position)
	{
		mItemManger.openItem(position);
	}

	@Override
	public void closeItem(int position)
	{
		mItemManger.closeItem(position);
	}

	@Override
	public void closeAllExcept(SwipeLayout layout)
	{
		mItemManger.closeAllExcept(layout);
	}

	@Override
	public List<Integer> getOpenItems()
	{
		return mItemManger.getOpenItems();
	}

	@Override
	public List<SwipeLayout> getOpenLayouts()
	{
		return mItemManger.getOpenLayouts();
	}

	@Override
	public void removeShownLayouts(SwipeLayout layout)
	{
		mItemManger.removeShownLayouts(layout);
	}

	@Override
	public boolean isOpen(int position)
	{
		return mItemManger.isOpen(position);
	}

	@Override
	public SwipeItemMangerImpl.Mode getMode()
	{
		return mItemManger.getMode();
	}

	@Override
	public void setMode(SwipeItemMangerImpl.Mode mode)
	{
		mItemManger.setMode(mode);
	}
}
