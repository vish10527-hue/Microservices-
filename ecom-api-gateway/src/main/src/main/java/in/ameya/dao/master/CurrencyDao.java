package in.ameya.dao.master;

import java.io.IOException;

import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.CurrencyDetails;
import in.ameya.model.master.CurrencySearch;

public interface CurrencyDao {

	DBResponse getCurrencyList(CurrencySearch CurrencySearch);

	DBResponse manageCurrency(CurrencyDetails currency, Integer loggedInKey, String action);

	DBResponse verifyCurrencyRecords(Integer loggedInKey, String currencyKeys, String remarks, String recordStatus);

	String exportCurrencyCSV(CurrencySearch currencySearch) throws IOException;

	ExportResponse exportCurrencyExcel(CurrencySearch currencySearch) throws IOException;

	CurrencyDetails getCurrencyDetailsById(Integer currencyId, Integer userId);

}
