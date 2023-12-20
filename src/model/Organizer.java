package model;

import java.util.ArrayList;

public class Organizer {
    private String username;
    private String password;
    private double balance;
    private ArrayList<Event> events;

    public Organizer(String username, String password, double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.events = new ArrayList<Event>();
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return this.balance;
    }

    public void addBalance(double balance) {
        this.balance += balance;
    }

    public void subtractBalance(double balance) {
        this.balance -= balance;
    }

    public ArrayList<Event> getEvents() {
        return this.events;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public void removeTicket(Event event) {
        this.events.remove(event);
    }

    public boolean equals(Organizer organizer) {
        return this.username.equals(organizer.getUsername()) && this.password.equals(organizer.getPassword())
                && this.balance == organizer.getBalance() && this.events.equals(organizer.getEvents())
                && this.hashCode() == organizer.hashCode();
    }

    @Override
    public int hashCode() {
        return this.username.hashCode() + this.password.hashCode() + (int) this.balance + this.events.hashCode();
    }
}
