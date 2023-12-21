package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
}
