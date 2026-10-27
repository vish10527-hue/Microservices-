package in.ameya.dao.master;

import java.io.IOException;

import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.NostroAcctsDetails;
import in.ameya.model.master.NostroAcctsSearch;

public interface NostroAcctsDao  {
	
	DBResponse getNostroAcctsList(NostroAcctsSearch NostroAcctsSearch);

	DBResponse manageNostroAccts(NostroAcctsDetails branch, Integer loggedInKey, String action);

	DBResponse verifyNostroAcctsRecords(Integer loggedInKey, String branchKeys, String remarks, String recordStatus);

	String exportNostroAcctsCSV(NostroAcctsSearch branchSearch) throws IOException;

	ExportResponse exportNostroAcctsExcel(NostroAcctsSearch branchSearch) throws IOException;

	NostroAcctsDetails getNostroAcctsDetailsById(Integer branchKey, Integer userId);


}
