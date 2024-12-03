package org.myapp.Menu;

import org.myapp.Model.Manager;

import java.util.Scanner;

import static org.myapp.Menu.Utility.isValidEmail;


public class ManagerMenu {
    Scanner scanner;
    private final Manager loggedInManager;

    public ManagerMenu(Manager manager, Scanner scanner) {
        this.loggedInManager = manager;
        this.scanner = scanner;
    }

    public void execute() {

        //Lv.3

        // Lv.2
        SubMenu profileMenu = new SubMenu("Profile");
        profileMenu.addMenuItem(new ActionMenuItem("View Profile", this::viewProfile));
        profileMenu.addMenuItem(new ActionMenuItem("Edit Profile", this::editProfile));
        profileMenu.addMenuItem(new ActionMenuItem("Back", () -> {}));


        SubMenu managerSection = new SubMenu("Yards And Bookings");
        managerSection.addMenuItem(new ActionMenuItem("Add new Yard", () -> {}));
        managerSection.addMenuItem(new ActionMenuItem("Edit managed yards", ()-> {}));
        managerSection.addMenuItem(new ActionMenuItem("Register to manage", ()->{}));
        managerSection.addMenuItem(new ActionMenuItem("Schedule and Bookings", ()->{}));
        managerSection.addMenuItem(new ActionMenuItem("Revenue", ()->{}));
        managerSection.addMenuItem(new ActionMenuItem("Back", () -> {}));

        //Lv1
        SubMenu managerManu = new SubMenu("Manager Menu");
        managerManu.addMenuItem(profileMenu);
        managerManu.addMenuItem(managerSection);
        managerManu.addMenuItem(new ActionMenuItem("Log Out", () -> {}));

        managerManu.execute();
    }

    private void editProfile() {

    }

    private void viewProfile() {
        loggedInManager.viewProfileDetails();
    }

}
