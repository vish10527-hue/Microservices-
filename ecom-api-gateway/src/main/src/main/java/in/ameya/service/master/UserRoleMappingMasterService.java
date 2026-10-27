package in.ameya.service.master;



import java.util.ArrayList;

import in.ameya.model.master.UserMasterVo;



public interface UserRoleMappingMasterService {

	ArrayList<UserMasterVo> fetchUserRoleMappingMasterList(UserMasterVo userMasterVo, String userKey);

	ArrayList<UserMasterVo> fetchUserRoleMappingMasterDetails(String userId, String approveFlag, String userKey);
	
	String checkUserRoleMappingMaster(String selRec, String appRejRemark, String checkType, String userKey);
	
	String assignUserRoleMappings(UserMasterVo userMasterVo, String sbSrNo, String userKey);

}
