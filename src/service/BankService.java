package service;

import model.Result;
import model.User;
import model.Account;

import java.util.ArrayList;
import java.util.List;


public class BankService {
       private List<User> users = new ArrayList<>();
       private List<Account> accounts = new ArrayList<>();

       private int userId = 1;
       private int accountId = 1;

       public User createUser(String name){
           User user = new User(userId++, name);
           users.add(user);
           return user;
       }

       public Account createAccount(int userId){
           Account account = new Account(accountId++, userId);
           accounts.add(account);
           return account;
       }

       public Account findAccount(int id){
           for (Account a: accounts){
               if (a.getId() == id) return a;
           }
           return null;
       }

       public Result<Void> deposit(int accountId,double amount){
           Account account = findAccount(accountId);
           if (account == null) return Result.fail("Account not found");
           Result<Account> depositResult = account.deposit(amount);
           if (!depositResult.isSuccess()) return Result.fail(depositResult.getError());
           return Result.ok(null);
       }

       public Result<Void>  withdraw(int accountId, double amount){
           Account account = findAccount(accountId);
           if (account == null) return Result.fail("Account not found");
           Result<Account> withdrawResult = account.withdraw(amount);
           if (!withdrawResult.isSuccess()) return Result.fail(withdrawResult.getError());
           return Result.ok(null);
       }

       public Result<Void> transfer(int fromId, int toID, double amount){
           Account from = findAccount(fromId);
           Account to = findAccount(toID);
           if (from == null || to == null) return Result.fail("Account not found");
           Result<Account> withdrawResult = from.withdraw(amount);
           if (!withdrawResult.isSuccess()) return Result.fail(withdrawResult.getError());
           return Result.ok(null);
       }

       public List<Account> getAccounts(){
           return accounts;
       }

       public List<User> getUsers() {
           return users;
       }
}
