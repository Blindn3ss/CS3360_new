package org.myapp.Menu;

import org.myapp.DAO.YardDAOImpl;
import org.myapp.Model.Booking;
import org.myapp.Model.BookingStatus;
import org.myapp.Model.Customer;
import org.myapp.Model.Yard;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

import static org.myapp.Menu.Utility.*;
import static org.myapp.Menu.Utility.isValidEmailFormat;

public class CustomerMenuAction {
    private final Customer loggedInCustomer;
    private final Scanner scanner;

    CustomerMenuAction (Customer loggedInCustomer){
        this.loggedInCustomer = loggedInCustomer;
        this.scanner = new Scanner(System.in);
    }

    public void cancelBooking() {
        viewCurrentBooking();
        int bookingId;
        System.out.println("Enter the ID of Booking you want cancel:");
        bookingId = scanner.nextInt();
        scanner.nextLine();

        Booking booking = new Booking();
        if (!booking.isBookingValid(bookingId, loggedInCustomer.getCustomerId())){
            System.out.println("Booking ID is not valid");
            return;
        }
        if (loggedInCustomer.cancelBooking(bookingId)){
            System.out.printf("You canceled the Booking #" + bookingId + " successfully.");
        }
        else {
            System.out.println("Something went wrong. Try again later!");
        }
    }

    public void viewBookingHistory() {
        System.out.println("\nBOOKING HISTORY");
        System.out.println("-----------------------------------------------------");
        List<Booking> bookingHistory;
        bookingHistory = loggedInCustomer.getBookingHistory();
        if (bookingHistory.isEmpty()){
            System.out.println("You have no completed booking");
        }
        else{
            for (Booking booking : bookingHistory){
                booking.viewBooking();
            }
        }
    }

    public void viewCurrentBooking() {
        System.out.println("\nPENDING");
        System.out.println("-----------------------------------------------------");
        List<Booking> pendingBookings;
        pendingBookings = loggedInCustomer.getPendingBooking();
        if(pendingBookings.isEmpty()){
            System.out.println("You have no pending booking.");
        }
        else{
            for (Booking booking : pendingBookings){
                booking.viewBooking();
            }
        }


        System.out.println("\nCONFIRMED");
        System.out.println("-----------------------------------------------------");
        List<Booking> confirmedBookings;
        confirmedBookings = loggedInCustomer.getConfirmedBooking();
        if(confirmedBookings.isEmpty()){
            System.out.println("You have no confirmed booking.");
        }
        else{
            for (Booking booking : confirmedBookings){
                booking.viewBooking();
            }
        }
        System.out.println();
    }

    public void bookNow() {
        int yardId;
        LocalDate date = null;

        System.out.println("Enter the Yard ID you want to book: ");
        yardId = scanner.nextInt();
        scanner.nextLine();

        Yard yard = YardDAOImpl.getInstance().getYardById(yardId);
        if (yard == null){
            System.out.println("Yard ID is not valid");
            return;
        }

        System.out.println("Enter the date (YYYY--MM-DD): ");
        String input;
        while (date == null || isInvalidDateForBooking(date)) {
            input = scanner.nextLine().trim();
            while (!Utility.isValidDateFormat(input)) {
                System.out.println("Invalid date format. Try again");
                input = scanner.nextLine().trim();
            }
            date = LocalDate.parse(input);

            if (isInvalidDateForBooking(date)) {
                String today = LocalDate.now().toString();
                String latestAllowedDate = latestAlowedDate.toString();

                System.out.println("+--------------------------- Rule! -------------------------------+");
                System.out.println("| 1. You can not enter a date from the past!                      |");
                System.out.println("| 2. Booking date must be at most 14 days after the current date! |");
                System.out.printf( "|    Today: %-53s |%n", today);
                System.out.printf( "|    The latest day you can enter is: %-27s |%n", latestAllowedDate);
                System.out.println( "+-----------------------------------------------------------------+");
                System.out.println( "Please try again.");
            }
        }
        Booking booking = new Booking();
        if (booking.bookingExist(yardId, date)){
            System.out.println("The yard you chosen was booked on " + date + ".");
            return;
        }

        Booking newBooking = new Booking(0,
                loggedInCustomer.getCustomerId(),
                yard.getYardId(),
                date,
                yard.getPricePerDay(),
                BookingStatus.PENDING);

        System.out.println("YOUR BOOKING");
        System.out.println("+--------------------------------------------------------------------+");
        System.out.println(newBooking.displayBookingInfo());
        System.out.println("YARD INFORMATION");
        System.out.println(yard.yardInfo());

        String response;
        while (true) {
            System.out.print("Are you sure you want to book? (Y/N): ");
            response = scanner.nextLine().trim().toUpperCase();
            if (response.equals("Y")) {
                if (loggedInCustomer.bookYard(newBooking)){
                    System.out.println("Your booking created. Waiting for the yard manager confirmed");
                }
                else {
                    System.out.println("Something went wrong. Try again.");
                }
                break;
            } else if (response.equals("N")) {
                System.out.println("Booking canceled.");
                break;
            } else {
                System.out.println("Invalid input. Please enter 'Y' for Yes or 'N' for No.");
            }
        }
    }

    public void viewProfile() {
        loggedInCustomer.viewProfileDetails();
    }

