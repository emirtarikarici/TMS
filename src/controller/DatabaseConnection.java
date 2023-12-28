package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Database;

public class DatabaseConnection {
    private Connection connection;
    private boolean isConnected = false;

    public DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Couldn't Find JDBC Driver");
        }

        try {
            this.connection = DriverManager.getConnection(Database.url, Database.username, Database.password);
            isConnected = true;
            System.out.println("Connected to Database!");
            this.createTables();
            System.out.println("Tables Created!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Couldn't Connect to Database!");
            System.exit(1);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public void closeConnection() {
        try {
            this.connection.close();
            System.out.println("Disconnected from Database!");
        } catch (SQLException e) {
            System.out.println("Couldn't Disconnect from Database!");
        }
    }

    public void createTables() {
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `user` (" +
                    "`username` VARCHAR(255) NOT NULL PRIMARY KEY," +
                    "`password` VARCHAR(255) NOT NULL," +
                    "`balance` DOUBLE DEFAULT 0.0)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `organizer` (" +
                    "`username` VARCHAR(255) NOT NULL PRIMARY KEY," +
                    "`password` VARCHAR(255) NOT NULL," +
                    "`balance` DOUBLE DEFAULT 0.0)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `event` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "`name` VARCHAR(255) NOT NULL," +
                    "`organizerUsername` VARCHAR(255) NOT NULL REFERENCES `organizer`(`username`)," +
                    "`date` TIMESTAMP NOT NULL," +
                    "`location` VARCHAR(255) NOT NULL," +
                    "`capacity` INT NOT NULL," +
                    "`sold` INT DEFAULT 0)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `ticket` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "`userUsername` VARCHAR(255) NOT NULL REFERENCES `user`(`username`)," +
                    "`eventId` INT NOT NULL REFERENCES `event`(`id`)," +
                    "`price` DOUBLE NOT NULL)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `transaction` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "`userUsername` VARCHAR(255) NOT NULL REFERENCES `user`(`username`)," +
                    "`organizerUsername` VARCHAR(255) NOT NULL REFERENCES `organizer`(`username`)," +
                    "`amount` DOUBLE NOT NULL," +
                    "`status` INT NOT NULL," +
                    "`ticketId` INT NOT NULL REFERENCES `ticket`(`id`))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
