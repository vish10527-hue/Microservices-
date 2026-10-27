package in.ameya.model.master;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDetails {

	private Integer userKey; 
	private String loginId; 
	private String userName; 
	private Integer branchKey; 
	private String branchCode;
	private String branchDescription;
	private String emailId; 
	private String mobileNo; 
	private String userPassword; 
	private String passwordExpyDate; 
	private Integer status; 
	private Integer galUserFlag; 
	private Integer loginAttempts; 
	private Integer loginStatus; 
	private Integer usrLockStatus; 
	private Integer usrDelStatus; 
	private String clientMachineIp; 
	private String userSessionId; 
	private String browserSessionId; 
	private String lastLoginDate;
	private String deactiveTillDate; 
	private Integer createdBy; 
	private String createdByLoginId;
	private String createdOn; 
	private Integer modifiedBy;
	private String modifiedByLoginId; 
	private String modifiedOn;
	private Integer checkedBy; 
	private String checkedByLoginId;
	private String checkedOn;
	private String appRejRemarks; 
	private Integer approvedFlag; 
	private Integer refUserKey; 
	private String userRoleNames;
	private String userRoleKeys;
	
	
	public Integer getUserKey() {
		return userKey;
	}
	public void setUserKey(Integer userKey) {
		this.userKey = userKey;
	}
	public String getLoginId() {
		return loginId;
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
	public Integer getBranchKey() {
		return branchKey;
	}
	public void setBranchKey(Integer branchKey) {
		this.branchKey = branchKey;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getBranchDescription() {
		return branchDescription;
	}
	public void setBranchDescription(String branchDescription) {
		this.branchDescription = branchDescription;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getPasswordExpyDate() {
		return passwordExpyDate;
	}
	public void setPasswordExpyDate(String passwordExpyDate) {
		this.passwordExpyDate = passwordExpyDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getGalUserFlag() {
		return galUserFlag;
	}
	public void setGalUserFlag(Integer galUserFlag) {
		this.galUserFlag = galUserFlag;
	}
	public Integer getLoginAttempts() {
		return loginAttempts;
	}
	public void setLoginAttempts(Integer loginAttempts) {
		this.loginAttempts = loginAttempts;
	}
	public Integer getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(Integer loginStatus) {
		this.loginStatus = loginStatus;
	}
	public Integer getUsrLockStatus() {
		return usrLockStatus;
	}
	public void setUsrLockStatus(Integer usrLockStatus) {
		this.usrLockStatus = usrLockStatus;
	}
	public Integer getUsrDelStatus() {
		return usrDelStatus;
	}
	public void setUsrDelStatus(Integer usrDelStatus) {
		this.usrDelStatus = usrDelStatus;
	}
	public String getClientMachineIp() {
		return clientMachineIp;
	}
	public void setClientMachineIp(String clientMachineIp) {
		this.clientMachineIp = clientMachineIp;
	}
	public String getUserSessionId() {
		return userSessionId;
	}
	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}
	public String getBrowserSessionId() {
		return browserSessionId;
	}
	public void setBrowserSessionId(String browserSessionId) {
		this.browserSessionId = browserSessionId;
	}
	public String getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public String getDeactiveTillDate() {
		return deactiveTillDate;
	}
	public void setDeactiveTillDate(String deactiveTillDate) {
		this.deactiveTillDate = deactiveTillDate;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedByLoginId() {
		return createdByLoginId;
	}
	public void setCreatedByLoginId(String createdByLoginId) {
		this.createdByLoginId = createdByLoginId;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public Integer getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedByLoginId() {
		return modifiedByLoginId;
	}
	public void setModifiedByLoginId(String modifiedByLoginId) {
		this.modifiedByLoginId = modifiedByLoginId;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public Integer getCheckedBy() {
		return checkedBy;
	}
	public void setCheckedBy(Integer checkedBy) {
		this.checkedBy = checkedBy;
	}
	public String getCheckedByLoginId() {
		return checkedByLoginId;
	}
	public void setCheckedByLoginId(String checkedByLoginId) {
		this.checkedByLoginId = checkedByLoginId;
	}
	public String getCheckedOn() {
		return checkedOn;
	}
	public void setCheckedOn(String checkedOn) {
		this.checkedOn = checkedOn;
	}
	public String getAppRejRemarks() {
		return appRejRemarks;
	}
	public void setAppRejRemarks(String appRejRemarks) {
		this.appRejRemarks = appRejRemarks;
	}
	public Integer getApprovedFlag() {
		return approvedFlag;
	}
	public void setApprovedFlag(Integer approvedFlag) {
		this.approvedFlag = approvedFlag;
	}
	public Integer getRefUserKey() {
		return refUserKey;
	}
	public void setRefUserKey(Integer refUserKey) {
		this.refUserKey = refUserKey;
	}
	public String getUserRoleNames() {
		return userRoleNames;
	}
	public void setUserRoleNames(String userRoleNames) {
		this.userRoleNames = userRoleNames;
	}
	
	
	@Override
	public String toString() {
		return "UserDetails [userKey=" + userKey + ", loginId=" + loginId + ", userName=" + userName + ", branchKey="
				+ branchKey + ", branchCode=" + branchCode + ", branchDescription=" + branchDescription + ", emailId="
				+ emailId + ", mobileNo=" + mobileNo + ", userPassword=" + userPassword + ", passwordExpyDate="
				+ passwordExpyDate + ", status=" + status + ", galUserFlag=" + galUserFlag + ", loginAttempts="
				+ loginAttempts + ", loginStatus=" + loginStatus + ", usrLockStatus=" + usrLockStatus
				+ ", usrDelStatus=" + usrDelStatus + ", clientMachineIp=" + clientMachineIp + ", userSessionId="
				+ userSessionId + ", browserSessionId=" + browserSessionId + ", lastLoginDate=" + lastLoginDate
				+ ", deactiveTillDate=" + deactiveTillDate + ", createdBy=" + createdBy + ", createdByLoginId="
				+ createdByLoginId + ", createdOn=" + createdOn + ", modifiedBy=" + modifiedBy + ", modifiedByLoginId="
				+ modifiedByLoginId + ", modifiedOn=" + modifiedOn + ", checkedBy=" + checkedBy + ", checkedByLoginId="
				+ checkedByLoginId + ", checkedOn=" + checkedOn + ", appRejRemarks=" + appRejRemarks + ", approvedFlag="
				+ approvedFlag + ", refUserKey=" + refUserKey + ", userRoleNames=" + userRoleNames + "]";
	}
	public String getUserRoleKeys() {
		return userRoleKeys;
	}
	public void setUserRoleKeys(String userRoleKeys) {
		this.userRoleKeys = userRoleKeys;
	}
	
	
}
