package categorymanagement;

import java.sql.SQLException;
import java.util.Scanner;

import usersmanagement.UserMenu;

public class Category {

	public void category() throws Exception {
		Scanner strInput = new Scanner(System.in);
        String choice,cont = "y";        
        
        	System.out.println();
        	System.out.println("\t=====================================");
        	System.out.println("\t\tCategories Managements ");
        	System.out.println("\t======================================\n");
        
	        System.out.println("1. Add New Category Record ");
	        System.out.println("2. View All Category Record ");	
	        System.out.println("3. Delete Category Record ");
	        System.out.println("4. Find Category Record ");
	        System.out.println("5. Update Category Record");
	        System.out.println("6. Exit \n");
	        System.out.print(">>\nEnter Option<1-6>:");
	    
	        choice = strInput.nextLine();
	        System.out.println();
	        if( choice.equals("1") ) {
	        	AddCategory.AddRecord();
	        } 
	        else if( choice.equals("2") ) {
	        	ViewCategory.viewRecord();
	        } 
	        else if( choice.equals("3") ) {
	        	DeleteCategory.deleteRecord();
	        } 
	        else if( choice.equals("4") ) {
	        	FindCategory.findRecord();
	        } 
	        else if( choice.equals("5") ) {
	        	UpdateCategory.updateRecord();
	        } 
	        else if( choice.equals("6") ) {
	        	UserMenu userMenu = new UserMenu();
	        	userMenu.managerMenu();
	        } 
		        	
	}

}
