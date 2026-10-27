package in.ameya.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import javax.net.ssl.*;

public class GetSSLContext {
	/**
	 * Loads an SSL certificate and initializes an SSLContext.
	 */
	public SSLContext getSSLContext(String certFilePath) throws Exception {
		String folderPath = certFilePath;
		String certiFileLoc = "";
		File folder = new File(folderPath);
		File[] certFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".crt"));

		if (certFiles != null && certFiles.length > 0) {
			for (File certFile : certFiles) {
				certiFileLoc = certFile.getAbsolutePath();
			//	System.out.println(certiFileLoc);
			}
		}

		// Load the .crt certificate
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		FileInputStream certInputStream = new FileInputStream(certiFileLoc);
		Certificate caCert = cf.generateCertificate(certInputStream);
		certInputStream.close();

		// Create a KeyStore and add the certificate
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null, null);
		keyStore.setCertificateEntry("caCert", caCert);

		// Create a TrustManager that trusts the certificate
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(keyStore);

		// Initialize SSL Context
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());

		return sslContext;
	}
}
