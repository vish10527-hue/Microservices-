package in.ameya.model.master;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;



public class CurrencyMasterVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String currencyId;
	
	@NotNull(groups={CurrencyMasterAddModGroup.class}, message = "Currency Code is required")
	@NotEmpty(groups={CurrencyMasterAddModGroup.class}, message = "Currency Code is required")
	@Pattern(groups={CurrencyMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Currency Code should not contain this special characters: !@#$%*<>.")
	private String currencyCode;
	
	@NotNull(groups={CurrencyMasterAddModGroup.class}, message = "Currency Description is required")
	@NotEmpty(groups={CurrencyMasterAddModGroup.class}, message = "Currency Description is required")
	@Pattern(groups={CurrencyMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Currency Description should not contain this special characters: !@#$%*<>.")
	private String currencyDesc;
	
	private String currencyDate;
	private String multiplicationFactor;
	
	@NotNull(groups={CurrencyMasterAddModGroup.class}, message = "Status is required")
	@NotEmpty(groups={CurrencyMasterAddModGroup.class}, message = "Status is required")
	private String status;
	private String appRejRemark;
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
	private String branchCode;
	
	private String searchCriteriaTextArray;
	
	private int pageNo;
	private int pageSize;
	private int recordCount;
	private String orderByColumn;
	private String orderAscDesc;
	private String appRejRemarks;
	
	private String errorCode;
	private String errorDesc;
	private String modelName;
	
	@Pattern(groups={CurrencyMasterSearchGroup.class},regexp = "^[^!#$%*<>]*$", message = "Currency Code should not contain this special characters: !@#$%*<>.")
	private String searchCurrencyCode;
	
	@Pattern(groups={CurrencyMasterSearchGroup.class},regexp = "^[^!#$%*<>]*$", message = "Currency Description should not contain this special characters: !@#$%*<>.")
	private String searchCurrencyDesc;
	
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
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
	public String getApproveFlag() {
		return approveFlag;
	}
	public void setApproveFlag(String approveFlag) {
		this.approveFlag = approveFlag;
	}
	public String getEditMode() {
		return editMode;
	}
	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}
	public String getSearchCriteria() {
		return searchCriteria;
	}
	public void setSearchCriteria(String searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
	public String getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCurrencyDesc() {
		return currencyDesc;
	}
	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}
	public String getCurrencyDate() {
		return currencyDate;
	}
	public void setCurrencyDate(String currencyDate) {
		this.currencyDate = currencyDate;
	}
	public String getMultiplicationFactor() {
		return multiplicationFactor;
	}
	public void setMultiplicationFactor(String multiplicationFactor) {
		this.multiplicationFactor = multiplicationFactor;
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
	
	
	/*
	 * public void reset(ActionMapping mapping, HttpServletRequest request) {
	 * currencyCode = null; currencyDesc = null; }
	 */
	
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
	
	public String getSearchCurrencyCode() {
		return searchCurrencyCode;
	}
	public void setSearchCurrencyCode(String searchCurrencyCode) {
		this.searchCurrencyCode = searchCurrencyCode;
	}
	public String getSearchCurrencyDesc() {
		return searchCurrencyDesc;
	}
	public void setSearchCurrencyDesc(String searchCurrencyDesc) {
		this.searchCurrencyDesc = searchCurrencyDesc;
	}



	public interface CurrencyMasterAddModGroup{}	
	public interface CurrencyMasterSearchGroup{}	
}

