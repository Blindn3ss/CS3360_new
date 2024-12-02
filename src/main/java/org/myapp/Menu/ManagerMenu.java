package org.myapp.Menu;

import org.myapp.Model.Customer;
import org.myapp.Model.Manager;

import java.util.Scanner;

public class ManagerMenu {
    Scanner scanner;
    private Manager loggedInManager;

    public ManagerMenu(Manager manager, Scanner scanner) {
        this.loggedInManager = manager;
        this.scanner = scanner;
    }


}
