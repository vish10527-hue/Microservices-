package in.ameya.schedulers.dao.mtSeriesDao;

public interface AmeyaSwiftMsgsDao {

	int createJob(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag);

//	int createJobUnknownMsg(String msgType, String strFileName, int sourceSystem, String fileModDateTime,
//			String murNumber, String fileData);

	int executeInsertStatement(String insertStatement);

//	int startAutomatchingMatchingProcess();
//
//	int startMsgReUnReconciledSMSProcess();
//
//	int startMsgReUnReconciledEMAILProcess();
//
//	void createSwiftOutwardFileForConcliare();
//
//	void createSwiftInOutCreditsFileForConcliare();
//
//	int sentEmailSMSCommonProcess();

}
