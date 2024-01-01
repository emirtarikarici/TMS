import controller.Controller;
import controller.DatabaseConnection;
import controller.LoginController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;



public class LoginControllerTest {
    private LoginController loginController;
    private Connection connection;
    private Controller controller;
    @BeforeEach
    void setUp() throws Exception {
        this.connection = new DatabaseConnection().getConnection();
        this.controller = new Controller(connection);
        loginController = new LoginController(connection);
    }

    @AfterEach
    void tearDown() throws Exception {
        Main.dropAllTables(connection);
        connection.close();
    }
}