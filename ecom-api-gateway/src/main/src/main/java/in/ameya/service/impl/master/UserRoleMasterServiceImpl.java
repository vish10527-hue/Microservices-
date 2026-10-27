package in.ameya.service.impl.master;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.UserRoleMasterDao;
import in.ameya.model.master.UserRoleMasterVo;
import in.ameya.service.master.UserRoleMasterService;

@Transactional(rollbackFor=RuntimeException.class)
@Service
public class UserRoleMasterServiceImpl implements UserRoleMasterService{

	@Autowired
	private UserRoleMasterDao UserRoleMasterDao;
	
	@Override
	public ArrayList<UserRoleMasterVo> getUserRoleMasterList(UserRoleMasterVo userRoleMasterVo,
			String userKey) {		
		return UserRoleMasterDao.getUserRoleMasterList(userRoleMasterVo, userKey);
	}

	@Override
	public String fetchExportUserRoleMasterCSV(UserRoleMasterVo userRoleMasterVo,
			String userKey) {
		return UserRoleMasterDao.fetchExportUserRoleMasterCSV(userRoleMasterVo, userKey);
	}
	
	@Override
	public ResultSet fetchExportUserRoleMasterExcel(UserRoleMasterVo userRoleMasterVo, String userKey) {
		return UserRoleMasterDao.fetchExportUserRoleMasterExcel(userRoleMasterVo, userKey);
	}
	
	@Override
	public UserRoleMasterVo getUserRoleMasterDetails(String selRec, String userKey) {
		return UserRoleMasterDao.getUserRoleMasterDetails(selRec, userKey);
	}

	@Override
	public String manageUserRoleMaster(UserRoleMasterVo UserRoleMasterVo, String userKey) {
		return UserRoleMasterDao.manageUserRoleMaster(UserRoleMasterVo, userKey);
	}
	
	@Override
	public String checkUserRoleMaster(String selRec, String appRejRemark,
			String checkType, String userKey) {
		return UserRoleMasterDao.checkUserRoleMaster(selRec, appRejRemark, checkType, userKey);
	}

	
}
