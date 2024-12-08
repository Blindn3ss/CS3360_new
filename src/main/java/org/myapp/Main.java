package org.myapp;

import org.myapp.Menu.WelcomeMenu;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Application...");
        WelcomeMenu welcomeMenu = new WelcomeMenu();

        try {
            welcomeMenu.execute();
        } catch (RuntimeException e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
        System.out.println("Exiting Application...");
    }
}