package in.ameya.model.transaction;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionSearch {
	
	private Integer count;
	private String messageKey;
	private String msgTypeKey;
	private String mxFlag;
	private String completeMTMessage;
	private String completeMXMessage;
	private String mxView;
	private String transRefNo;
	private String relatedRefNo;
	private String currency;
	private String valueDate;
	private String msgAmount;
	private String senderBic;
	private String receiverBic;
	private String inOutFlagDesc;
	private String mtMXType;
	private String iwrFlagDesc;

	private String uploadKey;
	private String msgCodeMT;
	private String msgCodeMX;
	private String fileName;
	private String status;
	private String noRecordsSaved;
	private String noRecordsRejected;
	private String noRecordsTotal;
	private String createdOn;
	private String field50;
	private String field59;
	private String lauStatusDesc;
	private String msgDesc;
    private String createdOnOrderBy;
    private String nostroName;
    private String tranRefNo;
    private String relRefNo;
    private String currKey;
    private String currCode;
    private String senderCode;
    private String matchedStatusDesc;
    private String utilisedAmount;
	/*
	 * private String benCusAccount; private String benCusName; private String
	 * benCusAddress; private String senderCode;
	 */

	// private String remittrName;

	public String getUploadKey() {
		return uploadKey;
	}

	public String getNostroName() {
		return nostroName;
	}

	public void setNostroName(String nostroName) {
		this.nostroName = nostroName;
	}

	public String getTranRefNo() {
		return tranRefNo;
	}

	public void setTranRefNo(String tranRefNo) {
		this.tranRefNo = tranRefNo;
	}

	public String getRelRefNo() {
		return relRefNo;
	}

	public void setRelRefNo(String relRefNo) {
		this.relRefNo = relRefNo;
	}

	public String getCurrKey() {
		return currKey;
	}

	public void setCurrKey(String currKey) {
		this.currKey = currKey;
	}

	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	public String getSenderCode() {
		return senderCode;
	}

	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}

	public String getMatchedStatusDesc() {
		return matchedStatusDesc;
	}

	public void setMatchedStatusDesc(String matchedStatusDesc) {
		this.matchedStatusDesc = matchedStatusDesc;
	}

	public String getUtilisedAmount() {
		return utilisedAmount;
	}

	public void setUtilisedAmount(String utilisedAmount) {
		this.utilisedAmount = utilisedAmount;
	}

	public void setUploadKey(String uploadKey) {
		this.uploadKey = uploadKey;
	}

	public String getMsgCodeMT() {
		return msgCodeMT;
	}

	public void setMsgCodeMT(String msgCodeMT) {
		this.msgCodeMT = msgCodeMT;
	}

	public String getMsgCodeMX() {
		return msgCodeMX;
	}

	public void setMsgCodeMX(String msgCodeMX) {
		this.msgCodeMX = msgCodeMX;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNoRecordsSaved() {
		return noRecordsSaved;
	}

	public void setNoRecordsSaved(String noRecordsSaved) {
		this.noRecordsSaved = noRecordsSaved;
	}

	public String getNoRecordsRejected() {
		return noRecordsRejected;
	}

	public void setNoRecordsRejected(String noRecordsRejected) {
		this.noRecordsRejected = noRecordsRejected;
	}

	public String getNoRecordsTotal() {
		return noRecordsTotal;
	}

	public void setNoRecordsTotal(String noRecordsTotal) {
		this.noRecordsTotal = noRecordsTotal;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getCompleteMTMessage() {
		return completeMTMessage;
	}

	public String getRelatedRefNo() {
		return relatedRefNo;
	}

	public void setRelatedRefNo(String relatedRefNo) {
		this.relatedRefNo = relatedRefNo;
	}

	public String getSenderBic() {
		return senderBic;
	}

	public void setSenderBic(String senderBic) {
		this.senderBic = senderBic;
	}

	public String getInOutFlagDesc() {
		return inOutFlagDesc;
	}

	public void setInOutFlagDesc(String inOutFlagDesc) {
		this.inOutFlagDesc = inOutFlagDesc;
	}

	public String getMtMXType() {
		return mtMXType;
	}

	public void setMtMXType(String mtMXType) {
		this.mtMXType = mtMXType;
	}

	public void setCompleteMTMessage(String completeMTMessage) {
		this.completeMTMessage = completeMTMessage;
	}

	public String getCompleteMXMessage() {
		return completeMXMessage;
	}

	public void setCompleteMXMessage(String completeMXMessage) {
		this.completeMXMessage = completeMXMessage;
	}

	public String getMxView() {
		return mxView;
	}

	public void setMxView(String mxView) {
		this.mxView = mxView;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

	public String getMsgAmount() {
		return msgAmount;
	}

	public void setMsgAmount(String msgAmount) {
		this.msgAmount = msgAmount;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getMsgTypeKey() {
		return msgTypeKey;
	}

	public void setMsgTypeKey(String msgTypeKey) {
		this.msgTypeKey = msgTypeKey;
	}

	public String getMxFlag() {
		return mxFlag;
	}

	public void setMxFlag(String mxFlag) {
		this.mxFlag = mxFlag;
	}

	public String getReceiverBic() {
		return receiverBic;
	}

	public void setReceiverBic(String receiverBic) {
		this.receiverBic = receiverBic;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getIwrFlagDesc() {
		return iwrFlagDesc;
	}

	public void setIwrFlagDesc(String iwrFlagDesc) {
		this.iwrFlagDesc = iwrFlagDesc;
	}

	public String getTransRefNo() {
		return transRefNo;
	}

	public void setTransRefNo(String transRefNo) {
		this.transRefNo = transRefNo;
	}

	public String getField50() {
		return field50;
	}

	public void setField50(String field50) {
		this.field50 = field50;
	}

	public String getField59() {
		return field59;
	}

	public void setField59(String field59) {
		this.field59 = field59;
	}

	public String getLauStatusDesc() {
		return lauStatusDesc;
	}

	public void setLauStatusDesc(String lauStatusDesc) {
		this.lauStatusDesc = lauStatusDesc;
	}

	public String getMsgDesc() {
		return msgDesc;
	}

	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}

	public String getCreatedOnOrderBy() {
		return createdOnOrderBy;
	}

	public void setCreatedOnOrderBy(String createdOnOrderBy) {
		this.createdOnOrderBy = createdOnOrderBy;
	}
	

}
