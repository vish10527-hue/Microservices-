package in.ameya.dao.impl.master;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import in.ameya.dao.master.LoginDao;
import in.ameya.model.CommonDataException;
import in.ameya.model.DBResponse;
import in.ameya.model.LoginDetails;
import in.ameya.model.Menu;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.DBConstants;
import oracle.jdbc.OracleTypes;

@Repository
public class LoginDaoImpl extends JdbcDaoSupport implements LoginDao {

	private static final Logger logger = LogManager.getLogger(LoginDao.class);
	
	@Autowired
	@Qualifier("MainDatabase")
	protected DataSource dataSource;
	
	@Override
	public int checkUsername(String username) {
		AtomicInteger value = new AtomicInteger();
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_USER_COUNT_LOGIN);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("I_LOGIN_ID", OracleTypes.VARCHAR),
					new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR,
							new CountRowMapper(value))
					);
	
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("I_LOGIN_ID", username)
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				logger.debug("checkUsername("+ username +"): " + out.get("IO_CURSOR"));
			}
		}
		catch (Exception e) {
			logger.error("checkUsername("+ username +")", e);
			throw new RuntimeException("Error While Processing Login Info");
		}
		
		return value.intValue();
	}

	class CountRowMapper implements RowMapper<Integer> {
		
		private AtomicInteger value;
		public CountRowMapper(AtomicInteger value) {
			this.value = value;
		}

		@Override
		public Integer mapRow(ResultSet rs, int rowNumber) throws SQLException {
			this.value.addAndGet(rs.getInt(1));
			if(rowNumber > 0) this.value.set(0);
			return rs.getInt(1);
		}
	}

	@Override
	public Map<String, Object> updateAttempts(String username) {
		Map<String, Object> result = new HashMap<String, Object>(2);
		
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_CMN_UPDATE_LOGIN_ATTEMPT);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("I_LOGIN_ID", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
					);
	
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("I_LOGIN_ID", username)
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				result.put("status", out.get("O_RESULT"));
				result.put("message", out.get("O_MESSAGE"));
			}
		}
		catch (Exception e) {
			logger.error("checkAttempts("+ username +")", e);
			throw new RuntimeException("Error While Updating Login Attempts");
		}
		
		return result;
	}

	@Override
	public Map<String, Object> updateStatus(Integer userId, int status, String token) {
		Map<String, Object> result = new HashMap<String, Object>(2);
		
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_CMN_RESET_LOGIN_STATUS);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("I_USER_KEY", OracleTypes.NUMBER),
					new SqlParameter("I_LOGIN_STATUS", OracleTypes.NUMBER),
					new SqlParameter("I_BROWSER_SESS_ID", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
					);
	
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("I_USER_KEY", userId)
					.addValue("I_LOGIN_STATUS", status)
					.addValue("I_BROWSER_SESS_ID", token)
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				result.put("status", out.get("O_RESULT"));
				result.put("message", out.get("O_MESSAGE"));
			}
		}
		catch (Exception e) {
			logger.error("updateStatus("+ userId +") -> " + status, e);
			throw new RuntimeException("Error While Updating Login Status");
		}
		
		return result;
	}

	@Override
	public Integer fetchLoginDetails(LoginDetails user) {
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_CHECK_VALID_LOGIN);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("I_LOGIN_ID", OracleTypes.VARCHAR),
					new SqlParameter("I_USER_PASSWORD", OracleTypes.VARCHAR),
					new SqlParameter("I_BROWSER_SESS_ID", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR),
					new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new LoginDetailsRowMapper(user))
					);
	
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("I_LOGIN_ID", user.getLoginId())
					.addValue("I_USER_PASSWORD", user.getUserSessionId())
					.addValue("I_BROWSER_SESS_ID", "")
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				logger.debug("fetchLoginDetails(): " + out.get("IO_CURSOR"));
				if((out.get("O_RESULT").equals(BigDecimal.ONE))) {
					return user.getUserKey();
				} else {
					user.setGalUserFlag(((BigDecimal) out.get("O_RESULT")).intValueExact());
					user.setSystemAlert((String) out.get("O_MESSAGE"));
				}
			}
		}
		catch (Exception e) {
			logger.error("fetchLoginDetails(): " + user, e);
			throw new RuntimeException("Error While Processing Login Info");
		}
		
		return null;
	}

	class LoginDetailsRowMapper implements RowMapper<Integer> {
		
		private LoginDetails user;
		public LoginDetailsRowMapper(LoginDetails user) {
			this.user = user;
		}

		@Override
		public Integer mapRow(ResultSet rs, int rowNumber) throws SQLException {
			int i = rs.getInt(1);
			
			if(i == 0) {
				return null;
			} else {
				user.setUserKey(i);
				i = 2;
			}
			
			user.setLoginId(rs.getString(i++));
			user.setUserName(rs.getString(i++));
			user.setLastLoginDate(rs.getString(i++));
			user.setGalUserFlag(rs.getInt(i++));
			user.setUserSessionId(rs.getString(i++));
			user.setBranchCode(rs.getString(i++));
			user.setSystemAlert(rs.getString(i++));
			
			return rs.getInt(1);
		}
	}

	@Override
	public ArrayList<Menu> getMenus(Integer userId) {
		
		ArrayList<Menu> menus = new ArrayList<Menu>();
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SELECT_MENU_LIST);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("I_USER_KEY", OracleTypes.NUMBER),
					new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new MenuRowMapper(menus))
					);
	
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("I_USER_KEY", userId)
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				logger.debug("fetchLoginDetails(): " + out.get("IO_CURSOR"));
			}
		}
		catch (Exception e) {
			logger.error("getMenus(): " + userId, e);
			throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching Menu List"));
		}
		
		return menus;
	}

	
	class MenuRowMapper implements RowMapper<Menu> {
		
		private ArrayList<Menu> menus;
		public MenuRowMapper(ArrayList<Menu> menus) {
			this.menus = menus;
		}

		@Override
		public Menu mapRow(ResultSet rs, int rowNumber) throws SQLException {
			Menu menu = new Menu();
			
			menu.setKey(rs.getInt("MENU_KEY"));
			menu.setParentKey(rs.getInt("PARENT_KEY"));
			
			menu.setName(rs.getString("DISPLAY_NAME"));
			menu.setPath(rs.getString("FOLDER_PATH"));
			
			menu.setType(rs.getInt("MENU_TYPE"));
			menu.setOrder(rs.getInt("ORDER_BY"));
			
			menus.add(menu);
			return menu;
		}
	}

}
