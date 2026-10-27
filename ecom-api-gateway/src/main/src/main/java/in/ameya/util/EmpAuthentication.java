package in.ameya.util;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import in.ameya.securities.passwordutility.AsciiEncryption;

@Component
@PropertySource(value = "classpath:application.properties")
public class EmpAuthentication {

	private static final Logger logger = LogManager.getLogger(EmpAuthentication.class);

	@Autowired
	Environment env;

	@Autowired
	AsciiEncryption asciiEncryption;

	private static int INDCOUNT = 0;
	private static String USRPRNCPL = "";
	static String ret_s5 = "";
	String ret_mesg = "Welcome ";
	HashMap hashmap = new HashMap(2);
	private static String URL_VAR = "url";
	public static HashMap urlHashMap = new HashMap();

	public Hashtable formatResults(NamingEnumeration namingenumeration) throws Exception {
		int i = 0;
		Hashtable hashtable = null;
		try {
			while (namingenumeration.hasMore()) {
				hashtable = new Hashtable();
				SearchResult searchresult = (SearchResult) namingenumeration.next();
				formatAttributes(searchresult.getAttributes(), hashtable);
				i++;
			}
		} catch (NamingException namingexception) {
			hashmap.put("error", "ConnectionError");
		}
		return hashtable;
	}

	public Hashtable formatAttributes(Attributes attributes, Hashtable hashtable) throws Exception {
		if (attributes == null)
			throw new Exception("This result has no attributes");
		try {
			for (NamingEnumeration namingenumeration = attributes.getAll(); namingenumeration.hasMore();) {
				Attribute attribute = (Attribute) namingenumeration.next();
				for (NamingEnumeration namingenumeration1 = attribute.getAll(); namingenumeration1.hasMore(); hashtable
						.put(attribute.getID(), namingenumeration1.next()))
					;
			}
		} catch (NamingException namingexception) {
			hashmap.put("error", "ConnectionError");
		}
		return hashtable;
	}

	private Hashtable getDN(String s, String url, String domain, String princ, String cred) throws Exception {
		logger.error("inside getDN");
		logger.error("getDN", "EmpAuthentication", "1");
		DirContext dircontext = null;
		try {
			dircontext = getDirContext(url, princ, cred);
			if (dircontext == null) {
				hashmap.put("error", "Ldap Error");
				return null;
			}
		} catch (Exception e) {
			hashmap.put("error", "ConnectionError");
			logger.error("ConnectionError");
		}
		String as[] = { "mailNickName", "distinguishedName", "mail", "name", "lname", "sAMAccountName", "Mobile" };

		SearchControls searchcontrols = new SearchControls();
		searchcontrols.setReturningAttributes(as);
		searchcontrols.setSearchScope(2);
		String s1 = "(&(sAMAccountName=" + s + ")( distinguishedName=*))";
		NamingEnumeration namingenumeration = null;
		Hashtable hashtable = null;
		String exten = "COM";
		logger.error("exten " + exten);
		namingenumeration = dircontext.search("DC=" + domain + ",DC=" + exten, s1, searchcontrols);
		if (namingenumeration != null) {
			hashtable = formatResults(namingenumeration);
		}
		if (hashtable == null) {
			ret_mesg = "Employee No Does not Exist";
			logger.error("Employee No Does not Exist");
			hashtable = new Hashtable();
			hashtable.put("error", "Invalid User/Password");
		} else {
			dircontext.close();
			return hashtable;
		}
		return hashtable;
	}

	public boolean checkEmployeeNo(String s) throws Exception {
		String domain = "";
		String princ = "";
		String cred = "";
		DirContext dircontext = getDirContext(domain, princ, cred);
		String as[] = { "sAMAccountName", "mailNickName", "distinguishedName" };
		SearchControls searchcontrols = new SearchControls();
		searchcontrols.setReturningAttributes(as);
		searchcontrols.setSearchScope(2);
		String s1 = "(&(sAMAccountName=" + s + ")( distinguishedName=*))";
		NamingEnumeration namingenumeration = dircontext.search("DC=" + domain + ",DC=com", s1, searchcontrols);
		Hashtable hashtable = formatResults(namingenumeration);
		if (hashtable == null) {
			ret_mesg = "Employee No Does not Exist";
			return false;

		} else {
			dircontext.close();
			return true;
		}
	}

