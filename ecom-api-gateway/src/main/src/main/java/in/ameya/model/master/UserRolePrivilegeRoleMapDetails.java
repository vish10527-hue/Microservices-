package in.ameya.model.master;

import in.ameya.model.Detail;

public class UserRolePrivilegeRoleMapDetails extends Detail {

	private Integer userRoleKey;
	private Integer privilegeKey;
	private Integer status;

	public Integer getUserRoleKey() {
		return userRoleKey;
	}

	public void setUserRoleKey(Integer userRoleKey) {
		this.userRoleKey = userRoleKey;
	}

	public Integer getPrivilegeKey() {
		return privilegeKey;
	}

	public void setPrivilegeKey(Integer privilegeKey) {
		this.privilegeKey = privilegeKey;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserRolePrivilegeRoleMapDetails [userRoleKey=" + userRoleKey + ", privilegeKey=" + privilegeKey
				+ ", status=" + status + ", " + detailToString() + "]";
	}

}
