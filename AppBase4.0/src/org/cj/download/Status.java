package org.cj.download;

public interface Status
{
	public static final int	UNKNOW		= 0;
	public static final int	DOWNLOADING	= 1;
	public static final int	PAUSE		= 2;
	public static final int	STOP		= 3;
	public static final int	COMPLETE	= 4;
	public static final int	ERROR		= 5;
}
