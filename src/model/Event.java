package model;

import java.time.LocalDateTime;

public class Event {
    private String name;
    private Organizer organizer;
    private LocalDateTime date;
    private double price;
    private int capacity;
    private int sold;

    public Event(String name, Organizer organizer, LocalDateTime date, double price, int capacity, int sold) {
        this.name = name;
        this.organizer = organizer;
        this.date = date;
        this.price = price;
        this.capacity = capacity;
        this.sold = sold;
    }

    public String getName() {
        return this.name;
    }

    public Organizer getOrganizer() {
        return this.organizer;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public double getPrice() {
        return this.price;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getSold() {
        return this.sold;
    }

    public boolean equals(Event event) {
        return this.name.equals(event.getName()) && this.organizer.equals(event.getOrganizer())
                && this.date.equals(event.getDate()) && this.price == event.getPrice()
                && this.capacity == event.getCapacity() && this.sold == event.getSold();
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() + this.organizer.hashCode() + this.date.hashCode() + (int) this.price
                + (int) this.capacity + (int) this.sold;
    }

}
