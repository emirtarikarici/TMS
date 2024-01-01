import controller.DatabaseConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello CS 320!");


        Connection connection = new DatabaseConnection().getConnection();

        connection = new DatabaseConnection().getConnection();

        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.createTables();

        connection.close();
        System.exit(0);
    }
}
