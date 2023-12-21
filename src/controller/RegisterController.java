package controller;

import java.sql.Connection;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class RegisterController {
    private Connection connection;
    private Statement statement;
    private AccountValidator accountValidator;

    public RegisterController(Connection connection) {
        this.connection = connection;
        this.accountValidator = new AccountValidator(connection);
    }

    public boolean register(String username, String password) {
        try {
            if (!accountValidator.validateUsername(username)) {
                JOptionPane.showMessageDialog(new JFrame(), "Username already exists!");
                return false;
            } else if (!accountValidator.validatePassword(password)) {
                JOptionPane.showMessageDialog(new JFrame(), "Password must be at least 6 characters!");
                return false;
            } else {
                statement = connection.createStatement();
                statement.executeUpdate(String.format("INSERT INTO user (username, password) VALUES ('%s', '%s')",
                        username, password));
                JOptionPane.showMessageDialog(new JFrame(), "Account Created!");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
