package categorymanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import dbconnection.Connector;
import usersmanagement.UserMenu;

public class ViewCategoryAdmin {

	private static PreparedStatement myStmt;
	
	public static void viewRecord() throws Exception {
		Connector mySqlCon = new Connector();
		Connection conn = mySqlCon.dbConnection();
		 
	    String sql = "SELECT * FROM categories";
	    Scanner strInput = new Scanner(System.in);
		myStmt = conn.prepareStatement(sql);
		ResultSet rs = myStmt.executeQuery();
		
		System.out.println(" \t ==LIST OF ALL CATEGORY RECORD== \n");
		System.out.println(" ==================================");
		System.out.println("| ID	    Name    ");
		System.out.println(" ==================================");
		
		while(rs.next()) {
			System.out.print(" " + rs.getInt("id") + "         "); 
			System.out.println(rs.getString("cat_name"));
		}
		System.out.println();
		System.out.println(" ==================================");
		System.out.println();
		conn.close();
		myStmt.close();
		rs.close();
		System.out.println("\nPress enter back to menu ......");
		try{System.in.read();}catch(Exception e){	e.printStackTrace();}
		AdminCategory userMenu = new AdminCategory();
    	userMenu.category();
	}

}
