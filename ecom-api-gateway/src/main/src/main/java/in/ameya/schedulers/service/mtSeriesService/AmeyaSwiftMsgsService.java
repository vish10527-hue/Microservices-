package in.ameya.schedulers.service.mtSeriesService;

public interface AmeyaSwiftMsgsService {

	public int createJob(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag);
	
//	public int createJobUnknownMsg(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, String fileData);
	
	public int executeInsertStatement(String insertStatement);

//	public int startAutomatchingMatchingProcess();
//
//	public int startMsgReUnReconciledSMSProcess();
//	
//	public int startMsgReUnReconciledEMAILProcess();
//
//	public void createSwiftOutwardFileForConcliare();
//
//	public void createSwiftInOutCreditsFileForConcliare();
//
//	public int sentEmailSMSCommonProcess();
}
