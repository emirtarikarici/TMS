package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class UserController {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private AccountValidator accountValidator;

    public UserController(Connection connection) {
        this.connection = connection;
        this.accountValidator = new AccountValidator(connection);
    }

    public boolean changeUsername(String username, String newUsername) {
        try {
            if (!accountValidator.validateUsername(newUsername)) {
                return false;
            } else {
                preparedStatement = connection.prepareStatement("UPDATE user SET username = ? WHERE username = ?");
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
                preparedStatement = connection.prepareStatement("UPDATE user SET password = ? WHERE username = ?");
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

    public boolean addBalance(String username, double amount) {
        try {
            preparedStatement = connection.prepareStatement(("SELECT * FROM user WHERE username = ?"));
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                preparedStatement = connection.prepareStatement("UPDATE user SET balance = ? WHERE username = ?");
                preparedStatement.setDouble(1, resultSet.getDouble("balance") + amount);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getBalance(String username) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            return (resultSet.next()) ? resultSet.getDouble("balance") : -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public User getUserByUsername(String username) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getString("username"), resultSet.getString("password"),
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
