package in.ameya.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginDetails {
	
	private Integer userKey;
	
	@JsonProperty("login_id")
	private String loginId;
	
	@JsonProperty("user_name")
	private String userName;
	private String lastLoginDate;
	private Integer galUserFlag;
	
	@JsonProperty("user_session_id")
	private String userSessionId;
	private String branchCode;
	private String systemAlert;
	private String deviceInfo;
	private String deviceIpAddress;
	private String relID;
	private String token;
		
	public String getRelID() {
		return relID;
	}
	public void setRelID(String relID) {
		this.relID = relID;
	}
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
	public String getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public Integer getGalUserFlag() {
		return galUserFlag;
	}
	public void setGalUserFlag(Integer galUserFlag) {
		this.galUserFlag = galUserFlag;
	}
	public String getUserSessionId() {
		return userSessionId;
	}
	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getSystemAlert() {
		return systemAlert;
	}
	public void setSystemAlert(String systemAlert) {
		this.systemAlert = systemAlert;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public String getDeviceIpAddress() {
		return deviceIpAddress;
	}
	public void setDeviceIpAddress(String deviceIpAddress) {
		this.deviceIpAddress = deviceIpAddress;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	

}
