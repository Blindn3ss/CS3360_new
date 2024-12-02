package org.myapp.DAO;

import org.myapp.Model.Manager;

import java.util.List;

public interface ManagerDAO {

    boolean createManager(Manager manager);

    Manager getManagerById(int managerId);

    boolean updateManager(Manager manager);

    boolean deleteManager(int managerId);

    Manager getManagerByUserName(String username);

    List<Manager> getAllManagers();
}


