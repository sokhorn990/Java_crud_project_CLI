package usersmanagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import dbconnection.Connector;

public class UserCRUDMenu {
	Scanner input = new Scanner(System.in);
	InputStreamReader read=new InputStreamReader(System.in);    
    BufferedReader buffered=new BufferedReader(read); 
	Connector mySqlCon = new Connector();
	Connection conn = mySqlCon.dbConnection();
	String name,gender,email,phone,address;
	int birthDay,birthMonth,birthYear,positionID,productID;
	// user menu
	public void menu() throws Exception {
		boolean status = false;
		do {
			System.out.println("\t=========================");
			System.out.println("\t\tMenu Users");
			System.out.println("\t=========================\n");
			System.out.println("1. Add New User");
			System.out.println("2. Modify User");
			System.out.println("3. Search User");
			System.out.println("4. Show All Users");
			System.out.println("5. Delete User");
			System.out.println("6. Exit");
			System.out.print("\nEnter menu option <1-6>:");
			int option = input.nextInt();
			switch(option) {
				case 1:
					this.addUser();
				break;
				case 2:
					this.modifyUser();
				break;
				case 3:
					this.searchUser();
				break;
				case 4:
					this.showAllUser();
				break;
				case 5:
					this.deleteUser();
				break;
				case 6:
					System.out.println("You have been exited!");
					UserMenu userMenu = new UserMenu();
					userMenu.managerMenu();
				break;
				default:
					System.out.println("Invalid Input!");
					status = true;
			}
		}while(status == true);
	}
	// add user to database
	public void addUser() throws SQLException, Exception {     
	    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
	    LocalDateTime now = LocalDateTime.now();
	    String currentDate = dateFormat.format(now);
		String mySqlInsert = "INSERT INTO users(username,gender,email,password,phone,address,dob,position_id,created_at,updated_at) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?)";
		String userProductInsert = "INSERT INTO user_products(product_id,user_id) VALUES(?,?)";
		String status = "";
		int userId = 0;
		do {
			if(mySqlCon != null) {
				java.sql.Statement getPosition = conn.createStatement();
				java.sql.Statement stmtLast = conn.createStatement();
				java.sql.Statement stmt = conn.createStatement();
				ResultSet positions = getPosition.executeQuery("SELECT * FROM positions");
				java.sql.Statement getProduct = conn.createStatement();
				ResultSet products = getProduct.executeQuery("SELECT * FROM products");
				
				System.out.println("\n\t==== ADD USER ====\n");
				System.out.print("Enter Username: ");
				name = buffered.readLine();
				System.out.print("\nEnter Gender: ");
				gender = buffered.readLine();
				System.out.print("\nEnter Email: ");
				email = buffered.readLine();
				System.out.print("\nEnter Phone: ");
				phone = buffered.readLine();
				System.out.print("\nEnter Address: ");
				address = buffered.readLine();
				do {
					System.out.print("\nEnter Birth Day <1-30>: ");
					birthDay = input.nextInt();
				}while(birthDay > 31 || birthDay < 1);
				do {
					System.out.print("\nEnter Birth Month <1-12>: ");
					birthMonth = input.nextInt();
				}while(birthMonth > 12 || birthMonth < 1);
				do {
					System.out.print("\nEnter Birth Year: ");
					birthYear = input.nextInt();
				}while(birthYear >= 2020 || birthYear < 1);
				
				System.out.println("Position Name::");
					while(positions.next()) {
						System.out.println("\t"+positions.getInt(1)+". "+positions.getString(2));
					}
					do {
						System.out.print("\nEnter Position <1-2>: ");
						positionID = input.nextInt();
					}while(positionID > 2 || positionID < 1);
					System.out.println("Position Name::");
					int length = 0;
					while(products.next()) {
						System.out.println("\t"+products.getInt(1)+". "+products.getString(2));
						length++;
					}
//					do{
						System.out.print("\nEnter Product: ");
						productID = input.nextInt();
//					}while(productID );
				PreparedStatement stmtInsert = conn.prepareStatement(mySqlInsert);
				PreparedStatement stmtInsertUserProduct = conn.prepareStatement(userProductInsert);
				stmtInsert.setString(1, name);
				stmtInsert.setString(2, gender);
				stmtInsert.setString(3, email);
				stmtInsert.setString(4, "12345678");
				stmtInsert.setString(5, phone);
				stmtInsert.setString(6, address);
				stmtInsert.setString(7, birthYear+"-"+birthMonth+"-"+birthDay);
				stmtInsert.setInt(8, positionID);
				stmtInsert.setString(9, currentDate);
				stmtInsert.setString(10, currentDate);
				stmtInsert.executeUpdate();
				ResultSet userLastInsert= stmtLast.executeQuery("SELECT * FROM users"
						+ " ORDER BY created_at DESC LIMIT 1");
				while(userLastInsert.next()) 
				{
				   userId = userLastInsert.getInt(1);
				}
				stmtInsertUserProduct.setInt(1, productID);
				stmtInsertUserProduct.setInt(2, userId);
				stmtInsertUserProduct.executeUpdate();
				System.out.println(">>User have been saved successfull!");
			}else {
				System.out.println(">>Saving have been fail!");
			}
			System.out.print("Do you want to add more <Y/N>?");
			status = input.next();
		}while(status.equals("Y") || status.equals("y"));
		this.menu();
	}
	public void modifyUser() throws Exception {
		int index = 1;
		String status = "";
		do {
			System.out.println("\n\t\t==== Modify User ====\n");
			System.out.print("Enter User ID: ");
			int userId = input.nextInt();
			if(mySqlCon != null) {
				java.sql.Statement stmt = conn.createStatement();
				java.sql.Statement stmtExists = conn.createStatement();
				ResultSet userExists = stmtExists.executeQuery("SELECT EXISTS(SELECT * FROM users WHERE id ='"+userId+"')");
				ResultSet user = stmt.executeQuery("SELECT * FROM users LEFT JOIN positions ON users.position_id = positions.id WHERE users.id='"+userId+"'");
				this.tableHeader();
				while(userExists.next()) {
					if(userExists.getInt(1) == 0) {
						System.out.println(">>Data not found!");
						System.out.print("\nDo you want to modify any users? <Y/N>: ");
						status = input.next();
						System.out.println();
					}else {
						while(user.next()) {
							System.out.println(String.format("%s%15s%25s%15s%15s%20s%15s", index++,user.getString(2),user.getString(4),user.getString(3),user.getString(7),user.getString(8),user.getString(16)));
							System.out.println("===================================================================================================================");
						}
						this.modifyMenu(userId);
					}
				}
			}
		}while(status.equals("Y") || status.equals("y"));
	}
	// show all users
	public void showAllUser() throws Exception {
		String status = "";
		int index = 1;
		if(mySqlCon != null) {
			java.sql.Statement getPosition = conn.createStatement();
			ResultSet users = getPosition.executeQuery("SELECT * FROM users LEFT JOIN positions ON users.position_id = positions.id ");

			System.out.println("\n\t\t\t==== Show All Users ====\n");
			this.tableHeader();
			if(users.next() == true) {
				while(users.next()) {
					System.out.println(String.format("%s%15s%25s%15s%15s%20s%15s", index++,users.getString(2),users.getString(4),users.getString(3),users.getString(7),users.getString(8),users.getString(16)));
					System.out.println("===================================================================================================================");
				}
			}else {
				System.out.println(">>Record empty!");
			}
			System.out.println("\n>>Press anykey back to menu...");
			try{System.in.read();}catch(Exception e){	e.printStackTrace();}
			this.menu();
		}
	}
	// delete user
	public void deleteUser() throws Exception {
		String status = "";
		String option = "";
		String deleteOption = "";
		int userId = 0;
		int index = 1;
		if(mySqlCon != null) {
			java.sql.Statement getUser = conn.createStatement();
			java.sql.Statement delete = conn.createStatement();
			java.sql.Statement getPosition = conn.createStatement();
			ResultSet users = getPosition.executeQuery("SELECT * FROM users INNER JOIN positions on users.position_id = positions.id");
			System.out.println("\t==== User Listing ====");
			this.tableHeader();
			while(users.next()) {
				System.out.println(String.format("%s%15s%25s%15s%15s%20s%15s", index++,users.getString(2),users.getString(4),users.getString(3),users.getString(7),users.getString(8),users.getString(10)));
				System.out.println("===================================================================================================================");
			}
			do {
				System.out.println("\t==== Delete User ====\n");
				System.out.print("Enter User ID: ");
				userId = input.nextInt();
				ResultSet user = getUser.executeQuery("SELECT EXISTS(SELECT * FROM users WHERE id='"+userId+"')");
				while(user.next()) {
					if(user.getInt(1) == 0) {
						System.out.println(">>Data not found!");
					}else {
						System.out.print(">> Are you sure to delete? <Y/N>: ");
						deleteOption = input.next();
						
						if(deleteOption.equals("Y") || deleteOption.equals("y")) {
							delete.executeUpdate("DELETE FROM users WHERE id='"+userId+"'");
							delete.executeUpdate("DELETE FROM user_products WHERE user_id='"+userId+"'");
							System.out.println(">>User have been delete successfully!");
						}
					}
				}
				System.out.print(">> Do you want to delete other users? <Y/N>: ");
				option = input.next();
			}while(option.equals("Y") || option.equals("y"));
			this.menu();
		}
	}
	// search user
	public void searchUser() throws Exception {
		boolean optionStatus = false;
		do {
			System.out.println("\t==== Search User ====\n");
			System.out.println("1. Search By ID");
			System.out.println("2. Search By Name");
			System.out.println("3. Exit");
			System.out.print("Choose Menu Option <1-3>:");
			int option = input.nextInt();
			switch(option) {
				case 1:
					this.searchById();
				break;
				case 2:
					this.searchByName();
				break;
				case 3:
					System.out.println("You have been exited!");
					this.menu();
				break;
				default:
					System.out.println(">>Invalid input!");
					optionStatus = true;
				break;
			}
		}while(optionStatus == true);
		this.menu();
	}
	// table header
	public void tableHeader() {
		System.out.println("===================================================================================================================");
		System.out.println(String.format("%s%15s%25s%15s%15s%20s%15s", "ID","Name","Email","Gender","Phone","Address","Positon"));
		System.out.println("===================================================================================================================");
	}
	// search user by id
	public void searchById() throws Exception {
		String status = "";
		do {
			System.out.println("\t==== Search User By ID ====\n");
			System.out.print("Enter User ID: ");
			int userId = input.nextInt();
			System.out.println();
			int index = 1;
			if(mySqlCon != null) {
				java.sql.Statement stmt = conn.createStatement();
				java.sql.Statement stmtExist = conn.createStatement();
				ResultSet userExist = stmtExist.executeQuery("SELECT EXISTS(SELECT * FROM users WHERE id='"+userId+"')");
				ResultSet user = stmt.executeQuery("SELECT * FROM users INNER JOIN positions on users.position_id = positions.id WHERE users.id='"+userId+"'");
				this.tableHeader();
				while(userExist.next()) {
					if(userExist.getInt(1) == 1){
						while(user.next()) {
							System.out.println(String.format("%s%15s%25s%15s%15s%20s%15s", index++,user.getString(2),user.getString(4),user.getString(3),user.getString(7),user.getString(8),user.getString(16)));
							System.out.println("===================================================================================================================");
						}
					}else {
						System.out.println(">> Data not found!");
					}
				}
			}
			System.out.print("\nDo you want to search any users? <Y/N>: ");
			status = input.next();
		}while(status.equals("Y") || status.equals("y"));
		this.searchUser();
		
	}
	// search user by name
		public void searchByName() throws Exception {
			String status = "";
			do {
				System.out.println("\t==== Search User By NAME ====\n");
				System.out.print("Enter User Name: ");
				String username = input.next();
				System.out.println();
				int index = 1;
				if(mySqlCon != null) {
					java.sql.Statement stmt = conn.createStatement();
					java.sql.Statement stmtExist = conn.createStatement();
					ResultSet userExist = stmtExist.executeQuery("SELECT EXISTS(SELECT * FROM users WHERE username LINE '%"+username+"%')");
					ResultSet user = stmt.executeQuery("SELECT * FROM users INNER JOIN positions on users.position_id = positions.id WHERE users.username LIKE '%"+username+"%'");
					this.tableHeader();
					while(userExist.next()) {
						if(userExist.getInt(1) == 1) {
							while(user.next()) {
								System.out.println(String.format("%s%15s%25s%15s%15s%20s%15s", index++,user.getString(2),user.getString(4),user.getString(3),user.getString(7),user.getString(8),user.getString(16)));
								System.out.println("===================================================================================================================");
							}
						}else {
							System.out.println("\t>>Data Not Found!");
						}
					}
					
				}
				System.out.print("\nDo you want to search any users? <Y/N>: ");
				status = input.next();
			}while(status.equals("Y") || status.equals("y"));
			this.searchUser();
		}
		// modify user menu
		public void modifyMenu(int id) throws Exception {
			boolean status = false;
			do {
				System.out.println("\n\t\t\t==== Modify User ====\n");
				System.out.println("1.Modify Username");
				System.out.println("2.Modify Email");
				System.out.println("3.Modify Phone");
				System.out.println("4.Modify Gender");
				System.out.println("5.Modify Address");
				System.out.println("6.Modify Birth Date");
				System.out.println("7.Modify User Position");
				System.out.println("8.Modify User Product");
				System.out.println("9.Exit");
				System.out.print("\nEnter Menu Option <1-9>:");
				int option = input.nextInt();
				switch(option) {
					case 1:
						this.modifyUsername(id);
					break;
					case 2:
						this.modifyEmail(id);
					break;
					case 3:
						this.modifyPhone(id);
					break;
					case 4:
						this.modifyGender(id);
					break;
					case 5:
						this.modifyAddress(id);
					break;
					case 6:
						this.modifyUserBirthDate(id);
					break;
					case 7:
						this.modifyPosition(id);
					break;
					case 8:
						this.modifyUserProduct(id);
					break;
					case 9:
						System.out.println("You have been exited!");
						this.menu();
					break;
					default:
						System.out.println(">>Invalid Input Option!");
						status = true;
					break;
				}
			}while(status == true);
		}
		// modify user date of birth
		private void modifyUserBirthDate(int id) throws Exception {
			System.out.println("User ID: "+id);
			String status = "";
			if(mySqlCon != null) {
				java.sql.Statement stmt = conn.createStatement();
				PreparedStatement updateQuery=conn.prepareStatement("update users set dob=? where id=?"); 
				ResultSet user = stmt.executeQuery("SELECT dob FROM users WHERE id='"+id+"'");
				while(user.next()) {
					System.out.print("\nUser Date Of Birth : "+user.getString(1));
				}
				System.out.print("\nEnter New Birth Day<1-30>:");
				int day = input.nextInt();
				System.out.print("\nEnter New Birth Month<1-12>:");
				int month = input.nextInt();
				System.out.print("\nEnter New Birth Year:");
				int year = input.nextInt();
				String date = year+"-"+month+"-"+day;
				updateQuery.setString(1,date);//1 specifies the first parameter in the query  
				updateQuery.setInt(2,id);  
				updateQuery.executeUpdate();
				System.out.println(">>User have been updated successfull!");
				System.out.print(">>>Do you want to edit any users? <Y/N>:");
				status = input.next();
				if(status.equals("Y") || status.equals("y")) {
					this.modifyUser();
				}else {
					this.menu();
				}
			}
			
		}
		private void modifyUserProduct(int id) throws Exception {
			System.out.println("User ID: "+id);
			String status = "";
			boolean exists = false;
			int index = 1;
			if(mySqlCon != null) {
				java.sql.Statement stmt = conn.createStatement();
				java.sql.Statement stmtExist = conn.createStatement();
				java.sql.Statement stmtUpro = conn.createStatement();
				ResultSet products = stmt.executeQuery("SELECT id,pro_name FROM products");
				ResultSet userProducts = stmtUpro.executeQuery("SELECT pro.id,pro.pro_name FROM  user_products AS upro "
						+ "INNER JOIN products AS pro ON pro.id = upro.product_id "
						+ "WHERE upro.user_id="+(id)+ " GROUP BY upro.user_id");
				ResultSet userProductsExist = stmtExist.executeQuery("SELECT EXISTS(SELECT * from user_products WHERE user_id="+(id)+" GROUP BY user_id)");
				while(userProductsExist.next()) {
					exists = userProductsExist.getBoolean(1);
				}
				System.out.println("User Products::");
				while(userProducts.next()) {
					System.out.println((index++) +"." +userProducts.getString(2));
				}
				System.out.println("\nProducts Name::");
				while(products.next()) {
					System.out.println(products.getInt(1)+"."+products.getString(2));
				}
				System.out.print("\nChoose Product ID : ");
				int productId = input.nextInt();
				if(exists == false) {
					stmt.executeUpdate("INSERT INTO user_products(product_id,user_id) VALUES('"+productId+"','"+id+"')");
				}else {
					stmt.executeUpdate("UPDATE user_products SET product_id='"+productId+"' WHERE user_id='"+id+"'");
				}
				
				System.out.println("User have been updated successfull!");
				System.out.print(">>>Do you want to edit any users? <Y/N>:");
				status = input.next();
				if(status.equals("Y") || status.equals("y")) {
					this.modifyUser();
				}else {
					this.menu();
				}
			}
		}
		private void modifyPosition(int id) throws Exception {
			System.out.println("User ID: "+id);
			String status = "";
			if(mySqlCon != null) {
				java.sql.Statement stmt = conn.createStatement();
				ResultSet user = stmt.executeQuery("SELECT positions.name FROM users LEFT JOIN positions ON users.position_id = positions.id  WHERE users.id='"+id+"'");
				while(user.next()) {
					System.out.print("\nUser Position : "+user.getString(1));
				}
				System.out.println("\nPosition Name:: ");
				System.out.println("\t1.Manager");
				System.out.println("\t2.Admin");
				System.out.print("\nEnter New Position<1-2>:");
				int positionId = input.nextInt();
				stmt.executeUpdate("UPDATE users SET position_id='"+positionId+"' WHERE id='"+id+"'");
				System.out.println("\nUser have been updated successfull!");
				System.out.print(">>>Do you want to edit any users? <Y/N>:");
				status = input.next();
				if(status.equals("Y") || status.equals("y")) {
					this.modifyUser();
				}else {
					this.menu();
				}
			}
			
		}
		private void modifyAddress(int id) throws Exception {
			System.out.println("User ID: "+id);
			String status = "";
			if(mySqlCon != null) {
				java.sql.Statement stmt = conn.createStatement();
				ResultSet user = stmt.executeQuery("SELECT address FROM users WHERE id='"+id+"'");
				while(user.next()) {
					System.out.print("\nUser Address : "+user.getString(1));
				}
				System.out.print("\nEnter New Address:");
				String address = buffered.readLine();
				stmt.executeUpdate("UPDATE users SET address='"+address+"' WHERE id='"+id+"'");
				System.out.println("User have been updated successfull!");
				System.out.print(">>>Do you want to edit any users? <Y/N>:");
				status = input.next();
				if(status.equals("Y") || status.equals("y")) {
					this.modifyUser();
				}else {
					this.menu();
				}
			}
			
		}
		private void modifyGender(int id) throws Exception {
			System.out.println("User ID: "+id);
			String status = "";
			if(mySqlCon != null) {
				java.sql.Statement stmt = conn.createStatement();
				ResultSet user = stmt.executeQuery("SELECT gender FROM users WHERE id='"+id+"'");
				while(user.next()) {
					System.out.print("\nUser Gender : "+user.getString(1));
				}
				System.out.print("\nEnter New Gender:");
				String gender = buffered.readLine();
				stmt.executeUpdate("UPDATE users SET gender='"+gender+"' WHERE id='"+id+"'");
				System.out.println("User have been updated successfull!");
				System.out.print(">>>Do you want to edit any users? <Y/N>:");
				status = input.next();
				if(status.equals("Y") || status.equals("y")) {
					this.modifyUser();
				}else {
					this.menu();
				}
			}
		}
		private void modifyPhone(int id) throws Exception {
			System.out.println("User ID: "+id);
			String status = "";
			if(mySqlCon != null) {
				java.sql.Statement stmt = conn.createStatement();
				ResultSet user = stmt.executeQuery("SELECT phone FROM users WHERE id='"+id+"'");
				while(user.next()) {
					System.out.print("\nUser Phone : "+user.getString(1));
				}
				System.out.print("\nEnter New Phone:");
				String phone = buffered.readLine();
				stmt.executeUpdate("UPDATE users SET phone='"+phone+"' WHERE id='"+id+"'");
				System.out.println("User have been updated successfull!");
				System.out.print(">>>Do you want to edit any users? <Y/N>:");
				status = input.next();
				if(status.equals("Y") || status.equals("y")) {
					this.modifyUser();
				}else {
					this.menu();
				}
			}
			
		}
		
