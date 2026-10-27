package in.ameya.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class CommonDataException extends RuntimeException {

	private static final long serialVersionUID = 419394399943344967L;
	
	private int status;
	private DBResponse details;
	
	public CommonDataException(int scInternalServerError, DBResponse details) {
		super();
		this.status = scInternalServerError;
		this.details = details;
	}

	public CommonDataException(HttpStatus noContent, DBResponse status2) {
		// TODO Auto-generated constructor stub
	}

	public int status() {
		return status;
	}
	
	public DBResponse details() {
		return details;
	}

}
