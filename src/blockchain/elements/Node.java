/**
 *
 * @author Prateek Kumar Oraon (B170078CS)
 *
 */

package blockchain.elements;

public class Node {
    public String name;
    public int accountNo;
    public int balance;

    public Node(int accountNo, String name, int balance){
        this.name = name;
        this.accountNo = accountNo;
        this.balance = balance;
    }
}
