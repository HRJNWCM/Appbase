// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Const.java

package org.cj.http;

public interface Const
{
	public static interface MessageStatus
	{

		public static final int	SUCESS	= 0;
		public static final int	ERROR	= 500;
		public static final int	TIMEOUT	= 10;
	}

	public enum NetWork
	{

		NORMAOL(200), NOINTERNET(-100), TIMEOUT(-102), ENCODEEXCEPTION(-201), DECODEEXCEPTION(
				-202), IOEXCEPTION(-500), OTHER(-400), SESSIONTIMEOUT(10);
		public int	status;

		private NetWork(int i)
		{
			// TODO Auto-generated constructor stub
			this.status = i;
		}

	}

	public static final int	LOGINED	= 161;
	public static final int	UNKNOW	= 177;
}
