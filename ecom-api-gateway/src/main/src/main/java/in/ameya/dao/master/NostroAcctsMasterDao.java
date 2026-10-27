package in.ameya.dao.master;



import java.sql.ResultSet;
import java.util.ArrayList;

import in.ameya.model.master.NostroAcctsMasterVo;

public interface NostroAcctsMasterDao {

	ArrayList<NostroAcctsMasterVo> getNostroAcctsMasterList(NostroAcctsMasterVo nostroAcctsMasterVo, String userId);

	String fetchExportNostroAcctsMasterCSV(NostroAcctsMasterVo nostroAcctsMasterVo, String userId);
	
	ResultSet fetchExportNostroAcctsMasterExcel(NostroAcctsMasterVo nostroAcctsMasterVo, String userId);

	NostroAcctsMasterVo getNostroAcctsMasterDetails(String selRec, String userId);

	NostroAcctsMasterVo manageNostroAcctsMaster(NostroAcctsMasterVo nostroAcctsMasterVo, String userId);
	
	NostroAcctsMasterVo checkNostroAcctsMaster(String selRec, String appRejRemark, String checkType, String userId);

	ArrayList<NostroAcctsMasterVo> getNostroMasterPopupList(NostroAcctsMasterVo nostroAcctsMasterVo, String userId);
}
