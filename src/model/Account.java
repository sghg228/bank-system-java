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

    public int getUserId() {
        return userId;
    }

    public Result<Account> deposit(double amount) {

        if (amount <= 0) {
            return Result.fail("Amount must be positive");
        }

        this.balance += amount;

        return Result.ok(this);
    }

    public Result<Account> withdraw(double amount) {

        if (amount <= 0) {
            return Result.fail("Amount must be positive");
        }

        if (balance < amount) {
            return Result.fail("Not enough money");
        }

        balance -= amount;

        return Result.ok(this);
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
