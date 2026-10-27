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

import in.ameya.dao.master.UserRolePrivilegeMasterDao;
import in.ameya.model.master.UserMasterVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.Constants;
import in.ameya.util.DBConstants;
import in.ameya.util.LoggerUtil;

@PropertySource(value="classpath:filePath.properties")
@Repository
public class UserRolePrivilegeMasterDaoImpl extends JdbcDaoSupport implements UserRolePrivilegeMasterDao{
		
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private Environment env;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<UserMasterVo> fetchUserRolePrivilegeMasterList(UserMasterVo userMasterVo, String userKey) {
		ArrayList<UserMasterVo> userRolePrivilegeList = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ATT_USR_ROLE_PRIV_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IPAGE_NO", Types.INTEGER),
					new SqlParameter("@IPAGE_SIZE", Types.INTEGER),
					new SqlParameter("@ISEARCH_FOR", Types.INTEGER),
					new SqlParameter("@IUSER_ROLE_NAME", Types.VARCHAR),
					new SqlParameter("@IUSER_ID", Types.VARCHAR),
					new SqlParameter("@IORDER_BY_COLUMN", Types.VARCHAR),
					new SqlParameter("@IORDER_ASC_DESC", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IPAGE_NO", userMasterVo.getPageNo())
					.addValue("@IPAGE_SIZE", Constants.MAX_PAGE_SIZE)
					.addValue("@ISEARCH_FOR", userMasterVo.getSearchCriteria())
					.addValue("@IUSER_ROLE_NAME", userMasterVo.getUserRoleName())
					.addValue("@IUSER_ID", userKey)
					.addValue("@IORDER_BY_COLUMN", (("").equalsIgnoreCase(userMasterVo.getOrderByColumn()) ? null : userMasterVo.getOrderByColumn()))
					.addValue("@IORDER_ASC_DESC", (("").equalsIgnoreCase(userMasterVo.getOrderAscDesc()) ? null : userMasterVo.getOrderAscDesc()));
			
			simpleJdbcCall.returningResultSet("getUserRolePrivilegeMasterList", new RowMapper<ArrayList<UserMasterVo>>() {
				
				@Override
				public ArrayList<UserMasterVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<UserMasterVo> userRolePrivilegeMasterListNew = new ArrayList<UserMasterVo>();
					try
					{
						while(rs.next())
						{
							UserMasterVo userMasterVo = new UserMasterVo();
							userMasterVo = new UserMasterVo();
							userMasterVo.setUserRoleId(rs.getString("USER_ROLE_ID"));
							userMasterVo.setUserRoleProfileDesc(rs.getString("USER_ROLE_PROFILE_DESC"));
							userMasterVo.setUserRoleCode(rs.getString("USER_ROLE_CODE"));
							userMasterVo.setUserRoleName(rs.getString("USER_ROLE_NAME"));
							userMasterVo.setUserType(rs.getString("USER_TYPE"));				
							//userMasterVo.setStatus(rs.getString("STATUS"));	
							userMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
							userMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
							userMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
							userMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
							userMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
							userMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
							userMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
							userMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
							userRolePrivilegeMasterListNew.add(userMasterVo);
						}
						
						return userRolePrivilegeMasterListNew;
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
				userRolePrivilegeList = (ArrayList<UserMasterVo>) results.get("getUserRolePrivilegeMasterList");
				if(userRolePrivilegeList != null && userRolePrivilegeList.size() > 0)
				{
					System.out.println("loginList->"+userRolePrivilegeList.size());
					//loginVoNew = loginList.get(0);
					//System.out.println("getUserId-->"+loginVoNew.getUserId());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserRolePrivilegeMasterDaoImpl-getUserRolePrivilegeMasterList", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return userRolePrivilegeList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<UserMasterVo> fetchUserRolePrivilegeMasterDetails(String userRoleId, String approveFlag, String userKey) {
		ArrayList<UserMasterVo> userRolePrivilegeList = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ATT_USR_ROLE_PRIV);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IUSER_ROLE_ID", Types.INTEGER),
					new SqlParameter("@IAPPROVED_FLAG", Types.INTEGER)
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IUSER_ROLE_ID", userRoleId)
					.addValue("@IAPPROVED_FLAG", approveFlag);
			
			simpleJdbcCall.returningResultSet("getUserMasterDetails", new RowMapper<ArrayList<UserMasterVo>>() {
				
				@Override
				public ArrayList<UserMasterVo> mapRow(ResultSet rs, int rowNum) throws SQLException {
					ArrayList<UserMasterVo> userRolePrivilegeMasterListNew = new ArrayList<UserMasterVo>();
					try
					{
						while(rs.next())
						{
							UserMasterVo userMasterVo = new UserMasterVo();
							userMasterVo = new UserMasterVo();
							userMasterVo.setUserRoleId(rs.getString("USER_ROLE_ID"));
							userMasterVo.setUserRoleProfileDesc(rs.getString("USER_ROLE_PROFILE_DESC"));
							userMasterVo.setUserRoleCode(rs.getString("USER_ROLE_CODE"));
							userMasterVo.setUserRoleName(rs.getString("USER_ROLE_NAME"));
							userMasterVo.setUserType(rs.getString("USER_TYPE"));				
							//userMasterVo.setStatus(rs.getString("STATUS"));	
							userMasterVo.setAppRejRemark(rs.getString("APP_REJ_REMARKS"));	
							userMasterVo.setApproveFlag(rs.getString("APPROVED_FLAG"));
							userMasterVo.setCreatedBy(rs.getString("CREATED_BY_LOGIN_ID"));
							userMasterVo.setModifiedBy(rs.getString("MODIFIED_BY_LOGIN_ID"));
							userMasterVo.setApprovedBy(rs.getString("CHECKED_BY_LOGIN_ID"));
							userMasterVo.setCreatedDate(rs.getString("CREATED_ON"));
							userMasterVo.setModifiedDate(rs.getString("MODIFIED_ON"));
							userMasterVo.setApprovedDate(rs.getString("CHECKED_ON"));
							userRolePrivilegeMasterListNew.add(userMasterVo);
						}
						
						return userRolePrivilegeMasterListNew;
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
				userRolePrivilegeList = (ArrayList<UserMasterVo>) results.get("getUserRolePrivilegeMasterList");
				if(userRolePrivilegeList != null && userRolePrivilegeList.size() > 0)
				{
					System.out.println("loginList->"+userRolePrivilegeList.size());
					//loginVoNew = loginList.get(0);
					//System.out.println("getUserId-->"+loginVoNew.getUserId());
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserRolePrivilegeMasterDaoImpl-getUserMasterDetails", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return userRolePrivilegeList;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String checkUserRolePrivilegeMaster(String selRec, String appRejRemark, String checkType, String userKey) {
		String errorMsg = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_CHK_USER_ROLE_PREV_MAP);
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
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserRolePrivilegeMasterDaoImpl-checkUserMaster", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return errorMsg;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String assignUserRolePrivileges(UserMasterVo userMasterVo, String sbSrNo, String userKey) {
		String errorMsg = null;
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ADM_MNGE_USR_RLE_PREV_MPP);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@IUSER_ROLE_ID", Types.INTEGER),
					new SqlParameter("@IROLE_PRVE_ID", Types.VARCHAR),
					new SqlParameter("@ISTATUS", Types.INTEGER),
					new SqlParameter("@ICREATED_BY", Types.INTEGER)				
					);
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@IUSER_ROLE_ID", userMasterVo.getUserRoleId())
					.addValue("@IROLE_PRVE_ID", sbSrNo)
					.addValue("@ISTATUS", Constants.RECORD_INSERT_STATUS)
					.addValue("@ICREATED_BY", userKey);
			
			simpleJdbcCall.returningResultSet("assignUserRolePrivileges", new RowMapper<String>() {
				
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
				List<String> UserDetailsList = (List<String>) results.get("assignUserRolePrivileges");
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
			loggerUtil.logErrorInsert(e.getMessage(), e, "UserRolePrivilegeMasterDaoImpl-assignUserRolePrivileges", userKey);
			throw new RuntimeException("Something went wrong please try again");
		}
		return errorMsg;
	}
	
	
	
	
	

}
