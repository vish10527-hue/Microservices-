package in.ameya.schedulers.vo.fileSeries;

import java.io.Serializable;
import java.math.BigDecimal;

public class OtherTxnsCsvVo implements Serializable {
	    
	    private String transactionRefNo="";
	    private String transactionDate="";
	    private String accountNo="";
	    private String currencyCode="";
	    private String drCrFlag="";
	    private String fycAmount="";
	    private String exchangeRate="";
	    private String lycAmount="";
	    private String module="";
	    private String branchReference="";
	    private String uploadKey="";
	    private String createdOn="";

		public OtherTxnsCsvVo() {
			super();
		}

		
		public OtherTxnsCsvVo(String transactionRefNo, String transactionDate, String accountNo, String currencyCode,
				String drCrFlag, String fycAmount, String exchangeRate, String lycAmount, String module, String branchReference) {
			super();
			this.transactionRefNo = transactionRefNo;
			this.transactionDate = transactionDate;
			this.accountNo = accountNo;
			this.currencyCode = currencyCode;
			this.drCrFlag = drCrFlag;
			this.fycAmount = fycAmount;
			this.exchangeRate = exchangeRate;
			this.lycAmount = lycAmount;
			this.module = module;
			this.branchReference=branchReference;
		}
		
		public String getAccountNo() {
			return accountNo;
		}

		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}

		public String getDrCrFlag() {
			return drCrFlag;
		}

		public void setDrCrFlag(String drCrFlag) {
			this.drCrFlag = drCrFlag;
		}

		public String getFycAmount() {
			return fycAmount;
		}

		public void setFycAmount(String fycAmount) {
			this.fycAmount = fycAmount;
		}

		public String getLycAmount() {
			return lycAmount;
		}

		public void setLycAmount(String lycAmount) {
			this.lycAmount = lycAmount;
		}

		public String getTransactionRefNo() {
			return transactionRefNo;
		}

		public void setTransactionRefNo(String transactionRefNo) {
			this.transactionRefNo = transactionRefNo;
		}

		public String getTransactionDate() {
			return transactionDate;
		}

		public void setTransactionDate(String transactionDate) {
			this.transactionDate = transactionDate;
		}

		public String getCurrencyCode() {
			return currencyCode;
		}

		public void setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}

		public String getExchangeRate() {
			return exchangeRate;
		}

		public void setExchangeRate(String exchangeRate) {
			this.exchangeRate = exchangeRate;
		}

		public String getModule() {
			return module;
		}

		public void setModule(String module) {
			this.module = module;
		}

		public String getBranchReference() {
			return branchReference;
		}

		public void setBranchReference(String branchReference) {
			this.branchReference = branchReference;
		}


		public String getUploadKey() {
			return uploadKey;
		}


		public void setUploadKey(String uploadKey) {
			this.uploadKey = uploadKey;
		}


		public String getCreatedOn() {
			return createdOn;
		}


		public void setCreatedOn(String createdOn) {
			this.createdOn = createdOn;
		}
		
}
