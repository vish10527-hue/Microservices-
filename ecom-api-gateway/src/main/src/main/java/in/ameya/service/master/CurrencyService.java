package in.ameya.service.master;

import java.io.IOException;

import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.CurrencyDetails;
import in.ameya.model.master.CurrencySearch;

public interface CurrencyService {

	DBResponse getCurrencyList(CurrencySearch currencySearch);

	DBResponse addCurrency(CurrencyDetails currency, Integer loggedInKey);

	DBResponse updateCurrency(CurrencyDetails currency, Integer loggedInKey);

	DBResponse discardCurrency(CurrencyDetails currency, Integer loggedInKey);

	DBResponse verifyCurrencyRecord(CurrencySearch currencySearch, Integer loggedInKey);

	String exportCurrencyCSV(CurrencySearch currencySearch) throws IOException;

	ExportResponse exportCurrencyExcel(CurrencySearch currencySearch) throws IOException;

	CurrencyDetails getCurrencyDetailsById(Integer currencyId, Integer userId);

}
