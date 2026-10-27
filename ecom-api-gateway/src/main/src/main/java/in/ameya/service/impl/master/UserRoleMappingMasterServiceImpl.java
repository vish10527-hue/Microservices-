package in.ameya.service.impl.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.UserRoleMappingMasterDao;
import in.ameya.model.master.UserMasterVo;
import in.ameya.service.master.UserRoleMappingMasterService;

@Transactional(rollbackFor=RuntimeException.class)
@Service
public class UserRoleMappingMasterServiceImpl implements UserRoleMappingMasterService{

	@Autowired
	private UserRoleMappingMasterDao userRoleMappingMasterDao;
	
	@Override
	public ArrayList<UserMasterVo> fetchUserRoleMappingMasterList(UserMasterVo userMasterVo,
			String userKey) {		
		return userRoleMappingMasterDao.fetchUserRoleMappingMasterList(userMasterVo, userKey);
	}
	
	@Override
	public ArrayList<UserMasterVo> fetchUserRoleMappingMasterDetails(String userId, String approveFlag, String userKey) {
		return userRoleMappingMasterDao.fetchUserRoleMappingMasterDetails(userId, approveFlag, userKey);
	}

	@Override
	public String checkUserRoleMappingMaster(String selRec, String appRejRemark,
			String checkType, String userKey) {
		return userRoleMappingMasterDao.checkUserRoleMappingMaster(selRec, appRejRemark, checkType, userKey);
	}
	
	@Override
	public String assignUserRoleMappings(UserMasterVo userMasterVo,
			String sbSrNo, String userKey) {
		return userRoleMappingMasterDao.assignUserRoleMappings(userMasterVo, sbSrNo, userKey);
	}
}
