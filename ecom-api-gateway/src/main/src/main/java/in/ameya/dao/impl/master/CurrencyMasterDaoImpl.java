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
import in.ameya.dao.master.CurrencyMasterDao;
import in.ameya.model.master.CurrencyMasterVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.DBConstants;
import in.ameya.util.DateUtil;
import in.ameya.util.LoggerUtil;

@PropertySource(value="classpath:filePath.properties")
@PropertySource(value="classpath:messages.properties")
@Repository
public class CurrencyMasterDaoImpl extends JdbcDaoSupport implements CurrencyMasterDao{
		
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private Environment env;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<CurrencyMasterVo> getCurrencyMasterList(CurrencyMasterVo currencyMasterVo, String userId) {
		ArrayList<CurrencyMasterVo> currencyMasterList = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_CURRENCY_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IPAGE_NO", Types.INTEGER),
					new SqlParameter("@IPAGE_SIZE", Types.INTEGER),
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@ICURR_CODE", Types.VARCHAR),
					new SqlParameter("@ICURR_DESC", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER),
					new SqlParameter("@IORDER_BY_COLUMN", Types.VARCHAR),
					new SqlParameter("@IORDER_ASC_DESC", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IPAGE_NO", currencyMasterVo.getPageNo())
					.addValue("@IPAGE_SIZE", currencyMasterVo.getPageSize())
					.addValue("@ISEARCH_FOR", currencyMasterVo.getSearchCriteria())
					.addValue("@ICURR_CODE", ("").equalsIgnoreCase(currencyMasterVo.getSearchCurrencyCode()) ? null : currencyMasterVo.getSearchCurrencyCode())
					.addValue("@ICURR_DESC", ("").equalsIgnoreCase(currencyMasterVo.getSearchCurrencyDesc()) ? null : currencyMasterVo.getSearchCurrencyDesc())
					.addValue("@IUSER_ID", userId)
					.addValue("@IORDER_BY_COLUMN", (("").equalsIgnoreCase(currencyMasterVo.getOrderByColumn()) ? null : currencyMasterVo.getOrderByColumn()))
					.addValue("@IORDER_ASC_DESC", (("").equalsIgnoreCase(currencyMasterVo.getOrderAscDesc()) ? null : currencyMasterVo.getOrderAscDesc()));
			
			simpleJdbcCall.returningResultSet("getCurrencyMasterList", new RowMapper<CurrencyMasterVo>() {
				
				@Override
				public CurrencyMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						CurrencyMasterVo currencyMasterVo = new CurrencyMasterVo();
						currencyMasterVo.setCurrencyId(rs.getString("CURR_ID"));
						currencyMasterVo.setCurrencyCode(rs.getString("CURR_CODE"));
						currencyMasterVo.setCurrencyDesc(rs.getString("CURR_DESCRIPTION"));
						currencyMasterVo.setCurrencyDate(rs.getString("CURR_DATE"));
						currencyMasterVo.setMultiplicationFactor(rs.getString("MULTIPLYING_FACTOR"));
						currencyMasterVo.setStatus(rs.getString("STATUS"));
						currencyMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
						currencyMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
						currencyMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
						currencyMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
						currencyMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
						currencyMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
						currencyMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
						currencyMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
						currencyMasterVo.setRecordCount(rs.getInt("TOT_COUNT"));
						
						return currencyMasterVo;				
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				currencyMasterList = (ArrayList<CurrencyMasterVo>) results.get("getCurrencyMasterList");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CurrencyMasterDaoImpl-getCurrencyMasterList", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return currencyMasterList;
	}
	
	
	@Override
	public String fetchExportCurrencyMasterCSV(CurrencyMasterVo currencyMasterVo, String userId) {
		String downloadString = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_EXPORT_CURRENCY_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@ICURR_CODE", Types.VARCHAR),
					new SqlParameter("@ICURR_DESC", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ISEARCH_FOR", currencyMasterVo.getSearchCriteria())
					.addValue("@ICURR_CODE", currencyMasterVo.getCurrencyCode())
					.addValue("@ICURR_DESC", currencyMasterVo.getCurrencyDesc())
					.addValue("@IUSER_ID", userId);
			
			simpleJdbcCall.returningResultSet("fetchExportCurrencyMasterCSV", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					CSVWriter writer = null;
					String downloadStringNew = null;
					try
					{
						downloadStringNew = env.getProperty("download.file.path") + "CurrencyMaster" + "-" + DateUtil.formatDate(new Date(), "ddMMyyyy") + ".csv";
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
				downloadString = (String) results.get("fetchExportCurrencyMasterCSV");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CurrencyMasterDaoImpl-fetchExportCurrencyMasterCSV", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return downloadString;
	}
	
	
	@Override
	public ResultSet fetchExportCurrencyMasterExcel(CurrencyMasterVo currencyMasterVo, String userId) {
		ResultSet  resultSet = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_EXPORT_CURRENCY_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@ICURR_CODE", Types.VARCHAR),
					new SqlParameter("@ICURR_DESC", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ISEARCH_FOR", currencyMasterVo.getSearchCriteria())
					.addValue("@ICURR_CODE", currencyMasterVo.getCurrencyCode())
					.addValue("@ICURR_DESC", currencyMasterVo.getCurrencyDesc())
					.addValue("@IUSER_ID", userId);
			
			simpleJdbcCall.returningResultSet("fetchExportCurrencyMasterExcel", new RowMapper<ResultSet>() {
				
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
				resultSet = (ResultSet) results.get("fetchExportCurrencyMasterExcel");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CurrencyMasterDaoImpl-fetchExportCurrencyMasterExcel", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return resultSet;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CurrencyMasterVo getCurrencyMasterDetails(String selRec, String userId) {
		CurrencyMasterVo currencyMasterVoNew = new CurrencyMasterVo();
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_CURRENCY_DETAILS);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ICURR_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ICURR_ID", selRec);
			
			simpleJdbcCall.returningResultSet("getCurrencyMasterDetails", new RowMapper<CurrencyMasterVo>() {
				
				@Override
				public CurrencyMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					CurrencyMasterVo currencyMasterVo = new CurrencyMasterVo();
					
					currencyMasterVo.setCurrencyId(rs.getString("CURR_ID"));
					currencyMasterVo.setCurrencyCode(rs.getString("CURR_CODE"));
					currencyMasterVo.setCurrencyDesc(rs.getString("CURR_DESCRIPTION"));
					currencyMasterVo.setCurrencyDate(rs.getString("CURR_DATE"));
					currencyMasterVo.setMultiplicationFactor(rs.getString("MULTIPLYING_FACTOR"));
					currencyMasterVo.setStatus(rs.getString("STATUS"));
					currencyMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
					currencyMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
					currencyMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
					currencyMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
					currencyMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
					currencyMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
					currencyMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
					currencyMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
					
					return currencyMasterVo;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<CurrencyMasterVo> CurrencyDetailsList = (List<CurrencyMasterVo>) results.get("getCurrencyMasterDetails");
				if(CurrencyDetailsList != null && CurrencyDetailsList.size() > 0)
				{
					currencyMasterVoNew = CurrencyDetailsList.get(0);
				}				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "CurrencyMasterDaoImpl-getCurrencyMasterDetails", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return currencyMasterVoNew;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public CurrencyMasterVo manageCurrencyMaster(CurrencyMasterVo currencyMasterVo, String userId) {
		CurrencyMasterVo currencyMasterVoNew = new CurrencyMasterVo();
		try
		{
			currencyMasterVoNew.setErrorCode("1");
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_MANAGE_CURRENCY);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ICURR_ID", Types.INTEGER),
					new SqlParameter("@ICURR_CODE", Types.VARCHAR),
					new SqlParameter("@ICURR_DESCRIPTION", Types.VARCHAR),
					new SqlParameter("@ISTATUS", Types.VARCHAR),
					new SqlParameter("@ICURR_DATE", Types.VARCHAR),
					new SqlParameter("@IMULTIPLYING_FACTOR", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER)
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ICURR_ID",currencyMasterVo.getCurrencyId())
					.addValue("@ICURR_CODE", currencyMasterVo.getCurrencyCode())
					.addValue("@ICURR_DESCRIPTION", currencyMasterVo.getCurrencyDesc())
					.addValue("@ISTATUS", currencyMasterVo.getStatus())
					.addValue("@ICURR_DATE", currencyMasterVo.getCurrencyDate())
					.addValue("@IMULTIPLYING_FACTOR", currencyMasterVo.getMultiplicationFactor())
					.addValue("@IUSER_ID", userId)
					;
			
			simpleJdbcCall.returningResultSet("manageCurrencyMaster", new RowMapper<CurrencyMasterVo>() {
				
				@Override
				public CurrencyMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					CurrencyMasterVo CurrencyMasterVo1 = new CurrencyMasterVo();
					String message = rs.getString(1);	
					CurrencyMasterVo1.setErrorDesc(message);
					
					return CurrencyMasterVo1;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<CurrencyMasterVo> currencyDetailsList = (List<CurrencyMasterVo>) results.get("manageCurrencyMaster");
				if(currencyDetailsList != null && currencyDetailsList.size() > 0)
				{
					currencyMasterVoNew = currencyDetailsList.get(0);
					currencyMasterVoNew.setErrorCode("1");
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
				currencyMasterVoNew.setErrorDesc(strErrorMsg);
			}
			else
			{
				currencyMasterVoNew.setErrorCode("-1");
				loggerUtil.logErrorInsert(e.getMessage(), e, "CurrencyMasterDaoImpl-manageCurrencyMaster", userId);
				throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
			}
		}
		return currencyMasterVoNew;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public CurrencyMasterVo checkCurrencyMaster(String selRec, String appRejRemark, String checkType, String userId) {
		CurrencyMasterVo currencyMasterVoNew = new CurrencyMasterVo();
		try
		{
			currencyMasterVoNew.setErrorCode("1");
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_CHECK_CURRENCY);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ICURRENCY_IDS", Types.VARCHAR),
					new SqlParameter("@IAPP_REJ_REMARKS", Types.VARCHAR),
					new SqlParameter("@ICHECK_STATUS", Types.INTEGER),
					new SqlParameter("@ICM_USER_ID", Types.INTEGER)				
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ICURRENCY_IDS", selRec)
					.addValue("@IAPP_REJ_REMARKS", appRejRemark)
					.addValue("@ICHECK_STATUS", checkType)
					.addValue("@ICM_USER_ID", userId);
			
			simpleJdbcCall.returningResultSet("checkCurrencyMaster", new RowMapper<CurrencyMasterVo>() {
				
				@Override
				public CurrencyMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					CurrencyMasterVo CurrencyMasterVo1 = new CurrencyMasterVo();
					String message = rs.getString(1);	
					CurrencyMasterVo1.setErrorDesc(message);
					
					return CurrencyMasterVo1;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<CurrencyMasterVo> CurrencyDetailsList = (List<CurrencyMasterVo>) results.get("checkCurrencyMaster");
				if(CurrencyDetailsList != null && CurrencyDetailsList.size() > 0)
				{
					currencyMasterVoNew = CurrencyDetailsList.get(0);
					currencyMasterVoNew.setErrorCode("1");
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
				currencyMasterVoNew.setErrorDesc(strErrorMsg);
			}
			else
			{
				currencyMasterVoNew.setErrorCode("-1");
				loggerUtil.logErrorInsert(e.getMessage(), e, "CurrencyMasterDaoImpl-checkCurrencyMaster", userId);
				throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
			}
		}
		return currencyMasterVoNew;
	}
	
	
	
	
	
	
	

}
