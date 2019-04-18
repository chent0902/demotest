package com.family.demotest.vo.msg;

public class KfImageVo extends KfBaseMsgVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3211530455127945791L;
	private KfImage image;

	public KfImage getImage() {
		return image;
	}

	public void setImage(KfImage image) {
		this.image = image;
	}
	
}

class KfImage{
	private String media_id;

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
	
}
