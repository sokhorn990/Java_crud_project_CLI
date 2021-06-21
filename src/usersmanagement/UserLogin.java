package usersmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import dbconnection.Connector;

public class UserLogin {

	public void login() throws Exception {
		Scanner input = new Scanner(System.in);
		Connector mySqlCon = new Connector();
		Connection conn = mySqlCon.dbConnection();
		String username = "";
		String password = "";
		int userPositionID = 0;
		String email = "";
		String pwdInput;
		String userNameInput;
		boolean status = false;
		if(mySqlCon != null) {
			java.sql.Statement stmt = conn.createStatement();
			java.sql.Statement stmtUser = conn.createStatement();
			java.sql.Statement update = conn.createStatement();
			do {
				System.out.println("\t=== LOGIN ===\n");
				System.out.print("Username: ");
				username = input.nextLine();
				System.out.print("Password: ");
				password = input.nextLine();
				ResultSet pwd = stmt.executeQuery("SELECT * FROM users WHERE password='"+password+"'");
				ResultSet user = stmtUser.executeQuery("SELECT * FROM users WHERE username='"+username+"' "
						+ "OR email='"+username+"'");
				if(pwd.next() && user.next()) {
					ResultSet users = stmt.executeQuery("SELECT * FROM users WHERE password='"+password+"' AND username='"+username+"'"
							+ " OR email='"+username+"'");
					String queryUpdate = "UPDATE users SET login_status='1' WHERE password='"+password+"' AND username='"+username+"'"
							+ " OR email='"+username+"'";
					update.executeUpdate(queryUpdate);
					while(users.next()) {
						userPositionID = users.getInt(10);
					}
					status = true;
					System.out.println("Login Successfully!");
				}else if(pwd.next() == false && user.next()) {
					status = false;
					System.out.println("Password invalid!");
				}else {
					status = false;
					System.out.println("Username and Password invalid!");
				}
				System.out.println();
			}while(status != true);
			UserMenu menu = new UserMenu();
			if(userPositionID == 2) {
				menu.adminMenu();
			}else {
				menu.managerMenu();
			}

		}else {
			System.out.println("Connection is fail!");
		}
	}
	
}
