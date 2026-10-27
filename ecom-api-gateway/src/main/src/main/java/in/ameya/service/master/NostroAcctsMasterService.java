package in.ameya.service.master;



import java.sql.ResultSet;
import java.util.ArrayList;

import in.ameya.model.master.NostroAcctsMasterVo;



public interface NostroAcctsMasterService {

	ArrayList<NostroAcctsMasterVo> getNostroAcctsMasterList(NostroAcctsMasterVo nostroAcctsMasterVo, String userId);

	String fetchExportNostroAcctsMasterCSV(NostroAcctsMasterVo nostroAcctsMasterVo, String userId);
	
	ResultSet fetchExportNostroAcctsMasterExcel(NostroAcctsMasterVo nostroAcctsMasterVo, String userId);
	
	NostroAcctsMasterVo getNostroAcctsMasterDetails(String userId, String selRec);
	
	NostroAcctsMasterVo manageNostroAcctsMaster(NostroAcctsMasterVo nostroAcctsMasterVo, String userId);
	
	NostroAcctsMasterVo checkNostroAcctsMaster(String selRec, String appRejRemark, String checkType, String userId);

	ArrayList<NostroAcctsMasterVo> getNostroMasterPopupList(NostroAcctsMasterVo nostroAcctsMasterVo, String userId);

}
