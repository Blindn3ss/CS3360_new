package org.myapp.Model;

import org.myapp.DAO.BookingDAOImpl;

import java.time.LocalDate;
import java.util.*;

import static org.myapp.Menu.Utility.LIMIT;

public class Schedule {

    public Schedule (){}

    public void viewScheduleForManager(List<Integer> managedYardId) {
        // concurrency - larger fake database (remove, try it after project)
        // Or can make a function to sort and count
        // Took many times for this so please dont change this except column width (nkvd)

        // get all date
        Map<Integer, List<Booking>> schedule = BookingDAOImpl.getInstance().getBookingsForYards(managedYardId);
        boolean hasBookings = schedule.values().stream().anyMatch(bookings -> bookings != null && !bookings.isEmpty());

        // If no bookings exist, print "No booking for now" and return
        if (!hasBookings) {
            System.out.println("No booking for now.");
            return;
        }

        // Create a set to store all distinct booking dates
        Set<LocalDate> allDates = new TreeSet<>();
        for (List<Booking> bookings : schedule.values()) {
            for (Booking booking : bookings) {
                allDates.add(booking.getBookingDate());
            }
        }

        // Define column width for formatting (adjust based on the expected length of values)
        int yardColumnWidth = 15; // should - 1 for + symbol
        int dateColumnWidth = 15; // should -1  for space

        // first border
        System.out.print("+");
        System.out.print("-".repeat(dateColumnWidth));
        for (Integer _ : schedule.keySet()) {
            System.out.print("+" + "-".repeat(yardColumnWidth-1));
        }
        System.out.print("+");
        System.out.println();

        // print the header (booking date and yard IDs)
        System.out.print("| ");
        System.out.printf("%-" + (dateColumnWidth -1) + "s", "Booking Date");
        for (Integer yardId : schedule.keySet()) {
            System.out.printf("| %-"+(yardColumnWidth-2) +"s", "YARD " + yardId);
        }
        System.out.print("|");
        System.out.println();

        // Print separator line (for the border of the table)
        System.out.print("+");
        System.out.print("-".repeat(dateColumnWidth)); // first column (Booking Date)
        for (Integer _ : schedule.keySet()) {
            System.out.print("+" + "-".repeat(yardColumnWidth-1)); //each yard column
        }
        System.out.print("+");
        System.out.println();

        // print each date and corresponding booking data for each yard
        // maybe we can add date from current -> current + 14 but not need.

        for (LocalDate date : allDates) {
            System.out.print("| ");
            System.out.printf("%-" + (dateColumnWidth-1) + "s", date.toString()); // Print the booking date column

            // view each yard's bookings for the current date
            // can used .parallelStream()
            for (Integer yardId : schedule.keySet()) {
                List<Booking> bookings = schedule.get(yardId);
                if (bookings != null) {
                    long pendingCount = bookings.stream()
                            .filter(b -> b.getBookingDate().equals(date) && b.getBookingStatus() == BookingStatus.PENDING)
                            .count();
                    boolean confirmed = bookings.stream().anyMatch(b -> b.getBookingDate().equals(date) && b.getBookingStatus() == BookingStatus.CONFIRMED);

                    // Print the booking status
                    if (confirmed) {
                        System.out.printf("| %-"+(yardColumnWidth-2)+"s", "CONFIRMED");
                    } else if (pendingCount > 0) {
                        System.out.printf("| %-"+(yardColumnWidth-2)+"s", pendingCount + " PENDING(S)");
                    } else {
                        System.out.printf("| %-"+(yardColumnWidth-2)+"s", "NONE");
                    }
                }
                else {
                    System.out.printf("| %-"+(yardColumnWidth-2)+"s", "NONE");
                }
            }
            System.out.print("|");
            System.out.println();
        }

        // print a final separator line at the bottom of the table
        System.out.print("+");
        System.out.print("-".repeat(dateColumnWidth)); // first column (Booking Date)
        for (Integer _ : schedule.keySet()) {
            System.out.print("+" + "-".repeat(yardColumnWidth-1)); // each yard column
        }
        System.out.print("+");
        System.out.println();
    }

    // schedule for customer when they want to book
    public void viewScheduleForCustomer(int yardId) {
        Set<LocalDate> upcomingDates = new TreeSet<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i <= LIMIT; i++) {
            upcomingDates.add(today.plusDays(i));
        }

        List<Booking> bookings = BookingDAOImpl.getInstance().getBookingsByYardId(yardId);

        Map<LocalDate, String> scheduleForCustomer = new LinkedHashMap<>();

        for (LocalDate date : upcomingDates) {
            Optional<Booking> confirmedBooking = bookings.stream()
                    .filter(booking -> booking.getYardId() == yardId && booking.getBookingDate().equals(date) && booking.getBookingStatus() == BookingStatus.CONFIRMED)
                    .findFirst();

            if (confirmedBooking.isPresent()) {
                scheduleForCustomer.put(date, "UNAVAILABLE");
            } else {
                scheduleForCustomer.put(date, "AVAILABLE");
            }
        }

        printCustomerScheduleTable(scheduleForCustomer);
    }

    private void printCustomerScheduleTable(Map<LocalDate, String> scheduleForCustomer) {
        int dateColumnWidth = 15; //  Ddate
        int statusColumnWidth = 20; //  (Available/Unavailable)
        System.out.println("+-----------------+----------------------+" );
        System.out.printf("| %-" + dateColumnWidth + "s | %-" + statusColumnWidth + "s |\n", "Booking Date", "Status");
        System.out.println("+-----------------+----------------------+" );

        for (Map.Entry<LocalDate, String> availability : scheduleForCustomer.entrySet()) {
            System.out.printf("| %-" + dateColumnWidth + "s | %-"+ statusColumnWidth + "s |\n", availability.getKey().toString(), availability.getValue());
        }
        System.out.println("+-----------------+----------------------+" );
    }
}

