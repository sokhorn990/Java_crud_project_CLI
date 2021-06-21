package categorymanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import dbconnection.Connector;

public class UpdateCategory {

	public static PreparedStatement myStmt;
	public static void updateRecord() throws Exception {
		 Scanner strInput = new Scanner(System.in);
		 String status = "";
		do {
			Connector mySqlCon = new Connector();
			Connection conn = mySqlCon.dbConnection();		
			 System.out.println(" \t ==UPDATE THE EACH CATEGORY RECORD== \n");
			 System.out.println(" ==================================");
			 
			 String sql1 = "UPDATE categories SET cat_name=? WHERE id=?";
			 String ID,name;
				
			 System.out.print("Enter the New Category ID: ");
			 ID = strInput.nextLine();
			 System.out.print("Enter the New Category Name: ");
			 name = strInput.nextLine();	
		
			try {
				myStmt = conn.prepareStatement(sql1);
				myStmt.setString(1,name);
				myStmt.setString(2, ID);
				
				if(myStmt.executeUpdate() !=0) {
					System.out.println("Category have been saved Updated!");
				}else {
					System.out.println("Category can't Update!!");
				}
				
				conn.close();
				myStmt.close();
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			System.out.print("\nDo you want to update any categories? <Y/N>:");
			status = strInput.next();
		}while(status.equals("Y") || status.equals("y"));
		Category menu = new Category();
		menu.category();
	}

}
