package in.ameya.model.master;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import in.ameya.model.Search;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRoleSearch extends Search {

	private String userRoleCode;
	private String userRoleName;

	public List<UserRoleDetails> userRoles;

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

	@Override
	public String toString() {
		return "UserRoleSearch [userRoleCode=" + userRoleCode + ", userRoleName=" + userRoleName + ", userRoles="
				+ userRoles + ", " + searchToString() + "]";
	}

}
