package usersmanagement;

import java.sql.SQLException;
import java.util.Scanner;

import categorymanagement.AdminCategory;
import categorymanagement.Category;
import product.ManagerProductClass;
import product.ProductManagement;

public class UserMenu {
	Scanner input = new Scanner(System.in);
	int menuOption =0;
	boolean status = false;
	
	// manager menu after login
	public void managerMenu() throws Exception {
		do {
			System.out.println("\t=========================");
			System.out.println("\t\tMain Menu");
			System.out.println("\t=========================\n");
			System.out.println("1.Users");
			System.out.println("2.Categories");
			System.out.println("3.Products");
			System.out.println("4.Logout");
			System.out.print("\nEnter Menu Option <1-4>:");
			menuOption = input.nextInt();
			switch(menuOption) {
				case 1:
					UserCRUDMenu manager = new UserCRUDMenu();
					manager.menu();
				break;
				case 2:
					Category category = new Category();
					category.category();
				break;
				case 3:
					ManagerProductClass product = new ManagerProductClass();
					product.showMenu();
				break;
				case 4:
					UserLogout logout = new UserLogout();
					logout.logout();
				break;
				default:
					System.out.println("Invalid Input!");
					status = true;
			}
		}while(status == true);
	}
	
	// admin menu after login
	public void adminMenu() throws Exception {
		do {
			System.out.println("\t=========================");
			System.out.println("\t\tMain Menu");
			System.out.println("\t=========================\n");
			System.out.println("1.Users");
			System.out.println("2.Categories");
			System.out.println("3.Products");
			System.out.println("4.Logout");
			System.out.print("\nEnter Menu Option <1-4>:");
			menuOption = input.nextInt();
			switch(menuOption) {
				case 1:
					AdminCRUDMenu admin = new AdminCRUDMenu();
					admin.menu();
				break;
				case 2:
					AdminCategory category = new AdminCategory();
					category.category();
				break;
				case 3:
					ProductManagement product = new ProductManagement();
					product.showMenu();
				break;
				case 4:
					UserLogout logout = new UserLogout();
					logout.logout();
				break;
				default:
					System.out.println("Invalid Input!");
					status = true;
			}
		}while(status == true);
	}
}
