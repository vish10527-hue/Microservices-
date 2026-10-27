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
import in.ameya.dao.master.CountryMasterDao;
import in.ameya.model.master.CountryMasterVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.DBConstants;
import in.ameya.util.DateUtil;
import in.ameya.util.LoggerUtil;

@PropertySource(value="classpath:filePath.properties")
@PropertySource(value="classpath:messages.properties")
@Repository
public class CountryMasterDaoImpl extends JdbcDaoSupport implements CountryMasterDao{
		
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private Environment env;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<CountryMasterVo> getCountryMasterList(CountryMasterVo countryMasterVo, String userId) {
		ArrayList<CountryMasterVo> countryMasterList = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_COUNTRY_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IPAGE_NO", Types.INTEGER),
					new SqlParameter("@IPAGE_SIZE", Types.INTEGER),
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@ICOUNTRY_CODE", Types.VARCHAR),
					new SqlParameter("@ICOUNTRY_NAME", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IPAGE_NO", countryMasterVo.getPageNo())
					.addValue("@IPAGE_SIZE", countryMasterVo.getPageSize())
					.addValue("@ISEARCH_FOR", countryMasterVo.getSearchCriteria())
					.addValue("@ICOUNTRY_CODE", ("").equalsIgnoreCase(countryMasterVo.getSearchCountryCode()) ? null : countryMasterVo.getSearchCountryCode())
					.addValue("@ICOUNTRY_NAME", ("").equalsIgnoreCase(countryMasterVo.getSearchCountryName()) ? null : countryMasterVo.getSearchCountryName())
					.addValue("@IUSER_ID", userId);
			
			simpleJdbcCall.returningResultSet("getCountryMasterList", new RowMapper<CountryMasterVo>() {
				
				@Override
				public CountryMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						CountryMasterVo countryMasterVo = new CountryMasterVo();
						countryMasterVo.setCountryId(rs.getString("COUNTRY_ID"));
						countryMasterVo.setCountryCode(rs.getString("COUNTRY_CODE"));
						countryMasterVo.setCountryName(rs.getString("COUNTRY_NAME"));
						countryMasterVo.setStatus(rs.getString("STATUS"));
						countryMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
						countryMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
						countryMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
						countryMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
						countryMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
						countryMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
						countryMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
						countryMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
						countryMasterVo.setRecordCount(rs.getInt("TOT_COUNT"));
						
						return countryMasterVo;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				countryMasterList = (ArrayList<CountryMasterVo>) results.get("getCountryMasterList");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CountryMasterDaoImpl-getCountryMasterList", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return countryMasterList;
	}
	
	
	@Override
	public String fetchExportCountryMasterCSV(CountryMasterVo countryMasterVo, String userId) {
		String downloadString = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_EXPORT_COUNTRY_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@ICOUNTRY_CODE", Types.VARCHAR),
					new SqlParameter("@ICOUNTRY_NAME", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ISEARCH_FOR", countryMasterVo.getSearchCriteria())
					.addValue("@ICOUNTRY_CODE", countryMasterVo.getCountryCode())
					.addValue("@ICOUNTRY_NAME", countryMasterVo.getCountryName())
					.addValue("@IUSER_ID", userId);
			
			simpleJdbcCall.returningResultSet("fetchExportCountryMasterCSV", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					CSVWriter writer = null;
					String downloadStringNew = null;
					try
					{
						downloadStringNew = env.getProperty("download.file.path") + "CountryMaster" + "-" + DateUtil.formatDate(new Date(), "ddMMyyyy") + ".csv";
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
				downloadString = (String) results.get("fetchExportCountryMasterCSV");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CountryMasterDaoImpl-fetchExportCountryMasterCSV", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return downloadString;
	}
	
	@Override
	public ResultSet fetchExportCountryMasterExcel(CountryMasterVo countryMasterVo, String userId) {
		ResultSet  resultSet = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_EXPORT_COUNTRY_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@ICOUNTRY_CODE", Types.VARCHAR),
					new SqlParameter("@ICOUNTRY_NAME", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ISEARCH_FOR", countryMasterVo.getSearchCriteria())
					.addValue("@ICOUNTRY_CODE", countryMasterVo.getCountryCode())
					.addValue("@ICOUNTRY_NAME", countryMasterVo.getCountryName())
					.addValue("@IUSER_ID", userId);
			
			simpleJdbcCall.returningResultSet("fetchExportCountryMasterExcel", new RowMapper<ResultSet>() {
				
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
				resultSet = (ResultSet) results.get("fetchExportCountryMasterExcel");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CountryMasterDaoImpl-fetchExportCountryMasterExcel", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return resultSet;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public CountryMasterVo getCountryMasterDetails(String selRec, String userId) {
		CountryMasterVo countryMasterVoNew = new CountryMasterVo();
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_COUNTRY_DETAILS);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ICOUNTRY_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ICOUNTRY_ID", selRec);
			
			simpleJdbcCall.returningResultSet("getCountryMasterDetails", new RowMapper<CountryMasterVo>() {
				
				@Override
				public CountryMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					CountryMasterVo countryMasterVo = new CountryMasterVo();
					
					countryMasterVo.setCountryId(rs.getString("COUNTRY_ID"));
					countryMasterVo.setCountryCode(rs.getString("COUNTRY_CODE"));
					countryMasterVo.setCountryName(rs.getString("COUNTRY_NAME"));
					
					countryMasterVo.setStatus(rs.getString("STATUS"));
					countryMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
					countryMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
					countryMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
					countryMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
					countryMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
					countryMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
					countryMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
					countryMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
					
					return countryMasterVo;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<CountryMasterVo> CountryDetailsList = (List<CountryMasterVo>) results.get("getCountryMasterDetails");
				if(CountryDetailsList != null && CountryDetailsList.size() > 0)
				{
					countryMasterVoNew = CountryDetailsList.get(0);
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CountryMasterDaoImpl-getCountryMasterDetails", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return countryMasterVoNew;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public CountryMasterVo manageCountryMaster(CountryMasterVo countryMasterVo, String userId) {
		CountryMasterVo countryMasterVoNew = new CountryMasterVo();
		try
		{
			countryMasterVoNew.setErrorCode("1");
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_MANAGE_COUNTRIES);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ICOUNTRY_ID", Types.INTEGER),
					new SqlParameter("@ICOUNTRY_CODE", Types.VARCHAR),
					new SqlParameter("@ICOUNTRY_NAME", Types.VARCHAR),
					new SqlParameter("@ISTATUS", Types.INTEGER),
					new SqlParameter("@IUSER_ID", Types.INTEGER)
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ICOUNTRY_ID",countryMasterVo.getCountryId())
					.addValue("@ICOUNTRY_CODE", countryMasterVo.getCountryCode())
					.addValue("@ICOUNTRY_NAME", countryMasterVo.getCountryName())
					.addValue("@ISTATUS", countryMasterVo.getStatus())
					.addValue("@IUSER_ID", userId)
					;
			
			simpleJdbcCall.returningResultSet("manageCountryMaster", new RowMapper<CountryMasterVo>() {
				
				@Override
				public CountryMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					CountryMasterVo countryMasterVo1 = new CountryMasterVo();
					String message = rs.getString(1);	
					countryMasterVo1.setErrorDesc(message);
					
					return countryMasterVo1;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<CountryMasterVo> countryMasterList = (List<CountryMasterVo>) results.get("manageCountryMaster");
				if(countryMasterList != null && countryMasterList.size() > 0)
				{
					countryMasterVoNew = countryMasterList.get(0);
					countryMasterVoNew.setErrorCode("1");
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
				countryMasterVoNew.setErrorDesc(strErrorMsg);
			}
			else
			{
				countryMasterVoNew.setErrorCode("-1");
				loggerUtil.logErrorInsert(e.getMessage(), e, "CountryMasterDaoImpl-manageCountryMaster", userId);
				throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
			}
		}
		return countryMasterVoNew;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public CountryMasterVo checkCountryMaster(String selRec, String appRejRemark, String checkType, String userId) {
		CountryMasterVo countryMasterVoNew = new CountryMasterVo();
		try
		{
			countryMasterVoNew.setErrorCode("1");
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_CHECK_COUNTRIES);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ICOUNTRY_IDS", Types.VARCHAR),
					new SqlParameter("@IAPP_REJ_REMARKS", Types.VARCHAR),
					new SqlParameter("@ICHECK_STATUS", Types.INTEGER),
					new SqlParameter("@ICM_USER_ID", Types.INTEGER)				
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ICOUNTRY_IDS", selRec)
					.addValue("@IAPP_REJ_REMARKS", appRejRemark)
					.addValue("@ICHECK_STATUS", checkType)
					.addValue("@ICM_USER_ID", userId);
			
			simpleJdbcCall.returningResultSet("checkCountryMaster", new RowMapper<CountryMasterVo>() {
				
				@Override
				public CountryMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					CountryMasterVo countryMasterVo1 = new CountryMasterVo();
					String message = rs.getString(1);	
					countryMasterVo1.setErrorDesc(message);
					
					return countryMasterVo1;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<CountryMasterVo> CountryDetailsList = (List<CountryMasterVo>) results.get("checkCountryMaster");
				if(CountryDetailsList != null && CountryDetailsList.size() > 0)
				{
					countryMasterVoNew = CountryDetailsList.get(0);
					countryMasterVoNew.setErrorCode("1");
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
				countryMasterVoNew.setErrorDesc(strErrorMsg);
			}
			else
			{
				countryMasterVoNew.setErrorCode("-1");
				loggerUtil.logErrorInsert(e.getMessage(), e, "CountryMasterDaoImpl-checkCountryMaster", userId);
				throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
			}
		}
		return countryMasterVoNew;
	}
	
	
	
	
	
	
	

}
