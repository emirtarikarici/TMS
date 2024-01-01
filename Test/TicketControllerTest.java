import controller.Controller;
import controller.DatabaseConnection;
import controller.RegisterController;
import controller.UserController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TicketControllerTest {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private Controller controller;

    private UserController userController;

    @BeforeEach
    void setUp() {
        this.connection = new DatabaseConnection().getConnection();
        this.controller = new Controller(connection);
        this.userController = new UserController(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        Main.dropAllTables(connection);
        connection.close();
    }


    @Test
    void createTicket() {
        try {
            statement = connection.createStatement();

            controller.registerController.register("abc", "111111", RegisterController.USER);
            controller.userController.addBalance("abc", 200.0);
            controller.registerController.register("eee", "111111", RegisterController.ORGANIZER);

            int eventId = controller.eventController.createEvent("Event", "eee", new Timestamp(System.currentTimeMillis()),"location*", 3, 20.30);

            int id1 = controller.ticketController.createTicket("abc", eventId);

            assertEquals(controller.ticketController.getTicketById(id1).getEventId(),eventId);
            assertEquals(controller.ticketController.getTicketById(id1).getUserUsername(),"abc");


        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }

    }

    @Test
    void getTicketsByUsername() {
        try {
            statement = connection.createStatement();
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp date = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(7));
            Timestamp date2 = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(3));

            controller.registerController.register("abc", "111111", RegisterController.USER);
            controller.userController.addBalance("abc", 200.0);
            controller.registerController.register("eee", "111111", RegisterController.ORGANIZER);
            controller.registerController.register("fff", "222222", RegisterController.ORGANIZER);
            int eventId1 = controller.eventController.createEvent("Event1", "eee", new Timestamp(date.getTime()), "location*", 3, 20.30);
            int eventId2 = controller.eventController.createEvent("Event2", "fff", new Timestamp(date2.getTime()), "location-", 3, 15.30);
            int id1 = controller.ticketController.createTicket("abc", eventId1);
            int id2 = controller.ticketController.createTicket("abc", eventId2);
            assertEquals(controller.ticketController.getTicketsByUsername("abc").get(0).getEventId(),eventId1);
            assertEquals(controller.ticketController.getTicketsByUsername("abc").get(1).getEventId(),eventId2);

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void getTicketsByEventId() {
        try {
            statement = connection.createStatement();
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp date = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(7));
            Timestamp date2 = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(3));

            controller.registerController.register("abc", "111111", RegisterController.USER);
            controller.userController.addBalance("abc", 200.0);
            controller.registerController.register("eee", "111111", RegisterController.ORGANIZER);
            controller.registerController.register("fff", "222222", RegisterController.ORGANIZER);
            int eventId1 = controller.eventController.createEvent("Event1", "eee", new Timestamp(date.getTime()), "location*", 3, 20.30);
            int eventId2 = controller.eventController.createEvent("Event2", "fff", new Timestamp(date2.getTime()), "location-", 3, 15.30);
            int id1 = controller.ticketController.createTicket("abc", eventId1);
            int id2 = controller.ticketController.createTicket("abc", eventId2);
            assertEquals(controller.ticketController.getTicketsByEventId(eventId1).get(0).getUserUsername(),"abc");
            assertEquals(controller.ticketController.getTicketsByEventId(eventId2).get(0).getUserUsername(),"abc");


        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }
    }
    @Test
    void getAllTickets() {
        try {
            statement = connection.createStatement();
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp date = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(7));
            Timestamp date2 = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(3));

            controller.registerController.register("abc", "111111", RegisterController.USER);
            controller.userController.addBalance("abc", 200.0);
            controller.registerController.register("def", "111111", RegisterController.USER);
            controller.userController.addBalance("def", 100.0);

            controller.registerController.register("eee", "111111", RegisterController.ORGANIZER);
            controller.registerController.register("fff", "222222", RegisterController.ORGANIZER);

            int eventId1 = controller.eventController.createEvent("Event1", "eee", new Timestamp(date.getTime()), "location*", 3, 20.30);
            int eventId2 = controller.eventController.createEvent("Event2", "fff", new Timestamp(date2.getTime()), "location-", 3, 15.30);
            int id1 = controller.ticketController.createTicket("abc", eventId1);
            int id2 = controller.ticketController.createTicket("def", eventId2);
            assertEquals(controller.ticketController.getAllTickets().get(0).getEventId(),eventId1);
            assertEquals(controller.ticketController.getAllTickets().get(1).getEventId(),eventId2);
            assertEquals(controller.ticketController.getAllTickets().get(0).getUserUsername(),"abc");
            assertEquals(controller.ticketController.getAllTickets().get(1).getUserUsername(),"def");

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    void getTicketById() {
        try {
            statement = connection.createStatement();
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp date = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(7));
            Timestamp date2 = new Timestamp(currentTimeMillis + TimeUnit.DAYS.toMillis(3));

            controller.registerController.register("abc", "111111", RegisterController.USER);
            controller.userController.addBalance("abc", 200.0);
            controller.registerController.register("def", "111111", RegisterController.USER);
            controller.userController.addBalance("def", 100.0);

            controller.registerController.register("eee", "111111", RegisterController.ORGANIZER);
            controller.registerController.register("fff", "222222", RegisterController.ORGANIZER);

            int eventId1 = controller.eventController.createEvent("Event1", "eee", new Timestamp(date.getTime()), "location*", 3, 20.30);
            int eventId2 = controller.eventController.createEvent("Event2", "fff", new Timestamp(date2.getTime()), "location-", 3, 15.30);
            int id1 = controller.ticketController.createTicket("abc", eventId1);
            int id2 = controller.ticketController.createTicket("def", eventId2);
            assertEquals(controller.ticketController.getTicketById(id1).getEventId(),eventId1);
            assertEquals(controller.ticketController.getTicketById(id2).getEventId(),eventId2);
            assertEquals(controller.ticketController.getTicketById(id1).getUserUsername(),"abc");
            assertEquals(controller.ticketController.getTicketById(id2).getUserUsername(),"def");

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }
    }
}