package in.ameya.service.impl.master;


import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.CommonExportDAO;
import in.ameya.model.master.BranchMasterVo;
import in.ameya.model.master.CountryMasterVo;
import in.ameya.model.master.CurrencyMasterVo;
import in.ameya.model.master.LoginVo;
import in.ameya.model.master.LookupMasterVo;
import in.ameya.model.master.NostroAcctsMasterVo;
import in.ameya.service.master.CommonExportService;
import jakarta.servlet.http.HttpServletResponse;

@Transactional(rollbackFor=RuntimeException.class)
@Service
public class CommonExportServiceImpl implements CommonExportService{

	@Autowired
	private CommonExportDAO commonExportDAO;
	
	
	@Override
	public ResultSet exportBranchDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, BranchMasterVo branchMasterVo){
		return commonExportDAO.exportBranchDataExcel(loginVo, response, reportName, branchMasterVo);
	}
	
	@Override
	public ResultSet exportBranchDataPDF(LoginVo loginVo, HttpServletResponse response, String reportName, BranchMasterVo branchMasterVo){
		return commonExportDAO.exportBranchDataPDF(loginVo, response, reportName, branchMasterVo);
	}
	
	@Override
	public String exportBranchDataCSV(LoginVo loginVo, HttpServletResponse response, BranchMasterVo branchMasterVo){
		return commonExportDAO.exportBranchDataCSV(loginVo, response, branchMasterVo);
	}
	
	@Override
	public ResultSet exportCountryDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, CountryMasterVo countryMasterVo){
		return commonExportDAO.exportCountryDataExcel(loginVo, response, reportName, countryMasterVo);
	}
	
	@Override
	public String exportCountryDataCSV(LoginVo loginVo, HttpServletResponse response, CountryMasterVo countryMasterVo){
		return commonExportDAO.exportCountryDataCSV(loginVo, response, countryMasterVo);
	}
	
	@Override
	public ResultSet exportCurrencyDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, CurrencyMasterVo currencyMasterVo){
		return commonExportDAO.exportCurrencyDataExcel(loginVo, response, reportName, currencyMasterVo);
	}
	
	@Override
	public ResultSet exportCurrencyDataPDF(LoginVo loginVo, HttpServletResponse response, String reportName, CurrencyMasterVo currencyMasterVo){
		return commonExportDAO.exportCurrencyDataPDF(loginVo, response, reportName, currencyMasterVo);
	}
	
	@Override
	public String exportCurrencyDataCSV(LoginVo loginVo, HttpServletResponse response, CurrencyMasterVo currencyMasterVo){
		return commonExportDAO.exportCurrencyDataCSV(loginVo, response, currencyMasterVo);
	}
	
	@Override
	public ResultSet exportNostroAcctsDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, NostroAcctsMasterVo nostroAcctsMasterVo){
		return commonExportDAO.exportNostroAcctsDataExcel(loginVo, response, reportName, nostroAcctsMasterVo);
	}
	
	@Override
	public ResultSet exportNostroAcctsDataPDF(LoginVo loginVo, HttpServletResponse response, String reportName, NostroAcctsMasterVo nostroAcctsMasterVo){
		return commonExportDAO.exportNostroAcctsDataPDF(loginVo, response, reportName, nostroAcctsMasterVo);
	}
	
	@Override
	public String exportNostroAcctsDataCSV(LoginVo loginVo, HttpServletResponse response, NostroAcctsMasterVo nostroAcctsMasterVo){
		return commonExportDAO.exportNostroAcctsDataCSV(loginVo, response, nostroAcctsMasterVo);
	}
	
	
	@Override
	public String exportLookupDataCSV(LoginVo loginVo, HttpServletResponse response, LookupMasterVo lookupMasterVo){
		return commonExportDAO.exportLookupDataCSV(loginVo, response, lookupMasterVo);
	}
	
	@Override
	public ResultSet exportLookupDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, LookupMasterVo lookupMasterVo){
		return commonExportDAO.exportLookupDataExcel(loginVo, response, reportName, lookupMasterVo);
	}
	
	
	
}
