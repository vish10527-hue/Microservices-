package in.ameya.service.impl.master;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.UserPrivilegeDao;
import in.ameya.model.DBResponse;
import in.ameya.model.master.UserPrivilegeDetails;
import in.ameya.model.master.UserPrivilegeSearch;
import in.ameya.service.master.UserPrivilegeService;
import in.ameya.util.Constants;

@Service
@Transactional(transactionManager = "MainTransactionManager", rollbackFor=RuntimeException.class)
public class UserPrivilegeServiceImpl implements UserPrivilegeService {
	
	@Autowired
	UserPrivilegeDao userPrivilegeDao;

	@Override
	public DBResponse getUserPrivilegeList(UserPrivilegeSearch userPrivilegeSearch) {
	    return userPrivilegeDao.getUserPrivilegeList(userPrivilegeSearch);
	}

	@Override
	public DBResponse addUserPrivilege(UserPrivilegeDetails userPrivilege, Integer loggedInKey) {
	    String privilegeKeys = userPrivilege.getPrivilegeKey().replace(",", "|");

//	    for (UserPrivilegeDetails privilege : userPrivilege.getUserPrivileges()) {
//	        privilegeKeys += privilege.getPrivilegeKey() + "|"; 
//	    }
	    userPrivilege.setPrivilegeKey(privilegeKeys);

	    return userPrivilegeDao.manageUserPrivilege(userPrivilege, loggedInKey, Constants.RECORD_INSERT);
	}

	@Override
	public DBResponse updateUserPrivilege(UserPrivilegeDetails userPrivilege, Integer loggedInKey) {
		 String privilegeKeys = userPrivilege.getPrivilegeKey().replace(",", "|");

		 userPrivilege.setPrivilegeKey(privilegeKeys);

	    return userPrivilegeDao.manageUserPrivilege(userPrivilege, loggedInKey, Constants.RECORD_UPDATE);
	}

	@Override
	public DBResponse discardUserPrivilege(UserPrivilegeDetails userPrivilege, Integer loggedInKey) {
	    return userPrivilegeDao.manageUserPrivilege(userPrivilege, loggedInKey, Constants.RECORD_DELETE);
	}

	@Override
	public DBResponse verifyUserPrivilegeRecord(UserPrivilegeSearch userPrivilegeSearch, Integer loggedInKey) {
	    String userRoleKey = "";
	    String remarks = "";
	    String recordStatus;

	    if (userPrivilegeSearch.userPrivileges == null || userPrivilegeSearch.userPrivileges.size() == 0) {
	        return new DBResponse(-1, "No Records Selected");
	    }

	    for (UserPrivilegeDetails userPrivilege : userPrivilegeSearch.userPrivileges) {
	        userRoleKey += userPrivilege.getUserRoleKey() + "|";
	        remarks += userPrivilege.getAppRejRemarks().replace("|", " ") + "|";
	    }

	    if (userPrivilegeSearch.getApprovedFlag() == 1) {
	        recordStatus = Constants.RECORD_APPROVE;
	    } else {
	        recordStatus = Constants.RECORD_REJECT;
	    }

	    return userPrivilegeDao.verifyUserPrivilegeRecords(loggedInKey, userRoleKey, remarks, recordStatus);
	}

	@Override
	public List<UserPrivilegeDetails> getUserPrivilegeDetailsById(String userRoleKey, Integer approvedFlg, Integer userId) {
		return userPrivilegeDao.getUserPrivilegeDetailsById(userRoleKey, approvedFlg, userId);
	}

	@Override
	public List<UserPrivilegeDetails> attachUserPrivilege( UserPrivilegeSearch userPrivilegeSearch, Integer userId) {
		return userPrivilegeDao.attachUserPrivilege(userPrivilegeSearch, userId);
	}


}
