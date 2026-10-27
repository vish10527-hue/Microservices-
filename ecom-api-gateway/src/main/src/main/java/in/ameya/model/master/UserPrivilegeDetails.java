package in.ameya.model.master;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import in.ameya.model.Detail;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserPrivilegeDetails extends Detail {

	private String userRoleKey;
	private String privilegeKey;
	private Integer status;
	private Integer createdBy;
	private String createdOn;
	private Integer modifiedBy;
	private String modifiedOn;
	private Integer checkedBy;
	private String checkedOn;
	private String appRejRemarks;
	private String userRoleCode;
	private String userRoleName;
	private Integer approvedFlag;
	private String privilegeName;
	private Integer parentPrivilegeKey;
	private Integer parentKey;
	private Integer menuKey;

	private List<UserPrivilegeDetails> childPrivileges;

	public String getUserRoleKey() {
		return userRoleKey;
	}

	public void setUserRoleKey(String userRoleKey) {
		this.userRoleKey = userRoleKey;
	}

	public String getPrivilegeKey() {
		return privilegeKey;
	}

	public void setPrivilegeKey(String privilegeKey) {
		this.privilegeKey = privilegeKey;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public String getPrivilegeName() {
		return privilegeName;
	}

	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}

	public Integer getParentPrivilegeKey() {
		return parentPrivilegeKey;
	}

	public void setParentPrivilegeKey(Integer parentPrivilegeKey) {
		this.parentPrivilegeKey = parentPrivilegeKey;
	}

	public Integer getParentKey() {
		return parentKey;
	}

	public void setParentKey(Integer parentKey) {
		this.parentKey = parentKey;
	}

	public Integer getMenuKey() {
		return menuKey;
	}

	public void setMenuKey(Integer menuKey) {
		this.menuKey = menuKey;
	}

	@Override
	public String toString() {
		return "UserPrivilegeDetails [userRoleKey=" + userRoleKey + ", privilegeKey=" + privilegeKey + ", status="
				+ status + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", modifiedBy=" + modifiedBy
				+ ", modifiedOn=" + modifiedOn + ", checkedBy=" + checkedBy + ", checkedOn=" + checkedOn
				+ ", appRejRemarks=" + appRejRemarks + ", userRoleCode=" + userRoleCode + ", userRoleName="
				+ userRoleName + ", approvedFlag=" + approvedFlag + ", privilegeName=" + privilegeName
				+ ", parentPrivilegeKey=" + parentPrivilegeKey + ", parentKey=" + parentKey + ", menuKey=" + menuKey
				+ "]";
	}

	public List<UserPrivilegeDetails> getChildPrivileges() {
		return childPrivileges;
	}

	public void setChildPrivileges(List<UserPrivilegeDetails> childPrivileges) {
		this.childPrivileges = childPrivileges;
	}


}
