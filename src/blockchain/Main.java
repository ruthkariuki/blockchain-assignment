/**
 *
 * @author Prateek Kumar Oraon (B170078CS)
 *
 */

package blockchain;

import blockchain.elements.Block;
import blockchain.elements.Transaction;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    // Stores all transactions which are pending
    private static ArrayList<Transaction> pendingTransactions;

    // Stores all transactions which are invalid. (Balance < amount transferred)
    private static ArrayList<Transaction> invalidTransactions;

    private static Blockchain blockchain;
    private static Scanner scanner;

    public static void main(String[] args){
        blockchain = Blockchain.getInstance();

        scanner = new Scanner(System.in);

        // Adding nodes to the blockchain
        blockchain.registerNode(100001,"Rajesh", 100);
        blockchain.registerNode( 100002,"Rohan", 200);
        blockchain.registerNode(100003,"Gauri", 300);
        blockchain.registerNode(100004,"Tezzz", 500);
        blockchain.registerNode(100005,"Mohit", 600);

        invalidTransactions = new ArrayList<>();
        pendingTransactions = new ArrayList<>();

        blockchain.showNodes();

        int check;
        do{
            int choice;
            System.out.println("Press 1 to create random transactions");
            System.out.println("Press 2 to show all Transactions");
            System.out.println("Press 3 to show balances");
            System.out.println("Press 4 to show all invalid transactions");
            System.out.println("Press 5 to show Block information");
            System.out.println("Press 0 to exit");

            choice = scanner.nextInt();
            check = choice;
            switch(choice){
                case 0:
                    System.out.println("Exiting");
                    scanner.close();
                    break;
                case 1:
                    System.out.println("Enter Number of transactions to perform");
                    int numTransaction = scanner.nextInt();
                    performRandomTransactions(numTransaction);
                    break;
                case 2:
                    blockchain.showAllTransaction();
                    break;
                case 3:
                    System.out.println("Balances");
                    blockchain.showNodes();
                    break;
                case 4:
                    showInvalidTransactions();
                    break;
                case 5:
                    System.out.println("Block Information\n");
                    showBlocksInformation();
                    break;
                default:
                    System.out.println("Invalid Option");
            }
        }while(check != 0);
    }

    private static void showBlocksInformation() {
        System.out.println("Blockchain length = " + blockchain.length());
        ArrayList<Block> blocks = blockchain.getBlocks();

        for(Block block : blocks){
            System.out.println("Block ID: " + block.getBlockId());
            System.out.println("Hash: " + block.getCurrentHash());
            System.out.println("Num of Transactions: " + block.getNumTransactions());
            System.out.println();
        }
    }

    private static void showInvalidTransactions() {
        System.out.println("Invalid Transactions:");
        for(Transaction transaction : invalidTransactions){
            System.out.println(
                    transaction.sender.name +
                            " ---> " +
                            "Balance = " +
                            transaction.senderBalance +
                            " ======> " +
                            transaction.receiver.name +
                            " ---> Amount = " +
                            transaction.amount
            );
        }
    }

    // Performs some random transactions
    private static void performRandomTransactions(int numTransactions){

        System.out.println("Status\tSender\tReceiver\tAmount\tTransaction ID");
        for(int i=0;i<numTransactions;i++){
            int[] arr = createRandomTransaction(blockchain.getNumNodes());

            Transaction transaction = new Transaction(
                    blockchain.getNode(arr[0]),
                    blockchain.getNode(arr[1]),
                    arr[2]
            );

            pendingTransactions.add(transaction);
            System.out.println(
                    transaction.status + "\t" +
                            transaction.sender.name + "\t" +
                            transaction.receiver.name + "\t" +
                            transaction.amount + "\t" +
                            transaction.transactionId
            );
        }

        System.out.println("\nPerform all Transactions (y/n)");
        String ch = scanner.next();
        if(ch.charAt(0) != 'y'){
            System.out.println("Transaction Cancelled");
            pendingTransactions.clear();
            return;
        }

        for(Transaction transaction : pendingTransactions){
            if(!blockchain.createTransaction(transaction)){
                invalidTransactions.add(transaction);
            }
        }

        pendingTransactions.clear();
        System.out.println("All Valid Transactions Successfully Performed\n");
    }

    private static int[] createRandomTransaction(int numNodes){
        Random rand = new Random();

        int amount = rand.nextInt(500);
        int sender = rand.nextInt(numNodes);
        int receiver = rand.nextInt(numNodes);
        while(sender == receiver) {
            receiver = rand.nextInt(numNodes);
        }

        return new int[]{sender, receiver, amount};
    }
}
