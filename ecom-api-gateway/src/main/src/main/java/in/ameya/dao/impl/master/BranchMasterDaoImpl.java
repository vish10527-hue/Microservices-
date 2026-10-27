package in.ameya.dao.impl.master;

import java.io.FileWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import au.com.bytecode.opencsv.CSVWriter;
import in.ameya.dao.master.BranchMasterDao;
import in.ameya.model.master.BranchMasterVo;
import in.ameya.model.master.LoginVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.DBConstants;
import in.ameya.util.DateUtil;
import in.ameya.util.ExportData;
import in.ameya.util.LoggerUtil;
import jakarta.servlet.http.HttpServletResponse;

@PropertySource(value="classpath:filePath.properties")
@PropertySource(value="classpath:messages.properties")
@Repository
public class BranchMasterDaoImpl extends JdbcDaoSupport implements BranchMasterDao{
		
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private Environment env;
	
	@Autowired
	ExportData exportData;	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<BranchMasterVo> getBranchMasterList(BranchMasterVo branchMasterVo, String userId) {
		ArrayList<BranchMasterVo> branchMasterList = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_BRANCH_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IPAGE_NO", Types.INTEGER),
					new SqlParameter("@IPAGE_SIZE", Types.INTEGER),
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@IBRANCH_CODE", Types.VARCHAR),
					new SqlParameter("@IBIC_CODE", Types.VARCHAR),
					new SqlParameter("@IBRANCH_DESC", Types.VARCHAR),
					new SqlParameter("@IBRANCH_TYPE", Types.CHAR),
					new SqlParameter("@IBCAT_BRANCH_ID", Types.INTEGER),
					new SqlParameter("@IUSER_ID", Types.INTEGER),
					new SqlParameter("@IORDER_BY_COLUMN", Types.VARCHAR),
					new SqlParameter("@IORDER_ASC_DESC", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IPAGE_NO", branchMasterVo.getPageNo())
					.addValue("@IPAGE_SIZE", branchMasterVo.getPageSize())
					.addValue("@ISEARCH_FOR", branchMasterVo.getSearchCriteria())
					.addValue("@IBRANCH_CODE", ("").equalsIgnoreCase(branchMasterVo.getSearchBranchCode()) ? null : branchMasterVo.getSearchBranchCode())
					.addValue("@IBIC_CODE", ("").equalsIgnoreCase(branchMasterVo.getSearchBicCode()) ? null : branchMasterVo.getSearchBicCode())
					.addValue("@IBRANCH_DESC", ("").equalsIgnoreCase(branchMasterVo.getSearchBranchDesc()) ? null : branchMasterVo.getSearchBranchDesc())
					.addValue("@IBRANCH_TYPE", ("").equalsIgnoreCase(branchMasterVo.getSearchBranchType()) ? null : branchMasterVo.getSearchBranchType())
					.addValue("@IBCAT_BRANCH_ID", ("").equalsIgnoreCase(branchMasterVo.getSearchBcatBranchId()) ? null : branchMasterVo.getSearchBcatBranchId())
					.addValue("@IUSER_ID", userId)
					.addValue("@IORDER_BY_COLUMN", (("").equalsIgnoreCase(branchMasterVo.getOrderByColumn()) ? null : branchMasterVo.getOrderByColumn()))
					.addValue("@IORDER_ASC_DESC", (("").equalsIgnoreCase(branchMasterVo.getOrderAscDesc()) ? null : branchMasterVo.getOrderAscDesc()));
			
			simpleJdbcCall.returningResultSet("getBranchMasterList", new RowMapper<BranchMasterVo>() {
				
				@Override
				public BranchMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						BranchMasterVo branchMasterVo = new BranchMasterVo();
						branchMasterVo.setBranchId(rs.getString("BRANCH_ID"));
						branchMasterVo.setBranchCode(rs.getString("BRANCH_CODE"));
						branchMasterVo.setBranchDesc(rs.getString("BRANCH_DESCRIPTION"));	
						branchMasterVo.setBranchType(rs.getString("BRANCH_TYPE"));
						branchMasterVo.setBcatBranchDesc(rs.getString("BCAT_BRANCH_CODE") +"-"+rs.getString("BCAT_BRANCH_DESCRIPTION"));
						branchMasterVo.setBicCode(rs.getString("BIC_CODE"));
						branchMasterVo.setStatus(rs.getString("STATUS"));
						branchMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
						branchMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
						branchMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
						branchMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
						branchMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
						branchMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
						branchMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
						branchMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
						branchMasterVo.setRecordCount(rs.getInt("TOT_COUNT"));
						
						return branchMasterVo;					
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				branchMasterList = (ArrayList<BranchMasterVo>) results.get("getBranchMasterList");				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "BranchMasterDaoImpl-getBranchMasterList", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return branchMasterList;
	}
	
	
	@Override
	public String fetchExportBranchMasterCSV(BranchMasterVo branchMasterVo, String userId) {
		String downloadString = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_EXPORT_BRANCH_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@IBRANCH_CODE", Types.VARCHAR),
					new SqlParameter("@IBIC_CODE", Types.VARCHAR),
					new SqlParameter("@IBRANCH_DESC", Types.VARCHAR),
					new SqlParameter("@IBRANCH_TYPE", Types.CHAR),
					new SqlParameter("@IBCAT_BRANCH_ID", Types.INTEGER),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ISEARCH_FOR", branchMasterVo.getSearchCriteria())
					.addValue("@IBRANCH_CODE", branchMasterVo.getBranchCode())
					.addValue("@IBIC_CODE", branchMasterVo.getBicCode())
					.addValue("@IBRANCH_DESC", branchMasterVo.getBranchDesc())
					.addValue("@IBRANCH_TYPE", branchMasterVo.getBranchType())
					.addValue("@IBCAT_BRANCH_ID", branchMasterVo.getBcatBranchId())
					.addValue("@IUSER_ID", userId);
			
			simpleJdbcCall.returningResultSet("fetchExportBranchMasterCSV", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					CSVWriter writer = null;
					String downloadStringNew = null;
					try
					{
						downloadStringNew = env.getProperty("download.file.path") + "BranchMaster" + "-" + DateUtil.formatDate(new Date(), "ddMMyyyy") + ".csv";
						writer = new CSVWriter(new FileWriter(downloadStringNew), '|', CSVWriter.NO_ESCAPE_CHARACTER, System.getProperty("line.separator"));
						writer.writeAll(rs, false);
						writer.close();
					}
					catch(Exception e)
					{
						e.printStackTrace();
						loggerUtil.logErrorInsert(e.getMessage(), e, "BGMasterDaoImpl-ExportCSVRowmapper", "");
					}
					return downloadStringNew;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				downloadString = (String) results.get("fetchExportBranchMasterCSV");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "BranchMasterDaoImpl-fetchExportBranchMasterCSV", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return downloadString;
	}
	
	@Override
	public ResultSet fetchExportBranchMasterExcel(LoginVo loginVo,BranchMasterVo branchMasterVo, String userId, HttpServletResponse response) {
		ResultSet resultSet = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		
		try
		{
			String call = "{ call "+DBConstants.STP_FETCH_EXPORT_BRANCH_LIST+"(?, ?, ?, ?, ?, ?, ?) }";
			
	        conn = mainDatasource.getConnection();

	        cstmt = conn.prepareCall(call);

	        cstmt.setString(1, branchMasterVo.getSearchCriteria());
	        cstmt.setString(2, branchMasterVo.getBranchCode());
	        cstmt.setString(3, branchMasterVo.getBicCode());
	        cstmt.setString(4, branchMasterVo.getBranchDesc());
	        cstmt.setString(5, branchMasterVo.getBranchType());
	        cstmt.setString(6, branchMasterVo.getBcatBranchId());
	        cstmt.setString(7, userId);
	        resultSet = cstmt.executeQuery();
	        
	        if(resultSet != null)
			{
	        	String sbufBCatBranchId = branchMasterVo.getSearchCriteria();
				
	        	String searchparamVals = "Search Criteria|"+""+"|Sol Id|"+branchMasterVo.getBranchCode()+"#"+ "Type|"+branchMasterVo.getBranchTypeTextArray()
						+"|BCat branch Id|"+sbufBCatBranchId+"#"+"BIC Code|"+branchMasterVo.getBicCode()+"|Branch Description|"+branchMasterVo.getBranchDesc()+"#";
			   try {
				   exportData.downloadFileDetailsExcelWithParams(response,"Branch_Master", resultSet, userId, searchparamVals, loginVo.getUserName(), loginVo.getBranchDescription());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					loggerUtil.logErrorInsert(e.getMessage(), e, "BGMasterDaoImpl-fetchExportBranchMasterExcel", userId);
					throw new RuntimeException("Something went wrong please try again");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "BranchMasterDaoImpl-fetchExportBranchMasterExcel", userId);
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
				loggerUtil.logErrorInsert(e.getMessage(), e, "BranchMasterDaoImpl-fetchExportBranchMasterExcel", userId);
				throw new RuntimeException("Something went wrong please try again");
			}
		}
		return resultSet;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public BranchMasterVo getBranchMasterDetails(String selRec, String userId) {
		BranchMasterVo branchMasterVoNew = new BranchMasterVo();
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_BRANCH_DETAILS);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IBRANCH_CODE", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IBRANCH_CODE", selRec);
			
			simpleJdbcCall.returningResultSet("getBranchMasterDetails", new RowMapper<BranchMasterVo>() {
				
				@Override
				public BranchMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					BranchMasterVo branchMasterVo = new BranchMasterVo();
					
					branchMasterVo.setBranchId(rs.getString("BRANCH_ID"));
					branchMasterVo.setBranchCode(rs.getString("BRANCH_CODE"));
					branchMasterVo.setBranchDesc(rs.getString("BRANCH_DESCRIPTION"));
					branchMasterVo.setBranchZone(rs.getString("ZONE"));
					branchMasterVo.setBranchState(rs.getString("STATE"));
					branchMasterVo.setEmailId(rs.getString("EMAIL_ID"));
					branchMasterVo.setBranchAddress(rs.getString("BRANCH_ADDRESS"));
					String isHoApprove = rs.getString("IS_HO_APPROVED");
					if(isHoApprove != null)
					{
						if(isHoApprove.equalsIgnoreCase("true"))
							isHoApprove = "1";
						else
							isHoApprove = "0";
					}
					branchMasterVo.sethOApproved(isHoApprove);
					branchMasterVo.setAdCode(rs.getString("AD_CODE"));
					branchMasterVo.setName1(rs.getString("NAME_1"));
					branchMasterVo.setJobFamily1(rs.getString("JOB_FAMILY_1"));
					branchMasterVo.setMobNo1(rs.getString("MOBILE_1"));
					branchMasterVo.setName2(rs.getString("NAME_2"));
					branchMasterVo.setJobFamily2(rs.getString("JOB_FAMILY_2"));
					branchMasterVo.setMobNo2(rs.getString("MOBILE_2"));
					branchMasterVo.setLandLine(rs.getString("LANDLINE"));					
					branchMasterVo.setBranchType(rs.getString("BRANCH_TYPE"));
					branchMasterVo.setBcatBranchId(rs.getString("BCAT_BRANCH_ID"));
					branchMasterVo.setBcatBranchDesc(rs.getString("BCAT_BRANCH_DESCRIPTION"));
					branchMasterVo.setBicCode(rs.getString("BIC_CODE"));
					branchMasterVo.setStatus(rs.getString("STATUS"));
					branchMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
					branchMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
					branchMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
					branchMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
					branchMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
					branchMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
					branchMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
					branchMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
					
					return branchMasterVo;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<BranchMasterVo> BranchDetailsList = (List<BranchMasterVo>) results.get("getBranchMasterDetails");
				if(BranchDetailsList != null && BranchDetailsList.size() > 0)
				{
					branchMasterVoNew = BranchDetailsList.get(0);
				}				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "BranchMasterDaoImpl-getBranchMasterDetails", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return branchMasterVoNew;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public BranchMasterVo manageBranchMaster(BranchMasterVo branchMasterVo, String userId) {
		BranchMasterVo branchMasterVoNew = new BranchMasterVo();
		try
		{
			branchMasterVoNew.setErrorCode("1");
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_MANAGE_BRANCH);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IBRANCH_ID", Types.INTEGER),
					new SqlParameter("@IBRANCH_CODE", Types.VARCHAR),
					new SqlParameter("@BIC_CODE", Types.VARCHAR),
					new SqlParameter("@IBRANCH_DESCRIPTION", Types.VARCHAR),
					new SqlParameter("@STATE", Types.VARCHAR),
					new SqlParameter("@ZONE", Types.VARCHAR),
					new SqlParameter("@EMAIL_ID", Types.VARCHAR),
					new SqlParameter("@BRANCH_ADDRESS", Types.VARCHAR),
					new SqlParameter("@IS_HO_APPROVED", Types.BIT),
					new SqlParameter("@AD_CODE", Types.VARCHAR),
					new SqlParameter("@NAME_1", Types.VARCHAR),
					new SqlParameter("@JOB_FAMILY_1", Types.VARCHAR),
					new SqlParameter("@MOBILE_1", Types.VARCHAR),
					new SqlParameter("@NAME_2", Types.VARCHAR),
					new SqlParameter("@JOB_FAMILY_2", Types.VARCHAR),
					new SqlParameter("@MOBILE_2", Types.VARCHAR),
					new SqlParameter("@LANDLINE", Types.VARCHAR),
					new SqlParameter("@IBRANCH_TYPE", Types.CHAR),
					new SqlParameter("@IBCAT_BRANCH_ID", Types.INTEGER),
					new SqlParameter("@ISTATUS", Types.INTEGER),
					new SqlParameter("@IUSER_ID", Types.INTEGER)
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IBRANCH_ID",branchMasterVo.getBranchId())
					.addValue("@IBRANCH_CODE", branchMasterVo.getBranchCode())
					.addValue("@BIC_CODE", branchMasterVo.getBicCode())
					.addValue("@IBRANCH_DESCRIPTION", branchMasterVo.getBranchDesc())
					.addValue("@STATE", branchMasterVo.getBranchState())
					.addValue("@ZONE", branchMasterVo.getBranchZone())
					.addValue("@EMAIL_ID", branchMasterVo.getEmailId())
					.addValue("@BRANCH_ADDRESS", branchMasterVo.getBranchAddress())
					.addValue("@IS_HO_APPROVED", branchMasterVo.gethOApproved())
					.addValue("@AD_CODE", branchMasterVo.getAdCode())
					.addValue("@NAME_1", branchMasterVo.getName1())
					.addValue("@JOB_FAMILY_1", branchMasterVo.getJobFamily1())
					.addValue("@MOBILE_1", branchMasterVo.getMobNo1())
					.addValue("@NAME_2", branchMasterVo.getName2())
					.addValue("@JOB_FAMILY_2", branchMasterVo.getJobFamily2())
					.addValue("@MOBILE_2", branchMasterVo.getMobNo2())
					.addValue("@LANDLINE", branchMasterVo.getLandLine())
					.addValue("@IBRANCH_TYPE", branchMasterVo.getBranchType())
					.addValue("@IBCAT_BRANCH_ID", branchMasterVo.getBcatBranchId())
					.addValue("@ISTATUS", branchMasterVo.getStatus())
					.addValue("@IUSER_ID", userId);
			
			simpleJdbcCall.returningResultSet("manageBranchMaster", new RowMapper<BranchMasterVo>() {
				
				@Override
				public BranchMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					BranchMasterVo branchMasterVo1 = new BranchMasterVo();
				
						String message = rs.getString(1);	
						branchMasterVo1.setErrorDesc(message);
					return branchMasterVo1;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<BranchMasterVo> branchMasterList = (List<BranchMasterVo>) results.get("manageBranchMaster");
				if(branchMasterList != null && branchMasterList.size() > 0)
				{
					branchMasterVoNew = branchMasterList.get(0);
					branchMasterVoNew.setErrorCode("1");
				}
			}
		}		
		catch(Exception e)
		{
			e.printStackTrace();
			String strErrorMsg = e.getMessage();
			strErrorMsg = strErrorMsg.split("\n")[0]; 
			if(strErrorMsg.indexOf("-20000") > -1 || strErrorMsg.indexOf("-20001") > -1)
			{
				strErrorMsg = strErrorMsg.substring(strErrorMsg.lastIndexOf("-2000") + 7).trim();
				branchMasterVoNew.setErrorDesc(strErrorMsg);
			}
			else
			{
				branchMasterVoNew.setErrorCode("-1");
				loggerUtil.logErrorInsert(e.getMessage(), e, "BranchMasterDaoImpl-manageBranchMaster", userId);
				throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
			}
		}
		return branchMasterVoNew;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public BranchMasterVo checkBranchMaster(String selRec, String appRejRemark, String checkType, String userId) {
		BranchMasterVo branchMasterVoNew = new BranchMasterVo();
		try
		{
			branchMasterVoNew.setErrorCode("1");
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_CHECK_BRANCH);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IBRANCH_IDS", Types.VARCHAR),
					new SqlParameter("@IAPP_REJ_REMARKS", Types.VARCHAR),
					new SqlParameter("@ICHECK_STATUS", Types.INTEGER),
					new SqlParameter("@ICM_USER_ID", Types.INTEGER)				
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IBRANCH_IDS", selRec)
					.addValue("@IAPP_REJ_REMARKS", appRejRemark)
					.addValue("@ICHECK_STATUS", checkType)
					.addValue("@ICM_USER_ID", userId);
			
			simpleJdbcCall.returningResultSet("checkBranchMaster", new RowMapper<BranchMasterVo>() {
				
				@Override
				public BranchMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					BranchMasterVo branchMasterVo1 = new BranchMasterVo();
					String message = rs.getString(1);	
					branchMasterVo1.setErrorDesc(message);
					
					return branchMasterVo1;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<BranchMasterVo> branchMasterList = (List<BranchMasterVo>) results.get("manageBranchMaster");
				if(branchMasterList != null && branchMasterList.size() > 0)
				{
					branchMasterVoNew = branchMasterList.get(0);
					branchMasterVoNew.setErrorCode("1");
				}
			}
		}
		catch(Exception e)
		{
			String strErrorMsg = e.getMessage();
			strErrorMsg = strErrorMsg.split("\n")[0]; 
			if(strErrorMsg.indexOf("-20000") > -1 || strErrorMsg.indexOf("-20001") > -1)
			{
				strErrorMsg = strErrorMsg.substring(strErrorMsg.lastIndexOf("-2000") + 7).trim();
				branchMasterVoNew.setErrorDesc(strErrorMsg);
			}
			else
			{
				branchMasterVoNew.setErrorCode("-1");
				loggerUtil.logErrorInsert(e.getMessage(), e, "BranchMasterDaoImpl-checkBranchMaster", userId);
				branchMasterVoNew.setErrorDesc(env.getProperty("app.common.error.invalid.request"));
				throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
			}
		}
		return branchMasterVoNew;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<BranchMasterVo> getBranchListPopup(BranchMasterVo branchMasterVo, String userId) {
		ArrayList<BranchMasterVo> branchMasterList = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_BRANCH_POPUP_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IPAGE_NO", Types.INTEGER),
					new SqlParameter("@IPAGE_SIZE", Types.INTEGER),
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@IBRANCH_CODE", Types.VARCHAR),
					new SqlParameter("@IBRANCH_DESC", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IPAGE_NO", branchMasterVo.getPageNo())
					.addValue("@IPAGE_SIZE", branchMasterVo.getPageSize())
					.addValue("@ISEARCH_FOR", branchMasterVo.getSearchCriteria())
					.addValue("@IBRANCH_CODE", branchMasterVo.getBranchCode())
					.addValue("@IBRANCH_DESC", branchMasterVo.getBranchDesc())
					.addValue("@IUSER_ID", userId);
			
			simpleJdbcCall.returningResultSet("getBranchListPopup", new RowMapper<ArrayList<BranchMasterVo>>() {
				
				@Override
				public ArrayList<BranchMasterVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<BranchMasterVo> branchMasterListNew = new ArrayList<BranchMasterVo>();
					while(rs.next())
					{
						BranchMasterVo branchMasterVo = new BranchMasterVo();
						branchMasterVo.setBranchId(rs.getString("BRANCH_ID"));
						branchMasterVo.setBranchCode(rs.getString("BRANCH_CODE"));
						branchMasterVo.setBranchDesc(rs.getString("BRANCH_DESCRIPTION"));
						branchMasterListNew.add(branchMasterVo);
					}					
					return branchMasterListNew;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				branchMasterList = (ArrayList<BranchMasterVo>) results.get("getBranchListPopup");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "BranchMasterDaoImpl-getBranchListPopup", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return branchMasterList;
	}
	
	
}
