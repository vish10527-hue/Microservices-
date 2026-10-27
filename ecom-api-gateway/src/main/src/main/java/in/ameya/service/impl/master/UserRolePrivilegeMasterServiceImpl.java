package in.ameya.service.impl.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.UserRolePrivilegeMasterDao;
import in.ameya.model.master.UserMasterVo;
import in.ameya.service.master.UserRolePrivilegeMasterService;

@Transactional(rollbackFor=RuntimeException.class)
@Service
public class UserRolePrivilegeMasterServiceImpl implements UserRolePrivilegeMasterService{

	@Autowired
	private UserRolePrivilegeMasterDao userRolePrivilegeMasterDao;
	
	@Override
	public ArrayList<UserMasterVo> fetchUserRolePrivilegeMasterList(UserMasterVo userMasterVo,
			String userKey) {		
		return userRolePrivilegeMasterDao.fetchUserRolePrivilegeMasterList(userMasterVo, userKey);
	}
	
	@Override
	public ArrayList<UserMasterVo> fetchUserRolePrivilegeMasterDetails(String userRoleId, String approveFlag, String userKey) {
		return userRolePrivilegeMasterDao.fetchUserRolePrivilegeMasterDetails(userRoleId, approveFlag, userKey);
	}

	@Override
	public String checkUserRolePrivilegeMaster(String selRec, String appRejRemark,
			String checkType, String userKey) {
		return userRolePrivilegeMasterDao.checkUserRolePrivilegeMaster(selRec, appRejRemark, checkType, userKey);
	}
	
	@Override
	public String assignUserRolePrivileges(UserMasterVo userMasterVo,
			String sbSrNo, String userKey) {
		return userRolePrivilegeMasterDao.assignUserRolePrivileges(userMasterVo, sbSrNo, userKey);
	}
}
