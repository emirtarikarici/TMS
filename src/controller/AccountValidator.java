package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountValidator {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public AccountValidator(Connection connection) {
        this.connection = connection;
    }

    public boolean validateUsername(String username) {
        try {
            preparedStatement = connection.prepareStatement(
                    "(SELECT * FROM user WHERE username = ?) UNION (SELECT * FROM organizer WHERE username = ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);
            resultSet = preparedStatement.executeQuery();
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
