package model;

public class Ticket {
    private int id;
    private String userUsername;
    private int eventId;
    private int status;

    public static final int ACTIVE = 0;
    public static final int CANCELLED = 1;

    public Ticket(int id, String userUsername, int eventId, int status) {
        this.id = id;
        this.userUsername = userUsername;
        this.eventId = eventId;
        this.status = status;
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

    public int getStatus() {
        return this.status;
    }

    public boolean equals(Ticket ticket) {
        return this.id == ticket.getTicketNumber() && this.userUsername.equals(ticket.getUserUsername())
                && this.eventId == ticket.getEventId() && this.status == ticket.getStatus();
    }

    @Override
    public int hashCode() {
        return this.id + this.userUsername.hashCode() + this.eventId + this.status;
    }
}
