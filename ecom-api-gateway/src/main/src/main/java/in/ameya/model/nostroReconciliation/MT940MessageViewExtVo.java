package in.ameya.model.nostroReconciliation;

import java.io.Serializable;

public class MT940MessageViewExtVo implements Serializable{

	private String creditType;
	
	
	private String valueDate;
	private String entryDate; 
	private String debitCreditIndicator;
	private String currCode;
	private String msgAmount;
	private String transTypeIdentificationCode; 
	private String tranRefNo;
	private String referenceNo;
	
	
	public String getCreditType() {
		return creditType;
	}
	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}
	public String getValueDate() {
		return valueDate;
	}
	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public String getDebitCreditIndicator() {
		return debitCreditIndicator;
	}
	public void setDebitCreditIndicator(String debitCreditIndicator) {
		this.debitCreditIndicator = debitCreditIndicator;
	}
	public String getCurrCode() {
		return currCode;
	}
	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}
	public String getMsgAmount() {
		return msgAmount;
	}
	public void setMsgAmount(String msgAmount) {
		this.msgAmount = msgAmount;
	}
	public String getTransTypeIdentificationCode() {
		return transTypeIdentificationCode;
	}
	public void setTransTypeIdentificationCode(String transTypeIdentificationCode) {
		this.transTypeIdentificationCode = transTypeIdentificationCode;
	}
	public String getTranRefNo() {
		return tranRefNo;
	}
	public void setTranRefNo(String tranRefNo) {
		this.tranRefNo = tranRefNo;
	}
	public String getReferenceNo() {
		return referenceNo;
	}
	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}
	
	
	
	
}
