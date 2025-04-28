import java.time.LocalDate;

public class Transaction {
    private static int counter = 1;
    private final int id;
    private double amount;
    private String category;
    private LocalDate date;

    public Transaction(int id, double amount, String category, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }
    
    public Transaction(double amount, String category, LocalDate date) {
        this.id = counter++;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "[" + id + "] Dátum: " + date + ", Kategória: " + category + ", Suma: " + amount;
    }

    public static void resetCounter() {
        counter = 1;
    }

    public static void setCounter(int newCounter) {
        counter = newCounter;
    }
}
