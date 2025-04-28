import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.YearMonth;
import java.util.Map;
import java.util.TreeMap;
import java.util.Scanner;


public class Manager {
    private static final String NO_TRANSACTIONS = "Žiadne transakcie.";
    private String fileName;

    private List<Transaction> transactions = new ArrayList<>();

    public Manager() {
        this("transactions.txt");
    }

    public Manager(String fileName) {
        this.fileName = fileName;
        loadTransactionsFromFile();
    }

    public void addTransaction(double amount, String category, LocalDate date) {
        Transaction transaction = new Transaction(amount, category, date);
        transactions.add(transaction);
        saveTransactionToFile(transaction);
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

    private void saveTransactionToFile(Transaction transaction) {
        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(transaction.getId() + ";" + transaction.getAmount() + ";" + transaction.getCategory() + ";" + transaction.getDate());

        } catch (IOException e) {
            System.out.println("Chyba pri ukladaní transakcie: " + e.getMessage());
        }
    }

    private void loadTransactionsFromFile() {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            int maxId = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                if (parts.length != 4) continue;

                int id = Integer.parseInt(parts[0]);
                double amount = Double.parseDouble(parts[1]);
                String category = parts[2];
                LocalDate date = LocalDate.parse(parts[3]);

                Transaction t = new Transaction(id, amount, category, date);
                transactions.add(t);

                if (id > maxId) {
                    maxId = id;
                }
            }
            Transaction.setCounter(maxId + 1);
        } catch (Exception e) {
            System.out.println("Chyba pri načítaní transakcií: " + e.getMessage());
        }
    }
}
