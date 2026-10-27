package in.ameya.model.transaction;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Transaction {
	
	private String uploadStatus;
	private String uploadKey;
	private String pageNo;
	private String pageSize;
//	private int recordCount;
	private String inOutFlag;
	private String msgTypeKey;
	private String currencyKey;
	private String currencyCode;
	private String amountRangeFrom;
	private String amountRangeTo;
	private String transactionReferenceNo;
	private String dateCriteria; // Uploade date or value date
	private String fromDate;
	private String toDate;
	private String loggedInKey;
	private String orderByColumn;
	private String orderAscDesc;
	private String msgType;
	private String fromValueDate;
	private String toValueDate;
	private String userId;
	private String loginUserKey;
	private String rowNumber;
    private String reasonForRejection;
    private String tranRefNo;
    private String valueDate;
    private String currCode;
    private String msgAmount;
    private String senderCode;
    private String recBic;
    private String completeMsg;
    private String formatedMsg;
    private String mtMxFlag;
    private String fileName;
    private String swiftBOStatus;
    private String remitName;
    private String benName;
    private String benAccNo;
    private String fileType;
    private String genSearch;
    private String msgIds; 
    private String status;
    
    private String internalRefNo;
    private String tranType;
    private String bankDetails;
    private String field86;
    private String accountId;
    private String entryDate;
    private String transTypeIdentificationCode;
    private String createdOn;
    public String nostroKey;
    public String matchedStatus;
    public String fromAmount;
    public String toAmount;
    
	public ArrayList<TransactionSearch> transactionSearch;
	public Integer count;
	public String getUploadStatus() {
		return uploadStatus;
	}
	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	public String getUploadKey() {
		return uploadKey;
	}
	public void setUploadKey(String uploadKey) {
		this.uploadKey = uploadKey;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getInOutFlag() {
		return inOutFlag;
	}
	public void setInOutFlag(String inOutFlag) {
		this.inOutFlag = inOutFlag;
	}
	public String getMsgTypeKey() {
		return msgTypeKey;
	}
	public void setMsgTypeKey(String msgTypeKey) {
		this.msgTypeKey = msgTypeKey;
	}
	public String getCurrencyKey() {
		return currencyKey;
	}
	public void setCurrencyKey(String currencyKey) {
		this.currencyKey = currencyKey;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getAmountRangeFrom() {
		return amountRangeFrom;
	}
	public void setAmountRangeFrom(String amountRangeFrom) {
		this.amountRangeFrom = amountRangeFrom;
	}
	public String getAmountRangeTo() {
		return amountRangeTo;
	}
	public void setAmountRangeTo(String amountRangeTo) {
		this.amountRangeTo = amountRangeTo;
	}
	public String getTransactionReferenceNo() {
		return transactionReferenceNo;
	}
	public void setTransactionReferenceNo(String transactionReferenceNo) {
		this.transactionReferenceNo = transactionReferenceNo;
	}
	public String getDateCriteria() {
		return dateCriteria;
	}
	public void setDateCriteria(String dateCriteria) {
		this.dateCriteria = dateCriteria;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getLoggedInKey() {
		return loggedInKey;
	}
	public void setLoggedInKey(String loggedInKey) {
		this.loggedInKey = loggedInKey;
	}
	public String getOrderByColumn() {
		return orderByColumn;
	}
	public void setOrderByColumn(String orderByColumn) {
		this.orderByColumn = orderByColumn;
	}
	public String getOrderAscDesc() {
		return orderAscDesc;
	}
	public void setOrderAscDesc(String orderAscDesc) {
		this.orderAscDesc = orderAscDesc;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getFromValueDate() {
		return fromValueDate;
	}
	public void setFromValueDate(String fromValueDate) {
		this.fromValueDate = fromValueDate;
	}
	public String getToValueDate() {
		return toValueDate;
	}
	public void setToValueDate(String toValueDate) {
		this.toValueDate = toValueDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginUserKey() {
		return loginUserKey;
	}
	public void setLoginUserKey(String loginUserKey) {
		this.loginUserKey = loginUserKey;
	}
	public String getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}
	public String getReasonForRejection() {
		return reasonForRejection;
	}
	public void setReasonForRejection(String reasonForRejection) {
		this.reasonForRejection = reasonForRejection;
	}
	public String getTranRefNo() {
		return tranRefNo;
	}
	public void setTranRefNo(String tranRefNo) {
		this.tranRefNo = tranRefNo;
	}
	public String getValueDate() {
		return valueDate;
	}
	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
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
	public String getSenderCode() {
		return senderCode;
	}
	public void setSenderCode(String senderCode) {
		this.senderCode = senderCode;
	}
	public String getRecBic() {
		return recBic;
	}
	public void setRecBic(String recBic) {
		this.recBic = recBic;
	}
	public String getCompleteMsg() {
		return completeMsg;
	}
	public void setCompleteMsg(String completeMsg) {
		this.completeMsg = completeMsg;
	}
	public String getFormatedMsg() {
		return formatedMsg;
	}
	public void setFormatedMsg(String formatedMsg) {
		this.formatedMsg = formatedMsg;
	}
	public String getMtMxFlag() {
		return mtMxFlag;
	}
	public void setMtMxFlag(String mtMxFlag) {
		this.mtMxFlag = mtMxFlag;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSwiftBOStatus() {
		return swiftBOStatus;
	}
	public void setSwiftBOStatus(String swiftBOStatus) {
		this.swiftBOStatus = swiftBOStatus;
	}
	public String getRemitName() {
		return remitName;
	}
	public void setRemitName(String remitName) {
		this.remitName = remitName;
	}
	public String getBenName() {
		return benName;
	}
	public void setBenName(String benName) {
		this.benName = benName;
	}
	public String getBenAccNo() {
		return benAccNo;
	}
	public void setBenAccNo(String benAccNo) {
		this.benAccNo = benAccNo;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getGenSearch() {
		return genSearch;
	}
	public void setGenSearch(String genSearch) {
		this.genSearch = genSearch;
	}
	public String getMsgIds() {
		return msgIds;
	}
	public void setMsgIds(String msgIds) {
		this.msgIds = msgIds;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInternalRefNo() {
		return internalRefNo;
	}
	public void setInternalRefNo(String internalRefNo) {
		this.internalRefNo = internalRefNo;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public String getBankDetails() {
		return bankDetails;
	}
	public void setBankDetails(String bankDetails) {
		this.bankDetails = bankDetails;
	}
	public String getField86() {
		return field86;
	}
	public void setField86(String field86) {
		this.field86 = field86;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public String getTransTypeIdentificationCode() {
		return transTypeIdentificationCode;
	}
	public void setTransTypeIdentificationCode(String transTypeIdentificationCode) {
		this.transTypeIdentificationCode = transTypeIdentificationCode;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getNostroKey() {
		return nostroKey;
	}
	public void setNostroKey(String nostroKey) {
		this.nostroKey = nostroKey;
	}
	public String getMatchedStatus() {
		return matchedStatus;
	}
	public void setMatchedStatus(String matchedStatus) {
		this.matchedStatus = matchedStatus;
	}
	public String getFromAmount() {
		return fromAmount;
	}
	public void setFromAmount(String fromAmount) {
		this.fromAmount = fromAmount;
	}
	public String getToAmount() {
		return toAmount;
	}
	public void setToAmount(String toAmount) {
		this.toAmount = toAmount;
	}
	public ArrayList<TransactionSearch> getTransactionSearch() {
		return transactionSearch;
	}
	public void setTransactionSearch(ArrayList<TransactionSearch> transactionSearch) {
		this.transactionSearch = transactionSearch;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Transaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	

	
}