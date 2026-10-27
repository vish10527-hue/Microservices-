package in.ameya.model.master;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import in.ameya.model.Search;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CurrencySearch extends Search {

	private String currencyCode;
	private String currencyDescription;

	public List<CurrencyDetails> currencies;

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

	@Override
	public String toString() {
		return "CurrencySearch [currencyCode=" + currencyCode + ", currencyDescription=" + currencyDescription
				+ ", currencies=" + currencies + ", " + searchToString() + "]";
	}

}
