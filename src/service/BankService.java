package service;

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

       public void deposit(int accountId,double amount){
           Account account = findAccount(accountId);
           if (account == null) throw  new IllegalArgumentException("Account not found");
           account.deposit(amount);
       }

       public void  withdraw(int accountId, double amount){
           Account account = findAccount(accountId);
           if (account == null) throw new IllegalArgumentException("Account not found");
           account.withdraw(amount);
       }

       public void transfer(int fromId, int toID, double amount){
           Account from = findAccount(fromId);
           Account to = findAccount(toID);
           if (from == null || to == null) throw new IllegalArgumentException("One of account not found");
           from.withdraw(amount);
           from.deposit(amount);
       }

       public List<Account> getAccounts(){
           return accounts;
       }

       public List<User> getUsers() {
           return users;
       }
}
