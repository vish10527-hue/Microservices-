package in.ameya.service.impl.master;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.UserDao;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.UserDetails;
import in.ameya.model.master.UserSearch;
import in.ameya.service.master.UserService;
import in.ameya.util.Constants;

@Service
@Transactional(transactionManager = "MainTransactionManager", rollbackFor=RuntimeException.class)
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserDao userDao;

	@Override
	public DBResponse getUserList(UserSearch userSearch) {
		return userDao.getUserList(userSearch);
	}

	@Override
	public DBResponse addUser(UserDetails user, Integer loggedInKey) {
		return userDao.manageUser(user, loggedInKey, Constants.RECORD_INSERT);
	}
	
	@Override
	public DBResponse updateUser(UserDetails user, Integer loggedInKey) {
		
		return userDao.manageUser(user, loggedInKey, Constants.RECORD_UPDATE);
	}
	
	@Override
	public DBResponse discardUser(UserDetails user, Integer loggedInKey) {
		return userDao.manageUser(user, loggedInKey, Constants.RECORD_DELETE);
	}

	@Override
	public DBResponse verifyUserRecord(UserSearch userSearch, Integer loggedInKey) {
		String userKeys = "";
		String remarks = "";
		String recordStatus;
		
		if(userSearch.users == null || userSearch.users.size() == 0) {
			return new DBResponse(-1, "No Records Selected");
		}
		
		for (UserDetails user : userSearch.users) {
			userKeys += user.getUserKey() + "|";
			remarks += user.getAppRejRemarks().replace("|", " ") + "|";
			
		}
		
		
		if(userSearch.getApprovedFlag() == 1) {
			recordStatus = Constants.RECORD_APPROVE;
		} else {
			recordStatus = Constants.RECORD_REJECT;
		}
		
		return userDao.verifyUserRecords(loggedInKey, userKeys, remarks, recordStatus);
	}
	
	@Override
	public String exportUserCSV(UserSearch userSearch)  throws IOException{
		return this.userDao.exportUserCSV(userSearch);
	}
	
	@Override
	public ExportResponse exportUserExcel(UserSearch userSearch)  throws IOException{
		return this.userDao.exportUserExcel(userSearch);
	}

	@Override
	public UserDetails getUserDetailsById(Integer userKey, Integer userId) {
		return this.userDao.getUserDetailsById(userKey, userId);
	}

}
