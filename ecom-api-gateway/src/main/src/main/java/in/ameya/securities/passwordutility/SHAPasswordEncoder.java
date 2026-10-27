package in.ameya.securities.passwordutility;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Base64;

public class SHAPasswordEncoder 
	{
	/**
		* Reference holding an instance of type MessageDigest.
		*/    
		        private MessageDigest _sha = null;
		/**
		* The label of the digested password.
		*/        
		        private String _label;
		/**
		* String containing the values .
		*/        
		        private String _hexits;
		        
		        
		/**
		* No-argument constructor for this class
		* @author   Ameya Infovision
		* @version %I%, %G%
		*/
		public SHAPasswordEncoder() throws NoSuchAlgorithmException{
		            _sha = MessageDigest.getInstance ("SHA-1");
		            _label = "{SHA}";
		            _hexits = "0123456789abcdef";
		        }
		        
		/**
		* This method compares the digested password with the clear text password .
		* @param     digestedPassword  Hashed or digested password in SHA-1 format.
		* @param     clearTextPasswd  The password that has to be compared with the hashed or digested password
		* @return    boolean Returns true if the password in digest matches the clear text password
		* @exception java.io.IOException 
		* @exception java.lang.Exception 
		*/
//		    public boolean checkPassword(String digest,String clearTextPasswd) throws IOException,Exception{
//		        boolean retVal = false;
//		        boolean verbose = false;
//		        BASE64Decoder base64Decoder = null;
//		        byte[] pwHash = null;
//		            
//			    if (digest.regionMatches (true, 0, "{SHA}", 0, 5)) {
//		    		digest = digest.substring (5);
//		        }
//		    	    
//			    base64Decoder = new BASE64Decoder();
//			    byte[][] hs = split (base64Decoder.decodeBuffer (digest), 20);
//			    byte[] hash = hs[0];
//
//			    _sha.reset();
//				_sha.update (clearTextPasswd.getBytes());
//				pwHash = _sha.digest();
//				
//				if (_sha.isEqual (hash, pwHash)) {
//				    retVal = true;
//				}
//		        return retVal;
//		    }
//
//		 
//		/**
//		* This method generates the SHA-1 format digest from clear text password.
//		* @param     clearTextPassword  The clear text password .
//		* @return    String digested or hashed equivalent of the clear text password .
//		* @exception java.io.IOException
//		* @exception java.security.NoSuchAlgorithmException 
//		*/  
//		    public String digestPassword(String clearTextPassword)
//		        throws IOException,NoSuchAlgorithmException{
//		            
//		    	   String label = _label;        
//				    String password = null;
//				    byte[] pwhash = null;
//				    String pwd = null;
//				    BASE64Encoder base64Encoder = new BASE64Encoder();
//				    
//			        _sha.reset();
//			        _sha.update (clearTextPassword.getBytes());
//			        pwhash = _sha.digest();
//
//			        password = label + base64Encoder.encode (pwhash);
//			        int i = password.length();
//			        pwd = password.substring(0,i-1);
//			        return pwd;
//		    }

		public boolean checkPassword(String digest, String clearTextPasswd) throws IOException, Exception {

		    boolean retVal = false;
		    byte[] pwHash = null;
		        
		    if (digest.regionMatches(true, 0, "{SHA}", 0, 5)) {
		        digest = digest.substring(5);
		    }
		    
		    // Replace BASE64Decoder with java.util.Base64
		    byte[][] hs = split(Base64.getDecoder().decode(digest), 20);
		    byte[] hash = hs[0];

		    _sha.reset();
		    _sha.update(clearTextPasswd.getBytes());
		    pwHash = _sha.digest();

		    if (_sha.isEqual(hash, pwHash)) {
		        retVal = true;
		    }
		    
		    return retVal;
		}

		 
		/**
		 * This method generates the SHA-1 format digest from clear text password.
		 * @param     clearTextPassword  The clear text password.
		 * @return    String digested or hashed equivalent of the clear text password.
		 * @exception java.io.IOException
		 * @exception java.security.NoSuchAlgorithmException 
		 */  
		public String digestPassword(String clearTextPassword)
		    throws IOException, NoSuchAlgorithmException {
		        
		    String label = _label;        
		    String password = null;
		    byte[] pwhash = null;
		    String pwd = null;
		    
		    _sha.reset();
		    _sha.update(clearTextPassword.getBytes());
		    pwhash = _sha.digest();

		    // Replace BASE64Encoder with java.util.Base64
		    password = label + Base64.getEncoder().encodeToString(pwhash);
		    
		    // Note: No need to remove last character with new Base64 encoder
		    return password;
		}

		/**
		* This method concatenates two byte arrays and returns the result to the caller .
		* @param     l  byte array
		* @param     r byte array
		* @return    byte[] The concatenated byte array
		* @exception none
		*/
		    private byte[] concatenate (byte[] l, byte[] r) {
			    byte[] b = new byte [l.length + r.length];
			    System.arraycopy (l, 0, b, 0, l.length);
			    System.arraycopy (r, 0, b, l.length, r.length);
			    return b;
		    }
		/**
		* This method breaks the byte array into two arrays
		* @param     src  byte array
		* @param     n length
		* @return    byte[][] The split byte array
		* @exception none
		*/
		    private byte[][] split (byte[] src, int n) {
			    byte[] l, r;
			    if (src.length <= n) {
			        l = src;
			        r = new byte[0];
			    } else {
			        l = new byte[n];
			        r = new byte[src.length - n];
			        System.arraycopy (src, 0, l, 0, n);
			        System.arraycopy (src, n, r, 0, r.length);
			    }
			    byte[][] lr = {l, r};
			    return lr;
		    }

		    
		/**
		* This utility method converts the byte array into its hexa-decimal equivalent.
		* @param     byte[]  byte array
		* @return    String Array in Hexa-decimal notation
		* @exception none
		*/
		    private String toHex( byte[] block ) {
		    	StringBuffer buf = new StringBuffer();
			    for ( int i = 0; i < block.length; ++i ) {
			        buf.append( _hexits.charAt( ( block[i] >>> 4 ) & 0xf ) );
			        buf.append( _hexits.charAt( block[i] & 0xf ) );
			    }
			    return buf + "";
		    }

		/**
		* This utility method converts hexa-decimal equivalent into byte array
		* @param     String Hexadecimal value
		* @return    byte[] Byte array of the argument
		* @exception none
		*/
		    private byte[] fromHex( String s ) {
			    s = s.toLowerCase();
			    byte[] b = new byte [(s.length() + 1) / 2];
			    int j = 0;
			    int h;
			    int nybble = -1;
			    for (int i = 0; i < s.length(); ++i) {
			        h = _hexits.indexOf (s.charAt(i));
			        if (h >= 0) {
				    if (nybble < 0) {
				        nybble = h;
				    } else {
				        b[j++] = (byte)((nybble << 4) + h);
				        nybble = -1;
				    }
			        }
			    }
			    if (nybble >= 0) {
			        b[j++] = (byte)(nybble << 4);
			    }
			    if (j < b.length) {
			        byte[] b2 = new byte [j];
			        System.arraycopy (b, 0, b2, 0, j);
			        b = b2;
			    }
			    return b;
		    }
		}///~SHAPasswordEncoder