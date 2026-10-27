package in.ameya.service.master;

import java.io.IOException;

import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.BranchDetails;
import in.ameya.model.master.BranchSearch;

public interface BranchService {

	DBResponse getBranchList(BranchSearch branchSearch);

	DBResponse addBranch(BranchDetails branch, Integer loggedInKey);

	DBResponse updateBranch(BranchDetails branch, Integer loggedInKey);

	DBResponse discardBranch(BranchDetails branch, Integer loggedInKey);

	DBResponse verifyBranchRecord(BranchSearch branchSearch, Integer loggedInKey);

	String exportBranchCSV(BranchSearch branchSearch) throws IOException;

	ExportResponse exportBranchExcel(BranchSearch branchSearch) throws IOException;

	BranchDetails getBranchDetailsById(Integer branchKey, Integer userId);

}
