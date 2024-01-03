import controller.*;
import model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class TransactionControllerTest {
    private Connection connection;
    private Controller controller;

    @BeforeEach
    public void setUp() {
        this.connection = new DatabaseConnection().getConnection();
        this.controller = new Controller(connection);
    }

    @AfterEach
    void tearDown() throws Exception {
        Main.dropAllTables(connection);
        connection.close();
    }

    @Test
    void createTransactionReturnsIdWhenValid() {
        controller.registerController.register("organizer", "password",RegisterController.ORGANIZER);
        controller.registerController.register("user", "password",RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = Timestamp.valueOf("2024-12-12 12:12:12");
        int eventId = controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 500, 300);
        int ticketId = controller.ticketController.createTicket("user", eventId);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", ticketId);
        assertTrue(transactionId > 0);
    }

    @Test
    void createTransactionReturnsMinusOneWhenInsufficientBalance() {
        controller.registerController.register("organizer", "password",RegisterController.ORGANIZER);
        controller.registerController.register("user", "password",RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 100);
        Timestamp timestamp = Timestamp.valueOf("2024-12-12 12:12:12");
        int eventId = controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 500, 300);
        int ticketId = controller.ticketController.createTicket("user", eventId);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", ticketId);
        assertEquals(-1, transactionId);
    }

    @Test
    void createTransactionReturnsMinusOneWhenEventIsFull() {
        controller.registerController.register("organizer", "password",RegisterController.ORGANIZER);
        controller.registerController.register("user", "password",RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = Timestamp.valueOf("2024-12-12 12:12:12");
        int eventId = controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 0, 300);
        int ticketId = controller.ticketController.createTicket("user", eventId);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", ticketId);
        assertEquals(-1, transactionId);
    }

    @Test
    void cancelTransactionReturnsTrueWhenValid() {
        controller.registerController.register("organizer", "password",RegisterController.ORGANIZER);
        controller.registerController.register("user", "password",RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = Timestamp.valueOf("2024-12-12 12:12:12");
        int eventId = controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 500, 300);
        int ticketId = controller.ticketController.createTicket("user", eventId);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", ticketId);
        assertTrue(controller.transactionController.cancelTransaction(transactionId));
    }

    @Test
    void getTransactionByUserReturnsTransactionsWhenValid() {
        controller.registerController.register("organizer", "password",RegisterController.ORGANIZER);
        controller.registerController.register("user", "password",RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = Timestamp.valueOf("2024-12-12 12:12:12");
        int eventId = controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 500, 300);
        int ticketId = controller.ticketController.createTicket("user", eventId);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", ticketId);
        ArrayList<Transaction> transactions = controller.transactionController.getTransactionByUser("user");
        assertFalse(transactions.isEmpty());
    }

    @Test
    void getTransactionByOrganizerReturnsTransactionsWhenValid() {
        controller.registerController.register("organizer", "password",RegisterController.ORGANIZER);
        controller.registerController.register("user", "password",RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = Timestamp.valueOf("2024-12-12 12:12:12");
        int eventId = controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 500, 300);
        int ticketId= controller.ticketController.createTicket("user", eventId);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", ticketId);
        ArrayList<Transaction> transactions = controller.transactionController.getTransactionByOrganizer("organizer");
        assertFalse(transactions.isEmpty());
    }

    @Test
    void getTransactionByIdReturnsTransactionsWhenValid() {
        controller.registerController.register("organizer", "password",RegisterController.ORGANIZER);
        controller.registerController.register("user", "password",RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = Timestamp.valueOf("2024-12-12 12:12:12");
        int eventId = controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 500, 300);
        int ticketId = controller.ticketController.createTicket("user", eventId);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", ticketId);
        Transaction transaction = controller.transactionController.getTransactionById(transactionId);
        assertNotNull(transaction);
    }

    @Test
    void getAllTransactionsReturnsTransactionsWhenValid() {
        controller.registerController.register("organizer", "password",RegisterController.ORGANIZER);
        controller.registerController.register("user", "password",RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = Timestamp.valueOf("2024-12-12 12:12:12");
        int eventId = controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 500, 300);
        int ticketId = controller.ticketController.createTicket("user", eventId);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", ticketId);
        ArrayList<Transaction> transactions = controller.transactionController.getAllTransactions();
        assertFalse(transactions.isEmpty());
    }

    @Test
    void getCompletedTransactionsReturnsTransactionsWhenExist() {
        controller.registerController.register("organizer", "password", RegisterController.ORGANIZER);
        controller.registerController.register("user", "password", RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = Timestamp.valueOf("2020-12-12 12:12:12");
        int eventId = controller.eventController.createEvent("event", "organizer", timestamp, "PSM", 500, 300);
        int ticketId = controller.ticketController.createTicket("user", eventId);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", ticketId);
        controller.transactionController.cancelTransaction(transactionId);
        ArrayList<Transaction> transactions = controller.transactionController.getCompletedTransactions();
        assertFalse(transactions.isEmpty());
    }

    @Test
    void getCancelledTransactionsReturnsTransactionsWhenExist() {
        controller.registerController.register("organizer", "password", RegisterController.ORGANIZER);
        controller.registerController.register("user", "password", RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = Timestamp.valueOf("2024-12-12 12:12:12");
        int eventId = controller.eventController.createEvent("event", "organizer", timestamp, "PSM", 500, 300);
        int ticketId = controller.ticketController.createTicket("user", eventId );
        int transactionId = controller.transactionController.createTransaction("user", "organizer", ticketId);
        controller.transactionController.cancelTransaction(transactionId);
        ArrayList<Transaction> transactions = controller.transactionController.getCancelledTransactions();
        assertFalse(transactions.isEmpty());
    }

    void getTransactionsByEventReturnsTransactionsWhenExist() {
        controller.registerController.register("organizer", "password", RegisterController.ORGANIZER);
        controller.registerController.register("user", "password", RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = Timestamp.valueOf("2020-12-12 12:12:12");
        int eventId = controller.eventController.createEvent("event", "organizer", timestamp, "PSM", 500, 300);
        int ticketId = controller.ticketController.createTicket("user", eventId);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", ticketId);
        ArrayList<Transaction> transactions = controller.transactionController.getTransactionsByEvent(eventId);
        assertFalse(transactions.isEmpty());
    }

    @Test
    void getTransactionByTicketsReturnsTransactionsWhenExist() {
        controller.registerController.register("organizer", "password", RegisterController.ORGANIZER);
        controller.registerController.register("user", "password", RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = Timestamp.valueOf("2020-12-12 12:12:12");
        int eventId = controller.eventController.createEvent("event", "organizer", timestamp, "PSM", 500, 300);
        int ticketId = controller.ticketController.createTicket("user", eventId);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", ticketId);
        Transaction transaction = controller.transactionController.getTransactionByTicket(ticketId);
        assertNotNull(transaction);
    }
}