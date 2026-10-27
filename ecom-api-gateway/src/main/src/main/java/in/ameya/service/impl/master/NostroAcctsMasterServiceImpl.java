package in.ameya.service.impl.master;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.NostroAcctsMasterDao;
import in.ameya.model.master.NostroAcctsMasterVo;
import in.ameya.service.master.NostroAcctsMasterService;

@Transactional(rollbackFor=RuntimeException.class)
@Service
public class NostroAcctsMasterServiceImpl implements NostroAcctsMasterService{

	@Autowired
	private NostroAcctsMasterDao nostroAcctsMasterDao;
	
	@Override
	public ArrayList<NostroAcctsMasterVo> getNostroAcctsMasterList(NostroAcctsMasterVo nostroAcctsMasterVo,
			String userId) {		
		return nostroAcctsMasterDao.getNostroAcctsMasterList(nostroAcctsMasterVo, userId);
	}

	@Override
	public String fetchExportNostroAcctsMasterCSV(NostroAcctsMasterVo nostroAcctsMasterVo,
			String userId) {
		return nostroAcctsMasterDao.fetchExportNostroAcctsMasterCSV(nostroAcctsMasterVo, userId);
	}	

	@Override
	public ResultSet fetchExportNostroAcctsMasterExcel(NostroAcctsMasterVo nostroAcctsMasterVo, String userId) {
		return nostroAcctsMasterDao.fetchExportNostroAcctsMasterExcel(nostroAcctsMasterVo, userId);
	}
	
	@Override
	public NostroAcctsMasterVo getNostroAcctsMasterDetails(String selRec, String userId) {
		return nostroAcctsMasterDao.getNostroAcctsMasterDetails(selRec, userId);
	}

	@Override
	public NostroAcctsMasterVo manageNostroAcctsMaster(NostroAcctsMasterVo nostroAcctsMasterVo, String userId) {
		return nostroAcctsMasterDao.manageNostroAcctsMaster(nostroAcctsMasterVo, userId);
	}
	
	@Override
	public NostroAcctsMasterVo checkNostroAcctsMaster(String selRec, String appRejRemark,
			String checkType, String userId) {
		return nostroAcctsMasterDao.checkNostroAcctsMaster(selRec, appRejRemark, checkType, userId);
	}
	
	@Override
	public ArrayList<NostroAcctsMasterVo> getNostroMasterPopupList(NostroAcctsMasterVo nostroAcctsMasterVo,
			String userId) {		
		return nostroAcctsMasterDao.getNostroMasterPopupList(nostroAcctsMasterVo, userId);
	}

}
