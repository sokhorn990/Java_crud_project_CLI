package usersmanagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import dbconnection.Connector;

public class UserLogout {
	public void logout() throws Exception {
		UserLogin loginPage = new UserLogin();
		Connector mySqlCon = new Connector();
		Connection conn = mySqlCon.dbConnection();
		if(mySqlCon != null) {
			java.sql.Statement stmtLogout = conn.createStatement();
			String logout= "UPDATE users SET login_status='0'";
			stmtLogout.executeUpdate(logout);
			System.out.println("You have been logout successfully!");
			loginPage.login();
		}
	}
}
