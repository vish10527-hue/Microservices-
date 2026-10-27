package in.ameya.dao.master;

import java.util.List;

import in.ameya.model.DBResponse;
import in.ameya.model.master.UserPrivilegeDetails;
import in.ameya.model.master.UserPrivilegeSearch;

public interface UserPrivilegeDao {

	DBResponse verifyUserPrivilegeRecords(Integer loggedInKey, String userPrivilegeKeys, String remarks,String recordStatus);

	DBResponse manageUserPrivilege(UserPrivilegeDetails userPrivilege, Integer loggedInKey,String recordUpdate);

	DBResponse getUserPrivilegeList(UserPrivilegeSearch userPrivilegeSearch);

	List<UserPrivilegeDetails> getUserPrivilegeDetailsById(String userRoleKey, Integer approvedFlg, Integer userId);

	List<UserPrivilegeDetails> attachUserPrivilege( UserPrivilegeSearch userPrivilegeSearch, Integer userId);

}
