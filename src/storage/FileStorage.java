package storage;

import model.Account;
import model.Result;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileStorage {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    final Object lockAccount = new Object();
    final Object lockUser = new Object();

    public void saveAccountAsync(List<Account> accounts){
        executor.submit(()->{
           synchronized (lockAccount){
               try(FileWriter writer =  new FileWriter("accounts.txt")){
                   for (Account a: accounts){
                       writer.write(a.getId() + "," + a.getUserId() + "," + a.getBalance() + "\n");
                   }
               }
               catch (IOException ex){
                   System.out.println(ex.getMessage() );
               }
           }
        });
    }

    public void saveUserAsync(List<User> users){
        executor.submit(()->{
           synchronized (lockUser){
               try(FileWriter writer =  new FileWriter("users.txt")){
                   for (User u: users){
                       writer.write(u.getId() + "," + u.getName() + "\n");
                   }
               }
               catch (IOException ex){
                   System.out.println(ex.getMessage() );
               }
           }
        });
    }

    public List<Account> loadAccount(){
        synchronized (lockAccount){
             List<Account> accounts = new ArrayList<>();
             try (BufferedReader reader = new BufferedReader( new FileReader("accounts.txt"))) {
                String line;
                while ((line = reader.readLine()) != null){
                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0]);
                    int userId = Integer.parseInt(parts[1]);
                    double balance = Double.parseDouble(parts[2]);

                    Account acc = new Account(id,userId);
                    Result<Account> depositeResult = acc.deposit(balance);

                    accounts.add(acc);
                }
             }
             catch (FileNotFoundException ex){
                 System.out.println("File accounts.txt not found");
             }
             catch (IOException ex){
                 System.out.println("Error loading file");
             }
             return accounts;
        }
    }

    public List<User> loadUser(){
        synchronized (lockUser){
            List<User> users = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader( new FileReader("users.txt"))) {
                String line;
                while ((line = reader.readLine()) != null){
                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0]);
                    String userName = parts[1];
                    users.add(new User(id,userName));
                }
            }
            catch (FileNotFoundException ex){
                System.out.println("File users.txt not found");
            }
            catch (IOException ex){
                System.out.println("Error loading file");
            }
            return users;
        }
    }

    public void shutdown(){
        executor.shutdown();
    }
}
