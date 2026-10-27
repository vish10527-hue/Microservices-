package in.ameya.schedulers.vo.fileSeries;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

public class FittCsvVo implements Serializable {
		
    private String productCode="";
    private String senderBic="";
    private String receiverBic="";
    private String bookDate="";
    private String senderReference="";
    private String relatedReference="";
    private String transactionTypeCode="";
    private String valueDate="";
    private String currencyCode="";    
    private String txnAmount="";  
    private String exchangeRate="";
    private String orderingCustomer="";
    private String beneficiaryCustomer="";
    private String uploadKey;
    private String branchReference;
    private String createdOn;
    

	public String getUploadKey() {
		return uploadKey;
	}

	public void setUploadKey(String uploadKey) {
		this.uploadKey = uploadKey;
	}

	public String getBranchReference() {
		return branchReference;
	}

	public void setBranchReference(String branchReference) {
		this.branchReference = branchReference;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public FittCsvVo() {
	}

	public FittCsvVo(String productCode, String senderBic, String receiverBic, String bookDate, String senderReference,
			String relatedReference, String transactionTypeCode, String valueDate, String currencyCode, String txnAmount,
			String exchangeRate, String orderingCustomer, String beneficiaryCustomer) {
		super();
		this.productCode = productCode;
		this.senderBic = senderBic;
		this.receiverBic = receiverBic;
		this.bookDate = bookDate;
		this.senderReference = senderReference;
		this.relatedReference = relatedReference;
		this.transactionTypeCode = transactionTypeCode;
		this.valueDate = valueDate;
		this.currencyCode = currencyCode;	
		this.txnAmount = txnAmount;		
		this.exchangeRate = exchangeRate;
		this.orderingCustomer = orderingCustomer;
		this.beneficiaryCustomer = beneficiaryCustomer;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSenderBic() {
		return senderBic;
	}

	public void setSenderBic(String senderBic) {
		this.senderBic = senderBic;
	}

	public String getReceiverBic() {
		return receiverBic;
	}

	public void setReceiverBic(String receiverBic) {
		this.receiverBic = receiverBic;
	}

	public String getBookDate() {
		return bookDate;
	}

	public void setBookDate(String bookDate) {
		this.bookDate = bookDate;
	}

	public String getSenderReference() {
		return senderReference;
	}

	public void setSenderReference(String senderReference) {
		this.senderReference = senderReference;
	}

	public String getRelatedReference() {
		return relatedReference;
	}

	public void setRelatedReference(String relatedReference) {
		this.relatedReference = relatedReference;
	}

	public String getTransactionTypeCode() {
		return transactionTypeCode;
	}

	public void setTransactionTypeCode(String transactionTypeCode) {
		this.transactionTypeCode = transactionTypeCode;
	}

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getOrderingCustomer() {
		return orderingCustomer;
	}

	public void setOrderingCustomer(String orderingCustomer) {
		this.orderingCustomer = orderingCustomer;
	}

	public String getBeneficiaryCustomer() {
		return beneficiaryCustomer;
	}

	public void setBeneficiaryCustomer(String beneficiaryCustomer) {
		this.beneficiaryCustomer = beneficiaryCustomer;
	}
}
