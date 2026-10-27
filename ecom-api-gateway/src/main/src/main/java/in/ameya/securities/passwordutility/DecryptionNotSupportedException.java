package in.ameya.securities.passwordutility;

import org.springframework.stereotype.Component;

@Component
public class DecryptionNotSupportedException extends Exception {

	private static final long serialVersionUID = 981322904497387124L;

	public DecryptionNotSupportedException() { }
	
}