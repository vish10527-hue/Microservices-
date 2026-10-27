package in.ameya.dao.dwr;

public interface DWRDao {

	public String getAllLookups(String lookupType);
	
	public String getMandate103Process(String lookupType);
	
	public String getPoolAccountManDateWiseList(String msgTypeKey, String manDateCode, String branchSrNo);
	
	public String getNostroList();
	
	public String getCountryList();
	
	public String getCurrencyList();
	
	public String getBankList();

	public String getBankListMT103CRH();
	
	public String getBranchIdList();
	
	public String getAllMsgType();
	
	public String getSelectedMsgType(String type);
	
	public String getAccessPrivilege(String privileges,String userRoleId);
	
	public String updateLoginStatus(String userName);
	
	public int checkValidSession(String userName, String sessionId);
	
	public int insertOracleErrorInTable(String error, String proName, String userId);
	
	public String getIFSCList();
	
	public String getPurposeList();
	
	public String getAuditDWRList();
	
	public String getPoolAcc(String lookupType);
	
	public String getValueDateForDashboard(String dashboardCountFor, String msgType);
	
	public String calculateCardRateFinacleForTRREFNo(String trRefNo);
	
	public String checkEODTimeForApprove(String msgId,String userId);
	
	public String calculateCardRateFinacleForTRREFNo(String dashboardCountFor,String msgType);
	
	public String checkEODTimeForApproveMT101(String msgId,String userId);
	
	public String getSSIAccountNos(String searchAccountNo);
	
	public String getSSICustomerNames(String searchCustomerName);
	
	public String getSSIBranches(String searchBranch);
	
	public String getBCatBranchList();
	
	public String getNostroAccountMapFinList();
	
	public int getNostroAccMapFinCurrency(String searchNostro);
	
	public String getMT199ReasonList(String reasonType);
	
	public String getRetailTFCReportList(String reportFor, String branchCode, String userId);
	
	public String getSettlementProductType(String msgCredId);
	
	public String getMT199TemplateText(String creditId, String templateType, double chargesRecovered);
	
	public String fetchMT199TemplateType(String msgType, String userType);
	
	public String saveMT199ROFPrint(String msgId, String creditId, String mt199SrNo, String convertAmtFlag, String  msgAmountSum, String tag79, String userId);
	
	public String getSIAcctHolderName(String accountNo);
	
	public String getCreditsHoldRemarks(String userId);
	
	public String getFLC700TemplateName(String custId);
	
	public String getFLC700TemplateDetails(String flcDetId);
	
	public String getPublishIdAfterDatePicked(String fromDate, String toDate);
	
	public String getUserList();
	
	public String getUserRolesProfileWise(String profileId);
	
	public String getMirrorList();
}
