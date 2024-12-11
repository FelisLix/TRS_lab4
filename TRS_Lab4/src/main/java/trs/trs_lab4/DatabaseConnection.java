package trs.trs_lab4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mariadb://localhost:3306/aquarium";
    private static final String USER = "cat";
    private static final String PASSWORD = "123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
