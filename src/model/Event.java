package model;

import java.sql.Timestamp;

public class Event {
    private int id;
    private String name;
    private String organizerUsername;
    private Timestamp date;
    private String location;
    private int capacity;
    private int sold;

    public Event(int id, String name, String organizerUsername, Timestamp date, String location, int capacity,
            int sold) {
        this.id = id;
        this.name = name;
        this.organizerUsername = organizerUsername;
        this.date = date;
        this.location = location;
        this.capacity = capacity;
        this.sold = sold;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getOrganizerUsername() {
        return this.organizerUsername;
    }

    public Timestamp getDate() {
        return this.date;
    }

    public String getLocation() {
        return this.location;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getSold() {
        return this.sold;
    }

    public boolean equals(Event event) {
        return this.id == event.id && this.name.equals(event.getName())
                && this.organizerUsername.equals(event.getOrganizerUsername())
                && this.date.equals(event.getDate()) && this.location.equals(event.getLocation())
                && this.capacity == event.getCapacity() && this.sold == event.getSold();
    }

    @Override
    public int hashCode() {
        return this.id + this.name.hashCode() + this.organizerUsername.hashCode() + this.date.hashCode()
                + this.location.hashCode()
                + this.capacity + this.sold;
    }

}
