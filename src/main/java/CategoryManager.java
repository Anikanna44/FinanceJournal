import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryManager {
    private List<String> availableCategories;

    public CategoryManager(List<Transaction> transactions) {
        updateCategoriesFromTransactions(transactions);
    }

    public void updateCategoriesFromTransactions(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            this.availableCategories = new ArrayList<>();
            return;
        }
        this.availableCategories = transactions.stream()
                .map(Transaction::getCategory)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void addCategoryIfNew(String categoryName) {
        if (!availableCategories.contains(categoryName))
            availableCategories.add(categoryName);
    }

    public List<String> getCategories() {
        return new ArrayList<>(availableCategories);
    }
}
