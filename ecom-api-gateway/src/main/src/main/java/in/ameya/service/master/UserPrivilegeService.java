package in.ameya.service.master;

import java.util.List;

import in.ameya.model.DBResponse;
import in.ameya.model.master.UserPrivilegeDetails;
import in.ameya.model.master.UserPrivilegeSearch;

public interface UserPrivilegeService {
	
	public DBResponse getUserPrivilegeList(UserPrivilegeSearch userPrivilegeSearch);

    public DBResponse addUserPrivilege(UserPrivilegeDetails userPrivilege, Integer loggedInKey);

    public DBResponse updateUserPrivilege(UserPrivilegeDetails userPrivilege, Integer loggedInKey);

    public DBResponse discardUserPrivilege(UserPrivilegeDetails userPrivilege, Integer loggedInKey);

    public DBResponse verifyUserPrivilegeRecord(UserPrivilegeSearch userPrivilegeSearch, Integer loggedInKey);

	public List<UserPrivilegeDetails> getUserPrivilegeDetailsById(String userRoleKey, Integer approvedFlg, Integer userId);

	public List<UserPrivilegeDetails> attachUserPrivilege( UserPrivilegeSearch userPrivilegeSearch, Integer userId);

}
