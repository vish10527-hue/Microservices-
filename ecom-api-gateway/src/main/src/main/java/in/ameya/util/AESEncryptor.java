package in.ameya.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec; 


public final class AESEncryptor {
	private final static int AesBlockByteSize = 128 / 8;

    private final static int PasswordSaltByteSize = 128 / 8;
    //private final static int PasswordByteSize = 256 / 8;
    private final static int PasswordIterationCount = 100000;

    private final static int SignatureByteSize = 256 / 8;

    private final static int MinimumEncryptedMessageByteSize =
        PasswordSaltByteSize + // auth salt
        PasswordSaltByteSize + // key salt
        AesBlockByteSize + // IV
        AesBlockByteSize + // cipher text min length
        SignatureByteSize; // signature tag
    
    public static void main(String[] args) 
    {
	    String a = TestEncrypt("RelierUat@123");
	    String b = TestDecrypt(a);
	    System.out.println("a-->"+a);
	    System.out.println("b-->"+b);
    }
    
    public static String TestEncrypt(String EncryptedText)
    {
	    //	String EncryptedText = "28";
	    	String Key = "xf6n28JF5vk3jk54l6sd9xbdhdgzHdhr";
	    	String strEncryptedText = "";
	    	
	    	try {
				strEncryptedText = ByteToHex(AESEncryptor.EncryptString(EncryptedText, Key));
				/*
				 * System.out.println(strEncryptedText); String strDecryptedText =
				 * AESEncryptor.DecryptToString(HexToByte(strEncryptedText), Key);
				 * System.out.println(strDecryptedText);
				 */
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return strEncryptedText;
    }
    
    public static String TestDecrypt(String EncryptedText)
    {
	    	//String EncryptedText = "1efc08f91542c57dfdbd5ffeb1dca86d2192d815e3bcb4882180093be479c11ad6ad3afd3aac596433eabe6980cc3a937a50e6cc0d9a0c00daf47c851fd74689bd632ac39d75d040a53a3e7efa3f3807ff0cda1f069c7fae5baf2253b2fadca8";
	    	String Key = "xf6n28JF5vk3jk54l6sd9xbdhdgzHdhr";
	    	String strPlainText = "";
	    	
	    	try {
				strPlainText = AESEncryptor.DecryptToString(HexToByte(EncryptedText), Key);
				//System.out.println(strPlainText);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return strPlainText;
    }
    
    public static byte[] HexToByte(String strHexText)
    {
    	byte[] result = new byte[strHexText.length() / 2];
        
        for (int i = 0; i < result.length; i++) {
            int index = i * 2;
            int val = Integer.parseInt(strHexText.substring(index, index + 2), 16);
            result[i] = (byte)(val);
        }
        return result;
    }
    
    public static String ByteToHex(byte[] bytes)
    {
    	String strHexText = "";
    	StringBuilder strBuilder = new StringBuilder();
    	for (byte bt : bytes)
    	{
    		strBuilder.append(String.format("%02x", bt&0xff));
    	}
    	strHexText = strBuilder.toString();
    	return strHexText;
    }
    
    public static byte[] EncryptString(String toEncrypt, String password) throws Exception
    {
        // encrypt
    	byte[] keySalt = GenerateRandomBytes(PasswordSaltByteSize);
    	byte[] key = GetKey(password, keySalt);
    	byte[] iv = GenerateRandomBytes(AesBlockByteSize);

        byte[] cipherText;
        Cipher aes = CreateAes();
        SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
        aes.init(Cipher.ENCRYPT_MODE, originalKey, new IvParameterSpec(iv));
        byte[] plainText = toEncrypt.getBytes(StandardCharsets.UTF_8);
        cipherText = aes.doFinal(plainText);
        
        // sign
        byte[] authKeySalt = GenerateRandomBytes(PasswordSaltByteSize);
        byte[] authKey = GetKey(password, authKeySalt);

        byte[] result = MergeArrays(SignatureByteSize, authKeySalt, keySalt, iv, cipherText);
        Mac hmacSha256;
        
        try {
            hmacSha256 = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException nsae) {
            hmacSha256 = Mac.getInstance("HMAC-SHA-256");
        }
        
        int payloadToSignLength = result.length - SignatureByteSize;
        byte[] payloadToVerify = Arrays.copyOfRange(result, 0, payloadToSignLength); 
        SecretKeySpec secretKeySpec = new SecretKeySpec(authKey, "HmacSHA256");
        
        hmacSha256.init(secretKeySpec);
        byte[] signatureTag = hmacSha256.doFinal(payloadToVerify);
        
        result = MergeArrays(0, payloadToVerify, signatureTag);
        return result;
    }
    
    
    public static String DecryptToString(byte[] encryptedData, String password) throws Exception
    {
        if (!(encryptedData != null && encryptedData.length >= MinimumEncryptedMessageByteSize))
        {
            throw new Exception("Invalid length of encrypted data");
        }

        byte[] authKeySalt = Arrays.copyOfRange(encryptedData, 0, PasswordSaltByteSize); 
        byte[] keySalt = Arrays.copyOfRange(encryptedData, PasswordSaltByteSize, (2 * PasswordSaltByteSize));
        byte[] iv = Arrays.copyOfRange(encryptedData, 2 * PasswordSaltByteSize, (2 * PasswordSaltByteSize) + AesBlockByteSize);
        byte[] signatureTag = Arrays.copyOfRange(encryptedData, encryptedData.length - SignatureByteSize, encryptedData.length);

        int cipherTextIndex = authKeySalt.length + keySalt.length + iv.length;
        int cipherTextLength =
            encryptedData.length - cipherTextIndex - signatureTag.length;

        byte[] authKey = GetKey(password, authKeySalt);
        byte[] key = GetKey(password, keySalt);
        
        Mac hmacSha256;
        
        try {
            hmacSha256 = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException nsae) {
            hmacSha256 = Mac.getInstance("HMAC-SHA-256");
        }
        int payloadToSignLength = encryptedData.length - SignatureByteSize;
        byte[] payloadToVerify = Arrays.copyOfRange(encryptedData, 0, payloadToSignLength); 
        SecretKeySpec secretKeySpec = new SecretKeySpec(authKey, "HmacSHA256");

        // verify signature
        hmacSha256.init(secretKeySpec);
        byte[] signatureTagExpected = hmacSha256.doFinal(payloadToVerify);
       
        // constant time checking to prevent timing attacks
        int signatureVerificationResult = 0;
        for (int i = 0; i < signatureTag.length; i++)
        {
            signatureVerificationResult |= signatureTag[i] ^ signatureTagExpected[i];
        }

        if (signatureVerificationResult != 0)
        {
            throw new Exception("Invalid signature");
        }
        
        Cipher aes = CreateAes();
        SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
        aes.init(Cipher.DECRYPT_MODE, originalKey, new IvParameterSpec(iv));
        byte[] blockToDectypt = Arrays.copyOfRange(encryptedData, cipherTextIndex, cipherTextIndex + cipherTextLength); 
        byte[] plainText = aes.doFinal(blockToDectypt);
        return new String(plainText, StandardCharsets.UTF_8); 
    }
    
    
    private static byte[] MergeArrays(int additionalCapacity, byte[]... arrays) throws IOException
    {
        byte[] extra = new byte[additionalCapacity];
        ByteArrayOutputStream bArray = new ByteArrayOutputStream();
        for (int i = 0; i < arrays.length; i++)
        {
        	bArray.write(arrays[i]);
        }
        bArray.write(extra);
        return bArray.toByteArray();

    }
    
    private static byte[] GenerateRandomBytes(int numberOfBytes)
    {
        byte[] randomBytes = new byte[numberOfBytes];
        Random r = new Random();
        r.nextBytes(randomBytes);
        return randomBytes;
    }
    
    private static byte[] GetKey(String password, byte[] passwordSalt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), passwordSalt, PasswordIterationCount, password.length() * 8);
        return factory.generateSecret(pbeKeySpec).getEncoded();
    }
    
    private static Cipher CreateAes() throws NoSuchAlgorithmException, NoSuchPaddingException
    {
    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        return cipher;
    }
    
}
