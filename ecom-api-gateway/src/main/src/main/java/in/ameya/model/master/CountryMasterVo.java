package in.ameya.model.master;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CountryMasterVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String countryId;
	
	@NotNull(groups={CountryMasterAddModGroup.class}, message = "Country Code is required")
	@NotEmpty(groups={CountryMasterAddModGroup.class}, message = "Country Code is required")
	@Pattern(groups={CountryMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Country Code should not contain this special characters: !@#$%*<>.")
	private String countryCode;
	
	@NotNull(groups={CountryMasterAddModGroup.class}, message = "Country Name is required")
	@NotEmpty(groups={CountryMasterAddModGroup.class}, message = "Country Name is required")
	@Pattern(groups={CountryMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Country Name should not contain this special characters: !@#$%*<>.")
	private String countryName; //Description
	
	@NotNull(groups={CountryMasterAddModGroup.class}, message = "Status is required")
	@NotEmpty(groups={CountryMasterAddModGroup.class}, message = "Status is required")
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
	
	private int pageNo;
	private int pageSize;
	private int recordCount;
	private String appRejRemarks;
	
	private String errorCode;
	private String errorDesc;
	private String modelName;
	
	@Pattern(groups={CountryMasterSearchGroup.class},regexp = "^[^!#$%*<>]*$", message = "Country Code should not contain this special characters: !@#$%*<>.")
	private String searchCountryCode;
	
	@Pattern(groups={CountryMasterSearchGroup.class},regexp = "^[^!#$%*<>]*$", message = "Country Name should not contain this special characters: !@#$%*<>.")
	private String searchCountryName;
	
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
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
	 * countryCode = null; countryName = null; }
	 */

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
	public String getSearchCountryCode() {
		return searchCountryCode;
	}
	public void setSearchCountryCode(String searchCountryCode) {
		this.searchCountryCode = searchCountryCode;
	}
	public String getSearchCountryName() {
		return searchCountryName;
	}
	public void setSearchCountryName(String searchCountryName) {
		this.searchCountryName = searchCountryName;
	}



	public interface CountryMasterAddModGroup{}	
	public interface CountryMasterSearchGroup{}	
}
