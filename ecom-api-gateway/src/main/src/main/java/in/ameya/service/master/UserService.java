package in.ameya.service.master;

import java.io.IOException;

import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.UserDetails;
import in.ameya.model.master.UserSearch;

public interface UserService {

	DBResponse getUserList(UserSearch userSearch);

	DBResponse addUser(UserDetails user, Integer loggedInKey);

	DBResponse updateUser(UserDetails user, Integer loggedInKey);

	DBResponse discardUser(UserDetails user, Integer loggedInKey);

	DBResponse verifyUserRecord(UserSearch userSearch, Integer loggedInKey);

	String exportUserCSV(UserSearch userSearch)  throws IOException;

	ExportResponse exportUserExcel(UserSearch userSearch)  throws IOException;

	UserDetails getUserDetailsById(Integer userKey, Integer userId);

}