    public void editProfile(){
        SubMenu editProfile = new SubMenu("Edit Your Profile");
        editProfile.addMenuItem(new ActionMenuItem("Change Full Name", ()->{
            String fullName;
            System.out.println("Enter your new full name: ");
            fullName = scanner.nextLine().trim();
            while(fullName.isEmpty()){
                System.out.println("Your Full Name should not be empty");
                fullName = scanner.nextLine().trim();
            }
            loggedInCustomer.setFullName(fullName);
            if (loggedInCustomer.updateProfile(loggedInCustomer)){
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
            loggedInCustomer.setEmail(email);
            if (loggedInCustomer.updateProfile(loggedInCustomer)){
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
            loggedInCustomer.setPhoneNumber(phoneNumber);
            if (loggedInCustomer.updateProfile(loggedInCustomer)){
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
            loggedInCustomer.setPassword(password);
            if (loggedInCustomer.updateProfile(loggedInCustomer)){
                System.out.println("Your password has been successfully updated");
            }
            else {
                System.out.println("Something went wrong.");
            }
        }));
        editProfile.addMenuItem(new ActionMenuItem("Back", ()->{}));
        editProfile.execute();
    }

    public void viewYardsWithFilter() {
        // Note: This function will build as a SubMenu but technically, this is not menu
        // Because we are using Runnable and run() so the variable are not thread-safe when changing in
        // these lambda expression.
        AtomicReference<String> location = new AtomicReference<>();
        AtomicReference<Integer> minCapacity = new AtomicReference<>();
        AtomicReference<Integer> maxCapacity = new AtomicReference<>();
        AtomicReference<Double> minPrice = new AtomicReference<>();
        AtomicReference<Double> maxPrice = new AtomicReference<>();
        AtomicReference<String> surfaceType = new AtomicReference<>();

        // The view with filter is a menu getting options from user
        SubMenu viewYardsWithFilter = new SubMenu("viewYardWithFilter");

        viewYardsWithFilter.addMenuItem(new ActionMenuItem("Current Filter", ()->{
            System.out.println("\n--- Current Filters ---");
            System.out.println("Location: " + (location.get() != null ? location : "None"));
            System.out.println("Min Capacity: " + (minCapacity.get() != null ? minCapacity : "None"));
            System.out.println("Max Capacity: " + (maxCapacity.get() != null ? maxCapacity : "None"));
            System.out.println("Min Price: " + (minPrice.get() != null ? minPrice : "None"));
            System.out.println("Max Price: " + (maxPrice.get() != null ? maxPrice : "None"));
            System.out.println("Surface Type: " + (surfaceType.get() != null ? surfaceType : "None"));
            System.out.println("------------------------");
        }));

        viewYardsWithFilter.addMenuItem(new ActionMenuItem("Add Location", () -> {
            System.out.print("Enter location: ");
            location.set(scanner.nextLine());
        }));

        viewYardsWithFilter.addMenuItem(new ActionMenuItem("Add Capacity", () -> {
            try {
                System.out.print("Enter minimum capacity: ");
                minCapacity.set(scanner.nextInt());
                scanner.nextLine();

                System.out.print("Enter maximum capacity (or press Enter to skip): ");
                String maxCapacityInput = scanner.nextLine().trim();
                if (!maxCapacityInput.isEmpty()) {
                    maxCapacity.set(Integer.parseInt(maxCapacityInput));
                } else {
                    maxCapacity.set(null);  // skipping
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input for capacity. Please enter valid integers.");
                scanner.nextLine();
            }
        }));

        viewYardsWithFilter.addMenuItem(new ActionMenuItem("Add Price Range", () -> {
            try {
                System.out.print("Enter minimum price (or press Enter to skip): ");
                String minPriceInput = scanner.nextLine().trim();
                if (!minPriceInput.isEmpty()) {
                    minPrice.set(Double.parseDouble(minPriceInput));
                }

                System.out.print("Enter maximum price (or press Enter to skip): ");
                String maxPriceInput = scanner.nextLine().trim();
                if (!maxPriceInput.isEmpty()) {
                    maxPrice.set(Double.parseDouble(maxPriceInput));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid price input. Please enter valid numbers.");
            }
        }));

        viewYardsWithFilter.addMenuItem(new ActionMenuItem("Add Surface Type", () -> {
            System.out.print("Enter surface type (or press Enter to skip): ");
            surfaceType.set(scanner.nextLine());
        }));

        viewYardsWithFilter.addMenuItem(new ActionMenuItem("View Result", () -> {
            Yard yard = new Yard();
            List<Yard> filteredYards = yard.getYardsWithFilter(minCapacity.get(),
                    maxCapacity.get(),
                    location.get(),
                    surfaceType.get(),
                    minPrice.get(),
                    maxPrice.get());

            if (filteredYards.isEmpty()) {
                System.out.println("No yards found with the given filters.");
            } else {
                System.out.println("\n\nFound the following yards:\n");
                for (Yard yard_ : filteredYards) {
                    System.out.println(yard_.yardInfo());
                }
            }
        }));

        viewYardsWithFilter.addMenuItem(new ActionMenuItem("Clear", () -> {
            location.set(null);
            minCapacity.set(null);
            maxCapacity.set(null);
            minPrice.set(null);
            maxPrice.set(null);
            surfaceType.set(null);

            System.out.println("Filters cleared.");
        }));

        viewYardsWithFilter.addMenuItem(new ActionMenuItem("Book Now", this::bookNow));
        viewYardsWithFilter.addMenuItem(new ActionMenuItem("Back", ()->{}));

        viewYardsWithFilter.execute();
    }
}
