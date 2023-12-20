package model;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private double balance;
    private ArrayList<Ticket> tickets;

    public User(String username, String password, double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.tickets = new ArrayList<Ticket>();
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

    public void setPassowrd(String password) {
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

    public ArrayList<Ticket> getTickets() {
        return this.tickets;
    }

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    public void removeTicket(Ticket ticket) {
        this.tickets.remove(ticket);
    }

    public boolean equals(User user) {
        return this.username.equals(user.getUsername()) && this.password.equals(user.getPassword())
                && this.balance == user.getBalance() && this.tickets.equals(user.getTickets())
                && this.hashCode() == user.hashCode();
    }

    @Override
    public int hashCode() {
        return this.username.hashCode() + this.password.hashCode() + (int) this.balance + this.tickets.hashCode();
    }
}
