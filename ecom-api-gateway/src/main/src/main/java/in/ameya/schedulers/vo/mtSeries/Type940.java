package in.ameya.schedulers.vo.mtSeries;

import java.util.ArrayList;

public class Type940 
{
	String completeMsgStr;
	int intMessageNo;
	String strSenderBIC;
	String strReceiverBIC;
	String strActivityCode;
	String strSenderCode;
	String strTRFNo;
	String strTranType;
	String strValueDate;
	String strCurrCode;
	String strAmount;
	String strMiddleAmount;
	String strAccountID;
	String strStatOrSeq;
	String strRelRef;
	String strClosingValueDate;
	String strClosingCurrCode;
	String strClosingMiddleAmount;
	String strClosingAmount;
	String strClosingTranType;
	String strClosingAvalBalAmount;
	String strClosingAvalBalCurrCode;
	String strClosingAvalBalTranType;
	String strClosingAvalBalDate;
	int intOpType, intClType;
	public ArrayList<Type61Data> lstType61Data;
	String inwardOutwardType;
	String ackNackedFlag;
	String forwardAvailableBalance;
	
	public Type940()
	{
		completeMsgStr = "";
		intMessageNo = 0;
		strSenderBIC = "";
		strReceiverBIC = "";
		strActivityCode = "";
        strSenderCode = "";
        strTRFNo = "";
        strTranType = "";
        strValueDate = "";
        strCurrCode = "";
        strAmount = "";
        strMiddleAmount = "";
        strAccountID = "";
        strStatOrSeq = "";
        strRelRef = "";
        strClosingTranType = "";
        strClosingValueDate = "";
    	strClosingCurrCode = "";
    	strClosingMiddleAmount = "";
    	strClosingAmount = "";
    	strClosingAvalBalAmount = "";
    	strClosingAvalBalCurrCode = "";
    	strClosingAvalBalTranType = "";
    	strClosingAvalBalDate = "";
    	intOpType = -1;
    	intClType = -1;
    	inwardOutwardType = "";
    	ackNackedFlag = "";
    	forwardAvailableBalance = "";
        lstType61Data = new ArrayList<Type61Data>();
	}
	
	public String getForwardAvailableBalance() {
		return forwardAvailableBalance;
	}

	public void setForwardAvailableBalance(String forwardAvailableBalance) {
		this.forwardAvailableBalance = forwardAvailableBalance;
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
	public String getClosingAvalBalAmount() {
		return strClosingAvalBalAmount;
	}

	public void setClosingAvalBalAmount(String strClosingAvalBalAmount) {
		this.strClosingAvalBalAmount = strClosingAvalBalAmount;
	}

	public String getClosingAvalBalCurrCode() {
		return strClosingAvalBalCurrCode;
	}

	public void setClosingAvalBalCurrCode(String strClosingAvalBalCurrCode) {
		this.strClosingAvalBalCurrCode = strClosingAvalBalCurrCode;
	}

	public String getClosingAvalBalTranType() {
		return strClosingAvalBalTranType;
	}

	public void setClosingAvalBalTranType(String strClosingAvalBalTranType) {
		this.strClosingAvalBalTranType = strClosingAvalBalTranType;
	}

	public String getClosingAvalBalDate() {
		return strClosingAvalBalDate;
	}

	public void setClosingAvalBalDate(String strClosingAvalBalDate) {
		this.strClosingAvalBalDate = strClosingAvalBalDate;
	}

	public int getMessageNo() {
		return intMessageNo;
	}

	public void setMessageNo(int intMessageNo) {
		this.intMessageNo = intMessageNo;
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
	
	public String getTranType() {
		return strTranType;
	}

	public void setTranType(String strTranType) {
		this.strTranType = strTranType;
	}
	
	public String getValueDate() {
		return strValueDate;
	}

	public void setValueDate(String strValueDate) {
		this.strValueDate = strValueDate;
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
	
	public String getMiddleAmount() {
		return strMiddleAmount;
	}

	public void setMiddleAmount(String strMiddleAmount) {
		this.strMiddleAmount = strMiddleAmount;
	}
	
	public String getClosingMiddleAmount() {
		return strClosingMiddleAmount;
	}
	
	public void setClosingMiddleAmount(String strClosingMiddleAmount) {
		this.strClosingMiddleAmount = strClosingMiddleAmount;
	}
	
	public int getOpType()
	{
		return intOpType;
	}
	
	public void setOpType(int intOpType) {
		this.intOpType = intOpType;
	}
	
	public int getClType()
	{
		return intClType;
	}
	
	public void setClType(int intClType) {
		this.intClType = intClType;
	}	

	public String getAccountID() {
		return strAccountID;
	}

	public void setAccountID(String strAccountID) {
		this.strAccountID = strAccountID;
	}

	public String getStatOrSeq() {
		return strStatOrSeq;
	}

	public void setStatOrSeq(String strStatOrSeq) {
		this.strStatOrSeq = strStatOrSeq;
	}

	public String getRelRef() {
		return strRelRef;
	}

	public void setRelRef(String strRelRef) {
		this.strRelRef = strRelRef;
	}
	
	public String getClosingTranType() {
		return strClosingTranType;
	}

	public void setClosingTranType(String strClosingTranType) {
		this.strClosingTranType = strClosingTranType;
	}
	
	public String getClosingValueDate() {
		return strClosingValueDate;
	}

	public void setClosingValueDate(String strClosingValueDate) {
		this.strClosingValueDate = strClosingValueDate;
	}

	public String getClosingCurrCode() {
		return strClosingCurrCode;
	}

	public void setClosingCurrCode(String strClosingCurrCode) {
		this.strClosingCurrCode = strClosingCurrCode;
	}

	public String getClosingAmount() {
		return strClosingAmount;
	}

	public void setClosingAmount(String strClosingAmount) {
		this.strClosingAmount = strClosingAmount;
	}

	public ArrayList<Type61Data> getType61DataList() {
		return lstType61Data;
	}

	public void setType61DataList(ArrayList<Type61Data> lstType61Data) {
		this.lstType61Data = lstType61Data;
	}
	
	public String getCompleteMsgStr() {
		return completeMsgStr;
	}
	
	public void setCompleteMsgStr(String completeMsgStr) {
		this.completeMsgStr = completeMsgStr;
	}

}
