package in.ameya.dao.master;



import java.sql.ResultSet;
import java.util.ArrayList;

import in.ameya.model.master.UserMasterVo;

public interface UserMasterDao {

	ArrayList<UserMasterVo> getUserMasterList(UserMasterVo userMasterVo, String userKey);

	String fetchExportUserMasterCSV(UserMasterVo userMasterVo, String userKey);
	
	ResultSet fetchExportUserMasterExcel(UserMasterVo userMasterVo, String userKey);

	UserMasterVo getUserMasterDetails(String selRec, String userKey);

	String manageUserMaster(UserMasterVo userMasterVo, String userKey);
	
	String checkUserMaster(String selRec, String appRejRemark, String checkType, String userKey);
	
}
