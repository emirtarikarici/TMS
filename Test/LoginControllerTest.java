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

    @BeforeEach
    void setUp() throws Exception {
        this.connection = new DatabaseConnection().getConnection();
        Controller controller = new Controller(connection);
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

    @Test
    void loginWithEmptyCredentialsReturnsFalse() throws Exception {
        String emptyUsername = "";
        String emptyPassword = "";
        Assertions.assertFalse(loginController.login(emptyUsername, emptyPassword));
    }

    @Test
    void loginWithNullCredentialsReturnsFalse() throws Exception {
        String nullUsername = null;
        String nullPassword = null;
        Assertions.assertFalse(loginController.login(nullUsername, nullPassword));
    }

    @Test
    void logoutAfterLoginChangesLoginStatus() throws Exception {
        String validUsername = "validUser";
        String validPassword = "validPass";
        loginController.login(validUsername, validPassword);
        loginController.logout();
        Assertions.assertFalse(loginController.isLoggedIn());
    }

    @Test
    void getAccountTypeForExistingUserReturnsUserType() throws Exception {
        String existingUsername = "existingUser";
        Assertions.assertEquals(RegisterController.USER, loginController.getAccountType(existingUsername));
    }

    @Test
    void getAccountTypeForExistingOrganizerReturnsOrganizerType() throws Exception {
        String existingUsername = "existingOrganizer";
        Assertions.assertEquals(RegisterController.ORGANIZER, loginController.getAccountType(existingUsername));
    }

    @Test
    void getAccountTypeForNonExistingUserTypeReturnsNegativeOne() throws Exception {
        String nonExistingUsername = "nonExistingUser";
        Assertions.assertEquals(-1, loginController.getAccountType(nonExistingUsername));
    }
}