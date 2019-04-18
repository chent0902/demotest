package com.family.demotest.vo.msg;

public class KfVideoVo extends KfBaseMsgVo {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5430886391629395948L;
	private KfVideo video;

	public KfVideo getVideo() {
		return video;
	}

	public void setVideo(KfVideo video) {
		this.video = video;
	}
}

class KfVideo {

	private String media_id;// ":"MEDIA_ID",
	private String thumb_media_id;// ":"MEDIA_ID",
	private String title;// ":"TITLE",
	private String description;// ":"DESCRIPTION"

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getThumb_media_id() {
		return thumb_media_id;
	}

	public void setThumb_media_id(String thumb_media_id) {
		this.thumb_media_id = thumb_media_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
