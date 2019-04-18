package com.family.demotest.vo.msg;

import java.io.Serializable;

public class KfaccountVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2281177884305439764L;
	/**
	 * 帐号
	 */
	private String kf_account;
	/**
	 * 昵称
	 */
	private String nickname;
	
	/**
	 * 密码(MD5加密后)
	 */
	private String password;

	public String getKf_account() {
		return kf_account;
	}

	public void setKf_account(String kf_account) {
		this.kf_account = kf_account;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
