import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher; 
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
 
public class DecryptText {

	private static SecretKeySpec secretKey ;
	private static byte[] key ;
	private static String decryptedString;
    
    /*
     * This method is a construtor which firstly creates the securityKey by calling the setKey method and 
     * decrypts the encryptedCredential String by passing over to the decrypt method
     */
	public DecryptText(String encryptedCredential) {
		final String strPssword = "encryptor key";
		DecryptText.setKey(strPssword);
		DecryptText.decrypt(encryptedCredential.trim()); 
	}

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
	
    // Getter method of the Decrypted String
	public static String getDecryptedString() {
		return decryptedString;
	}

    //Setter method of the Decrypted String
	public static void setDecryptedString(String decryptedString) {
		DecryptText.decryptedString = decryptedString;
	}
	
    /*
     * This method decrypts the encrypted String passed from the constructor.
     * 
     * @Return: The Decrypted String.
     */
	public static String decrypt(String str) {

		try {
            
            //Creating a Cipher with the format of "Algorithm Name/ Mode/Padding Type of PKCS5Padding
            //AES - Advanced Encryption Standard 
            //ECB - Electronic Codebook Mode
            //PKCS5PADDING - A type of Padding Scheme
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
 
            //Intializing Cipher object with Decrypt Mode
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
            
            //Base64.decodeBase64 will be converting the encrypted String into the byte array which will be 
            // further used for the decryption process.
			byte[] dedcodedArray = Base64.decodeBase64(str);
             
            //The data is decrypted, depending the mode of the Cipher object initialization. 
            //The bytes in the input buffer, and any input bytes that may have been buffered during a previous
            //update operation, are processed, with padding being applied onto it. 
            //The result is stored in a new buffer. 
			byte[] finalArray = cipher.doFinal(dedcodedArray);
            
            //Converting the byte array to String and passing the resulting String to the setter method.
			setDecryptedString(new String(finalArray));

		} catch (Exception e) {

			System.out.println("Error while decrypting: " + e.toString());
		}
		
		return null;

	}  
 
}
