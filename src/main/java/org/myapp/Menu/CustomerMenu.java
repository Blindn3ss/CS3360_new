package org.myapp.Menu;

import org.myapp.DAO.BookingDAOImpl;
import org.myapp.DAO.CustomerDAOImpl;
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

public class CustomerMenu {
    Scanner scanner;
    private final Customer loggedInCustomer;

    public CustomerMenu(Customer customer, Scanner scanner) {
        this.loggedInCustomer = customer;
        this.scanner = scanner;
    }

    public void execute() {

        //Lv.3

        // Lv.2
        SubMenu profileMenu = new SubMenu("Profile");
        profileMenu.addMenuItem(new ActionMenuItem("View Profile", this::viewProfile));
        profileMenu.addMenuItem(new ActionMenuItem("Edit Profile", this::editProfile));
        profileMenu.addMenuItem(new ActionMenuItem("Back", () -> {}));


        SubMenu yardBookingSection = new SubMenu("Yards And Bookings");
        yardBookingSection.addMenuItem(new ActionMenuItem("View All Yards", () -> {}));
        yardBookingSection.addMenuItem(new ActionMenuItem("View Yards With Filter", this::viewYardsWithFilter));
        yardBookingSection.addMenuItem(new ActionMenuItem("Book Now", this::bookNow));
        yardBookingSection.addMenuItem(new ActionMenuItem("View Current Booking", this::viewCurrentBooking));
        yardBookingSection.addMenuItem(new ActionMenuItem("View Booking History ", this::viewBookingHistory));
        yardBookingSection.addMenuItem(new ActionMenuItem("Cancel a booking", this::cancelBooking));
        yardBookingSection.addMenuItem(new ActionMenuItem("Back", () -> {}));

        //Lv1
        SubMenu customerMenu = new SubMenu("Customer Menu");
        customerMenu.addMenuItem(profileMenu);
        customerMenu.addMenuItem(yardBookingSection);
        customerMenu.addMenuItem(new ActionMenuItem("Log Out", ()->{}));

        customerMenu.execute();
    }


    private void cancelBooking() {
        viewCurrentBooking();

        int bookId;
        System.out.println("Enter the ID of Booking you want cancel:");
        bookId = scanner.nextInt();
        scanner.nextLine();
        if (!BookingDAOImpl.getInstance().isBookingValidToCanCel(bookId, loggedInCustomer.getCustomerId())){
            System.out.println("Booking ID is not valid");
            return;
        }
        if (BookingDAOImpl.getInstance().updateBookingStatusWithId(bookId, BookingStatus.CANCEL)){
            System.out.printf("You canceled the Booking #" + bookId + " successfully.");
        }
        else {
            System.out.println("Something went wrong. Try again later!");
        }
    }

    private void viewBookingHistory() {
        List<Booking> pendingBookings;
        pendingBookings = BookingDAOImpl.getInstance()
                .getBookingsByCustomerIdAndStatus(  loggedInCustomer.getCustomerId(),
                        BookingStatus.COMPLETED);
        for (Booking booking : pendingBookings){
            System.out.println(booking.viewBooking());
        }
    }

    private void viewCurrentBooking() {
        System.out.println("\nPENDING");
        List<Booking> pendingBookings;
        pendingBookings = BookingDAOImpl.getInstance()
                                        .getBookingsByCustomerIdAndStatus(loggedInCustomer.getCustomerId(),
                                                                            BookingStatus.PENDING);
        for (Booking booking : pendingBookings){
            System.out.println(booking.viewBooking());
        }

        System.out.println("\nCONFIRMED");
        List<Booking> confirmedBookings;
        confirmedBookings = BookingDAOImpl.getInstance()
                                          .getBookingsByCustomerIdAndStatus(loggedInCustomer.getCustomerId(),
                                                                            BookingStatus.CONFIRMED);
        for (Booking booking : confirmedBookings) {
            System.out.println(booking.viewBooking());
        }
        System.out.println();
    }

