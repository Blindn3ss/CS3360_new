package org.myapp.Model;

import org.myapp.DAO.BookingDAOImpl;
import org.myapp.DAO.ManagerDAOImpl;
import org.myapp.DAO.YardDAOImpl;
import org.myapp.Database.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Manager {
    private int managerId;
    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String email;

    public Manager(){
        this.managerId = 0;
        this.username = "";
        this.password = "";
        this.fullName = "";
        this.phoneNumber = "";
        this.email = "";
    }

    public Manager(int managerId, String username, String password, String fullName, String phoneNumber, String email) {
        this.managerId = managerId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void viewProfileDetails() {
        System.out.println("\nManager Profile Details:");
        System.out.println("Manager ID: " + managerId);
        System.out.println("Username: " + username);
        System.out.println("Full Name: " + fullName);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Email: " + email);
    }

    @Override
    public String toString() {
        return "Manager [managerId=" + managerId + ", username=" + username +
                ", fullName=" + fullName + ", phoneNumber=" + phoneNumber +
                ", email=" + email + "]";
    }

    public boolean managerIsExist(String username) {
        Manager manager = ManagerDAOImpl.getInstance().getManagerByUserName(username);
        return manager != null;
    }

    public boolean managerPasswordValid(String username, String password) {
        Manager manager = ManagerDAOImpl.getInstance().getManagerByUserName(username);
        return manager.getPassword().equals(password);
    }

    public Manager managerLogIn(String username){
        return ManagerDAOImpl.getInstance().getManagerByUserName(username);
    }

    public boolean managerSignUp(String username, String password, String fullName, String phoneNumber, String email){
        Manager manager = new Manager(-1, username, password, fullName, phoneNumber, email);
        return ManagerDAOImpl.getInstance().createManager(manager);
    }

    public boolean updateProfile(Manager loggedInManager) {
        return ManagerDAOImpl.getInstance().updateManager(loggedInManager);
    }

    public boolean addNewYard(String yardName,
                              String yardLocation,
                              int yardCapacity,
                              String surfaceType,
                              double pricePerDay,
                              String description) {
        Yard yard = new Yard();
        yard.setYardName(yardName);
        yard.setYardLocation(yardLocation);
        yard.setYardCapacity(yardCapacity);
        yard.setSurfaceType(surfaceType);
        yard.setPricePerDay(pricePerDay);
        yard.setDescription(description);

        return YardDAOImpl.getInstance().createYard(yard);
    }

    public boolean havePermission(int yardId) {
        return ManagerDAOImpl.getInstance().checkPermission(this.managerId, yardId);
    }

    public boolean updateYardName(int yardId, String yardName) {
        Yard yard = YardDAOImpl.getInstance().getYardById(yardId);
        yard.setYardName(yardName);
        return YardDAOImpl.getInstance().updateYard(yard);
    }

    public boolean updateYardLocation(int yardId, String yardLocation) {
        Yard yard = YardDAOImpl.getInstance().getYardById(yardId);
        yard.setYardLocation(yardLocation);
        return YardDAOImpl.getInstance().updateYard(yard);
    }

    public boolean updateYardCapacity(int yardId, int yardCapacity) {
        Yard yard = YardDAOImpl.getInstance().getYardById(yardId);
        yard.setYardCapacity(yardCapacity);
        return YardDAOImpl.getInstance().updateYard(yard);
    }

    public boolean updateYardSurfaceType(int yardId, String surfaceType) {
        Yard yard = YardDAOImpl.getInstance().getYardById(yardId);
        yard.setSurfaceType(surfaceType);
        return YardDAOImpl.getInstance().updateYard(yard);
    }

    public boolean updateYardPricePerDay(int yardId, double pricePerDay) {
        Yard yard = YardDAOImpl.getInstance().getYardById(yardId);
        yard.setPricePerDay(pricePerDay);
        return YardDAOImpl.getInstance().updateYard(yard);
    }

    public boolean registerToManage(int yardId){
        return ManagerDAOImpl.getInstance().addPermission(this.managerId, yardId);
    }

    public boolean removeManagement(int yardId) {
        return ManagerDAOImpl.getInstance().removePermission(this.managerId, yardId);
    }

    public List<Integer> getListOfYardId(){
        return ManagerDAOImpl.getInstance().getYardIdsForManager(this.managerId);
    }


    /**
     * Confirms a booking with the specified booking ID and cancels the other bookings in the given list.
     * The method updates the status of the bookings in the list within a single database transaction.
     * The booking with the specified ID is set to {@link BookingStatus#CONFIRMED}, and all other bookings
     * in the list are set to {@link BookingStatus#CANCELLED}. If any update fails, the transaction is rolled back.
     *
     * @param bookingId The ID of the booking to confirm.
     * @param pendings  The list of pending bookings to process.
     * @return {@code true} if all updates were successful, {@code false} otherwise. If an exception occurs,
     *         or if any update fails, the transaction is rolled back.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean confirmBooking(int bookingId, List<Booking> pendings) {
        // read javadoc if you dont understand why it need a se seperated connection
        // don't know if other also need, for now just let this one be. (nkvd)
        Database db = new Database();
        Connection connection = null;
        try {
            connection = db.connect();
            connection.setAutoCommit(false);
            boolean success = true;
            for (Booking b : pendings) {
                if (b.getBookingId() == bookingId) {
                    b.setBookingStatus(BookingStatus.CONFIRMED);

                    if (!BookingDAOImpl.getInstance().updateBookingWithConnection(connection, b)) {
                        success = false;
                        break;
                    }
                } else {
                    b.setBookingStatus(BookingStatus.CANCELLED);

                    if (!BookingDAOImpl.getInstance().updateBookingWithConnection(connection, b)) {
                        success = false;
                        break;
                    }
                }
            }

            if (success) {
                connection.commit();
            } else {
                connection.rollback();
            }

            return success;

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

