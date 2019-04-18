package com.family.demotest.vo.msg;

public class KfVoiceVo extends KfBaseMsgVo{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2936274056521814172L;
	private KfVoice voice;

	public KfVoice getVoice() {
		return voice;
	}

	public void setVoice(KfVoice voice) {
		this.voice = voice;
	}
	
}

class KfVoice{
	private String media_id;

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
	
	
}
