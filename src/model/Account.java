package model;

public class Account {
    private int id;
    private int userId;
    private double balance;

    public Account(int id, int userId){
        this.id = id;
        this.userId = userId;
        this.balance = 0;
    }

    public int getId(){
        return id;
    }

    public double getBalance(){
        return balance;
    }

    public void deposit(double amount) {
        validateAmount(amount);
        balance += amount;
    }

    public void withdraw(double amount){
        validateAmount(amount);
        if (balance < amount){
            throw new IllegalStateException("Not enough money");
        }
        balance -= amount;
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
