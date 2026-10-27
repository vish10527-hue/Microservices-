package in.ameya.service.master;

import java.io.IOException;

import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.LookupDetails;
import in.ameya.model.master.LookupSearch;

public interface LookupService {

	public DBResponse getLookupList(LookupSearch LookupSearch);
	
	public DBResponse addLookup(LookupDetails Lookup, Integer loggedInKey);
	
	public DBResponse updateLookup(LookupDetails Lookup, Integer loggedInKey);
	
	public DBResponse discardLookup(LookupDetails Lookup, Integer loggedInKey);

	public DBResponse verifyLookupRecord(LookupSearch LookupSearch, Integer loggedInKey);

	String exportLookupCSV(LookupSearch lookupSearch) throws IOException;

	ExportResponse exportLookupExcel(LookupSearch lookupSearch) throws IOException;

	public LookupDetails getLookupDetailsById(Integer lookupId, Integer userId);
	
}
