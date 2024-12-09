package org.myapp.Model;

import org.myapp.DAO.BookingDAOImpl;
import org.myapp.DAO.CustomerDAOImpl;

import java.time.LocalDate;
import java.util.List;

public class Booking {
    private int bookingId;
    private int customerId;
    private int yardId;
    private LocalDate bookingDate;
    private double bookingPrice;
    private BookingStatus bookingStatus; // Updated to use enum

    public Booking (){
        this.bookingId = 0;
        this.customerId = 0;
        this.yardId = 0;
        this.bookingDate = LocalDate.parse("1990-01-01");
        this.bookingPrice = 0;
        this.bookingStatus = BookingStatus.COMPLETED;
    }

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

    public void viewBooking() {
        System.out.printf("Booking #%d | Yard ID: %d | Date: %s | Status: %s | Total: $%.2f%n",
                bookingId,
                yardId,
                bookingDate.toString(),
                bookingStatus.name(),
                bookingPrice);
    }

    public void viewBooking2() {
        System.out.printf(String.format("Booking #%d | Yard ID: #%d | Customer ID: #%d | Date: %s | Status: %s | Total: $%.2f",
                bookingId,
                yardId,
                customerId,
                bookingDate.toString(),
                bookingStatus.name(),
                bookingPrice));
    }

    public String displayBookingInfo() {
        return  "Booking Info\n"+
                "------------\n"+
                "User ID: " + customerId + "\n" +
                "Yard ID: " + yardId + "\n" +
                "Booking Date: " + bookingDate + "\n" +
                "Total Price: $" + bookingPrice + "\n" +
                "Booking Status: " + bookingStatus;
    }

    public boolean bookingExist(int yardId, LocalDate date){
        return BookingDAOImpl.getInstance().getBookedYardByDate(yardId, date) != null;
    }

    public boolean isBookingValid(int bookingId, int customerId) {
        return BookingDAOImpl.getInstance().isBookingValidToCanCel(bookingId, customerId);
    }

    public List<Booking> getPendingBookingOfYardInDate(int yardId, String date) {
        return BookingDAOImpl.getInstance().getPendingBookingOfYardInDate(yardId, date);
    }

    public void getContactInfoOfCustomer(){
        Customer customer = CustomerDAOImpl.getInstance().getCustomerById(this.customerId);
        customer.viewContactInfo();
    }
}

