package in.ameya.util;

import java.io.Serializable;
import java.sql.ResultSet;

public class ExportDataResultSet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ResultSet resultSet;
	private String sheetName;

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
}
