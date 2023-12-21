package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import model.Ticket;

public class TicketController {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public TicketController(Connection connection) {
        this.connection = connection;
    }

    public boolean createTicket(String userUsername, int eventId, double price) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate(
                    String.format("INSERT INTO ticket (userUsername, eventId, price) VALUES ('%s', %d, %f)",
                            userUsername, eventId, price));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTicketById(int id) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate(String.format("DELETE FROM ticket WHERE id = %d", id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTicketByUsernameAndEventId(String userUsername, int eventId) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate(String.format("DELETE FROM ticket WHERE userUsername = '%s' AND eventId = %d",
                    userUsername, eventId));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Ticket> getTicketsByUsername(String userUsername) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format("SELECT * FROM ticket WHERE userUsername = '%s'",
                    userUsername));
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
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format("SELECT * FROM ticket WHERE eventId = %d", eventId));
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
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ticket");
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
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format("SELECT * FROM ticket WHERE id = %d", id));
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
