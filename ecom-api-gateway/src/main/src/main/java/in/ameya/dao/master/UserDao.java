package in.ameya.dao.master;

import java.io.IOException;

import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.UserDetails;
import in.ameya.model.master.UserSearch;

public interface UserDao {

	DBResponse getUserList(UserSearch userSearch);

	DBResponse manageUser(UserDetails user, Integer loggedInKey, String action);

	DBResponse verifyUserRecords(Integer loggedInKey, String userKeys, String remarks, String recordStatus);

	ExportResponse exportUserExcel(UserSearch userSearch)  throws IOException;;

	String exportUserCSV(UserSearch userSearch)  throws IOException;;

	UserDetails getUserDetailsById(Integer userKey, Integer userId);

}