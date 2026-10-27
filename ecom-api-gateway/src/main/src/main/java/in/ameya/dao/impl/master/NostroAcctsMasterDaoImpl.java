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
import in.ameya.dao.master.NostroAcctsMasterDao;
import in.ameya.model.master.NostroAcctsMasterVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.DBConstants;
import in.ameya.util.DateUtil;
import in.ameya.util.LoggerUtil;

@PropertySource(value="classpath:filePath.properties")
@PropertySource(value="classpath:messages.properties")
@Repository
public class NostroAcctsMasterDaoImpl extends JdbcDaoSupport implements NostroAcctsMasterDao{
		
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private Environment env;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<NostroAcctsMasterVo> getNostroAcctsMasterList(NostroAcctsMasterVo nostroAcctsMasterVo, String userId) {
		ArrayList<NostroAcctsMasterVo> nostroAcctsMasterList = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_NOSTRO_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IPAGE_NO", Types.INTEGER),
					new SqlParameter("@IPAGE_SIZE", Types.INTEGER),
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@INOSTRO_NO", Types.VARCHAR),
					new SqlParameter("@IBANK_CODE", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER),
					new SqlParameter("@IORDER_BY_COLUMN", Types.VARCHAR),
					new SqlParameter("@IORDER_ASC_DESC", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IPAGE_NO", nostroAcctsMasterVo.getPageNo())
					.addValue("@IPAGE_SIZE", nostroAcctsMasterVo.getPageSize())
					.addValue("@ISEARCH_FOR", nostroAcctsMasterVo.getSearchCriteria())
					.addValue("@INOSTRO_NO", ("").equalsIgnoreCase(nostroAcctsMasterVo.getSearchNostroAcctsNo()) ? null : nostroAcctsMasterVo.getSearchNostroAcctsNo())
					.addValue("@IBANK_CODE", ("").equalsIgnoreCase(nostroAcctsMasterVo.getSearchBankCode()) ? null : nostroAcctsMasterVo.getSearchBankCode())
					.addValue("@IUSER_ID", userId)
					.addValue("@IORDER_BY_COLUMN", (("").equalsIgnoreCase(nostroAcctsMasterVo.getOrderByColumn()) ? null : nostroAcctsMasterVo.getOrderByColumn()))
					.addValue("@IORDER_ASC_DESC", (("").equalsIgnoreCase(nostroAcctsMasterVo.getOrderAscDesc()) ? null : nostroAcctsMasterVo.getOrderAscDesc()));
			
			simpleJdbcCall.returningResultSet("getNostroAcctsMasterList", new RowMapper<NostroAcctsMasterVo>() {
				
				@Override
				public NostroAcctsMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
						NostroAcctsMasterVo nostroAcctsMasterVo = new NostroAcctsMasterVo();
						nostroAcctsMasterVo.setNostroAcctsId(rs.getString("NOSTRO_ID"));
						nostroAcctsMasterVo.setNostroAcctsNo(rs.getString("NOSTRO_NO"));
						nostroAcctsMasterVo.setNostroName(rs.getString("NOSTRO_NAME"));
						nostroAcctsMasterVo.setBankCode(rs.getString("BANK_CODE"));
						//nostroAcctsMasterVo.setRealizationAcct(rs.getString("MIRROR_REALZ_ACCT"));
						nostroAcctsMasterVo.setBranchCode(rs.getString("BRANCH_CODE"));
						nostroAcctsMasterVo.setCurrencyCode(rs.getString("CURR_CODE"));
						nostroAcctsMasterVo.setToleranceAmount(rs.getString("TOLERANCE_AMOUNT")); 
						//nostroAcctsMasterVo.setBicCode(rs.getString("BIC"));
						nostroAcctsMasterVo.setStatus(rs.getString("STATUS"));
						nostroAcctsMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
						nostroAcctsMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
						nostroAcctsMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
						nostroAcctsMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
						nostroAcctsMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
						//nostroAcctsMasterVo.setNostroName("NOSTRO_NAME");
						nostroAcctsMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
						nostroAcctsMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
						nostroAcctsMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
						nostroAcctsMasterVo.setRecordCount(rs.getInt("TOT_COUNT"));
						nostroAcctsMasterVo.setMirrorAcctNo(rs.getString("FIN_NOSTRO_MIRROR"));
						nostroAcctsMasterVo.setAccOpeningDate(rs.getString("ACCOUNT_OPENING_DATE"));
						nostroAcctsMasterVo.setAccClosingDate(rs.getString("ACCOUNT_CLOSING_DATE"));
						return nostroAcctsMasterVo;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				nostroAcctsMasterList = (ArrayList<NostroAcctsMasterVo>) results.get("getNostroAcctsMasterList");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "NostroAcctsMasterDaoImpl-getNostroAcctsMasterList", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return nostroAcctsMasterList;
	}
	
	
	
	@Override
	public String fetchExportNostroAcctsMasterCSV(NostroAcctsMasterVo nostroAcctsMasterVo, String userId) {
		String downloadString = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_EXPORT_NOSTRO_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@INOSTRO_NO", Types.VARCHAR),
					new SqlParameter("@IBANK_CODE", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ISEARCH_FOR", nostroAcctsMasterVo.getSearchCriteria())
					.addValue("@INOSTRO_NO", nostroAcctsMasterVo.getNostroAcctsNo())
					.addValue("@IBANK_CODE", nostroAcctsMasterVo.getBankCode())
					.addValue("@IUSER_ID", userId);
			
			simpleJdbcCall.returningResultSet("fetchExportNostroAcctsMasterCSV", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					CSVWriter writer = null;
					String downloadStringNew = null;
					try
					{
						downloadStringNew = env.getProperty("download.file.path") + "NostroAcctsMaster" + "-" + DateUtil.formatDate(new Date(), "ddMMyyyy") + ".csv";
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
				downloadString = (String) results.get("fetchExportNostroAcctsMasterCSV");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "NostroAcctsMasterDaoImpl-fetchExportNostroAcctsMasterCSV", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return downloadString;
	}
	
	@Override
	public ResultSet fetchExportNostroAcctsMasterExcel(NostroAcctsMasterVo nostroAcctsMasterVo, String userId) {
		ResultSet  resultSet = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_EXPORT_NOSTRO_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@INOSTRO_NO", Types.VARCHAR),
					new SqlParameter("@IBANK_CODE", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ISEARCH_FOR", nostroAcctsMasterVo.getSearchCriteria())
					.addValue("@INOSTRO_NO", nostroAcctsMasterVo.getNostroAcctsNo())
					.addValue("@IBANK_CODE", nostroAcctsMasterVo.getBankCode())
					.addValue("@IUSER_ID", userId);
			
			simpleJdbcCall.returningResultSet("fetchExportNostroAcctsMasterExcel", new RowMapper<ResultSet>() {
				
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
				resultSet = (ResultSet) results.get("fetchExportNostroAcctsMasterExcel");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "NostroAcctsMasterDaoImpl-fetchExportNostroAcctsMasterExcel", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return resultSet;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public NostroAcctsMasterVo getNostroAcctsMasterDetails(String selRec, String userId) {
		NostroAcctsMasterVo nostroAcctsMasterVoNew = new NostroAcctsMasterVo();
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_NOSTRO_DETAILS);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@INOSTRO_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@INOSTRO_ID", selRec);
			
			simpleJdbcCall.returningResultSet("getNostroAcctsMasterDetails", new RowMapper<NostroAcctsMasterVo>() {
				
				@Override
				public NostroAcctsMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					NostroAcctsMasterVo nostroAcctsMasterVo = new NostroAcctsMasterVo();
					
					nostroAcctsMasterVo.setNostroAcctsId(rs.getString("NOSTRO_ID"));
					nostroAcctsMasterVo.setNostroAcctsNo(rs.getString("NOSTRO_NO"));
					nostroAcctsMasterVo.setNostroName(rs.getString("NOSTRO_NAME"));
					nostroAcctsMasterVo.setBankCode((rs.getString("BANK_CODE")));
					//nostroAcctsMasterVo.setRealizationAcct((rs.getString("MIRROR_REALZ_ACCT")));
					nostroAcctsMasterVo.setBranchCode((rs.getString("BRANCH_CODE")));
					nostroAcctsMasterVo.setCurrencyCode(rs.getString("CURR_CODE"));
					nostroAcctsMasterVo.setBicCode(rs.getString("BIC"));
					nostroAcctsMasterVo.setToleranceAmount(rs.getString("TOLERANCE_AMOUNT"));  
					nostroAcctsMasterVo.setStatus(rs.getString("STATUS"));
					nostroAcctsMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
					nostroAcctsMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
					nostroAcctsMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
					nostroAcctsMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
					nostroAcctsMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
					nostroAcctsMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
					nostroAcctsMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
					nostroAcctsMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
					nostroAcctsMasterVo.setMirrorAcctNo(rs.getString("FIN_NOSTRO_MIRROR"));
					nostroAcctsMasterVo.setAccOpeningDate(rs.getString("ACCOUNT_OPENING_DATE"));
					nostroAcctsMasterVo.setAccClosingDate(rs.getString("ACCOUNT_CLOSING_DATE"));
					return nostroAcctsMasterVo;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<NostroAcctsMasterVo> NostroAcctsDetailsList = (List<NostroAcctsMasterVo>) results.get("getNostroAcctsMasterDetails");
				if(NostroAcctsDetailsList != null && NostroAcctsDetailsList.size() > 0)
				{
					nostroAcctsMasterVoNew = NostroAcctsDetailsList.get(0);
				}				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "NostroAcctsMasterDaoImpl-getNostroAcctsMasterDetails", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return nostroAcctsMasterVoNew;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public NostroAcctsMasterVo manageNostroAcctsMaster(NostroAcctsMasterVo nostroAcctsMasterVo, String userId) {
		NostroAcctsMasterVo nostroAcctsMasterVoNew = new NostroAcctsMasterVo();
		try
		{
			nostroAcctsMasterVoNew.setErrorCode("1");
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_MANAGE_NOSTRO_ACCTS);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@INOSTRO_ID", Types.INTEGER),
					new SqlParameter("@INOSTRO_NO", Types.VARCHAR),
					new SqlParameter("@IMIRROR_NO", Types.VARCHAR),
					new SqlParameter("@IBANK_CODE", Types.VARCHAR),
					new SqlParameter("@INOSTRO_NAME", Types.VARCHAR),
					new SqlParameter("@IBRANCH_CODE", Types.VARCHAR),
					new SqlParameter("@ICURR_CODE", Types.VARCHAR),
					new SqlParameter("@IBIC", Types.VARCHAR),
					new SqlParameter("@ITOLERANCE_AMOUNT", Types.NUMERIC),
					new SqlParameter("@IOPENING_DATE", Types.DATE),
					new SqlParameter("@ICLOSING_DATE", Types.DATE),
					new SqlParameter("@ISTATUS", Types.INTEGER),
					new SqlParameter("@IUSER_ID", Types.INTEGER)
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@INOSTRO_ID",nostroAcctsMasterVo.getNostroAcctsId())
					.addValue("@INOSTRO_NO", nostroAcctsMasterVo.getNostroAcctsNo())
					.addValue("@IMIRROR_NO", nostroAcctsMasterVo.getMirrorAcctNo())
					.addValue("@IBANK_CODE", nostroAcctsMasterVo.getBankCode())
					.addValue("@INOSTRO_NAME", nostroAcctsMasterVo.getNostroName())
					.addValue("@IBRANCH_CODE", nostroAcctsMasterVo.getBranchCode())
					.addValue("@ICURR_CODE", nostroAcctsMasterVo.getCurrencyCode())
					.addValue("@IBIC", nostroAcctsMasterVo.getBicCode())
					.addValue("@ITOLERANCE_AMOUNT", nostroAcctsMasterVo.getToleranceAmount())
					.addValue("@IOPENING_DATE", ("").equalsIgnoreCase(nostroAcctsMasterVo.getAccOpeningDate()) ? null : nostroAcctsMasterVo.getAccOpeningDate())
					.addValue("@ICLOSING_DATE", ("").equalsIgnoreCase(nostroAcctsMasterVo.getAccClosingDate()) ? null : nostroAcctsMasterVo.getAccClosingDate())
					.addValue("@ISTATUS", nostroAcctsMasterVo.getStatus())
					.addValue("@IUSER_ID", userId)
					;
			
			simpleJdbcCall.returningResultSet("manageNostroAcctsMaster", new RowMapper<NostroAcctsMasterVo>() {
				
				@Override
				public NostroAcctsMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					NostroAcctsMasterVo nostroAcctsMasterVo1 = new NostroAcctsMasterVo();
					String message = rs.getString(1);	
					nostroAcctsMasterVo1.setErrorDesc(message);
					
					return nostroAcctsMasterVo1;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<NostroAcctsMasterVo> NostroAcctsDetailsList = (List<NostroAcctsMasterVo>) results.get("manageNostroAcctsMaster");
				if(NostroAcctsDetailsList != null && NostroAcctsDetailsList.size() > 0)
				{
					nostroAcctsMasterVoNew = NostroAcctsDetailsList.get(0);
					nostroAcctsMasterVoNew.setErrorCode("1");
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
				nostroAcctsMasterVoNew.setErrorDesc(strErrorMsg);
			}
			else
			{
				nostroAcctsMasterVoNew.setErrorCode("-1");
				loggerUtil.logErrorInsert(e.getMessage(), e, "NostroAcctsMasterDaoImpl-manageNostroAcctsMaster", userId);
				throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
			}
		}
		return nostroAcctsMasterVoNew;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public NostroAcctsMasterVo checkNostroAcctsMaster(String selRec, String appRejRemark, String checkType, String userId) {
		NostroAcctsMasterVo nostroAcctsMasterVoNew = new NostroAcctsMasterVo();
		try
		{
			nostroAcctsMasterVoNew.setErrorCode("1");
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_CHECK_NOSTRO_ACCTS);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@INOSTRO_ACCTS_IDS", Types.VARCHAR),
					new SqlParameter("@IAPP_REJ_REMARKS", Types.VARCHAR),
					new SqlParameter("@ICHECK_STATUS", Types.INTEGER),
					new SqlParameter("@ICM_USER_ID", Types.INTEGER)				
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@INOSTRO_ACCTS_IDS", selRec)
					.addValue("@IAPP_REJ_REMARKS", appRejRemark)
					.addValue("@ICHECK_STATUS", checkType)
					.addValue("@ICM_USER_ID", userId);
			
			simpleJdbcCall.returningResultSet("checkNostroAcctsMaster", new RowMapper<NostroAcctsMasterVo>() {
				
				@Override
				public NostroAcctsMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					NostroAcctsMasterVo nostroAcctsMasterVo1 = new NostroAcctsMasterVo();
					String message = rs.getString(1);	
					nostroAcctsMasterVo1.setErrorDesc(message);
					
					return nostroAcctsMasterVo1;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<NostroAcctsMasterVo> NostroAcctsDetailsList = (List<NostroAcctsMasterVo>) results.get("manageNostroAcctsMaster");
				if(NostroAcctsDetailsList != null && NostroAcctsDetailsList.size() > 0)
				{
					nostroAcctsMasterVoNew = NostroAcctsDetailsList.get(0);
					nostroAcctsMasterVoNew.setErrorCode("1");
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
				nostroAcctsMasterVoNew.setErrorDesc(strErrorMsg);
			}
			else
			{
				nostroAcctsMasterVoNew.setErrorCode("-1");
				loggerUtil.logErrorInsert(e.getMessage(), e, "NostroAcctsMasterDaoImpl-checkNostroAcctsMaster", userId);
				throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
			}
		}
		return nostroAcctsMasterVoNew;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<NostroAcctsMasterVo> getNostroMasterPopupList(NostroAcctsMasterVo nostroAcctsMasterVo, String userId) {
		ArrayList<NostroAcctsMasterVo> nostroAcctsMasterList = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_NOSTRO_POPUP_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IPAGE_NO", Types.INTEGER),
					new SqlParameter("@IPAGE_SIZE", Types.INTEGER),
					new SqlParameter("@INOSTRO_NO", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IPAGE_NO", nostroAcctsMasterVo.getPageNo())
					.addValue("@IPAGE_SIZE", nostroAcctsMasterVo.getPageSize())
					.addValue("@INOSTRO_NO", nostroAcctsMasterVo.getNostroAcctsNo());
			
			simpleJdbcCall.returningResultSet("getNostroMasterPopupList", new RowMapper<ArrayList<NostroAcctsMasterVo>>() {
				
				@Override
				public ArrayList<NostroAcctsMasterVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<NostroAcctsMasterVo> nostroAcctsMasterListNew = new ArrayList<NostroAcctsMasterVo>();
					while(rs.next())
					{
						NostroAcctsMasterVo nostroAcctsMasterVo = new NostroAcctsMasterVo();
						nostroAcctsMasterVo.setNostroAcctsId(rs.getString("NOSTRO_ID"));
						nostroAcctsMasterVo.setNostroAcctsNo(rs.getString("NOSTRO_NO"));
						nostroAcctsMasterVo.setRealizationAcct(rs.getString("FIN_NOSTRO_MIRROR"));
						nostroAcctsMasterVo.setBankCode(rs.getString("BANK_CODE"));
						nostroAcctsMasterVo.setBranchCode(rs.getString("BRANCH_CODE"));
						nostroAcctsMasterVo.setCurrencyCode(rs.getString("CURR_CODE"));
						nostroAcctsMasterListNew.add(nostroAcctsMasterVo);
					}
					
					return nostroAcctsMasterListNew;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				nostroAcctsMasterList = (ArrayList<NostroAcctsMasterVo>) results.get("getNostroMasterPopupList");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "NostroAcctsMasterDaoImpl-getNostroMasterPopupList", userId);
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return nostroAcctsMasterList;
	}
	
	
	
	

}
