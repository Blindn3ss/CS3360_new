package org.myapp.Menu;

public abstract class MenuItem {
    private String name;

    public MenuItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void execute(); // Action to be performed when the item is selected
}

