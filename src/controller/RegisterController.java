package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class RegisterController {
    private Connection connection;
    private PreparedStatement preparedStatement;
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
                if (type == USER) {
                    preparedStatement = connection
                            .prepareStatement("INSERT INTO user (username, password) VALUES (?, ?)");
                } else if (type == ORGANIZER) {
                    preparedStatement = connection
                            .prepareStatement("INSERT INTO organizer (username, password) VALUES (?, ?)");
                }
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(new JFrame(), "Account Created!");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
