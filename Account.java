import java.util.ArrayList;

public class Account {
    
    private String name;
    
    private String uuid;
    /*
     *The User object that owns the account
     */
    private User holder;
    
    private ArrayList<Transaction> transactions;
    
    public Account(String name, User holder, Bank theBank){
        //Sets the account name, holder
        this.name = name;
        this.holder = holder;

        //get the account UUID
        this.uuid = theBank.getNewAccountUUID();

        //initialize transactions to empty list
        this.transactions = new ArrayList<Transaction>();

        
    }

    
    public String getUUID(){
        return this.uuid;
    }

   
    public String getSummaryLine(){

        //get account balance
        double balance = this.getBalance();

        //formats the line
        if(balance >= 0){
            return String.format("%s, $%.02f : %s", this.uuid, balance, this.name
            );
        }
        else{
            return String.format("%s, $(%.02f) : %s", this.uuid, balance, this.name
            );
        }
    }

    public double getBalance(){
        double balance = 0;
        for(Transaction s : this.transactions){
            balance += s.getAmount();
        }
        return balance;
    }

    
    public void printTransHistory(){
        System.out.printf("\n Transaction history for account %s\n", this.uuid);
        for(int i = this.transactions.size()-1; i >= 0; i--){
            System.out.println(this.transactions.get(i).getSummaryLine());
        }
        System.out.println();
    }

    
    public void addTransaction(double amount, String memo){
        //adds transactiont to list of transactions
        Transaction newTrans = new Transaction(amount, memo, this);
        this.transactions.add(newTrans);

    }
}
