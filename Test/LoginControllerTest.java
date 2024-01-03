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
        controller.registerController.register(validUsername, validPassword, RegisterController.USER);
        Assertions.assertTrue(loginController.login(validUsername, validPassword));
    }

    @Test
    void loginWithInvalidCredentialsReturnsFalse() throws Exception {
        String validUsername = "validUser";
        String validPassword = "validPass";
        String invalidUsername = "invalidUser";
        String invalidPassword = "invalidPass";
        controller.registerController.register(validUsername, validPassword, RegisterController.USER);
        Assertions.assertFalse(loginController.login(invalidUsername, invalidPassword));
    }

    @Test
    void loginWithInvalidUsernameReturnsFalse() throws Exception {
        String validUsername = "validUser";
        String validPassword = "validPass";
        String invalidUsername = "invalidUser";
        controller.registerController.register(validUsername, validPassword, RegisterController.USER);
        Assertions.assertFalse(loginController.login(invalidUsername, validPassword));
    }

    @Test
    void loginWithInvalidPasswordReturnsFalse() throws Exception {
        String validUsername = "validUser";
        String validPassword = "validPass";
        String invalidPassword = "invalidPass";
        controller.registerController.register(validUsername, validPassword, RegisterController.USER);
        Assertions.assertFalse(loginController.login(validUsername, invalidPassword));
    }

    @Test
    void loginWithEmptyCredentialsReturnsFalse() throws Exception {
        String emptyUsername = "";
        String emptyPassword = "";
        controller.registerController.register(emptyUsername, emptyPassword, RegisterController.USER);
        Assertions.assertFalse(loginController.login(emptyUsername, emptyPassword));
    }

    @Test
    void loginWithNullCredentialsReturnsFalse() throws Exception {
        String nullUsername = null;
        String nullPassword = null;
        controller.registerController.register(nullUsername, nullPassword, RegisterController.USER);
        Assertions.assertFalse(loginController.login(nullUsername, nullPassword));
    }

    @Test
    void logoutAfterLoginChangesLoginStatus() throws Exception {
        String validUsername = "validUser";
        String validPassword = "validPass";
        controller.registerController.register(validUsername, validPassword, RegisterController.USER);
        loginController.login(validUsername, validPassword);
        loginController.logout();
        Assertions.assertFalse(loginController.isLoggedIn());
    }

    @Test
    void getAccountTypeForExistingUserReturnsUserType() throws Exception {
        String existingUsername = "existingUser";
        String password = "password";
        controller.registerController.register(existingUsername,password, RegisterController.USER);
        Assertions.assertEquals(RegisterController.USER, loginController.getAccountType(existingUsername));
    }

    @Test
    void getAccountTypeForExistingOrganizerReturnsOrganizerType() throws Exception {
        String existingUsername = "existingOrganizer";
        String password = "password";
        controller.registerController.register(existingUsername,password, RegisterController.ORGANIZER);
        Assertions.assertEquals(RegisterController.ORGANIZER, loginController.getAccountType(existingUsername));
    }

    @Test
    void getAccountTypeForNonExistingUserTypeReturnsNegativeOne() throws Exception {
        String nonExistingUsername = "nonExistingUser";
        Assertions.assertEquals(-1, loginController.getAccountType(nonExistingUsername));
    }
}