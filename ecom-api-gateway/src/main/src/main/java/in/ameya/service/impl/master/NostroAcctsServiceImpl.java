package in.ameya.service.impl.master;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.NostroAcctsDao;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.NostroAcctsDetails;
import in.ameya.model.master.NostroAcctsSearch;
import in.ameya.service.master.NostroAcctsService;
import in.ameya.util.Constants;

@Service
@Transactional(transactionManager = "MainTransactionManager", rollbackFor=RuntimeException.class)
public class NostroAcctsServiceImpl implements NostroAcctsService {
	
	@Autowired
	NostroAcctsDao nostroAcctsDao;

	@Override
	public DBResponse getNostroAcctsList(NostroAcctsSearch nostroAcctsSearch) {
		return nostroAcctsDao.getNostroAcctsList(nostroAcctsSearch);
	}

	@Override
	public DBResponse addNostroAccts(NostroAcctsDetails nostroAccts, Integer loggedInKey) {
		return nostroAcctsDao.manageNostroAccts(nostroAccts, loggedInKey, Constants.RECORD_INSERT);
	}
	
	@Override
	public DBResponse updateNostroAccts(NostroAcctsDetails nostroAccts, Integer loggedInKey) {
		return nostroAcctsDao.manageNostroAccts(nostroAccts, loggedInKey, Constants.RECORD_UPDATE);
	}
	
	@Override
	public DBResponse discardNostroAccts(NostroAcctsDetails nostroAccts, Integer loggedInKey) {
		return nostroAcctsDao.manageNostroAccts(nostroAccts, loggedInKey, Constants.RECORD_DELETE);
	}

	@Override
	public DBResponse verifyNostroAcctsRecord(NostroAcctsSearch nostroAcctsSearch, Integer loggedInKey) {
		String nostroAcctsKeys = "";
		String remarks = "";
		String recordStatus;
		
		if(nostroAcctsSearch.nostroAccts == null || nostroAcctsSearch.nostroAccts.size() == 0) {
			return new DBResponse(-1, "No Records Selected");
		}
		
		for (NostroAcctsDetails nostroAccts : nostroAcctsSearch.nostroAccts) {
			nostroAcctsKeys += nostroAccts.getNostroAcctsKey() + "|";
			remarks += nostroAccts.getAppRejRemarks().replace("|", " ") + "|";
		}
		
		//nostroAcctsKeys = nostroAcctsKeys.substring(0, nostroAcctsKeys.length() - 1);
		//remarks = remarks.substring(0, remarks.length() - 1);
		
		if(nostroAcctsSearch.getApprovedFlag() == 1) {
			recordStatus = Constants.RECORD_APPROVE;
		} else {
			recordStatus = Constants.RECORD_REJECT;
		}
		
		return nostroAcctsDao.verifyNostroAcctsRecords(loggedInKey, nostroAcctsKeys, remarks, recordStatus);
	}

	@Override
	public String exportNostroAcctsCSV(NostroAcctsSearch nostroAcctsSearch) throws IOException{
		return nostroAcctsDao.exportNostroAcctsCSV(nostroAcctsSearch);
	}
	
	@Override
	public ExportResponse exportNostroAcctsExcel(NostroAcctsSearch nostroAcctsSearch) throws IOException{
		return nostroAcctsDao.exportNostroAcctsExcel(nostroAcctsSearch);
	}

	@Override
	public NostroAcctsDetails getNostroAcctsDetailsById(Integer nostroAcctsKey, Integer userId) {
		return nostroAcctsDao.getNostroAcctsDetailsById(nostroAcctsKey, userId);
	}
}