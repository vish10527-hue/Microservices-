package in.ameya.service.master;



import java.sql.ResultSet;
import java.util.ArrayList;

import in.ameya.model.master.CountryMasterVo;



public interface CountryMasterService {

	ArrayList<CountryMasterVo> getCountryMasterList(CountryMasterVo countryMasterVo, String userId);

	String fetchExportCountryMasterCSV(CountryMasterVo countryMasterVo, String userId);
	
	ResultSet fetchExportCountryMasterExcel(CountryMasterVo countryMasterVo, String userId);
	
	CountryMasterVo getCountryMasterDetails(String selRec,String userId);
	
	CountryMasterVo manageCountryMaster(CountryMasterVo countryMasterVo, String userId);
	
	CountryMasterVo checkCountryMaster(String selRec, String appRejRemark, String checkType, String userId);

	

}
