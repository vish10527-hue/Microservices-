package in.ameya.schedulers.vo.fileSeries;

import java.io.Serializable;
import java.math.BigDecimal;

public class FottCsvVo implements Serializable {
	
	private String productCode="";
    private String senderBic="";
    private String receiverBic="";
    private String bookDate="";
    private String senderReference="";
    private String transactionTypeCode="";
    private String valueDate="";
    private String currencyCode="";
    private String transactionAmount="";
    private String instructedCurrencyCode="";
    private String instructedAmount;
    private String exchangeRate="";
    private String orderingCustomer2="";
    private String orderingInstitution="";
    private String orderingInstitution2="";
    private String branchReference="";
    private String receiver1="";
    private String accountWithInstitution="";
    private String beneficiaryCustomer="";  
    private String uploadKey;
    private String currCode;
    private String nostroDrcr;
    private String lcyBalDrcr;
    private String counterParty;
    private String nostroRefdate;
    private String usdBalDrcr;
    private String nostroRefdate1;
    private String createdOn;
    
    public FottCsvVo() {
		super();
	}

	public FottCsvVo(String productCode, String senderBic, String receiverBic, String bookDate, String senderReference,
			String transactionTypeCode, String valueDate, String currencyCode, String transactionAmount,
			String instructedCurrencyCode, String instructedAmount, String exchangeRate, String orderingCustomer2,
			String orderingInstitution, String orderingInstitution2, String branchReference, String receiver1,
			String accountWithInstitution, String beneficiaryCustomer) {
		super();
		this.productCode = productCode;
		this.senderBic = senderBic;
		this.receiverBic = receiverBic;
		this.bookDate = bookDate;
		this.senderReference = senderReference;
		this.transactionTypeCode = transactionTypeCode;
		this.valueDate = valueDate;
		this.currencyCode = currencyCode;
		this.transactionAmount = transactionAmount;
		this.instructedCurrencyCode = instructedCurrencyCode;
		this.instructedAmount = instructedAmount;
		this.exchangeRate = exchangeRate;
		this.orderingCustomer2 = orderingCustomer2;
		this.orderingInstitution = orderingInstitution;
		this.orderingInstitution2 = orderingInstitution2;
		this.branchReference = branchReference;
		this.receiver1 = receiver1;
		this.accountWithInstitution = accountWithInstitution;
		this.beneficiaryCustomer = beneficiaryCustomer;
	}

	
	public String getUploadKey() {
		return uploadKey;
	}

	public void setUploadKey(String uploadKey) {
		this.uploadKey = uploadKey;
	}

	public String getCurrCode() {
		return currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	public String getNostroDrcr() {
		return nostroDrcr;
	}

	public void setNostroDrcr(String nostroDrcr) {
		this.nostroDrcr = nostroDrcr;
	}

	public String getLcyBalDrcr() {
		return lcyBalDrcr;
	}

	public void setLcyBalDrcr(String lcyBalDrcr) {
		this.lcyBalDrcr = lcyBalDrcr;
	}

	public String getCounterParty() {
		return counterParty;
	}

	public void setCounterParty(String counterParty) {
		this.counterParty = counterParty;
	}

	public String getNostroRefdate() {
		return nostroRefdate;
	}

	public void setNostroRefdate(String nostroRefdate) {
		this.nostroRefdate = nostroRefdate;
	}

	public String getUsdBalDrcr() {
		return usdBalDrcr;
	}

	public void setUsdBalDrcr(String usdBalDrcr) {
		this.usdBalDrcr = usdBalDrcr;
	}

	public String getNostroRefdate1() {
		return nostroRefdate1;
	}

	public void setNostroRefdate1(String nostroRefdate1) {
		this.nostroRefdate1 = nostroRefdate1;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
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

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getInstructedCurrencyCode() {
		return instructedCurrencyCode;
	}

	public void setInstructedCurrencyCode(String instructedCurrencyCode) {
		this.instructedCurrencyCode = instructedCurrencyCode;
	}

	public String getInstructedAmount() {
		return instructedAmount;
	}

	public void setInstructedAmount(String instructedAmount) {
		this.instructedAmount = instructedAmount;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getOrderingCustomer2() {
		return orderingCustomer2;
	}

	public void setOrderingCustomer2(String orderingCustomer2) {
		this.orderingCustomer2 = orderingCustomer2;
	}

	public String getOrderingInstitution() {
		return orderingInstitution;
	}

	public void setOrderingInstitution(String orderingInstitution) {
		this.orderingInstitution = orderingInstitution;
	}

	public String getOrderingInstitution2() {
		return orderingInstitution2;
	}

	public void setOrderingInstitution2(String orderingInstitution2) {
		this.orderingInstitution2 = orderingInstitution2;
	}

	public String getBranchReference() {
		return branchReference;
	}

	public void setBranchReference(String branchReference) {
		this.branchReference = branchReference;
	}

	public String getReceiver1() {
		return receiver1;
	}

	public void setReceiver1(String receiver1) {
		this.receiver1 = receiver1;
	}

	public String getAccountWithInstitution() {
		return accountWithInstitution;
	}

	public void setAccountWithInstitution(String accountWithInstitution) {
		this.accountWithInstitution = accountWithInstitution;
	}

	public String getBeneficiaryCustomer() {
		return beneficiaryCustomer;
	}

	public void setBeneficiaryCustomer(String beneficiaryCustomer) {
		this.beneficiaryCustomer = beneficiaryCustomer;
	}
	
	public void setReceiver(String receiver1) {
		this.receiver1 = receiver1;
	}
	
	public String getReceiver() {
		return receiver1;
	}
}