package org.cj.androidexception;

public class EncodeMessageException extends MException {

	/**
	 * 消息编码异常
	 */
	private static final long serialVersionUID = -242527246862074519L;

	public EncodeMessageException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public EncodeMessageException(String msg, int code) {
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
