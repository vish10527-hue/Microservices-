package in.ameya.service.impl.dwr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.dwr.DWRDao;
import in.ameya.service.dwr.DWRService;


@Transactional(rollbackFor=RuntimeException.class)
@Service
public class DWRServiceImpl implements DWRService{

	@Autowired
	private DWRDao dwrDao;
	
	@Override
	public String getAllLookups(String lookupType) {		
		return dwrDao.getAllLookups(lookupType);
	}
	
	@Override
	public String getMandate103Process(String lookupType) {		
		return dwrDao.getAllLookups(lookupType);
	}
	
	@Override
	public String getPoolAccountManDateWiseList(String msgTypeKey, String manDateCode, String branchSrNo){		
		return dwrDao.getPoolAccountManDateWiseList(msgTypeKey,manDateCode,branchSrNo);
	}
	
	@Override
	public String getNostroList() {		
		return dwrDao.getNostroList();
	}
	
	@Override
	public String getCountryList() {		
		return dwrDao.getCountryList();
	}
	
	@Override
	public String getCurrencyList() {		
		return dwrDao.getCurrencyList();
	}
	
	@Override
	public String getBankList() {		
		return dwrDao.getBankList();
	}
	
	@Override
	public String getBankListMT103CRH() {		
		return dwrDao.getBankListMT103CRH();
	}
	
	@Override
	public String getBranchIdList() {		
		return dwrDao.getBranchIdList();
	}
	
	@Override
	public String getAllMsgType() {		
		return dwrDao.getAllMsgType();
	}
	
	@Override
	public String getSelectedMsgType(String type) {		
		return dwrDao.getSelectedMsgType(type);
	}
	
	@Override
	public String getAccessPrivilege(String privileges,String userRoleId) {		
		return dwrDao.getAccessPrivilege(privileges,userRoleId);
	}
	
	@Override
	public String updateLoginStatus(String userName) {		
		return dwrDao.updateLoginStatus(userName);
	}
	
	@Override
	public int checkValidSession(String userName, String sessionId) {		
		return dwrDao.checkValidSession(userName,sessionId);
	}
	
	@Override
	public int insertOracleErrorInTable(String error, String proName, String userId) {		
		return dwrDao.insertOracleErrorInTable(error,proName,userId);
	}
	
	@Override
	public String getIFSCList() {		
		return dwrDao.getIFSCList();
	}
	
	@Override
	public String getPurposeList() {		
		return dwrDao.getPurposeList();
	}
	
	@Override
	public String getAuditDWRList() {		
		return dwrDao.getAuditDWRList();
	}
	
	@Override
	public String getPoolAcc(String lookupType) {		
		return dwrDao.getPoolAcc(lookupType);
	}
	
	@Override
	public String getValueDateForDashboard(String dashboardCountFor, String msgType) {		
		return dwrDao.getValueDateForDashboard(dashboardCountFor,msgType);
	}
		
	@Override
	public String calculateCardRateFinacleForTRREFNo(String trRefNo) {		
		return dwrDao.calculateCardRateFinacleForTRREFNo(trRefNo);
	}
	
	@Override
	public String checkEODTimeForApprove(String msgId,String userId) {		
		return dwrDao.checkEODTimeForApprove(msgId,userId);
	}
	
	@Override
	public String calculateCardRateFinacleForTRREFNo(String dashboardCountFor,String msgType) {		
		return dwrDao.calculateCardRateFinacleForTRREFNo(dashboardCountFor,msgType);
	}
	
	@Override
	public String checkEODTimeForApproveMT101(String msgId,String userId) {		
		return dwrDao.checkEODTimeForApprove(msgId,userId);
	}
	
	@Override
	public String getSSIAccountNos(String searchAccountNo) {		
		return dwrDao.getSSIAccountNos(searchAccountNo);
	}
	
	@Override
	public String getSSICustomerNames(String searchCustomerName) {		
		return dwrDao.getSSICustomerNames(searchCustomerName);
	}
	
	@Override
	public String getSSIBranches(String searchBranch) {		
		return dwrDao.getSSIBranches(searchBranch);
	}
	
	@Override
	public String getBCatBranchList() {		
		return dwrDao.getBCatBranchList();
	}
	
	@Override
	public String getNostroAccountMapFinList() {		
		return dwrDao.getNostroAccountMapFinList();
	}
	
	@Override
	public int getNostroAccMapFinCurrency(String searchNostro) {		
		return dwrDao.getNostroAccMapFinCurrency(searchNostro);
	}
	
	@Override
	public String getMT199ReasonList(String reasonType) {		
		return dwrDao.getMT199ReasonList(reasonType);
	}
	
	@Override
	public String getRetailTFCReportList(String reportFor, String branchCode, String userId) {		
		return dwrDao.getRetailTFCReportList(reportFor,branchCode,userId);
	}
	
	@Override
	public String getSettlementProductType(String msgCredId) {		
		return dwrDao.getSettlementProductType(msgCredId);
	}
	
	@Override
	public String getMT199TemplateText(String creditId, String templateType, double chargesRecovered) {		
		return dwrDao.getMT199TemplateText(creditId,templateType,chargesRecovered);
	}
	
	@Override
	public String fetchMT199TemplateType(String msgType, String userType) {		
		return dwrDao.fetchMT199TemplateType(msgType,userType);
	}
	
	@Override
	public String saveMT199ROFPrint(String msgId, String creditId, String mt199SrNo, String convertAmtFlag, String  msgAmountSum, String tag79, String userId) {		
		return dwrDao.saveMT199ROFPrint(msgId, creditId, mt199SrNo, convertAmtFlag, msgAmountSum, tag79, userId);
	}
	
	@Override
	public String getSIAcctHolderName(String accountNo) {		
		return dwrDao.getSIAcctHolderName(accountNo);
	}
	
	@Override
	public String getCreditsHoldRemarks(String userId) {		
		return dwrDao.getCreditsHoldRemarks(userId);
	}
	
	@Override
	public String getFLC700TemplateName(String custId) {		
		return dwrDao.getFLC700TemplateName(custId);
	}
	
	@Override
	public String getFLC700TemplateDetails(String flcDetId) {		
		return dwrDao.getFLC700TemplateDetails(flcDetId);
	}
	
	@Override
	public String getPublishIdAfterDatePicked(String fromDate, String toDate) {		
		return dwrDao.getPublishIdAfterDatePicked(fromDate,toDate);
	}
	
	@Override
	public String getUserList() {		
		return dwrDao.getUserList();
	}
	
	@Override
	public String getUserRolesProfileWise(String profileId) {		
		return dwrDao.getUserList();
	}
	
	@Override
	public String getMirrorList() {		
		return dwrDao.getMirrorList();
	}
}

