/**
 * 
 */
package org.cj.androidexception;

/**
 * @author HR
 * 
 *         2014-5-6 上午9:11:30
 */
public class ServerErrorException extends MException
{
	private static final long	serialVersionUID	= 1L;

	public ServerErrorException(String error)
	{
		// TODO Auto-generated constructor stub
		super(error);
	}

	public ServerErrorException(String error, int code)
	{
		// TODO Auto-generated constructor stub
		super(error, code);
	}

	@Override
	public String getCauseMsg()
	{
		// TODO Auto-generated method stub
		return super.getCauseMsg();
	}

	@Override
	public String getMessage()
	{
		// TODO Auto-generated method stub
		return getCauseMsg();
	}

	@Override
	public String getLocalizedMessage()
	{
		// TODO Auto-generated method stub
		return getCauseMsg();
	}

	@Override
	public int getCauseCode()
	{
		// TODO Auto-generated method stub
		return super.getCauseCode();
	}

	@Override
	public Throwable getCause()
	{
		// TODO Auto-generated method stub
		return super.getCause();
	}
}
