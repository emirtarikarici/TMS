package model;

public class Transaction {
    private int id;
    private String userUsername;
    private String organizerUsername;
    private double amount;
    private int status;
    private int ticketId;

    public static final int STATUS_COMPLETED = 0;
    public static final int STATUS_CANCELLED = 1;

    public Transaction(int id, String userUsername, String organizerUsername, double amount, int status, int ticketId) {
        this.id = id;
        this.userUsername = userUsername;
        this.organizerUsername = organizerUsername;
        this.amount = amount;
        this.status = status;
        this.ticketId = ticketId;
    }

    public int getTransactionNumber() {
        return this.id;
    }

    public String getUser() {
        return this.userUsername;
    }

    public String getOrganizer() {
        return this.organizerUsername;
    }

    public double getAmount() {
        return this.amount;
    }

    public int getStatus() {
        return this.status;
    }

    public int getTicketId() {
        return this.ticketId;
    }

    public boolean equals(Transaction transaction) {
        return this.id == transaction.getTransactionNumber() && this.userUsername.equals(transaction.getUser())
                && this.organizerUsername.equals(transaction.getOrganizer()) && this.amount == transaction.getAmount()
                && this.status == transaction.getStatus() && this.ticketId == transaction.getTicketId();
    }

    @Override
    public int hashCode() {
        return this.id + this.userUsername.hashCode() + this.organizerUsername.hashCode() + (int) this.amount
                + this.status + this.ticketId;
    }
}
