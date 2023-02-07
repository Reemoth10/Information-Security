

// All the imports that we need for this project:
import java.io.File;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Task7 {

   
    public static byte[] HexToByte(String hex){ // Turn Hex to Bytes value.
       
        byte[] data = new byte[hex.length()/2];
        for (int i = 0; i < hex.length(); i += 2){
            data[i/2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)+Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }


   // Turning Byte to hex values:
    public static String BytesToHex(byte[] bytes){ 

        char[] HexChars = new char[bytes.length * 2];
        char[] HexArrayValue = "0123456789ABCDEF".toCharArray();
        
        for (int i = 0; i < bytes.length; i++){
            int x = bytes[i] & 0xFF;
            HexChars[i*2] = HexArrayValue[x >>> 4];
            HexChars[i*2+1] = HexArrayValue[x & 0x0F];
        }
        return new String(HexChars); // return new hex char.
    }


    //The Encryption Method:
    public static byte[] encrypt(String PT, byte[] invex, String K){ // PT: plaintext, invex: initial vector, K: key.
        
        try{

            IvParameterSpec iv = new IvParameterSpec(invex);
            SecretKeySpec skeySpec = new SecretKeySpec(K.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] cipherText = cipher.doFinal(PT.getBytes());

            return cipherText;
        } catch (Exception exp){  

        }
        return null;
    }


    //The Decryption Method:
    public static String decrypt(byte[] encrypt, byte[] invex, String K){ // encrypt: encrypted message, invex: initial value, K: key.

        try{

            IvParameterSpec iv = new IvParameterSpec(invex);
            SecretKeySpec skeySpec = new SecretKeySpec(K.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(encrypt);

            return new String(original);
        } catch (Exception exp){
            
        }
        return null;
    }


    //Guess The Key Method:
    static public void GuessTheKey() {

        try{

            File file = new File("/Users/XI/Desktop/words.txt"); // using file IO.
            Scanner scan = new Scanner(file); // Scanner to scan document.

            while (scan.hasNextLine()){ // Loop to read from file.
                String K = scan.nextLine(); //K: key.
                K = K.replace("\n", "");

                 if (K.length() < 16){
                    for (int i = K.length(); i < 16; i++) {
                        K += "#"; // Replace the rest of the key that is < 16 with hashes.
                    }
                } 

                //The inputs for GuessTheKey:
                String plaintext = "This is a top secret.";//PlainText 
                byte[] iv = HexToByte("aabbccddeeff00998877665544332211"); //IV 
                byte[] encryptText = encrypt(plaintext, iv, K);

                if (encryptText != null){

                    //Turn Bytes to Hex:
                    String encryptedHex = BytesToHex(encryptText);
                    if (encryptedHex.toLowerCase().equals("764aa26b55a4da654df6b19e4bce00f4ed05e09346fb0e762583cb7da2ac93a2")){
                        System.err.println("Key Search Is Now Done! The Results >>>");

                        String plain = decrypt(encryptText, iv, K); //Decryption of the text:
                        if (plain != null){ //Print the final results.
                            System.out.println("Plain Test is: " + plain);
                            System.out.println("Key Used in ASCII: " + K);
                            String keyHex = BytesToHex(K.getBytes());//Key in hex
                            System.out.println("Key Used in Hex: " + keyHex);
                            System.out.println("Encrypted Text in Hex: " + encryptedHex);

                        }else{
                            System.err.println("No Key Found"); // Error message if key not found.
                        }

                    }
                }
            }

        }
        catch (Exception exp){
            exp.printStackTrace();
        }
    }


    public static void main(String[] args){
        GuessTheKey();
    }
}
