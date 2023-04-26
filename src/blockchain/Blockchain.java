/**
 *
 * @author Prateek Kumar Oraon (B170078CS)
 *
 */

package blockchain;

import blockchain.elements.Block;
import blockchain.elements.Node;
import blockchain.elements.Transaction;
import blockchain.utility.Hash;
import java.util.ArrayList;

public class Blockchain {

    // Contains all the nodes in blockchain
    private ArrayList<Node> nodes;

    // Contains all blocks in blockchain
    private ArrayList<Block> blocks;

    // Blockchain instance
    private static Blockchain blockchain;

    // Constructor
    public Blockchain(){
        this.nodes = new ArrayList<>();
        this.blocks = new ArrayList<>();
    }

    // Returning instance of this singleton class
    public static Blockchain getInstance(){
        if(blockchain == null){
            blockchain = new Blockchain();
        }

        return blockchain;
    }

    // Returns number of nodes in the blockchain
    int getNumNodes(){
        return this.nodes.size();
    }

    // Returns a node present at the particular index in nodes
    Node getNode(int index){
        return nodes.get(index);
    }

    // Returns length of blockchain (Number of blocks)
    int length(){
        return this.blocks.size();
    }

    // Returns all blocks in blockchain
    ArrayList<Block> getBlocks(){
        return this.blocks;
    }

    // Registers a new node to the blockchain
    void registerNode(int accountNo, String name, int initialBalance){
        this.nodes.add(new Node(accountNo, name, initialBalance));
    }

    // Creates a new transaction
    boolean createTransaction(Transaction transaction){
        if(blocks.size() == 0){
            // if there are no blocks then a new block will be created first
            addNewBlock();
        }

        // getting the currently used block i.e. the block in which the transactions are
        // currently being done
        Block currentBlock = this.blocks.get(this.blocks.size()-1);

        // reference to 2nd last block
        Block prevBlock = null;

        if(this.length() > 1){
            // if the size of blockchain is greater than one than 2nd last block is
            // taken otherwise prevBlock is kept null
            prevBlock = this.blocks.get(this.blocks.size()-2);
        }


        if(currentBlock.isFull()){
            // if number of transactions in current block is greater than 10
            // the a new block is created
            addNewBlock();

            // current block will become the 2nd last block
            prevBlock = currentBlock;

            // now the new block created will be the current block in which
            // transactions will be done
            currentBlock = this.blocks.get(this.blocks.size() - 1);
        }


        if(
                prevBlock != null &&
                currentBlock.getPrevHash().equals(prevBlock.getCurrentHash()) &&
                currentBlock.verifyHash(currentBlock.getCurrentHash())
        ){

            // If blockchain length is greater than 1 and
            // current block's previous hash is equal to current hash of previous block and
            // current block's current hash is equal to the hash of current block's previous hash and blockID
            // then the transactions is added to currentBlock with status false as the transactions are not
            // verified yet
            currentBlock.addNewTransaction(transaction);

            // if transactions are verified and confirmed then the status is changed to true and returned
            return transaction.confirmTransaction();
        }else if(currentBlock.verifyHash(currentBlock.getCurrentHash())){

            // If currentBlock's previous Hash is equal to calculated hash of previous hash and block ID
            // then the transaction is added with status false
            currentBlock.addNewTransaction(transaction);

            // if transaction is verified and confirmed then the status is changed to true and returned
            return transaction.confirmTransaction();
        }

        return false;
    }

    private void addNewBlock(){
        Block block;

        // Checking if the blockchain length is 0
        if(this.length() == 0) {
            // First block's hash is calculated upon hashing "first-block"
            block = new Block(Hash.calculateHash("first-block"));
        }else{
            block = new Block(getLastBlockHash());
        }
        blocks.add(block);
    }

    // Returns the hash of last block i.e. currently used block in blockchain
    private String getLastBlockHash(){
        return this.blocks.get(this.blocks.size()-1).getCurrentHash();
    }

    // Prints all nodes in blockchain
    void showNodes(){
        System.out.println("Name\tBalance");
        for(Node node : this.nodes){
            System.out.println(node.name + "\t" + node.balance);
        }
        System.out.println();
    }

    // Prints all Transactions - successful and unsuccessful both
    void showAllTransaction(){
        System.out.println("Status\tSender\tReciever\tAmount\tTransaction ID");
        for(Block block : this.blocks){
            for(Transaction transaction : block.getLedger()){
                System.out.println(
                        transaction.status + "\t" +
                        transaction.sender.name + "\t" +
                        transaction.receiver.name + "\t" +
                        transaction.amount + "\t" +
                        transaction.transactionId
                );
            }
        }
    }
}
