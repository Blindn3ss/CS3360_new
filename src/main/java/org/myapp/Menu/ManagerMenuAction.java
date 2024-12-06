package org.myapp.Menu;

import org.myapp.Model.Manager;
import org.myapp.Model.Yard;

import java.util.InputMismatchException;
import java.util.Scanner;

import static org.myapp.Menu.Utility.isValidEmailFormat;

public class ManagerMenuAction {
    private final Manager loggedInManager;
    private final Scanner scanner;

    ManagerMenuAction(Manager loggedInManager) {
        this.loggedInManager = loggedInManager;
        this.scanner = new Scanner(System.in);
    }


    public void viewProfile() {
        loggedInManager.viewProfileDetails();
    }

    public void editProfile() {
        SubMenu editProfile = new SubMenu("Edit Your Profile");
        editProfile.addMenuItem(new ActionMenuItem("Change Full Name", ()->{
            String fullName;
            System.out.println("Enter your new full name: ");
            fullName = scanner.nextLine().trim();
            while(fullName.isEmpty()){
                System.out.println("Your Full Name should not be empty");
                fullName = scanner.nextLine().trim();
            }
            loggedInManager.setFullName(fullName);
            if (loggedInManager.updateProfile(loggedInManager)){
                System.out.println("Your full name has been successfully updated to: " + fullName);
            }
            else {
                System.out.println("Something went wrong.");
            }
        }));

        editProfile.addMenuItem(new ActionMenuItem("Change Email", ()->{
            String email;
            System.out.println("Enter your new email: ");
            email = scanner.nextLine().trim();
            while(email.isEmpty()){
                System.out.println("Your email should not be empty");
                email = scanner.nextLine().trim();
            }
            while(!isValidEmailFormat(email)){
                System.out.println("Your email is invalid");
                email = scanner.nextLine().trim();
            }
            loggedInManager.setEmail(email);
            if (loggedInManager.updateProfile(loggedInManager)){
                System.out.println("Your email has been successfully updated to: " + email);
            }
            else {
                System.out.println("Something went wrong.");
            }
        }));
        editProfile.addMenuItem(new ActionMenuItem("Change Phone Number", ()->{
            String phoneNumber;
            System.out.println("Enter your new phone number:");
            phoneNumber = scanner.nextLine().trim();
            while (phoneNumber.isEmpty()){
                System.out.println("Your phone number should not be empty");
                phoneNumber = scanner.nextLine().trim();
            }
            loggedInManager.setPhoneNumber(phoneNumber);
            if (loggedInManager.updateProfile(loggedInManager)){
                System.out.println("Your phone number has been successfully updated to: " + phoneNumber);
            }
            else {
                System.out.println("Something went wrong.");
            }
        }));
        editProfile.addMenuItem(new ActionMenuItem("Change password", ()->{
            String password;
            System.out.println("Enter your new password");
            password = scanner.nextLine().trim();
            while (password.isEmpty()){
                System.out.println("Please enter your new password");
                password = scanner.nextLine().trim();
            }
            loggedInManager.setPassword(password);
            if (loggedInManager.updateProfile(loggedInManager)){
                System.out.println("Your password has been successfully updated");
            }
            else {
                System.out.println("Something went wrong.");
            }
        }));
        editProfile.addMenuItem(new ActionMenuItem("Back", ()->{}));
        editProfile.execute();
    }

    public void addNewYard() {
        Scanner scanner = new Scanner(System.in); // Create Scanner object to read input

        Yard yard = new Yard();

        System.out.println("Enter the name of the yard: ");
        String yardName = scanner.nextLine().trim();

        System.out.println("Enter the location of the yard: ");
        String yardLocation = scanner.nextLine().trim();

        int yardCapacity = 0;
        boolean validCapacity = false;
        while (!validCapacity) {
            try {
                System.out.println("Enter the capacity of the yard: ");
                yardCapacity = scanner.nextInt(); // Read the capacity
                scanner.nextLine(); // Consume the leftover newline
                validCapacity = true; // If no exception, break the loop
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid integer for the yard capacity.");
                scanner.nextLine(); // Clear the buffer
            }
        }

        // Read the surface type
        System.out.println("Enter the surface type of the yard (e.g., grass, turf): ");
        String surfaceType = scanner.nextLine().trim();

        double pricePerDay = 0.0;
        boolean validPrice = false;
        while (!validPrice) {
            try {
                System.out.println("Enter the price per day: ");
                pricePerDay = scanner.nextDouble();
                scanner.nextLine();
                validPrice = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number for the price per day.");
                scanner.nextLine();
            }
        }

        System.out.println("Enter a description of the yard: ");
        String description = scanner.nextLine().trim();

        yard.setYardName(yardName);
        yard.setYardLocation(yardLocation);
        yard.setYardCapacity(yardCapacity);
        yard.setSurfaceType(surfaceType);
        yard.setPricePerDay(pricePerDay);
        yard.setDescription(description);

        System.out.println("New yard added: " + yard);

        scanner.close();
    }

    public void editManagedYards() {
    }

    public void registerToManage() {
    }

    public void viewScheduleAndBookings() {
    }

    public void viewRevenue() {

    }
}


