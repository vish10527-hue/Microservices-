package in.ameya.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import in.ameya.securities.passwordutility.AsciiEncryption;
import in.ameya.securities.passwordutility.DecryptionNotSupportedException;
import in.ameya.securities.passwordutility.EncryptDecrypt;


@Component
public class MessageUtil {

	@Autowired
	private ConfigurableEnvironment env;

	private ResourcePropertySource prop;

    private static final Logger log = LoggerFactory.getLogger(MessageUtil.class);

    private static Map<String, Properties> propertyMap = new HashMap<String, Properties>();
	
 // ketan
 	public static String getExternalBundleParserConfPath(String code)
     {
         String serverPath;
         Properties prop = new Properties();
         String s = "";
         try
         {
         	serverPath = (System.getProperty("user.dir")+File.separator+"src"+File.separator+"properties"+File.separator+"ParserConf.properties");
   			prop.load(new FileInputStream(serverPath));
   			s = prop.getProperty(code);	
 		    if(s == null || s.trim().length() == 0)
         	   s = "Message for code " + code + " has not been defined " + "in properties file '" + "properties.ParserConf" + ".properties'";
             s = s.trim();
         }
         catch(NullPointerException nullpointerexception)
         {
         	s = "No exception code specified - unable to get exception message";
         }
         catch(MissingResourceException missingresourceexception)
         {
         	s = "Missing properties file: 'properties.FilePathExternal.properties' or missing resource key " + code ;
         }
         catch (IOException e) {
         	s = "Missing properties file: 'properties.FilePathExternal.properties' or missing resource key " + code ;
 		}
       
         return s;
     }
	
	public static String getConfiguration(String bundle, String key, boolean decypted) {
		String value = "";
		bundle = bundle.toLowerCase();
		Properties prop = propertyMap.get(bundle);
		
		if(prop == null) {
			String serverPath = (System.getProperty("user.dir") +File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+""+bundle+".properties");

			prop = new Properties();
  		 	try {
				prop.load(new FileInputStream(serverPath));
				propertyMap.put(bundle, prop);
			} catch (IOException e) {
				value = "Missing properties file: '"+bundle+".properties'";
				log.error(value, e, String.format("MessageUtil.getConfiguration(%s, %s, %b)", bundle, key, decypted), null);
				return value;
			}
		}
		
        try {
  			value = prop.getProperty(key);	
		    if(value == null || value.trim().length() == 0) {
		    	value = "Message for code " + key + " has not been defined " + "in properties file '" + bundle + ".properties'";
		    } else if(decypted) {
		    	try {
		    		value = EncryptDecrypt.decrypt(value.trim());
				} catch (Exception e) {
					value = "Message for code " + key + ": '"+ value+"' cannot be decrypted";
					log.error(value, e, String.format("MessageUtil.getConfiguration(%s, %s, %b)", bundle, key, decypted), null);
				}
		    } else
            value = value.trim();
        } catch (MissingResourceException | NullPointerException e) {
        	value = "Missing resource key: '" + key + "' in properties file: '"+bundle+".properties'" ;
        	log.error(value, e, String.format("MessageUtil.getConfiguration(%s, %s, %b)", bundle, key, decypted), null);
		}
        return value;
    }
	
	public static String getConfiguration(String bundle, String key) {
		return getConfiguration(bundle, key, false);
	}
	
	public static String getConfigurationDecrypted(String bundle, String key) {
		return getConfiguration(bundle, key, true);
    }

	
	/**
	 * @param password
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String getDecryptPassword(String password){
		AsciiEncryption ae = new AsciiEncryption();
		try {
			password = ae.decrypt(password);
		} catch (DecryptionNotSupportedException e) {
			// TODO Auto-generated catch block
		}
		return password;
	}
	

	
	public String getMXPath(String key) {
		key = key.trim();
		String prefix = "";
		String actualKey = "";
		String splitArray[] = key.split("[.]", 0);
		if(splitArray.length == 2) {
			prefix = splitArray[0];
			actualKey = splitArray[1];
		} else if(splitArray.length > 2) {
			prefix = splitArray[0];
			actualKey = String.join(".", Arrays.copyOfRange(splitArray, 1, splitArray.length));
		} else {
			System.err.println("Error NoSplit : getMXPath with key -> " + key);
			return "";
		}
		String propertyName = "";
        Properties properties = new Properties();
        String value = "";
        try {
        	propertyName = prefix + "MXpath.properties";
        	prop = new ResourcePropertySource("classpath:"+propertyName);
        	env.getPropertySources().addFirst(prop);
  			value = env.getProperty(actualKey);	
  		    if(value == null || value.trim().length() == 0) {
  		    	System.err.println("Error NoValue : getMXPath with key -> " + key);
  		    	value = "";
  		    }
        } catch(MissingResourceException ex) {
        	System.err.println("Error NoProperty : getMXPath with key -> " + actualKey);
        }catch(Exception ex) {
        	System.err.println("Error NoProperty : getMXPath with key -> " + actualKey);
        }finally {
        	env.getPropertySources().remove("class path resource ["+propertyName+"]");
		}
        //return value.trim();
     // Modify XPath if performAction is "Y"
        return value.trim();
    }
	private static String modifyXPath(String xpath, String performAction) {
	    if (xpath == null || !xpath.startsWith("//") || !"Y".equalsIgnoreCase(performAction)) {
	        return xpath; // Return as-is if the flag is "N" or invalid input
	    }

	    // Determine the prefix based on the root element
	    String prefix = xpath.startsWith("//AppHdr") ? "head:" : xpath.startsWith("//Document") ? "pacs:" : "";
	    if (prefix.isEmpty()) {
	        return xpath; // If it doesn't match known cases, return as-is
	    }

	    // Use StringBuilder for efficient modification
	    StringBuilder modifiedXPath = new StringBuilder("//" + prefix);
	    for (int i = 2; i < xpath.length(); i++) {
	        if (xpath.charAt(i) == '/' && (i + 1 < xpath.length() && xpath.charAt(i + 1) != '@')) {
	            modifiedXPath.append('/').append(prefix); // Append prefix only if NOT followed by '@'
	        } else {
	            modifiedXPath.append(xpath.charAt(i)); // Append normally
	        }
	    }

	    return modifiedXPath.toString();
	}

}
