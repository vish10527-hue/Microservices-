package in.ameya.service.master;

import java.io.IOException;

import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.UserRoleDetails;
import in.ameya.model.master.UserRoleSearch;

public interface UserRoleService {

	public DBResponse getUserRoleList(UserRoleSearch UserRoleSearch);
	
	public DBResponse addUserRole(UserRoleDetails UserRole, Integer loggedInKey);
	
	public DBResponse updateUserRole(UserRoleDetails UserRole, Integer loggedInKey);
	
	public DBResponse discardUserRole(UserRoleDetails UserRole, Integer loggedInKey);

	public DBResponse verifyUserRoleRecord(UserRoleSearch UserRoleSearch, Integer loggedInKey);

	String exportUserRoleCSV(UserRoleSearch userRoleSearch) throws IOException;;

	ExportResponse exportUserRoleExcel(UserRoleSearch userRoleSearch)  throws IOException;;

	public UserRoleDetails getUserRoleDetailsById(Integer userRoleKey, Integer userId);
	
}
