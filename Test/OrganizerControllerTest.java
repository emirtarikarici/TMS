import controller.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class OrganizerControllerTest {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private Controller controller;

    private OrganizerController organizerController;

    @BeforeEach
    void setUp() {
        this.connection = new DatabaseConnection().getConnection();
        this.controller = new Controller(connection);
        this.organizerController = new OrganizerController(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        Main.dropAllTables(connection);
        connection.close();
    }


    @Test
    void changeUsername() {
        try {
            statement = connection.createStatement();
            controller.registerController.register("xxx", "111111", RegisterController.ORGANIZER);
            organizerController.changeUsername("xxx", "new_organizer");
            assertFalse(controller.loginController.login("xxx", "111111"));
            assertTrue(controller.loginController.login("new_organizer", "111111"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void changePassword() {
        try {
            statement = connection.createStatement();
            controller.registerController.register("xxx", "111111", RegisterController.ORGANIZER);
            organizerController.changePassword("xxx", "new_password");
            assertFalse(controller.loginController.login("xxx", "111111"));
            assertTrue(controller.loginController.login("xxx", "new_password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getBalance() {
        try {
            statement = connection.createStatement();
            controller.registerController.register("xxx", "111111", RegisterController.ORGANIZER);
            controller.registerController.register("yyy", "222222", RegisterController.USER);
            controller.userController.addBalance("yyy", 100.5);
            int id = controller.eventController.createEvent("event","xxx", new Timestamp(System.currentTimeMillis()),"location",3,20.30);
            int ticket = controller.ticketController.createTicket("yyy",id);
            controller.transactionController.createTransaction("yyy","xxx",ticket);
            assertEquals(20.30, controller.organizerController.getBalance("xxx"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    void getOrganizerByUsername(){
        try {
            statement = connection.createStatement();
            controller.registerController.register("xxx", "111111", RegisterController.ORGANIZER);
            controller.registerController.register("yyy", "222222", RegisterController.USER);
            controller.userController.addBalance("yyy", 200.0);

            int eventId = controller.eventController.createEvent("event","xxx", new Timestamp(System.currentTimeMillis()),"location",3,70.0);
            int ticketId = controller.ticketController.createTicket("yyy",eventId);
            controller.transactionController.createTransaction("yyy","xxx", ticketId);

            assertEquals("xxx", organizerController.getOrganizerByUsername("xxx").getUsername());
            assertEquals("111111", organizerController.getOrganizerByUsername("xxx").getPassword());
            assertEquals(70.0, organizerController.getOrganizerByUsername("xxx").getBalance());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL Exception occurred");
        }
    }
}