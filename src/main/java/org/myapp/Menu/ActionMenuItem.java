package org.myapp.Menu;

public class ActionMenuItem extends MenuItem {
    private Runnable action;

    public ActionMenuItem(String name, Runnable action) {
        super(name);
        this.action = action;
    }

    @Override
    public void execute() {
        action.run();
    }
}
