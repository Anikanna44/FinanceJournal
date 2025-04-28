import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

class SaveAndLoadFileTest {

    private Manager manager;
    private static final String TEST_FILE = "test_transactions.txt";

    @BeforeEach
    void setUp() {
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
    void testSaveAndLoadTransaction() throws IOException {
        LocalDate today = LocalDate.now();

        manager.addTransaction(150.0, "Vklad", today);

        List<String> lines = Files.readAllLines(new File(TEST_FILE).toPath());
        assertEquals(1, lines.size());

        String line = lines.get(0);
        assertTrue(line.contains("Vklad"));
        assertTrue(line.contains(String.valueOf(today)));

        Manager newManager = new Manager(TEST_FILE);
        List<Transaction> loadedTransactions = newManager.getTransactions();

        assertEquals(1, loadedTransactions.size());

        Transaction t = loadedTransactions.get(0);
        assertEquals(150.0, t.getAmount());
        assertEquals("Vklad", t.getCategory());
        assertEquals(today, t.getDate());
    }

    @Test
    void testMultipleTransactionsPersistence() throws IOException {
        LocalDate today = LocalDate.now();

        manager.addTransaction(100.0, "Platba", today);
        manager.addTransaction(-50.0, "Nákup", today);
        manager.addTransaction(200.0, "Príjem", today);

        List<String> lines = Files.readAllLines(new File(TEST_FILE).toPath());
        assertEquals(3, lines.size());

        Manager newManager = new Manager(TEST_FILE);
        List<Transaction> loadedTransactions = newManager.getTransactions();
        assertEquals(3, loadedTransactions.size());

        double balance = newManager.calculateBalance();
        assertEquals(250.0, balance);
    }
}
