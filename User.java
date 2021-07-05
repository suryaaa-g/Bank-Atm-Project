import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.security.MessageDigest;
public class User {
    //First name of user 
    private String firstName;
   //Last name of user 
    private String lastName;
    //id number 
    private String uuid;
    //security reason we use a hash
    private byte pinHash[];
    //number of account User has 
    private ArrayList<Account> accounts;

    //Constructor that creates a User 
    public User(String firstName, String lastName, String pin, Bank theBank){
        //Setting the users name
        this.firstName = firstName;
        this.lastName = lastName;

        //Stores the hash
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        }
        catch (NoSuchAlgorithmException e) {
           //Exception handler
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        // gets the new id for the uSer 
        this.uuid = theBank.getNewUserUUID(); 

        //creates an empty list of accounts 
        this.accounts = new ArrayList<Account>();

        //prints successful user formation 
        System.out.printf("New User %s, %s with ID %s created.\n", lastName,
                firstName, this.uuid);
    }
    
    public void addAccount(Account theAccount){
        this.accounts.add(theAccount);
    }

    
    public String getUUID(){
        return this.uuid;
    }

    
    public boolean validatePin(String pin){

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(pin.getBytes()), this.pinHash);
        }
        catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }
        return false;

    }

   
    public String getFirstName(){
        return this.firstName;
    }

    
    public void printAccountSummary(){

        System.out.printf("\n\n %s's accounts summary\n", this.firstName);
        for(int i = 0; i < this.accounts.size(); i++){
            System.out.printf("  %d) %s\n", i+1, this.accounts.get(i).getSummaryLine());
        }
        System.out.println();
    }

    
    public int numAccounts(){
        return this.accounts.size();
    }

    
    public void printAccTransHistory(int acctIdx){
        this.accounts.get(acctIdx).printTransHistory();
    }

    
    public double getAcctBalance(int accIdx){
        return this.accounts.get(accIdx).getBalance();
    }

    
    public String getAcctUUID(int acctIdx){
        return this.accounts.get(acctIdx).getUUID();
    }

    
    public void addAcctTransaction(int acctIdx, double amount, String memo){
        this.accounts.get(acctIdx).addTransaction(amount, memo);
    }
}
