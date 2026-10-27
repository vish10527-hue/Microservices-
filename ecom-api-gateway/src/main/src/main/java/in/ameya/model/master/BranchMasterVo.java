package in.ameya.model.master;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class BranchMasterVo implements Serializable{
	
	/**
	 * 
	 */
	    private static final long serialVersionUID = 1L;

		private String branchId;
		
		@NotNull(groups={BranchMasterAddModGroup.class}, message = "Sol Id is required")
		@NotEmpty(groups={BranchMasterAddModGroup.class}, message = "Sol Id is required")
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Sol Id should not contain this special characters: !@#$%*<>.")
		private String branchCode;
		
		@NotNull(groups={BranchMasterAddModGroup.class}, message = "Branch Description is required")
		@NotEmpty(groups={BranchMasterAddModGroup.class}, message = "Branch Description is required")
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Branch Description should not contain this special characters: !@#$%*<>.")
		private String branchDesc;	
		
		@NotNull(groups={BranchMasterAddModGroup.class}, message = "Status is required")
		@NotEmpty(groups={BranchMasterAddModGroup.class}, message = "Status is required")
		private String status;
		
		private String appRejRemark;
		private String approveFlag;
		private String searchCriteria;
		private String editMode;
		
		@NotNull(groups={BranchMasterAddModGroup.class}, message = "Branch Type is required")
		@NotEmpty(groups={BranchMasterAddModGroup.class}, message = "Branch Type is required")
		private String branchType;
		
		private String bcatBranchId;
		private String bcatBranchDesc;
		
		@NotNull(groups={BranchMasterAddModGroup.class}, message = "BIC Code is required")
		@NotEmpty(groups={BranchMasterAddModGroup.class}, message = "BIC Code is required")
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "BIC Code should not contain this special characters: !@#$%*<>.")
		private String bicCode;
		
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
		private Date auditDate;
		private String orderByColumn;
		private String orderAscDesc;
		
		/*@NotNull(groups={BranchMasterAddModGroup.class})
		@NotEmpty(groups={BranchMasterAddModGroup.class})*/
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Email Id should not contain this special characters: !@#$%*<>.")
		private String emailId;
		
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Branch Zone should not contain this special characters: !@#$%*<>.")
		private String branchZone;
		
		/*@NotNull(groups={BranchMasterAddModGroup.class})
		@NotEmpty(groups={BranchMasterAddModGroup.class})*/
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Branch Address should not contain this special characters: !@#$%*<>.")
		private String branchAddress;
		
		@NotNull(groups={BranchMasterAddModGroup.class}, message = "HO Approved is required")
		@NotEmpty(groups={BranchMasterAddModGroup.class}, message = "HO Approved is required")
		private String hOApproved;
		
		/*@NotNull(groups={BranchMasterAddModGroup.class})
		@NotEmpty(groups={BranchMasterAddModGroup.class})*/
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "AD Code should not contain this special characters: !@#$%*<>.")
		private String adCode;
		
		/*@NotNull(groups={BranchMasterAddModGroup.class})
		@NotEmpty(groups={BranchMasterAddModGroup.class})*/
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Name1 should not contain this special characters: !@#$%*<>.")
		private String name1;
		
		/*@NotNull(groups={BranchMasterAddModGroup.class})
		@NotEmpty(groups={BranchMasterAddModGroup.class})*/
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Job Family1 should not contain this special characters: !@#$%*<>.")
		private String jobFamily1;
		
		/*@NotNull(groups={BranchMasterAddModGroup.class})
		@NotEmpty(groups={BranchMasterAddModGroup.class})*/
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Mobile No.1 should not contain this special characters: !@#$%*<>.")
		private String mobNo1;
		
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Name2 should not contain this special characters: !@#$%*<>.")
		private String name2;
		
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Job Family2 should not contain this special characters: !@#$%*<>.")
		private String jobFamily2;
		
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Mobile No.2 should not contain this special characters: !@#$%*<>.")
		private String mobNo2;
		
		/*@NotNull(groups={BranchMasterAddModGroup.class})
		@NotEmpty(groups={BranchMasterAddModGroup.class})*/
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Landline should not contain this special characters: !@#$%*<>.")
		private String landLine;
		
		@Pattern(groups={BranchMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "Branch State should not contain this special characters: !@#$%*<>.")
		private String branchState;
		
		
		private String searchCriteriaTextArray;
		private String branchTypeTextArray;
		
		private HashMap<String, String> bCatBranchIdMap;
		private String bcatBranchIds;
		private String appRejRemarks;
		
		private String errorCode;
		private String errorDesc;
		private String modelName;
		
		@Pattern(groups={BranchMasterSearchGroup.class},regexp = "^[^!#$%*<>]*$", message = "Sol Id should not contain this special characters: !@#$%*<>.")
		private String searchBranchCode;
		
		private String searchBranchType;
		private String searchBcatBranchId;
		
		@Pattern(groups={BranchMasterSearchGroup.class},regexp = "^[^!#$%*<>]*$", message = "BIC Code should not contain this special characters: !@#$%*<>.")
		private String searchBicCode;
		
		@Pattern(groups={BranchMasterSearchGroup.class},regexp = "^[^!#$%*<>]*$", message = "Branch Description should not contain this special characters: !@#$%*<>.")
		private String searchBranchDesc;	
			
		
		public String getBranchState() {
			return branchState;
		}
		public void setBranchState(String branchState) {
			this.branchState = branchState;
		}
		public String getMobNo1() {
			return mobNo1;
		}
		public void setMobNo1(String mobNo1) {
			this.mobNo1 = mobNo1;
		}
		public String getAdCode() {
			return adCode;
		}
		public void setAdCode(String adCode) {
			this.adCode = adCode;
		}
		public String getName1() {
			return name1;
		}
		public void setName1(String name1) {
			this.name1 = name1;
		}
		public String getJobFamily1() {
			return jobFamily1;
		}
		public void setJobFamily1(String jobFamily1) {
			this.jobFamily1 = jobFamily1;
		}
		
		public String getName2() {
			return name2;
		}
		public void setName2(String name2) {
			this.name2 = name2;
		}
		public String getJobFamily2() {
			return jobFamily2;
		}
		public void setJobFamily2(String jobFamily2) {
			this.jobFamily2 = jobFamily2;
		}
		public String getMobNo2() {
			return mobNo2;
		}
		public void setMobNo2(String mobNo2) {
			this.mobNo2 = mobNo2;
		}
		public String getLandLine() {
			return landLine;
		}
		public void setLandLine(String landLine) {
			this.landLine = landLine;
		}
		public String gethOApproved() {
			return hOApproved;
		}
		public void sethOApproved(String hOApproved) {
			this.hOApproved = hOApproved;
		}
		public String getBranchAddress() {
			return branchAddress;
		}
		public void setBranchAddress(String branchAddress) {
			this.branchAddress = branchAddress;
		}
		public String getBranchZone() {
			return branchZone;
		}
		public void setBranchZone(String branchZone) {
			this.branchZone = branchZone;
		}
		public String getEmailId() {
			return emailId;
		}
		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}
		public int getRecordCount() {
			return recordCount;
		}
		public void setRecordCount(int recordCount) {
			this.recordCount = recordCount;
		}
		public String getBranchType() {
			return branchType;
		}
		public void setBranchType(String branchType) {
			this.branchType = branchType;
		}
		public String getBcatBranchId() {
			return bcatBranchId;
		}
		public void setBcatBranchId(String bcatBranchId) {
			this.bcatBranchId = bcatBranchId;
		}
		public Date getAuditDate() {
			return auditDate;
		}
		public void setAuditDate(Date auditDate) {
			this.auditDate = auditDate;
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
		public String getBranchId() {
			return branchId;
		}
		public void setBranchId(String branchId) {
			this.branchId = branchId;
		}
		public String getBranchCode() {
			return branchCode;
		}
		public void setBranchCode(String branchCode) {
			this.branchCode = branchCode;
		}
		public String getBranchDesc() {
			return branchDesc;
		}
		public void setBranchDesc(String branchDesc) {
			this.branchDesc = branchDesc;
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
		public String getBcatBranchDesc() {
			return bcatBranchDesc;
		}
		public void setBcatBranchDesc(String bcatBranchDesc) {
			this.bcatBranchDesc = bcatBranchDesc;
		}
		
		public String getBicCode() {
			return bicCode;
		}
		public void setBicCode(String bicCode) {
			this.bicCode = bicCode;
		}
		
		/*
		 * public void reset(ActionMapping mapping, HttpServletRequest request) {
		 * branchCode = null; branchDesc = null; bicCode = null; }
		 */
		
		public String getSearchCriteriaTextArray() {
			return searchCriteriaTextArray;
		}
		public void setSearchCriteriaTextArray(String searchCriteriaTextArray) {
			this.searchCriteriaTextArray = searchCriteriaTextArray;
		}
		public String getBranchTypeTextArray() {
			return branchTypeTextArray;
		}
		public void setBranchTypeTextArray(String branchTypeTextArray) {
			this.branchTypeTextArray = branchTypeTextArray;
		}
		public HashMap<String, String> getbCatBranchIdMap() {
			return bCatBranchIdMap;
		}
		public void setbCatBranchIdMap(HashMap<String, String> bCatBranchIdMap) {
			this.bCatBranchIdMap = bCatBranchIdMap;
		}
		public String getBcatBranchIds() {
			return bcatBranchIds;
		}
		public void setBcatBranchIds(String bcatBranchIds) {
			this.bcatBranchIds = bcatBranchIds;
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
		public String getSearchBranchCode() {
			return searchBranchCode;
		}
		public void setSearchBranchCode(String searchBranchCode) {
			this.searchBranchCode = searchBranchCode;
		}
		public String getSearchBranchType() {
			return searchBranchType;
		}
		public void setSearchBranchType(String searchBranchType) {
			this.searchBranchType = searchBranchType;
		}
		public String getSearchBcatBranchId() {
			return searchBcatBranchId;
		}
		public void setSearchBcatBranchId(String searchBcatBranchId) {
			this.searchBcatBranchId = searchBcatBranchId;
		}
		public String getSearchBicCode() {
			return searchBicCode;
		}
		public void setSearchBicCode(String searchBicCode) {
			this.searchBicCode = searchBicCode;
		}
		public String getSearchBranchDesc() {
			return searchBranchDesc;
		}
		public void setSearchBranchDesc(String searchBranchDesc) {
			this.searchBranchDesc = searchBranchDesc;
		}


		public interface BranchMasterAddModGroup{}	
		public interface BranchMasterSearchGroup{}
		
	}

		
	

