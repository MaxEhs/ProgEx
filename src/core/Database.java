package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import classes.Furniture;
import classes.Admin;

public class Database {

	public static ObservableList<Furniture> loadFurniture() {

		ObservableList<Furniture> furnitureList = FXCollections.observableArrayList();

		Connection c = null;
		Statement stmt = null;

		try {

			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Database.db");

		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}

		try {

			// get Furniture from DB
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Furniture;");
			while (rs.next()) {
				int id = rs.getInt("ID");
				int category = rs.getInt("Category");
				int stock = rs.getInt("Stock");
				String name = rs.getString("Name");
				String description = rs.getString("Description");
				String pictureFile = rs.getString("PictureFile");
				float price = rs.getFloat("Price");

				Furniture temp = new Furniture(id, category, stock, name, description, pictureFile, price);

				furnitureList.add(temp);
			}

			rs.close();
			c.close();

		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
		return furnitureList;
	}

	public static void saveFurniture(ObservableList<Furniture> furnitureList) throws Exception {

		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Database.db");
			c.setAutoCommit(false);

			// Delete the old stuff
			stmt = c.createStatement();

			stmt.executeUpdate("DELETE FROM Furniture;");
			stmt.executeUpdate("DELETE FROM sqlite_sequence;");

			// Save everything
			for (Furniture f : furnitureList) {
				stmt.executeUpdate(
						"INSERT INTO Furniture (ID, Category, Stock, Name, Description, PictureFile, Price)VALUES("
								+ f.getId() + ", " + f.getCategory() + ", " + f.getStock() + ", '" + f.getName()
								+ "', '" + f.getDescription() + "', '" + f.getPictureFile() + "', " + f.getPrice()
								+ ");");
			}

			c.commit();

			System.out.println("++EVERYTHING NOW SAVED++");
		} catch (Exception e) {
			c.rollback();
			System.out.println(e);
		} finally {
			c.close();
		}
	}

	public static ArrayList<Admin> loadAdmins() {

		ArrayList<Admin> adminList = new ArrayList<Admin>();

		Connection c = null;
		Statement stmt = null;

		try {

			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Database.db");

		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}

		try {

			// get Admins from DB
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Admin;");
			while (rs.next()) {
				int id = rs.getInt("ID");
				String login = rs.getString("Login");
				String password = rs.getString("Password");

				Admin temp = new Admin(id, login, password);

				adminList.add(temp);
			}

			rs.close();
			c.close();

		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}

		return adminList;
	}

}
