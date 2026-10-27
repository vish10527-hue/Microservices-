package in.ameya.util;

import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import in.ameya.model.master.DropdownVo;
import in.ameya.securities.passwordutility.EncryptDecrypt;
import in.ameya.service.master.DropdownService;

@Component
@PropertySource("classpath:application.properties")
public class AESEncryptDecryptUtil {
	
	static String password = "1743011100228071209168110254207085225104076139099223191104189057052004";
	static String salt = "1743011251094159195099140010066165";
	//private static final Logger log = LoggerFactory.getLogger(AmeyaUtil.class);
	
	@Value("${oracle.db.encrypt.decrypt.enable}")
	private boolean oracledbencdecenable;
				
	
	AESEncryptDecryptUtil(DropdownService dropdownService) 
	{
		ArrayList<DropdownVo> list = dropdownService.getSystemParams("AES_ENCRYPT_DECRYPT_SALT");
		if (list != null) {
			salt = list.get(0).getKey();
		}
	}
	 
		
	public String encrypt(String plainText) throws Exception
	{
		String encodeToString = "";
		try 
		{
			if(plainText != null && !("").equalsIgnoreCase(plainText) && oracledbencdecenable)
			{
				String passwordTemp = EncryptDecrypt.decrypt(password);
				String saltTemp = EncryptDecrypt.decrypt(salt);
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				SecretKey secret = new SecretKeySpec(String.format("%1$32s", passwordTemp).getBytes(), "AES");
				IvParameterSpec ivSpec = new IvParameterSpec(String.format("%1$16s", saltTemp).getBytes());
				cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
				byte[] encrypted = cipher.doFinal(plainText.getBytes("utf-8"));
				encodeToString = java.util.Base64.getEncoder().encodeToString(encrypted);
			}
			else
			{
				encodeToString = plainText;
			}
		}catch (Exception e) 
		{
			encodeToString = plainText;
		}
		return encodeToString;
	}
	
	public static String decrypt(String cipherText) throws Exception
	{
		String decryptedString = "";
		try {
				if(cipherText != null && !("").equalsIgnoreCase(cipherText))
				{
					String passwordTemp = EncryptDecrypt.decrypt(password);
					String saltTemp = EncryptDecrypt.decrypt(salt);
					byte[] sb = Base64.getDecoder().decode(cipherText);
					Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
					SecretKey secret = new SecretKeySpec(String.format("%1$32s", passwordTemp).getBytes(), "AES");
					IvParameterSpec ivSpec = new IvParameterSpec(String.format("%1$16s", saltTemp).getBytes());;
					cipher.init(Cipher.DECRYPT_MODE, secret, ivSpec);
					byte[] decrypted = cipher.doFinal(sb);
					decryptedString = new String(decrypted);
				}
				else
				{
					decryptedString = cipherText;
				}
		}catch (Exception e) 
		{
			decryptedString = cipherText;
		}
		return decryptedString;
	}
	
	
	public static void main(String args[]) throws Exception 
	{
		//AESEncryptDecryptUtil a = new AESEncryptDecryptUtil();
		//String enc = a.encrypt("Ketan");
		//String dec = a.decrypt("NyI2sj2766MmsAZQbkDC0jSEMBt89rtmRBMd0SVjlt5sk8p2sBz++Spc4E14wZKpNXCg1/wy1klTjVaBTSr87wqpVsKQzFNhG5XqXPZSpJ3PRDbDn2eEKE28/JIM//sJzjMEFlGhiXbM2Zua+T3rhYLhN918ookwMdn7x0h8dRNjOromer6kWu5m4fFvqO20swL/3sj7s+YhvA3HMuaA4/1rgL8lM6dLYhfxaDkz2MOSs1ZT4fvzVhDRLvLRjm/bgadXmq1qP/JM1wCo6tlYsUSufUhI7bS2SbERRefrrZcaInOVwuj9tjVLhaVPKrTNsuSuolsWwr1J06wtp3XfIqDo8k8Y0d9zOgaEmPlGsX2AweBJr8d87c+geappo473Y5jdamd66+OT0CwpRGl9BY9uOaPdh3GzdvlmoFRS8aa+BxP/DsJPAmH20cRPioVGOrlWQ6T94pkIlGo/C8KKgSZdMuOWu6FRcKKILp/v7SnMqsjLOa7mRzUjfPtVNbgPlAUoYWJZYOjWrNJYWWE+kHXDx2BgwBz4GsDOzqbwRVrKnF+auVcA1SKp2ACG6FD8/SX7dxfRmW3SMqmumqeD8UcbyiQWopbGxqGbwA/v8/7mfR2RhsOV+QVBuLRP8vxdKdCLmOXOj4xtJR+zyq67nKStfWTyU2Zjk0gUIDxNzmxgy60jG1Me+SvsVE1uNFeHV2cu/CjOJtBvu3ONyulQ+wvqUZF9GvKbg/Lq6YzT1FvJMMbiTwBnTitUzkCaPEG25fioensF/1QwQFjeq/M2F0X1v+zZB1Xl7ht/WLPH3xU8SYXq3fKoGdU09xqmKdpK1WdykHk75e1ruv6jpBMCn7CPQtkukoUYMWWrWTwrEDiyrM6G/6a7RVcuVM6eNnX76gKpvzTHCR2rR2iS0sTkZA==");
		//System.out.println("--->"+enc);
		//System.out.println("--->"+dec);
	}

}
