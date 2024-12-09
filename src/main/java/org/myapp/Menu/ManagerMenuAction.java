package org.myapp.Menu;

import org.myapp.Model.*;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static org.myapp.Menu.Utility.isValidDateFormat;
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
        editProfile.addMenuItem(new ActionMenuItem("Change Full Name", () -> {
            String fullName;
            System.out.println("Enter your new full name: ");
            fullName = scanner.nextLine().trim();
            while (fullName.isEmpty()) {
                System.out.println("Your Full Name should not be empty");
                fullName = scanner.nextLine().trim();
            }
            loggedInManager.setFullName(fullName);
            if (loggedInManager.updateProfile(loggedInManager)) {
                System.out.println("Your full name has been successfully updated to: " + fullName);
            } else {
                System.out.println("Something went wrong.");
            }
        }));

        editProfile.addMenuItem(new ActionMenuItem("Change Email", () -> {
            String email;
            System.out.println("Enter your new email: ");
            email = scanner.nextLine().trim();
            while (email.isEmpty()) {
                System.out.println("Your email should not be empty");
                email = scanner.nextLine().trim();
            }
            while (!isValidEmailFormat(email)) {
                System.out.println("Your email is invalid");
                email = scanner.nextLine().trim();
            }
            loggedInManager.setEmail(email);
            if (loggedInManager.updateProfile(loggedInManager)) {
                System.out.println("Your email has been successfully updated to: " + email);
            } else {
                System.out.println("Something went wrong.");
            }
        }));
        editProfile.addMenuItem(new ActionMenuItem("Change Phone Number", () -> {
            String phoneNumber;
            System.out.println("Enter your new phone number:");
            phoneNumber = scanner.nextLine().trim();
            while (phoneNumber.isEmpty()) {
                System.out.println("Your phone number should not be empty");
                phoneNumber = scanner.nextLine().trim();
            }
            loggedInManager.setPhoneNumber(phoneNumber);
            if (loggedInManager.updateProfile(loggedInManager)) {
                System.out.println("Your phone number has been successfully updated to: " + phoneNumber);
            } else {
                System.out.println("Something went wrong.");
            }
        }));
        editProfile.addMenuItem(new ActionMenuItem("Change password", () -> {
            String password;
            System.out.println("Enter your new password");
            password = scanner.nextLine().trim();
            while (password.isEmpty()) {
                System.out.println("Please enter your new password");
                password = scanner.nextLine().trim();
            }
            loggedInManager.setPassword(password);
            if (loggedInManager.updateProfile(loggedInManager)) {
                System.out.println("Your password has been successfully updated");
            } else {
                System.out.println("Something went wrong.");
            }
        }));
        editProfile.addMenuItem(new ActionMenuItem("Back", () -> {
        }));
        editProfile.execute();
    }

    public void addNewYard() {
        System.out.println("Enter the name of the yard: ");
        String yardName = scanner.nextLine().trim();

        System.out.println("Enter the location of the yard: ");
        String yardLocation = scanner.nextLine().trim();

        int yardCapacity = 0;
        boolean validCapacity = false;
        while (!validCapacity) {
            try {
                System.out.println("Enter the capacity of the yard: ");
                yardCapacity = scanner.nextInt();
                scanner.nextLine();
                validCapacity = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid integer for the yard capacity.");
                scanner.nextLine();
            }
        }

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

        Yard yard = new Yard(0, yardName, yardLocation, yardCapacity, surfaceType, pricePerDay, description);
        yard.yardInfo();

        String confirmation;
        while (true) {
            System.out.print("\n\"Are you sure want to add this yard into database? (y/n)\"");
            confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("y")) {
                if (loggedInManager.addNewYard(yardName, yardLocation, yardCapacity,
                        surfaceType, pricePerDay, description)) {
                    System.out.println("You just add a new Yard to database");
                } else {
                    System.out.println("Something went wrong.");
                }
                break;
            } else if (confirmation.equals("n")) {
                System.out.println("You cancelled adding new yard.");
                break;
            } else {
                System.out.println("Invalid input. Please enter 'y' for Yes or 'n' for No.");
            }
        }
    }

    public void editManagedYards() {
        if (loggedInManager.showCurrentManagedYards()) {
            System.out.println("You have not managed any yard yet.");
            return;
        }
        System.out.print("Type the ID of Yard: ");
        int yardId = scanner.nextInt();
        scanner.nextLine();

        if (!loggedInManager.havePermission(yardId)) {
            System.out.println("You don't manage this yard.");
            System.out.println("Please choose another ID.");
            return;
        }

        SubMenu editManagedYard = new SubMenu("Edit Yard " + yardId);
        editManagedYard.addMenuItem(new ActionMenuItem("Change Yard's Name", () -> {
            String yardName;
            System.out.println("Enter new yard's name: ");
            yardName = scanner.nextLine().trim();
            while (yardName.isEmpty()) {
                System.out.println("Yard's Name should not be empty");
                yardName = scanner.nextLine().trim();
            }
            if (loggedInManager.updateYardName(yardId, yardName)) {
                System.out.println("Name of Yard " + yardId + " has been successfully updated to: " + yardName);
            } else {
                System.out.println("Something went wrong.");
            }
        }));

        editManagedYard.addMenuItem(new ActionMenuItem("Change Yard's Location", () -> {
            String yardLocation;
            System.out.println("Enter new location: ");
            yardLocation = scanner.nextLine().trim();
            while (yardLocation.isEmpty()) {
                System.out.println("Yard's location should not be empty");
                yardLocation = scanner.nextLine().trim();
            }
            if (loggedInManager.updateYardLocation(yardId, yardLocation)) {
                System.out.println("Yard Location has been successfully updated to: " + yardLocation);
            } else {
                System.out.println("Something went wrong.");
            }
        }));
        editManagedYard.addMenuItem(new ActionMenuItem("Change Yard's Capacity", () -> {
            int yardCapacity;
            while (true) {
                System.out.println("Type new capacity of the yard: ");
                if (scanner.hasNextInt()) {
                    yardCapacity = scanner.nextInt();
                    break;
                } else {
                    System.out.println("That's not an integer. Please try again.");
                    scanner.next();
                }
            }

            if (loggedInManager.updateYardCapacity(yardId, yardCapacity)) {
                System.out.println("Capacity of Yard " + yardId + " has been changed successfully!");
            } else {
                System.out.println("Something went wrong.");
            }
        }));
        editManagedYard.addMenuItem(new ActionMenuItem("Change Yard's Surface Type", () -> {
            String surfaceType;
            System.out.println("Type new surface type of the yard: ");
            surfaceType = scanner.nextLine();
            while (surfaceType.isEmpty()) {
                System.out.println("Yard's Surface Type should not be empty");
                surfaceType = scanner.nextLine().trim();
            }
            if (loggedInManager.updateYardSurfaceType(yardId, surfaceType)) {
                System.out.println("Surface Type of Yard " + yardId + " " +
                        "has been successfully updated to: " + surfaceType);
            } else {
                System.out.println("Something went wrong.");
            }
        }));

        editManagedYard.addMenuItem(new ActionMenuItem("Change Yard's Price", () -> {
            double pricePerDay;
            while (true) {
                System.out.println("Type new Price Per Day of the yard: ");
                if (scanner.hasNextDouble()) {
                    pricePerDay = scanner.nextDouble();
                    break;
                } else {
                    System.out.println("That's not a valid price. Please enter a valid number.");
                    scanner.next();
                }
            }

            if (loggedInManager.updateYardPricePerDay(yardId, pricePerDay)) {
                System.out.println("The Price Per Day of Yard " + yardId + " has been updates to " + pricePerDay);
            } else {
                System.out.println("Something went wrong");
            }

        }));

        editManagedYard.addMenuItem(new ActionMenuItem("Back", () -> {
        }));
        editManagedYard.execute();
    }

    public void registerToManage() {
        for (Yard yard : loggedInManager.getListOfUnmanagedYards()){
            System.out.println(yard.yardInfo());
        }
        System.out.println("Above are yards you are not managing.");
        int yardId;
        while (true) {
            System.out.println("Type the yard ID you want to register: ");
            if (scanner.hasNextInt()) {
                yardId = scanner.nextInt();
                break;
            } else {
                System.out.println("That's not a valid ID. Please enter a valid ID.");
                scanner.next();
            }
        }

        if (loggedInManager.registerToManage(yardId)) {
            System.out.println("You successfully registered to manage the yard " + yardId);
        } else {
            System.out.println("Something went wrong. Please try again.");
        }
    }

    public void removeManagement() {
        if (loggedInManager.showCurrentManagedYards()) {
            System.out.println("You have not managed any yard yet!!");
            return;
        }
        int yardId;
        while (true) {
            System.out.println("Type the yard ID you want to remove your management right from: ");
            if (scanner.hasNextInt()) {
                yardId = scanner.nextInt();
                break;
            } else {
                System.out.println("That's not a valid ID. Please enter a valid ID.");
                scanner.next();
            }
        }
        if (loggedInManager.removeManagement(yardId)) {
            System.out.println("You successfully removed the management right from yard " + yardId);
        } else {
            System.out.println("Something went wrong or you are not authorized to remove the management right from this yard.");
        }
    }

    public void viewSchedule() {
        Schedule schedule = new Schedule();
        List<Integer> yardIdList = loggedInManager.getListOfYardId();
        if (yardIdList.isEmpty()){
            System.out.println("It seems like you need to register to manage some yards!");
        }
        else {
            schedule.viewScheduleForManager(yardIdList);
        }

    }

    public void confirmBooking() {
        int yardId;
        String date;
        int bookingId;
        while (true) {
            System.out.print("Type yard ID: ");
            if (scanner.hasNextInt()) {
                yardId = scanner.nextInt();
                if (yardId > 0) {
                    if (!loggedInManager.havePermission(yardId)){
                        System.out.println("You don't have permission on this yard.");
                        return;
                    }
                    break;
                } else if (yardId == 0) {
                    return;
                } else {
                    System.out.println("Please enter a positive integer for yard ID.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer for yard ID.");
                scanner.next();
            }
        }
        scanner.nextLine();
        while(true) {
            System.out.print("Type date (yyyy-mm-dd): ");
            date = scanner.nextLine().trim();
            if (isValidDateFormat(date)) {
                break;
            } else {
                System.out.println("Invalid date format. Please enter the date in yyyy-mm-dd format.");
            }
        }

        Booking booking = new Booking();
        List<Booking> pendings = booking.getPendingBookingOfYardInDate(yardId, date);

        if (pendings.isEmpty()){
            System.out.println("There is no pending booking of yard #" + yardId + " in " + date);
            return;
        }

        System.out.println();
        for (Booking b : pendings) {
            System.out.println(b.viewBooking2());
        }
        System.out.println();

        Booking bookingToConfirm = null;
        while (true) {
            System.out.print("Choose the booking ID you want to confirm: ");
            if (scanner.hasNextInt()) {
                bookingId = scanner.nextInt();
                if (bookingId > 0) {
                    boolean isValidBookingId = false;
                    for (Booking b : pendings) {
                        if (b.getBookingId() == bookingId) {
                            isValidBookingId = true;
                            bookingToConfirm = b;
                            break;
                        }
                    }
                    if (isValidBookingId) {
                        break;
                    } else {
                        System.out.println("Invalid booking ID. Please choose a valid booking ID from the list.");
                    }
                } else {
                    System.out.println("Please enter a positive integer for booking ID.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer for booking ID.");
                scanner.next();
            }
        }

        bookingToConfirm.viewBooking();
        bookingToConfirm.getContactInfoOfCustomer();

        scanner.nextLine();
        System.out.println("\nNOTE: ");
        System.out.println("If there are many pending bookings in a date,");
        System.out.println("confirming one of them will cancel the rest.");

        String confirmation;
        while (true) {
            System.out.print("\nAre you sure you want to confirm this (y/n)? ");
            confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("y")) {
                if (loggedInManager.confirmBooking(bookingId, pendings)){
                    System.out.println("You confirmed the booking #" + bookingId);
                }
                else {
                    System.out.println("Something went wrong.");
                }
                break;
            } else if (confirmation.equals("n")) {
                System.out.println("You cancelled the confirmation.");
                break;
            } else {
                System.out.println("Invalid input. Please enter 'y' for Yes or 'n' for No.");
            }
        }
    }

    public void viewRevenue() {
        int currentYear = LocalDate.now().getYear();
        for (int year = currentYear - 5; year <= currentYear; year++) {
            Revenue revenue = new Revenue(year);
            revenue.setRevenueData(loggedInManager.getManagerId());
            revenue.viewRevenueTable();
        }
    }
}


