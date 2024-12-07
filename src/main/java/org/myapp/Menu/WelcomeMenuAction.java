package org.myapp.Menu;

import org.myapp.Model.Customer;
import org.myapp.Model.Manager;

import java.util.Scanner;

import static org.myapp.Menu.Utility.isValidEmailFormat;

public class WelcomeMenuAction{
    private final Scanner scanner;

    WelcomeMenuAction(){
        this.scanner = new Scanner(System.in);
    }


    public void logIn() {
        System.out.print("\nUsername: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Log in as customer/manager: ");
        String role = scanner.nextLine().trim().toLowerCase();

        while (!role.equals("customer") && !role.equals("manager")){
            System.out.println("Type 'customer' or 'manager'. Try again.");
            System.out.print("Log in as customer/manager: ");
            role = scanner.nextLine().trim().toLowerCase();
        }

        if (role.equals("customer")) {
            Customer customer = new Customer();
            if (customer.customerIsExist(username) && customer.customerPasswordValid(username, password)) {
                System.out.println("Welcome Customer");
                new CustomerMenu(customer.customerLogIn(username)).execute();
            } else {
                System.out.println("Invalid username or password");
            }
        }
        else {
            Manager manager = new Manager();
            if(manager.managerIsExist(username) && manager.managerPasswordValid(username, password)){
                System.out.println("Welcome Manager");
                new ManagerMenu(manager.managerLogIn(username)).execute();
            }
            else {
                System.out.println("Invalid username or password");
            }
        }
    }

    public void signUp() {
        String key;
        String secretKey = "addMoreManager";
        System.out.print("\nEnter username: ");
        String username = scanner.nextLine().trim();
        Customer customer = new Customer();
        Manager manager = new Manager();
        if (customer.customerIsExist(username) || manager.managerIsExist(username)){
            System.out.println("Username already exist.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        while (!isValidEmailFormat(email)){
            System.out.println("Email is not valid");
            email = scanner.nextLine().trim();
        }
        System.out.println("Type secret key for manager, ");
        System.out.println("Leave blank for customer,");
        System.out.print("Or -1 to cancel sign up: ");
        key = scanner.nextLine().trim();
        while (!key.equals(secretKey) && !key.isEmpty() && !key.equals("-1") ){
            System.out.println("Try again");
            key = scanner.nextLine().trim();
        }
        if (key.isEmpty()){
            if (customer.customerSignUp(username, password, fullName, phoneNumber, email)){
                System.out.println("Customer account created successfully!");
            }
            else {
                System.out.println("Something went wrong. Try again.");
            }

        }
        else if (key.equals(secretKey)){
            if (manager.managerSignUp(username, password, fullName, phoneNumber, email)){
                System.out.println("Manager account created successfully!");
            }
            else {
                System.out.println("Something went wrong. Try again.");
            }
        }
        else{
            System.out.println("You just cancel sign up.");
        }
    }
}