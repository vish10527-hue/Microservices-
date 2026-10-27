package in.ameya.dao.impl.master;

import java.io.FileWriter;
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
import in.ameya.dao.master.LookupMasterDao;
import in.ameya.model.master.LookupMasterVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.DBConstants;
import in.ameya.util.DateUtil;
import in.ameya.util.LoggerUtil;

@PropertySource(value="classpath:filePath.properties")
@Repository
public class LookupMasterDaoImpl extends JdbcDaoSupport implements LookupMasterDao{
		
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private Environment env;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<LookupMasterVo> getLookupMasterList(LookupMasterVo lookupMasterVo, String userKey) {
		ArrayList<LookupMasterVo> lookupMasterList = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_LOOKUP_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IPAGE_NO", Types.INTEGER),
					new SqlParameter("@IPAGE_SIZE", Types.INTEGER),
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@ILOOKUP_TYPE", Types.VARCHAR),
					new SqlParameter("@ILOOKUP_CODE", Types.VARCHAR),
					new SqlParameter("@ILOOKUP_DESCRIPTION", Types.VARCHAR),					
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IPAGE_NO", lookupMasterVo.getPageNo())
					.addValue("@IPAGE_SIZE", lookupMasterVo.getPageSize())
					.addValue("@ISEARCH_FOR", lookupMasterVo.getSearchCriteria())
					.addValue("@ILOOKUP_TYPE", lookupMasterVo.getSearchLookupType())
					.addValue("@ILOOKUP_CODE", lookupMasterVo.getSearchLookupCode())
					.addValue("@ILOOKUP_DESCRIPTION", lookupMasterVo.getSearchLookupDesc())
					.addValue("@IUSER_ID", userKey);
			
			simpleJdbcCall.returningResultSet("getLookupMasterList", new RowMapper<LookupMasterVo>() {
				
				@Override
				public LookupMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					LookupMasterVo lookupMasterVo = new LookupMasterVo();
					try
					{
						lookupMasterVo.setLookupKey(rs.getString("LOOKUP_ID"));
						lookupMasterVo.setLookupCode(rs.getString("LOOKUP_CODE"));
						lookupMasterVo.setLookupDesc(rs.getString("LOOKUP_DESCRIPTION"));
						lookupMasterVo.setStatus(rs.getString("STATUS"));
						lookupMasterVo.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));	
						lookupMasterVo.setApprovedFlag(rs.getString("APPROVED_FLAG"));
						lookupMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
						lookupMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
						lookupMasterVo.setReviewedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
						lookupMasterVo.setCreatedOn(rs.getString("CREATED_ON"));
						lookupMasterVo.setModifiedOn(rs.getString("MODIFIED_ON"));
						lookupMasterVo.setReviewedOn(rs.getString("CHECKED_ON"));
						lookupMasterVo.setRecordCount(rs.getInt("TOT_COUNT"));
						
						return lookupMasterVo;
					}
					catch(Exception e)
					{
						e.printStackTrace();
						throw new RuntimeException("Something went wrong please try again");
					}
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				lookupMasterList = (ArrayList<LookupMasterVo>) results.get("getLookupMasterList");
				if(lookupMasterList != null && lookupMasterList.size() > 0)
				{
					System.out.println("loginList->"+lookupMasterList.size());
					//loginVoNew = loginList.get(0);
					//System.out.println("getLookupKey-->"+loginVoNew.getLookupKey());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "LookupMasterDaoImpl-getLookupMasterList", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return lookupMasterList;
	}
	
	
	@Override
	public String fetchExportLookupMasterCSV(LookupMasterVo lookupMasterVo, String userKey) {
		String downloadString = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_EXPORT_LOOKUP_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@ILOOKUP_TYPE", Types.INTEGER),
					new SqlParameter("@ILOOKUP_CODE", Types.VARCHAR),
					new SqlParameter("@ILOOKUP_DESCRIPTION", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ISEARCH_FOR", lookupMasterVo.getSearchCriteria())
					.addValue("@ILOOKUP_TYPE", lookupMasterVo.getLookupType())
					.addValue("@ILOOKUP_CODE", lookupMasterVo.getLookupCode())
					.addValue("@ILOOKUP_DESCRIPTION", lookupMasterVo.getLookupDesc())
					.addValue("@IUSER_ID", userKey);
			
			simpleJdbcCall.returningResultSet("fetchExportLookupMasterCSV", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					CSVWriter writer = null;
					String downloadStringNew = null;
					try
					{
						downloadStringNew = env.getProperty("download.file.path") + "LookupMaster" + "-" + DateUtil.formatDate(new Date(), "ddMMyyyy") + ".csv";
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
				downloadString = (String) results.get("fetchExportLookupMasterCSV");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "LookupMasterDaoImpl-fetchExportLookupMasterCSV", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return downloadString;
	}
	
	
	@Override
	public ResultSet fetchExportLookupMasterExcel(LookupMasterVo lookupMasterVo, String userKey) {
		ResultSet  resultSet = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_EXPORT_LOOKUP_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@ILOOKUP_TYPE", Types.INTEGER),
					new SqlParameter("@ILOOKUP_CODE", Types.VARCHAR),
					new SqlParameter("@ILOOKUP_DESCRIPTION", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ISEARCH_FOR", lookupMasterVo.getSearchCriteria())
					.addValue("@ILOOKUP_TYPE", lookupMasterVo.getLookupType())
					.addValue("@ILOOKUP_CODE", lookupMasterVo.getLookupCode())
					.addValue("@ILOOKUP_DESCRIPTION", lookupMasterVo.getLookupDesc())
					.addValue("@IUSER_ID", userKey);
			
			simpleJdbcCall.returningResultSet("fetchExportLookupMasterExcel", new RowMapper<ResultSet>() {
				
				@Override
				public ResultSet mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					return rs;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				resultSet = (ResultSet) results.get("fetchExportLookupMasterExcel");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "LookupMasterDaoImpl-fetchExportLookupMasterExcel", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return resultSet;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public LookupMasterVo getLookupMasterDetails(String selRec, String userKey) {
		LookupMasterVo lookupMasterVoNew = new LookupMasterVo();
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_LOOKUP_DETAILS);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ILOOKUP_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ILOOKUP_ID", selRec);
			
			simpleJdbcCall.returningResultSet("getLookupMasterDetails", new RowMapper<LookupMasterVo>() {
				
				@Override
				public LookupMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					LookupMasterVo lookupMasterVo = new LookupMasterVo();
					
					lookupMasterVo.setLookupKey(rs.getString("LOOKUP_ID"));
					lookupMasterVo.setLookupCode(rs.getString("LOOKUP_CODE"));
					lookupMasterVo.setLookupType(rs.getString("LOOKUP_TYPE"));
					lookupMasterVo.setLookupTypeDesc(rs.getString("LOOKUP_TYPE_DESC"));
					lookupMasterVo.setLookupDesc(rs.getString("LOOKUP_DESCRIPTION"));
					lookupMasterVo.setStatus(rs.getString("STATUS"));
					lookupMasterVo.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));	
					lookupMasterVo.setApprovedFlag(rs.getString("APPROVED_FLAG"));
					lookupMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
					lookupMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
					lookupMasterVo.setReviewedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
					lookupMasterVo.setCreatedOn(rs.getString("CREATED_ON"));
					lookupMasterVo.setModifiedOn(rs.getString("MODIFIED_ON"));
					lookupMasterVo.setReviewedOn(rs.getString("CHECKED_ON"));					
					
					return lookupMasterVo;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<LookupMasterVo> LookupDetailsList = (List<LookupMasterVo>) results.get("getLookupMasterDetails");
				if(LookupDetailsList != null && LookupDetailsList.size() > 0)
				{
					lookupMasterVoNew = LookupDetailsList.get(0);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "LookupMasterDaoImpl-getLookupMasterDetails", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return lookupMasterVoNew;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public LookupMasterVo manageLookupMaster(String manageType, LookupMasterVo lookupMasterVo, String userKey) {
		LookupMasterVo lookupMasterVo1 = new LookupMasterVo();
		try
		{
			lookupMasterVo1.setErrorCode("1");
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_MANAGE_LOOKUPS);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ILOOKUP_ID", Types.INTEGER),
					new SqlParameter("@ILOOKUP_CODE", Types.VARCHAR),
					new SqlParameter("@ILOOKUP_TYPE", Types.INTEGER),
					new SqlParameter("@ILOOKUP_DESCRIPTION", Types.VARCHAR),
					new SqlParameter("@IRESERVED_FLAG", Types.INTEGER),
					new SqlParameter("@ISTATUS", Types.INTEGER),
					new SqlParameter("@IUSER_ID", Types.INTEGER)				
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ILOOKUP_ID", lookupMasterVo.getLookupKey())
					.addValue("@ILOOKUP_CODE", lookupMasterVo.getLookupCode())
					.addValue("@ILOOKUP_TYPE", lookupMasterVo.getLookupType())
					.addValue("@ILOOKUP_DESCRIPTION", lookupMasterVo.getLookupDesc())
					.addValue("@IRESERVED_FLAG", lookupMasterVo.getReservedFlag())
					.addValue("@ISTATUS", lookupMasterVo.getStatus())
					.addValue("@IUSER_ID", userKey);
			
			simpleJdbcCall.returningResultSet("manageLookupMaster", new RowMapper<LookupMasterVo>() {
				
				@Override
				public LookupMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					LookupMasterVo LookupMasterVoNew = new LookupMasterVo();
					String message = rs.getString(1);	
					LookupMasterVoNew.setErrorDesc(message);
					return LookupMasterVoNew;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<LookupMasterVo> LookupDetailsList = (List<LookupMasterVo>) results.get("manageLookupMaster");
				if(LookupDetailsList != null && LookupDetailsList.size() > 0)
				{
					lookupMasterVo1 = LookupDetailsList.get(0);
					lookupMasterVo1.setErrorCode("1");
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
				lookupMasterVo1.setErrorDesc(strErrorMsg);
			}
			else
			{
				lookupMasterVo1.setErrorCode("-1");
				loggerUtil.logErrorInsert(e.getMessage(), e, "LookupMasterDaoImpl-manageLookupMaster", userKey);
				throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
			}
		}
		return lookupMasterVo1;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public LookupMasterVo checkLookupMaster(String selRec, String appRejRemark, String checkType, String userKey) {
		LookupMasterVo lookupMasterVoNew = new LookupMasterVo();
		try
		{
			lookupMasterVoNew.setErrorCode("1");
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_CHECK_LOOKUP);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ILOOKUP_IDS", Types.VARCHAR),
					new SqlParameter("@IAPP_REJ_REMARKS", Types.VARCHAR),
					new SqlParameter("@ICHECK_STATUS", Types.INTEGER),
					new SqlParameter("@ICM_USER_ID", Types.INTEGER)				
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ILOOKUP_IDS", selRec)
					.addValue("@IAPP_REJ_REMARKS", appRejRemark)
					.addValue("@ICHECK_STATUS", checkType)
					.addValue("@ICM_USER_ID", userKey);
			
			simpleJdbcCall.returningResultSet("checkLookupMaster", new RowMapper<LookupMasterVo>() {
				
				@Override
				public LookupMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					LookupMasterVo lookupMasterVo1 = new LookupMasterVo();
					String message = rs.getString(1);	
					lookupMasterVo1.setErrorDesc(message);
					return lookupMasterVo1;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<LookupMasterVo> LookupDetailsList = (List<LookupMasterVo>) results.get("checkLookupMaster");
				if(LookupDetailsList != null && LookupDetailsList.size() > 0)
				{
					lookupMasterVoNew = LookupDetailsList.get(0);
					lookupMasterVoNew.setErrorCode("1");
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
				lookupMasterVoNew.setErrorDesc(strErrorMsg);
			}
			else
			{
				lookupMasterVoNew.setErrorCode("-1");
				loggerUtil.logErrorInsert(e.getMessage(), e, "LookupMasterDaoImpl-checkLookupMaster", userKey);
				throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
			}
		}
		return lookupMasterVoNew;
	}
}
