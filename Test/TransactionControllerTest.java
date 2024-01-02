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
        Timestamp timestamp = new Timestamp("2024-12-12 12:12:12.000".hashCode());
        controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 500, 300);
        controller.ticketController.createTicket("user", 1);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", 1);
        assertTrue(transactionId > 0);
    }

    @Test
    void createTransactionReturnsMinusOneWhenInsufficientBalance() {
        controller.registerController.register("organizer", "password",RegisterController.ORGANIZER);
        controller.registerController.register("user", "password",RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 100);
        Timestamp timestamp = new Timestamp("2024-12-12 12:12:12".hashCode());
        controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 500, 300);
        controller.ticketController.createTicket("user", 1);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", 1);
        assertEquals(-1, transactionId);
    }

    @Test
    void createTransactionReturnsMinusOneWhenEventIsFull() {
        controller.registerController.register("organizer", "password",RegisterController.ORGANIZER);
        controller.registerController.register("user", "password",RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = new Timestamp("2024-12-12 12:12:12".hashCode());
        controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 0, 300);
        controller.ticketController.createTicket("user", 1);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", 1);
        assertEquals(-1, transactionId);
    }

    @Test
    void cancelTransactionReturnsTrueWhenValid() {
        controller.registerController.register("organizer", "password",RegisterController.ORGANIZER);
        controller.registerController.register("user", "password",RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = new Timestamp("2024-12-12 12:12:12.000".hashCode());
        controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 500, 300);
        controller.ticketController.createTicket("user", 1);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", 1);
        assertTrue(controller.transactionController.cancelTransaction(transactionId));
    }

    @Test
    void getTransactionByUserReturnsTransactionsWhenValid() {
        controller.registerController.register("organizer", "password",RegisterController.ORGANIZER);
        controller.registerController.register("user", "password",RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = new Timestamp("2024-12-12 12:12:12.000".hashCode());
        controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 500, 300);
        controller.ticketController.createTicket("user", 1);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", 1);
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
        Timestamp timestamp = new Timestamp("2024-12-12 12:12:12.000".hashCode());
        controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 500, 300);
        controller.ticketController.createTicket("user", 1);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", 1);
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
        Timestamp timestamp = new Timestamp("2024-12-12 12:12:12.000".hashCode());
        controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 500, 300);
        controller.ticketController.createTicket("user", 1);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", 1);
        Transaction transaction = controller.transactionController.getTransactionById(1);
        assertNotNull(transaction);
    }

    @Test
    void getAllTransactionsReturnsTransactionsWhenValid() {
        controller.registerController.register("organizer", "password",RegisterController.ORGANIZER);
        controller.registerController.register("user", "password",RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = new Timestamp("2024-12-12 12:12:12.000".hashCode());
        controller.eventController.createEvent("event", "organizer",timestamp, "PSM", 500, 300);
        controller.ticketController.createTicket("user", 1);
        int transactionId = controller.transactionController.createTransaction("user", "organizer", 1);
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
        Timestamp timestamp = new Timestamp("2020-12-12 12:12:12".hashCode());
        controller.eventController.createEvent("event", "organizer", timestamp, "PSM", 500, 300);
        controller.ticketController.createTicket("user", 1);
        controller.transactionController.createTransaction("user", "organizer", 1);
        controller.transactionController.cancelTransaction(1);
        ArrayList<Transaction> transactions = controller.transactionController.getCompletedTransactions();
        assertTrue(transactions.isEmpty());
    }

    @Test
    void getCancelledTransactionsReturnsTransactionsWhenExist() {
        controller.registerController.register("organizer", "password", RegisterController.ORGANIZER);
        controller.registerController.register("user", "password", RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = new Timestamp("2024-12-12 12:12:12.000".hashCode());
        controller.eventController.createEvent("event", "organizer", timestamp, "PSM", 500, 300);
        controller.ticketController.createTicket("user", 1);
        controller.transactionController.createTransaction("user", "organizer", 1);
        controller.transactionController.cancelTransaction(1);
        ArrayList<Transaction> transactions = controller.transactionController.getCancelledTransactions();
        assertFalse(transactions.isEmpty());
    }

    void getTransactionsByEventReturnsTransactionsWhenExist() {
        controller.registerController.register("organizer", "password", RegisterController.ORGANIZER);
        controller.registerController.register("user", "password", RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = new Timestamp("2020-12-12 12:12:12".hashCode());
        controller.eventController.createEvent("event", "organizer", timestamp, "PSM", 500, 300);
        controller.ticketController.createTicket("user", 1);
        controller.transactionController.createTransaction("user", "organizer", 1);
        ArrayList<Transaction> transactions = controller.transactionController.getTransactionsByEvent(1);
        assertFalse(transactions.isEmpty());
    }

    @Test
    void getTransactionByTicketsReturnsTransactionsWhenExist() {
        controller.registerController.register("organizer", "password", RegisterController.ORGANIZER);
        controller.registerController.register("user", "password", RegisterController.USER);
        controller.loginController.login("user", "password");
        controller.loginController.login("organizer", "password");
        controller.userController.addBalance("user", 1000);
        Timestamp timestamp = new Timestamp("2020-12-12 12:12:12".hashCode());
        controller.eventController.createEvent("event", "organizer", timestamp, "PSM", 500, 300);
        controller.ticketController.createTicket("user", 1);
        controller.transactionController.createTransaction("user", "organizer", 1);
        Transaction transaction = controller.transactionController.getTransactionByTicket(1);
        assertNotNull(transaction);
    }
}