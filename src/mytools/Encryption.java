
package mytools;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class Encryption {
    
    public static String UNICODE_FORMAT = "UTF-8";
    
    public static SecretKey generateKey(){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(UNICODE_FORMAT);
            SecretKey myKey = keyGenerator.generateKey();
            return myKey;
            
        } catch (Exception e) {
            return null;
        }
    }
    
    public static byte[] encryptString(String dataToEncrypt, SecretKey myKey, Cipher cipher){
        byte[] encryptedData = {};
        
        return encryptedData;
    }
    
    public static String decryptString(byte[] dataToDecreypt, SecretKey myKey, Cipher cipher){
        String text = "";
        
        return text;
    }
}
