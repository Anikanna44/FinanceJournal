import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionStorage {

    private String fileName;

    public TransactionStorage(String fileName){
        this.fileName = fileName;
    }
    public List<Transaction> loadTransactionsFromFile() {
        File file = new File(fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        List<Transaction> transactions = new ArrayList<>();
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
        return transactions;
    }

    public void saveTransactionToFile(Transaction transaction) {
        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(transaction.getId() + ";" + transaction.getAmount() + ";" + transaction.getCategory() + ";" + transaction.getDate());

        } catch (IOException e) {
            System.out.println("Chyba pri ukladaní transakcie: " + e.getMessage());
        }
    }

    public boolean deleteTransactionFromFile(int transactionId) {
        List<Transaction> allTransactions = loadTransactionsFromFile();
        List<Transaction> transactionsToKeep = allTransactions.stream()
                .filter(t -> t.getId() != transactionId)
                .toList();

        if (allTransactions.size() == transactionsToKeep.size()) {
            // nothing to delete
            return false;
        }

        // Empty the file
        try (FileWriter fw = new FileWriter(fileName, false)) {
            fw.write("");
        } catch (IOException e) {
            System.out.println("Chyba pri mazaní obsahu súboru transakcií: " + e.getMessage());
            return false;
        }
        for (Transaction t : transactionsToKeep) {
            saveTransactionToFile(t);
        }
        return true;
    }
}
