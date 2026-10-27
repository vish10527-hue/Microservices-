package in.ameya.service.impl.master;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.CountryMasterDao;
import in.ameya.model.master.CountryMasterVo;
import in.ameya.service.master.CountryMasterService;

@Transactional(rollbackFor=RuntimeException.class)
@Service
public class CountryMasterServiceImpl implements CountryMasterService{

	@Autowired
	private CountryMasterDao countryMasterDao;
	
	@Override
	public ArrayList<CountryMasterVo> getCountryMasterList(CountryMasterVo countryMasterVo,
			String userId) {		
		return countryMasterDao.getCountryMasterList(countryMasterVo, userId);
	}

	@Override
	public String fetchExportCountryMasterCSV(CountryMasterVo countryMasterVo,
			String userId) {
		return countryMasterDao.fetchExportCountryMasterCSV(countryMasterVo, userId);
	}
	
	@Override
	public ResultSet fetchExportCountryMasterExcel(CountryMasterVo countryMasterVo, String userId) {
		return countryMasterDao.fetchExportCountryMasterExcel(countryMasterVo, userId);
	}
	
	@Override
	public CountryMasterVo getCountryMasterDetails(String selRec, String userId) {
		return countryMasterDao.getCountryMasterDetails(selRec, userId);
	}

	@Override
	public CountryMasterVo manageCountryMaster(CountryMasterVo countryMasterVo, String userId) {
		return countryMasterDao.manageCountryMaster(countryMasterVo, userId);
	}
	
	@Override
	public CountryMasterVo checkCountryMaster(String selRec, String appRejRemark,
			String checkType, String userId) {
		return countryMasterDao.checkCountryMaster(selRec, appRejRemark, checkType, userId);
	}

	
}
