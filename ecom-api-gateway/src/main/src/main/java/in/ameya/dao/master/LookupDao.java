package in.ameya.dao.master;

import java.io.IOException;

import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.LookupDetails;
import in.ameya.model.master.LookupSearch;

public interface LookupDao {

	DBResponse getLookupList(LookupSearch LookupSearch);

	DBResponse manageLookup(LookupDetails lookup, Integer loggedInKey, String action);

	DBResponse verifyLookupRecords(Integer loggedInKey, String lookupKeys, String remarks, String recordStatus);

	String exportLookupCSV(LookupSearch lookupSearch) throws IOException;

	ExportResponse  exportLookupExcel(LookupSearch lookupSearch) throws IOException;

	LookupDetails getLookupDetailsById(Integer lookupId, Integer userId);

}
