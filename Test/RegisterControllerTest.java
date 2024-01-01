import controller.Controller;
import controller.DatabaseConnection;
import controller.RegisterController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;


import static org.junit.jupiter.api.Assertions.*;

public class RegisterControllerTest {
    private Connection connection;
    private RegisterController registerController;

    @BeforeEach
    public void setUp() throws Exception {
        this.connection = new DatabaseConnection().getConnection();
        Controller controller = new Controller(connection);
        registerController = new RegisterController(connection);
    }

    @AfterEach
    void tearDown() throws Exception {
        Main.dropAllTables(connection);
        connection.close();
    }

    @Test
    public void shouldReturnFalseWhenUsernameExists() throws Exception {
        registerController.register("existingUser", "password", RegisterController.USER);
        assertFalse(registerController.register("existingUser", "password", RegisterController.USER));
    }

    @Test
    public void shouldReturnFalseWhenPasswordIsShort() throws Exception {
        assertFalse(registerController.register("username", "short", RegisterController.USER));
    }

    @Test
    public void shouldCreateUserWhenValidCredentialsAreProvided() throws Exception {
        assertTrue(registerController.register("newUser", "password", RegisterController.USER));
    }

    @Test
    public void shouldCreateOrganizerWhenValidCredentialsAreProvided() throws Exception {
        assertTrue(registerController.register("newOrganizer", "password", RegisterController.ORGANIZER));
    }
}