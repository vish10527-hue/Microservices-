package in.ameya.service.master;



import java.sql.ResultSet;
import java.util.ArrayList;

import in.ameya.model.master.UserMasterVo;



public interface UserMasterService {

	ArrayList<UserMasterVo> getUserMasterList(UserMasterVo userMasterVo, String userKey);

	String fetchExportUserMasterCSV(UserMasterVo userMasterVo, String userKey);
	
	ResultSet fetchExportUserMasterExcel(UserMasterVo userMasterVo, String userKey);
	
	UserMasterVo getUserMasterDetails(String userKey, String selRec);
	
	String manageUserMaster(UserMasterVo userMasterVo, String userKey);
	
	String checkUserMaster(String selRec, String appRejRemark, String checkType, String userKey);

}
