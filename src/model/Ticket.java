package model;

public class Ticket {
    private int id;
    private User user;
    private Event event;

    public Ticket(int id, User user, Event event) {
        this.id = id;
        this.user = user;
        this.event = event;
    }

    public int getTicketNumber() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public Event getEvent() {
        return this.event;
    }

    public boolean equals(Ticket ticket) {
        return this.id == ticket.getTicketNumber() && this.user.equals(ticket.getUser())
                && this.event.equals(ticket.getEvent());
    }

    @Override
    public int hashCode() {
        return this.id + this.user.hashCode() + this.event.hashCode();
    }
}
