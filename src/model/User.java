package model;

public class User {
    private String username;
    private String password;
    private double balance;

    public User(String username, String password, double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
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

    public boolean equals(User user) {
        return this.username.equals(user.getUsername()) && this.password.equals(user.getPassword())
                && this.balance == user.getBalance()
                && this.hashCode() == user.hashCode();
    }

    @Override
    public int hashCode() {
        return this.username.hashCode() + this.password.hashCode() + (int) this.balance;
    }
}
