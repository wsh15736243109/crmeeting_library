package com.cr.pn.netWork.netty.base.exception;

public class MapKeyException extends NettyException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ex;

	public MapKeyException(String ex) {
		super(ex);
		this.ex = ex;
	}

	public String getEx() {
		return ex;
	}
	
}
