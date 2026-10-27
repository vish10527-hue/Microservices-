package in.ameya.securities.passwordutility;


import java.security.KeyFactory;

import java.security.*;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.springframework.stereotype.Component;

@Component
public class AsymmetricEncrypter
{
	public static final String FIX_LENGTH_DATA = "NoPadding";
	public static final String VARIABLE_LENGTH_DATA = "PKCS5Padding";

	private Cipher cipher = null;
	// private String padding = null;
	private String algorithm = null;
	// private String enctype = null;
	// IvParameterSpec ivspec = null;
	RSAPublicKeySpec pub = null;
	RSAPrivateKeySpec priv = null;

	public AsymmetricEncrypter()
	{
		algorithm = "RSA";
	}

	public AsymmetricEncrypter(String algo)
	{
		algorithm = algo;
	}

	public void generateKeyPair(int length)
	{
		try
		{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm);
			kpg.initialize(length);
			KeyPair kp = kpg.genKeyPair();
			//Key publicKey = kp.getPublic();
			//Key privateKey = kp.getPrivate();
			KeyFactory fact = KeyFactory.getInstance(algorithm);
			pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
			priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);
		} catch (NoSuchAlgorithmException na)
		{
			System.out.println("Error in the algoritm specified: " + algorithm);
			na.printStackTrace();
		} catch (InvalidKeySpecException ikse)
		{
			System.out.println("Error in the key spec for: " + algorithm);
			ikse.printStackTrace();
		}
	}

	public byte[] encrypt(byte[] plainText) throws Exception
	{
		KeyFactory fact = KeyFactory.getInstance(algorithm);
		PublicKey pubKey = fact.generatePublic(pub);
		cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		byte[] encryptedText = cipher.doFinal(plainText);
		return encryptedText;
	}

	public byte[] decrypt(byte[] encryptedText) throws Exception
	{
		KeyFactory fact = KeyFactory.getInstance(algorithm);
		PrivateKey privateKey = fact.generatePrivate(priv);
		cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] plainText = cipher.doFinal(encryptedText);
		return plainText;
	}
	
	public byte[] decrypt(byte[] encryptedText, PrivateKey privateKey) throws Exception
	{
		//KeyFactory fact = KeyFactory.getInstance(algorithm);
		//PrivateKey privateKey = fact.generatePrivate(priv);
		cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] plainText = cipher.doFinal(encryptedText);
		return plainText;
	}
	
	public PrivateKey getPrivateKey() throws Exception
	{
		KeyFactory fact = KeyFactory.getInstance(algorithm);
		return fact.generatePrivate(priv);
	}
	
	public PublicKey getPublicKey() throws Exception
	{
		KeyFactory fact = KeyFactory.getInstance(algorithm);
		return fact.generatePublic(pub);
	}
}