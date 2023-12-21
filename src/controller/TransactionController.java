package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.Transaction;

import javax.swing.JFrame;

public class TransactionController {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public TransactionController(Connection connection) {
        this.connection = connection;
    }

    public boolean validateTransaction(String username, int eventId) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT user.balance AS balance, ticket.price AS price, event.capacity AS capacity, event.sold AS sold FROM user, event, ticket WHERE user.username = '%s' AND event.eventId = %d AND ticket.eventId = %d",
                    username, eventId, eventId));
            if (!(resultSet.getDouble("balance") >= resultSet.getDouble("price"))) {
                JOptionPane.showMessageDialog(new JFrame(), "Insufficient balance!");
                return false;
            } else if (!(resultSet.getInt("capacity") > resultSet.getInt("sold"))) {
                JOptionPane.showMessageDialog(new JFrame(), "Event is full!");
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createTransaction(String userUsername, String organizerUsername, double amount, int ticketId) {
        try {
            statement = connection.createStatement();
            if (this.validateTransaction(userUsername, ticketId)) {
                statement.executeUpdate(String.format(
                        "UPDATE user SET balance = balance - %f WHERE username = '%s'", amount, userUsername));
                statement.executeUpdate(String.format(
                        "UPDATE organizer SET balance = balance + %f WHERE username = '%s'", amount,
                        organizerUsername));
                statement.executeUpdate(String.format(
                        "UPDATE event SET sold = sold + 1 WHERE eventId = (SELECT eventId FROM ticket WHERE ticketId = %d)",
                        ticketId));
                statement.executeUpdate(String.format(
                        "INSERT INTO transaction (userUsername, organizerUsername, amount, status, ticketId) VALUES ('%s', '%s', %f, %d, %d)",
                        userUsername, organizerUsername, amount, Transaction.STATUS_COMPLETED, ticketId));
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelTransaction(int transactionId) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT userUsername, organizerUsername, amount, status, ticketId FROM transaction WHERE transactionId = %d",
                    transactionId));
            if (resultSet.getInt("status") == Transaction.STATUS_COMPLETED) {
                statement.executeUpdate(String.format(
                        "UPDATE user SET balance = balance + %f WHERE username = '%s'", resultSet.getDouble("amount"),
                        resultSet.getString("userUsername")));
                statement.executeUpdate(String.format(
                        "UPDATE organizer SET balance = balance - %f WHERE username = '%s'",
                        resultSet.getDouble("amount"), resultSet.getString("organizerUsername")));
                statement.executeUpdate(String.format(
                        "UPDATE event SET sold = sold - 1 WHERE eventId = (SELECT eventId FROM ticket WHERE ticketId = %d)",
                        resultSet.getInt("ticketId")));
                statement.executeUpdate(String.format(
                        "UPDATE transaction SET status = %d WHERE transactionId = %d",
                        Transaction.STATUS_CANCELLED, transactionId));
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Transaction> getTransactionByUser(String userUsername) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM transaction WHERE userUsername = '%s'", userUsername));
            ArrayList<Transaction> transactions = new ArrayList<Transaction>();
            while (resultSet.next()) {
                transactions.add(new Transaction(resultSet.getInt("id"),
                        resultSet.getString("userUsername"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("status"),
                        resultSet.getInt("ticketId")));
            }
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Transaction> getTransactionByOrganizer(String organizerUsername) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM transaction WHERE organizerUsername = '%s'", organizerUsername));
            ArrayList<Transaction> transactions = new ArrayList<Transaction>();
            while (resultSet.next()) {
                transactions.add(new Transaction(resultSet.getInt("transactionId"),
                        resultSet.getString("userUsername"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("status"),
                        resultSet.getInt("ticketId")));
            }
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Transaction getTransactionById(int transactionId) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM transaction WHERE transactionId = %d", transactionId));
            if (resultSet.next()) {
                return new Transaction(resultSet.getInt("transactionId"),
                        resultSet.getString("userUsername"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("status"),
                        resultSet.getInt("ticketId"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Transaction> getAllTransactions() {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM transaction");
            ArrayList<Transaction> transactions = new ArrayList<Transaction>();
            while (resultSet.next()) {
                transactions.add(new Transaction(resultSet.getInt("transactionId"),
                        resultSet.getString("userUsername"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("status"),
                        resultSet.getInt("ticketId")));
            }
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Transaction> getCompletedTransactions() {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM transaction WHERE status = 0");
            ArrayList<Transaction> transactions = new ArrayList<Transaction>();
            while (resultSet.next()) {
                transactions.add(new Transaction(resultSet.getInt("transactionId"),
                        resultSet.getString("userUsername"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("status"),
                        resultSet.getInt("ticketId")));
            }
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Transaction> getCancelledTransactions() {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM transaction WHERE status = 1");
            ArrayList<Transaction> transactions = new ArrayList<Transaction>();
            while (resultSet.next()) {
                transactions.add(new Transaction(resultSet.getInt("transactionId"),
                        resultSet.getString("userUsername"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("status"),
                        resultSet.getInt("ticketId")));
            }
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Transaction> getTransactionsByEvent(int eventId) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM transaction WHERE ticketId = (SELECT ticketId FROM ticket WHERE eventId = %d)",
                    eventId));
            ArrayList<Transaction> transactions = new ArrayList<Transaction>();
            while (resultSet.next()) {
                transactions.add(new Transaction(resultSet.getInt("transactionId"),
                        resultSet.getString("userUsername"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("status"),
                        resultSet.getInt("ticketId")));
            }
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Transaction getTransactionByTicket(int ticketId) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM transaction WHERE ticketId = %d", ticketId));
            if (resultSet.next()) {
                return new Transaction(resultSet.getInt("transactionId"),
                        resultSet.getString("userUsername"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getDouble("amount"),
                        resultSet.getInt("status"),
                        resultSet.getInt("ticketId"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}