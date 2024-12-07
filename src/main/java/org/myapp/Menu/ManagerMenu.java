package org.myapp.Menu;

import org.myapp.Model.Manager;


public class ManagerMenu {
    private final ManagerMenuAction managerActions;

    public ManagerMenu(Manager loggedInManager) {
        this.managerActions = new ManagerMenuAction(loggedInManager);
    }

    public void execute() {

        //Lv.3
        SubMenu scheduleAndBooking = new SubMenu("Schedule and Bookings");
        scheduleAndBooking.addMenuItem(new ActionMenuItem("View Schedule" , managerActions::viewSchedule));
        scheduleAndBooking.addMenuItem(new ActionMenuItem("Confirm a Booking" , managerActions::confirmBooking));
        scheduleAndBooking.addMenuItem(new ActionMenuItem("Back", () -> {}));

        // Lv.2
        SubMenu profileMenu = new SubMenu("Profile");
        profileMenu.addMenuItem(new ActionMenuItem("View Profile", managerActions::viewProfile));
        profileMenu.addMenuItem(new ActionMenuItem("Edit Profile", managerActions::editProfile));
        profileMenu.addMenuItem(new ActionMenuItem("Back", () -> {}));


        SubMenu managerSection = new SubMenu("Yards And Bookings");
        managerSection.addMenuItem(new ActionMenuItem("Add new Yard", managerActions::addNewYard));
        managerSection.addMenuItem(new ActionMenuItem("Edit managed yards", managerActions::editManagedYards));
        managerSection.addMenuItem(new ActionMenuItem("Register to manage", managerActions::registerToManage));
        managerSection.addMenuItem(new ActionMenuItem("Remove Management", managerActions::removeManagement));
        managerSection.addMenuItem(scheduleAndBooking);
        managerSection.addMenuItem(new ActionMenuItem("Revenue", managerActions::viewRevenue));
        managerSection.addMenuItem(new ActionMenuItem("Back", () -> {}));

        //Lv1
        SubMenu managerManu = new SubMenu("Manager Menu");
        managerManu.addMenuItem(profileMenu);
        managerManu.addMenuItem(managerSection);
        managerManu.addMenuItem(new ActionMenuItem("Log Out", () -> {}));

        managerManu.execute();
    }
}
