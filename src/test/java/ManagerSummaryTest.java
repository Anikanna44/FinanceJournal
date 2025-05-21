import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerSummaryTest {

    private static final String TEST_FILE = "test_transactions.txt";
    private Manager manager;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        Transaction.resetCounter();
        manager = new Manager(TEST_FILE) {
            @Override
            public List<Transaction> getTransactions() {
                return super.getTransactions();
            }
        };
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testSummaryWithNoTransactions() {
        manager = new Manager(TEST_FILE) {
            @Override
            public List<Transaction> getTransactions() {
                return List.of(); // simulate no data
            }
        };
        manager.printSummary();

        String output = outContent.toString();
        assertTrue(output.contains("Žiadne transakcie"));
    }

    @Test
    public void testMonthlySummaryShowsRecentMonth() {
        LocalDate today = LocalDate.now();
        manager.addTransaction(100.0, "Príjem", today);
        manager.addTransaction(-50.0, "Výdavok", today.minusMonths(1));

        manager.printSummary();

        String output = outContent.toString();
        YearMonth thisMonth = YearMonth.now();
        YearMonth lastMonth = thisMonth.minusMonths(1);

        assertTrue(output.contains(thisMonth.toString()));
        assertTrue(output.contains(lastMonth.toString()));
    }

    @Test
    public void testCategorySummaryForCurrentMonth() {
        LocalDate today = LocalDate.now();
        manager.addTransaction(200.0, "Výplata", today);
        manager.addTransaction(-80.0, "Potraviny", today);
        manager.addTransaction(-20.0, "Potraviny", today);

        manager.printSummary();
        String output = outContent.toString();

        assertTrue(output.contains("Výplata: 200,00") || output.contains("Výplata: 200.00"));
        assertTrue(output.contains("Potraviny: 100,00") || output.contains("Potraviny: 100.00"));

    }

    @Test
    public void testCategorySummarySkipsOldTransactions() {
        LocalDate lastYear = LocalDate.now().minusYears(1);
        manager.addTransaction(500.0, "Starý Príjem", lastYear);
        manager.addTransaction(-250.0, "Starý Výdavok", lastYear);

        manager.printSummary();
        String output = outContent.toString();

        assertFalse(output.contains("Starý Príjem"));
        assertFalse(output.contains("Starý Výdavok"));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut); // Restore standard output
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
