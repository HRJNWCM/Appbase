package org.cj.layout.swipelayout.interfaces;

import java.util.List;

import org.cj.layout.swipelayout.SwipeLayout;
import org.cj.layout.swipelayout.implments.SwipeItemMangerImpl;

public interface SwipeItemMangerInterface
{
	public void openItem(int position);

	public void closeItem(int position);

	public void closeAllExcept(SwipeLayout layout);

	public void closeAll();

	public List<Integer> getOpenItems();

	public List<SwipeLayout> getOpenLayouts();

	public void removeShownLayouts(SwipeLayout layout);

	public boolean isOpen(int position);

	public SwipeItemMangerImpl.Mode getMode();

	public void setMode(SwipeItemMangerImpl.Mode mode);
}