    private void bookNow() {
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
        while (date == null || isBeforeCurrentDate(date)){
            input = scanner.nextLine().trim();
            while (!isValidDateFormat(input)){
                System.out.println("Invalid date format. Try again");
                input = scanner.nextLine().trim();
            }
            date = LocalDate.parse(input);
            if (isBeforeCurrentDate(date)){
                System.out.println("Can not enter a date from past!");
                System.out.println("Try again.");
            }
        }

        if (bookingExist(yardId, date)){
            System.out.println("The yard you chosen was booked on " + date + ".");
            return;
        }

        Booking booking = new Booking(0,
                loggedInCustomer.getCustomerId(),
                yard.getYardId(),
                date,
                yard.getPricePerDay(),
                BookingStatus.PENDING);

        System.out.println("YOUR BOOKING");
        System.out.println("+--------------------------------------------------------------------+");
        System.out.println(booking.displayBookingInfo());
        System.out.println("YARD INFORMATION");
        System.out.println(yard.yardInfo());
        
        String response;
        while (true) {
            System.out.print("Are you sure you want to book? (Y/N): ");
            response = scanner.nextLine().trim().toUpperCase();
            if (response.equals("Y")) {
                if (BookingDAOImpl.getInstance().createBooking(booking)){
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

    private void viewProfile() {
        loggedInCustomer.viewProfileDetails();
    }

    private void editProfile(){
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
            CustomerDAOImpl.getInstance().updateCustomer(loggedInCustomer);
            System.out.println("Your full name has been successfully updated to: " + fullName);
        }));

        editProfile.addMenuItem(new ActionMenuItem("Change Email", ()->{
            String email;
            System.out.println("Enter your new email: ");
            email = scanner.nextLine().trim();
            while(email.isEmpty()){
                System.out.println("Your email should not be empty");
                email = scanner.nextLine().trim();
            }
            while(!isValidEmail(email)){
                System.out.println("Your email is invalid");
                email = scanner.nextLine().trim();
            }
            loggedInCustomer.setEmail(email);
            CustomerDAOImpl.getInstance().updateCustomer(loggedInCustomer);
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
            CustomerDAOImpl.getInstance().updateCustomer(loggedInCustomer);
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
            boolean isUpdated;
            isUpdated = CustomerDAOImpl.getInstance().updateCustomer(loggedInCustomer);
            if (isUpdated) {
                System.out.println("Updated complete.");
            }
            else {
                System.out.println("Something went wrong. Try again later.");
            }
        }));
        editProfile.addMenuItem(new ActionMenuItem("Back", ()->{}));

        editProfile.execute();

    }

    private void viewYardsWithFilter() {
        // Because we are using Runnable and run() so the variable are not thread-safe when changing in
        // these lambda expression.
        AtomicReference<String> location = new AtomicReference<>();
        AtomicReference<Integer> minCapacity = new AtomicReference<>();
        AtomicReference<Integer> maxCapacity = new AtomicReference<>();
        AtomicReference<Double> minPrice = new AtomicReference<>();
        AtomicReference<Double> maxPrice = new AtomicReference<>();
        AtomicReference<String> surfaceType = new AtomicReference<>();

        // The view with filter is a menu getting options from user
        SubMenu viewYardsWithFilter = new SubMenu("Filter Yards");
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
                scanner.nextLine();  // Consume the newline character

                System.out.print("Enter maximum capacity (or press Enter to skip): ");
                String maxCapacityInput = scanner.nextLine().trim();
                if (!maxCapacityInput.isEmpty()) {
                    maxCapacity.set(Integer.parseInt(maxCapacityInput));
                } else {
                    maxCapacity.set(null);  // skipping
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input for capacity. Please enter valid integers.");
                scanner.nextLine();  // Clear the invalid input
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
            List<Yard> filteredYards = YardDAOImpl.getInstance().getYardsWithFilter(minCapacity.get(),
                                                                                    maxCapacity.get(),
                                                                                    location.get(),
                                                                                    surfaceType.get(),
                                                                                    minPrice.get(),
                                                                                    maxPrice.get());
            if (filteredYards.isEmpty()) {
                System.out.println("No yards found with the given filters.");
            } else {
                System.out.println("\n\nFound the following yards:\n");
                for (Yard yard : filteredYards) {
                    System.out.println(yard.yardInfo());
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
