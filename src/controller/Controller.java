package controller;

import java.sql.Connection;

public class Controller {
    private Connection connection;
    public EventController eventController;
    public LoginController loginController;
    public OrganizerController organizerController;
    public RegisterController registerController;
    public TicketController ticketController;
    public TransactionController transactionController;
    public UserController userController;

    public Controller(Connection connection) {
        this.connection = connection;
        this.eventController = new EventController(connection);
        this.loginController = new LoginController(connection);
        this.organizerController = new OrganizerController(connection);
        this.registerController = new RegisterController(connection);
        this.ticketController = new TicketController(connection);
        this.transactionController = new TransactionController(connection);
        this.userController = new UserController(connection);
    }

    public Connection getConnection() {
        return connection;
    }
}
