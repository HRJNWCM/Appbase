package org.cj.layout.swipelayout.adapters;

import java.util.ArrayList;
import java.util.List;

import org.cj.layout.swipelayout.SwipeLayout;
import org.cj.layout.swipelayout.implments.SwipeItemMangerImpl;
import org.cj.layout.swipelayout.interfaces.SwipeAdapterInterface;
import org.cj.layout.swipelayout.interfaces.SwipeItemMangerInterface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseSwipeAdapter extends BaseAdapter implements
        SwipeItemMangerInterface, SwipeAdapterInterface
{
	protected List<? extends Object>	objects	 = new ArrayList<Object>();
	public LayoutInflater	         inflater;
	public Context	                 context;
	protected SwipeItemMangerImpl	 mItemManger	= new SwipeItemMangerImpl(this);

	public BaseSwipeAdapter(Context context, List<? extends Object> objects)
	{
		// TODO Auto-generated constructor stub
		this.context = context;
		this.objects = objects;
		inflater = LayoutInflater.from(context);
	}

	public void setList(List<? extends Object> objects)
	{
		this.objects = objects;
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return objects.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		// TODO Auto-generated method stub
		return objects.get(arg0);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * return the
	 * {@link com.marshalchen.common.uimodule.swipelayout.SwipeLayout} resource
	 * id, int the view item.
	 * 
	 * @param position
	 * @return
	 */
	public abstract int getSwipeLayoutResourceId(int position);

	/**
	 * generate a new view item. Never bind SwipeListener or fill values here,
	 * every item has a chance to fill value or bind listeners in fillValues. to
	 * fill it in {@code fillValues} method.
	 * 
	 * @param position
	 * @param parent
	 * @return
	 */
	public abstract View generateView(int position, ViewGroup parent);

	/**
	 * fill values or bind listeners to the view.
	 * 
	 * @param position
	 * @param convertView
	 */
	public abstract void fillValues(int position, View convertView);

	@Override
	public final View getView(int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		if (v == null)
		{
			v = generateView(position, parent);
			mItemManger.initialize(v, position);
		} else
		{
			mItemManger.updateConvertView(v, position);
		}
		fillValues(position, v);
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
	public void closeAll()
	{
		// TODO Auto-generated method stub
		mItemManger.closeAll();
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
