package in.ameya.service.impl.master;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ameya.dao.master.LookupMasterDao;
import in.ameya.model.master.LookupMasterVo;
import in.ameya.service.master.LookupMasterService;


@Service
public class LookupMasterServiceImpl implements LookupMasterService{

	@Autowired
	private LookupMasterDao lookupMasterDao;
	
	@Override
	public ArrayList<LookupMasterVo> getLookupMasterList(LookupMasterVo lookupMasterVo,
			String userKey) {		
		return lookupMasterDao.getLookupMasterList(lookupMasterVo, userKey);
	}

	@Override
	public String fetchExportLookupMasterCSV(LookupMasterVo lookupMasterVo,
			String userKey) {
		return lookupMasterDao.fetchExportLookupMasterCSV(lookupMasterVo, userKey);
	}

	@Override
	public ResultSet fetchExportLookupMasterExcel(LookupMasterVo lookupMasterVo,
			String userKey) {
		return lookupMasterDao.fetchExportLookupMasterExcel(lookupMasterVo, userKey);
	}

	@Override
	public LookupMasterVo getLookupMasterDetails(String selRec, String userKey) {
		return lookupMasterDao.getLookupMasterDetails(selRec, userKey);
	}

	@Override
	public LookupMasterVo manageLookupMaster(String manageType,
			LookupMasterVo lookupMasterVo, String userKey) {
		return lookupMasterDao.manageLookupMaster(manageType, lookupMasterVo, userKey);
	}

	@Override
	public LookupMasterVo checkLookupMaster(String selRec, String appRejRemark,
			String checkType, String userKey) {
		return lookupMasterDao.checkLookupMaster(selRec, appRejRemark, checkType, userKey);
	}

}
