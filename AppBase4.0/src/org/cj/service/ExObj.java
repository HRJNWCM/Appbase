package org.cj.service;

import java.io.Serializable;

public class ExObj implements Serializable
{
	public static final String	TAG					= ExObj.class
															.getSimpleName();
	private static final long	serialVersionUID	= 1L;
	String						name;
	String						msg;

	public void setName(String name)
	{
		this.name = name;
	}

	public void setError(String msg)
	{
		this.msg = msg;
	}

	public String getName()
	{
		return name;
	}

	public String getError()
	{
		return msg;
	}
}