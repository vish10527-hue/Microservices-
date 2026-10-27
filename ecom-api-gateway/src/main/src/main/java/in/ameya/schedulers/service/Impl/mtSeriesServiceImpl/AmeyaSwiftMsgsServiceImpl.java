package in.ameya.schedulers.service.Impl.mtSeriesServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.schedulers.dao.mtSeriesDao.AmeyaSwiftMsgsDao;
import in.ameya.schedulers.service.mtSeriesService.AmeyaSwiftMsgsService;



@Transactional(rollbackFor=RuntimeException.class)
@Service
public class AmeyaSwiftMsgsServiceImpl implements AmeyaSwiftMsgsService{
	
	@Autowired
	AmeyaSwiftMsgsDao ameyaSwiftMsgsDao;

	@Override
	public int createJob(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag) {
		return ameyaSwiftMsgsDao.createJob(msgType, strFileName, sourceSystem, fileModDateTime, murNumber, inOutFlag);
	}

//	@Override
//	public int createJobUnknownMsg(String msgType, String strFileName, int sourceSystem, String fileModDateTime,
//			String murNumber, String fileData) {
//		return ameyaSwiftMsgsDao.createJobUnknownMsg(msgType, strFileName, sourceSystem, fileModDateTime, murNumber, fileData);
//	}

	@Override
	public int executeInsertStatement(String insertStatement) {
		return ameyaSwiftMsgsDao.executeInsertStatement(insertStatement);
	}

//	@Override
//	public int startAutomatchingMatchingProcess() {
//		return ameyaSwiftMsgsDao.startAutomatchingMatchingProcess();
//	}
//
//	@Override
//	public int startMsgReUnReconciledSMSProcess() {
//		return ameyaSwiftMsgsDao.startMsgReUnReconciledSMSProcess();
//	}
//
//	@Override
//	public int startMsgReUnReconciledEMAILProcess() {
//		return ameyaSwiftMsgsDao.startMsgReUnReconciledEMAILProcess();
//	}
//
//	@Override
//	public void createSwiftOutwardFileForConcliare() {
//		ameyaSwiftMsgsDao.createSwiftOutwardFileForConcliare();		
//	}
//
//	@Override
//	public void createSwiftInOutCreditsFileForConcliare() {
//		ameyaSwiftMsgsDao.createSwiftInOutCreditsFileForConcliare();	
//	}
//
//	@Override
//	public int sentEmailSMSCommonProcess() {
//		return ameyaSwiftMsgsDao.sentEmailSMSCommonProcess();	
//	}

}
