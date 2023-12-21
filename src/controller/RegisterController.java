package controller;

import java.sql.Connection;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class RegisterController {
    private Connection connection;
    private Statement statement;
    private AccountValidator accountValidator;

    public static final int USER = 0;
    public static final int ORGANIZER = 1;

    public RegisterController(Connection connection) {
        this.connection = connection;
        this.accountValidator = new AccountValidator(connection);
    }

    public boolean register(String username, String password, int type) {
        try {
            if (!accountValidator.validateUsername(username)) {
                JOptionPane.showMessageDialog(new JFrame(), "Username already exists!");
                return false;
            } else if (!accountValidator.validatePassword(password)) {
                JOptionPane.showMessageDialog(new JFrame(), "Password must be at least 6 characters!");
                return false;
            } else {
                statement = connection.createStatement();
                if (type == USER) {
                    statement.executeUpdate(String.format("INSERT INTO user (username, password) VALUES ('%s', '%s')",
                            username, password));
                } else if (type == ORGANIZER) {
                    statement.executeUpdate(String.format(
                            "INSERT INTO organizer (username, password) VALUES ('%s', '%s')", username, password));
                }
                JOptionPane.showMessageDialog(new JFrame(), "Account Created!");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
