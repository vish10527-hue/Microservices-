package in.ameya.model.reports;

import java.math.BigDecimal;

public class NostroSummaryVo {

    private String nostroName;
    private String accountId;
    private String currCode;
    private BigDecimal openAmt;
    private BigDecimal creditSum;
    private Integer creditCnt;
    private BigDecimal debitSum;
    private Integer debitCnt;
    private BigDecimal debitCreditSum;
    private BigDecimal closeAmt;
    private BigDecimal balDiffSum;
    private Integer unconfirmDebitCnt;
    private BigDecimal unconfirmDebitSum;
    private Integer unconfirmCreditCnt;
    private BigDecimal unconfirmCreditSum;
    
	public String getNostroName() {
		return nostroName;
	}
	public void setNostroName(String nostroName) {
		this.nostroName = nostroName;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getCurrCode() {
		return currCode;
	}
	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}
	public BigDecimal getOpenAmt() {
		return openAmt;
	}
	public void setOpenAmt(BigDecimal openAmt) {
		this.openAmt = openAmt;
	}
	public BigDecimal getCreditSum() {
		return creditSum;
	}
	public void setCreditSum(BigDecimal creditSum) {
		this.creditSum = creditSum;
	}
	public Integer getCreditCnt() {
		return creditCnt;
	}
	public void setCreditCnt(Integer creditCnt) {
		this.creditCnt = creditCnt;
	}
	public BigDecimal getDebitSum() {
		return debitSum;
	}
	public void setDebitSum(BigDecimal debitSum) {
		this.debitSum = debitSum;
	}
	public Integer getDebitCnt() {
		return debitCnt;
	}
	public void setDebitCnt(Integer debitCnt) {
		this.debitCnt = debitCnt;
	}
	public BigDecimal getDebitCreditSum() {
		return debitCreditSum;
	}
	public void setDebitCreditSum(BigDecimal debitCreditSum) {
		this.debitCreditSum = debitCreditSum;
	}
	public BigDecimal getCloseAmt() {
		return closeAmt;
	}
	public void setCloseAmt(BigDecimal closeAmt) {
		this.closeAmt = closeAmt;
	}
	public BigDecimal getBalDiffSum() {
		return balDiffSum;
	}
	public void setBalDiffSum(BigDecimal balDiffSum) {
		this.balDiffSum = balDiffSum;
	}
	public Integer getUnconfirmDebitCnt() {
		return unconfirmDebitCnt;
	}
	public void setUnconfirmDebitCnt(Integer unconfirmDebitCnt) {
		this.unconfirmDebitCnt = unconfirmDebitCnt;
	}
	public BigDecimal getUnconfirmDebitSum() {
		return unconfirmDebitSum;
	}
	public void setUnconfirmDebitSum(BigDecimal unconfirmDebitSum) {
		this.unconfirmDebitSum = unconfirmDebitSum;
	}
	public Integer getUnconfirmCreditCnt() {
		return unconfirmCreditCnt;
	}
	public void setUnconfirmCreditCnt(Integer unconfirmCreditCnt) {
		this.unconfirmCreditCnt = unconfirmCreditCnt;
	}
	public BigDecimal getUnconfirmCreditSum() {
		return unconfirmCreditSum;
	}
	public void setUnconfirmCreditSum(BigDecimal unconfirmCreditSum) {
		this.unconfirmCreditSum = unconfirmCreditSum;
	}

}
