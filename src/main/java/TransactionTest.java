import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @BeforeEach
    void resetCounter() {
        Transaction.resetCounter();
    }

    @Test
    void testTransactionCreation() {
        Transaction t = new Transaction(100.0, "Food", LocalDate.of(2024, 4, 27));
        assertEquals(1, t.getId());
        assertEquals(100.0, t.getAmount());
        assertEquals("Food", t.getCategory());
        assertEquals(LocalDate.of(2024, 4, 27), t.getDate());
    }

    @Test
    void testTransactionIdAutoIncrementMultiple() {
        Transaction t1 = new Transaction(50.0, "Transport", LocalDate.now());
        Transaction t2 = new Transaction(75.0, "Groceries", LocalDate.now());
        Transaction t3 = new Transaction(100.0, "Entertainment", LocalDate.now());

        assertEquals(1, t1.getId());
        assertEquals(2, t2.getId());
        assertEquals(3, t3.getId());
    }

    @Test
    void testTransactionToStringFormat() {
        Transaction t = new Transaction(250.0, "Salary", LocalDate.of(2024, 5, 1));
        String output = t.toString();
        assertTrue(output.contains("Dátum"));
        assertTrue(output.contains("Kategória"));
        assertTrue(output.contains("Suma"));
    }

    @Test
    void testNegativeAmountIsAllowedInTransaction() {
        Transaction t = new Transaction(-150.0, "Refund", LocalDate.now());
        assertEquals(-150.0, t.getAmount());
    }

    @Test
    void testTransactionDifferentDates() {
        Transaction t1 = new Transaction(100, "Test", LocalDate.of(2023, 1, 1));
        Transaction t2 = new Transaction(200, "Test", LocalDate.of(2024, 1, 1));

        assertNotEquals(t1.getDate(), t2.getDate());
    }
}
