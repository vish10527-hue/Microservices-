package in.ameya.model.master;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import in.ameya.model.Detail;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LookupDetails extends Detail{

	private Integer lookupKey;
	private Integer lookupType;
	private String lookupCode;
	private String lookupDescription;
	private Integer reservedFlag;
	private Integer status;
	private Integer refLookupKey;
	private String lookupTypeDesc;
	
	public Integer getLookupKey() {
		return lookupKey;
	}
	public void setLookupKey(Integer lookupKey) {
		this.lookupKey = lookupKey;
	}
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
	public Integer getReservedFlag() {
		return reservedFlag;
	}
	public void setReservedFlag(Integer reservedFlag) {
		this.reservedFlag = reservedFlag;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "MasterLookupsDetail [lookupKey=" + lookupKey + ", lookupType=" + lookupType + ", lookupCode="
				+ lookupCode + ", lookupDescription=" + lookupDescription + ", reservedFlag=" + reservedFlag
				+ ", status=" + status + ", refLookupKey=" + refLookupKey + ", " + detailToString() + "]";
	}
	public Integer getRefLookupKey() {
		return refLookupKey;
	}
	public void setRefLookupKey(Integer refLookupKey) {
		this.refLookupKey = refLookupKey;
	}
	public String getLookupTypeDesc() {
		return lookupTypeDesc;
	}
	public void setLookupTypeDesc(String lookupTypeDesc) {
		this.lookupTypeDesc = lookupTypeDesc;
	}
	
	
	

}
