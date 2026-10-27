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
import in.ameya.dao.master.UserMasterDao;
import in.ameya.model.master.UserMasterVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.Constants;
import in.ameya.util.DBConstants;
import in.ameya.util.DateUtil;
import in.ameya.util.LoggerUtil;

@PropertySource(value="classpath:filePath.properties")
@Repository
public class UserMasterDaoImpl extends JdbcDaoSupport implements UserMasterDao{
		
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private Environment env;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<UserMasterVo> getUserMasterList(UserMasterVo userMasterVo, String userKey) {
		ArrayList<UserMasterVo> userMasterList = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_USER_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IPAGE_NO", Types.INTEGER),
					new SqlParameter("@IPAGE_SIZE", Types.INTEGER),
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@ISTATUS", Types.VARCHAR),
					new SqlParameter("@ILOGIN_ID", Types.VARCHAR),
					new SqlParameter("@IBRANCH_ID", Types.VARCHAR),
					new SqlParameter("@IUSER_NAME", Types.VARCHAR),
					new SqlParameter("@USER_ROLE_PROFILE_ID", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER),
					new SqlParameter("@IORDER_BY_COLUMN", Types.VARCHAR),
					new SqlParameter("@IORDER_ASC_DESC", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IPAGE_NO", userMasterVo.getPageNo())
					.addValue("@IPAGE_SIZE", Constants.MAX_PAGE_SIZE)
					.addValue("@ISEARCH_FOR", userMasterVo.getSearchCriteria())
					.addValue("@ISTATUS", (("").equalsIgnoreCase(userMasterVo.getStatus()) ? null : userMasterVo.getStatus()))
					.addValue("@ILOGIN_ID", (("").equalsIgnoreCase(userMasterVo.getLoginId()) ? null : userMasterVo.getLoginId()))
					.addValue("@IBRANCH_ID", (("").equalsIgnoreCase(userMasterVo.getBranchId()) ? null : userMasterVo.getBranchId()))
					.addValue("@IUSER_NAME", (("").equalsIgnoreCase(userMasterVo.getUserName()) ? null : userMasterVo.getUserName()))
					.addValue("@USER_ROLE_PROFILE_ID", (("").equalsIgnoreCase(userMasterVo.getUserRoleProfileId()) ? null : userMasterVo.getUserRoleProfileId()))
					.addValue("@IUSER_ID", userKey)
					.addValue("@IORDER_BY_COLUMN", (("").equalsIgnoreCase(userMasterVo.getOrderByColumn()) ? null : userMasterVo.getOrderByColumn()))
					.addValue("@IORDER_ASC_DESC", (("").equalsIgnoreCase(userMasterVo.getOrderAscDesc()) ? null : userMasterVo.getOrderAscDesc()));
			
			simpleJdbcCall.returningResultSet("getUserMasterList", new RowMapper<ArrayList<UserMasterVo>>() {
				
				@Override
				public ArrayList<UserMasterVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<UserMasterVo> userMasterListNew = new ArrayList<UserMasterVo>();
					try
					{
						while(rs.next())
						{
							UserMasterVo userMasterVo = new UserMasterVo();
							userMasterVo.setUserId(rs.getString("USER_ID"));
							userMasterVo.setLoginId(rs.getString("LOGIN_ID"));
							userMasterVo.setUserName(rs.getString("USER_NAME"));						
							userMasterVo.setBranchId(rs.getString("BRANCH_ID"));
							userMasterVo.setPhoneNo(rs.getString("PHONE_NO"));
							userMasterVo.setEmailId(rs.getString("EMAIL_ID"));
							userMasterVo.setStatus(rs.getString("STATUS"));
							userMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
							userMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
							userMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
							userMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
							userMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
							userMasterVo.setLockStatus(rs.getString("USR_LOCK_STATUS"));
							userMasterVo.setDeleteStatus(rs.getString("USR_DEL_STATUS"));
							userMasterVo.setDeActivationDate(rs.getString("DEACTIVE_TILL_DATE"));
							userMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
							userMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
							userMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
							userMasterVo.setBranchCode(rs.getString("BRANCH_DESCRIPTION"));
							userMasterVo.setUserRoleProfileDesc(rs.getString("USER_ROLE_PROFILE_DESC"));
							userMasterVo.setRecordCount(rs.getInt("TOT_COUNT"));
							userMasterListNew.add(userMasterVo);
						}
						
						return userMasterListNew;
					}
					catch(Exception e)
					{
						e.printStackTrace();
						loggerUtil.logErrorInsert(e.getMessage(), e, "UserMasterDaoImpl-getUserMasterList", userKey);
						throw new RuntimeException("Something went wrong please try again");
					}
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				userMasterList = (ArrayList<UserMasterVo>) results.get("getUserMasterList");
				if(userMasterList != null && userMasterList.size() > 0)
				{
					System.out.println("loginList->"+userMasterList.size());
					//loginVoNew = loginList.get(0);
					//System.out.println("getUserId-->"+loginVoNew.getUserId());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserMasterDaoImpl-getUserMasterList", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return userMasterList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String fetchExportUserMasterCSV(UserMasterVo userMasterVo, String userKey) {
		String downloadString = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_EXPORT_USER_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@ISTATUS", Types.VARCHAR),
					new SqlParameter("@ILOGIN_ID", Types.VARCHAR),
					new SqlParameter("@IBRANCH_ID", Types.VARCHAR),
					new SqlParameter("@IUSER_NAME", Types.VARCHAR),
					new SqlParameter("@USER_ROLE_PROFILE_ID", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ISEARCH_FOR", userMasterVo.getSearchCriteria())
					.addValue("@ISTATUS", (("").equalsIgnoreCase(userMasterVo.getStatus()) ? null : userMasterVo.getStatus()))
					.addValue("@ILOGIN_ID", (("").equalsIgnoreCase(userMasterVo.getLoginId()) ? null : userMasterVo.getLoginId()))
					.addValue("@IBRANCH_ID", (("").equalsIgnoreCase(userMasterVo.getBranchId()) ? null : userMasterVo.getBranchId()))
					.addValue("@IUSER_NAME", (("").equalsIgnoreCase(userMasterVo.getUserName()) ? null : userMasterVo.getUserName()))
					.addValue("@USER_ROLE_PROFILE_ID", (("").equalsIgnoreCase(userMasterVo.getUserRoleProfileId()) ? null : userMasterVo.getUserRoleProfileId()))
					.addValue("@IUSER_ID", userKey);
			
			simpleJdbcCall.returningResultSet("fetchExportUserMasterCSV", new RowMapper<String>() {
				
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					CSVWriter writer = null;
					String downloadStringNew = null;
					try
					{
						downloadStringNew = env.getProperty("download.file.path") + "UserMaster" + "-" + DateUtil.formatDate(new Date(), "ddMMyyyy") + ".csv";
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
				downloadString = (String) results.get("fetchExportUserMasterCSV");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserMasterDaoImpl-fetchExportUserMasterCSV", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return downloadString;
	}
	
	
	@Override
	public ResultSet fetchExportUserMasterExcel(UserMasterVo userMasterVo, String userKey) {
		ResultSet resultSet = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_EXPORT_USER_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@ISTATUS", Types.VARCHAR),
					new SqlParameter("@ILOGIN_ID", Types.VARCHAR),
					new SqlParameter("@IBRANCH_ID", Types.VARCHAR),
					new SqlParameter("@IUSER_NAME", Types.VARCHAR),
					new SqlParameter("@USER_ROLE_PROFILE_ID", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@ISEARCH_FOR", userMasterVo.getSearchCriteria())
					.addValue("@ISTATUS", (("").equalsIgnoreCase(userMasterVo.getStatus()) ? null : userMasterVo.getStatus()))
					.addValue("@ILOGIN_ID", (("").equalsIgnoreCase(userMasterVo.getLoginId()) ? null : userMasterVo.getLoginId()))
					.addValue("@IBRANCH_ID", (("").equalsIgnoreCase(userMasterVo.getBranchId()) ? null : userMasterVo.getBranchId()))
					.addValue("@IUSER_NAME", (("").equalsIgnoreCase(userMasterVo.getUserName()) ? null : userMasterVo.getUserName()))
					.addValue("@USER_ROLE_PROFILE_ID", (("").equalsIgnoreCase(userMasterVo.getUserRoleProfileId()) ? null : userMasterVo.getUserRoleProfileId()))
					.addValue("@IUSER_ID", userKey);
			
			simpleJdbcCall.returningResultSet("fetchExportUserMasterExcel", new RowMapper<ResultSet>() {
				
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
				resultSet = (ResultSet) results.get("fetchExportUserMasterExcel");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserMasterDaoImpl-fetchExportUserMasterExcel", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return resultSet;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public UserMasterVo getUserMasterDetails(String selRec, String userKey) {
		UserMasterVo userMasterVoNew = new UserMasterVo();
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_USER_DETAILS);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IUSER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IUSER_ID", selRec);
			
			simpleJdbcCall.returningResultSet("getUserMasterDetails", new RowMapper<UserMasterVo>() {
				
				@Override
				public UserMasterVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					UserMasterVo userMasterVo = new UserMasterVo();
					
					userMasterVo.setUserId(rs.getString("USER_ID"));
					userMasterVo.setLoginId(rs.getString("LOGIN_ID"));
					userMasterVo.setUserName(rs.getString("USER_NAME"));
					userMasterVo.setPhoneNo(rs.getString("PHONE_NO"));
					userMasterVo.setEmailId(rs.getString("EMAIL_ID"));
					userMasterVo.setBranchId(rs.getString("BRANCH_ID"));
					userMasterVo.setBranchCode(rs.getString("BRANCH_CODE"));
					userMasterVo.setStatus(rs.getString("STATUS"));
					userMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
					userMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
					userMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
					userMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
					userMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
					userMasterVo.setDeActivationDate(rs.getString("DEACTIVE_TILL_DATE"));
					userMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
					userMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
					userMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
					userMasterVo.setLockStatus(rs.getString("USR_LOCK_STATUS"));
					userMasterVo.setDeleteStatus(rs.getString("USR_DEL_STATUS"));
					userMasterVo.setUserRoleId(rs.getString("USER_ROLE_ID"));
					userMasterVo.setUserRoleName(rs.getString("USER_ROLE_NAME"));
					userMasterVo.setUserRoleProfileId(rs.getString("USER_ROLE_PROFILE_ID"));
					userMasterVo.setUserRoleProfileDesc(rs.getString("USER_ROLE_PROFILE_DESC"));
					
					return userMasterVo;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				List<UserMasterVo> UserDetailsList = (List<UserMasterVo>) results.get("getUserMasterDetails");
				if(UserDetailsList != null && UserDetailsList.size() > 0)
				{
					//System.out.println("UserDetailsList->"+UserDetailsList.size());
					userMasterVoNew = UserDetailsList.get(0);
					//System.out.println("getUserId-->"+userMasterVoNew.getUserId());
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserMasterDaoImpl-getUserMasterDetails", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return userMasterVoNew;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String manageUserMaster(UserMasterVo userMasterVo, String userKey) {
		String errorMsg = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_MANAGE_USER);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IUSER_ID", Types.INTEGER),
					new SqlParameter("@ILOGIN_ID", Types.VARCHAR),
					new SqlParameter("@IUSER_NAME", Types.VARCHAR),
					new SqlParameter("@IBRANCH_ID", Types.INTEGER),
					new SqlParameter("@IPHONE_NO", Types.VARCHAR),
					new SqlParameter("@IEMAIL_ID", Types.VARCHAR),
					new SqlParameter("@IDEACTIVE_TILL_DATE", Types.DATE),
					new SqlParameter("@ISTATUS", Types.INTEGER),
					new SqlParameter("@IUSR_LOCK_STATUS", Types.INTEGER),
					new SqlParameter("@IUSR_DEL_STATUS", Types.INTEGER),
					new SqlParameter("@ICM_USER_ID", Types.INTEGER),
					new SqlParameter("@IUSER_PASSWORD", Types.VARCHAR),
					new SqlParameter("@GAL_USER_FLAG", Types.INTEGER),
					new SqlParameter("@IUSER_ROLE_ID", Types.INTEGER)				
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IUSER_ID", "0")
					.addValue("@ILOGIN_ID", userMasterVo.getLoginId())
					.addValue("@IUSER_NAME", userMasterVo.getUserName())
					.addValue("@IBRANCH_ID", userMasterVo.getBranchId())
					.addValue("@IPHONE_NO", userMasterVo.getPhoneNo())
					.addValue("@IEMAIL_ID", userMasterVo.getEmailId())
					.addValue("@IDEACTIVE_TILL_DATE", userMasterVo.getDeActivationDate())
					.addValue("@ISTATUS", userMasterVo.getStatus())
					.addValue("@IUSR_LOCK_STATUS", userMasterVo.getLockStatus())
					.addValue("@IUSR_DEL_STATUS", userMasterVo.getDeleteStatus())
					.addValue("@ICM_USER_ID", userKey)
					.addValue("@IUSER_PASSWORD", userMasterVo.getUserPassword())
					.addValue("@GAL_USER_FLAG", 0)
					.addValue("@IUSER_ROLE_ID", userMasterVo.getUserRoleId());
			
			simpleJdbcCall.returningResultSet("manageUserMaster", new RowMapper<String>() {
				
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
				List<String> UserDetailsList = (List<String>) results.get("manageUserMaster");
				if(UserDetailsList != null && UserDetailsList.size() > 0)
				{
					System.out.println("UserDetailsList->"+UserDetailsList.size());
					errorMsg = UserDetailsList.get(0);
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserMasterDaoImpl-manageUserMaster", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return errorMsg;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String checkUserMaster(String selRec, String appRejRemark, String checkType, String userKey) {
		String errorMsg = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_CHECK_USER);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IUSER_IDS", Types.VARCHAR),
					new SqlParameter("@IAPP_REJ_REMARKS", Types.VARCHAR),
					new SqlParameter("@ICHECK_STATUS", Types.INTEGER),
					new SqlParameter("@ICM_USER_ID", Types.INTEGER)				
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IUSER_IDS", selRec)
					.addValue("@IAPP_REJ_REMARKS", appRejRemark)
					.addValue("@ICHECK_STATUS", checkType)
					.addValue("@ICM_USER_ID", userKey);
			
			simpleJdbcCall.returningResultSet("checkUserMaster", new RowMapper<String>() {
				
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
				List<String> UserDetailsList = (List<String>) results.get("checkUserMaster");
				if(UserDetailsList != null && UserDetailsList.size() > 0)
				{
					System.out.println("UserDetailsList->"+UserDetailsList.size());
					errorMsg = UserDetailsList.get(0);
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserMasterDaoImpl-checkUserMaster", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return errorMsg;
	}
	
	
	
	
	
	
	

}
