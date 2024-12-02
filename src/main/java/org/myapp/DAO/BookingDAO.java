package org.myapp.DAO;

import org.myapp.Model.Booking;

import java.util.List;

public interface BookingDAO {
    boolean createBooking(Booking booking);
    Booking getBookingById(int bookingId);
    boolean updateBooking(Booking booking);
    boolean deleteBooking(int bookingId);
    List<Booking> getAllBookings();
    List<Booking> getBookingsByCustomerId(int customerId);
    List<Booking> getBookingsByYardId(int yardId);
    List<Booking> getBookingsByStatus(String status);
}