		private void modifyEmail(int id) throws Exception {
			System.out.println("User ID: "+id);
			String status = "";
			if(mySqlCon != null) {
				java.sql.Statement stmt = conn.createStatement();
				ResultSet user = stmt.executeQuery("SELECT email FROM users WHERE id='"+id+"'");
				while(user.next()) {
					System.out.print("\nUser Email : "+user.getString(1));
				}
				System.out.print("\nEnter New Email:");
				String email = buffered.readLine();
				stmt.executeUpdate("UPDATE users SET email='"+email+"' WHERE id='"+id+"'");
				System.out.println("User have been updated successfull!");
				System.out.print(">>>Do you want to edit any users? <Y/N>:");
				status = input.next();
				if(status.equals("Y") || status.equals("y")) {
					this.modifyUser();
				}else {
					this.menu();
				}
			}
		}
		
		private void modifyUsername(int id) throws Exception {
			String status = "";
			System.out.println("User ID: "+id);
			if(mySqlCon != null) {
				java.sql.Statement stmt = conn.createStatement();
				ResultSet user = stmt.executeQuery("SELECT username FROM users WHERE id='"+id+"'");
				while(user.next()) {
					System.out.print("\nUsername : "+user.getString(1));
				}
				System.out.print("\nEnter New Username:");
				String username = buffered.readLine();
				stmt.executeUpdate("UPDATE users SET username='"+username+"' WHERE id='"+id+"'");
				System.out.println("User have been updated successfull!");
				System.out.print(">>>Do you want to edit any users? <Y/N>:");
				status = input.next();
				if(status.equals("Y") || status.equals("y")) {
					this.modifyUser();
				}else {
					this.menu();
				}
			}
		}
}
