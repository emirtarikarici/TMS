package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Organizer;

public class OrganizerController {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private AccountValidator accountValidator;

    public OrganizerController(Connection connection) {
        this.connection = connection;
        this.accountValidator = new AccountValidator(connection);
    }

    public boolean changeUsername(String username, String newUsername) {
        try {
            if (!accountValidator.validateUsername(newUsername)) {
                return false;
            } else {
                preparedStatement = connection.prepareStatement("UPDATE organizer SET username = ? WHERE username = ?");
                preparedStatement.setString(1, newUsername);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changePassword(String username, String newPassword) {
        try {
            if (!accountValidator.validatePassword(newPassword)) {
                return false;
            } else {
                preparedStatement = connection.prepareStatement("UPDATE organizer SET password = ? WHERE username = ?");
                preparedStatement.setString(1, newPassword);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getBalance(String username) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM organizer WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Organizer getOrganizerByUsername(String username) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM organizer WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Organizer(resultSet.getString("username"), resultSet.getString("password"),
                        resultSet.getDouble("balance"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
