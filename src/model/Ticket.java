package model;

public class Ticket {
    private int id;
    private String userUsername;
    private Event event;

    public Ticket(int id, String userUsername, Event event) {
        this.id = id;
        this.userUsername = userUsername;
        this.event = event;
    }

    public int getTicketNumber() {
        return this.id;
    }

    public String getUser() {
        return this.userUsername;
    }

    public Event getEvent() {
        return this.event;
    }

    public boolean equals(Ticket ticket) {
        return this.id == ticket.getTicketNumber() && this.userUsername.equals(ticket.getUser())
                && this.event.equals(ticket.getEvent());
    }

    @Override
    public int hashCode() {
        return this.id + this.userUsername.hashCode() + this.event.hashCode();
    }
}
