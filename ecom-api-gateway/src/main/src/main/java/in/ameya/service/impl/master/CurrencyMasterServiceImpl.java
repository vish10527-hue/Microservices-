package in.ameya.service.impl.master;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.CurrencyMasterDao;
import in.ameya.model.master.CurrencyMasterVo;
import in.ameya.service.master.CurrencyMasterService;

@Transactional(rollbackFor=RuntimeException.class)
@Service
public class CurrencyMasterServiceImpl implements CurrencyMasterService{

	@Autowired
	private CurrencyMasterDao currencyMasterDao;
	
	@Override
	public ArrayList<CurrencyMasterVo> getCurrencyMasterList(CurrencyMasterVo currencyMasterVo,
			String userId) {		
		return currencyMasterDao.getCurrencyMasterList(currencyMasterVo, userId);
	}

	@Override
	public String fetchExportCurrencyMasterCSV(CurrencyMasterVo currencyMasterVo,
			String userId) {
		return currencyMasterDao.fetchExportCurrencyMasterCSV(currencyMasterVo, userId);
	}
	
	@Override
	public ResultSet fetchExportCurrencyMasterExcel(CurrencyMasterVo currencyMasterVo, String userId) {
		return currencyMasterDao.fetchExportCurrencyMasterExcel(currencyMasterVo, userId);
	}
	
	@Override
	public CurrencyMasterVo getCurrencyMasterDetails(String selRec, String userId) {
		return currencyMasterDao.getCurrencyMasterDetails(selRec, userId);
	}

	@Override
	public CurrencyMasterVo manageCurrencyMaster(CurrencyMasterVo currencyMasterVo, String userId) {
		return currencyMasterDao.manageCurrencyMaster(currencyMasterVo, userId);
	}
	
	@Override
	public CurrencyMasterVo checkCurrencyMaster(String selRec, String appRejRemark,
			String checkType, String userId) {
		return currencyMasterDao.checkCurrencyMaster(selRec, appRejRemark, checkType, userId);
	}

	
}
