package in.ameya.model.master;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import in.ameya.model.Search;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LookupSearch extends Search {

	private Integer lookupType;
	private String lookupCode;
	private String lookupDescription;

	public List<LookupDetails> lookups;

	public Integer getLookupType() {
		return lookupType;
	}

	public void setLookupType(Integer lookupType) {
		this.lookupType = lookupType;
	}

	public String getLookupCode() {
		return lookupCode;
	}

	public void setLookupCode(String lookupCode) {
		this.lookupCode = lookupCode;
	}

	public String getLookupDescription() {
		return lookupDescription;
	}

	public void setLookupDescription(String lookupDescription) {
		this.lookupDescription = lookupDescription;
	}

	public List<LookupDetails> getLookups() {
		return lookups;
	}

	public void setLookups(List<LookupDetails> lookups) {
		this.lookups = lookups;
	}

	@Override
	public String toString() {
		return "LookupSearch [lookupType=" + lookupType + ", lookupCode=" + lookupCode + ", lookupDescription="
				+ lookupDescription + ", lookups=" + lookups + "]";
	}

	

}
