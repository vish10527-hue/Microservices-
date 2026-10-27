package in.ameya.schedulers.vo.mtSeries;

public class Type910 
{
	String completeMsgStr;
	String strSenderBIC;
	String strReceiverBIC;
	String strActivityCode;
	String strSenderCode;
	String strTRFNo;
	String strRelRef;
	String strValDate;
	String strCurrCode;
	String strAmount;
	String strOrdInst;
	String strIntermed;
	String strAccountDetails;
	String strSenderInfo;
	String strAccountID;
	String strOrdCustInf;
	String inwardOutwardType;
	String ackNackedFlag;
	
	public Type910()
	{
		completeMsgStr = "";
		strSenderBIC = "";
		strReceiverBIC = "";
		strActivityCode = "";
        strSenderCode = "";
        strTRFNo = "";
        strRelRef = "";
        strValDate = "";
        strCurrCode = "";
        strAmount = "";
        strOrdInst = "";
        strAccountID = "";
        strOrdCustInf = "";
        strIntermed = "";
        strAccountDetails = "";
        strSenderInfo = "";
        inwardOutwardType = "";
        ackNackedFlag = "";
	}
	public String getAckNackedFlag() {
		return ackNackedFlag;
	}

	public void setAckNackedFlag(String ackNackedFlag) {
		this.ackNackedFlag = ackNackedFlag;
	}
	public String getInwardOutwardType() {
		return inwardOutwardType;
	}

	public void setInwardOutwardType(String inwardOutwardType) {
		this.inwardOutwardType = inwardOutwardType;
	}
	public String getSenderBIC() {
		return strSenderBIC;
	}

	public void setSenderBIC(String strSenderBIC) {
		this.strSenderBIC = strSenderBIC;
	}

	public String getReceiverBIC() {
		return strReceiverBIC;
	}

	public void setReceiverBIC(String strReceiverBIC) {
		this.strReceiverBIC = strReceiverBIC;
	}
	
	public String getActivityCode() {
		return strActivityCode;
	}

	public void setActivityCode(String strActivityCode) {
		this.strActivityCode = strActivityCode;
	}

	public String getSenderCode() {
		return strSenderCode;
	}

	public void setSenderCode(String strSenderCode) {
		this.strSenderCode = strSenderCode;
	}

	public String getTRFNo() {
		return strTRFNo;
	}

	public void setTRFNo(String strTRFNo) {
		this.strTRFNo = strTRFNo;
	}

	public String getRelRef() {
		return strRelRef;
	}

	public void setRelRef(String strRelRef) {
		this.strRelRef = strRelRef;
	}

	public String getValDate() {
		return strValDate;
	}

	public void setValDate(String strValDate) {
		this.strValDate = strValDate;
	}

	public String getCurrCode() {
		return strCurrCode;
	}

	public void setCurrCode(String strCurrCode) {
		this.strCurrCode = strCurrCode;
	}

	public String getAmount() {
		return strAmount;
	}

	public void setAmount(String strAmount) {
		this.strAmount = strAmount;
	}

	public String getOrdInst() {
		return strOrdInst;
	}

	public void setOrdInst(String strOrdInst) {
		this.strOrdInst = strOrdInst;
	}

	public String getIntermed() {
		return strIntermed;
	}

	public void setIntermed(String strIntermed) {
		this.strIntermed = strIntermed;
	}

	public String getAccountDetails() {
		return strAccountDetails;
	}

	public void setAccountDetails(String strAccountDetails) {
		this.strAccountDetails = strAccountDetails;
	}

	public String getSenderInfo() {
		return strSenderInfo;
	}

	public void setSenderInfo(String strSenderInfo) {
		this.strSenderInfo = strSenderInfo;
	}

	public String getAccountID() {
		return strAccountID;
	}

	public void setAccountID(String strAccountID) {
		this.strAccountID = strAccountID;
	}

	public String getOrdCustInf() {
		return strOrdCustInf;
	}

	public void setOrdCustInf(String strOrdCustInf) {
		this.strOrdCustInf = strOrdCustInf;
	}
	
	public String getCompleteMsgStr() {
		return completeMsgStr;
	}
	
	public void setCompleteMsgStr(String completeMsgStr) {
		this.completeMsgStr = completeMsgStr;
	}

}
