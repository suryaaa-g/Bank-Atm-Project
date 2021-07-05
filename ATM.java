import java.util.Scanner;


public class ATM {
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        
        Bank theBank = new Bank("Business Bank");

        
        User aUser = theBank.addUser("John", "Wick", "1234");

       
        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while(true){

            
            curUser = ATM.mainMenuPrompt(theBank, sc);

            
            ATM.printUserMenu(curUser, sc);
        }
    }

    
    public static User mainMenuPrompt(Bank theBank, Scanner sc){
        //initialize
        String userID;
        String pin;
        User authUser;

        //prompt the user ID and pin until it is correct
        System.out.printf("\n\nWelcome to the %s\n\n", theBank.getName());
        do {

            System.out.print("Enter user ID: ");
            userID = sc.nextLine();
            System.out.print("Enter pin: ");
            pin = sc.nextLine();

            //try to get the user obj corresponding to the ID and pin combo
            authUser = theBank.userLogin(userID, pin);
            if(authUser == null){
                System.out.println("Incorrect userID/pin combo, please try again.");
            }
        }
        while( authUser == null); 
        return authUser;
    }

    public static void printUserMenu(User theUser, Scanner sc){

        // print summary of users accounts
        theUser.printAccountSummary();

        // initialize
        int choice;

        //user menu
        do{
            System.out.printf("Welcome %s, what would you like to do?\n", theUser.getFirstName());
            System.out.println(" 1) Show account transaction history");
            System.out.println(" 2) Withdraw");
            System.out.println(" 3) Deposit");
            System.out.println(" 4) Transfer");
            System.out.println(" 5) Quit");
            System.out.println();
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            if(choice < 1 || choice > 5){
                System.out.println("Invalid choice. Please choose 1-5");
            }

        }
        while(choice < 1 || choice > 5);

        //process choice
        switch (choice){
            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;
            case 5:
                sc.nextLine();
                break;
        }

        //redisplay this menu unless user wants to quit
        if(choice != 5){
            ATM.printUserMenu(theUser, sc);
        }
    }
    public static void showTransHistory(User theUser, Scanner sc){
        int theAcct;
        //get account to display transaction history
        do{
            System.out.printf("Enter the number (1-%d) of the account whose transactions " +
                    "you  want to see: ", theUser.numAccounts());
            theAcct = sc.nextInt()-1;
            if(theAcct < 0 || theAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again.");

            }
        }
        while(theAcct < 0 || theAcct >= theUser.numAccounts());

        //print transaction history
        theUser.printAccTransHistory(theAcct);
    }

    
    public static void transferFunds(User theUser, Scanner sc){

        //initialize
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        do{
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer from: ", theUser.numAccounts());
            fromAcct = sc.nextInt()-1;
            if(fromAcct < 0 || fromAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again.");
            }
        }
        while(fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);
        //get the account to transfer to.
        do{
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer to: ", theUser.numAccounts());
            toAcct = sc.nextInt()-1;
            if(toAcct < 0 || toAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again.");
            }
        }
        while(toAcct < 0 || toAcct >= theUser.numAccounts());

        //get amount to transfer
        do{
            System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if(amount < 0){
                System.out.println("Amount must be greater than zero");
            }
            else if(amount > acctBal){
                System.out.printf("Amount must not be greater than \n " +
                        "balance of $%.02f. \n", acctBal);
            }
        }
        while(amount < 0 || amount > acctBal);
        //do transfer
        theUser.addAcctTransaction(fromAcct, -1*amount, String.format("Transfer" +
                "to account %s", theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(toAcct, amount, String.format("Transfer" +
                "to account %s", theUser.getAcctUUID(fromAcct)));
    }

    
    public static void withdrawFunds(User theUser, Scanner sc){
        //initialize
        int fromAcct;
        double amount;
        double acctBal;
        String memo;
        //get the Account to transfer from.
        do{
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to withdraw from: ", theUser.numAccounts());
            fromAcct = sc.nextInt()-1;
            if(fromAcct < 0 || fromAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again.");
            }
        }
        while(fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);
        //get amount to transfer
        do{
            System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if(amount < 0){
                System.out.println("Amount must be greater than zero");
            }
            else if(amount > acctBal){
                System.out.printf("Amount must not be greater than \n " +
                        "balance of $%.02f. \n", acctBal);
            }
        }
        while(amount < 0 || amount > acctBal);
        sc.nextLine();

        //get the memo
        System.out.print("Enter a memo: ");
        memo = sc.nextLine();

        //do withdrawl
        theUser.addAcctTransaction(fromAcct, -1*amount, memo);
    }

   
    public static void depositFunds(User theUser, Scanner sc){
        //initialize
        int toAcct;
        double amount;
        double acctBal;
        String memo;
        //get the Account to transfer from.
        do{
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer to deposit in: ", theUser.numAccounts());
            toAcct = sc.nextInt()-1;
            if(toAcct < 0 || toAcct >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again.");
            }
        }
        while(toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAcct);
        //get amount to transfer
        do{
            System.out.printf("Enter the amount to transfer (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if(amount < 0) {
                System.out.println("Amount must be greater than zero");
            }
        }
        while(amount < 0);
        sc.nextLine();

        //get the memo
        System.out.print("Enter a memo: ");
        memo = sc.nextLine();

        //do deposit
        theUser.addAcctTransaction(toAcct, amount, memo);
    }
}
