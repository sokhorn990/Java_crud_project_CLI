package usersmanagement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import dbconnection.Connector;

public class AdminCRUDMenu {
	Scanner input = new Scanner(System.in);
	InputStreamReader read=new InputStreamReader(System.in);    
    BufferedReader buffered=new BufferedReader(read); 
	Connector mySqlCon = new Connector();
	Connection conn = mySqlCon.dbConnection();
	UserCRUDMenu tableHeader = new UserCRUDMenu();
	// show all users
		public void showAllUser() throws Exception {
			String status = "";
			int index = 1;
			if(mySqlCon != null) {
				java.sql.Statement getPosition = conn.createStatement();
				ResultSet users = getPosition.executeQuery("SELECT * FROM users LEFT JOIN positions ON users.position_id = positions.id ");

				System.out.println("\n\t\t\t==== Show All Users ====\n");
				tableHeader.tableHeader();
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
		// user menu
		public void menu() throws Exception {
			boolean status = false;
			do {
				System.out.println("\t=========================");
				System.out.println("\t\tMenu Users");
				System.out.println("\t=========================\n");
				System.out.println("1. Search User");
				System.out.println("2. Show All Users");
				System.out.println("3. Exit");
				System.out.print("\nEnter menu option <1-3>:");
				int option = input.nextInt();
				switch(option) {
					case 1:
						this.searchUser();
					break;
					case 2:
						this.showAllUser();
					break;
					case 3:
						System.out.println("You have been exited!");
						UserMenu userMenu = new UserMenu();
						userMenu.managerMenu();
					break;
					default:
						System.out.println("Invalid Input!");
						status = true;
						break;
				}
			}while(status == true);
		}
		private void searchUser() throws Exception {
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
					tableHeader.tableHeader();
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
						tableHeader.tableHeader();
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
}
