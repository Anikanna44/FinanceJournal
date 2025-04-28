import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class InputValidationTest {

    @Test
    void testValidAmount() {
        assertEquals(99.99, Main.parseAmount("99.99"));
    }

    @Test
    void testAmountZeroThrows() {
        assertThrows(IllegalArgumentException.class, () -> Main.parseAmount("0.00"));
    }

    @Test
    void testAmountNegativeThrows() {
        assertEquals(-20.5, Main.parseAmount("-20.5"));
    }

    @Test
    void testAmountInvalidFormatThrows() {
        assertThrows(IllegalArgumentException.class, () -> Main.parseAmount("abc123"));
    }

    @Test
    void testValidDateParsing() {
        LocalDate expectedDate = LocalDate.of(2023, 12, 31);
        assertEquals(expectedDate, Main.parseDate("2023-12-31"));
    }

    @Test
    void testInvalidDateMonth() {
        assertThrows(IllegalArgumentException.class, () -> Main.parseDate("2023-13-01"));
    }

    @Test
    void testInvalidDateDay() {
        assertThrows(IllegalArgumentException.class, () -> Main.parseDate("2023-02-30"));
    }

    @Test
    void testInvalidDateFormat() {
        assertThrows(IllegalArgumentException.class, () -> Main.parseDate("31-12-2023"));
    }
}
