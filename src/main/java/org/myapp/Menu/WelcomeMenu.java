package org.myapp.Menu;


public class WelcomeMenu {
    private final WelcomeMenuAction actions;

    public WelcomeMenu() {
        this.actions = new WelcomeMenuAction();
    }

    public void execute() {
        SubMenu welcomeMenu = new SubMenu("Welcome Menu");
        welcomeMenu.addMenuItem(new ActionMenuItem("Log In", actions::logIn));
        welcomeMenu.addMenuItem(new ActionMenuItem("Sign Up", actions::signUp));
        welcomeMenu.addMenuItem(new ActionMenuItem("Exit", this::exitApp));

        welcomeMenu.execute();
    }


    private void exitApp() {
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
