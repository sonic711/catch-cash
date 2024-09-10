package com.sean.web.vo;

import java.io.Serializable;

public class CoreBean implements Serializable {
	private static final long serialVersionUID = -5776171669323102900L;
	private boolean successful;
	private String msg;

	public CoreBean() {
	}

	public CoreBean(boolean successful) {
		this.successful = successful;
	}

	public CoreBean(boolean successful, String msg) {
		this.successful = successful;
		this.msg = msg;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
