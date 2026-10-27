package in.ameya.dao.master;



import java.sql.ResultSet;
import java.util.ArrayList;

import in.ameya.model.master.BranchMasterVo;
import in.ameya.model.master.LoginVo;
import jakarta.servlet.http.HttpServletResponse;

public interface BranchMasterDao {

	ArrayList<BranchMasterVo> getBranchMasterList(BranchMasterVo branchMasterVo, String userId);

	String fetchExportBranchMasterCSV(BranchMasterVo branchMasterVo, String userId);
	
	ResultSet fetchExportBranchMasterExcel(LoginVo loginVo,BranchMasterVo branchMasterVo, String userId, HttpServletResponse response);

	BranchMasterVo getBranchMasterDetails(String selRec, String userId);

	BranchMasterVo manageBranchMaster(BranchMasterVo branchMasterVo, String userId);
	
	BranchMasterVo checkBranchMaster(String selRec, String appRejRemark, String checkType, String userId);

	ArrayList<BranchMasterVo> getBranchListPopup(BranchMasterVo branchMasterVo, String userId);
	
}
