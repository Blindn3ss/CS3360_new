package org.myapp.DAO;

import org.myapp.Model.Booking;
import org.myapp.Model.BookingStatus;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BookingDAO {
    boolean createBooking(Booking booking);

    Booking getBookingById(int bookingId);

    boolean updateBooking(Booking booking);

    boolean updateBookingWithConnection(Connection connection, Booking booking);

    boolean updateBookingWithStatus(int bookingId, BookingStatus status);

    boolean deleteBooking(int bookingId);

    List<Booking> getAllBookings();

    List<Booking> getBookingsByCustomerId(int customerId);

    List<Booking> getBookingsByYardId(int yardId);

    Booking getBookedYardByDate(int yardId, LocalDate date);

    List<Booking> getBookingsByStatus(BookingStatus status); // Updated to use BookingStatus enum

    List<Booking> getBookingsByCustomerIdAndStatus(int customerId, BookingStatus status);

    // Note: If can not use generalize, then manually do from beginning (nkvd)
    boolean isBookingValidToCanCel(int bookingId, int customerId);

    Map<Integer, List<Booking>> getBookingsForYards(List<Integer> yardIds);

    List<Booking> getPendingBookingOfYardInDate(int yardId, String date);

    double getRevenueOfYardInMonth(int yardId, int month, int year);
}


