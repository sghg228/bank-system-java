package service;

import model.Result;
import model.User;
import model.Account;

import storage.FileStorage;

import logging.AuditLogger;

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

       private void findLatestUserId(){
           userIdBase = users.getLast().getId() + 1;
       }

       private void findLatestAccountId(){
           accountIdBase = accounts.getLast().getId() + 1;
       }

       public User createUser(String name){
           User user = new User(userIdBase++, name);
           users.add(user);
           save();
           AuditLogger.log("User created: id=" + user.getId() + ", name=" + user.getName());
           return user;
       }

       public void start(){
           loadAccounts();
           loadUsers();
           if (!users.isEmpty()) findLatestUserId();
           if (!accounts.isEmpty()) findLatestAccountId();
       }

       public Result<Account> createAccount(int userId){
           User user = findUser(userId);
           if (user == null) return Result.fail("User with id: " + userId + " doesn't exist");
           Account account = new Account(accountIdBase++, userId);
           accounts.add(account);
           save();
           AuditLogger.log("Account created: id=" + account.getId() + ", userId=" + user.getId());
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
           if (!depositResult.isSuccess()){
               AuditLogger.log("Failed deposit: userId=" + account.getUserId() + ", reason=" + depositResult.getError());
               return Result.fail(depositResult.getError());
           }
           AuditLogger.log("Deposit: userID=" + account.getUserId() + "amount=" + amount);
           save();
           return Result.ok(null);
       }

       public Result<Void>  withdraw(int accountId, double amount){
           Account account = findAccount(accountId);
           if (account == null) return Result.fail("Account not found");
           Result<Account> withdrawResult = account.withdraw(amount);
           if (!withdrawResult.isSuccess()){
               AuditLogger.log("Failed withdraw: userId=" + account.getUserId() + ", reason=" + withdrawResult.getError());
               return Result.fail(withdrawResult.getError());
           }
           AuditLogger.log("Withdraw: userID=" + account.getUserId() + "amount=" + amount);
           save();
           return Result.ok(null);
       }

       public Result<Void> transfer(int fromId, int toID, double amount){
           Account from = findAccount(fromId);
           Account to = findAccount(toID);
           if (from == null || to == null){
               AuditLogger.log("Error: account not found");
               return Result.fail("Account not found");
           }
           Result<Account> withdrawResult = from.withdraw(amount);
           if (!withdrawResult.isSuccess()){
               AuditLogger.log("Error transfer(withdraw): from=" + fromId + ", to=" + toID + ", reason=" + withdrawResult.getError());
               return Result.fail(withdrawResult.getError());
           }
           Result<Account> depositeResult = from.deposit(amount);
           if (!depositeResult.isSuccess()){
               AuditLogger.log("Error transfer(deposit): from=" + fromId + ", to=" + toID + ", reason=" + withdrawResult.getError());
               Result<Account> depositeBackResult = from.deposit(amount);
               if (!depositeBackResult.isSuccess()){
                   AuditLogger.log("Fatal error: accountID=" + from.getId() + " lose money amount=" + amount);
               }
               return Result.fail(depositeResult.getError());
           }
           AuditLogger.log("Transfer: fromId=" + fromId + ", toId=" + toID + ", amount=" + amount);
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
           AuditLogger.log("SAVE");


       }

       public void close(){
           storage.shutdown();
       }

}
