import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Manager {
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(double amount, String category, LocalDate date) {
        Transaction transaction = new Transaction(amount, category, date);
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public void listTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("Žiadne transakcie.");
        } else {
            for (Transaction t : transactions) {
                System.out.println(t);
            }
        }
    }

    public Transaction findTransactionById(int id) {
        for (Transaction t : transactions) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }
}
