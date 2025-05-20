import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Manager manager = new Manager();

    public static void main(String[] args) {
        System.out.println("Vitaj v tvojom finančnom denníčku");

        showSummary();
        while (true) {
            System.out.println("\n1. Pridaj transakciu\n2. Zobraz transakcie\n3. Zobraz zostatok\n4. Filtruj transakcie\n5. Vymaž transakciu\n6. Koniec");
            System.out.print("Vyber možnosť: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    addTransaction();
                    break;
                case "2":
                    manager.listTransactions();
                    break;
                case "3":
                    showSummary();
                    break;
                case "4":
                    filterTransactions();
                    break;
                case "5":
                    deleteTransaction();
                    break;
                case "6":
                    System.out.println("Ukončujem program.");
                    return;
                default:
                    System.out.println("Neplatná možnosť.");
            }
        }
    }

    private static void showSummary() {
        manager.printSummary();
        System.out.println("\nZostatok účtu: " + manager.calculateBalance());

    }

    private static void addTransaction() {
        try {
            double amount = readAmount();
            String category = readCategory();
            LocalDate date = readDate();

            manager.addTransaction(amount, category, date);
            System.out.println("Transakcia pridaná.");

        } catch (Exception e) {
            System.out.println("Chybný vstup: " + e.getMessage());
        }
    }

    private static void deleteTransaction() {
        System.out.print("Zadaj ID transakcie na vymazanie: ");
        String idInput = scanner.nextLine();
        try {
            int id = Integer.parseInt(idInput);
            if (manager.deleteTransactionById(id)) {
                System.out.println("Transakcia s ID " + id + " bola vymazaná.");
            } else {
                System.out.println("Transakcia s ID " + id + " nebola nájdená.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Neplatné ID.");
        }
    }


    private static String readCategory() {
        System.out.print("Zadaj kategóriu: ");
        String category = scanner.nextLine();
        if (category.isBlank()) {
            throw new IllegalArgumentException("Kategória nesmie byť prázdna.");
        }
        return category.trim();
    }


    public static double readAmount() {
        System.out.print("Zadaj sumu: ");
        String amountInput = scanner.nextLine();

        return parseAmount(amountInput);
    }

    public static LocalDate readDate() {
        System.out.print("Zadaj dátum (RRRR-MM-DD): ");
        String dateInput = scanner.nextLine();

        return parseDate(dateInput);
    }

    public static double parseAmount(String input) {
        try {
            double amount = Double.parseDouble(input);
            if (amount == 0) {
                throw new IllegalArgumentException("Suma nesmie byť nula.");
            }
            return amount;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Suma musí byť platné číslo.");
        }
    }

    public static LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Dátum musí byť vo formáte RRRR-MM-DD.");
        }
    }

    private static void filterTransactions() {
        TransactionFilter filter = new TransactionFilter(manager.getCopyTransactions());

        System.out.println("Filtrovať podľa:\n1. Kategórie\n2. Dátumu\n3. Suma\n4. Ukončiť filtrovanie");
        System.out.print("Zadaj možnosť: ");
        String choice = scanner.nextLine();

        List<Transaction> results;

        switch (choice) {
            case "1":
                results = filterByCategory(filter);
                break;
            case "2":
                results = filterByDateRange(filter);
                break;
            case "3":
                results = filterByAmount(filter);
                break;
            case "4":
                return;
            default:
                System.out.println("Neplatná možnosť.");
                return;
        }

        if (results.isEmpty()) {
            System.out.println("Žiadne transakcie nezodpovedajú filtru.");
        } else {
            System.out.println("Vyfiltrované transakcie:");
            for (Transaction t : results) {
                System.out.println(t);
            }
        }
    }

    private static List<Transaction> filterByAmount(TransactionFilter filter) {
        List<Transaction> results;
        try {
            System.out.print("Zadaj minimálnu sumu: ");
            double min = parseAmount(scanner.nextLine());
            System.out.print("Zadaj maximálnu sumu: ");
            double max = parseAmount(scanner.nextLine());
            results = filter.filterByAmountRange(min, max);
        } catch (Exception e) {
            System.out.println("Neplatná suma: " + e.getMessage());
            return null;
        }
        return results;
    }

    private static List<Transaction> filterByDateRange(TransactionFilter filter) {
        List<Transaction> results;
        try {
            System.out.print("Zadaj dátum OD (RRRR-MM-DD): ");
            LocalDate from = parseDate(scanner.nextLine());
            System.out.print("Zadaj dátum DO (RRRR-MM-DD): ");
            LocalDate to = parseDate(scanner.nextLine());
            results = filter.filterByDateRange(from, to);
        } catch (Exception e) {
            System.out.println("Neplatný dátum: " + e.getMessage());
            return null;
        }
        return results;
    }

    private static List<Transaction> filterByCategory(TransactionFilter filter) {
        List<Transaction> results;
        System.out.print("Zadaj kategóriu: ");
        String category = scanner.nextLine();
        results = filter.filterByCategory(category);
        return results;
    }


}