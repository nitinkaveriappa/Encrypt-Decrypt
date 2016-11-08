import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptText {

	private static SecretKeySpec secretKey;
	private static byte[] key;

	private static String encryptedString;

    /*
     * This method creates the secretKey with respect to the type of Algorithm used.
     */
	public static void setKey(String myKey) {

		MessageDigest sha = null;
		try {
			//Initializing the Key Bytes format as UTF-8
			key = myKey.getBytes("UTF-8");
            
            //Initializing the MessageDigest object with the Instance of "SHA-1" format
			sha = MessageDigest.getInstance("SHA-1");
            
            //Performs a final update on the digest using the specified array of bytes,then completes the digest computation. 
            //That is, this method first calls update(input), passing the input array to the update method, then calls digest(). 
			key = sha.digest(key);
			
			byte[] copyArray = new byte[16];
			System.arraycopy(key, 0, copyArray, 0, Math.min(key.length, 16));
 
            //Creating the secretKey with the byteArray created and the Algorithm.
			secretKey = new SecretKeySpec(copyArray, "AES");

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
  
    // Getter method of the Encrypted String
	public static String getEncryptedString() {
		return encryptedString;
	}

    //Setter method of the Encrypted String
	public static void setEncryptedString(String encryptedString) {
		EncryptText.encryptedString = encryptedString;
	}

    /*
     * This method encrypts the String passed from the main method.
     * 
     * @Return: The Encrypted String.
     */
	public static String encrypt(String strToEncrypt) {
		try {
            
            //Creating a Cipher with the format of "Algorithm Name/ Mode/Padding Type of PKCS5Padding
            //AES - Advanced Encryption Standard 
            //ECB - Electronic Codebook Mode
            //PKCS5PADDING - A type of Padding Scheme
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            //Intializing Cipher object with Encrypt Mode
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            //Base64.decodeBase64 will be converting the String which needs to be Encrypted into the byte array 
            //which will be further used for the Encryption process.
            //Converting the byte array to String and passing the resulting String to the setter method.
			setEncryptedString(Base64.encodeBase64String(cipher.doFinal(strToEncrypt
					.getBytes("UTF-8"))));

		} catch (Exception e) {

			System.out.println("Error while encrypting: " + e.toString());
		}
		return null; 
	} 
	
	public static void main(String args[]) {

        //Update this field to the value for which the encryption should be performed.
		final String strToEncrypt = "D0rpssD3";
		final String strPssword = "encryptor key";
		EncryptText.setKey(strPssword);

        //Calling the encrypt method by passing the String to Encrypt
		EncryptText.encrypt(strToEncrypt.trim());

        //Prints the Encrypted String.
		System.out.println("String to Encrypt: " + strToEncrypt);
		System.out.println("Encrypted: " + EncryptText.getEncryptedString());
 
	}

}

 
