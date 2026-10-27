package in.ameya.service.impl.master;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.BranchMasterDao;
import in.ameya.model.master.BranchMasterVo;
import in.ameya.model.master.LoginVo;
import in.ameya.service.master.BranchMasterService;
import jakarta.servlet.http.HttpServletResponse;

@Transactional(rollbackFor=RuntimeException.class)
@Service
public class BranchMasterServiceImpl implements BranchMasterService{

	@Autowired
	private BranchMasterDao branchMasterDao;
	
	@Override
	public ArrayList<BranchMasterVo> getBranchMasterList(BranchMasterVo branchMasterVo,
			String userId) {		
		return branchMasterDao.getBranchMasterList(branchMasterVo, userId);
	}

	@Override
	public String fetchExportBranchMasterCSV(BranchMasterVo branchMasterVo,
			String userId) {
		return branchMasterDao.fetchExportBranchMasterCSV(branchMasterVo, userId);
	}
	
	@Override
	public ResultSet fetchExportBranchMasterExcel(LoginVo loginVo,BranchMasterVo branchMasterVo,
			String userId, HttpServletResponse response) {
		return branchMasterDao.fetchExportBranchMasterExcel(loginVo, branchMasterVo, userId, response);
	}
	
	@Override
	public BranchMasterVo getBranchMasterDetails(String selRec, String userId) {
		return branchMasterDao.getBranchMasterDetails(selRec, userId);
	}

	@Override
	public BranchMasterVo manageBranchMaster(BranchMasterVo branchMasterVo, String userId) {
		return branchMasterDao.manageBranchMaster(branchMasterVo, userId);
	}
	
	@Override
	public BranchMasterVo checkBranchMaster(String selRec, String appRejRemark,
			String checkType, String userId) {
		return branchMasterDao.checkBranchMaster(selRec, appRejRemark, checkType, userId);
	}
	
	@Override
	public ArrayList<BranchMasterVo> getBranchListPopup(BranchMasterVo branchMasterVo,
			String userId) {		
		return branchMasterDao.getBranchListPopup(branchMasterVo, userId);
	}
	
}
