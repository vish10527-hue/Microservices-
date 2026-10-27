package in.ameya.service.master;



import java.util.ArrayList;

import in.ameya.model.master.UserMasterVo;



public interface UserRolePrivilegeMasterService {

	ArrayList<UserMasterVo> fetchUserRolePrivilegeMasterList(UserMasterVo userMasterVo, String userKey);

	ArrayList<UserMasterVo> fetchUserRolePrivilegeMasterDetails(String userRoleId, String approveFlag, String userKey);
	
	String checkUserRolePrivilegeMaster(String selRec, String appRejRemark, String checkType, String userKey);
	
	String assignUserRolePrivileges(UserMasterVo userMasterVo, String sbSrNo, String userKey);

}
