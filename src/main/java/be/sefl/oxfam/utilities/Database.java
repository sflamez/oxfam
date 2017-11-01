package be.sefl.oxfam.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import be.sefl.oxfam.object.Article;
import be.sefl.oxfam.object.Category;
import be.sefl.oxfam.object.Order;

public class Database {

	private final static String PAR_FILE = "c:/Development/OxfamDB.mdb";
	private final static String PAR_JDBC_URL = "jdbc:ucanaccess://" + PAR_FILE;

	/**
	 * @return the categories from DB
	 */
	public List<Category> getCategories() throws Exception {
		List<Category> categories = new ArrayList<>();
		try (Connection connection = connect(); Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("select * from Category");
			while (resultSet.next()) {
				int ID = resultSet.getInt("ID");
				int BTW = resultSet.getInt("BTW");
				String naam = resultSet.getString("Name");
				categories.add(ID - 1, new Category(ID, BTW, naam));
			}
		}

		return categories;
	}

	/**
	 * @return the articles of the specified category from DB
	 */
	public List<Article> getArticles(Category category) throws Exception {
		List<Article> articles = new ArrayList<>();
		try (Connection connection = connect();
				PreparedStatement statement = connection
						.prepareStatement("select * from Article where Category = ? order by SortNumber")) {
			statement.setInt(1, category.getID());
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int ID = resultSet.getInt("ID");
				String description = resultSet.getString("Description");
				String shortDescription = resultSet.getString("ShortDescription");
				double price = resultSet.getDouble("Price");
				int stock = resultSet.getInt("Stock");
				int BTW = category.getBTW();
				articles.add(new Article(ID, BTW, description, shortDescription, price, stock, category));
			}
		}
		return articles;
	}

	public void processOrderInDB(Order order) throws Exception {
		try (Connection connection = connect();
				PreparedStatement changeStock = connection.prepareStatement("update Article set Stock=? where ID=?")) {
			for (Article article : order.getArticles()) {
				// update stock in database
				if (!article.getCategory().isLeeggoed()) {
					article.takeFromStock(order.getCount(article));
					changeStock.setInt(1, article.getStock());
					changeStock.setInt(2, article.getId());
					changeStock.executeUpdate();
				}
			}
		}

	}

	/** Resets stock for each article to 9999. */
	public void resetStock() {
	}

	private Connection connect() throws Exception {
		return DriverManager.getConnection(PAR_JDBC_URL);
	}

}
