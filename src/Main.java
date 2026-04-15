import service.BankService;
import model.Account;
import model.User;
import model.Result;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BankService bank = new BankService();
        Scanner scanner = new Scanner(System.in);
        bank.start();

        while (true) {
            System.out.println("\n=== BANK MENU ===");
            System.out.println("1. Create user");
            System.out.println("2. Create account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. Show accounts");
            System.out.println("0. Exit");

            System.out.print("Choose option: ");
            int choice = scanner.nextInt();

            try {
                switch (choice) {

                    case 1:
                        System.out.print("Enter user name: ");
                        String name = scanner.next();
                        User user = bank.createUser(name);
                        System.out.println("Created user with ID: " + user.getId());
                        break;

                    case 2:
                        System.out.print("Enter user ID: ");
                        int userId = scanner.nextInt();
                        Account acc = bank.createAccount(userId);
                        System.out.println("Created account with ID: " + acc.getId());
                        break;

                    case 3:
                        System.out.print("Enter account ID: ");
                        int accId = scanner.nextInt();
                        System.out.print("Enter amount: ");
                        double amount = scanner.nextDouble();

                        Result<Void> depositResult = bank.deposit(accId,amount);

                        if (!depositResult.isSuccess()){
                            System.out.println("Error: " + depositResult.getError());
                        }
                        else {
                            System.out.println("Deposit successful");
                        }
                        break;

                    case 4:
                        System.out.print("Enter account ID: ");
                        accId = scanner.nextInt();
                        System.out.print("Enter amount: ");
                        amount = scanner.nextDouble();

                        Result<Void> withdrawResult = bank.withdraw(accId, amount);

                        if (!withdrawResult.isSuccess()){
                            System.out.println("Errror: " + withdrawResult.getError());
                        }
                        else {
                            System.out.println("Withdraw successful");
                        }
                        break;

                    case 5:
                        System.out.print("From account ID: ");
                        int fromId = scanner.nextInt();
                        System.out.print("To account ID: ");
                        int toId = scanner.nextInt();
                        System.out.print("Amount: ");
                        amount = scanner.nextDouble();

                        Result<Void> transferResult = bank.transfer(fromId, toId, amount);

                        if (!transferResult.isSuccess()) {
                            System.out.println("Error: " + transferResult.getError());
                        } else {
                            System.out.println("Transfer successful");
                        }

                    case 6:
                        for (Account a : bank.getAccounts()) {
                            System.out.println("Account ID: " + a.getId() +
                                    " | User ID: " + a.getId() +
                                    " | Balance: " + a.getBalance());
                        }
                        break;

                    case 0:
                        System.out.println("Bye!");
                        return;

                    default:
                        System.out.println("Invalid option");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}