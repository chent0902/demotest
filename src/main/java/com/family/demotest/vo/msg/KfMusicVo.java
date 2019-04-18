package com.family.demotest.vo.msg;

public class KfMusicVo  extends KfBaseMsgVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5941902151950462217L;
	private KfMusic music;

	public KfMusic getMusic() {
		return music;
	}

	public void setMusic(KfMusic music) {
		this.music = music;
	}
	
}

class KfMusic{
	
	private String title;// ":"MUSIC_TITLE",
	private String description;// ":"MUSIC_DESCRIPTION",
	private String musicurl;// ":"MUSIC_URL",
	private String hqmusicurl;// ":"HQ_MUSIC_URL",
	private String thumb_media_id;// ":"THUMB_MEDIA_ID"

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

	public String getMusicurl() {
		return musicurl;
	}

	public void setMusicurl(String musicurl) {
		this.musicurl = musicurl;
	}

	public String getHqmusicurl() {
		return hqmusicurl;
	}

	public void setHqmusicurl(String hqmusicurl) {
		this.hqmusicurl = hqmusicurl;
	}

	public String getThumb_media_id() {
		return thumb_media_id;
	}

	public void setThumb_media_id(String thumb_media_id) {
		this.thumb_media_id = thumb_media_id;
	}

}