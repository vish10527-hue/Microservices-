package in.ameya.dao.master;

import java.io.IOException;

import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.UserRoleDetails;
import in.ameya.model.master.UserRoleSearch;

public interface UserRoleDao {

	DBResponse getUserRoleList(UserRoleSearch UserRoleSearch);

	DBResponse manageUserRole(UserRoleDetails userRole, Integer loggedInKey, String action);

	DBResponse verifyUserRoleRecords(Integer loggedInKey, String userRoleKeys, String remarks, String recordStatus);

	String exportUserRoleCSV(UserRoleSearch UserRoleSearch) throws IOException;

	ExportResponse exportUserRoleExcel(UserRoleSearch UserRoleSearch) throws IOException;

	UserRoleDetails getUserRoleDetailsById(Integer userRoleKey, Integer userId);

}
