package org.cj.download.info;

public class DownInfo implements _IDownInfo
{
	String	id	 = "";
	String	url	 = "";	  //下载地址
	long	size;
	String	path	= ""; //本地存放路径
	String	name	= "";

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

	@Override
	public String getId()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUrl()
	{
		// TODO Auto-generated method stub
		return url;
	}

	@Override
	public long getSize()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
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
	 * @param size
	 *            the size to set
	 */
	public void setSize(long size)
	{
		this.size = size;
	}

	@Override
	public String getPath()
	{
		// TODO Auto-generated method stub
		return path;
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		if (!name.equals("")) return name;
		int index = this.url.lastIndexOf("/") + 1;
		return this.url.substring(index);
	}

	@Override
	public void setName(String name)
	{
		// TODO Auto-generated method stub
		this.name = name + this.url.substring(this.url.lastIndexOf("."));
	}
}
