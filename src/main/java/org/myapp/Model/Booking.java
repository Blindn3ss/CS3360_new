package org.myapp.Model;

import java.time.LocalDate;

public class Booking {
    private int bookingId;
    private int customerId;
    private int yardId;
    private LocalDate bookingDate;
    private double totalPrice;
    private BookingStatus bookingStatus; // Updated to use enum

    public Booking(int bookingId, int customerId, int yardId, LocalDate bookingDate,
                   double totalPrice, BookingStatus bookingStatus) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.yardId = yardId;
        this.bookingDate = bookingDate;
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

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getBookingStatus() { // Updated getter
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) { // Updated setter
        this.bookingStatus = bookingStatus;
    }

    public String displayBookingInfo() {
        return "Booking ID: " + bookingId + "\n" +
                "User ID: " + customerId + "\n" +
                "Yard ID: " + yardId + "\n" +
                "Booking Date: " + bookingDate + "\n" +
                "Total Price: $" + totalPrice + "\n" +
                "Booking Status: " + bookingStatus; // Will display as the name of the enum
    }
}

