package com.family.demotest.vo.msg;

import java.util.List;

public class KfNewsVo extends KfBaseMsgVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9207356262213223338L;
	private KfNews news;

	public KfNews getNews() {
		return news;
	}

	public void setNews(KfNews news) {
		this.news = news;
	}

}

class KfNews {

	private List<Kfarticles> articles;

	public List<Kfarticles> getArticles() {
		return articles;
	}

	public void setArticles(List<Kfarticles> articles) {
		this.articles = articles;
	}

}

class Kfarticles {

	private String title;// ":"Happy Day",
	private String description;// ":"Is Really A Happy Day",
	private String url;// ":"URL",
	private String picurl;// ":"PIC_URL"

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

}