import controller.TransactionController;
import model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;


public class TransactionControllerTest {
    private TestableTransactionController transactionController;
    private Connection connection;

    @BeforeEach
    public void setUp() {
        transactionController = new TestableTransactionController(connection);
    }

    @AfterEach
    void tearDown() throws Exception {
        Main.dropAllTables(connection);
        connection.close();
    }

    @Test
    public void validateTransactionReturnsTrueWhenValid() {
        transactionController.setValidateTransactionResult(true);
        assertTrue(transactionController.validateTransaction("username", 1));
    }

    @Test
    public void validateTransactionReturnsFalseWhenInvalid() {
        transactionController.setValidateTransactionResult(false);
        assertFalse(transactionController.validateTransaction("username", 1));
    }

    private static class TestableTransactionController extends TransactionController {
        private Boolean validateTransactionResult;
        private Integer createTransactionResult;
        private Boolean cancelTransactionResult;
        private ArrayList<Transaction> getTransactionByUserResult;

        public TestableTransactionController(Connection connection) {
            super(connection);
        }

        @Override
        public boolean validateTransaction(String username, int ticketId) {
            return validateTransactionResult;
        }

        @Override
        public int createTransaction(String userUsername, String organizerUsername, int ticketId) {
            return createTransactionResult;
        }

        @Override
        public boolean cancelTransaction(int id) {
            return cancelTransactionResult;
        }

        @Override
        public ArrayList<Transaction> getTransactionByUser(String userUsername) {
            return getTransactionByUserResult;
        }

        public void setValidateTransactionResult(Boolean validateTransactionResult) {
            this.validateTransactionResult = validateTransactionResult;
        }

        public void setCreateTransactionResult(Integer createTransactionResult) {
            this.createTransactionResult = createTransactionResult;
        }

        public void setCancelTransactionResult(Boolean cancelTransactionResult) {
            this.cancelTransactionResult = cancelTransactionResult;
        }

        public void setGetTransactionByUserResult(ArrayList<Transaction> getTransactionByUserResult) {
            this.getTransactionByUserResult = getTransactionByUserResult;
        }
    }
}