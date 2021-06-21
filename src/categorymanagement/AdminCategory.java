package categorymanagement;

import java.sql.SQLException;
import java.util.Scanner;

import usersmanagement.UserMenu;
public class AdminCategory {
	
	public void category() throws Exception {
		Scanner strInput = new Scanner(System.in);
        String choice,cont = "y";        
        	System.out.println();
        	System.out.println("\t=====================================");
        	System.out.println("\t\tCategories Managements ");
        	System.out.println("\t======================================\n");
        
	        System.out.println("1. View All Category Record ");
	        System.out.println("2. Find Category Record ");	
	        System.out.println("3. Exit ");	
	        System.out.print(">>\nEnter Option<1-3>:");
	    
	        choice = strInput.nextLine();
	        System.out.println();
	        if( choice.equals("1") ) {
	        	ViewCategoryAdmin.viewRecord();
	        } 
	        else if( choice.equals("2") ) {
	        	FindCategoryAdmin.findRecord();
	        	
	        }else if(choice.equals("3")) {
	        	UserMenu userMenu = new UserMenu();
	        	userMenu.managerMenu();
	        }else {
	        	System.out.println(">>\nInvalid input!");
	        }
       	
		
	}
}
