package in.ameya.service.master;

import java.sql.ResultSet;
import java.util.ArrayList;

import in.ameya.model.master.LookupMasterVo;

public interface LookupMasterService {

	ArrayList<LookupMasterVo> getLookupMasterList(LookupMasterVo lookupMasterVo,
			String userKey);

	String fetchExportLookupMasterCSV(LookupMasterVo lookupMasterVo, String userKey);

	ResultSet fetchExportLookupMasterExcel(LookupMasterVo lookupMasterVo,
			String userKey);

	LookupMasterVo getLookupMasterDetails(String userKey, String selRec);

	LookupMasterVo manageLookupMaster(String manageType, LookupMasterVo lookupMasterVo, String userKey);

	LookupMasterVo checkLookupMaster(String selRec, String appRejRemark,
			String checkType, String userKey);

}
