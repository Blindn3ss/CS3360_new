package org.myapp;

import org.myapp.Menu.WelcomeMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello world!");
        System.out.println("Starting Application...");
        WelcomeMenu welcomeMenu = new WelcomeMenu(scanner);

        try {
            welcomeMenu.execute();
        } catch (RuntimeException e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
        System.out.println("Exiting Application...");

    }
}