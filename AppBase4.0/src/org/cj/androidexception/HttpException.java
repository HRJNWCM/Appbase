package org.cj.androidexception;

public class HttpException extends MException {

	/**
	 * 网络连接异常
	 */
	private static final long serialVersionUID = -1760551601965868561L;

	public HttpException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public HttpException(String msg, int code) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getCauseMsg() {
		// TODO Auto-generated method stub
		return super.getCauseMsg();
	}

	@Override
	public int getCauseCode() {
		// TODO Auto-generated method stub
		return super.getCauseCode();
	}

}
