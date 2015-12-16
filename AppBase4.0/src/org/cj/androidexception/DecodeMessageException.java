package org.cj.androidexception;

/**
 * 消息解码异常
 * 
 */
public class DecodeMessageException extends MException
{
	private static final long	serialVersionUID	= 886679757189257008L;

	public DecodeMessageException(String msg)
	{
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public DecodeMessageException(String msg, int code)
	{
		super(msg);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getCauseMsg()
	{
		// TODO Auto-generated method stub
		return super.getCauseMsg();
	}

	@Override
	public int getCauseCode()
	{
		// TODO Auto-generated method stub
		return super.getCauseCode();
	}
}
