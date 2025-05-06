import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionFilterTest {

    private List<Transaction> transactions;
    private TransactionFilter filter;

    @BeforeEach
    public void setup() {
        Transaction.resetCounter();
        transactions = List.of(
                new Transaction(100.0, "Salary", LocalDate.of(2024, 1, 10)),
                new Transaction(-20.0, "Food", LocalDate.of(2024, 2, 5)),
                new Transaction(-50.0, "Transport", LocalDate.of(2024, 3, 15)),
                new Transaction(200.0, "Bonus", LocalDate.of(2024, 2, 1)),
                new Transaction(-5.0, "Food", LocalDate.of(2024, 2, 20))
        );
        filter = new TransactionFilter(transactions);
    }

    @Test
    public void testFilterByCategory() {
        List<Transaction> result = filter.filterByCategory("Food");
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(t -> t.getCategory().equalsIgnoreCase("Food")));
    }

    @Test
    public void testFilterByDateRange() {
        LocalDate from = LocalDate.of(2024, 2, 1);
        LocalDate to = LocalDate.of(2024, 2, 28);

        List<Transaction> result = filter.filterByDateRange(from, to);
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(t ->
                !t.getDate().isBefore(from) && !t.getDate().isAfter(to)));
    }

    @Test
    public void testFilterByAmountRange() {
        double min = -25.0;
        double max = 0.0;

        List<Transaction> result = filter.filterByAmountRange(min, max);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(t ->
                t.getAmount() >= min && t.getAmount() <= max));
    }

    @Test
    public void testEmptyTransactionList() {
        TransactionFilter emptyFilter = new TransactionFilter(List.of());
        List<Transaction> result = emptyFilter.filterByCategory("Food");
        assertTrue(result.isEmpty(), "Výsledok by mal byť prázdny pri prázdnom zozname.");
    }

    @Test
    public void testCategoryNotFound() {
        List<Transaction> result = filter.filterByCategory("NonExistentCategory");
        assertTrue(result.isEmpty(), "Výsledok by mal byť prázdny, ak kategória neexistuje.");
    }

    @Test
    public void testDateRangeReversed() {
        LocalDate from = LocalDate.of(2024, 3, 1);
        LocalDate to = LocalDate.of(2024, 2, 1); // zámerne opačne

        List<Transaction> result = filter.filterByDateRange(from, to);

        // Očakávame, že metóda rozsah automaticky opraví a vráti relevantné záznamy
        assertEquals(3, result.size(), "Aj opačný rozsah dátumov by mal vrátiť správne transakcie.");
        for (Transaction t : result) {
            assertTrue(
                    !t.getDate().isBefore(to) && !t.getDate().isAfter(from),
                    "Transakcia má byť v opravenom rozsahu dátumov."
            );
        }
    }

    @Test
    public void testAmountRangeReversed() {
        double min = 100.0;
        double max = -100.0; // zámerne opačne

        List<Transaction> result = filter.filterByAmountRange(min, max);

        // Očakávame, že metóda rozsah automaticky opraví a vráti relevantné záznamy
        assertEquals(4, result.size(), "Aj opačný rozsah súm by mal vrátiť správne transakcie.");
        for (Transaction t : result) {
            assertTrue(
                    t.getAmount() >= max && t.getAmount() <= min,
                    "Transakcia má byť v opravenom rozsahu súm."
            );
        }
    }

    @Test
    public void testAmountRangeExactMatch() {
        double exactAmount = -20.0;
        List<Transaction> result = filter.filterByAmountRange(-20.0, -20.0);
        assertEquals(1, result.size());
        assertEquals(exactAmount, result.get(0).getAmount());
    }

}
