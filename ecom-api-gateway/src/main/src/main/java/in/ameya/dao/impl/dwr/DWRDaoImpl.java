package in.ameya.dao.impl.dwr;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import in.ameya.dao.dwr.DWRDao;
import in.ameya.model.dwr.DwrVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.DBConstants;
import in.ameya.util.LoggerUtil;
import in.ameya.util.NumberUtil;

@PropertySource(value="classpath:filePath.properties")
@Repository
public class DWRDaoImpl extends JdbcDaoSupport implements DWRDao{
		
	@Autowired
	private LoggerUtil loggerUtil;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getAllLookups(String lookupType) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ALL_LOOKUP);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_LOOKUP_TYPE", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_LOOKUP_TYPE", lookupType);
			
			simpleJdbcCall.returningResultSet("getAllLookups", new RowMapper<DwrVo>() {
				
				@Override
				public DwrVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					try
					{
						DwrVo dwrVo = new DwrVo();
						dwrVo.setKey(rs.getString("LOOKUP_ID"));
						dwrVo.setValue(rs.getString("LOOKUP_DESCRIPTION"));
						
						return dwrVo;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getAllLookups");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getAllLookups", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getMandate103Process(String lookupType) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_MANDATE_103_PROCESS);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_LOOKUP_TYPE", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_LOOKUP_TYPE", lookupType);
			
			simpleJdbcCall.returningResultSet("getMandate103Process", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("LOOKUP_ID"));
							dwrVo.setValue(rs.getString("LOOKUP_DESCRIPTION"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getMandate103Process");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getMandate103Process", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getPoolAccountManDateWiseList(String msgTypeKey, String manDateCode, String branchSrNo) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_POOLACC_MANDATE_WISE);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_MSG_TYPE", Types.INTEGER),
					new SqlParameter("@I_MANDATE_CODE", Types.INTEGER),
					new SqlParameter("@I_BRANCH_SR_NO", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_MSG_TYPE", msgTypeKey)
					.addValue("@I_MANDATE_CODE", manDateCode)
					.addValue("@I_BRANCH_SR_NO", branchSrNo);
			
			simpleJdbcCall.returningResultSet("getPoolAccountManDateWiseList", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("POOL_ACCT_ID"));
							dwrVo.setValue(rs.getString("POOL_ACCT_NO"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getPoolAccountManDateWiseList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getPoolAccountManDateWiseList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getNostroList() {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ALL_NOSTRO_ACCTS);
			simpleJdbcCall.declareParameters();
	
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			simpleJdbcCall.returningResultSet("getNostroList", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("NOSTRO_ID"));
							dwrVo.setValue(rs.getString("NOSTRO_ACCT_NO"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getNostroList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getNostroList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getCountryList() {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ALL_COUNTRY);
			simpleJdbcCall.declareParameters();
	
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			simpleJdbcCall.returningResultSet("getCountryList", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("COUNTRY_ID"));
							dwrVo.setValue(rs.getString("COUNTRY_NAME"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getCountryList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getCountryList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getCurrencyList() {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ALL_CURRENCY);
			simpleJdbcCall.declareParameters();
	
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			simpleJdbcCall.returningResultSet("getCurrencyList", new RowMapper<DwrVo>() {
				
				@Override
				public DwrVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					try
					{
						DwrVo dwrVo = new DwrVo();
						dwrVo.setKey(rs.getString("CURR_ID"));
						dwrVo.setValue(rs.getString("CURR_CODE"));
						
						return dwrVo;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getCurrencyList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getCurrencyList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getBankList() {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ALL_BANKS);
			simpleJdbcCall.declareParameters();
	
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			simpleJdbcCall.returningResultSet("getBankList", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("BANK_ID"));
							dwrVo.setValue(rs.getString("BANK_NAME"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getBankList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getBankList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getBankListMT103CRH() {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ALL_BANKS_103_CRH);
			simpleJdbcCall.declareParameters();
	
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			simpleJdbcCall.returningResultSet("getBankListMT103CRH", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("BANK_ID"));
							dwrVo.setValue(rs.getString("BANK_NAME"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getBankListMT103CRH");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getBankListMT103CRH", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getBranchIdList() {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ALL_BRANCHES);
			simpleJdbcCall.declareParameters();
	
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			simpleJdbcCall.returningResultSet("getBranchIdList", new RowMapper<DwrVo>() {
				
				@Override
				public DwrVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					String branchId = null;
					String branchCode = null;
					String branchDesc = null;
					try
					{
						DwrVo dwrVo = new DwrVo();
						branchId = rs.getString("BRANCH_ID");
						branchCode = rs.getString("BRANCH_CODE");
						branchDesc = rs.getString("BRANCH_DESCRIPTION");
						branchCode = branchCode + "-" + branchDesc;
						dwrVo.setKey(branchId);
						dwrVo.setValue(branchCode);
						
						return dwrVo;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getBranchIdList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getBranchIdList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getAllMsgType() {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ALL_MSG_TYPES);
			simpleJdbcCall.declareParameters();
	
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			simpleJdbcCall.returningResultSet("getAllMsgType", new RowMapper<DwrVo>() {
				
				@Override
				public DwrVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					try
					{
						DwrVo dwrVo = new DwrVo();
						dwrVo.setKey(rs.getString("MSG_TYPE_ID"));
						dwrVo.setValue(rs.getString("MSG_CODE"));
						return dwrVo;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getAllMsgType");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getAllMsgType", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getSelectedMsgType(String type) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_SELECTED_MSG_TYPES);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ITYPE", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ITYPE", type);
			
			simpleJdbcCall.returningResultSet("getSelectedMsgType", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("MSG_ID"));
							dwrVo.setValue(rs.getString("MSG_CODE"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getSelectedMsgType");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getSelectedMsgType", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getAccessPrivilege(String privileges,String userRoleId) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_USER_PRIVILEGE);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IPRIVILEGES", Types.INTEGER),
					new SqlParameter("@IROLE_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IPRIVILEGES", privileges)
					.addValue("@IROLE_ID", userRoleId);
			
			simpleJdbcCall.returningResultSet("getAccessPrivilege", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					String privilegeId = null;
					try
					{
						while(rs.next())
						{
							privilegeId = rs.getString("USER_PRIVILEGES");
						}
						
						return privilegeId;
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
				returnJsonString = (String) results.get("getAccessPrivilege");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getAccessPrivilege", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String updateLoginStatus(String userName) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_GNL_RESET_LOGIN_STATUS);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_USER_NAME", Types.VARCHAR),
					new SqlParameter("@I_LOGIN_STATUS", Types.INTEGER),
					new SqlParameter("@I_BROWSER_SESS_ID", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_USER_NAME", userName)
					.addValue("@I_LOGIN_STATUS", 0)
					.addValue("@I_BROWSER_SESS_ID", "");
			
			simpleJdbcCall.returningResultSet("updateLoginStatus", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					String message = null;
					try
					{
						while(rs.next())
						{
							message = rs.getString(1);
						}
						
						return message;
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
				returnJsonString = (String) results.get("updateLoginStatus");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-updateLoginStatus", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public int checkValidSession(String userName, String sessionId) {
		int i = 0;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_CHECK_VALID_SESSIONID);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_USER_NAME", Types.VARCHAR),
					new SqlParameter("@I_SESSION_ID", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_USER_NAME", userName)
					.addValue("@I_SESSION_ID", sessionId);
			
			simpleJdbcCall.returningResultSet("checkValidSession", new RowMapper<Integer>() {
				
				@Override
				public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
					int j = 0;
					try
					{
						while(rs.next())
						{
							j = rs.getInt(1);
						}
						
						return j;
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
				i = (int) results.get("checkValidSession");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-checkValidSession", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return i;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public int insertOracleErrorInTable(String error, String proName, String userId) {
		int i = 0;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_GEN_INSERT_ORACLE_ERROR);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_ERROR_DESC", Types.VARCHAR),
					new SqlParameter("@I_PROCEDURE_NAME", Types.VARCHAR),
					new SqlParameter("@I_USER_ID", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_ERROR_DESC", error)
					.addValue("@I_PROCEDURE_NAME", proName)
					.addValue("@I_USER_ID", userId);
			
			simpleJdbcCall.returningResultSet("insertOracleErrorInTable", new RowMapper<Integer>() {
				
				@Override
				public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
					int j = 0;
					try
					{
						while(rs.next())
						{
							j = rs.getInt(1);
						}
						
						return j;
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
				i = (int) results.get("insertOracleErrorInTable");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-insertOracleErrorInTable", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return i;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getIFSCList() {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ALL_IFSC);
			simpleJdbcCall.declareParameters();
	
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			simpleJdbcCall.returningResultSet("getIFSCList", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("IFSC_ID"));
							dwrVo.setValue(rs.getString("IFSC_CODE"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getIFSCList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getIFSCList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getPurposeList() {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ALL_PURPOSE_CODE);
			simpleJdbcCall.declareParameters();
	
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			simpleJdbcCall.returningResultSet("getPurposeList", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("PURPOSE_ID"));
							dwrVo.setValue(rs.getString("PURPOSE_CODE") + "-" + rs.getString("PURPOSE_DESCRIPTION"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getPurposeList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getPurposeList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getAuditDWRList() {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ALL_AUDIT);
			simpleJdbcCall.declareParameters();
	
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			simpleJdbcCall.returningResultSet("getAuditDWRList", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("TABLE_ID"));
							dwrVo.setValue(rs.getString("TABLE_DESCRIPTION"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getAuditDWRList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getAuditDWRList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getPoolAcc(String lookupType) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ALL_LOOKUP);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_LOOKUP_TYPE", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_LOOKUP_TYPE", lookupType);
			
			simpleJdbcCall.returningResultSet("getPoolAcc", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("LOOKUP_ID"));
							dwrVo.setValue(rs.getString("LOOKUP_DESCRIPTION"));
							if (!(dwrVo.getKey()).equalsIgnoreCase("15")) {
								dwrVoNew.add(dwrVo);
							}
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getPoolAcc");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getPoolAcc", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getValueDateForDashboard(String dashboardCountFor, String msgType) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_VAL_DATE_DASHBOARD);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_DASH_COUNT", Types.INTEGER),
					new SqlParameter("@I_MSG_TYPE", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_DASH_COUNT", dashboardCountFor)
					.addValue("@I_MSG_TYPE", msgType);
			
			simpleJdbcCall.returningResultSet("getValueDateForDashboard", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("VALUE_DATE"));
							dwrVo.setValue(rs.getString("VALUE_DATE"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getValueDateForDashboard");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getValueDateForDashboard", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String calculateCardRateFinacleForTRREFNo(String trRefNo) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_CAL_CARD_RATE_TRREFNO_FIN);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_REF_NO", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_REF_NO", trRefNo);
			
			simpleJdbcCall.returningResultSet("calculateCardRateFinacleForTRREFNo", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					String dblCardRate = "0";
					try
					{
						while(rs.next())
						{
							String outVal = rs.getString(1);
							if (!outVal.equalsIgnoreCase(""))
								dblCardRate = NumberUtil.getFormattedAmount(Double.parseDouble(outVal), 4);
						}
						
						return dblCardRate;
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
				returnJsonString = (String) results.get("calculateCardRateFinacleForTRREFNo");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-calculateCardRateFinacleForTRREFNo", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String checkEODTimeForApprove(String msgId,String userId) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_TRN_DPA_CHECK_EOD);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_MSG_ID", Types.INTEGER),
					new SqlParameter("@I_USER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_MSG_ID", msgId)
					.addValue("@I_USER_ID", userId);
			
			simpleJdbcCall.returningResultSet("checkEODTimeForApprove", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					String retValue = "";
					try
					{
						while(rs.next())
						{
							retValue = rs.getString(1) + "|" + rs.getString(2);
						}
						
						return retValue;
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
				returnJsonString = (String) results.get("checkEODTimeForApprove");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-checkEODTimeForApprove", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String calculateCardRateFinacleForTRREFNo(String dashboardCountFor,String msgType) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_VAL_DATE_DASH_MT101);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_DASH_COUNT", Types.INTEGER),
					new SqlParameter("@I_MSG_TYPE", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_DASH_COUNT", dashboardCountFor)
					.addValue("@I_MSG_TYPE", msgType);
			
			simpleJdbcCall.returningResultSet("calculateCardRateFinacleForTRREFNo", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("VALUE_DATE"));
							dwrVo.setValue(rs.getString("VALUE_DATE"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("calculateCardRateFinacleForTRREFNo");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-calculateCardRateFinacleForTRREFNo", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String checkEODTimeForApproveMT101(String msgId,String userId) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP101_TRN_DPA_CHECK_EOD);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_MSG_ID", Types.INTEGER),
					new SqlParameter("@I_USER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_MSG_ID", msgId)
					.addValue("@I_USER_ID", userId);
			
			simpleJdbcCall.returningResultSet("checkEODTimeForApproveMT101", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					String retValue = "";
					try
					{
						while(rs.next())
						{
							retValue = rs.getString(1) + "|" + rs.getString(2);
						}
						
						return retValue;
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
				returnJsonString = (String) results.get("checkEODTimeForApproveMT101");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-checkEODTimeForApproveMT101", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getSSIAccountNos(String searchAccountNo) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_SSI_ACCOUNT_NO);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_ACC_NO", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_ACC_NO", searchAccountNo);
			
			simpleJdbcCall.returningResultSet("getSSIAccountNos", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("CUST_ACCOUNT_NO"));
							dwrVo.setValue(rs.getString("CUST_ACCOUNT_NO"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getSSIAccountNos");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getSSIAccountNos", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getSSICustomerNames(String searchCustomerName) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_SSI_CUSTOMER_NAME);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_CUST_NAME", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_CUST_NAME", searchCustomerName);
			
			simpleJdbcCall.returningResultSet("getSSICustomerNames", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("CUSTOMER_NAME"));
							dwrVo.setValue(rs.getString("CUSTOMER_NAME"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getSSICustomerNames");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getSSICustomerNames", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getSSIBranches(String searchBranch) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_SSI_CUSTOMER_BRANCH);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_BRANCH", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_BRANCH", searchBranch);
			
			simpleJdbcCall.returningResultSet("getSSIBranches", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("BRANCH_ID"));
							dwrVo.setValue(rs.getString("BRANCH_CODE"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getSSIBranches");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getSSIBranches", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getBCatBranchList() {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_BCAT_BRANCH_FIN);
			simpleJdbcCall.declareParameters();
	
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			simpleJdbcCall.returningResultSet("getBCatBranchList", new RowMapper<DwrVo>() {
				
				@Override
				public DwrVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					try
					{
						DwrVo dwrVo = new DwrVo();
						dwrVo.setKey(rs.getString("BRANCH_ID"));
						dwrVo.setValue(rs.getString("BRANCH_CODE") + "-" + rs.getString("BRANCH_DESCRIPTION"));
						
						return dwrVo;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getBCatBranchList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getBCatBranchList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getNostroAccountMapFinList() {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_NOSTRO_ACCTS_FIN);
			simpleJdbcCall.declareParameters();
	
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			simpleJdbcCall.returningResultSet("getNostroAccountMapFinList", new RowMapper<DwrVo>() {
				
				@Override
				public DwrVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					DwrVo dwrVoNew = new DwrVo();
					try
					{
						dwrVoNew.setKey(rs.getString("BIC"));
						dwrVoNew.setValue(rs.getString("NOSTRO_NO"));
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getNostroAccountMapFinList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getNostroAccountMapFinList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public int getNostroAccMapFinCurrency(String searchNostro) {
		int i = 0;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_NOSTRO_ACC_FIN_CURR);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@INOSTRO_NO", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@INOSTRO_NO", searchNostro);
			
			simpleJdbcCall.returningResultSet("getNostroAccMapFinCurrency", new RowMapper<Integer>() {
				
				@Override
				public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
					int j = 0;
					try
					{
						while(rs.next())
						{
							j = rs.getInt("CURR_ID");
						}
						
						return j;
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
				i = (int) results.get("getNostroAccMapFinCurrency");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getNostroAccMapFinCurrency", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return i;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getMT199ReasonList(String reasonType) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_MT199_REASONS);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IREASON_TYPE", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IREASON_TYPE", reasonType);
			
			simpleJdbcCall.returningResultSet("getMT199ReasonList", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("REASON_ID"));
							dwrVo.setValue(rs.getString("REASON_DESC"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getMT199ReasonList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getMT199ReasonList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getRetailTFCReportList(String reportFor, String branchCode, String userId) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_COMMON_REPORT_EXPORT);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IREPORT_CODE", Types.CHAR),
					new SqlParameter("@IUSER_BRANCH_CODE", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IREPORT_CODE", reportFor)
					.addValue("@IUSER_BRANCH_CODE", branchCode)
					.addValue("@IUSER_ID", userId);
			
			simpleJdbcCall.returningResultSet("getRetailTFCReportList", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("REPORT_ID"));
							dwrVo.setValue(rs.getString("REPORT_NAME"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getRetailTFCReportList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getRetailTFCReportList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getSettlementProductType(String msgCredId) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_CREDITS_PRODUCT_TYPE);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IMSG_ID", Types.CHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IMSG_ID", msgCredId);
			
			simpleJdbcCall.returningResultSet("getSettlementProductType", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("PRODUCT_TYPE"));
							dwrVo.setValue(rs.getString("PRODUCT_TYPE"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getSettlementProductType");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getSettlementProductType", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getMT199TemplateText(String creditId, String templateType, double chargesRecovered) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_RETURNOF_FUND_FORMAT);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_creditId", Types.VARCHAR),
					new SqlParameter("@I_templateType", Types.VARCHAR),
					new SqlParameter("@I_chargesRecovered", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_creditId", creditId)
					.addValue("@I_templateType", templateType)
					.addValue("@I_chargesRecovered", chargesRecovered);
			
			simpleJdbcCall.returningResultSet("getMT199TemplateText", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					String returnStr = "";
					try
					{
						while(rs.next())
						{
							returnStr = rs.getString("INT_REF_NO")+"$"+rs.getString("TRAN_REF_NO")+"$"+rs.getString("NARRATIVE");
						}
						
						return returnStr;
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
				returnJsonString = (String) results.get("getMT199TemplateText");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getMT199TemplateText", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String fetchMT199TemplateType(String msgType, String userType) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_MT199_TEMPLATE_TYPE);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ImsgType", Types.VARCHAR),
					new SqlParameter("@IuserType", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ImsgType", msgType)
					.addValue("@IuserType", userType);
			
			simpleJdbcCall.returningResultSet("fetchMT199TemplateType", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("TEMPLATE_TYPE"));
							dwrVo.setValue(rs.getString("TEMPLATE_DESC"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("fetchMT199TemplateType");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-fetchMT199TemplateType", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String saveMT199ROFPrint(String msgId, String creditId, String mt199SrNo, String convertAmtFlag, String  msgAmountSum, String tag79, String userId) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_TRN_INSERT_MT199ROF_PRINT);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IMSG_ID", Types.INTEGER),
					new SqlParameter("@ICREDIT_ID", Types.INTEGER),
					new SqlParameter("@IMT199_SR_NO", Types.VARCHAR),
					new SqlParameter("@ICHARGES", Types.INTEGER),
					new SqlParameter("@IREM_AMOUNT", Types.INTEGER),
					new SqlParameter("@IREASON_ROF", Types.VARCHAR),
					new SqlParameter("@IPRINT_BY", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IMSG_ID", msgId)
					.addValue("@ICREDIT_ID", creditId)
					.addValue("@IMT199_SR_NO", mt199SrNo)
					.addValue("@ICHARGES", convertAmtFlag)
					.addValue("@IREM_AMOUNT", msgAmountSum)
					.addValue("@IREASON_ROF", tag79)
					.addValue("@IPRINT_BY", userId);
			
			simpleJdbcCall.returningResultSet("saveMT199ROFPrint", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					String retValue = "0";
					try
					{
						while(rs.next())
						{
							retValue = rs.getString(1); 
						}
						
						return retValue;
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
				returnJsonString = (String) results.get("saveMT199ROFPrint");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-saveMT199ROFPrint", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getSIAcctHolderName(String accountNo) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKG_TRN_FETCH_STP_FETCH_ACCOUNT_HOLDER_NAME);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IaccountNo", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IaccountNo", accountNo);
			
			simpleJdbcCall.returningResultSet("getSIAcctHolderName", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					String retValue = "0";
					try
					{
						while(rs.next())
						{
							retValue = rs.getString("ACCOUNT_HOLDER_NAME");
						}
						
						return retValue;
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
				returnJsonString = (String) results.get("getSIAcctHolderName");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getSIAcctHolderName", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getCreditsHoldRemarks(String userId) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_CREDITS_HOLD_REMARKS);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IUSER_ID", userId);
			
			simpleJdbcCall.returningResultSet("getCreditsHoldRemarks", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("CREDIT_HOLD_ID"));
							dwrVo.setValue(rs.getString("HOLD_REMARK"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getCreditsHoldRemarks");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getCreditsHoldRemarks", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getFLC700TemplateName(String custId) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_700_CUSTFLC_TEMPNAME);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IcustId", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IcustId", custId);
			
			simpleJdbcCall.returningResultSet("getFLC700TemplateName", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("FLC_DET_ID"));
							dwrVo.setValue(rs.getString("TEMPLATE_NAME"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getFLC700TemplateName");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getFLC700TemplateName", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getFLC700TemplateDetails(String flcDetId) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_700_CUSTFLC_TEMPDET);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IcustId", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IflcDetId", flcDetId);
			
			simpleJdbcCall.returningResultSet("getFLC700TemplateDetails", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					String retString = "";
					try
					{
						while(rs.next())
						{
							Blob field46ABlob = rs.getBlob("FIELD_46A");
							byte[] bdata1 = field46ABlob.getBytes(1, (int) field46ABlob.length());
							String field46AString = new String(bdata1);
							
							Blob field47ABlob = rs.getBlob("FIELD_47A");
							byte[] bdata2 = field47ABlob.getBytes(1, (int) field47ABlob.length());
							String field47AString = new String(bdata2);
							
							retString = field46AString.trim() + "#_*" + field47AString.trim();
						}
						
						return retString;
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
				returnJsonString = (String) results.get("getFLC700TemplateDetails");;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getFLC700TemplateDetails", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getPublishIdAfterDatePicked(String fromDate, String toDate) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_DATE_WISE_PUBLISH_ID);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IfromDate", Types.DATE),
					new SqlParameter("@toDate", Types.DATE));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IfromDate", fromDate)
					.addValue("@toDate", toDate);
			
			simpleJdbcCall.returningResultSet("getPublishIdAfterDatePicked", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("PUBLISH_ID"));
							dwrVo.setValue(rs.getString("PUBLISH_DET"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getPublishIdAfterDatePicked");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getPublishIdAfterDatePicked", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String getUserList() {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ALL_USERS);
			simpleJdbcCall.declareParameters();
	
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			simpleJdbcCall.returningResultSet("getUserList", new RowMapper<DwrVo>() {
				
				@Override
				public DwrVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					String userId = null;
					String loginId = null;
					try
					{
						DwrVo dwrVo = new DwrVo();
						userId = rs.getString("USER_ID");
						loginId = rs.getString("LOGIN_ID");
						
						dwrVo.setKey(userId);
						dwrVo.setValue(loginId);
						
						return dwrVo;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getUserList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getUserList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getUserRolesProfileWise(String profileId) {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_USER_ROLES_PROFILE_WISE);
			simpleJdbcCall.declareParameters(new SqlParameter("@IUSER_ROLE_PROFILE_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IUSER_ROLE_PROFILE_ID", profileId);
			
			simpleJdbcCall.returningResultSet("getUserRolesProfileWise", new RowMapper<ArrayList<DwrVo>>() {
				
				@Override
				public ArrayList<DwrVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<DwrVo> dwrVoNew = new ArrayList<DwrVo>();
					try
					{
						while(rs.next())
						{
							DwrVo dwrVo = new DwrVo();
							dwrVo.setKey(rs.getString("USER_ROLE_ID"));
							dwrVo.setValue(rs.getString("USER_ROLE_NAME"));
							dwrVoNew.add(dwrVo);
						}
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getUserRolesProfileWise");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getUserRolesProfileWise", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	public String getJSONString(List<DwrVo> list)
	{
		String returnJsonString = "{";//"{\"0\":\"-- Select --\"";
		if(list != null && !list.isEmpty())
		{
			int listSize = list.size();
			for(int i = 0; i < listSize; i++)
			{
				DwrVo dwrVo = list.get(i);						
				String lookupId = dwrVo.getKey();
				String lookupDesc = dwrVo.getValue();
				if(i == 0)
					returnJsonString = returnJsonString+"\""+lookupDesc+"#"+lookupId+"\""+":"+"\""+lookupDesc+"\"";	
				else
					returnJsonString = returnJsonString+","+"\""+lookupDesc+"#"+lookupId+"\""+":"+"\""+lookupDesc+"\"";
			}
		}
		returnJsonString = returnJsonString +"}";
		return returnJsonString;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public String getMirrorList() {
		String returnJsonString = "";
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_NOSTRO_ACCTS_MIRROR_FIN);
			simpleJdbcCall.declareParameters();
	
			MapSqlParameterSource params = new MapSqlParameterSource();
			
			simpleJdbcCall.returningResultSet("getMirrorList", new RowMapper<DwrVo>() {
				
				@Override
				public DwrVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					DwrVo dwrVoNew = new DwrVo();
					try
					{
						dwrVoNew.setKey(rs.getString("BIC"));
						dwrVoNew.setValue(rs.getString("NOSTRO_NO"));
						
						return dwrVoNew;
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
				ArrayList<DwrVo> list = (ArrayList<DwrVo>) results.get("getMirrorList");
				returnJsonString = getJSONString(list);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "DWRDaoImpl-getMirrorList", "");
			throw new RuntimeException("Something went wrong please try again");
		}
		return returnJsonString;
	}
	
	
	
	

}
