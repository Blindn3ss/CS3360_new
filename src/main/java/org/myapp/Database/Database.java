package org.myapp.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connection = null;
    @SuppressWarnings("CallToPrintStackTrace")
    public Connection connect(){
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/cs3360";
            String user = "root";
            String password = "zaq12wsx";

            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    public void disconnect(){
        try{
            if (connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
