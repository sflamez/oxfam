package be.sefl.oxfam.object;

import java.io.Serializable;

import be.sefl.oxfam.constants.Constants;

/**
 * @author sefl
 */
public class Article implements Serializable {

	private static final long serialVersionUID = -6575292635308849239L;

	private int id;
	private int btw;
	private String description;
	private String shortDescription;
	private double price;
	private int stock;
	private Category category;
	
	public Article(int id, int btw, String description, String shortDescription, double price, int stock, Category category) {
		this.id = id;
		this.btw = btw;
		this.description = description;
		this.shortDescription = shortDescription;
		this.price = price;
		this.stock = stock;
		this.category = category;
	}
	
	public int getId() {
		return id;
	}
	
	public int getBtw() {
		return btw;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getShortDescription() {
		return shortDescription;
	}
	
	public double getPrice() {
		return price;
	}
	
	public int getStock() {
		return stock;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public void addToStock(int change) {
		stock += change;
	}
	
	public void takeFromStock(int change) {
		stock -= change;
	}
	
	public String toString() {
		String tab = (id >= 10000 ? "\t" : "\t\t");
		return (id + ":" + tab + description + ", " + price + Constants.EURO);
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof Article)) {
			return false;
		}
		Article art = (Article) obj;
		return id==art.id;
	}
}