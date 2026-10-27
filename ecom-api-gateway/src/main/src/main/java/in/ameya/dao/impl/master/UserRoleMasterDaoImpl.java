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
import in.ameya.dao.master.UserRoleMasterDao;
import in.ameya.model.master.UserRoleMasterVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.Constants;
import in.ameya.util.DBConstants;
import in.ameya.util.DateUtil;
import in.ameya.util.LoggerUtil;

@PropertySource(value="classpath:filePath.properties")
@Repository
public class UserRoleMasterDaoImpl extends JdbcDaoSupport implements UserRoleMasterDao{
		
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private Environment env;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<UserRoleMasterVo> getUserRoleMasterList(UserRoleMasterVo userRoleMasterVo, String userKey) {
		ArrayList<UserRoleMasterVo> userRoleMasterList = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_USER_ROLE_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IPAGE_NO", Types.INTEGER),
					new SqlParameter("@IPAGE_SIZE", Types.INTEGER),
					new SqlParameter("@IUSER_PROFILE_ID", Types.INTEGER),
					new SqlParameter("@IUSER_TYPE", Types.INTEGER),
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@IUSER_ROLE_NAME", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER),
					new SqlParameter("@IORDER_BY_COLUMN", Types.VARCHAR),
					new SqlParameter("@IORDER_ASC_DESC", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IPAGE_NO", userRoleMasterVo.getPageNo())
					.addValue("@IPAGE_SIZE", Constants.MAX_PAGE_SIZE)
					.addValue("@IUSER_PROFILE_ID", userRoleMasterVo.getUserRoleProfileId())
					.addValue("@IUSER_TYPE", userRoleMasterVo.getUserType())
					.addValue("@ISEARCH_FOR", userRoleMasterVo.getSearchCriteria())
					.addValue("@IUSER_ROLE_NAME", userRoleMasterVo.getUserRoleName())
					.addValue("@IUSER_ID", userKey)
					.addValue("@IORDER_BY_COLUMN", (("").equalsIgnoreCase(userRoleMasterVo.getOrderByColumn()) ? null : userRoleMasterVo.getOrderByColumn()))
					.addValue("@IORDER_ASC_DESC", (("").equalsIgnoreCase(userRoleMasterVo.getOrderAscDesc()) ? null : userRoleMasterVo.getOrderAscDesc()));
			
			simpleJdbcCall.returningResultSet("getUserRoleMasterList", new RowMapper<ArrayList<UserRoleMasterVo>>() {
				
				@Override
				public ArrayList<UserRoleMasterVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<UserRoleMasterVo> userRoleMasterListNew = new ArrayList<UserRoleMasterVo>();
					try
					{
						while(rs.next())
						{
							UserRoleMasterVo userRoleMasterVo = new UserRoleMasterVo();
							userRoleMasterVo.setUserRoleId(rs.getString("USER_ROLE_ID"));
							userRoleMasterVo.setUserRoleCode(rs.getString("USER_ROLE_CODE"));
							userRoleMasterVo.setUserRoleName(rs.getString("USER_ROLE_NAME"));
							userRoleMasterVo.setUserType(rs.getString("USER_TYPE"));				
							userRoleMasterVo.setUserTypeDesc(rs.getString("USER_TYPE_DESC"));
							userRoleMasterVo.setUserRoleProfileId(rs.getString("USER_ROLE_PROFILE_ID"));
							userRoleMasterVo.setUserRoleProfileDesc(rs.getString("USER_ROLE_PROFILE_DESC"));
							userRoleMasterVo.setStatus(rs.getString("STATUS"));
							userRoleMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
							userRoleMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
							userRoleMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
							userRoleMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
							userRoleMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
							userRoleMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
							userRoleMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
							userRoleMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
							userRoleMasterListNew.add(userRoleMasterVo);
						}
						
						return userRoleMasterListNew;
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
				userRoleMasterList = (ArrayList<UserRoleMasterVo>) results.get("getUserRoleMasterList");
				if(userRoleMasterList != null && userRoleMasterList.size() > 0)
				{
					System.out.println("loginList->"+userRoleMasterList.size());
					//loginVoNew = loginList.get(0);
					//System.out.println("getUserId-->"+loginVoNew.getUserId());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserRoleMasterDaoImpl-getUserRoleMasterList", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return userRoleMasterList;
	}
	
	
	@Override
	public String fetchExportUserRoleMasterCSV(UserRoleMasterVo userRoleMasterVo, String userKey) {
		String downloadString = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_EXPORT_USER_RLE_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IUSER_PROFILE_ID", Types.INTEGER),
					new SqlParameter("@IUSER_TYPE", Types.VARCHAR),
					new SqlParameter("@ISEARCH_FOR", Types.VARCHAR),
					new SqlParameter("@IUSER_ROLE_NAME", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IUSER_PROFILE_ID", userRoleMasterVo.getUserRoleProfileId())
					.addValue("@IUSER_TYPE", userRoleMasterVo.getUserType())
					.addValue("@ISEARCH_FOR", userRoleMasterVo.getSearchCriteria())
					.addValue("@IUSER_ROLE_NAME", userRoleMasterVo.getUserRoleName())
					.addValue("@IUSER_ID", userKey);
			
			simpleJdbcCall.returningResultSet("fetchExportUserRoleMasterCSV", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					CSVWriter writer = null;
					String downloadStringNew = null;
					try
					{
						downloadStringNew = env.getProperty("download.file.path") + "UserRoleMaster" + "-" + DateUtil.formatDate(new Date(), "ddMMyyyy") + ".csv";
						writer = new CSVWriter(new FileWriter(downloadStringNew), '|', CSVWriter.NO_ESCAPE_CHARACTER, System.getProperty("line.separator"));
						writer.writeAll(rs, false);
						writer.close();
					}
					catch(Exception e)
					{
						e.printStackTrace();
						loggerUtil.logErrorInsert(e.getMessage(), e, "UserRoleMasterDaoImpl-ExportCSVRowmapper", "");
					}
					return downloadStringNew;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				downloadString = (String) results.get("fetchExportUserRoleMasterCSV");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserRoleMasterDaoImpl-fetchExportUserRoleMasterCSV", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return downloadString;
	}
	
	
	@Override
	public ResultSet fetchExportUserRoleMasterExcel(UserRoleMasterVo userRoleMasterVo, String userKey) {
		ResultSet  resultSet = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_EXPORT_USER_RLE_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IUSER_PROFILE_ID", Types.INTEGER),
					new SqlParameter("@IUSER_TYPE", Types.VARCHAR),
					new SqlParameter("@ISEARCH_FOR", Types.VARCHAR),
					new SqlParameter("@IUSER_ROLE_NAME", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IUSER_PROFILE_ID", userRoleMasterVo.getUserRoleProfileId())
					.addValue("@IUSER_TYPE", userRoleMasterVo.getUserType())
					.addValue("@ISEARCH_FOR", userRoleMasterVo.getSearchCriteria())
					.addValue("@IUSER_ROLE_NAME", userRoleMasterVo.getUserRoleName())
					.addValue("@IUSER_ID", userKey);
			
			simpleJdbcCall.returningResultSet("fetchExportUserRoleMasterExcel", new RowMapper<ResultSet>() {
				
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
				resultSet = (ResultSet) results.get("fetchExportUserRoleMasterExcel");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserRoleMasterDaoImpl-fetchExportUserRoleMasterExcel", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return resultSet;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public UserRoleMasterVo getUserRoleMasterDetails(String selRec, String userKey) {
		UserRoleMasterVo userMasterVoNew = new UserRoleMasterVo();
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_USER_ROLE_DETAILS);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IUSER_ROLE_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IUSER_ROLE_ID", selRec);
			
			simpleJdbcCall.returningResultSet("getUserRoleMasterDetails", new RowMapper<UserRoleMasterVo>() {
				
				@Override
				public UserRoleMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					UserRoleMasterVo userRoleMasterVo = new UserRoleMasterVo();
					
					userRoleMasterVo.setUserRoleId(rs.getString("USER_ROLE_ID"));
					userRoleMasterVo.setUserRoleProfileId(rs.getString("USER_ROLE_PROFILE_ID"));
					userRoleMasterVo.setUserRoleProfileDesc(rs.getString("USER_ROLE_PROFILE_DESC"));
					userRoleMasterVo.setUserRoleCode(rs.getString("USER_ROLE_CODE"));
					userRoleMasterVo.setUserRoleName(rs.getString("USER_ROLE_NAME"));
					userRoleMasterVo.setUserType(rs.getString("USER_TYPE"));			
					userRoleMasterVo.setUserTypeDesc(rs.getString("USER_TYPE_DESC"));
					userRoleMasterVo.setStatus(rs.getString("STATUS"));
					userRoleMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
					userRoleMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
					userRoleMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
					userRoleMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
					userRoleMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
					userRoleMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
					userRoleMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
					userRoleMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
					
					return userRoleMasterVo;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<UserRoleMasterVo> UserRoleDetailsList = (List<UserRoleMasterVo>) results.get("getUserRoleMasterDetails");
				if(UserRoleDetailsList != null && UserRoleDetailsList.size() > 0)
				{
					System.out.println("UserRoleDetailsList->"+UserRoleDetailsList.size());
					userMasterVoNew = UserRoleDetailsList.get(0);
					System.out.println("getUserId-->"+userMasterVoNew.getUserId());
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserRoleMasterDaoImpl-getUserRoleMasterDetails", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return userMasterVoNew;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String manageUserRoleMaster(UserRoleMasterVo userRoleMasterVo, String userKey) {
		String errorMsg = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_MANAGE_USER_ROLES);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IUSER_ROLE_ID", Types.INTEGER),
					new SqlParameter("@IUSER_ROLE_PROFILE_ID", Types.INTEGER),
					new SqlParameter("@IUSER_ROLE_CODE", Types.VARCHAR),
					new SqlParameter("@IUSER_ROLE_NAME", Types.VARCHAR),
					new SqlParameter("@IUSER_TYPE", Types.INTEGER),
					new SqlParameter("@IATTACHED_ROLE_ID", Types.INTEGER),
					new SqlParameter("@ISTATUS", Types.INTEGER),
					new SqlParameter("@IUSER_ID", Types.INTEGER)
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IUSER_ROLE_ID", userRoleMasterVo.getUserRoleId())
					.addValue("@IUSER_ROLE_PROFILE_ID", userRoleMasterVo.getUserRoleProfileId())
					.addValue("@IUSER_ROLE_CODE", userRoleMasterVo.getUserRoleCode())
					.addValue("@IUSER_ROLE_NAME", userRoleMasterVo.getUserRoleName())
					.addValue("@IUSER_TYPE", userRoleMasterVo.getUserType())
					.addValue("@IATTACHED_ROLE_ID", "")
					.addValue("@ISTATUS", userRoleMasterVo.getStatus())
					.addValue("@IUSER_ID", userKey);
			
			simpleJdbcCall.returningResultSet("manageUserRoleMaster", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					String message = rs.getString(1);	
					
					return message;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<String> UserRoleDetailsList = (List<String>) results.get("manageUserRoleMaster");
				if(UserRoleDetailsList != null && UserRoleDetailsList.size() > 0)
				{
					System.out.println("UserRoleDetailsList->"+UserRoleDetailsList.size());
					errorMsg = UserRoleDetailsList.get(0);
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserRoleMasterDaoImpl-manageUserRoleMaster", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return errorMsg;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String checkUserRoleMaster(String selRec, String appRejRemark, String checkType, String userKey) {
		String errorMsg = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_CHECK_USER_ROLES);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IUSER_ROLE_IDS", Types.VARCHAR),
					new SqlParameter("@IAPP_REJ_REMARKS", Types.VARCHAR),
					new SqlParameter("@ICHECK_STATUS", Types.INTEGER),
					new SqlParameter("@ICM_USER_ID", Types.INTEGER)				
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IUSER_ROLE_IDS", selRec)
					.addValue("@IAPP_REJ_REMARKS", appRejRemark)
					.addValue("@ICHECK_STATUS", checkType)
					.addValue("@ICM_USER_ID", userKey);
			
			simpleJdbcCall.returningResultSet("checkUserRoleMaster", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					String message = rs.getString(1);	
					
					return message;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<String> UserRoleDetailsList = (List<String>) results.get("checkUserRoleMaster");
				if(UserRoleDetailsList != null && UserRoleDetailsList.size() > 0)
				{
					System.out.println("UserRoleDetailsList->"+UserRoleDetailsList.size());
					errorMsg = UserRoleDetailsList.get(0);
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserRoleMasterDaoImpl-checkUserRoleMaster", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return errorMsg;
	}
	
	
	
	
	
	
	

}
