package com.family.demotest.vo.msg;

public class KfTextVo extends KfBaseMsgVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5248793939245707186L;
	private KfText text;

	public KfText getText() {
		return text;
	}
	public void setText(String content){
		KfText kfText = new KfText();
		kfText.setContent(content);
		this.text = kfText;
	}
}

class KfText {
	// 发送内容
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
