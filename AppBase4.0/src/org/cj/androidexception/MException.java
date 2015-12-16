package org.cj.androidexception;

public class MException extends Exception
{
	private static final long	serialVersionUID	= 1L;
	public String	          causeMsg	         = null;
	public int	              causeCode	         = 0;

	public MException(String msg)
	{
		super(msg);
		this.causeMsg = msg;
	}

	public MException(String msg, int code)
	{
		super(msg);
		this.causeMsg = msg;
		this.causeCode = code;
	}

	/**
	 * 返回 错误信息
	 * 
	 * @return
	 */
	public String getCauseMsg()
	{
		return this.causeMsg;
	}

	/**
	 * 返回错误代码
	 * 
	 * @return
	 */
	public int getCauseCode()
	{
		return this.causeCode;
	}
}
