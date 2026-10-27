package in.ameya.model;

public class Detail {

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

	protected String detailToString() {
		return "createdBy=" + createdBy + ", createdByLoginId=" + createdByLoginId + ", createdOn=" + createdOn
				+ ", modifiedBy=" + modifiedBy + ", modifiedByLoginId=" + modifiedByLoginId + ", modifiedOn="
				+ modifiedOn + ", checkedBy=" + checkedBy + ", checkedByLoginId=" + checkedByLoginId + ", checkedOn="
				+ checkedOn + ", appRejRemarks=" + appRejRemarks + ", approvedFlag=" + approvedFlag;
	}

}
