package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
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

    private boolean validateEvent(LocalDateTime date, String location) {
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

    public boolean createEvent(String name, String organizerUsername, LocalDateTime date, String location,
            int capacity) {
        try {
            statement = connection.createStatement();
            if (this.validateEvent(date, location)) {
                statement.executeUpdate(String.format(
                        "INSERT INTO event (name, organizerUsername, date, location, capacity) VALUES ('%s', '%s', '%s', '%s', %d)",
                        name, organizerUsername, date.toString(), location, capacity));
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateEvent(int eventId, String name, String organizerUsername, LocalDateTime date,
            String location, int capacity) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate(String.format(
                    "UPDATE event SET name = '%s', organizerUsername = '%s', date = '%s', location = '%s', capacity = %d WHERE eventId = %d",
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
                    "SELECT * FROM event WHERE userUsername = '%s'", userUsername));
            ArrayList<Event> events = new ArrayList<Event>();
            while (resultSet.next()) {
                events.add(new Event(resultSet.getInt("eventId"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getDate("date").toLocalDate().atStartOfDay(),
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
                events.add(new Event(resultSet.getInt("eventId"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getDate("date").toLocalDate().atStartOfDay(),
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

    public Event getEventById(int eventId) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM event WHERE eventId = %d", eventId));
            return new Event(resultSet.getInt("eventId"),
                    resultSet.getString("name"),
                    resultSet.getString("organizerUsername"),
                    resultSet.getDate("date").toLocalDate().atStartOfDay(),
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
                events.add(new Event(resultSet.getInt("eventId"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getDate("date").toLocalDate().atStartOfDay(),
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
                events.add(new Event(resultSet.getInt("eventId"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getDate("date").toLocalDate().atStartOfDay(),
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
                events.add(new Event(resultSet.getInt("eventId"),
                        resultSet.getString("name"),
                        resultSet.getString("organizerUsername"),
                        resultSet.getDate("date").toLocalDate().atStartOfDay(),
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

    public boolean isEventFull(int eventId) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT capacity, sold FROM event WHERE eventId = %d", eventId));
            return resultSet.getInt("capacity") == resultSet.getInt("sold");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEventExpired(int eventId) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(String.format(
                    "SELECT date FROM event WHERE eventId = %d", eventId));
            return resultSet.getDate("date").toLocalDate().atStartOfDay().isBefore(LocalDateTime.now());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
