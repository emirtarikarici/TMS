package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class LoginController {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private boolean isLoggedIn;

    public LoginController(Connection connection) {
        this.connection = connection;
    }

    public boolean login(String username, String password) {
        try {
            preparedStatement = connection.prepareStatement(
                    "(SELECT * FROM user WHERE username = ? AND password = ?) UNION (SELECT * FROM organizer WHERE username = ? AND password = ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (!password.equals(resultSet.getString("password"))) {
                    JOptionPane.showMessageDialog(new JFrame(), "Password is incorrect!");
                    return false;
                } else {
                    this.isLoggedIn = true;
                    return true;
                }
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "User does not exist!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logout() {
        this.isLoggedIn = false;
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public int getAccountType(String username) {
        try {
            /*
             * statement = connection.createStatement();
             * if (statement.executeQuery(String.format(
             * "SELECT * FROM user WHERE username = '%s'", username)).next()) {
             * return RegisterController.USER;
             * } else if (statement.executeQuery(String.format(
             * "SELECT * FROM organizer WHERE username = '%s'", username)).next()) {
             * return RegisterController.ORGANIZER;
             * } else {
             * System.out.println("User does not exist!");
             * return -1;
             * }
             */

            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return RegisterController.USER;
            } else {
                preparedStatement = connection.prepareStatement("SELECT * FROM organizer WHERE username = ?");
                preparedStatement.setString(1, username);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return RegisterController.ORGANIZER;
                } else {
                    System.out.println("User does not exist!");
                    return -1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
