package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Event;

public class EventController {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public EventController(Connection connection) {
        this.connection = connection;
    }

    private boolean validateEvent(Timestamp date, String location) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM event WHERE date = ? AND location = ?");
            preparedStatement.setTimestamp(1, date);
            preparedStatement.setString(2, location);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(new JFrame(), "An event already exists at specified date and location!");
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int createEvent(String name, String organizerUsername, Timestamp date, String location, int capacity,
            double price) {
        try {
            if (this.validateEvent(date, location)) {
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO event (name, organizerUsername, date, location, capacity, sold, price) VALUES (?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, organizerUsername);
                preparedStatement.setTimestamp(3, date);
                preparedStatement.setString(4, location);
                preparedStatement.setInt(5, capacity);
                preparedStatement.setInt(6, 0);
                preparedStatement.setDouble(7, price);
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

    public boolean updateEvent(int eventId, String name, String organizerUsername, Timestamp date,
            String location, int capacity, double price) {
        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE event SET name = ?, organizerUsername = ?, date = ?, location = ?, capacity = ?, price = ? WHERE id = ?");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, organizerUsername);
            preparedStatement.setTimestamp(3, date);
            preparedStatement.setString(4, location);
            preparedStatement.setInt(5, capacity);
            preparedStatement.setDouble(6, price);
            preparedStatement.setInt(7, eventId);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Event> getEventsByUser(String userUsername) {
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM event WHERE id IN (SELECT eventId FROM ticket WHERE userUsername = ?)");
            preparedStatement.setString(1, userUsername);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Event> events = new ArrayList<Event>();
            while (resultSet.next()) {
                events.add(new Event(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("location"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("sold"),
                        resultSet.getDouble("price")));
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Event> getEventsByOrganizer(String organizerUsername) {
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM event WHERE organizerUsername = ?");
            preparedStatement.setString(1, organizerUsername);
            resultSet = preparedStatement.executeQuery();
            ArrayList<Event> events = new ArrayList<Event>();
            while (resultSet.next()) {
                events.add(new Event(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("location"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("sold"),
                        resultSet.getDouble("price")));
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Event getEventById(int id) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM event WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return new Event(resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("organizerUsername"),
                    resultSet.getTimestamp("date"),
                    resultSet.getString("location"),
                    resultSet.getInt("capacity"),
                    resultSet.getInt("sold"),
                    resultSet.getDouble("price"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Event> getExpiredEvents() {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM event WHERE date < NOW()");
            resultSet = preparedStatement.executeQuery();
            ArrayList<Event> events = new ArrayList<Event>();
            while (resultSet.next()) {
                events.add(new Event(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("location"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("sold"),
                        resultSet.getDouble("price")));
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Event> getUpcomingEvents() {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM event WHERE date > NOW()");
            resultSet = preparedStatement.executeQuery();
            ArrayList<Event> events = new ArrayList<Event>();
            while (resultSet.next()) {
                events.add(new Event(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("location"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("sold"),
                        resultSet.getDouble("price")));
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Event> getAllEvents() {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM event");
            resultSet = preparedStatement.executeQuery();
            ArrayList<Event> events = new ArrayList<Event>();
            while (resultSet.next()) {
                events.add(new Event(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("location"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("sold"),
                        resultSet.getDouble("price")));
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isEventFull(int id) {
        try {
            preparedStatement = connection.prepareStatement("SELECT capacity, sold FROM event WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            return (resultSet.next()) ? resultSet.getInt("capacity") == resultSet.getInt("sold") : false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEventExpired(int id) {
        try {
            preparedStatement = connection.prepareStatement("SELECT date FROM event WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            return (resultSet.next()) ? resultSet.getTimestamp("date").before(new Timestamp(System.currentTimeMillis()))
                    : false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
