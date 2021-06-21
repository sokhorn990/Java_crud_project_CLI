package product;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.sun.jdi.connect.spi.Connection;

import dbconnection.Connector;
import usersmanagement.UserMenu;

public class ProductManagement {
	static java.sql.Connection connection = null;
	static Statement insertStatement = null;
	static Statement selectStatement = null;
	static Statement updateStatement = null;
	static Statement deleteStatement = null;
	static ResultSet result;
	static PreparedStatement statement;

	public static void showMenu() throws Exception {
		System.out.println("\t=====================================");
		System.out.println("\t\tPRODUCTS MANAGEMENTS ");
		System.out.println("\t=====================================");
		System.out.println("1. Add New Product");
		System.out.println("2. Modify Product");
		System.out.println("3. Search Product");
		System.out.println("4. Show Product");
		System.out.println("5. Delete Product");
		System.out.println("6. Exit");
		System.out.print("\nChoose Option Menu<1-6>: ");

		Scanner userInput = new Scanner(System.in);
		int option;
		boolean status = false;
		do {
			option = userInput.nextInt();
			switch (option) {
				case 1:
					System.out.println(" == ADD NEW PRODUCT == ");
					addProduct();
	
					break;
				case 2:
					System.out.println("");
					System.out.println(" == MODIFY PRODUCT  == ");
					UpdateProduct();
					break;
				case 3:
					System.out.println("");
					System.out.println(" == SEARCH PRODUCT  == ");
					searchProduct();
					break;
				case 4:
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
				case 5:
					System.out.println(" == DELETE PRDUCT== ");
					deleteProduct();
					break;
				case 6:
					System.out.println("\n == EXIT == ");
					UserMenu menu = new UserMenu();
					menu.adminMenu();
	
					break;
				default:
					System.out.println("\nInvalid input!");
					status = true;
				break;
			}

		} while (status == true);

	}

//	public void choice() throws ClassNotFoundException, SQLException {
//
//	}

//ADD NEW PRODUCT

	public static void addProduct() throws Exception {
		Scanner input = new Scanner(System.in);
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dateFormat.format(now);
		String status = "";
		do {
			connection = Connector.dbConnection();
			String sql = "INSERT INTO products " + "(pro_name, amount,category_id, created_at, updated_at) "
					+ "VALUES(?,?,?,?,?)";
			Statement stmt = connection.createStatement();
			ResultSet categories = stmt.executeQuery("SELECT * FROM categories");
			statement = connection.prepareStatement(sql);
			Scanner srtInput = new Scanner(System.in);
			Scanner intInput = new Scanner(System.in);

			System.out.print("Insert Product Name: ");
			String pro_name = srtInput.nextLine();

			System.out.print("Insert Amount: ");
			int amount = intInput.nextInt();
			int index = 1;
			while(categories.next()) {
				System.out.println(index++ +"."+ categories.getString(2));
			}
			System.out.print("\nInsert Category <1-"+(index-1)+">: ");
			int category_id = intInput.nextInt();

//			Step 2:Create statement using connection object
			statement = connection.prepareStatement(sql);
			statement.setString(1, pro_name);
			statement.setInt(2, amount);
			statement.setInt(3, category_id);
			statement.setString(4, currentDate);
			statement.setString(5, currentDate);

			int data = statement.executeUpdate();
			if (data != 0) {
				System.out.println("\n>> Product has been save successfully! <<");
			} else {
				System.out.println("\\n>> Saving product has been fail!! <<");
			}
			System.out.print("\\n>> Do you want to add any Product<Y/N>: ");
			status = input.next();
		}while(status.equals("Y") || status.equals("y"));
		showMenu();
	}

//SHOW ALL PRODUCT
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
		System.out.println(String.format("%10s%15s%15s%15s%25s%25s", "Product ID ", "Product Name", "Category ID",
				"Amount", "Create_at", "Updated_at"));
		System.out.println(
				"===================================================================================================================");

		return listProduct;

	}

