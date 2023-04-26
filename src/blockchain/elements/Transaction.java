/**
 *
 * @author Prateek Kumar Oraon (B170078CS)
 *
 */

package blockchain.elements;

import blockchain.utility.Hash;
import java.sql.Timestamp;
import java.util.Random;

public class Transaction {
    public Node sender;
    public String transactionId;
    public Node receiver;
    public int amount;
    public int senderBalance;
    public int receiverBalance;
    public boolean status = false;

    public Transaction(Node sender, Node receiver, int amount){
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;

        // Transaction id is hash of the combination of sender's account number,
        // receiver's account number, current timestamp and a random number between 0 to 999
        this.transactionId = Hash
                .calculateHash(
                        Integer.toString(sender.accountNo) +
                                Integer.toString(receiver.accountNo) +
                                getCurrentTimeStamp() +
                                new Random().nextInt(1000)
                );
    }

    // Confirming the transaction is valid or not
    public boolean confirmTransaction(){
        this.status = isValid();
        if(this.status){

            // If valid than performing transaction and
            // reflecting it into sender's and receiver's
            // account balance
            sender.balance = senderBalance = sender.balance - amount;
            receiver.balance = receiverBalance = receiver.balance + amount;
        }else {
            senderBalance = sender.balance;
            receiverBalance = receiver.balance;
        }

        return this.status;
    }

    private boolean isValid(){
        return this.sender.balance >= this.amount;
    }

    private String getCurrentTimeStamp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime()+"";
    }

}
