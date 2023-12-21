package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AccountValidator {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public AccountValidator(Connection connection) {
        this.connection = connection;
    }

    public boolean validateUsername(String username) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "(SELECT * FROM user WHERE username = '%s') UNION (SELECT * FROM organizer WHERE username = '%s')",
                    username,
                    username));
            if (resultSet.next()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validatePassword(String password) {
        return password.length() >= 6;
    }
}
