package in.ameya.model.master;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import in.ameya.model.Search;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserPrivilegeSearch extends Search {

	private Integer loggedInKey;
	private Integer pageNo;
	private Integer pageSize;
	private String orderByColumn;
	private String orderAscDesc;
	private String userRoleCode;
	private String userRoleName;
	private Integer approvedFlag;
	private String userRoleKey;

	public ArrayList<UserPrivilegeDetails> userPrivileges;
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

	public Integer getApprovedFlag() {
		return approvedFlag;
	}

	public void setApprovedFlag(Integer approvedFlag) {
		this.approvedFlag = approvedFlag;
	}


	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public ArrayList<UserPrivilegeDetails> getUserPrivileges() {
		return userPrivileges;
	}

	public void setUserPrivileges(ArrayList<UserPrivilegeDetails> userPrivileges) {
		this.userPrivileges = userPrivileges;
	}

	public String getUserRoleKey() {
		return userRoleKey;
	}

	public void setUserRoleKey(String userRoleKey) {
		this.userRoleKey = userRoleKey;
	}

	@Override
	public String toString() {
		return "UserPrivilegeSearch [loggedInKey=" + loggedInKey + ", pageNo=" + pageNo + ", pageSize=" + pageSize
				+ ", orderByColumn=" + orderByColumn + ", orderAscDesc=" + orderAscDesc + ", userRoleCode="
				+ userRoleCode + ", userRoleName=" + userRoleName + ", approvedFlag=" + approvedFlag + ", userRoleKey="
				+ userRoleKey + ", userPrivileges=" + userPrivileges + ", count=" + count + "]";
	}

}
