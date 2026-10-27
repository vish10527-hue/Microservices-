package in.ameya.model.master;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import in.ameya.model.Detail;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRoleDetails extends Detail {

	private Integer userRoleKey;
	private String userRoleCode;
	private String userRoleName;
	private Integer status;
	private Integer refUserRoleKey;
	private String privileges;

	public Integer getUserRoleKey() {
		return userRoleKey;
	}

	public void setUserRoleKey(Integer userRoleKey) {
		this.userRoleKey = userRoleKey;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getRefUserRoleKey() {
		return refUserRoleKey;
	}

	public void setRefUserRoleKey(Integer refUserRoleKey) {
		this.refUserRoleKey = refUserRoleKey;
	}

	public String getPrivileges() {
		return privileges;
	}

	public void setPrivileges(String privileges) {
		this.privileges = privileges;
	}

	@Override
	public String toString() {
		return "UserRoleDetails [userRoleKey=" + userRoleKey + ", userRoleCode=" + userRoleCode + ", userRoleName="
				+ userRoleName + ", status=" + status + ", refUserRoleKey=" + refUserRoleKey + ", privileges="
				+ privileges + ", " + detailToString() + "]";
	}

}
