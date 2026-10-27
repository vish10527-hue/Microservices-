package in.ameya.model.master;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import in.ameya.model.Search;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BranchSearch extends Search {

	private String branchDescription;
	private String branchCode;

	public List<BranchDetails> branches;

	public String getBranchDescription() {
		return branchDescription;
	}

	public void setBranchDescription(String branchDescription) {
		this.branchDescription = branchDescription;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	@Override
	public String toString() {
		return "BranchSearch [branchDescription=" + branchDescription + ", branchCode=" + branchCode + ", branches="
				+ branches + ", " + searchToString() + "]";
	}

}
