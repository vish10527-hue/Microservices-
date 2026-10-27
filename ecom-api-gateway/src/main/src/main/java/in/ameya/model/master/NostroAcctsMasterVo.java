package in.ameya.model.master;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public class NostroAcctsMasterVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nostroAcctsId;
	
	@NotNull(groups={NostroAcctsMasterAddModGroup.class}, message = "Nostro Account no. is required")
	@NotEmpty(groups={NostroAcctsMasterAddModGroup.class}, message = "Nostro Account no. is required")
	@Pattern(groups={NostroAcctsMasterAddModGroup.class}, regexp = "^[^!#$%*<>]*$", message = "Nostro Account no. should not contain this special characters: !@#$%*<>.")
	private String nostroAcctsNo;
	
	@NotNull(groups={NostroAcctsMasterAddModGroup.class}, message = "Mirror Account no. is required")
	@NotEmpty(groups={NostroAcctsMasterAddModGroup.class}, message = "Mirror Account no. is required")
	@Pattern(groups={NostroAcctsMasterAddModGroup.class}, regexp = "^[^!#$%*<>]*$", message = "Mirror Account no. should not contain this special characters: !@#$%*<>.")
	private String mirrorAcctNo;
	
	@NotNull(groups={NostroAcctsMasterAddModGroup.class}, message = "Nostro Name is required")
	@NotEmpty(groups={NostroAcctsMasterAddModGroup.class}, message = "Nostro Name is required")
	@Pattern(groups={NostroAcctsMasterAddModGroup.class}, regexp = "^[^!#$%*<>]*$", message = "Nostro Name should not contain this special characters: !@#$%*<>.")
	private String nostroName;
	
	@Pattern(groups={NostroAcctsMasterAddModGroup.class}, regexp = "^[^!#$%*<>]*$", message = "Bank Name should not contain this special characters: !@#$%*<>.")
	private String bankName;
	
	@Pattern(groups={NostroAcctsMasterAddModGroup.class}, regexp = "^[^!#$%*<>]*$", message = "BIC Code should not contain this special characters: !@#$%*<>.")
	private String bicCode;
	
	@Pattern(groups={NostroAcctsMasterAddModGroup.class}, regexp = "^[^!#$%*<>]*$", message = "Bank Address should not contain this special characters: !@#$%*<>.")
	private String bankAddrDetails;
	
	private String sightingCRDYN;
	
	@Pattern(groups={NostroAcctsMasterAddModGroup.class}, regexp = "^[^!#$%*<>]*$", message = "Pool Account should not contain this special characters: !@#$%*<>.")
	private String poolAccount;
	
	@Pattern(groups={NostroAcctsMasterAddModGroup.class}, regexp = "^[^!#$%*<>]*$", message = "Realization Account should not contain this special characters: !@#$%*<>.")
	private String realizationAcct;
	
	@Pattern(groups={NostroAcctsMasterAddModGroup.class}, regexp = "^[^!#$%*<>]*$", message = "Branch Code should not contain this special characters: !@#$%*<>.")
	private String branchCode;
	
	@Pattern(groups={NostroAcctsMasterAddModGroup.class}, regexp = "^[^!#$%*<>]*$", message = "Bank Code should not contain this special characters: !@#$%*<>.")
	private String bankCode;
	
	@NotNull(groups={NostroAcctsMasterAddModGroup.class}, message = "Status is required")
	@NotEmpty(groups={NostroAcctsMasterAddModGroup.class}, message = "Status is required")
	private String status;
	
	private String appRejRemark;
	
	@NotNull(groups={NostroAcctsMasterAddModGroup.class}, message = "Currency is required")
	@NotEmpty(groups={NostroAcctsMasterAddModGroup.class}, message = "Currency is required")
	@Pattern(groups={NostroAcctsMasterAddModGroup.class}, regexp = "^[^!#$%*<>]*$", message = "Currency should not contain this special characters: !@#$%*<>.")
	private String currencyCode;
	
	private String approveFlag;
	private String searchCriteria;
	private String editMode;
	
	private String refSerialNo;
	private String approvedBy;
	private String approvedDate;
	private String createdBy;
	private String createdDate;
	private String modifiedBy;
	private String modifiedDate;
	private String userId;
	
	private int pageNo;
	private int pageSize;
	private int recordCount;
	
	@NotNull(groups={NostroAcctsMasterAddModGroup.class}, message = "Tolerance Amount is required")
	@NotEmpty(groups={NostroAcctsMasterAddModGroup.class}, message = "Tolerance Amount is required")
	@Pattern(groups={NostroAcctsMasterAddModGroup.class}, regexp = "^[^!#$%*<>]*$", message = "Tolerance Amount should not contain this special characters: !@#$%*<>.")
	private String toleranceAmount;
	
	private String searchCriteriaTextArray;
	private String orderByColumn;
	private String orderAscDesc;
	private String appRejRemarks;
	
	private String errorCode;
	private String errorDesc;
	private String modelName;
	
	@Pattern(groups={NostroAcctsMasterSearchGroup.class}, regexp = "^[^!#$%*<>]*$", message = "Nostro Account no. should not contain this special characters: !@#$%*<>.")
	private String searchNostroAcctsNo;
	
	@Pattern(groups={NostroAcctsMasterSearchGroup.class}, regexp = "^[^!#$%*<>]*$", message = "Bank Code should not contain this special characters: !@#$%*<>.")
	private String searchBankCode;
	
	private String accOpeningDate;
	private String accClosingDate;
	
	
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public String getNostroAcctsCode() {
		return currencyCode;
	}
	public void setNostroAcctsCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getNostroAcctsId() {
		return nostroAcctsId;
	}
	public void setNostroAcctsId(String nostroAcctsId) {
		this.nostroAcctsId = nostroAcctsId;
	}
	public String getNostroAcctsNo() {
		return nostroAcctsNo;
	}
	public void setNostroAcctsNo(String nostroAcctsNo) {
		this.nostroAcctsNo = nostroAcctsNo;
	}
	public String getNostroName() {
		return nostroName;
	}
	public void setNostroName(String nostroName) {
		this.nostroName = nostroName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBicCode() {
		return bicCode;
	}
	public void setBicCode(String bicCode) {
		this.bicCode = bicCode;
	}
	public String getBankAddrDetails() {
		return bankAddrDetails;
	}
	public void setBankAddrDetails(String bankAddrDetails) {
		this.bankAddrDetails = bankAddrDetails;
	}
	public String getSightingCRDYN() {
		return sightingCRDYN;
	}
	public void setSightingCRDYN(String sightingCRDYN) {
		this.sightingCRDYN = sightingCRDYN;
	}
	public String getPoolAccount() {
		return poolAccount;
	}
	public void setPoolAccount(String poolAccount) {
		this.poolAccount = poolAccount;
	}
	public String getRealizationAcct() {
		return realizationAcct;
	}
	public void setRealizationAcct(String realizationAcct) {
		this.realizationAcct = realizationAcct;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAppRejRemark() {
		return appRejRemark;
	}
	public void setAppRejRemark(String appRejRemark) {
		this.appRejRemark = appRejRemark;
	}
	public String getApproveFlag() {
		return approveFlag;
	}
	public void setApproveFlag(String approveFlag) {
		this.approveFlag = approveFlag;
	}
	public String getSearchCriteria() {
		return searchCriteria;
	}
	public void setSearchCriteria(String searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
	public String getEditMode() {
		return editMode;
	}
	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}
	public String getRefSerialNo() {
		return refSerialNo;
	}
	public void setRefSerialNo(String refSerialNo) {
		this.refSerialNo = refSerialNo;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public String getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	/*
	 * public void reset(ActionMapping mapping, HttpServletRequest request) {
	 * nostroAcctsNo = null; bankCode = null; }
	 */
	
	public String getToleranceAmount() {
		return toleranceAmount;
	}
	public void setToleranceAmount(String toleranceAmount) {
		this.toleranceAmount = toleranceAmount;
	}
	public String getSearchCriteriaTextArray() {
		return searchCriteriaTextArray;
	}
	public void setSearchCriteriaTextArray(String searchCriteriaTextArray) {
		this.searchCriteriaTextArray = searchCriteriaTextArray;
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
	
	public String getAppRejRemarks() {
		return appRejRemarks;
	}
	public void setAppRejRemarks(String appRejRemarks) {
		this.appRejRemarks = appRejRemarks;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getSearchNostroAcctsNo() {
		return searchNostroAcctsNo;
	}
	public void setSearchNostroAcctsNo(String searchNostroAcctsNo) {
		this.searchNostroAcctsNo = searchNostroAcctsNo;
	}
	public String getSearchBankCode() {
		return searchBankCode;
	}
	public void setSearchBankCode(String searchBankCode) {
		this.searchBankCode = searchBankCode;
	}
	public String getMirrorAcctNo() {
		return mirrorAcctNo;
	}
	public void setMirrorAcctNo(String mirrorAcctNo) {
		this.mirrorAcctNo = mirrorAcctNo;
	}
	
	public String getAccOpeningDate() {
		return accOpeningDate;
	}
	public void setAccOpeningDate(String accOpeningDate) {
		this.accOpeningDate = accOpeningDate;
	}
	public String getAccClosingDate() {
		return accClosingDate;
	}
	public void setAccClosingDate(String accClosingDate) {
		this.accClosingDate = accClosingDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public interface NostroAcctsMasterAddModGroup{}	
	public interface NostroAcctsMasterSearchGroup{}	
	
	
}
