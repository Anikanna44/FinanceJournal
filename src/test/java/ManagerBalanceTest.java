import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ManagerBalanceTest {

    private static final String TEST_FILE = "test_transactions.txt";
    private Manager manager;

    @BeforeEach
    void setUp() {
        Transaction.resetCounter();
        manager = new Manager(TEST_FILE) {
            @Override
            public void addTransaction(double amount, String category, LocalDate date) {
                super.getTransactions().add(new Transaction(amount, category, date)); // skip file write
            }
        };
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testEmptyTransactionList() {
        assertEquals(0.0, manager.calculateBalance(), "Balance should be 0 when no transactions exist.");
    }

    @Test
    void testSinglePositiveTransaction() {
        manager.addTransaction(150.0, "Príjem", LocalDate.now());
        assertEquals(150.0, manager.calculateBalance());
    }

    @Test
    void testSingleNegativeTransaction() {
        manager.addTransaction(-75.0, "Výdavok", LocalDate.now());
        assertEquals(-75.0, manager.calculateBalance());
    }

    @Test
    void testMixedTransactions() {
        manager.addTransaction(1000.0, "Výplata", LocalDate.now());
        manager.addTransaction(-200.0, "Nájom", LocalDate.now());
        manager.addTransaction(-150.0, "Potraviny", LocalDate.now());
        manager.addTransaction(50.0, "Cashback", LocalDate.now());

        double expectedBalance = 1000.0 - 200.0 - 150.0 + 50.0;
        assertEquals(expectedBalance, manager.calculateBalance());
    }

    @Test
    void testPrecisionWithDecimals() {
        manager.addTransaction(99.99, "Bonus", LocalDate.now());
        manager.addTransaction(-49.49, "Káva", LocalDate.now());

        assertEquals(50.50, manager.calculateBalance(), 0.0001); // delta for precision
    }
}
