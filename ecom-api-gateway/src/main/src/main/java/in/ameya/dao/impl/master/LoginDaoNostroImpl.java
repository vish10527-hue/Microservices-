package in.ameya.dao.impl.master;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import in.ameya.dao.master.LoginDaoNostro;
import in.ameya.model.master.LoginVo;
import in.ameya.model.master.MenuVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.securities.passwordutility.SHAPasswordEncoder;
import in.ameya.util.DBConstants;
import in.ameya.util.LoggerUtil;


@Repository
public class LoginDaoNostroImpl extends JdbcDaoSupport  implements LoginDaoNostro{

	@Autowired
	private LoggerUtil loggerUtil;
	
	
	@Override
	public LoginVo getLoginDetails(LoginVo loginVo) {
		
		LoginVo loginVoNew = new LoginVo();
		
		try
		{
			//SimpleDateFormat sf = new SimpleDateFormat(AmeyaUtil.DATE_FORMAT_1);
			SHAPasswordEncoder spe = new SHAPasswordEncoder();
			String dycriptPassword = spe.digestPassword(loginVo.getPassword());
			
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_CHECK_VALID_LOGIN_SSO);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_USER_ID", Types.INTEGER),
					new SqlParameter("@I_LOGIN_ID", Types.VARCHAR),
					new SqlParameter("@I_USER_PASSWORD", Types.VARCHAR),
					new SqlParameter("@I_BROWSER_SESS_ID", Types.VARCHAR));
					//new SqlParameter("o_token", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_USER_ID", loginVo.getUserId())
					.addValue("@I_LOGIN_ID", loginVo.getLoginId())
					.addValue("@I_USER_PASSWORD", dycriptPassword)
					.addValue("@I_BROWSER_SESS_ID", loginVo.getSessionId());
			
			simpleJdbcCall.returningResultSet("loginDetails", new RowMapper<LoginVo>() {
				
				@Override
				public LoginVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					LoginVo loginVo = new LoginVo();
					String errorCode = rs.getString(1);						
					if(errorCode != null)
					{
						String message = rs.getString(2);
						if(!errorCode.equalsIgnoreCase("-25")) 
						{
							loginVo.setUserId(rs.getString("USER_ID"));
							loginVo.setLoginId(rs.getString("LOGIN_ID"));
							loginVo.setUserName(rs.getString("USER_NAME"));
							loginVo.setLastLoginDate(rs.getString("LAST_LOGIN_DATE"));
							loginVo.setUserRoleName(rs.getString("USER_ROLE_NAME"));
							
							/*loginVo.setIsGalUser(rs.getString("GAL_USER_FLAG"));
							loginVo.setSessionId(rs.getString("USER_SESSION_ID"));
							loginVo.setSystemAlert(rs.getString("SYSTEM_ALERT"));
							loginVo.setBranchCode(rs.getString("BRANCH_CODE"));	
							loginVo.setBranchId(rs.getString("BRANCH_ID"));
							loginVo.setBranchDescription(rs.getString("BRANCH_DESCRIPTION"));*/
							loginVo.setErrorCode("1");
							loginVo.setErrorDesc("Success");
							loginVo.setBranchDescription("");
						}
						else
						{
							loginVo.setErrorCode(errorCode);
							loginVo.setErrorDesc(message);
						}
					}
					return loginVo;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				@SuppressWarnings("unchecked")
				List<LoginVo> loginList = (List<LoginVo>) results.get("loginDetails");
				if(loginList != null && loginList.size() > 0)
				{
					loginVoNew = loginList.get(0);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "LoginDaoImpl-getUserMasterList", loginVo.getLoginId());
			throw new RuntimeException("Something went wrong please try again");
		}
	 return loginVoNew;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<MenuVo> getMenuList(String userId) {
		
		ArrayList<MenuVo> menuList = new ArrayList<MenuVo>();
		
		try
		{
			
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_SELECT_MENU_LIST_SSO);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_USER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_USER_ID", userId);
			
			simpleJdbcCall.returningResultSet("getMenuList", new RowMapper<MenuVo>() {
				
				@Override
				public MenuVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					MenuVo menuVo = new MenuVo();
					menuVo.setMenuId(rs.getString("MENU_ID"));
					menuVo.setMenuParentId(rs.getLong("PARENT_ID"));
					menuVo.setDisplayName(rs.getString("DISPLAY_NAME"));
					menuVo.setFolderPath(rs.getString("FOLDER_PATH"));
					menuVo.setMenuType(rs.getString("MENU_TYPE"));
					menuVo.setActionsInfo(rs.getString("ACTIONS_INFO"));
					return menuVo;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				menuList = (ArrayList<MenuVo>) results.get("getMenuList");				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "LoginDaoImpl-getMenuList", userId);
			throw new RuntimeException("Something went wrong please try again");
		}
		
	 return menuList;
	}
	
	
	@Override
	public void logoutUserSession(LoginVo loginVo) 
	{				
		try
		{
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_LOGOUT_USER_SESSION_SSO);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_USER_ID", Types.INTEGER),
					new SqlParameter("@I_LOGIN_ID", Types.VARCHAR),
					new SqlParameter("@I_BROWSER_SESS_ID", Types.VARCHAR));
					//new SqlParameter("o_token", Types.VARCHAR));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_USER_ID", loginVo.getUserId())
					.addValue("@I_LOGIN_ID", loginVo.getLoginId())
					.addValue("@I_BROWSER_SESS_ID", loginVo.getSessionId());
			
			simpleJdbcCall.returningResultSet("loginDetails", new RowMapper<LoginVo>() {
				
				@Override
				public LoginVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					LoginVo loginVo = new LoginVo();
					String errorCode = rs.getString(1);						
					loginVo.setErrorCode(errorCode);
					return loginVo;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			simpleJdbcCall.execute(params);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "LoginDaoImpl-logoutUserSession", loginVo.getLoginId());
			throw new RuntimeException("Something went wrong please try again");
		}
	}


	//@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, String> getUserPrivilageList(String userId) {
		
		HashMap<String, String> userPrivilageMap = new HashMap<String, String>();
		/*ArrayList<MenuVo> privList = null;
		try
		{
			
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_SELECT_USER_PRIVI_LIST);
			simpleJdbcCall.declareParameters(
					new SqlParameter("@I_USER_ID", Types.INTEGER));
	
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("@I_USER_ID", userId);
			
			simpleJdbcCall.returningResultSet("getUserPrivilageList", new RowMapper<MenuVo>() {
				
				@Override
				public MenuVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					MenuVo menuVo = new MenuVo(); 
					menuVo.setPrivilegeId(rs.getString("PRIVILEGE_ID"));
					return menuVo;
				}
			});
			
			// turn off processing parameter metadata information 
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
			
			Map<String, Object> results = simpleJdbcCall.execute(params);
			
			if (results != null) 
			{			
				privList = (ArrayList<MenuVo>) results.get("getUserPrivilageList");
				if(privList != null)
				{
					long listSize = privList.size();
					for(int i = 0; i < listSize; i++)
					{
						MenuVo menuVo = privList.get(i);
						userPrivilageMap.put(menuVo.getPrivilegeId(), userId);
					}
				}	
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "LoginDaoImpl-getUserPrivilageList", userId);
			throw new RuntimeException("Something went wrong please try again");
		}*/
		
	 return userPrivilageMap;
	}

	
}
