package in.ameya.model.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;



public class UserMasterVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(groups={UserMasterAddModGroup.class})
	@NotEmpty(groups={UserMasterAddModGroup.class})
	@Pattern(groups={UserMasterAddModGroup.class},regexp = "^[^!#$%*<>]*$", message = "should not contain this special characters: !@#$%*<>.")
	private String loginId;
	
	@NotNull(groups={UserMasterAddModGroup.class})
	@NotEmpty(groups={UserMasterAddModGroup.class})
	@Pattern(groups={UserMasterAddModGroup.class}, regexp = "^[^!#$%*<>]*$", message = "should not contain this special characters: !@#$%*<>.")
	private String userName;
	
	private String userPassword;
	
	@NotNull(groups={UserMasterAddModGroup.class})
	@NotEmpty(groups={UserMasterAddModGroup.class})
	private String branchId;
	private String branchCode;
	
	@Pattern(groups={UserMasterAddModGroup.class}, regexp = "[0-9]*$", message = "should be a Number")
	private String phoneNo;
	
	@Pattern(groups={UserMasterAddModGroup.class}, regexp = "^[^!#$%*<>]*$", message = "should not contain this special characters: !#$%*<>.")
	private String emailId;
	
	@NotNull(groups={UserMasterAddModGroup.class})
	@NotEmpty(groups={UserMasterAddModGroup.class})
	private String status;
	private String appRejRemark;
	private String approveFlag;
	private String searchCriteria;
	private String editMode;
	private String lockStatus;
	private String deleteStatus;
	
	private String refSerialNo;
	private String approvedBy;
	private String approvedDate;
	private String createdBy;
	private String createdDate;
	private String modifiedBy;
	private String modifiedDate;
	private String userId;
	private String command;
	private String assignFlag;
	
	private String privilegeId; 
	private String privilegeName; 
	private String menuId; 
	private String parentPriviegeId; 
	private String parentId;
	private String deActivationDate;
	
	private int pageNo;
	private int pageSize;
	private String auditRecType;
	private Date auditDate;
	private int recordCount;

	private String orderByColumn;
	private String orderAscDesc;
	
	private ArrayList<UserMasterVo> branchIdList;
	
	/**User Role Mapping Form**/
	private String mapId;
	
	@NotNull(groups={UserMasterAddModGroup.class})
	@NotEmpty(groups={UserMasterAddModGroup.class})
	private String userRoleId; 
	
	
	private String userRoleCode; 		
	private String userRoleName;
	private String userType;
	private String attachedRoleId;
	
	@NotNull(groups={UserMasterAddModGroup.class})
	@NotEmpty(groups={UserMasterAddModGroup.class})
	private String userRoleProfileId;
	
	private String userRoleProfileDesc;
	private String appRejRemarks;
	/**End**/
	
	public String getLoginId() {
		return loginId;
	}
	public String getDeleteStatus() {
		return deleteStatus;
	}
	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	public String getLockStatus() {
		return lockStatus;
	}
	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getStatus() {
		return status;
	}
	
	public ArrayList<UserMasterVo> getBranchIdList() {
		return branchIdList;
	}
	public void setBranchIdList(ArrayList<UserMasterVo> branchIdList) {
		this.branchIdList = branchIdList;
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
	public String getPrivilegeId() {
		return privilegeId;
	}
	public void setPrivilegeId(String privilegeId) {
		this.privilegeId = privilegeId;
	}
	public String getPrivilegeName() {
		return privilegeName;
	}
	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getParentPriviegeId() {
		return parentPriviegeId;
	}
	public void setParentPriviegeId(String parentPriviegeId) {
		this.parentPriviegeId = parentPriviegeId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
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
	public String getMapId() {
		return mapId;
	}
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	public String getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getUserRoleCode() {
		return userRoleCode;
	}
	public void setUserRoleCode(String userRoleCode) {
		this.userRoleCode = userRoleCode;
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
	public String getAttachedRoleId() {
		return attachedRoleId;
	}
	public void setAttachedRoleId(String attachedRoleId) {
		this.attachedRoleId = attachedRoleId;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getAssignFlag() {
		return assignFlag;
	}
	public void setAssignFlag(String assignFlag) {
		this.assignFlag = assignFlag;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getAuditRecType() {
		return auditRecType;
	}
	public void setAuditRecType(String auditRecType) {
		this.auditRecType = auditRecType;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getDeActivationDate() {
		return deActivationDate;
	}
	public void setDeActivationDate(String deActivationDate) {
		this.deActivationDate = deActivationDate;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
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
	
	
	public String getAppRejRemarks() {
		return appRejRemarks;
	}
	public void setAppRejRemarks(String appRejRemarks) {
		this.appRejRemarks = appRejRemarks;
	}


	public interface UserMasterAddModGroup{}	
	public interface UserMasterSearchGroup{}
}
