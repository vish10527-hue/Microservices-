package in.ameya.dao.impl.master;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
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

import in.ameya.dao.master.UserRoleMappingMasterDao;
import in.ameya.model.master.UserMasterVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.Constants;
import in.ameya.util.DBConstants;
import in.ameya.util.LoggerUtil;

@PropertySource(value="classpath:filePath.properties")
@Repository
public class UserRoleMappingMasterDaoImpl extends JdbcDaoSupport implements UserRoleMappingMasterDao{
		
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private Environment env;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<UserMasterVo> fetchUserRoleMappingMasterList(UserMasterVo userMasterVo, String userKey) {
		ArrayList<UserMasterVo> userRoleMappingList = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ATT_USR_USR_ROLE_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IPAGE_NO", Types.INTEGER),
					new SqlParameter("@IPAGE_SIZE", Types.INTEGER),
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@ILOGIN_ID", Types.VARCHAR),
					new SqlParameter("@IBRANCH_ID", Types.VARCHAR),
					new SqlParameter("@IUSER_NAME", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.INTEGER),
					new SqlParameter("@IORDER_BY_COLUMN", Types.VARCHAR),
					new SqlParameter("@IORDER_ASC_DESC", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IPAGE_NO", userMasterVo.getPageNo())
					.addValue("@IPAGE_SIZE", Constants.MAX_PAGE_SIZE)
					.addValue("@ISEARCH_FOR", userMasterVo.getSearchCriteria())
					.addValue("@ILOGIN_ID", userMasterVo.getLoginId())
					.addValue("@IBRANCH_ID", userMasterVo.getBranchId())
					.addValue("@IUSER_NAME", userMasterVo.getUserName())
					.addValue("@IUSER_ID", userKey)
					.addValue("@IORDER_BY_COLUMN", (("").equalsIgnoreCase(userMasterVo.getOrderByColumn()) ? null : userMasterVo.getOrderByColumn()))
					.addValue("@IORDER_ASC_DESC", (("").equalsIgnoreCase(userMasterVo.getOrderAscDesc()) ? null : userMasterVo.getOrderAscDesc()));
			
			simpleJdbcCall.returningResultSet("getUserRoleMappingMasterList", new RowMapper<ArrayList<UserMasterVo>>() {
				
				@Override
				public ArrayList<UserMasterVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<UserMasterVo> userRoleMappingMasterListNew = new ArrayList<UserMasterVo>();
					try
					{
						while(rs.next())
						{
							UserMasterVo userMasterVo = new UserMasterVo();
							userMasterVo = new UserMasterVo();
							userMasterVo.setUserId(rs.getString("USER_ID"));
							userMasterVo.setLoginId(rs.getString("LOGIN_ID"));
							userMasterVo.setUserName(rs.getString("USER_NAME"));
							userMasterVo.setEmailId(rs.getString("EMAIL_ID"));
							userMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
							userMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
							userMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
							userMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
							userMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
							userMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
							userMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
							userMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
							userMasterVo.setBranchCode(rs.getString("BRANCH_DESCRIPTION"));
							userMasterVo.setRecordCount(rs.getInt("TOT_COUNT"));
							userRoleMappingMasterListNew.add(userMasterVo);
						}
						
						return userRoleMappingMasterListNew;
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
				userRoleMappingList = (ArrayList<UserMasterVo>) results.get("getUserRoleMappingMasterList");
				if(userRoleMappingList != null && userRoleMappingList.size() > 0)
				{
					System.out.println("loginList->"+userRoleMappingList.size());
					//loginVoNew = loginList.get(0);
					//System.out.println("getUserId-->"+loginVoNew.getUserId());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserRoleMappingMasterDaoImpl-getUserRoleMappingMasterList", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return userRoleMappingList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<UserMasterVo> fetchUserRoleMappingMasterDetails(String userId, String approveFlag, String userKey) {
		ArrayList<UserMasterVo> userRoleMappingList = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ATT_USER_USER_ROLES);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IUSER_ID", Types.INTEGER),
					new SqlParameter("@IAPPROVED_FLAG", Types.INTEGER)
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IUSER_ID", userId)
					.addValue("@IAPPROVED_FLAG", approveFlag);
			
			simpleJdbcCall.returningResultSet("getUserMasterDetails", new RowMapper<ArrayList<UserMasterVo>>() {
				
				@Override
				public ArrayList<UserMasterVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<UserMasterVo> userRoleMappingMasterListNew = new ArrayList<UserMasterVo>();
					try
					{
						while(rs.next())
						{
							UserMasterVo userMasterVo = new UserMasterVo();
							userMasterVo = new UserMasterVo();
							userMasterVo.setUserId(String.valueOf(rs.getInt("USER_ID")));
							userMasterVo.setUserRoleId(String.valueOf(rs.getInt("USER_ROLE_ID")));
							userMasterVo.setUserRoleName(rs.getString("USER_ROLE_NAME"));
							userMasterVo.setUserRoleProfileDesc(rs.getString("USER_ROLE_PROFILE_DESC"));
							userRoleMappingMasterListNew.add(userMasterVo);
						}
						
						return userRoleMappingMasterListNew;
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
				userRoleMappingList = (ArrayList<UserMasterVo>) results.get("getUserRoleMappingMasterList");
				if(userRoleMappingList != null && userRoleMappingList.size() > 0)
				{
					System.out.println("loginList->"+userRoleMappingList.size());
					//loginVoNew = loginList.get(0);
					//System.out.println("getUserId-->"+loginVoNew.getUserId());
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserRoleMappingMasterDaoImpl-getUserMasterDetails", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return userRoleMappingList;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String checkUserRoleMappingMaster(String selRec, String appRejRemark, String checkType, String userKey) {
		String errorMsg = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_CHECK_USER_ROLE_MAP);
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
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserRoleMappingMasterDaoImpl-checkUserMaster", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return errorMsg;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String assignUserRoleMappings(UserMasterVo userMasterVo, String sbSrNo, String userKey) {
		String errorMsg = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_MANAGE_USER_ROLE_MAPP);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IUSER_ID", Types.INTEGER),
					new SqlParameter("@IUSER_ROLE_ID", Types.VARCHAR),
					new SqlParameter("@ISTATUS", Types.INTEGER),
					new SqlParameter("@ICREATED_BY", Types.INTEGER)				
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IUSER_ID", userMasterVo.getUserId())
					.addValue("@IUSER_ROLE_ID", sbSrNo)
					.addValue("@ISTATUS", Constants.RECORD_INSERT_STATUS)
					.addValue("@ICREATED_BY", userKey);
			
			simpleJdbcCall.returningResultSet("assignUserRoleMappings", new RowMapper<String>() {
				
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
				List<String> UserDetailsList = (List<String>) results.get("assignUserRoleMappings");
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
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserRoleMappingMasterDaoImpl-assignUserRoleMappings", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return errorMsg;
	}
	
	
	
	
	

}
