package in.ameya.schedulers.vo.mtSeries;

public class Type61Data 
{
	String strValDate;
	String strTranType;
	String strDealAmount;
	String strTranRefNo;
	String strMTRefno;
	String strBankDetails;
	String strField86Val;
	String strStatLine;
	String strEntryDate;
	String ackNackedFlag;
	String transTypeIdentificationCode;
	
	public Type61Data()
	{
		strValDate = "";
        strTranType = "";
        strDealAmount = "";
        strTranRefNo = "";
        strMTRefno = "";
        strBankDetails = "";
        strStatLine = "";
        strField86Val = "";
        strEntryDate = "";
        ackNackedFlag = "";
        transTypeIdentificationCode = "";
	}
	
	public String getTransTypeIdentificationCode() {
		return transTypeIdentificationCode;
	}

	public void setTransTypeIdentificationCode(String transTypeIdentificationCode) {
		this.transTypeIdentificationCode = transTypeIdentificationCode;
	}

	public String getAckNackedFlag() {
		return ackNackedFlag;
	}

	public void setAckNackedFlag(String ackNackedFlag) {
		this.ackNackedFlag = ackNackedFlag;
	}
	public String getValDate() {
		return strValDate;
	}

	public void setValDate(String strValDate) {
		this.strValDate = strValDate;
	}

	public String getTranType() {
		return strTranType;
	}

	public void setTranType(String strTranType) {
		this.strTranType = strTranType;
	}

	public String getDealAmount() {
		return strDealAmount;
	}

	public void setDealAmount(String strDealAmount) {
		this.strDealAmount = strDealAmount;
	}

	public String getTranRefNo() {
		return strTranRefNo;
	}

	public void setTranRefNo(String strTranRefNo) {
		this.strTranRefNo = strTranRefNo;
	}

	public String getMTRefno() {
		return strMTRefno;
	}

	public void setMTRefno(String strMTRefno) {
		this.strMTRefno = strMTRefno;
	}

	public String getBankDetails() {
		return strBankDetails;
	}

	public void setBankDetails(String strBankDetails) {
		this.strBankDetails = strBankDetails;
	}

	public String getField86Val() {
		return strField86Val;
	}

	public void setField86Val(String strField86Val) {
		this.strField86Val = strField86Val;
	}

	public String getStatLine() {
		return strStatLine;
	}

	public void setStatLine(String strStatLine) {
		this.strStatLine = strStatLine;
	}
	
	public String getEntryDate() {
		return strEntryDate;
	}

	public void setEntryDate(String strEntryDate) {
		this.strEntryDate = strEntryDate;
	}

}
