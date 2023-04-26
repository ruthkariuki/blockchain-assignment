/**
 *
 * @author Prateek Kumar Oraon (B170078CS)
 *
 */

package blockchain.utility;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash{

    public static String calculateHash(String input){

        try{
            return getHash(input);
        }catch(NoSuchAlgorithmException ex){
            System.out.println("Algorithm Not Found " + ex.toString());
        }

        return null;
    }

    private static String getHash(String input) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        BigInteger num = new BigInteger(1, hash);
        StringBuilder str = new StringBuilder(num.toString(16));


        while(str.length() < 32){
            str.insert(0,'0');
        }

        return str.toString();
    }
}
