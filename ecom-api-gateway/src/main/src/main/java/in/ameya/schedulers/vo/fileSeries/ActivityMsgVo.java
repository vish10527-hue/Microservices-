package in.ameya.schedulers.vo.fileSeries;

import java.io.Serializable;

public class ActivityMsgVo implements Serializable{

	private int msgKey;
	private int activityKey;
	private String systemRemark;
	private String userRemark;
	private String activityOn;
	private String activityTime;
	private String createdOn;
	private String createdTime;
	private String matchedStatus;
	private String tranRefNo;
	private String msgAmount;
	private String activityDoneBy;
	public int getMsgKey() {
		return msgKey;
	}
	public void setMsgKey(int msgKey) {
		this.msgKey = msgKey;
	}
	public int getActivityKey() {
		return activityKey;
	}
	public void setActivityKey(int activityKey) {
		this.activityKey = activityKey;
	}
	public String getSystemRemark() {
		return systemRemark;
	}
	public void setSystemRemark(String systemRemark) {
		this.systemRemark = systemRemark;
	}
	public String getUserRemark() {
		return userRemark;
	}
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	public String getActivityOn() {
		return activityOn;
	}
	public void setActivityOn(String activityOn) {
		this.activityOn = activityOn;
	}
	public String getActivityTime() {
		return activityTime;
	}
	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getMatchedStatus() {
		return matchedStatus;
	}
	public void setMatchedStatus(String matchedStatus) {
		this.matchedStatus = matchedStatus;
	}
	public String getTranRefNo() {
		return tranRefNo;
	}
	public void setTranRefNo(String tranRefNo) {
		this.tranRefNo = tranRefNo;
	}
	public String getMsgAmount() {
		return msgAmount;
	}
	public void setMsgAmount(String msgAmount) {
		this.msgAmount = msgAmount;
	}
	public String getActivityDoneBy() {
		return activityDoneBy;
	}
	public void setActivityDoneBy(String activityDoneBy) {
		this.activityDoneBy = activityDoneBy;
	}
	public ActivityMsgVo() {
	}
	
	
}
