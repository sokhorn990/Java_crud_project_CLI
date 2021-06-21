package product;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dbconnection.Connector;
import usersmanagement.UserMenu;

public class ManagerProductClass {
	static java.sql.Connection connection = null;
	static Statement insertStatement = null;
	static Statement selectStatement = null;
	static Statement updateStatement = null;
	static Statement deleteStatement = null;
	static ResultSet result;
	static PreparedStatement statement;

	public static void showMenu() throws Exception {
		System.out.println("=====================================");
		System.out.println(" PRODUCTS MANAGEMENTS ");
		System.out.println("=====================================");

		System.out.println("1. Search Product");
		System.out.println("2. Show Product");
		System.out.println("3. Exit");
		System.out.print("\nChoose Option Menu<1-3>: ");

		Scanner userInput = new Scanner(System.in);
		boolean status = false;
		int option;
		do {

			option = userInput.nextInt();
			switch (option) {
			case 1:
				System.out.println("");
				System.out.println(" == SEARCH PRODUCT  == ");
				searchProduct();
				break;
			case 2:
				System.out.println("");
				System.out.println(" == SHOW ALL PRODUCT == ");
				ProductManagement productManager = new ProductManagement();
				List<Product> productList = new ArrayList<Product>();
				productList = productManager.getAllProduct();
				for (Product product : productList) {
					System.out.println(product);
					System.out.println(
							"===================================================================================================================");
				}
				System.out.println("\nPress enter back to menu ......");
				try{System.in.read();}catch(Exception e){	e.printStackTrace();}
				showMenu();
				break;
			case 3:
				System.out.println("");
				System.out.println(" == EXIT == ");
				UserMenu menu = new UserMenu();
				menu.adminMenu();

				break;
			default:
				System.out.println("\nInvalid input!");
				status = true;
				break;
			}
//			
		} while (status == true);

	}

	// SEARCH PRODUCT
	public static void searchProduct() throws Exception {
		Scanner input = new Scanner(System.in);
		String url = "jdbc:mysql://localhost:3306/sms_database";
		String user = "root";
		String password = "";
		String status = "";
		do {
			try {
				java.sql.Connection con = DriverManager.getConnection(url, user, password);
				java.sql.Statement statement = con.createStatement();
				java.sql.Statement stmt = con.createStatement();
				System.out.println("Enter Product id: ");
				int id = input.nextInt();
				ResultSet product = stmt.executeQuery("SELECT EXISTS(SELECT * from products" + " where id='" + id+"')");
				while(product.next()) {
					if(product.getInt(1) == 1) {
						ResultSet result = statement.executeQuery("SELECT * from products" + " where id=" + id);
						System.out.println(
								"===================================================================================================================");
						System.out.println(String.format("%10s%15s%15s%15s%25s%25s", "Product ID ", "Product Name", "Amount",
								"Category ID", "Create_at", "Updated_at"));
						System.out.println(
								"===================================================================================================================");
			
						while (result.next())
							System.out.println(String.format("%10s%15s%15s%15s%25s%25s", result.getInt(1), result.getString(2),
									result.getString(3), result.getString(4), result.getString(5), result.getString(6)));
						System.out.println(
								"===================================================================================================================");
//						con.close();
					}else {
						System.out.println(">> Data not found!");
					}
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.print("Do yo want to search any product<Y/N>?");
			status = input.next();
		}while(status.equals("Y") || status.equals("y"));
		showMenu();
	}

	// SHOW ALL PRODUCT
	public List<Product> getAllProduct() throws ClassNotFoundException {
		List<Product> listProduct = new ArrayList<Product>();
		try {
			connection = Connector.dbConnection();
			selectStatement = connection.createStatement();
			result = selectStatement.executeQuery("SELECT * FROM products");
			while (result.next()) {
				int productId = result.getInt(1);
				String pro_name = result.getString(2);
				int amount = result.getInt(3);
				int category_id = result.getInt(4);
				String created_at = result.getString(5);
				String updated_at = result.getString(6);

				Product product = new Product(productId, pro_name, amount, category_id, created_at, updated_at);
				listProduct.add(product);

			}
		} catch (SQLException e) {
			System.out.println("Connection failed!");
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		System.out.println(
				"===================================================================================================================");
		System.out.println(String.format("%10s%15s%15s%15s%25s%25s", "Product ID ", "Product Name", "Amount",
				"Category ID", "Create_at", "Updated_at"));
		System.out.println(
				"===================================================================================================================");

		return listProduct;

	}

}
