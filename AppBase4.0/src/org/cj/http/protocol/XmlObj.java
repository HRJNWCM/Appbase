package org.cj.http.protocol;

public class XmlObj
{
	String	name;
	long	length;

	public XmlObj(String name, long length)
	{
		// TODO Auto-generated constructor stub
		this.name = name;
		this.length = length;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the length
	 */
	public long getLength()
	{
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(long length)
	{
		this.length = length;
	}

}
