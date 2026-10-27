package in.ameya.model.master;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import in.ameya.model.Detail;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CurrencyDetails extends Detail {

	private Integer currencyKey;
	private String currencyCode;
	private String currencyDescription;
	private Integer status;
	private Integer refCurrencyKey;
	
	public Integer getCurrencyKey() {
		return currencyKey;
	}
	public void setCurrencyKey(Integer currencyKey) {
		this.currencyKey = currencyKey;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCurrencyDescription() {
		return currencyDescription;
	}
	public void setCurrencyDescription(String currencyDescription) {
		this.currencyDescription = currencyDescription;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getRefCurrencyKey() {
		return refCurrencyKey;
	}
	public void setRefCurrencyKey(Integer refCurrencyKey) {
		this.refCurrencyKey = refCurrencyKey;
	}
	@Override
	public String toString() {
		return "MasterCurrencyencyDetail [currencyKey=" + currencyKey + ", currencyCode=" + currencyCode + ", currencyDescription="
				+ currencyDescription + ", status=" + status + ", refCurrencyKey=" + refCurrencyKey + ", " + detailToString() + "]";
	}
	
	

}
