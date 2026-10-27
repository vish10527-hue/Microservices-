package in.ameya.dao.master;

import java.sql.ResultSet;
import java.util.ArrayList;

import in.ameya.model.master.LookupMasterVo;


public interface LookupMasterDao {

	ArrayList<LookupMasterVo> getLookupMasterList(LookupMasterVo lookupMasterVo, String userKey);

	String fetchExportLookupMasterCSV(LookupMasterVo lookupMasterVo, String userKey);

	ResultSet fetchExportLookupMasterExcel(LookupMasterVo lookupMasterVo, String userKey);
	
	LookupMasterVo getLookupMasterDetails(String selRec, String userKey);

	LookupMasterVo manageLookupMaster(String manageType, LookupMasterVo lookupMasterVo, String userKey);

	LookupMasterVo checkLookupMaster(String selRec, String appRejRemark, String checkType, String userKey);	

}
