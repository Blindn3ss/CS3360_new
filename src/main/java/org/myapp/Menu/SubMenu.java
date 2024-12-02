package org.myapp.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SubMenu extends MenuItem {
    private List<MenuItem> items;

    public SubMenu(String name) {
        super(name);
        this.items = new ArrayList<>();
    }

    public void addMenuItem(MenuItem item) {
        items.add(item);
    }

    @Override
    public void execute() {
        while (true) {
            System.out.println("\n\t\t\t--- " + getName() + " ---");

            int maxWidth = 25;

            for (int i = 0; i < items.size(); i++) {
                String optionText = String.format("%-2d. %-"+maxWidth+"s", i + 1, items.get(i).getName());

                System.out.print(optionText);

                if (i % 2 == 0 && i + 1 < items.size()) {
                    System.out.print("\t");
                }

                if (i % 2 != 0 || i == items.size() - 1) {
                    System.out.println();
                }
            }

            int choice = getUserChoice();
            MenuItem selectedItem = items.get(choice - 1);

            if (choice == items.size()) {
                if (Objects.equals(selectedItem.getName(), "Log Out")) {
                    selectedItem.execute();
                    break;
                } else if (Objects.equals(selectedItem.getName(), "Back")) {
                    break;
                } else {
                    selectedItem.execute();
                }
            } else {
                selectedItem.execute();
            }
        }
    }



    private int getUserChoice() {
        int choice;
        while (true) {
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(new java.util.Scanner(System.in).nextLine());
                if (choice >= 0 && choice <= items.size()) {
                    return choice;
                }
            } catch (NumberFormatException e) {
                // Ignore invalid input
            }
            System.out.println("Invalid choice. Please try again.");
        }
    }

    public List<MenuItem> getItems() {
        return items;
    }
}


