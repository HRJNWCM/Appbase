package org.cj.upgrade;

public class DownLoadInfo
{
	String	url;
	long	size;
	String	des;
	int	   mode;

	/**
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

	/**
	 * @return the size
	 */
	public long getSize()
	{
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(long size)
	{
		this.size = size;
	}

	/**
	 * @return the des
	 */
	public String getDes()
	{
		return des;
	}

	/**
	 * @param des
	 *            the des to set
	 */
	public void setDes(String des)
	{
		this.des = des;
	}

	/**
	 * @return the mode
	 */
	public int getMode()
	{
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(int mode)
	{
		this.mode = mode;
	}
}
