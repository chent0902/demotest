package com.family.demotest.vo.imgMsg;



import java.util.List;

/**
 * 图文消息model
 * 
 * @author wujf
 * @date 2014-08-20
 */
public class NewsMessage extends Base {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6977308464429455751L;
	// 图文消息个数，限制为10条以内
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<Article> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
	}
}