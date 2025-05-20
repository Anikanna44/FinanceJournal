import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class CategoryManagerTest {

    private CategoryManager categoryManager;
    private List<Transaction> transactions;

    // Helper to create transactions
    private Transaction createTransaction(String category) {
        return new Transaction(0, 0, category, LocalDate.now());
    }

    @BeforeEach
    void setUp() {
        transactions = new ArrayList<>();
    }

    @Test
    void testAddCategoryIfNew_AddNew() {
        categoryManager = new CategoryManager(Collections.emptyList());
        categoryManager.addCategoryIfNew("Groceries");
        assertTrue(categoryManager.getCategories().contains("Groceries"), "New category should be added.");
        assertEquals(1, categoryManager.getCategories().size());
    }

    @Test
    void testAddCategoryIfNew_AddExisting() {
        transactions.add(createTransaction("Food"));
        categoryManager = new CategoryManager(transactions);

        categoryManager.addCategoryIfNew("Food");
        assertEquals(1, categoryManager.getCategories().size(), "Adding an existing category should not duplicate.");
        assertTrue(categoryManager.getCategories().contains("Food"));
    }

    @Test
    void testUpdateCategoriesFromTransactions() {
        transactions.add(createTransaction("Entertainment"));
        categoryManager = new CategoryManager(transactions);
        assertEquals(Collections.singletonList("Entertainment"), categoryManager.getCategories());

        List<Transaction> newTransactions = Arrays.asList(
                createTransaction("Health"),
                createTransaction("Travel")
        );
        categoryManager.updateCategoriesFromTransactions(newTransactions);
        List<String> expected = Arrays.asList("Health", "Travel");
        assertEquals(expected, categoryManager.getCategories(), "Categories should be updated.");
    }

    @Test
    void testCategoriesAreSortedCaseInsensitively() {
        transactions.add(createTransaction("z"));
        transactions.add(createTransaction("B"));
        transactions.add(createTransaction("A"));
        categoryManager = new CategoryManager(transactions);

        List<String> expected = Arrays.asList("A", "B", "z");
        assertEquals(expected, categoryManager.getCategories(), "Categories should be sorted case-insensitively.");
    }
}
