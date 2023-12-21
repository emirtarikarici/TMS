package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class LoginController {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private boolean isLoggedIn;

    public LoginController(Connection connection) {
        this.connection = connection;
    }

    public boolean login(String username, String password) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "(SELECT * FROM user WHERE username = '%s' AND password = '%s') UNION (SELECT * FROM organizer WHERE username = '%s' AND password = '%s')",
                    username, password, username, password));
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
}
