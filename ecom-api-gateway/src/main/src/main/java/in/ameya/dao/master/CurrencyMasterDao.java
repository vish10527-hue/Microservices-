package in.ameya.dao.master;



import java.sql.ResultSet;
import java.util.ArrayList;

import in.ameya.model.master.CurrencyMasterVo;

public interface CurrencyMasterDao {

	ArrayList<CurrencyMasterVo> getCurrencyMasterList(CurrencyMasterVo currencyMasterVo, String userId);

	String fetchExportCurrencyMasterCSV(CurrencyMasterVo currencyMasterVo, String userId);
	
	ResultSet fetchExportCurrencyMasterExcel(CurrencyMasterVo currencyMasterVo, String userId);

	CurrencyMasterVo getCurrencyMasterDetails(String selRec, String userId);

	CurrencyMasterVo manageCurrencyMaster(CurrencyMasterVo currencyMasterVo, String userId);
	
	CurrencyMasterVo checkCurrencyMaster(String selRec, String appRejRemark, String checkType, String userId);
}
