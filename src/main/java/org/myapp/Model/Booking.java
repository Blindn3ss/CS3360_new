package org.myapp.Model;

import java.time.LocalDate;

public class Booking {
    private int bookingId;
    private int customerId;
    private int yardId;
    private LocalDate bookingDate;
    private double bookingPrice;
    private BookingStatus bookingStatus; // Updated to use enum

    public Booking(int bookingId, int customerId, int yardId, LocalDate bookingDate,
                   double bookingPrice, BookingStatus bookingStatus) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.yardId = yardId;
        this.bookingDate = bookingDate;
        this.bookingPrice = bookingPrice;
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

    public double getBookingPrice() {
        return bookingPrice;
    }

    public void setBookingPrice(double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    public BookingStatus getBookingStatus() { // Updated getter
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) { // Updated setter
        this.bookingStatus = bookingStatus;
    }

    public String viewBooking() {
            return String.format("Booking #%d | Yard ID: %d | Date: %s | Status: %s | Total: $%.2f",
                    bookingId,
                    customerId,
                    bookingDate.toString(),
                    bookingStatus.name(),
                    bookingPrice);
    }

    public String displayBookingInfo() {
        return  "Booking Info\n"+
                "-----------------------------------\n"+
                "User ID: " + customerId + "\n" +
                "Yard ID: " + yardId + "\n" +
                "Booking Date: " + bookingDate + "\n" +
                "Total Price: $" + bookingPrice + "\n" +
                "Booking Status: " + bookingStatus;
    }
}

