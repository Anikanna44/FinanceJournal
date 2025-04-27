import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Manager manager = new Manager();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1. Pridaj transakciu\n2. Zobraz transakcie\n3. Koniec");
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
                    System.out.println("Ukončujem program.");
                    return;
                default:
                    System.out.println("Neplatná možnosť.");
            }
        }
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
            if (amount <= 0) {
                throw new IllegalArgumentException("Suma musí byť kladné číslo.");
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

}