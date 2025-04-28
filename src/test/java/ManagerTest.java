import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    private Manager manager;
    private static final String TEST_FILE = "test_transactions.txt";

    @BeforeEach
    void setUp() {
        manager = new Manager(TEST_FILE);
        Transaction.resetCounter();

    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testAddSingleTransaction() {
        manager.addTransaction(150.0, "Utilities", LocalDate.now());
        assertEquals(1, manager.getTransactions().size());
    }

    @Test
    void testAddMultipleTransactions() {
        manager.addTransaction(100.0, "Food", LocalDate.now());
        manager.addTransaction(200.0, "Rent", LocalDate.now());
        manager.addTransaction(50.0, "Transport", LocalDate.now());

        List<Transaction> transactions = manager.getTransactions();
        assertEquals(3, transactions.size());
    }

    @Test
    void testFindTransactionValidId() {
        manager.addTransaction(500.0, "Bonus", LocalDate.now());
        Transaction found = manager.findTransactionById(1);
        assertNotNull(found);
        assertEquals(500.0, found.getAmount());
    }

    @Test
    void testFindTransactionInvalidId() {
        assertNull(manager.findTransactionById(404));
    }

    @Test
    void testListTransactionsEmpty() {
        assertTrue(manager.getTransactions().isEmpty());
    }

    @Test
    void testListTransactionsNotEmpty() {
        manager.addTransaction(123.0, "Shopping", LocalDate.now());
        assertFalse(manager.getTransactions().isEmpty());
    }

    @Test
    void testSeparateListsCopies() {
        manager.addTransaction(300.0, "Gift", LocalDate.now());
        List<Transaction> copy1 = manager.getTransactions();
        List<Transaction> copy2 = manager.getTransactions();

        assertNotSame(copy1, copy2);
        copy1.clear(); // modify copy1
        assertFalse(manager.getTransactions().isEmpty()); // original still has data
    }

    @Test
    void testCalculateBalance_NoTransactions() {
        assertEquals(0.0, manager.calculateBalance());
    }

    @Test
    void testCalculateBalance_SingleTransaction() {
        manager.addTransaction(100.0, "Platba", LocalDate.now());
        assertEquals(100.0, manager.calculateBalance());
    }

    @Test
    void testCalculateBalance_MultipleTransactions() {
        manager.addTransaction(100.0, "Platba", LocalDate.now());
        manager.addTransaction(200.0, "Predaj", LocalDate.now());
        manager.addTransaction(50.0, "Nákup", LocalDate.now());
        assertEquals(350.0, manager.calculateBalance());
    }

    @Test
    void testCalculateBalance_WithNegativeTransactions() {
        manager.addTransaction(500.0, "Vklad", LocalDate.now());
        manager.addTransaction(-200.0, "Výber", LocalDate.now());
        manager.addTransaction(-100.0, "Výber", LocalDate.now());
        assertEquals(200.0, manager.calculateBalance());
    }
}
