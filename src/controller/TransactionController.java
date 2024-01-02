package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Ticket;
import model.Transaction;

public class TransactionController {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public TransactionController(Connection connection) {
        this.connection = connection;
    }

    public boolean validateTransaction(String username, int ticketId) {
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT COUNT(*) AS c FROM ticket WHERE userUsername = ? AND status = ? AND eventId IN (SELECT id FROM event WHERE date = (SELECT date FROM event WHERE id = (SELECT eventId FROM ticket WHERE id = ?)))");
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, Ticket.ACTIVE);
            preparedStatement.setInt(3, ticketId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (resultSet.getInt("c") > 1) {
                JOptionPane.showMessageDialog(new JFrame(),
                        "You already have a ticket for another event at this time and date!");
                preparedStatement = connection.prepareStatement("DELETE FROM ticket WHERE id = ?");
                preparedStatement.setInt(1, ticketId);
                preparedStatement.executeUpdate();
                return false;
            }
            preparedStatement = connection.prepareStatement(
                    "SELECT user.balance AS balance, event.price AS price, event.capacity AS capacity, event.sold AS sold FROM user, event, ticket WHERE user.username = ? AND ticket.id = ? AND event.id = (SELECT eventId FROM ticket WHERE id = ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, ticketId);
            preparedStatement.setInt(3, ticketId);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(new JFrame(), "User, event or ticket not found!");
                return false;
            } else if (!(resultSet.getDouble("balance") >= resultSet.getDouble("price"))) {
                JOptionPane.showMessageDialog(new JFrame(), "Insufficient balance!");
                preparedStatement = connection.prepareStatement("DELETE FROM ticket WHERE id = ?");
                preparedStatement.setInt(1, ticketId);
                preparedStatement.executeUpdate();
                return false;
            } else if (!(resultSet.getInt("capacity") > resultSet.getInt("sold"))) {
                JOptionPane.showMessageDialog(new JFrame(), "Event is full!");
                preparedStatement = connection.prepareStatement("DELETE FROM ticket WHERE id = ?");
                preparedStatement.setInt(1, ticketId);
                preparedStatement.executeUpdate();
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int createTransaction(String userUsername, String organizerUsername, int ticketId) {
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT event.price FROM ticket, event WHERE ticket.id = ? AND event.id = (SELECT eventId FROM ticket WHERE id = ?)");
            preparedStatement.setInt(1, ticketId);
            preparedStatement.setInt(2, ticketId);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(new JFrame(), "Ticket or event not found!");
                return -1;
            }
            double amount = resultSet.getDouble("price");
            if (this.validateTransaction(userUsername, ticketId)) {
                preparedStatement = connection.prepareStatement(
                        "UPDATE user SET balance = balance - ? WHERE username = ?");
                preparedStatement.setDouble(1, amount);
                preparedStatement.setString(2, userUsername);
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement(
                        "UPDATE organizer SET balance = balance + ? WHERE username = ?");
                preparedStatement.setDouble(1, amount);
                preparedStatement.setString(2, organizerUsername);
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement(
                        "UPDATE event SET sold = sold + 1 WHERE id = (SELECT eventId FROM ticket WHERE id = ?)");
                preparedStatement.setInt(1, ticketId);
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO transaction (userUsername, organizerUsername, amount, status, ticketId) VALUES (?, ?, ?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, userUsername);
                preparedStatement.setString(2, organizerUsername);
                preparedStatement.setDouble(3, amount);
                preparedStatement.setInt(4, Transaction.STATUS_COMPLETED);
                preparedStatement.setInt(5, ticketId);
                preparedStatement.executeUpdate();
                resultSet = preparedStatement.getGeneratedKeys();
                return resultSet.next() ? resultSet.getInt(1) : -1;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean cancelTransaction(int id) {
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT userUsername, organizerUsername, amount, status, ticketId FROM transaction WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(new JFrame(), "Transaction not found!");
                return false;
            } else if (resultSet.getInt("status") == Transaction.STATUS_COMPLETED) {
                preparedStatement = connection.prepareStatement(
                        "UPDATE user SET balance = balance + ? WHERE username = ?");
                preparedStatement.setDouble(1, resultSet.getDouble("amount"));
                preparedStatement.setString(2, resultSet.getString("userUsername"));
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement(
                        "UPDATE organizer SET balance = balance - ? WHERE username = ?");
                preparedStatement.setDouble(1, resultSet.getDouble("amount"));
                preparedStatement.setString(2, resultSet.getString("organizerUsername"));
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement(
                        "UPDATE event SET sold = sold - 1 WHERE id = (SELECT eventId FROM ticket WHERE id = ?)");
                preparedStatement.setInt(1, resultSet.getInt("ticketId"));
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement(
                        "UPDATE transaction SET status = ? WHERE id = ?");
                preparedStatement.setInt(1, Transaction.STATUS_CANCELLED);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement(
                        "UPDATE ticket SET status = ? WHERE id = ?");
                preparedStatement.setInt(1, Ticket.CANCELLED);
                preparedStatement.setInt(2, resultSet.getInt("ticketId"));
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

    public ArrayList<Transaction> getTransactionByUser(String userUsername) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM transaction WHERE userUsername = ?");
            preparedStatement.setString(1, userUsername);
            resultSet = preparedStatement.executeQuery();
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
            preparedStatement = connection.prepareStatement("SELECT * FROM transaction WHERE organizerUsername = ?");
            preparedStatement.setString(1, organizerUsername);
            resultSet = preparedStatement.executeQuery();
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

    public Transaction getTransactionById(int id) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM transaction WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Transaction(resultSet.getInt("id"),
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
            preparedStatement = connection.prepareStatement("SELECT * FROM transaction");
            resultSet = preparedStatement.executeQuery();
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

    public ArrayList<Transaction> getCompletedTransactions() {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM transaction WHERE status = ?");
            preparedStatement.setInt(1, Transaction.STATUS_COMPLETED);
            resultSet = preparedStatement.executeQuery();
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

    public ArrayList<Transaction> getCancelledTransactions() {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM transaction WHERE status = ?");
            preparedStatement.setInt(1, Transaction.STATUS_CANCELLED);
            resultSet = preparedStatement.executeQuery();
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

    public ArrayList<Transaction> getTransactionsByEvent(int eventId) {
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM transaction WHERE ticketId = (SELECT id FROM ticket WHERE eventId = ?)");
            preparedStatement.setInt(1, eventId);
            resultSet = preparedStatement.executeQuery();
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

    public Transaction getTransactionByTicket(int ticketId) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM transaction WHERE ticketId = ?");
            preparedStatement.setInt(1, ticketId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Transaction(resultSet.getInt("id"),
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
