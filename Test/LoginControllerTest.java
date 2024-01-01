import controller.Controller;
import controller.DatabaseConnection;
import controller.LoginController;
import controller.RegisterController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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


    @Test
    void loginWithValidCredentialsReturnsTrue() throws Exception {
        String validUsername = "validUser";
        String validPassword = "validPass";
        Assertions.assertTrue(loginController.login(validUsername, validPassword));
    }

    @Test
    void loginWithInvalidCredentialsReturnsFalse() throws Exception {
        String invalidUsername = "invalidUser";
        String invalidPassword = "invalidPass";
        Assertions.assertFalse(loginController.login(invalidUsername, invalidPassword));
    }

    @Test
    void loginWithInvalidUsernameReturnsFalse() throws Exception {
        String invalidUsername = "invalidUser";
        String validPassword = "validPass";
        Assertions.assertFalse(loginController.login(invalidUsername, validPassword));
    }

    @Test
    void loginWithInvalidPasswordReturnsFalse() throws Exception {
        String validUsername = "validUser";
        String invalidPassword = "invalidPass";
        Assertions.assertFalse(loginController.login(validUsername, invalidPassword));
    }
}