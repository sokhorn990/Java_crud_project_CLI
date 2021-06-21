package categorymanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import dbconnection.Connector;

public class FindCategoryAdmin {
	
	private static PreparedStatement myStmt;

	public static void findRecord() throws Exception {
		Scanner strInput = new Scanner(System.in);
		String status = "";
		do {
			try {
				Connector mySqlCon = new Connector();
				Connection conn = mySqlCon.dbConnection();
				int ID;
				System.out.print("Enter the Category ID: ");
				ID = strInput.nextInt();
				String sql = "SELECT * FROM categories WHERE id=?";	
				myStmt = conn.prepareStatement(sql);
				myStmt.setInt(1, ID);
				ResultSet rs = myStmt.executeQuery();
				
				rs = myStmt.executeQuery();
				
				System.out.println(" \t ==LIST OF EACH CATEGORY RECORD== \n");
				System.out.println(" ==================================");
				System.out.println("| ID	    Name    ");
				System.out.println(" =================================="); 
				
				while(rs.next()) {
					System.out.print("  "+rs.getInt("id")+ "          "); 
					System.out.print(rs.getString("cat_name"));
				}
				System.out.println();
				System.out.println(" ==================================");
				System.out.println();
				conn.close();
				myStmt.close();
					
				}catch(SQLException e) {
					e.printStackTrace();
				}
			System.out.print("\nDo you want to find any categories? <Y/N>:");
			status = strInput.next();
		}while(status.equals("Y") || status.equals("y"));
		AdminCategory menu = new AdminCategory();
		menu.category();
	}

}