	public DirContext getDirContext(String url, String princ, String cred) throws Exception {
		logger.error("getDirContext", "EmpAuthentication", "1");
		// Set up the SSL context with the certificate
		String CERT_PATH = env.getProperty("ldap.CERT_PATH");
		
		// Load the certificate
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		Certificate cert;
		try (FileInputStream fis = new FileInputStream(CERT_PATH)) {
			cert = cf.generateCertificate(fis);
		}

		// Create a KeyStore and add the certificate
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null, null); // Initialize empty KeyStore
		keyStore.setCertificateEntry("ldapsCert", cert);

		// Create TrustManagerFactory and initialize with KeyStore
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(keyStore);

		// Create and initialize the SSLContext
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, tmf.getTrustManagers(), null);

		// Set the SSLContext as the default
		SSLContext.setDefault(sslContext);
		
		String INITCTX = env.getProperty("ldap.PROVIDER");
		InitialDirContext initialdircontext = null;
		Hashtable hashtable = new Hashtable(11);
		hashtable.put("java.naming.factory.initial", INITCTX);
		hashtable.put("java.naming.provider.url", url);
		//hashtable.put("java.naming.security.principal", princ);
		//hashtable.put("java.naming.security.credentials", cred);
		
		// Specify LDAPS protocol
		hashtable.put(Context.SECURITY_PROTOCOL, "ssl");
		
		 // Authentication info
		hashtable.put(Context.SECURITY_AUTHENTICATION, "simple");
		hashtable.put(Context.SECURITY_PRINCIPAL, princ); // Replace with your bind DN
		hashtable.put(Context.SECURITY_CREDENTIALS, cred); // Replace with your password
		
		boolean valid = false;
		int ind = INDCOUNT;
		HashMap map = new HashMap();
		map = urlHashMap;

		try {
			initialdircontext = new InitialDirContext(hashtable);
			logger.error("getDirContext" + initialdircontext, "EmpAuthentication", "1");
			valid = true;
			return initialdircontext;
		} catch (Exception exception) {
			exception.printStackTrace();
			ret_mesg = "ConnectionError";
			hashmap.put("error", "ConnectionError");
		}
		return initialdircontext;
	}

	public Hashtable authenticateUser(String s, String s1) throws Exception {
		String INITCTX = env.getProperty("ldap.PROVIDER");
		String princ = env.getProperty("ldap.PRINCIPAL");
		String cred = asciiEncryption.decrypt(env.getProperty("ldap.CREDENTIALS"));
		String url = env.getProperty("ldap.URL");
		String domain = env.getProperty("ldap.DOMAIN");
		InitialDirContext initialdircontext = null;
		HashMap map = new HashMap();
		Hashtable hashtable = new Hashtable(11);
		hashtable.put("java.naming.factory.initial", INITCTX);
		logger.error("authenticateUser", "EmpAuthentication", "1");
		boolean flag = false;
		boolean valid = false;
		int ind = 0;
		String excep = "";
		map = urlHashMap;
		ind = INDCOUNT;

		if (!valid) {
			try {
				hashtable.put("java.naming.provider.url", url);
				valid = true;
			} catch (Exception e) {
				logger.error("Could not Connect to LDAP Server");
				throw new Exception("Could not Connect to LDAP Server");
			}
		}

		Hashtable hashtable1 = getDN(s, url, domain, princ, cred);

		if (hashtable1 == null) {
			hashtable1.put("error", "Invalid UserName/Password");
			return hashtable1;
		}
		try {
			USRPRNCPL = (String) hashtable1.get("distinguishedName");
		} catch (Exception e) {
			hashtable1.put("error", "Invalid UserName/Password");
			flag = false;
		}
		if (USRPRNCPL == null) {
			hashtable1.put("error", "Invalid UserName/Password");
			flag = false;
		} else {
			hashtable.put("java.naming.security.authentication", "simple");
			hashtable.put("java.naming.security.principal", USRPRNCPL);
			hashtable.put("java.naming.security.credentials", s1);

			try {
				initialdircontext = new InitialDirContext(hashtable);
				logger.error("successfull");
				hashtable1.put("no-error", "successfull");
				flag = true;
			} catch (Exception exception) {
				logger.error("Invalid UserName/Password");
				hashtable1.put("error", "Invalid UserName/Password");
			}
		}
		return hashtable1;
	}


}