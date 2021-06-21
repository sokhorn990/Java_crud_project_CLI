package categorymanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import dbconnection.Connector;

public class AddCategory {
	
	private static  PreparedStatement myStmt;
	
	public static void AddRecord() throws Exception {
		 Scanner strInput = new Scanner(System.in);
		 String status = "";
		do {
			Connector mySqlCon = new Connector();
			Connection conn = mySqlCon.dbConnection();
			
		    String sql = "INSERT INTO categories (cat_name) values (?)";
			
			String name;
			
			System.out.print("\nEnter the Category Name: ");
			name = strInput.next();	
			
			myStmt = conn.prepareStatement(sql);
			// set param values
			myStmt.setString(1, name);
			if(myStmt.executeUpdate() !=0) {
				System.out.println("Category have been saved successfully!");
			}else {
				System.out.println("Saving have been fail!");
			}
			
			
			System.out.print("\nDo you want to add any categories? <Y/N>:");
			status = strInput.next();
		}while(status.equals("Y") || status.equals("y"));
		Category menu = new Category();
		menu.category();
	}
	
}
