package in.ameya.dao.impl.master;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import in.ameya.dao.master.UserDao;
import in.ameya.model.CommonDataException;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.UserDetails;
import in.ameya.model.master.UserSearch;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.DBConstants;
import in.ameya.util.ExportData;
import oracle.jdbc.OracleTypes;

@Repository
@PropertySource(value="classpath:filePath.properties")
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

	private static final Logger logger = LogManager.getLogger(UserDao.class);
	
	@Autowired
	ExportData exportData;	
		
	@Autowired
	@Qualifier("MainDatabase")
	protected DataSource dataSource;
	
	@SuppressWarnings("unchecked")
	@Override
	public DBResponse getUserList(UserSearch userSearch) {
		
		AtomicInteger totalCount = new AtomicInteger(0);
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_USER_MST_LIST);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("IPAGE_NO", OracleTypes.INTEGER),
					new SqlParameter("IPAGE_SIZE", OracleTypes.INTEGER),
					new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
					new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
					new SqlParameter("ILOGIN_ID", OracleTypes.VARCHAR),
					new SqlParameter("IUSER_NAME", OracleTypes.VARCHAR),
					new SqlParameter("IUSR_LOCK_STATUS", OracleTypes.INTEGER),
					new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
					new SqlParameter("ISTATUS", OracleTypes.INTEGER),
					new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new UserDetailsRowMapper(totalCount))
					);
	
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", userSearch.getLoggedInKey())
					.addValue("IPAGE_NO", userSearch.getPageNo())
					.addValue("IPAGE_SIZE", userSearch.getPageSize())
					.addValue("IORDER_BY_COLUMN", userSearch.getOrderByColumn())
					.addValue("IORDER_ASC_DESC", userSearch.getOrderAscDesc())
					.addValue("ILOGIN_ID", userSearch.getLoginId())
					.addValue("IUSER_NAME", userSearch.getUserName())
					.addValue("IUSR_LOCK_STATUS", userSearch.getUsrLockStatus())
					.addValue("IAPPROVED_FLAG", userSearch.getApprovedFlag())
					.addValue("ISTATUS", userSearch.getStatus())
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				userSearch.users = (ArrayList<UserDetails>) out.get("IO_CURSOR");
				userSearch.count = totalCount.intValue();
				return new DBResponse(1);
			} else {
				return new DBResponse(-1, "No result from query.");
			}
		}
		catch (Exception e) {
			logger.error("getUserList("+ userSearch +"): " + totalCount, e);
			throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching User List"));
		}
	}

	
	class UserDetailsRowMapper implements RowMapper<UserDetails> {
		
		private AtomicInteger count;
		public UserDetailsRowMapper(AtomicInteger count) {
			this.count = count;
		}

		@Override
		public UserDetails mapRow(ResultSet rs, int rowNumber) throws SQLException {
			this.count.accumulateAndGet(rs.getInt("TOT_COUNT"), Math::max);
			UserDetails user = new UserDetails();
			
			user.setUserKey(rs.getInt("USER_KEY"));
			user.setLoginId(rs.getString("LOGIN_ID"));
			user.setUserName(rs.getString("USER_NAME"));
			user.setBranchKey(rs.getInt("BRANCH_KEY"));
			user.setBranchCode(rs.getString("BRANCH_CODE"));
			user.setBranchDescription(rs.getString("BRANCH_DESCRIPTION"));
			user.setEmailId(rs.getString("EMAIL_ID"));
			user.setMobileNo(rs.getString("MOBILE_NO"));
			user.setUserPassword(rs.getString("USER_PASSWORD"));
			user.setPasswordExpyDate(rs.getString("PASSWORD_EXPY_DATE"));
			user.setStatus(rs.getInt("STATUS"));
			user.setGalUserFlag(rs.getInt("GAL_USER_FLAG"));
			user.setLoginAttempts(rs.getInt("LOGIN_ATTEMPTS"));
			user.setLoginStatus(rs.getInt("LOGIN_STATUS"));
			user.setUsrLockStatus(rs.getInt("USR_LOCK_STATUS"));
			user.setUsrDelStatus(rs.getInt("USR_DEL_STATUS"));
			user.setClientMachineIp(rs.getString("CLIENT_MACHINE_IP"));
			user.setUserSessionId(rs.getString("USER_SESSION_ID"));
			user.setBrowserSessionId(rs.getString("BROWSER_SESSION_ID"));
			user.setLastLoginDate(rs.getString("LAST_LOGIN_DATE"));
			user.setDeactiveTillDate(rs.getString("DEACTIVE_TILL_DATE"));
			user.setCreatedBy(rs.getInt("CREATED_BY"));
			user.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
			user.setCreatedOn(rs.getString("CREATED_ON"));
			user.setModifiedBy(rs.getInt("MODIFIED_BY"));
			user.setModifiedByLoginId(rs.getString("MODIFIED_BY_LOGIN_ID"));
			user.setModifiedOn(rs.getString("MODIFIED_ON"));
			user.setCheckedBy(rs.getInt("CHECKED_BY"));
			user.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
			user.setCheckedOn(rs.getString("CHECKED_ON"));
			user.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));
			user.setApprovedFlag(rs.getInt("APPROVED_FLAG"));
			user.setRefUserKey(rs.getInt("REF_USER_KEY"));
			user.setUserRoleNames(rs.getString("USER_ROLE_NAMES"));
			
			return user;
		}
	}


	@Override
	public DBResponse manageUser(UserDetails user, Integer loggedInKey, String action) {
		DBResponse resp = null;
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_USER_MASTER_MANAGE);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("IUSER_KEY", OracleTypes.NUMBER),
					new SqlParameter("ILOGIN_ID", OracleTypes.VARCHAR),
					new SqlParameter("IUSER_NAME", OracleTypes.VARCHAR),
					new SqlParameter("IBRANCH_KEY", OracleTypes.NUMBER),
					new SqlParameter("IEMAIL_ID", OracleTypes.VARCHAR),
					new SqlParameter("IMOBILE_NO", OracleTypes.VARCHAR),
					new SqlParameter("ISTATUS", OracleTypes.NUMBER),
					new SqlParameter("IGAL_USER_FLAG", OracleTypes.NUMBER),
					new SqlParameter("IUSR_LOCK_STATUS", OracleTypes.NUMBER),
					new SqlParameter("IUSR_DEL_STATUS", OracleTypes.NUMBER),
					new SqlParameter("IUSER_ROLE_KEYS", OracleTypes.VARCHAR),
					new SqlParameter("IAPP_REJ_REMARKS", OracleTypes.VARCHAR),
					new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
					new SqlParameter("IREF_USER_KEY", OracleTypes.NUMBER),
					new SqlParameter("IRECORD_STATUS", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
					);
	
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", loggedInKey)
					.addValue("IUSER_KEY", user.getUserKey())
					.addValue("ILOGIN_ID", user.getLoginId())
					.addValue("IUSER_NAME", user.getUserName())
					.addValue("IBRANCH_KEY", user.getBranchKey())
					.addValue("IEMAIL_ID", user.getEmailId())
					.addValue("IMOBILE_NO", user.getMobileNo())
					.addValue("ISTATUS", user.getStatus())
					.addValue("IGAL_USER_FLAG", "1")
					.addValue("IUSR_LOCK_STATUS", user.getUsrLockStatus())
					.addValue("IUSR_DEL_STATUS", user.getUsrDelStatus())
					.addValue("IUSER_ROLE_KEYS", user.getUserRoleKeys())
					.addValue("IAPP_REJ_REMARKS", user.getAppRejRemarks())
					.addValue("IAPPROVED_FLAG", user.getApprovedFlag())
					.addValue("IREF_USER_KEY", user.getRefUserKey())
					.addValue("IRECORD_STATUS", action)
					;
			
					Map<String, Object> out = simpleJdbcCall.execute(paramSource);
					if (out != null) {
						BigDecimal result = (BigDecimal) out.get("O_RESULT");
						resp = new DBResponse(result.intValue(), (String) out.get("O_MESSAGE"), user);

						if (resp.getStatus() == 1) {
							return resp;
						}
					}
		}
		catch (Exception e) {
			logger.error("manageUser("+action+"): " + user, e);
			throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching User List"));
		}
		if(resp == null) {
			resp = new DBResponse(-1, null);
		}
		return resp;
	}


	@Override
	public DBResponse verifyUserRecords(Integer loggedInKey, String userKeys, String remarks, String recordStatus) {
		DBResponse resp = null;
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_USER_MASTER_CHECK);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("IUSER_KEYS", OracleTypes.VARCHAR),
					new SqlParameter("IAPP_REJ_REMARKS", OracleTypes.VARCHAR),
					new SqlParameter("IRECORD_STATUS", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
					);
	
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", loggedInKey)
					.addValue("IUSER_KEYS", userKeys)
					.addValue("IAPP_REJ_REMARKS", remarks)
					.addValue("IRECORD_STATUS", recordStatus)
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				resp = new DBResponse((BigDecimal)out.get("O_RESULT"), (String) out.get("O_MESSAGE"));
			} 
		}
		catch (Exception e) {
			logger.error("manageUser("+recordStatus+"): " + userKeys + " -> " + remarks, e);
			resp = new DBResponse(-1, "Error While Verifing User Records");
		}
		
		if(resp == null) {
			resp = new DBResponse(-1, "No Records Found.");
		}
		
		return resp;
	}
	
	
	@Override
	public String exportUserCSV(UserSearch userSearch) {
	    SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_USER_MST_EXP);

	    simpleJdbcCall.declareParameters(
	            new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	            new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
	            new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlParameter("ILOGIN_ID", OracleTypes.VARCHAR),
	            new SqlParameter("IUSER_NAME", OracleTypes.VARCHAR),
	            new SqlParameter("IUSR_LOCK_STATUS", OracleTypes.INTEGER),
	            new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	            new SqlParameter("ISTATUS", OracleTypes.INTEGER),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("ILOGGEDIN_KEY", userSearch.getLoggedInKey())
	            .addValue("IORDER_BY_COLUMN", userSearch.getOrderByColumn())
	            .addValue("IORDER_ASC_DESC", userSearch.getOrderAscDesc())
	            .addValue("ILOGIN_ID", userSearch.getLoginId())
	            .addValue("IUSER_NAME", userSearch.getUserName())
	            .addValue("IUSR_LOCK_STATUS", userSearch.getUsrLockStatus())
	            .addValue("IAPPROVED_FLAG", userSearch.getApprovedFlag())
	            .addValue("ISTATUS", userSearch.getStatus());

	    Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	    if (out != null) {
	        @SuppressWarnings("unchecked")
	        ArrayList<Map<String, Object>> resultSet = (ArrayList<Map<String, Object>>) out.get("IO_CURSOR");

	        if (resultSet != null && !resultSet.isEmpty()) {
	            String csvContent = exportData.generateCSV(resultSet);

	            String base64Csv = exportData.encodeBase64(csvContent);

	            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	            String filename = "UserMaster_" + timestamp + ".csv";

	            return filename + "|" + base64Csv;
	        } else {
	            return "No data found";
	        }
	    } else {
	        return "Error during CSV generation";
	    }
	}

	
	@Override
	public ExportResponse exportUserExcel(UserSearch userSearch) throws IOException {
	    SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_USER_MST_EXP);

	    simpleJdbcCall.declareParameters(
	            new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	            new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
	            new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlParameter("ILOGIN_ID", OracleTypes.VARCHAR),
	            new SqlParameter("IUSER_NAME", OracleTypes.VARCHAR),
	            new SqlParameter("IUSR_LOCK_STATUS", OracleTypes.INTEGER),
	            new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	            new SqlParameter("ISTATUS", OracleTypes.INTEGER),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("ILOGGEDIN_KEY", userSearch.getLoggedInKey())
	            .addValue("IORDER_BY_COLUMN", userSearch.getOrderByColumn())
	            .addValue("IORDER_ASC_DESC", userSearch.getOrderAscDesc())
	            .addValue("ILOGIN_ID", userSearch.getLoginId())
	            .addValue("IUSER_NAME", userSearch.getUserName())
	            .addValue("IUSR_LOCK_STATUS", userSearch.getUsrLockStatus())
	            .addValue("IAPPROVED_FLAG", userSearch.getApprovedFlag())
	            .addValue("ISTATUS", userSearch.getStatus());

	    Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	    @SuppressWarnings("unchecked")
	    ArrayList<Map<String, Object>> resultSet = (ArrayList<Map<String, Object>>) out.get("IO_CURSOR");

	    if (resultSet != null && !resultSet.isEmpty()) {
	        List<String> columnOrder = new ArrayList<>(resultSet.get(0).keySet());

	        ArrayList<JSONObject> jsonData = new ArrayList<>();
	        for (Map<String, Object> row : resultSet) {
	            JSONObject jsonObject = new JSONObject(row);
	            jsonData.add(jsonObject);
	        }

	        String filename = "UserMaster_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
	        String base64Data = exportData.generateExcelBase64(jsonData, columnOrder);

	        return new ExportResponse(base64Data, filename);
	    }

	    return new ExportResponse(null, null);
	}

	    
	    @Override
	    public UserDetails getUserDetailsById(Integer userKey, Integer userId) {
	        try {
	            SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_USER_MST_DET);

	            simpleJdbcCall.declareParameters(
	                    new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	                    new SqlParameter("IUSER_KEY", OracleTypes.NUMBER),
	                    new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new UserDetailsViewRowMapper())
	            );

	            SqlParameterSource paramSource = new MapSqlParameterSource()
	                    .addValue("ILOGGEDIN_KEY", userId)
	                    .addValue("IUSER_KEY", userKey);

	            Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	            if (out != null && out.get("IO_CURSOR") != null) {
	                @SuppressWarnings("unchecked")
					List<UserDetails> userList = (List<UserDetails>) out.get("IO_CURSOR");
	                if (!userList.isEmpty()) {
	                    return userList.get(0); 
	                } else {
	                    throw new CommonDataException(HttpStatus.NOT_FOUND, new DBResponse(-1, "User not found for ID: " + userId));
	                }
	            } else {
	                throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "No data returned from procedure."));
	            }
	        } catch (Exception e) {
	            logger.error("Error in getUserDetailsById(userId=" + userId + ", loggedInUserId=" + userId + "): ", e);
	            throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error fetching user details"));
	        }
	    }
	    
	    class UserDetailsViewRowMapper implements RowMapper<UserDetails> {

	        @Override
	        public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
	            UserDetails user = new UserDetails();

	            user.setUserKey(rs.getInt("USER_KEY"));
	            user.setLoginId(rs.getString("LOGIN_ID"));
	            user.setUserName(rs.getString("USER_NAME"));
	            user.setBranchKey(rs.getInt("BRANCH_KEY"));
	            user.setBranchCode(rs.getString("BRANCH_CODE"));
	            user.setBranchDescription(rs.getString("BRANCH_DESCRIPTION"));
	            user.setEmailId(rs.getString("EMAIL_ID"));
	            user.setMobileNo(rs.getString("MOBILE_NO"));
	            user.setUserPassword(rs.getString("USER_PASSWORD"));
	            user.setPasswordExpyDate(rs.getString("PASSWORD_EXPY_DATE"));
	            user.setStatus(rs.getInt("STATUS"));
	            user.setGalUserFlag(rs.getInt("GAL_USER_FLAG"));
	            user.setLoginAttempts(rs.getInt("LOGIN_ATTEMPTS"));
	            user.setLoginStatus(rs.getInt("LOGIN_STATUS"));
	            user.setUsrLockStatus(rs.getInt("USR_LOCK_STATUS"));
	            user.setUsrDelStatus(rs.getInt("USR_DEL_STATUS"));
	            user.setClientMachineIp(rs.getString("CLIENT_MACHINE_IP"));
	            user.setUserSessionId(rs.getString("USER_SESSION_ID"));
	            user.setBrowserSessionId(rs.getString("BROWSER_SESSION_ID"));
	            user.setLastLoginDate(rs.getString("LAST_LOGIN_DATE"));
	            user.setDeactiveTillDate(rs.getString("DEACTIVE_TILL_DATE"));
	            user.setCreatedBy(rs.getInt("CREATED_BY"));
	            user.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
	            user.setCreatedOn(rs.getString("CREATED_ON"));
	            user.setModifiedBy(rs.getInt("MODIFIED_BY"));
	            user.setModifiedByLoginId(rs.getString("MODIFIED_BY_LOGIN_ID"));
	            user.setModifiedOn(rs.getString("MODIFIED_ON"));
	            user.setCheckedBy(rs.getInt("CHECKED_BY"));
	            user.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
	            user.setCheckedOn(rs.getString("CHECKED_ON"));
	            user.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));
	            user.setApprovedFlag(rs.getInt("APPROVED_FLAG"));
	            user.setRefUserKey(rs.getInt("REF_USER_KEY"));
	            user.setUserRoleKeys(rs.getString("USER_ROLE_KEYS"));
	            user.setUserRoleNames(rs.getString("USER_ROLE_NAMES"));
	            return user;
	        }
	    }

}
