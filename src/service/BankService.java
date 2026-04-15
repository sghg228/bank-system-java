package service;

import model.Result;
import model.User;
import model.Account;

import storage.FileStorage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BankService {
       private List<User> users = new ArrayList<>();
       private List<Account> accounts = new ArrayList<>();
       FileStorage storage = new FileStorage();

       private int userIdBase = 1;
       private int accountIdBase = 1;

       public void loadAccounts(){
           accounts = storage.loadAccount();
       }

       public void loadUsers(){
            users = storage.loadUser();
       }

       public User createUser(String name){
           User user = new User(userIdBase++, name);
           users.add(user);
           return user;
       }

       public void start(){
           loadAccounts();
           loadUsers();
       }

       public Result<Account> createAccount(int userId){
           User user = findUser(userId);
           if (user == null) return Result.fail("User with id: " + userId + " doesn't exist");
           Account account = new Account(accountIdBase++, userId);
           accounts.add(account);
           save();
           return Result.ok(account);
       }

       public Account findAccount(int id){
           for (Account a: accounts){
               if (a.getId() == id) return a;
           }
           return null;
       }

       public User findUser(int id){
           for (User u: users){
               if (u.getId() == id) return u;
           }
           return null;
       }

       public Result<Void> deposit(int accountId,double amount){
           Account account = findAccount(accountId);
           if (account == null) return Result.fail("Account not found");
           Result<Account> depositResult = account.deposit(amount);
           if (!depositResult.isSuccess()) return Result.fail(depositResult.getError());
           save();
           return Result.ok(null);
       }

       public Result<Void>  withdraw(int accountId, double amount){
           Account account = findAccount(accountId);
           if (account == null) return Result.fail("Account not found");
           Result<Account> withdrawResult = account.withdraw(amount);
           if (!withdrawResult.isSuccess()) return Result.fail(withdrawResult.getError());
           save();
           return Result.ok(null);
       }

       public Result<Void> transfer(int fromId, int toID, double amount){
           Account from = findAccount(fromId);
           Account to = findAccount(toID);
           if (from == null || to == null) return Result.fail("Account not found");
           Result<Account> withdrawResult = from.withdraw(amount);
           if (!withdrawResult.isSuccess()) return Result.fail(withdrawResult.getError());
           save();
           return Result.ok(null);
       }

       public List<Account> getAccounts(){
           return accounts;
       }

       public List<User> getUsers() {
           return users;
       }

       private void save(){

           storage.saveAccountAsync(accounts);
           storage.saveUserAsync(users);


       }

       public void close(){
           storage.shutdown();
       }

}
