package in.ameya.model.master;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import in.ameya.model.Search;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserSearch extends Search{

	private Integer loggedInKey;
	private Integer pageNo;
	private Integer pageSize;
	private String orderByColumn;
	private String orderAscDesc;
	private String loginId;
	private String userName;
	private Integer usrLockStatus;
	private Integer approvedFlag;
	private Integer status;
	
	public ArrayList<UserDetails> users;
	public Integer count;
	
	
	public Integer getLoggedInKey() {
		return loggedInKey;
	}
	public void setLoggedInKey(Integer loggedInKey) {
		this.loggedInKey = loggedInKey;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
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
	public Integer getUsrLockStatus() {
		return usrLockStatus;
	}
	public void setUsrLockStatus(Integer usrLockStatus) {
		this.usrLockStatus = usrLockStatus;
	}
	public Integer getApprovedFlag() {
		return approvedFlag;
	}
	public void setApprovedFlag(Integer approvedFlag) {
		this.approvedFlag = approvedFlag;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	@Override
	public String toString() {
		return "UserSearch [loggedInKey=" + loggedInKey + ", pageNo=" + pageNo + ", pageSize=" + pageSize
				+ ", orderByColumn=" + orderByColumn + ", orderAscDesc=" + orderAscDesc + ", loginId=" + loginId
				+ ", userName=" + userName + ", usrLockStatus=" + usrLockStatus + ", approvedFlag=" + approvedFlag
				+ ", status=" + status + ", users=" + users + ", count=" + count + "]";
	}
	
}
