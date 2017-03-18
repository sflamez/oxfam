package be.sefl.oxfam.object;

import java.util.ArrayList;
import java.util.List;

import be.sefl.oxfam.constants.Constants;

/**
 * @author sefl
 */
public class Category {

	private int ID;
	private int BTW;
	private String name;
	private List<Article> articles;
	
	public Category(int ID, int BTW, String name) {
		this.ID = ID;
		this.BTW = BTW;
		this.name = name;
		articles = new ArrayList<Article>();
	}
	
	public int getID() {
		return ID;
	}
	
	public int getBTW() {
		return BTW;
	}
	
	public Article getArticleAtIndex(int idx) {
		return articles.get(idx);
	}
	
	public List<Article> getArticles() {
		return articles;
	}
	
	public int getArticleCount() {
		return articles.size();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void addArticle(Article article) {
		this.articles.add(article);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		String ret = "Category " + name + " contains:" + Constants.NEWLINE;
		for (int i = 0; i < this.articles.size(); i++) {
			ret += "\t"+ this.articles.get(i) + Constants.NEWLINE;
		}
		return ret;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Category)) {
			return false;
		}
		Category cat = (Category) obj;
		return this.name.equals(cat.name);
	}
}