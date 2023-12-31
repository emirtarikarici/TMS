package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import model.Ticket;

public class TicketController {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public TicketController(Connection connection) {
        this.connection = connection;
    }

    public int createTicket(String userUsername, int eventId, double price) {
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO ticket (userUsername, eventId, price) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, userUsername);
            preparedStatement.setInt(2, eventId);
            preparedStatement.setDouble(3, price);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getInt(1) : -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean deleteTicketById(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM ticket WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTicketByUsernameAndEventId(String userUsername, int eventId) {
        try {
            preparedStatement = connection
                    .prepareStatement("DELETE FROM ticket WHERE userUsername = ? AND eventId = ?");
            preparedStatement.setString(1, userUsername);
            preparedStatement.setInt(2, eventId);
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Ticket> getTicketsByUsername(String userUsername) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM ticket WHERE userUsername = ?");
            preparedStatement.setString(1, userUsername);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Ticket> tickets = new ArrayList<Ticket>();
            while (resultSet.next()) {
                tickets.add(new Ticket(resultSet.getInt("id"), resultSet.getString("userUsername"),
                        resultSet.getInt("eventId"), resultSet.getDouble("price")));
            }
            return tickets;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Ticket> getTicketsByEventId(int eventId) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM ticket WHERE eventId = ?");
            preparedStatement.setInt(1, eventId);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Ticket> tickets = new ArrayList<Ticket>();
            while (resultSet.next()) {
                tickets.add(new Ticket(resultSet.getInt("id"), resultSet.getString("userUsername"),
                        resultSet.getInt("eventId"), resultSet.getDouble("price")));
            }
            return tickets;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Ticket> getAllTickets() {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM ticket");
            resultSet = preparedStatement.executeQuery();
            ArrayList<Ticket> tickets = new ArrayList<Ticket>();
            while (resultSet.next()) {
                tickets.add(new Ticket(resultSet.getInt("id"), resultSet.getString("userUsername"),
                        resultSet.getInt("eventId"), resultSet.getDouble("price")));
            }
            return tickets;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Ticket getTicketById(int id) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM ticket WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Ticket(resultSet.getInt("id"), resultSet.getString("userUsername"),
                        resultSet.getInt("eventId"), resultSet.getDouble("price"));
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
