import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionFilter {
    private List<Transaction> transactions = null;

    public TransactionFilter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> filterByCategory(String category) {
        return transactions.stream()
                .filter(t -> t.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByDateRange(LocalDate from, LocalDate to) {
        LocalDate actualFrom = from.isBefore(to) ? from : to;
        LocalDate actualTo = from.isAfter(to) ? from : to;

        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(actualFrom) && !t.getDate().isAfter(actualTo))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByAmountRange(double min, double max) {
        double actualMin = Math.min(min, max);
        double actualMax = Math.max(min, max);
        return transactions.stream()
                .filter(t -> t.getAmount() >= actualMin && t.getAmount() <= actualMax)
                .collect(Collectors.toList());
    }
}
