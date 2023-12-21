package model;

public class Ticket {
    private int id;
    private String userUsername;
    private int eventId;
    private double price;

    public Ticket(int id, String userUsername, int eventId, double price) {
        this.id = id;
        this.userUsername = userUsername;
        this.eventId = eventId;
        this.price = price;
    }

    public int getTicketNumber() {
        return this.id;
    }

    public String getUserUsername() {
        return this.userUsername;
    }

    public int getEventId() {
        return this.eventId;
    }

    public double getPrice() {
        return this.price;
    }

    public boolean equals(Ticket ticket) {
        return this.id == ticket.getTicketNumber() && this.userUsername.equals(ticket.getUserUsername())
                && this.eventId == ticket.getEventId() && this.price == ticket.getPrice();
    }

    @Override
    public int hashCode() {
        return this.id + this.userUsername.hashCode() + this.eventId + (int) this.price;
    }
}
