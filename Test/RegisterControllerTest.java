import controller.RegisterController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterControllerTest {
    private Connection connection;
    private RegisterController registerController;

    @BeforeEach
    public void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
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
}