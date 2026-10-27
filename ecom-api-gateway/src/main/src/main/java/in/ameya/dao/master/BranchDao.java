package in.ameya.dao.master;

import java.io.IOException;

import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.BranchDetails;
import in.ameya.model.master.BranchSearch;

public interface BranchDao {

	DBResponse getBranchList(BranchSearch BranchSearch);

	DBResponse manageBranch(BranchDetails branch, Integer loggedInKey, String action);

	DBResponse verifyBranchRecords(Integer loggedInKey, String branchKeys, String remarks, String recordStatus);

	String exportBranchCSV(BranchSearch branchSearch) throws IOException;

	ExportResponse exportBranchExcel(BranchSearch branchSearch) throws IOException;

	BranchDetails getBranchDetailsById(Integer branchKey, Integer userId);

}
