package org.myapp.Menu;

import org.myapp.Model.Customer;

public class CustomerMenu {
    private final CustomerMenuAction actions;

    public CustomerMenu(Customer customer) {
        this.actions = new CustomerMenuAction(customer);
    }

    public void execute() {
        //Lv.3

        // Lv.2
        SubMenu profileMenu = new SubMenu("Profile");
        profileMenu.addMenuItem(new ActionMenuItem("View Profile", actions::viewProfile));
        profileMenu.addMenuItem(new ActionMenuItem("Edit Profile", actions::editProfile));
        profileMenu.addMenuItem(new ActionMenuItem("Back", () -> {}));


        SubMenu yardBookingSection = new SubMenu("Yards And Bookings");
        yardBookingSection.addMenuItem(new ActionMenuItem("View Yards With Filter", actions::viewYardsWithFilter));
        yardBookingSection.addMenuItem(new ActionMenuItem("Book Now", actions::bookNow));
        yardBookingSection.addMenuItem(new ActionMenuItem("View Current Booking", actions::viewCurrentBooking));
        yardBookingSection.addMenuItem(new ActionMenuItem("View Booking History ", actions::viewBookingHistory));
        yardBookingSection.addMenuItem(new ActionMenuItem("Cancel a booking", actions::cancelBooking));
        yardBookingSection.addMenuItem(new ActionMenuItem("Back", () -> {}));

        //Lv1
        SubMenu customerMenu = new SubMenu("Customer Menu");
        customerMenu.addMenuItem(profileMenu);
        customerMenu.addMenuItem(yardBookingSection);
        customerMenu.addMenuItem(new ActionMenuItem("Log Out", ()->{}));

        customerMenu.execute();
    }
}
