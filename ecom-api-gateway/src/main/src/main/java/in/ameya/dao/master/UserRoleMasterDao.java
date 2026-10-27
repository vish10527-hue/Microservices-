package in.ameya.dao.master;



import java.sql.ResultSet;
import java.util.ArrayList;

import in.ameya.model.master.UserRoleMasterVo;

public interface UserRoleMasterDao {

	ArrayList<UserRoleMasterVo> getUserRoleMasterList(UserRoleMasterVo userRoleMasterVo, String userKey);

	String fetchExportUserRoleMasterCSV(UserRoleMasterVo userRoleMasterVo, String userKey);
	
	ResultSet fetchExportUserRoleMasterExcel(UserRoleMasterVo userRoleMasterVo, String userKey);

	UserRoleMasterVo getUserRoleMasterDetails(String selRec, String userKey);

	String manageUserRoleMaster(UserRoleMasterVo userRoleMasterVo, String userKey);
	
	String checkUserRoleMaster(String selRec, String appRejRemark, String checkType, String userKey);

	
}
