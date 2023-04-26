/**
 *
 * @author Prateek Kumar Oraon (B170078CS)
 *
 */

package blockchain.elements;

import blockchain.utility.Hash;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

public class Block {
    private String currentHash;
    private String prevHash;
    private ArrayList<Transaction> ledger;
    private String blockId;

    public Block(String prevHash){
        this.prevHash = prevHash;

        // Block ID is created by hashing combination of current timestamp
        // and a random number in between 0 to 999
        this.blockId = Hash.calculateHash(getCurrentTimeStamp() + new Random().nextInt(1000));

        // Initial Current Hash is calculated by hashing combination of Previous blocks hash and block ID
        this.currentHash = Hash.calculateHash(prevHash + blockId);

        this.ledger = new ArrayList<>();
    }

    // Returns total number of transactions in the block
    public int getNumTransactions(){
        return this.ledger.size();
    }

    public ArrayList<Transaction> getLedger(){
        return this.ledger;
    }

    public String getPrevHash(){
        return this.prevHash;
    }

    public String getBlockId(){
        return this.blockId;
    }

    public String getCurrentHash(){
        return this.currentHash;
    }

    // Returns a string containing current timestamp in milli seconds
    private String getCurrentTimeStamp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime()+"";

    }

    // checks if the block has 10 transactions
    // if yes than a new block will be created
    // in blockchain
    public boolean isFull(){
        return !(this.ledger.size() < 10);
    }

    // New transaction is added, valid and invalid both
    public void addNewTransaction(Transaction transaction){
        this.ledger.add(transaction);
        this.currentHash = Hash.calculateHash(this.currentHash + transaction.transactionId);
    }

    public boolean verifyHash(String hash) {

        String initialHash = Hash.calculateHash(this.prevHash + this.blockId);
        for(Transaction transaction : this.ledger){
            initialHash = Hash.calculateHash(initialHash + transaction.transactionId);
        }

        return initialHash.equals(hash);
    }
}
