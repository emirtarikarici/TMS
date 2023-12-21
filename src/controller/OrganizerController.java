package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrganizerController {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private AccountValidator accountValidator;

    public OrganizerController(Connection connection) {
        this.connection = connection;
        this.accountValidator = new AccountValidator(connection);
    }

    public boolean changeUsername(String username, String newUsername) {
        try {
            statement = connection.createStatement();
            if (!accountValidator.validateUsername(newUsername)) {
                return false;
            } else {
                statement.executeUpdate(String.format("UPDATE organizer SET username = '%s' WHERE username = '%s'",
                        newUsername, username));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changePassword(String username, String newPassword) {
        try {
            statement = connection.createStatement();
            if (!accountValidator.validatePassword(newPassword)) {
                return false;
            } else {
                statement.executeUpdate(String.format("UPDATE organizer SET password = '%s' WHERE username = '%s'",
                        newPassword, username));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getBalance(String username) {
        try {
            statement = connection.createStatement();
            resultSet = statement
                    .executeQuery(String.format("SELECT * FROM organizer WHERE username = '%s'", username));
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
}
