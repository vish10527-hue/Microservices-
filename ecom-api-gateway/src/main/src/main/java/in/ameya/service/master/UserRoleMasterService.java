package in.ameya.service.master;



import java.sql.ResultSet;
import java.util.ArrayList;

import in.ameya.model.master.UserRoleMasterVo;



public interface UserRoleMasterService {

	ArrayList<UserRoleMasterVo> getUserRoleMasterList(UserRoleMasterVo UserRoleMasterVo, String userKey);

	String fetchExportUserRoleMasterCSV(UserRoleMasterVo UserRoleMasterVo, String userKey);
	
	ResultSet fetchExportUserRoleMasterExcel(UserRoleMasterVo userRoleMasterVo, String userKey);
	
	UserRoleMasterVo getUserRoleMasterDetails(String userKey, String selRec);
	
	String manageUserRoleMaster(UserRoleMasterVo UserRoleMasterVo, String userKey);
	
	String checkUserRoleMaster(String selRec, String appRejRemark, String checkType, String userKey);

	

}
