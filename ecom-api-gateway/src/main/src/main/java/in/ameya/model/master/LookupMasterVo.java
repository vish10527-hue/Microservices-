package in.ameya.model.master;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class LookupMasterVo implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private int pageNo;
	private int pageSize;
	private String userIdCheck;
	private String status;
	private String approvedFlag;
	private String appRejRemarks;
	private String createdBy;
	private String createdOn;
	private String modifiedBy;
	private String modifiedOn;
	private String reviewedBy;
	private String reviewedOn;
	private String isUnderProcess;
	private int recordCount;
	private String callBackKey;
	private String callBackName;
	private String chkBoxSelRecordsKey;
	private String chkBoxSelRecordsDesc;
	private String createdByKey;
	private String modifiedByKey;
	private String reviewedByKey;
	private int reservedFlag;
	
	@NotNull(groups={LookupMasterAddModGroup.class})
	@NotEmpty(groups={LookupMasterAddModGroup.class})
	private String lookupType;
	private String lookupTypeDesc;
	
	@Pattern(groups={LookupMasterAddModGroup.class},regexp = "^[^!$%*<>]*$", message = "should not contain this special characters: !@$%*<>.")
	private String lookupCode;
	
	@NotNull(groups={LookupMasterAddModGroup.class})
	@NotEmpty(groups={LookupMasterAddModGroup.class})
	@Pattern(groups={LookupMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "should not contain this special characters: !@#$%*<>.")
	private String lookupDesc;
	
	private String lookupKey;
	private String lookupTypeKey;
	private String lookupMasterRefKey;
	private String lookupParentKey;
	private String deviationCategory;
	
	private String searchStatus;
	private String searchLookupType;
	@Pattern(groups={LookupMasterSearchGroup.class}, regexp = "^[^!#$%*<>]*$", message = "should not contain this special characters: !#$%*<>.")
	private String searchLookupCode;
	@Pattern(groups={LookupMasterSearchGroup.class}, regexp = "^[^!#$%*<>]*$", message = "should not contain this special characters: !#$%*<>.")
	private String searchLookupDesc;
	private String searchLookupTypeKey;
	private String modelName;	
	private String searchCriteria;
	private String errorDesc;
	private String errorCode;
	private String appRejRemark;
	
	public String getLookupTypeKey() {
		return lookupTypeKey;
	}
	public void setLookupTypeKey(String lookupTypeKey) {
		this.lookupTypeKey = lookupTypeKey;
	}
	public String getSearchLookupTypeKey() {
		return searchLookupTypeKey;
	}
	public void setSearchLookupTypeKey(String searchLookupTypeKey) {
		this.searchLookupTypeKey = searchLookupTypeKey;
	}	
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public String getLookupMasterRefKey() {
		return lookupMasterRefKey;
	}
	public void setLookupMasterRefKey(String lookupMasterRefKey) {
		this.lookupMasterRefKey = lookupMasterRefKey;
	}
	public String getLookupParentKey() {
		return lookupParentKey;
	}
	public void setLookupParentKey(String lookupParentKey) {
		this.lookupParentKey = lookupParentKey;
	}
	public String getDeviationCategory() {
		return deviationCategory;
	}
	public void setDeviationCategory(String deviationCategory) {
		this.deviationCategory = deviationCategory;
	}
	public String getLookupKey() {
		return lookupKey;
	}
	public void setLookupKey(String lookupKey) {
		this.lookupKey = lookupKey;
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
	public String getUserIdCheck() {
		return userIdCheck;
	}
	public void setUserIdCheck(String userIdCheck) {
		this.userIdCheck = userIdCheck;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getApprovedFlag() {
		return approvedFlag;
	}
	public void setApprovedFlag(String approvedFlag) {
		this.approvedFlag = approvedFlag;
	}
	public String getAppRejRemarks() {
		return appRejRemarks;
	}
	public void setAppRejRemarks(String appRejRemarks) {
		this.appRejRemarks = appRejRemarks;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getReviewedBy() {
		return reviewedBy;
	}
	public void setReviewedBy(String reviewedBy) {
		this.reviewedBy = reviewedBy;
	}
	public String getReviewedOn() {
		return reviewedOn;
	}
	public void setReviewedOn(String reviewedOn) {
		this.reviewedOn = reviewedOn;
	}
	public String getIsUnderProcess() {
		return isUnderProcess;
	}
	public void setIsUnderProcess(String isUnderProcess) {
		this.isUnderProcess = isUnderProcess;
	}
	public String getLookupType() {
		return lookupType;
	}
	public void setLookupType(String lookupType) {
		this.lookupType = lookupType;
	}
	public String getLookupTypeDesc() {
		return lookupTypeDesc;
	}
	public void setLookupTypeDesc(String lookupTypeDesc) {
		this.lookupTypeDesc = lookupTypeDesc;
	}
	public String getLookupCode() {
		return lookupCode;
	}
	public void setLookupCode(String lookupCode) {
		this.lookupCode = lookupCode;
	}
	public String getLookupDesc() {
		return lookupDesc;
	}
	public void setLookupDesc(String lookupDesc) {
		this.lookupDesc = lookupDesc;
	}
	public String getSearchCriteria() {
		return searchCriteria;
	}
	public void setSearchCriteria(String searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
	public String getSearchStatus() {
		return searchStatus;
	}
	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}
	public String getSearchLookupType() {
		return searchLookupType;
	}
	public void setSearchLookupType(String searchLookupType) {
		this.searchLookupType = searchLookupType;
	}
	public String getSearchLookupCode() {
		return searchLookupCode;
	}
	public void setSearchLookupCode(String searchLookupCode) {
		this.searchLookupCode = searchLookupCode;
	}
	public String getSearchLookupDesc() {
		return searchLookupDesc;
	}
	public void setSearchLookupDesc(String searchLookupDesc) {
		this.searchLookupDesc = searchLookupDesc;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCallBackKey() {
		return callBackKey;
	}
	public void setCallBackKey(String callBackKey) {
		this.callBackKey = callBackKey;
	}
	public String getCallBackName() {
		return callBackName;
	}
	public void setCallBackName(String callBackName) {
		this.callBackName = callBackName;
	}
	public String getChkBoxSelRecordsKey() {
		return chkBoxSelRecordsKey;
	}
	public void setChkBoxSelRecordsKey(String chkBoxSelRecordsKey) {
		this.chkBoxSelRecordsKey = chkBoxSelRecordsKey;
	}
	public String getChkBoxSelRecordsDesc() {
		return chkBoxSelRecordsDesc;
	}
	public void setChkBoxSelRecordsDesc(String chkBoxSelRecordsDesc) {
		this.chkBoxSelRecordsDesc = chkBoxSelRecordsDesc;
	}
	public String getCreatedByKey() {
		return createdByKey;
	}
	public void setCreatedByKey(String createdByKey) {
		this.createdByKey = createdByKey;
	}
	public String getModifiedByKey() {
		return modifiedByKey;
	}
	public void setModifiedByKey(String modifiedByKey) {
		this.modifiedByKey = modifiedByKey;
	}
	public String getReviewedByKey() {
		return reviewedByKey;
	}
	public void setReviewedByKey(String reviewedByKey) {
		this.reviewedByKey = reviewedByKey;
	}
	
	public int getReservedFlag() {
		return reservedFlag;
	}
	public void setReservedFlag(int reservedFlag) {
		this.reservedFlag = reservedFlag;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getAppRejRemark() {
		return appRejRemark;
	}
	public void setAppRejRemark(String appRejRemark) {
		this.appRejRemark = appRejRemark;
	}


	/* ****************************Validation groups declaration********************************* */
	public interface LookupMasterSearchGroup{}
	public interface LookupMasterAddModGroup{}	
}
