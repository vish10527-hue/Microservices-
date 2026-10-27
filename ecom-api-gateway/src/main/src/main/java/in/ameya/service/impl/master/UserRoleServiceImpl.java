package in.ameya.service.impl.master;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.UserRoleDao;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.UserRoleDetails;
import in.ameya.model.master.UserRoleSearch;
import in.ameya.service.master.UserRoleService;
import in.ameya.util.Constants;

@Service
@Transactional(transactionManager = "MainTransactionManager", rollbackFor=RuntimeException.class)
public class UserRoleServiceImpl implements UserRoleService {
	
	@Autowired
	UserRoleDao userRoleDao;

	@Override
	public DBResponse getUserRoleList(UserRoleSearch userRoleSearch) {
		return userRoleDao.getUserRoleList(userRoleSearch);
	}

	@Override
	public DBResponse addUserRole(UserRoleDetails userRole, Integer loggedInKey) {
		return userRoleDao.manageUserRole(userRole, loggedInKey, Constants.RECORD_INSERT);
	}
	
	@Override
	public DBResponse updateUserRole(UserRoleDetails userRole, Integer loggedInKey) {
		return userRoleDao.manageUserRole(userRole, loggedInKey, Constants.RECORD_UPDATE);
	}
	
	@Override
	public DBResponse discardUserRole(UserRoleDetails userRole, Integer loggedInKey) {
		return userRoleDao.manageUserRole(userRole, loggedInKey, Constants.RECORD_DELETE);
	}

	@Override
	public DBResponse verifyUserRoleRecord(UserRoleSearch userRoleSearch, Integer loggedInKey) {
		String userRoleKeys = "";
		String remarks = "";
		String recordStatus;
		
		if(userRoleSearch.userRoles == null || userRoleSearch.userRoles.size() == 0) {
			return new DBResponse(-1, "No Records Selected");
		}
		
		for (UserRoleDetails userRole : userRoleSearch.userRoles) {
			userRoleKeys += userRole.getUserRoleKey() + "|";
			remarks += userRole.getAppRejRemarks().replace("|", " ") + "|";
		}
		
		//userRoleKeys = userRoleKeys.substring(0, userRoleKeys.length() - 1);
		//remarks = remarks.substring(0, remarks.length() - 1);
		
		if(userRoleSearch.getApprovedFlag() == 1) {
			recordStatus = Constants.RECORD_APPROVE;
		} else {
			recordStatus = Constants.RECORD_REJECT;
		}
		
		return userRoleDao.verifyUserRoleRecords(loggedInKey, userRoleKeys, remarks, recordStatus);
	}

	@Override
	public String exportUserRoleCSV(UserRoleSearch userRoleSearch) throws IOException  {
		return userRoleDao.exportUserRoleCSV(userRoleSearch);
	}
	
	@Override
	public ExportResponse exportUserRoleExcel(UserRoleSearch userRoleSearch) throws IOException{
		return userRoleDao.exportUserRoleExcel(userRoleSearch);
	}

	@Override
	public UserRoleDetails getUserRoleDetailsById(Integer userRoleKey, Integer userId) {
		return userRoleDao.getUserRoleDetailsById(userRoleKey, userId);
	}
}
