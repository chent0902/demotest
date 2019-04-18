package com.family.demotest.vo.msg;

import java.io.Serializable;

public class KfBaseMsgVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6018380863043127630L;

	private String touser;

	private String msgtype;
	
	private String token; //微信token

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	

}
