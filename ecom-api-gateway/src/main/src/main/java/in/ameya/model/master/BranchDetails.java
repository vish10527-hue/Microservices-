package in.ameya.model.master;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import in.ameya.model.Detail;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BranchDetails extends Detail {

	private Integer branchKey;
	private String branchCode;
	private String branchDescription;
	private Integer status;
	private Integer refBranchKey;
	
	public Integer getBranchKey() {
		return branchKey;
	}
	public void setBranchKey(Integer branchKey) {
		this.branchKey = branchKey;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getBranchDescription() {
		return branchDescription;
	}
	public void setBranchDescription(String branchDescription) {
		this.branchDescription = branchDescription;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getRefBranchKey() {
		return refBranchKey;
	}
	public void setRefBranchKey(Integer refBranchKey) {
		this.refBranchKey = refBranchKey;
	}
	@Override
	public String toString() {
		return "MasterBranchesDetail [branchKey=" + branchKey + ", branchCode=" + branchCode + ", branchDescription="
				+ branchDescription + ", status=" + status + ", refBranchKey=" + refBranchKey + ", " + detailToString() + "]";
	}
	
	

}
