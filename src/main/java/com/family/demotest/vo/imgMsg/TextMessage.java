package com.family.demotest.vo.imgMsg;



/**
 * 文本消息
 * 
 * @author wujf
 * @date 2014-08-20
 */
public class TextMessage extends Base {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4893694014293012049L;
	// 回复的消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}