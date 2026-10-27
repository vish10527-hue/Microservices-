package in.ameya.service.impl.master;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.BranchDao;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.BranchDetails;
import in.ameya.model.master.BranchSearch;
import in.ameya.service.master.BranchService;
import in.ameya.util.Constants;

@Service
@Transactional(transactionManager = "MainTransactionManager", rollbackFor=RuntimeException.class)
public class BranchServiceImpl implements BranchService {
	
	@Autowired
	BranchDao branchDao;

	@Override
	public DBResponse getBranchList(BranchSearch branchSearch) {
		return branchDao.getBranchList(branchSearch);
	}

	@Override
	public DBResponse addBranch(BranchDetails branch, Integer loggedInKey) {
		return branchDao.manageBranch(branch, loggedInKey, Constants.RECORD_INSERT);
	}
	
	@Override
	public DBResponse updateBranch(BranchDetails branch, Integer loggedInKey) {
		return branchDao.manageBranch(branch, loggedInKey, Constants.RECORD_UPDATE);
	}
	
	@Override
	public DBResponse discardBranch(BranchDetails branch, Integer loggedInKey) {
		return branchDao.manageBranch(branch, loggedInKey, Constants.RECORD_DELETE);
	}

	@Override
	public DBResponse verifyBranchRecord(BranchSearch branchSearch, Integer loggedInKey) {
		String branchKeys = "";
		String remarks = "";
		String recordStatus;
		
		if(branchSearch.branches == null || branchSearch.branches.size() == 0) {
			return new DBResponse(-1, "No Records Selected");
		}
		
		for (BranchDetails branch : branchSearch.branches) {
			branchKeys += branch.getBranchKey() + "|";
			remarks += branch.getAppRejRemarks().replace("|", " ") + "|";
		}
		
		//branchKeys = branchKeys.substring(0, branchKeys.length() - 1);
		//remarks = remarks.substring(0, remarks.length() - 1);
		
		if(branchSearch.getApprovedFlag() == 1) {
			recordStatus = Constants.RECORD_APPROVE;
		} else {
			recordStatus = Constants.RECORD_REJECT;
		}
		
		return branchDao.verifyBranchRecords(loggedInKey, branchKeys, remarks, recordStatus);
	}

	@Override
	public String exportBranchCSV(BranchSearch branchSearch) throws IOException{
		return branchDao.exportBranchCSV(branchSearch);
	}
	
	@Override
	public ExportResponse exportBranchExcel(BranchSearch branchSearch) throws IOException{
		return branchDao.exportBranchExcel(branchSearch);
	}

	@Override
	public BranchDetails getBranchDetailsById(Integer branchKey, Integer userId) {
		return branchDao.getBranchDetailsById(branchKey, userId);
	}
}
