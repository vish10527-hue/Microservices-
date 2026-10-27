package in.ameya.schedulers.vo.mtSeries;

public class Type999 {
	
	String completeMsgStr;
	String receiverBIC;
	String activityCode;
	String senderCode;
	String transRefNo;
	String relRefNo;
	String narrative;
	String inwardOutwardType;
	String ackNackedFlag;
	
	public Type999()
	{
		completeMsgStr = "";
		receiverBIC = "";
		activityCode = "";
		senderCode = "";
		transRefNo = "";
		relRefNo = "";
		narrative = "";
		inwardOutwardType = "";
		ackNackedFlag = "";
	}

	public String getReceiverBIC() {
		return receiverBIC;
	}

	public void setReceiverBIC(String receiverBIC) {
		this.receiverBIC = receiverBIC;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getSenderCode() {
		return senderCode;
	}

	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}

	public String getTransRefNo() {
		return transRefNo;
	}

	public void setTransRefNo(String transRefNo) {
		this.transRefNo = transRefNo;
	}

	public String getRelRefNo() {
		return relRefNo;
	}

	public void setRelRefNo(String relRefNo) {
		this.relRefNo = relRefNo;
	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}

	public String getInwardOutwardType() {
		return inwardOutwardType;
	}

	public void setInwardOutwardType(String inwardOutwardType) {
		this.inwardOutwardType = inwardOutwardType;
	}

	public String getAckNackedFlag() {
		return ackNackedFlag;
	}

	public void setAckNackedFlag(String ackNackedFlag) {
		this.ackNackedFlag = ackNackedFlag;
	}
	
	public String getCompleteMsgStr() {
		return completeMsgStr;
	}
	
	public void setCompleteMsgStr(String completeMsgStr) {
		this.completeMsgStr = completeMsgStr;
	}
	
}
