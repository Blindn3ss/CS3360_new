package org.myapp.Model;

import org.myapp.DAO.BookingDAOImpl;
import org.myapp.DAO.ManagerDAOImpl;

import java.util.*;

// Note this revenue can be better, for now just let it be for the project (nkvd)

public class Revenue {
    private Map<Integer, List<Double>> revenueData;
    private int year;

    public Revenue(int year) {
        this.year = year;
        this.revenueData = new HashMap<>();
    }

    public void setRevenueData(int managerId) {
        List<Integer> yardIds = ManagerDAOImpl.getInstance().getYardIdsForManager(managerId);
        for (Integer yardId : yardIds) {
            List<Double> monthlyRevenue = new ArrayList<>(12);
            for (int month = 1; month <= 12; month++) {
                double revenue = BookingDAOImpl.getInstance().getRevenueOfYardInMonth(yardId, month, year);
                monthlyRevenue.add(revenue);
            }
            revenueData.put(yardId, monthlyRevenue);
        }
    }

    /**
     * Prints the revenue table for all yards managed by the manager.
     * Displays the monthly revenue for each yard, as well as total revenue for each month.
     */
    public void viewRevenueTable() {
        if (revenueData.isEmpty()) {
            System.out.println("No revenue data available.");
            return;
        }

        int yardColumnWidth = 15;  // width for the Yard ID column
        int monthColumnWidth = 12; // width for each month column
        int totalColumnWidth = 12; // width for the total revenue column

        // Title of table
        System.out.print("+");
        System.out.print("-".repeat(yardColumnWidth));
        for (int i = 0; i < 12; i++) {
            System.out.print("-".repeat(monthColumnWidth));
        }
        System.out.print("-".repeat(totalColumnWidth) + "+");
        System.out.println();
        System.out.print("| ");
        System.out.printf("%-" + (yardColumnWidth - 1 + monthColumnWidth*12 + totalColumnWidth) + "s",
                                                                            "REVENUE OF " + this.year );
        System.out.print("|");
        System.out.println();

        // Print the top border of the table
        System.out.print("+");
        System.out.print("-".repeat(yardColumnWidth));
        for (int i = 0; i < 12; i++) {
            System.out.print("+" + "-".repeat(monthColumnWidth-1));
        }
        System.out.print("-".repeat(totalColumnWidth) + "+");
        System.out.println();

        // Print the header row (Yard ID and Months)
        System.out.print("| ");
        System.out.printf("%-" + (yardColumnWidth - 1) + "s", "Yard ID");
        for (int month = 1; month <= 12; month++) {
            System.out.printf("| %-"+(monthColumnWidth - 2) + "s", getMonthName(month));  // Print month names
        }
        System.out.printf("| %-"+(totalColumnWidth - 2) + "s", "Total"); // Total column header
        System.out.println("|");

        //  separator after the header row
        System.out.print("+");
        System.out.print("-".repeat(yardColumnWidth));
        for (int i = 0; i < 12; i++) {
            System.out.print("+" + "-".repeat(monthColumnWidth-1));
        }
        System.out.print("-".repeat(totalColumnWidth) + "+");
        System.out.println();

        // Iterate through each yard's revenue data and print the monthly revenue for each yard
        for (Map.Entry<Integer, List<Double>> entry : revenueData.entrySet()) {
            Integer yardId = entry.getKey();
            List<Double> monthlyRevenue = entry.getValue();

            System.out.print("| ");
            System.out.printf("%-" + (yardColumnWidth - 1) + "d", yardId);

            double totalRevenueForYard = 0;

            // Print revenue for each month, calculate total revenue for the yard
            for (int month = 0; month < 12; month++) {
                double revenue = monthlyRevenue.get(month);
                System.out.printf("| %-"+(monthColumnWidth - 2) + ".2f", revenue);
                totalRevenueForYard += revenue;
            }
            System.out.printf("| %-"+(totalColumnWidth - 2) + ".2f", totalRevenueForYard);
            System.out.println("|");
        }

        // Print the final separator line
        System.out.print("+");
        System.out.print("-".repeat(yardColumnWidth));
        for (int i = 0; i < 12; i++) {
            System.out.print("+" + "-".repeat(monthColumnWidth-1));
        }
        System.out.print("-".repeat(totalColumnWidth) + "+");
        System.out.println();
    }

    private String getMonthName(int month) {
        String[] monthNames = {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        return monthNames[month - 1];
    }
}

