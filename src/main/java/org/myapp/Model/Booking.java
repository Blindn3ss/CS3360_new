package org.myapp.Model;

import java.time.LocalDate;

public class Booking {
    private int bookingId;
    private int customerId;
    private int yardId;
    private LocalDate bookingDate;
    private int timeSlotId;
    private double totalPrice;
    private String bookingStatus;

    public Booking(int bookingId, int customerId, int yardId, LocalDate bookingDate,
                   int timeSlotId, double totalPrice, String bookingStatus) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.yardId = yardId;
        this.bookingDate = bookingDate;
        this.timeSlotId = timeSlotId;
        this.totalPrice = totalPrice;
        this.bookingStatus = bookingStatus;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int userId) {
        this.customerId = userId;
    }

    public int getYardId() {
        return yardId;
    }

    public void setYardId(int yardId) {
        this.yardId = yardId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    // Method to display booking details
    public String displayBookingInfo() {
        return "Booking ID: " + bookingId + "\n" +
                "User ID: " + customerId + "\n" +
                "Yard ID: " + yardId + "\n" +
                "Booking Date: " + bookingDate + "\n" +
                "Time Slot ID: " + timeSlotId + "\n" +
                "Total Price: $" + totalPrice + "\n" +
                "Booking Status: " + bookingStatus;
    }
}
