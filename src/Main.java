import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import controller.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection connection = new DatabaseConnection().getConnection();

        dropAllTables(connection);

        connection = new DatabaseConnection().getConnection();

        Controller controller = new Controller(connection);

        boolean r1 = controller.registerController.register("turgay", "password1", RegisterController.USER);
        boolean r2 = controller.registerController.register("azra", "password2", RegisterController.ORGANIZER);

        boolean l1 = controller.loginController.login("turgay", "password1");
        boolean l2 = controller.loginController.login("azra", "password2");

        System.out.println("Logins are successful!");

        boolean u1 = controller.userController.addBalance("turgay", 1000);
        boolean u3 = controller.userController.addBalance("turgay", 2000);
        boolean u2 = controller.userController.changePassword("turgay", "password3");

        boolean o3 = controller.organizerController.changePassword("azra", "password4");

        System.out.println("Account changes are successful!");

        int e1 = controller.eventController.createEvent("event1", "azra",
                new Timestamp(System.currentTimeMillis() + 1000000000), "ozu", 100,10.0);
        int e2 = controller.eventController.createEvent("event2", "azra",
                new Timestamp(System.currentTimeMillis() + 2000000000), "ozu", 100, 40.5);

        System.out.println("Event creation is successful!");

        int t1 = controller.ticketController.createTicket("turgay", e1);
        int t2 = controller.transactionController.createTransaction("turgay", "azra", t1);

        System.out.println("Ticket and transaction creation is successful!");

        connection.close();
        System.exit(0);
    }

    public static void dropAllTables(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS `transaction`");
            statement.executeUpdate("DROP TABLE IF EXISTS `ticket`");
            statement.executeUpdate("DROP TABLE IF EXISTS `event`");
            statement.executeUpdate("DROP TABLE IF EXISTS `user`");
            statement.executeUpdate("DROP TABLE IF EXISTS `organizer`");
            System.out.println("Tables Dropped!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't Drop Tables!");
        }
    }
}
