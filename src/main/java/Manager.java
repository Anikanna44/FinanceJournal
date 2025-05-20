import java.time.LocalDate;
import java.util.*;
import java.time.YearMonth;


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

    public boolean deleteTransactionById(int id) {
        Transaction transactionToRemove = findTransactionById(id);
        if (transactionToRemove != null) {
            transactions.remove(transactionToRemove);
            storage.deleteTransactionFromFile(id);
            return true;
        }
        return false; // Transaction not found
    }


    public double calculateBalance() {
        double balance = 0;
        for (Transaction t : transactions) {
            balance += t.getAmount();
        }
        return balance;
    }

    public void printSummary() {
        printMonthlySummary();
        printLastMonthSummaryByCategories();
    }

    private void printLastMonthSummaryByCategories() {
        YearMonth now = YearMonth.now();
        System.out.println("\nDetailné zhrnutie pre " + now + ":");

        Map<String, Double> incomeByCategory = new HashMap<>();
        Map<String, Double> expenseByCategory = new HashMap<>();

        for (Transaction t : transactions) {
            if (YearMonth.from(t.getDate()).equals(now)) {
                String category = t.getCategory();
                double amount = t.getAmount();

                if (amount >= 0) {
                    incomeByCategory.put(category, incomeByCategory.getOrDefault(category, 0.0) + amount);
                } else {
                    expenseByCategory.put(category, expenseByCategory.getOrDefault(category, 0.0) + Math.abs(amount));
                }
            }
        }

        if (!incomeByCategory.isEmpty()) {
            System.out.println("\n Príjmy podľa kategórií:");
            incomeByCategory.forEach((cat, amt) -> System.out.printf(" - %s: %.2f\n", cat, amt));
        } else {
            System.out.println("\n Žiadne príjmy za tento mesiac.");
        }

        if (!expenseByCategory.isEmpty()) {
            System.out.println("\n Výdavky podľa kategórií:");
            expenseByCategory.forEach((cat, amt) -> System.out.printf(" - %s: %.2f\n", cat, amt));
        } else {
            System.out.println("\n Žiadne výdavky za tento mesiac.");
        }
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

        System.out.println("\nZhrnutie zostatkov za posledných 12 mesiacov:");
        YearMonth now = YearMonth.now();
        for (int i = 0; i < 12; i++) {
            YearMonth month = now.minusMonths(i);
            double sum = monthlyBalances.getOrDefault(month, 0.0);
            System.out.printf(" %s: %.2f\n", month, sum);
        }
    }


    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Transaction> getCopyTransactions() {
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
