import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.YearMonth;
import java.util.Map;
import java.util.TreeMap;



public class Manager {
    private static final String NO_TRANSACTIONS = "Žiadne transakcie.";
    private TransactionStorage storage;

    private List<Transaction> transactions;

    public Manager() {
        this("transactions.txt");
    }

    public Manager(String fileName) {
        this.storage = new TransactionStorage(fileName);
        this.transactions = storage.loadTransactionsFromFile();
    }

    public void addTransaction(double amount, String category, LocalDate date) {
        Transaction transaction = new Transaction(amount, category, date);
        transactions.add(transaction);
        storage.saveTransactionToFile(transaction);
    }

    public double calculateBalance() {
        double balance = 0;
        for (Transaction t : transactions) {
            balance += t.getAmount();
        }
        return balance;
    }

    public void printMonthlySummary() {
        if (transactions.isEmpty()) {
            System.out.println(NO_TRANSACTIONS);
            return;
        }

        Map<YearMonth, Double> monthlyBalances = new TreeMap<>();

        for (Transaction t : transactions) {
            YearMonth ym = YearMonth.from(t.getDate());
            monthlyBalances.put(ym, monthlyBalances.getOrDefault(ym, 0.0) + t.getAmount());
        }

        System.out.println("\nZhrnutie za posledných 12 mesiacov:");
        YearMonth now = YearMonth.now();
        for (int i = 0; i < 12; i++) {
            YearMonth month = now.minusMonths(i);
            double sum = monthlyBalances.getOrDefault(month, 0.0);
            System.out.println(month + ": " + sum);
        }
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public void listTransactions() {
        if (transactions.isEmpty()) {
            System.out.println(NO_TRANSACTIONS);
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
