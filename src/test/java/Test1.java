import java.io.File;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class Test1{
    private static final String TEST_FILE = "test_transactions.txt";
    private Manager manager;

    @BeforeEach
    void setUp() {
        Transaction.resetCounter();
        manager = new Manager(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
     void testAddTransaction() {
        manager.addTransaction(100.0, "Strava", LocalDate.of(2025, 4, 25));

        List<Transaction> transactions = manager.getTransactions();
        assertEquals(1, transactions.size());
        assertEquals(100.0, transactions.get(0).getAmount());
        assertEquals("Strava", transactions.get(0).getCategory());
        assertEquals(LocalDate.of(2025, 4, 25), transactions.get(0).getDate());
        assertEquals(1, transactions.get(0).getId());
    }

    @Test
    void testListTransactionsWhenEmpty() {
        List<Transaction> transactions = manager.getTransactions();
        assertTrue(transactions.isEmpty());
    }

    @Test
    void testMultipleTransactions() {
        manager.addTransaction(50.0, "Doprava", LocalDate.of(2025, 3, 20));
        manager.addTransaction(150.0, "Bývanie", LocalDate.of(2025, 4, 21));

        List<Transaction> transactions = manager.getTransactions();
        assertEquals(2, transactions.size());
        assertEquals("Doprava", transactions.get(0).getCategory());
        assertEquals("Bývanie", transactions.get(1).getCategory());
        assertEquals(50.0, transactions.get(0).getAmount());
        assertEquals(150.0, transactions.get(1).getAmount());
        assertEquals(1, transactions.get(0).getId());
        assertEquals(2, transactions.get(1).getId());
        assertEquals(LocalDate.of(2025, 3, 20), transactions.get(0).getDate());
        assertEquals(LocalDate.of(2025, 4, 21), transactions.get(1).getDate());
    }

    @Test
    void testPrintTransactions() {
        manager.addTransaction(50.0, "Doprava", LocalDate.of(2025, 3, 20));
        manager.addTransaction(150.0, "Bývanie", LocalDate.of(2025, 4, 21));

        List<Transaction> transactions = manager.getTransactions();
        assertEquals("[1] Dátum: 2025-03-20, Kategória: Doprava, Suma: 50.0", transactions.get(0).toString());
        assertEquals("[2] Dátum: 2025-04-21, Kategória: Bývanie, Suma: 150.0", transactions.get(1).toString());

    }
}
