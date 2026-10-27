package in.ameya.service.impl.master;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.LookupDao;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.LookupDetails;
import in.ameya.model.master.LookupSearch;
import in.ameya.service.master.LookupService;
import in.ameya.util.Constants;

@Service
@Transactional(transactionManager = "MainTransactionManager", rollbackFor=RuntimeException.class)
public class LookupServiceImpl implements LookupService {
	
	@Autowired
	LookupDao lookupDao;

	@Override
	public DBResponse getLookupList(LookupSearch lookupSearch) {
		return lookupDao.getLookupList(lookupSearch);
	}

	@Override
	public DBResponse addLookup(LookupDetails lookup, Integer loggedInKey) {
		return lookupDao.manageLookup(lookup, loggedInKey, Constants.RECORD_INSERT);
	}
	
	@Override
	public DBResponse updateLookup(LookupDetails lookup, Integer loggedInKey) {
		return lookupDao.manageLookup(lookup, loggedInKey, Constants.RECORD_UPDATE);
	}
	
	@Override
	public DBResponse discardLookup(LookupDetails lookup, Integer loggedInKey) {
		return lookupDao.manageLookup(lookup, loggedInKey, Constants.RECORD_DELETE);
	}

	@Override
	public DBResponse verifyLookupRecord(LookupSearch lookupSearch, Integer loggedInKey) {
		String lookupKeys = "";
		String remarks = "";
		String recordStatus;
		
		if(lookupSearch.lookups == null || lookupSearch.lookups.size() == 0) {
			return new DBResponse(-1, "No Records Selected");
		}
		
		for (LookupDetails lookup : lookupSearch.lookups) {
			lookupKeys += lookup.getLookupKey() + "|";
			remarks += lookup.getAppRejRemarks().replace("|", " ") + "|";
		}
		
		//lookupKeys = lookupKeys.substring(0, lookupKeys.length() - 1);
		//remarks = remarks.substring(0, remarks.length() - 1);
		
		if(lookupSearch.getApprovedFlag() == 1) {
			recordStatus = Constants.RECORD_APPROVE;
		} else {
			recordStatus = Constants.RECORD_REJECT;
		}
		
		return lookupDao.verifyLookupRecords(loggedInKey, lookupKeys, remarks, recordStatus);
	}

	@Override
	public String exportLookupCSV(LookupSearch lookupSearch) throws IOException{
		return lookupDao.exportLookupCSV(lookupSearch);
	}
	
	@Override
	public ExportResponse exportLookupExcel(LookupSearch lookupSearch) throws IOException {
		return lookupDao.exportLookupExcel(lookupSearch);
	}

	@Override
	public LookupDetails getLookupDetailsById(Integer lookupId, Integer userId) {
		return lookupDao.getLookupDetailsById(lookupId, userId);
	}
}
