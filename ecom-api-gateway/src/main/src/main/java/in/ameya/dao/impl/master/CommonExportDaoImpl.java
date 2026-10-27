package in.ameya.dao.impl.master;

import java.io.FileWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import au.com.bytecode.opencsv.CSVWriter;
import in.ameya.dao.master.CommonExportDAO;
import in.ameya.model.master.BranchMasterVo;
import in.ameya.model.master.CountryMasterVo;
import in.ameya.model.master.CurrencyMasterVo;
import in.ameya.model.master.GenericReportVO;
import in.ameya.model.master.LoginVo;
import in.ameya.model.master.LookupMasterVo;
import in.ameya.model.master.NostroAcctsMasterVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.Constants;
import in.ameya.util.DBConstants;
import in.ameya.util.DateUtil;
import in.ameya.util.ExportData;
import in.ameya.util.LoggerUtil;
import jakarta.servlet.http.HttpServletResponse;

@PropertySource(value="classpath:filePath.properties")
@Repository
public class CommonExportDaoImpl extends JdbcDaoSupport implements CommonExportDAO{
		
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private Environment env;
	
	@Autowired
	ExportData exportData;	
	
	
	@Override
	public ResultSet exportBranchDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, BranchMasterVo branchMasterVo) {
		ResultSet resultSet = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		
		try
		{
			String call = "{ call "+DBConstants.STP_FETCH_EXPORT_BRANCH_LIST+"(?, ?, ?, ?, ?, ?, ?) }";
			
	        conn = mainDatasource.getConnection();

	        cstmt = conn.prepareCall(call);

	        cstmt.setString(1, branchMasterVo.getSearchCriteria());
	        cstmt.setString(2, branchMasterVo.getSearchBranchCode());
	        cstmt.setString(3, branchMasterVo.getSearchBicCode());
	        cstmt.setString(4, branchMasterVo.getSearchBranchDesc());
	        cstmt.setString(5, branchMasterVo.getSearchBranchType());
	        cstmt.setString(6, branchMasterVo.getSearchBcatBranchId());
	        cstmt.setString(7, loginVo.getUserId());
	        resultSet = cstmt.executeQuery();
	        
	        if(resultSet != null)
			{
	        	String[] branchIdsArr = branchMasterVo.getBcatBranchDesc().split("#");
				StringBuffer sbufBCatBranchId = new StringBuffer();
				if(branchMasterVo.getBcatBranchDesc() != "") {
					if(branchIdsArr.length > 0){
						for(int i=0; i<branchIdsArr.length; i++){
							String strVal = branchIdsArr[i];
							if(!strVal.startsWith("--")){
								sbufBCatBranchId.append(strVal);
								sbufBCatBranchId.append(",");
							}
						}
					}
				}
				
				String searchparamVals = "Search Criteria|"+branchMasterVo.getSearchCriteriaTextArray()+"|Sol Id|"+branchMasterVo.getSearchBranchCode()+"#"
						+ "Type|"+branchMasterVo.getBranchTypeTextArray()
						+"|BCat branch Id|"+sbufBCatBranchId.toString().trim()+"#"
						+"BIC Code|"+branchMasterVo.getSearchBicCode()
						+"|Branch Description|"+branchMasterVo.getSearchBranchDesc()+"#";
				
			   try {
				   exportData.downloadFileDetailsExcelWithParams(response,reportName, resultSet, loginVo.getUserId(), searchparamVals, loginVo.getUserName(), loginVo.getBranchDescription());
				} catch (Exception e) {
					e.printStackTrace();
					loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportBranchDataExcel", loginVo.getUserId());
					throw new RuntimeException("Something went wrong please try again");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportBranchDataExcel", loginVo.getUserId());
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		finally
		{
			try
			{
				if(cstmt != null)
		        {
		        	cstmt.close();
		        	cstmt = null;
		        }
		        
				if(conn != null)
				{
					conn.close();
					conn = null;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportBranchDataExcel", loginVo.getUserId());
				throw new RuntimeException("Something went wrong please try again");
			}
		}
		return resultSet;
	}
	
	@Override
	public ResultSet exportBranchDataPDF(LoginVo loginVo, HttpServletResponse response, String reportName, BranchMasterVo branchMasterVo) {
		ResultSet resultSet = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		
		try
		{
			String call = "{ call "+DBConstants.STP_FETCH_EXPORT_BRANCH_LIST+"(?, ?, ?, ?, ?, ?, ?) }";
			
	        conn = mainDatasource.getConnection();

	        cstmt = conn.prepareCall(call);

	        cstmt.setString(1, branchMasterVo.getSearchCriteria());
	        cstmt.setString(2, ("").equalsIgnoreCase(branchMasterVo.getSearchBranchCode()) ? null : branchMasterVo.getSearchBranchCode());
	        cstmt.setString(3, ("").equalsIgnoreCase(branchMasterVo.getSearchBicCode()) ? null : branchMasterVo.getSearchBicCode());
	        cstmt.setString(4, ("").equalsIgnoreCase(branchMasterVo.getSearchBranchDesc()) ? null : branchMasterVo.getSearchBranchDesc());
	        cstmt.setString(5, ("").equalsIgnoreCase(branchMasterVo.getSearchBranchType()) ? null : branchMasterVo.getSearchBranchType());
	        cstmt.setString(6, ("").equalsIgnoreCase(branchMasterVo.getSearchBcatBranchId()) ? null : branchMasterVo.getSearchBcatBranchId());
	        cstmt.setString(7, loginVo.getUserId());
	        resultSet = cstmt.executeQuery();
	        
	        if(resultSet != null)
			{
			  String xmlReportFileName = "reconRptMstSOLID.xml";
			  String reportTitle = "Bank Sol ID Master";
			  String reportOutFileName = "BankSolIDMaster";
			  String userName = loginVo.getUserName();
			  String branchDescription = loginVo.getBranchDescription();
			  GenericReportVO objRptVO = new GenericReportVO();
			  objRptVO.setReportTitle(reportTitle);
			  objRptVO.setXmlReportFileName(xmlReportFileName);
			  objRptVO.setReportOutFileName(reportOutFileName);
			  objRptVO.setReportName(reportName);
			  objRptVO.setRptBranchName(branchDescription);
			  objRptVO.setRptUserName(userName);
			  
			  String[] branchIdsArr = branchMasterVo.getBcatBranchDesc().split("#");
				StringBuffer sbufBCatBranchId = new StringBuffer();
				if(branchMasterVo.getBcatBranchDesc() != "") {
					if(branchIdsArr.length > 0){
						for(int i=0; i<branchIdsArr.length; i++){
							String strVal = branchIdsArr[i];
							if(!strVal.startsWith("--")){
								sbufBCatBranchId.append(strVal);
								sbufBCatBranchId.append(",");
							}
						}
					}
				}
			  
			  objRptVO.setSearchCriteriaDesc(branchMasterVo.getSearchCriteriaTextArray());
			  objRptVO.setsOLID(branchMasterVo.getSearchBranchCode());
			  objRptVO.setSearchTYPE(branchMasterVo.getBranchTypeTextArray());
			  objRptVO.setSearchCode(sbufBCatBranchId.toString().trim());
			  objRptVO.setbICCODE(branchMasterVo.getSearchBicCode());
			  objRptVO.setSearchName(branchMasterVo.getSearchBranchDesc());
				
			   try {
				   exportData.downloadFileDetailsPDF(response, objRptVO, resultSet, loginVo.getUserId(), conn);
				} catch (Exception e) {
					e.printStackTrace();
					loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportBranchDataPDF", loginVo.getUserId());
					throw new RuntimeException("Something went wrong please try again");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportBranchDataPDF", loginVo.getUserId());
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		finally
		{
			try
			{
				if(cstmt != null)
		        {
		        	cstmt.close();
		        	cstmt = null;
		        }
		        
				if(conn != null)
				{
					conn.close();
					conn = null;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportBranchDataPDF", loginVo.getUserId());
				throw new RuntimeException("Something went wrong please try again");
			}
		}
		return resultSet;
	}
	
	@Override
	public String exportBranchDataCSV(LoginVo loginVo, HttpServletResponse response, BranchMasterVo branchMasterVo) {
		String downloadString = null;
		ResultSet resultSet = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		CSVWriter writer = null;
		
		try
		{
			String call = "{ call "+DBConstants.STP_FETCH_EXPORT_BRANCH_LIST+"(?, ?, ?, ?, ?, ?, ?) }";
			
	        conn = mainDatasource.getConnection();

	        cstmt = conn.prepareCall(call);

	        cstmt.setString(1, branchMasterVo.getSearchCriteria());
	        cstmt.setString(2, branchMasterVo.getSearchBranchCode());
	        cstmt.setString(3, branchMasterVo.getSearchBicCode());
	        cstmt.setString(4, branchMasterVo.getSearchBranchDesc());
	        cstmt.setString(5, branchMasterVo.getSearchBranchType());
	        cstmt.setString(6, branchMasterVo.getSearchBcatBranchId());
	        cstmt.setString(7, loginVo.getUserId());
	        resultSet = cstmt.executeQuery();
	        
	        if(resultSet != null)
			{
	        	downloadString = env.getProperty("download.file.path")+"BranchMaster"+"-"+DateUtil.formatDate(new Date(), "ddMMyyyy")+".csv"; 
				writer = new CSVWriter(new FileWriter(downloadString), ',', CSVWriter.NO_ESCAPE_CHARACTER, System.getProperty("line.separator"));
				
				SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_1);  
			    Date date = new Date();
			    String currentDate = formatter.format(date);
				String  reportDate = "Report Date, "+currentDate;
				writer.writeNext(reportDate.split(","));
				
				writer.writeAll(resultSet, false);
				if(writer != null)
					writer.close();
				
				if(downloadString != null)
				{
					exportData.downloadFileDetailsDeleteCSV(response, downloadString, loginVo.getUserId());			
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportBranchDataCSV", loginVo.getUserId());
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		finally
		{
			try
			{
				if(writer != null)
					writer.close();
				
				if (resultSet != null) {
			    	resultSet.close();
			    	resultSet = null;
				}
				
				if(cstmt != null)
		        {
		        	cstmt.close();
		        	cstmt = null;
		        }
		        
				if(conn != null)
				{
					conn.close();
					conn = null;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportBranchDataCSV", loginVo.getUserId());
				throw new RuntimeException("Something went wrong please try again");
			}
		}
		return downloadString;
	}
	
	
	@Override
	public ResultSet exportCountryDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, CountryMasterVo countryMasterVo) {
		ResultSet resultSet = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		
		try
		{
			String call = "{ call "+DBConstants.STP_FETCH_EXPORT_COUNTRY_LIST+"(?, ?, ?, ?) }";
			
	        conn = mainDatasource.getConnection();

	        cstmt = conn.prepareCall(call);

	        cstmt.setString(1, countryMasterVo.getSearchCriteria());
	        cstmt.setString(2, countryMasterVo.getSearchCountryCode());
	        cstmt.setString(3, countryMasterVo.getSearchCountryName());
	        cstmt.setString(4, loginVo.getUserId());
	        resultSet = cstmt.executeQuery();
	        
	        if(resultSet != null)
			{
	           try {
				   exportData.downloadFileDetailsExcel(response, reportName, resultSet, loginVo.getUserId());
				} catch (Exception e) {
					e.printStackTrace();
					loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportCountryDataExcel", loginVo.getUserId());
					throw new RuntimeException("Something went wrong please try again");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportCountryDataExcel", loginVo.getUserId());
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		finally
		{
			try
			{
				if(cstmt != null)
		        {
		        	cstmt.close();
		        	cstmt = null;
		        }
		        
				if(conn != null)
				{
					conn.close();
					conn = null;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportCountryDataExcel", loginVo.getUserId());
				throw new RuntimeException("Something went wrong please try again");
			}
		}
		return resultSet;
	}
	
	@Override
	public String exportCountryDataCSV(LoginVo loginVo, HttpServletResponse response, CountryMasterVo countryMasterVo) {
		String downloadString = null;
		ResultSet resultSet = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		CSVWriter writer = null;
		
		try
		{
			String call = "{ call "+DBConstants.STP_FETCH_EXPORT_COUNTRY_LIST+"(?, ?, ?, ?) }";
			
	        conn = mainDatasource.getConnection();

	        cstmt = conn.prepareCall(call);

	        cstmt.setString(1, countryMasterVo.getSearchCriteria());
	        cstmt.setString(2, countryMasterVo.getSearchCountryCode());
	        cstmt.setString(3, countryMasterVo.getSearchCountryName());
	        cstmt.setString(4, loginVo.getUserId());
	        resultSet = cstmt.executeQuery();
	        
	        if(resultSet != null)
			{
	        	downloadString = env.getProperty("download.file.path")+"CountryMaster"+"-"+DateUtil.formatDate(new Date(), "ddMMyyyy")+".csv"; 
				writer = new CSVWriter(new FileWriter(downloadString), ',', CSVWriter.NO_ESCAPE_CHARACTER, System.getProperty("line.separator"));
				
				SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_1);  
			    Date date = new Date();
			    String currentDate = formatter.format(date);
				String  reportDate = "Report Date, "+currentDate;
				writer.writeNext(reportDate.split(","));
				
				writer.writeAll(resultSet, false);
				if(writer != null)
					writer.close();
				
				if(downloadString != null)
				{
					exportData.downloadFileDetailsDeleteCSV(response, downloadString, loginVo.getUserId());			
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportCountryDataCSV", loginVo.getUserId());
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		finally
		{
			try
			{
				if(writer != null)
					writer.close();
				
				if (resultSet != null) {
			    	resultSet.close();
			    	resultSet = null;
				}
				
				if(cstmt != null)
		        {
		        	cstmt.close();
		        	cstmt = null;
		        }
		        
				if(conn != null)
				{
					conn.close();
					conn = null;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportCountryDataCSV", loginVo.getUserId());
				throw new RuntimeException("Something went wrong please try again");
			}
		}
		return downloadString;
	}
	
	@Override
	public ResultSet exportCurrencyDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, CurrencyMasterVo currencyMasterVo) {
		ResultSet resultSet = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		
		try
		{
			String call = "{ call "+DBConstants.STP_EXPORT_CURRENCY_LIST+"(?, ?, ?, ?) }";
			
	        conn = mainDatasource.getConnection();

	        cstmt = conn.prepareCall(call);

	        cstmt.setString(1, currencyMasterVo.getSearchCriteria());
	        cstmt.setString(2, currencyMasterVo.getSearchCurrencyCode());
	        cstmt.setString(3, currencyMasterVo.getSearchCurrencyDesc());
	        cstmt.setString(4, loginVo.getUserId());
	        resultSet = cstmt.executeQuery();
	        
	        if(resultSet != null)
			{
	        	String searchparamVals = "Search Criteria|"+currencyMasterVo.getSearchCriteriaTextArray()
				+"|FCY|"+currencyMasterVo.getSearchCurrencyCode()+"#"
				+ "Currency Description|"+currencyMasterVo.getSearchCurrencyDesc()+"| | #";
				
			   try {
				   exportData.downloadFileDetailsExcelWithParams(response,reportName, resultSet, loginVo.getUserId(), searchparamVals, loginVo.getUserName(), loginVo.getBranchDescription());
				} catch (Exception e) {
					e.printStackTrace();
					loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportCurrencyDataExcel", loginVo.getUserId());
					throw new RuntimeException("Something went wrong please try again");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportCurrencyDataExcel", loginVo.getUserId());
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		finally
		{
			try
			{
				if(cstmt != null)
		        {
		        	cstmt.close();
		        	cstmt = null;
		        }
		        
				if(conn != null)
				{
					conn.close();
					conn = null;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportCurrencyDataExcel", loginVo.getUserId());
				throw new RuntimeException("Something went wrong please try again");
			}
		}
		return resultSet;
	}
	
	@Override
	public ResultSet exportCurrencyDataPDF(LoginVo loginVo, HttpServletResponse response, String reportName, CurrencyMasterVo currencyMasterVo) {
		ResultSet resultSet = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		
		try
		{
			String call = "{ call "+DBConstants.STP_EXPORT_CURRENCY_LIST+"(?, ?, ?, ?) }";
			
	        conn = mainDatasource.getConnection();

	        cstmt = conn.prepareCall(call);

	        cstmt.setString(1, currencyMasterVo.getSearchCriteria());
	        cstmt.setString(2, currencyMasterVo.getSearchCurrencyCode());
	        cstmt.setString(3, currencyMasterVo.getSearchCurrencyDesc());
	        cstmt.setString(4, loginVo.getUserId());
	        resultSet = cstmt.executeQuery();
	        
	        if(resultSet != null)
			{
		        String xmlReportFileName = "reconRptMstCurreny.xml";
		      	String reportTitle = "Currency Master";
		      	String reportOutFileName = "CurrencyMaster";
		      	String userName = loginVo.getUserName();
		      	String branchDescription = loginVo.getBranchDescription();
	      	  
		      	GenericReportVO objRptVO = new GenericReportVO();
		      	objRptVO.setReportTitle(reportTitle);
		      	objRptVO.setXmlReportFileName(xmlReportFileName);
		      	objRptVO.setReportOutFileName(reportOutFileName);
			  
		      	objRptVO.setReportName(reportName);
				objRptVO.setRptBranchName(branchDescription);
				objRptVO.setRptUserName(userName);
				
				objRptVO.setSearchCriteriaDesc(currencyMasterVo.getSearchCriteriaTextArray());
				objRptVO.setfCY(currencyMasterVo.getSearchCurrencyCode());
				objRptVO.setfCYDesc(currencyMasterVo.getSearchCurrencyDesc());
				
			   try {
				   exportData.downloadFileDetailsPDF(response, objRptVO, resultSet, loginVo.getUserId(), conn);
				} catch (Exception e) {
					e.printStackTrace();
					loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportCurrencyDataPDF", loginVo.getUserId());
					throw new RuntimeException("Something went wrong please try again");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportCurrencyDataPDF", loginVo.getUserId());
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		finally
		{
			try
			{
				if(cstmt != null)
		        {
		        	cstmt.close();
		        	cstmt = null;
		        }
		        
				if(conn != null)
				{
					conn.close();
					conn = null;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportCurrencyDataPDF", loginVo.getUserId());
				throw new RuntimeException("Something went wrong please try again");
			}
		}
		return resultSet;
	}
	
	@Override
	public String exportCurrencyDataCSV(LoginVo loginVo, HttpServletResponse response, CurrencyMasterVo currencyMasterVo) {
		String downloadString = null;
		ResultSet resultSet = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		CSVWriter writer = null;
		
		try
		{
			String call = "{ call "+DBConstants.STP_EXPORT_CURRENCY_LIST+"(?, ?, ?, ?) }";
			
	        conn = mainDatasource.getConnection();

	        cstmt = conn.prepareCall(call);

	        cstmt.setString(1, currencyMasterVo.getSearchCriteria());
	        cstmt.setString(2, currencyMasterVo.getSearchCurrencyCode());
	        cstmt.setString(3, currencyMasterVo.getSearchCurrencyDesc());
	        cstmt.setString(4, loginVo.getUserId());
	        resultSet = cstmt.executeQuery();
	        
	        if(resultSet != null)
			{
	        	downloadString = env.getProperty("download.file.path")+"CurrencyMaster"+"-"+DateUtil.formatDate(new Date(), "ddMMyyyy")+".csv"; 
				writer = new CSVWriter(new FileWriter(downloadString), ',', CSVWriter.NO_ESCAPE_CHARACTER, System.getProperty("line.separator"));
				
				SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_1);  
			    Date date = new Date();
			    String currentDate = formatter.format(date);
				String  reportDate = "Report Date, "+currentDate;
				writer.writeNext(reportDate.split(","));
				
				writer.writeAll(resultSet, false);
				if(writer != null)
					writer.close();
				
				if(downloadString != null)
				{
					exportData.downloadFileDetailsDeleteCSV(response, downloadString, loginVo.getUserId());			
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportCurrencyDataCSV", loginVo.getUserId());
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		finally
		{
			try
			{
				if(writer != null)
					writer.close();
				
				if (resultSet != null) {
			    	resultSet.close();
			    	resultSet = null;
				}
				
				if(cstmt != null)
		        {
		        	cstmt.close();
		        	cstmt = null;
		        }
		        
				if(conn != null)
				{
					conn.close();
					conn = null;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportCurrencyDataCSV", loginVo.getUserId());
				throw new RuntimeException("Something went wrong please try again");
			}
		}
		return downloadString;
	}
	
	@Override
	public ResultSet exportNostroAcctsDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, NostroAcctsMasterVo nostroAcctsMasterVo) {
		ResultSet resultSet = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		
		try
		{
			String call = "{ call "+DBConstants.STP_FETCH_EXPORT_NOSTRO_LIST+"(?, ?, ?, ?) }";
			
	        conn = mainDatasource.getConnection();

	        cstmt = conn.prepareCall(call);

	        cstmt.setString(1, nostroAcctsMasterVo.getSearchCriteria());
	        cstmt.setString(2, nostroAcctsMasterVo.getSearchNostroAcctsNo());
	        cstmt.setString(3, nostroAcctsMasterVo.getSearchBankCode());
	        cstmt.setString(4, loginVo.getUserId());
	        resultSet = cstmt.executeQuery();
	        
	        if(resultSet != null)
			{
	        	String searchparamVals = "Search Criteria|"+nostroAcctsMasterVo.getSearchCriteriaTextArray()
				+"|Nostro Account No|"+nostroAcctsMasterVo.getSearchNostroAcctsNo()+"#"
				+ "Bank Code|"+nostroAcctsMasterVo.getSearchBankCode()+"| | #";
				
			   try {
				   exportData.downloadFileDetailsExcelWithParams(response,reportName, resultSet, loginVo.getUserId(), searchparamVals, loginVo.getUserName(), loginVo.getBranchDescription());
				} catch (Exception e) {
					e.printStackTrace();
					loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportNostroAcctsDataExcel", loginVo.getUserId());
					throw new RuntimeException("Something went wrong please try again");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportNostroAcctsDataExcel", loginVo.getUserId());
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		finally
		{
			try
			{
				if(cstmt != null)
		        {
		        	cstmt.close();
		        	cstmt = null;
		        }
		        
				if(conn != null)
				{
					conn.close();
					conn = null;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportNostroAcctsDataExcel", loginVo.getUserId());
				throw new RuntimeException("Something went wrong please try again");
			}
		}
		return resultSet;
	}
	
	@Override
	public ResultSet exportNostroAcctsDataPDF(LoginVo loginVo, HttpServletResponse response, String reportName, NostroAcctsMasterVo nostroAcctsMasterVo) {
		ResultSet resultSet = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		
		try
		{
			String call = "{ call "+DBConstants.STP_FETCH_EXPORT_NOSTRO_LIST+"(?, ?, ?, ?) }";
			
	        conn = mainDatasource.getConnection();

	        cstmt = conn.prepareCall(call);

	        cstmt.setString(1, nostroAcctsMasterVo.getSearchCriteria());
	        cstmt.setString(2, nostroAcctsMasterVo.getSearchNostroAcctsNo());
	        cstmt.setString(3, nostroAcctsMasterVo.getSearchBankCode());
	        cstmt.setString(4, loginVo.getUserId());
	        resultSet = cstmt.executeQuery();
	        
	        if(resultSet != null)
			{
	        	String xmlReportFileName = "reconRptMstNastro.xml";
	      	  	String reportTitle = "Nostro Accounts Master";
	      	  	String reportOutFileName = "NostroAccountsMaster";
	      	  
	      	  	String userName = loginVo.getUserName();
	      	  	String branchDescription = loginVo.getBranchDescription();
	      	  
	      	  	GenericReportVO objRptVO = new GenericReportVO();
	      	  	objRptVO.setReportTitle(reportTitle);
	      	  	objRptVO.setXmlReportFileName(xmlReportFileName);
	      	  	objRptVO.setReportOutFileName(reportOutFileName);
		      	objRptVO.setReportName(reportName);
				objRptVO.setRptBranchName(branchDescription);
				objRptVO.setRptUserName(userName);
				objRptVO.setSearchCriteriaDesc(nostroAcctsMasterVo.getSearchCriteriaTextArray());
				objRptVO.setNostroNo(nostroAcctsMasterVo.getSearchNostroAcctsNo());
				objRptVO.setNostroName(nostroAcctsMasterVo.getSearchBankCode());
				
			   try {
				   exportData.downloadFileDetailsPDF(response, objRptVO, resultSet, loginVo.getUserId(), conn);
				} catch (Exception e) {
					e.printStackTrace();
					loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportNostroAcctsDataPDF", loginVo.getUserId());
					throw new RuntimeException("Something went wrong please try again");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportNostroAcctsDataPDF", loginVo.getUserId());
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		finally
		{
			try
			{
				if(cstmt != null)
		        {
		        	cstmt.close();
		        	cstmt = null;
		        }
		        
				if(conn != null)
				{
					conn.close();
					conn = null;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportNostroAcctsDataPDF", loginVo.getUserId());
				throw new RuntimeException("Something went wrong please try again");
			}
		}
		return resultSet;
	}
	
	@Override
	public String exportNostroAcctsDataCSV(LoginVo loginVo, HttpServletResponse response, NostroAcctsMasterVo nostroAcctsMasterVo) {
		String downloadString = null;
		ResultSet resultSet = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		CSVWriter writer = null;
		
		try
		{
			String call = "{ call "+DBConstants.STP_FETCH_EXPORT_NOSTRO_LIST+"(?, ?, ?, ?) }";
			
	        conn = mainDatasource.getConnection();

	        cstmt = conn.prepareCall(call);

	        cstmt.setString(1, nostroAcctsMasterVo.getSearchCriteria());
	        cstmt.setString(2, nostroAcctsMasterVo.getSearchNostroAcctsNo());
	        cstmt.setString(3, nostroAcctsMasterVo.getSearchBankCode());
	        cstmt.setString(4, loginVo.getUserId());
	        resultSet = cstmt.executeQuery();
	        
	        if(resultSet != null)
			{
	        	downloadString = env.getProperty("download.file.path")+"NostroAcctsMaster"+"-"+DateUtil.formatDate(new Date(), "ddMMyyyy")+".csv";			
				writer = new CSVWriter(new FileWriter(downloadString), ',', CSVWriter.NO_ESCAPE_CHARACTER, System.getProperty("line.separator"));			
				
				SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_1);  
			    Date date = new Date();
			    String currentDate = formatter.format(date);
				String  reportDate = "Report Date, "+currentDate;
				writer.writeNext(reportDate.split(","));
				
				writer.writeAll(resultSet, false);
				if(writer != null)
					writer.close();
				
				if(downloadString != null)
				{
					exportData.downloadFileDetailsDeleteCSV(response, downloadString, loginVo.getUserId());			
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportNostroAcctsDataCSV", loginVo.getUserId());
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		finally
		{
			try
			{
				if(writer != null)
					writer.close();
				
				if (resultSet != null) {
			    	resultSet.close();
			    	resultSet = null;
				}
				
				if(cstmt != null)
		        {
		        	cstmt.close();
		        	cstmt = null;
		        }
		        
				if(conn != null)
				{
					conn.close();
					conn = null;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportNostroAcctsDataCSV", loginVo.getUserId());
				throw new RuntimeException("Something went wrong please try again");
			}
		}
		return downloadString;
	}
	
	
	@Override
	public String exportLookupDataCSV(LoginVo loginVo, HttpServletResponse response, LookupMasterVo lookupMasterVo) {
		String downloadString = null;
		ResultSet resultSet = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		CSVWriter writer = null;
		
		try
		{
			String call = "{ call "+DBConstants.STP_EXPORT_LOOKUP_LIST+"(?, ?, ?, ?, ?) }";
			
	        conn = mainDatasource.getConnection();

	        cstmt = conn.prepareCall(call);

	        cstmt.setString(1, lookupMasterVo.getSearchCriteria());
	        cstmt.setString(2, lookupMasterVo.getSearchLookupType());
	        cstmt.setString(3, lookupMasterVo.getSearchLookupCode());
	        cstmt.setString(4, lookupMasterVo.getSearchLookupDesc());
	        cstmt.setString(5, loginVo.getUserId());
	        resultSet = cstmt.executeQuery();
	        
	        if(resultSet != null)
			{
	        	downloadString = env.getProperty("download.file.path")+"LookupMaster"+"-"+DateUtil.formatDate(new Date(), "ddMMyyyy")+".csv"; 
				writer = new CSVWriter(new FileWriter(downloadString), ',', CSVWriter.NO_ESCAPE_CHARACTER, System.getProperty("line.separator"));
				
				SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_1);  
			    Date date = new Date();
			    String currentDate = formatter.format(date);
				String  reportDate = "Report Date, "+currentDate;
				writer.writeNext(reportDate.split(","));
				
				writer.writeAll(resultSet, false);
				if(writer != null)
					writer.close();
				
				if(downloadString != null)
				{
					exportData.downloadFileDetailsDeleteCSV(response, downloadString, loginVo.getUserId());			
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportLookupDataCSV", loginVo.getUserId());
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		finally
		{
			try
			{
				if(writer != null)
					writer.close();
				
				if (resultSet != null) {
			    	resultSet.close();
			    	resultSet = null;
				}
				
				if(cstmt != null)
		        {
		        	cstmt.close();
		        	cstmt = null;
		        }
		        
				if(conn != null)
				{
					conn.close();
					conn = null;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportLookupDataCSV", loginVo.getUserId());
				throw new RuntimeException("Something went wrong please try again");
			}
		}
		return downloadString;
	}
	
	@Override
	public ResultSet exportLookupDataExcel(LoginVo loginVo, HttpServletResponse response, String reportName, LookupMasterVo lookupMasterVo) {
		ResultSet resultSet = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		
		try
		{
			String call = "{ call "+DBConstants.STP_EXPORT_LOOKUP_LIST+"(?, ?, ?, ?, ?) }";
			
	        conn = mainDatasource.getConnection();

	        cstmt = conn.prepareCall(call);

	        cstmt.setString(1, lookupMasterVo.getSearchCriteria());
	        cstmt.setString(2, lookupMasterVo.getSearchLookupType());
	        cstmt.setString(3, lookupMasterVo.getSearchLookupCode());
	        cstmt.setString(4, lookupMasterVo.getSearchLookupDesc());
	        cstmt.setString(5, loginVo.getUserId());
	        resultSet = cstmt.executeQuery();
	        
	        if(resultSet != null)
			{
	           try {
				   exportData.downloadFileDetailsExcel(response, reportName, resultSet, loginVo.getUserId());
				} catch (Exception e) {
					e.printStackTrace();
					loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportLookupDataExcel", loginVo.getUserId());
					throw new RuntimeException("Something went wrong please try again");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportLookupDataExcel", loginVo.getUserId());
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		finally
		{
			try
			{
				if(cstmt != null)
		        {
		        	cstmt.close();
		        	cstmt = null;
		        }
		        
				if(conn != null)
				{
					conn.close();
					conn = null;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "CommonExportDaoImpl-exportLookupDataExcel", loginVo.getUserId());
				throw new RuntimeException("Something went wrong please try again");
			}
		}
		return resultSet;
	}
	
}
