import controller.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private Controller controller;

    private UserController userController;

    @BeforeEach
    void setUp() {
        this.connection = new DatabaseConnection().getConnection();
        this.controller = new Controller(connection);
        this.userController = new UserController(connection);
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
            controller.registerController.register("xxx", "111111", RegisterController.USER);
            userController.changeUsername("xxx", "new_username");
            assertFalse(controller.loginController.login("xxx", "111111"));
            assertTrue(controller.loginController.login("new_username", "111111"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void changePassword() {
        try {
            statement = connection.createStatement();
            controller.registerController.register("xxx", "111111", RegisterController.USER);
            userController.changePassword("xxx", "new_password");
            assertFalse(controller.loginController.login("xxx", "111111"));
            assertTrue(controller.loginController.login("xxx", "new_password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addBalance() {
        try {
            statement = connection.createStatement();
            controller.registerController.register("xxx", "111111", RegisterController.USER);
            userController.addBalance("xxx", 200.0);
            resultSet = statement.executeQuery("SELECT balance FROM user");


            if (resultSet.next()) {

                assertEquals(resultSet.getDouble("balance"), 200.0);
            } else {
                fail("No rows found in the result set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL Exception occurred");
        }
    }

    @Test
    void getBalance() {
        try {
            statement = connection.createStatement();
            controller.registerController.register("xxx", "111111", RegisterController.USER);
            userController.addBalance("xxx", 200.0);
            assertEquals(userController.getBalance("xxx"), 200.0);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL Exception occurred");
        }
    }

    @Test
    void getUserByUserName() {
        try {
            statement = connection.createStatement();
            controller.registerController.register("xxx", "111111", RegisterController.USER);
            userController.addBalance("xxx", 200.0);
            assertEquals(userController.getUserByUsername("xxx").getUsername(), "xxx");
            assertEquals(userController.getUserByUsername("xxx").getPassword(), "111111");
            assertEquals(userController.getUserByUsername("xxx").getBalance(), 200.0);

        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL Exception occurred");
        }
    }
}