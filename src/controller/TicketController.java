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

    public int createTicket(String userUsername, int eventId) {
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO ticket (userUsername, eventId, status) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, userUsername);
            preparedStatement.setInt(2, eventId);
            preparedStatement.setInt(3, Ticket.ACTIVE);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getInt(1) : -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
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
                        resultSet.getInt("eventId"), resultSet.getInt("status")));
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
                        resultSet.getInt("eventId"), resultSet.getInt("status")));
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
                        resultSet.getInt("eventId"), resultSet.getInt("status")));
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
                        resultSet.getInt("eventId"), resultSet.getInt("status"));
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
