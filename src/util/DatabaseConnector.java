package util;

import java.sql.*;

public class DatabaseConnector {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/TechShopDB";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Abhi@4664#";

    public static Connection openConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}




