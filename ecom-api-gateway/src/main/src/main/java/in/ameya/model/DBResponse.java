package in.ameya.model;

import java.math.BigDecimal;

public class DBResponse {

	private Integer status;
	private String message;
	private Object data;
	
	public DBResponse(Integer status, String message) {
		this.status = status;
		this.message = message;
	}

	
	public DBResponse(Integer status, String message, Object data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}


	public DBResponse(Integer status) {
		this(status, "No Response");
	}
	
	public DBResponse(BigDecimal result, String message) {
		this(result.intValue(), message);
	}

	public int getStatus() {
		return status != null ? status : 0;
	}
	public String getMessage() {
		return message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
}
