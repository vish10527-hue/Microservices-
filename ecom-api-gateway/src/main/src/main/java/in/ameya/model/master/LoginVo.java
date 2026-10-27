package in.ameya.model.master;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


public class LoginVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(groups={LoginPageGroup.class}, message = "Login Id Can't Be Blank.")
	@Size(groups={LoginPageGroup.class}, min=1,max=20)
	private String loginId;
	
	@NotEmpty(groups={LoginPageGroup.class}, message = "Password Can't Be Blank.")
	private String password;
	private String newLoginId;
	private String newPassword;
	private String token;
	private String errorCode;
	private String errorDesc;
	private String userId;
	private String userName;
	private String lastLoginDate;
    private String userRoleId;
	private String userRoleName;
	private String userType;
	private String deviceInfo;
	private String clientMachineIp;
	private String basicInfo;
	private String timeStamp;
	private String publickey;
	private String branchDescription;
	private String branchCode;
	private String branchTreasuryUser;
	private String branchId;
	private String isGalUser;
	private String sessionId;
	private String systemAlert;
		
	
	public String getIsGalUser() {
		return isGalUser;
	}
	public void setIsGalUser(String isGalUser) {
		this.isGalUser = isGalUser;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSystemAlert() {
		return systemAlert;
	}
	public void setSystemAlert(String systemAlert) {
		this.systemAlert = systemAlert;
	}
	public String getBasicInfo() {
		return basicInfo;
	}
	public void setBasicInfo(String basicInfo) {
		this.basicInfo = basicInfo;
	}
	public String getDtToday() {
		return dtToday;
	}
	public void setDtToday(String dtToday) {
		this.dtToday = dtToday;
	}
	private String dtToday;
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public String getClientMachineIp() {
		return clientMachineIp;
	}
	public void setClientMachineIp(String clientMachineIp) {
		this.clientMachineIp = clientMachineIp;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPublickey() {
		return publickey;
	}
	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}
	public String getBranchDescription() {
		return branchDescription;
	}
	public void setBranchDescription(String branchDescription) {
		this.branchDescription = branchDescription;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getBranchTreasuryUser() {
		return branchTreasuryUser;
	}
	public void setBranchTreasuryUser(String branchTreasuryUser) {
		this.branchTreasuryUser = branchTreasuryUser;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	
	
	public String getNewLoginId() {
		return newLoginId;
	}
	public void setNewLoginId(String newLoginId) {
		this.newLoginId = newLoginId;
	}


	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}


	/* ****************************Validation groups declaration********************************* */
	public interface LoginPageGroup{}	
	
}
