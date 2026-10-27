package in.ameya.securities.service;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


@Service
public class EncryptionService implements EnvironmentAware {
	
	private byte[] secbyte;
	private byte[] ivparmbyte;
	
	@Override
    public void setEnvironment(final Environment env) {
        secbyte = env.getRequiredProperty("encryption.key").getBytes();
		ivparmbyte = env.getRequiredProperty("encryption.iv").getBytes();
    }
	
	
	public String encrypt(String plainText) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKey secret = new SecretKeySpec(secbyte, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivparmbyte);
		cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
		byte[] encrypted = cipher.doFinal(plainText.getBytes("utf-8"));
		String encodeToString = java.util.Base64.getEncoder().encodeToString(encrypted);
		return encodeToString;
	}
    

	public String decrypt(String cipherText) throws Exception {
		byte[] sb = Base64.getDecoder().decode(cipherText);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		SecretKeySpec secret = new SecretKeySpec(secbyte, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivparmbyte);
		cipher.init(Cipher.DECRYPT_MODE, secret, ivSpec);
		byte[] decrypted = cipher.doFinal(sb);
		return new String(decrypted);
	}

}
