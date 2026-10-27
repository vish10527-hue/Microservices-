package in.ameya.model.master;

import java.io.Serializable;
import java.util.HashMap;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public class UserRoleMasterVo implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	@NotNull(groups={UserRoleMasterAddModGroup.class})
	@NotEmpty(groups={UserRoleMasterAddModGroup.class})
	@Pattern(groups={UserRoleMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "should not contain this special characters: !@#$%*<>.")
	private String userRoleCode;
	private String userRoleId;
	
	@NotNull(groups={UserRoleMasterAddModGroup.class})
	@NotEmpty(groups={UserRoleMasterAddModGroup.class})
	@Pattern(groups={UserRoleMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "should not contain this special characters: !@#$%*<>.")
	private String userRoleName;
	
	@NotNull(groups={UserRoleMasterAddModGroup.class})
	@NotEmpty(groups={UserRoleMasterAddModGroup.class})
	private String userType;
	
	private String userTypeDesc;
	
	@NotNull(groups={UserRoleMasterAddModGroup.class})
	@NotEmpty(groups={UserRoleMasterAddModGroup.class})
	private String userRoleProfileId;
	
	private String userRoleProfileDesc;
	
	private String attachedRoleId;
	
	@NotNull(groups={UserRoleMasterAddModGroup.class})
	@NotEmpty(groups={UserRoleMasterAddModGroup.class})
	private String status;
	
	private String appRejRemark;
	private String approveFlag;
	private String searchCriteria;
	
	private String approvedBy;
	private String approvedDate;
	private String createdBy;
	private String createdDate;
	private String modifiedBy;
	private String modifiedDate;
	private String userId;
	private String branchCode;
	
	private int pageNo;
	private int pageSize;
	private int recordCount;
	private String orderByColumn;
	private String orderAscDesc;
	
	private HashMap<String, String> userTypeMap;
	private HashMap<String, String> userRoleProfileMap;
	private String userTypeIds;
	private String userRoleProfileIds;
	private String searchCriteriaTextArray;
	private String AppRejRemarks;
	private String command;
	
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
	public String getUserRoleCode() {
		return userRoleCode;
	}
	public void setUserRoleCode(String userRoleCode) {
		this.userRoleCode = userRoleCode;
	}	
	public String getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getUserRoleName() {
		return userRoleName;
	}
	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserTypeDesc() {
		return userTypeDesc;
	}
	public void setUserTypeDesc(String userTypeDesc) {
		this.userTypeDesc = userTypeDesc;
	}
	public String getAttachedRoleId() {
		return attachedRoleId;
	}
	public void setAttachedRoleId(String attachedRoleId) {
		this.attachedRoleId = attachedRoleId;
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
	
	public String getUserRoleProfileId() {
		return userRoleProfileId;
	}
	public void setUserRoleProfileId(String userRoleProfileId) {
		this.userRoleProfileId = userRoleProfileId;
	}
	public String getUserRoleProfileDesc() {
		return userRoleProfileDesc;
	}
	public void setUserRoleProfileDesc(String userRoleProfileDesc) {
		this.userRoleProfileDesc = userRoleProfileDesc;
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
	public HashMap<String, String> getUserTypeMap() {
		return userTypeMap;
	}
	public void setUserTypeMap(HashMap<String, String> userTypeMap) {
		this.userTypeMap = userTypeMap;
	}
	public HashMap<String, String> getUserRoleProfileMap() {
		return userRoleProfileMap;
	}
	public void setUserRoleProfileMap(HashMap<String, String> userRoleProfileMap) {
		this.userRoleProfileMap = userRoleProfileMap;
	}
	public String getUserTypeIds() {
		return userTypeIds;
	}
	public void setUserTypeIds(String userTypeIds) {
		this.userTypeIds = userTypeIds;
	}
	public String getUserRoleProfileIds() {
		return userRoleProfileIds;
	}
	public void setUserRoleProfileIds(String userRoleProfileIds) {
		this.userRoleProfileIds = userRoleProfileIds;
	}
	public String getSearchCriteriaTextArray() {
		return searchCriteriaTextArray;
	}
	public void setSearchCriteriaTextArray(String searchCriteriaTextArray) {
		this.searchCriteriaTextArray = searchCriteriaTextArray;
	}
	
	public String getAppRejRemarks() {
		return AppRejRemarks;
	}
	public void setAppRejRemarks(String appRejRemarks) {
		AppRejRemarks = appRejRemarks;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}


	public interface UserRoleMasterAddModGroup{}	
	public interface UserRoleMasterSearchGroup{}
}
