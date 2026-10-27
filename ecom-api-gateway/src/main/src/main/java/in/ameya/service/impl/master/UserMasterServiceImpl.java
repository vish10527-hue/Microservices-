package in.ameya.service.impl.master;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.UserMasterDao;
import in.ameya.model.master.UserMasterVo;
import in.ameya.service.master.UserMasterService;

@Transactional(rollbackFor=RuntimeException.class)
@Service
public class UserMasterServiceImpl implements UserMasterService{

	@Autowired
	private UserMasterDao userMasterDao;
	
	@Override
	public ArrayList<UserMasterVo> getUserMasterList(UserMasterVo userMasterVo,
			String userKey) {		
		return userMasterDao.getUserMasterList(userMasterVo, userKey);
	}

	@Override
	public String fetchExportUserMasterCSV(UserMasterVo userMasterVo,
			String userKey) {
		return userMasterDao.fetchExportUserMasterCSV(userMasterVo, userKey);
	}
	
	@Override
	public ResultSet fetchExportUserMasterExcel(UserMasterVo userMasterVo, String userKey) {
		return userMasterDao.fetchExportUserMasterExcel(userMasterVo, userKey);
	}
	
	@Override
	public UserMasterVo getUserMasterDetails(String selRec, String userKey) {
		return userMasterDao.getUserMasterDetails(selRec, userKey);
	}

	@Override
	public String manageUserMaster(UserMasterVo userMasterVo, String userKey) {
		return userMasterDao.manageUserMaster(userMasterVo, userKey);
	}
	
	@Override
	public String checkUserMaster(String selRec, String appRejRemark,
			String checkType, String userKey) {
		return userMasterDao.checkUserMaster(selRec, appRejRemark, checkType, userKey);
	}
	
}
