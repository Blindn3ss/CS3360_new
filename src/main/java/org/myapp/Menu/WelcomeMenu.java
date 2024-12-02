package org.myapp.Menu;

import org.myapp.DAO.CustomerDAOImpl;
import org.myapp.DAO.ManagerDAOImpl;
import org.myapp.Database.Database;
import org.myapp.Model.Customer;
import org.myapp.Model.Manager;

import java.sql.*;
import java.util.Scanner;

import static org.myapp.Menu.Utility.doesUsernameExist;
import static org.myapp.Menu.Utility.isValidEmail;


public class WelcomeMenu {
    private final Scanner scanner;

    public WelcomeMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void execute() {
        SubMenu welcomeMenu = new SubMenu("Welcome Menu");
        welcomeMenu.addMenuItem(new ActionMenuItem("Log In", this::logIn));
        welcomeMenu.addMenuItem(new ActionMenuItem("Sign Up", () -> {
            try {
                signUp();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        welcomeMenu.addMenuItem(new ActionMenuItem("Exit", this::exitApp));

        welcomeMenu.execute();
    }

    private void logIn() {
        System.out.print("\nUsername: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Customer customer = CustomerDAOImpl.getInstance().getCustomerByUsername(username);
        if (customer != null){
            if (customer.getPassword().equals(password)){
                System.out.println("Welcome Customer");
                new CustomerMenu(customer, scanner).execute();
            }
            else {
                System.out.println("Invalid username or password");
            }
        }
        else{
            ManagerDAOImpl managerDAO = new ManagerDAOImpl();
            Manager manager = managerDAO.getManagerByUserName(username);
            if (manager != null){
                if (manager.getPassword().equals(password)) {
                    System.out.println("Welcome Manager");
                    new ManagerMenu(manager, scanner).execute();
                }
                else{
                    System.out.println("Invalid username or password");
                }
            }
            else {
                System.out.println("Account doesn't exist.");
                System.out.println("Please try again or sign up a new account.");
            }
        }


    }


    private void signUp() throws SQLException {
        String key;
        String secretKey = "addMoreManager";
        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();
        if (doesUsernameExist(username)) {
            System.out.println("Username already exists.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        while (!isValidEmail(email)){
            System.out.println("Email is not valid");
            email = scanner.nextLine();
        }
        System.out.println("Type secret key for manager, ");
        System.out.println("Leave blank for customer,");
        System.out.print("Or -1 to cancel sign up: ");
        key = scanner.nextLine();
        while (!key.equals(secretKey) && !key.isEmpty() && !key.equals("-1") ){
            System.out.println("Try again");
            key = scanner.nextLine();
        }
        if (key.isEmpty()){
            Customer customer = new Customer(-1, username, password, fullName, phoneNumber, email);
            CustomerDAOImpl.getInstance().createCustomer(customer);
            System.out.println("Customer account created successfully!");
        }
        else if (key.equals(secretKey)){
            Manager manager = new Manager(-1, username, password, fullName, phoneNumber, email);
            ManagerDAOImpl.getInstance().createManager(manager);
            System.out.println("Manager account created successfully!");
        }
        else{
            System.out.println("You just cancel sign up.");
        }
    }


    private void exitApp() {
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
