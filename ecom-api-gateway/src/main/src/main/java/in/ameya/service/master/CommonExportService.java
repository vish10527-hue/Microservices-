package in.ameya.service.master;

import java.sql.ResultSet;

import in.ameya.model.master.BranchMasterVo;
import in.ameya.model.master.CountryMasterVo;
import in.ameya.model.master.CurrencyMasterVo;
import in.ameya.model.master.LoginVo;
import in.ameya.model.master.LookupMasterVo;
import in.ameya.model.master.NostroAcctsMasterVo;
import jakarta.servlet.http.HttpServletResponse;

public interface CommonExportService {

	ResultSet exportBranchDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, BranchMasterVo branchMasterVo);
	
	ResultSet exportBranchDataPDF(LoginVo loginVo, HttpServletResponse response, String reportName, BranchMasterVo branchMasterVo);
	
	String exportBranchDataCSV(LoginVo loginVo, HttpServletResponse response, BranchMasterVo branchMasterVo);
	
	ResultSet exportCountryDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, CountryMasterVo countryMasterVo);
	
	String exportCountryDataCSV(LoginVo loginVo, HttpServletResponse response, CountryMasterVo countryMasterVo);
	
	ResultSet exportCurrencyDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, CurrencyMasterVo currencyMasterVo);
	
	ResultSet exportCurrencyDataPDF(LoginVo loginVo, HttpServletResponse response, String reportName, CurrencyMasterVo currencyMasterVo);
	
	String exportCurrencyDataCSV(LoginVo loginVo, HttpServletResponse response, CurrencyMasterVo currencyMasterVo);
	
	ResultSet exportNostroAcctsDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, NostroAcctsMasterVo nostroAcctsMasterVo);
	
	ResultSet exportNostroAcctsDataPDF(LoginVo loginVo, HttpServletResponse response, String reportName, NostroAcctsMasterVo nostroAcctsMasterVo);
	
	String exportNostroAcctsDataCSV(LoginVo loginVo, HttpServletResponse response, NostroAcctsMasterVo nostroAcctsMasterVo);
	
	String exportLookupDataCSV(LoginVo loginVo, HttpServletResponse response, LookupMasterVo lookupMasterVo);

	ResultSet exportLookupDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, LookupMasterVo lookupMasterVo);
}