//UPDATE PRODUCT

	public static void UpdateProduct() throws Exception {
		Scanner strInput = new Scanner(System.in);
		Scanner input = new Scanner(System.in);
		String status = "";
		do {
			try {
				String url = "jdbc:mysql://localhost:3306/sms_database";
				String user = "root";
				String password = "";
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				String currentDate = dateFormat.format(now);
				java.sql.Connection con = DriverManager.getConnection(url, user, password);

				System.out.print("\nEnter Product id: ");
				int id = input.nextInt();
				String query = "update products     SET pro_name=?," + "amount=?," + "category_id=?," + "updated_at=? "
						+ "where id = '" + id + "'";
				PreparedStatement statement = con.prepareStatement(query);
				Statement statementGet = con.createStatement();
				Statement stmtExists = con.createStatement();
				ResultSet existsProduct = stmtExists.executeQuery("SELECT EXISTS(SELECT * FROM products WHERE id='"+id+"')");
				ResultSet categories = statementGet.executeQuery("SELECT * FROM categories");
				while(existsProduct.next()) {
					if(existsProduct.getInt(1) == 1) {
						System.out.print("\nInsert Product Name: ");
						String pro_name = strInput.nextLine();

						System.out.print("\nInsert Amount: ");
						int amount = input.nextInt();
						System.out.println("Categories Name: ");
						int index = 1;
						while(categories.next()) {
							System.out.println(categories.getString(1) +"."+ categories.getString(2));
						}
						System.out.print("\nInsert Category: ");
						int category_id = input.nextInt();
						// Step 2:Create statement using connection object

						statement.setString(1, pro_name);
						statement.setInt(2, amount);
						statement.setInt(3, category_id);
						statement.setString(4, currentDate);
						// Step 3: Execute the query or update query
						statement.executeUpdate();
						System.out.println("Product have been updated!!");
					}else {
						System.out.println(">>Data not found!");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.print("Do yo want to update any product<Y/N>?");
			status = strInput.next();
		}while(status.equals("Y") || status.equals("y"));
		showMenu();
	}

//DELETE PRODUCT
	public static void deleteProduct() throws Exception {
		Scanner strInput = new Scanner(System.in);
		String status = "";
		String option = "";
		do {
			try {
				connection = Connector.dbConnection();
				selectStatement = connection.createStatement();
				System.out.println("Enter products id: ");
				int pro_id = strInput.nextInt();
				Statement stmt = connection.createStatement();
				ResultSet productExists = stmt.executeQuery("SELECT EXISTS(SELECT * FROM products WHERE id='"+pro_id+"')");
				while(productExists.next()) {
					if(productExists.getInt(1) == 1) {
						String query = "delete from products where id = " + pro_id;
						String userProduct = "delete from user_products where product_id = " + pro_id;
						PreparedStatement statement = connection.prepareStatement(query);
						System.out.print("Are you sure to delete the product ? <Y/N>: ");
						status = strInput.next();
						if(status.equals("Y") || status.equals("y")) {
							statement.executeUpdate(query);
							statement.executeUpdate(userProduct);
							System.out.println("\n>>  Product Have been deleted! <<");
						}
					}else {
						System.out.println(">> Data not found!");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.print("Do yo want to delete any product<Y/N>?");
			option = strInput.next();
		}while(option.equals("Y") || option.equals("y"));
		showMenu();
	}

	// SEARCH PRODUCT
	public static void searchProduct() throws Exception {
		Scanner strInput = new Scanner(System.in);
		String url = "jdbc:mysql://localhost:3306/sms_database";
		String user = "root";
		String password = "";
		String status = "";
		do {
			try {
				java.sql.Connection con = DriverManager.getConnection(url, user, password);
				java.sql.Statement statement = con.createStatement();
				java.sql.Statement stmt = con.createStatement();
				Scanner input = new Scanner(System.in);
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
			status = strInput.next();
		}while(status.equals("Y") || status.equals("y"));
		showMenu();
	}
}