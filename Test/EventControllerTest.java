import controller.*;
import model.Database;
import model.Event;
import model.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class EventControllerTest {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private Controller controller;
    private EventController eventController;

    private TicketController ticketController;


    @BeforeEach
    void setUp() {
        this.connection = new DatabaseConnection().getConnection();
        this.controller = new Controller(connection);
        eventController = new EventController(connection);
        ticketController = new TicketController(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        Main.dropAllTables(connection);
        connection.close();
    }

    @Test
    void createEvent() {
        try {
            statement = connection.createStatement();
            controller.registerController.register("xxx", "111111", RegisterController.ORGANIZER);
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM event");
            resultSet.next();
            int initialEventCount = resultSet.getInt(1);


            eventController.createEvent("Test Event", "xxx", new Timestamp(System.currentTimeMillis()), "Test Location", 100,10.30);

            resultSet = statement.executeQuery("SELECT COUNT(*) FROM event");
            resultSet.next();
            int finalEventCount = resultSet.getInt(1);


            assertEquals(initialEventCount + 1, finalEventCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    void updateEvent() {
        try {
            statement = connection.createStatement();
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp date = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(14));
            controller.registerController.register("xxx", "111111", RegisterController.ORGANIZER);
            int eventID_1 = eventController.createEvent("Initial Event", "xxx", new Timestamp(currentTimeMillis), "Initial Location", 100, 20.50);
            boolean x = eventController.updateEvent(eventID_1,"Updated Event", "xxx", date, "Updated Location", 150, 40.50);
            System.out.println(x);
            assertEquals(eventController.getEventById(eventID_1).getName(), "Updated Event");
            assertEquals(eventController.getEventById(eventID_1).getOrganizerUsername(), "xxx");

            long expectedTime = eventController.getEventById(eventID_1).getDate().getTime();
            long actualTime = date.getTime();
            long tolerance = 1000;

            assertTrue(Math.abs(expectedTime - actualTime) <= tolerance);

            assertEquals(eventController.getEventById(eventID_1).getLocation(), "Updated Location");
            assertEquals(eventController.getEventById(eventID_1).getCapacity(), 150);
            assertEquals(eventController.getEventById(eventID_1).getPrice(), 40.50);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    void getEventsByUser() {
        try {
            statement = connection.createStatement();
            long currentTimeMillis = System.currentTimeMillis();

            controller.registerController.register("abc", "123456", RegisterController.USER);
            controller.registerController.register("xxx", "111111", RegisterController.ORGANIZER);
            controller.registerController.register("yyy", "111111", RegisterController.ORGANIZER);

            int id1 = controller.eventController.createEvent("EventName1", "xxx", new Timestamp(currentTimeMillis), "location1", 30, 70.29);
            int id2 = controller.eventController.createEvent("EventName2", "yyy", new Timestamp(currentTimeMillis), "location2", 40, 40.39);

            int ticket1 = controller.ticketController.createTicket("abc", id1);
            int ticket2 = controller.ticketController.createTicket("abc", id2);

            ArrayList<Ticket> tickets = controller.ticketController.getTicketsByUsername("abc");

            assertNotNull(tickets);

            assertEquals(2, tickets.size());

            assertEquals("EventName1", controller.eventController.getEventById(tickets.get(0).getEventId()).getName());
            assertEquals("EventName2", controller.eventController.getEventById(tickets.get(1).getEventId()).getName());
            assertEquals(70.29, controller.eventController.getEventById(tickets.get(0).getEventId()).getPrice());
            assertEquals(40.39, controller.eventController.getEventById(tickets.get(1).getEventId()).getPrice());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }
    }


    @Test
    void getEventsByOrganizer() {
        try {
            statement = connection.createStatement();
            long currentTimeMillis = System.currentTimeMillis();
            controller.registerController.register("aaa", "111111", RegisterController.ORGANIZER);

            int id3 = eventController.createEvent("EventName3", "aaa", new Timestamp(currentTimeMillis), "location3", 20,40.50);
            int id4 = eventController.createEvent("EventName4", "aaa", new Timestamp(currentTimeMillis), "location4", 60, 69.49);

            ArrayList<Event> events = eventController.getEventsByOrganizer("aaa");

            assertNotNull(events);

            assertEquals(2, events.size());
            assertEquals("EventName3", events.get(0).getName());
            assertEquals("EventName4", events.get(1).getName());
            assertEquals(40.50, events.get(0).getPrice());
            assertEquals(69.49, events.get(1).getPrice());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }
    }


    @Test
    void getEventById() {
        try {
            statement = connection.createStatement();
            long currentTimeMillis = System.currentTimeMillis();
            controller.registerController.register("xxx", "111111", RegisterController.ORGANIZER);

            int id5 = eventController.createEvent("EventName5", "xxx", new Timestamp(currentTimeMillis), "location5", 70, 20.30);
            assertEquals("EventName5", eventController.getEventById(id5).getName());
            assertEquals("location5", eventController.getEventById(id5).getLocation());
            assertEquals("xxx", eventController.getEventById(id5).getOrganizerUsername());
            assertEquals(70, eventController.getEventById(id5).getCapacity());
            assertEquals(20.30, eventController.getEventById(id5).getPrice());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }
    }


    @Test
    void getExpiredEvents() {
        try {
            statement = connection.createStatement();
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp date = new Timestamp(currentTimeMillis - TimeUnit.DAYS.toMillis(7));
            Timestamp date2 = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(3));

            controller.registerController.register("eee", "111111", RegisterController.ORGANIZER);
            controller.registerController.register("ppp", "111111", RegisterController.ORGANIZER);

            eventController.createEvent("Expired1", "eee", new Timestamp(date.getTime()), "location*", 20, 10.40);
            eventController.createEvent("notExpired1", "ppp", new Timestamp(date2.getTime()), "location-", 40, 70.50);

            assertEquals(1, eventController.getExpiredEvents().size());
            assertEquals("Expired1", eventController.getExpiredEvents().get(0).getName());
            assertEquals("location*", eventController.getExpiredEvents().get(0).getLocation());
            assertEquals("eee", eventController.getExpiredEvents().get(0).getOrganizerUsername());
            assertEquals(20, eventController.getExpiredEvents().get(0).getCapacity());
            assertEquals(10.40, eventController.getExpiredEvents().get(0).getPrice());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void getUpcomingEvents() {
        try {
            statement = connection.createStatement();
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp date = new Timestamp(currentTimeMillis - TimeUnit.DAYS.toMillis(7));
            Timestamp date2 = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(3));

            controller.registerController.register("eee", "111111", RegisterController.ORGANIZER);
            controller.registerController.register("ppp", "111111", RegisterController.ORGANIZER);

            eventController.createEvent("Expired1", "eee", new Timestamp(date.getTime()), "location*", 20, 10.40);
            eventController.createEvent("notExpired1", "ppp", new Timestamp(date2.getTime()), "location-", 40, 70.50);

            assertEquals(1, eventController.getUpcomingEvents().size());
            assertEquals("notExpired1", eventController.getUpcomingEvents().get(0).getName());
            assertEquals("location-", eventController.getUpcomingEvents().get(0).getLocation());
            assertEquals("ppp", eventController.getUpcomingEvents().get(0).getOrganizerUsername());
            assertEquals(40, eventController.getUpcomingEvents().get(0).getCapacity());
            assertEquals(70.50, eventController.getUpcomingEvents().get(0).getPrice());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }

    }

    @Test
    void getAllEvents() {
        try {
            statement = connection.createStatement();
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp date = new Timestamp(currentTimeMillis - TimeUnit.DAYS.toMillis(7));
            Timestamp date2 = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(3));

            controller.registerController.register("eee", "111111", RegisterController.ORGANIZER);
            controller.registerController.register("ppp", "111111", RegisterController.ORGANIZER);

            eventController.createEvent("Expired1", "eee", new Timestamp(date.getTime()), "location*", 20,50.0);
            eventController.createEvent("notExpired1", "ppp", new Timestamp(date2.getTime()), "location-", 40, 100.0);
            assertEquals(2, eventController.getAllEvents().size());
            assertEquals("Expired1", eventController.getAllEvents().get(0).getName());
            assertEquals("location*", eventController.getAllEvents().get(0).getLocation());
            assertEquals("eee", eventController.getAllEvents().get(0).getOrganizerUsername());
            assertEquals(20, eventController.getAllEvents().get(0).getCapacity());
            assertEquals(50.0,eventController.getAllEvents().get(0).getPrice());
            assertEquals("notExpired1", eventController.getAllEvents().get(1).getName());
            assertEquals("location-", eventController.getAllEvents().get(1).getLocation());
            assertEquals("ppp", eventController.getAllEvents().get(1).getOrganizerUsername());
            assertEquals(40, eventController.getAllEvents().get(1).getCapacity());
            assertEquals(100.0,eventController.getAllEvents().get(1).getPrice());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }

    }


    @Test
    void isEventFull() {
        try {
            statement = connection.createStatement();
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp date = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(7));
            Timestamp date2 = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(3));

            controller.registerController.register("abc", "111111", RegisterController.USER);
            controller.userController.addBalance("abc", 200.0);
            controller.registerController.register("def", "222222", RegisterController.USER);
            controller.userController.addBalance("def", 200.0);
            controller.registerController.register("klm", "333333", RegisterController.USER);
            controller.userController.addBalance("klm", 200.0);

            controller.registerController.register("eee", "111111", RegisterController.ORGANIZER);
            controller.registerController.register("bbb", "111111", RegisterController.ORGANIZER);


            int eventId = eventController.createEvent("Event", "eee", new Timestamp(date.getTime()), "location*", 3,75.0);

            int id1 = ticketController.createTicket("abc", eventId);

            int id2 = ticketController.createTicket("def", eventId);

            int id3 = ticketController.createTicket("klm", eventId);

            controller.transactionController.createTransaction("abc", "eee", id1);
            controller.transactionController.createTransaction("def", "eee", id2);
            controller.transactionController.createTransaction("klm", "eee", id3);

            assertTrue(eventController.isEventFull(eventId));

            int eventId2 = eventController.createEvent("Event2", "bbb", new Timestamp(date2.getTime()), "location-", 4, 100.50);
            int id4 = ticketController.createTicket("abc", eventId2);
            int id5 = ticketController.createTicket("def", eventId2);

            controller.transactionController.createTransaction("abc", "bbb", id4);
            controller.transactionController.createTransaction("def", "bbb", id5);

            assertFalse(eventController.isEventFull(eventId2));

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }

    }

    @Test
    void isEventExpired() throws SQLException {
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp date = new Timestamp(currentTimeMillis - TimeUnit.DAYS.toMillis(7));
        Timestamp date2 = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(3));
        controller.registerController.register("eee", "111111", RegisterController.ORGANIZER);
        controller.registerController.register("ppp", "111111", RegisterController.ORGANIZER);
        int id1 = eventController.createEvent("Expired1", "eee", new Timestamp(date.getTime()), "location*", 20, 15.40);
        int id2 = eventController.createEvent("notExpired1", "ppp", new Timestamp(date2.getTime()), "location-", 40, 60.80);
        assertTrue(eventController.isEventExpired(id1));
        assertFalse(eventController.isEventExpired(id2));

    }
}