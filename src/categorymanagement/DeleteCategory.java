package categorymanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import dbconnection.Connector;

public class DeleteCategory {
	
	private static PreparedStatement myStmt;
	
	public static void deleteRecord() throws Exception {
		Scanner strInput = new Scanner(System.in);
		String status = "";
		do {
			Connector mySqlCon = new Connector();
			Connection conn = mySqlCon.dbConnection();		
			try {
				System.out.println(" \t ==DELETE THE CATEGORY RECORD== \n");
				System.out.println(" =================================="); 
				
				int ID;
				
				System.out.print("Enter the Category ID: ");
				ID = strInput.nextInt();
				
				String sql="DELETE FROM categories where id=?";
				myStmt = conn.prepareStatement(sql);
				myStmt.setInt(1, ID);
				if(myStmt.executeUpdate() !=0) {
					System.out.println("Category have been deleted success!");
				}else {
					System.out.println("Category can't delete!!");
				}
				conn.close();
				myStmt.close();
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
			System.out.print("\nDo you want to delete any categories? <Y/N>:");
			status = strInput.next();
		}while(status.equals("Y") || status.equals("y"));
		Category menu = new Category();
		menu.category();
	}

}
