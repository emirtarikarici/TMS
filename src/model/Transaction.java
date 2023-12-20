package model;

public class Transaction {
    private int id;
    private User user;
    private Organizer organizer;
    private double amount;
    private int status;

    public static final int STATUS_COMPLETED = 0;
    public static final int STATUS_CANCELLED = 1;

    public Transaction(int id, User user, Organizer organizer, double amount, int status) {
        this.id = id;
        this.user = user;
        this.organizer = organizer;
        this.amount = amount;
        this.status = status;
    }

    public int getTransactionNumber() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public Organizer getOrganizer() {
        return this.organizer;
    }

    public double getAmount() {
        return this.amount;
    }

    public int getStatus() {
        return this.status;
    }

    public boolean equals(Transaction transaction) {
        return this.id == transaction.getTransactionNumber() && this.user.equals(transaction.getUser())
                && this.organizer.equals(transaction.getOrganizer()) && this.amount == transaction.getAmount()
                && this.status == transaction.getStatus();
    }

    @Override
    public int hashCode() {
        return this.id + this.user.hashCode() + this.organizer.hashCode() + (int) this.amount + (int) this.status;
    }
}
