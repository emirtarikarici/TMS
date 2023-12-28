package controller;

import java.sql.Connection;
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
    private Statement statement;
    private ResultSet resultSet;

    public EventController(Connection connection) {
        this.connection = connection;
    }

    private boolean validateEvent(Timestamp date, String location) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM event WHERE date = '%s' AND location = '%s'", date.toString(), location));
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

    public int createEvent(String name, String organizerUsername, Timestamp date, String location,
            int capacity) {
        try {
            statement = connection.createStatement();
            if (this.validateEvent(date, location)) {
                statement.executeUpdate(String.format(
                        "INSERT INTO event (name, organizerUsername, date, location, capacity) VALUES ('%s', '%s', '%s', '%s', %d)",
                        name, organizerUsername, date.toString(), location, capacity), Statement.RETURN_GENERATED_KEYS);
                resultSet = statement.getGeneratedKeys();
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
            String location, int capacity) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate(String.format(
                    "UPDATE event SET name = '%s', organizerUsername = '%s', date = '%s', location = '%s', capacity = %d WHERE id = %d",
                    name, organizerUsername, date.toString(), location, capacity, eventId));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Event> getEventsByUser(String userUsername) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM event WHERE id = (SELECT eventId FROM ticket WHERE userUsername = '%s')",
                    userUsername));
            ArrayList<Event> events = new ArrayList<Event>();
            while (resultSet.next()) {
                events.add(new Event(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("location"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("sold")));
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Event> getEventsByOrganizer(String organizerUsername) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM event WHERE organizerUsername = '%s'", organizerUsername));
            ArrayList<Event> events = new ArrayList<Event>();
            while (resultSet.next()) {
                events.add(new Event(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("location"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("sold")));
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Event getEventById(int id) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM event WHERE id = %d", id));
            resultSet.next();
            return new Event(resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("organizerUsername"),
                    resultSet.getTimestamp("date"),
                    resultSet.getString("location"),
                    resultSet.getInt("capacity"),
                    resultSet.getInt("sold"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Event> getExpiredEvents() {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM event WHERE date < NOW()");
            ArrayList<Event> events = new ArrayList<Event>();
            while (resultSet.next()) {
                events.add(new Event(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("location"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("sold")));
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Event> getUpcomingEvents() {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM event WHERE date > NOW()");
            ArrayList<Event> events = new ArrayList<Event>();
            while (resultSet.next()) {
                events.add(new Event(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("location"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("sold")));
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Event> getAllEvents() {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM event");
            ArrayList<Event> events = new ArrayList<Event>();
            while (resultSet.next()) {
                events.add(new Event(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getTimestamp("date"),
                        resultSet.getString("location"),
                        resultSet.getInt("capacity"),
                        resultSet.getInt("sold")));
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isEventFull(int id) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT capacity, sold FROM event WHERE id = %d", id));
            return (resultSet.next()) ? resultSet.getInt("capacity") == resultSet.getInt("sold") : false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEventExpired(int id) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT date FROM event WHERE id = %d", id));
            return (resultSet.next())
                    ? resultSet.getTimestamp("date").before(new Timestamp(System.currentTimeMillis()))
                    : false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
