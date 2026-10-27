package in.ameya.service.impl.master;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.CurrencyDao;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.CurrencyDetails;
import in.ameya.model.master.CurrencySearch;
import in.ameya.service.master.CurrencyService;
import in.ameya.util.Constants;

@Service
@Transactional(transactionManager = "MainTransactionManager", rollbackFor=RuntimeException.class)
public class CurrencyServiceImpl implements CurrencyService {
	
	@Autowired
	CurrencyDao currencyDao;

	@Override
	public DBResponse getCurrencyList(CurrencySearch currencySearch) {
		return currencyDao.getCurrencyList(currencySearch);
	}

	@Override
	public DBResponse addCurrency(CurrencyDetails currency, Integer loggedInKey) {
		return currencyDao.manageCurrency(currency, loggedInKey, Constants.RECORD_INSERT);
	}
	
	@Override
	public DBResponse updateCurrency(CurrencyDetails currency, Integer loggedInKey) {
		return currencyDao.manageCurrency(currency, loggedInKey, Constants.RECORD_UPDATE);
	}
	
	@Override
	public DBResponse discardCurrency(CurrencyDetails currency, Integer loggedInKey) {
		return currencyDao.manageCurrency(currency, loggedInKey, Constants.RECORD_DELETE);
	}

	@Override
	public DBResponse verifyCurrencyRecord(CurrencySearch currencySearch, Integer loggedInKey) {
		String currencyKeys = "";
		String remarks = "";
		String recordStatus;
		
		if(currencySearch.currencies == null || currencySearch.currencies.size() == 0) {
			return new DBResponse(-1, "No Records Selected");
		}
		
		for (CurrencyDetails currency : currencySearch.currencies) {
			currencyKeys += currency.getCurrencyKey() + "|";
			remarks += currency.getAppRejRemarks().replace("|", " ") + "|";
		}
		
		//currencyKeys = currencyKeys.substring(0, currencyKeys.length() - 1);
		//remarks = remarks.substring(0, remarks.length() - 1);
		
		if(currencySearch.getApprovedFlag() == 1) {
			recordStatus = Constants.RECORD_APPROVE;
		} else {
			recordStatus = Constants.RECORD_REJECT;
		}
		
		return currencyDao.verifyCurrencyRecords(loggedInKey, currencyKeys, remarks, recordStatus);
	}

	@Override
	public String  exportCurrencyCSV(CurrencySearch currencySearch) throws IOException {
		return currencyDao.exportCurrencyCSV(currencySearch);
	}
	
	@Override
	public ExportResponse exportCurrencyExcel(CurrencySearch currencySearch) throws IOException{
		return currencyDao.exportCurrencyExcel(currencySearch);
	}

	@Override
	public CurrencyDetails getCurrencyDetailsById(Integer currencyId, Integer userId) {
		return currencyDao.getCurrencyDetailsById(currencyId, userId);
	}
}
