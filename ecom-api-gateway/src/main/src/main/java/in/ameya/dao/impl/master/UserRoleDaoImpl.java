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

import in.ameya.dao.master.UserRoleDao;
import in.ameya.model.CommonDataException;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.UserRoleDetails;
import in.ameya.model.master.UserRoleSearch;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.DBConstants;
import in.ameya.util.ExportData;
import oracle.jdbc.OracleTypes;

@Repository
@PropertySource(value="classpath:filePath.properties")
public class UserRoleDaoImpl extends JdbcDaoSupport implements UserRoleDao {

	private static final Logger logger = LogManager.getLogger(UserRoleDao.class);
	
	@Autowired
	ExportData exportData;	
	
	@Autowired
	@Qualifier("MainDatabase")
	protected DataSource dataSource;
	
	@SuppressWarnings("unchecked")
	@Override
	public DBResponse getUserRoleList(UserRoleSearch userRoleSearch) {
		
		AtomicInteger totalCount = new AtomicInteger(0);
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_USER_ROLE_MST_LIST);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("IPAGE_NO", OracleTypes.INTEGER),
					new SqlParameter("IPAGE_SIZE", OracleTypes.INTEGER),
					new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
					new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
					new SqlParameter("IUSER_ROLE_CODE", OracleTypes.VARCHAR),
					new SqlParameter("IUSER_ROLE_NAME", OracleTypes.VARCHAR),
					new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
					new SqlParameter("ISTATUS", OracleTypes.INTEGER),
					new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new UserRoleDetailsRowMapper(totalCount))
					);
			
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", userRoleSearch.getLoggedInKey())
					.addValue("IPAGE_NO", userRoleSearch.getPageNo())
					.addValue("IPAGE_SIZE", userRoleSearch.getPageSize())
					.addValue("IORDER_BY_COLUMN", userRoleSearch.getOrderByColumn())
					.addValue("IORDER_ASC_DESC", userRoleSearch.getOrderAscDesc())
					.addValue("IUSER_ROLE_CODE", userRoleSearch.getUserRoleCode())
					.addValue("IUSER_ROLE_NAME", userRoleSearch.getUserRoleName())
					.addValue("IAPPROVED_FLAG", userRoleSearch.getApprovedFlag())
					.addValue("ISTATUS", userRoleSearch.getStatus()) 
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				userRoleSearch.userRoles = (ArrayList<UserRoleDetails>) out.get("IO_CURSOR");
				userRoleSearch.count = totalCount.intValue();
				return new DBResponse(1);
			} else {
				return new DBResponse(-1, "No result from query.");
			}
		}
		catch (Exception e) {
			logger.error("getUserRoleList("+ userRoleSearch +"): " + totalCount, e);
			throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching UserRole List"));
		}
	}

	
	class UserRoleDetailsRowMapper implements RowMapper<UserRoleDetails> {
		
		private AtomicInteger count;
		public UserRoleDetailsRowMapper(AtomicInteger count) {
			this.count = count;
		}

		@Override
		public UserRoleDetails mapRow(ResultSet rs, int rowNumber) throws SQLException {
			this.count.accumulateAndGet(rs.getInt("TOT_COUNT"), Math::max);
			UserRoleDetails userRole = new UserRoleDetails();
			
			userRole.setUserRoleKey(rs.getInt("USER_ROLE_KEY"));
			userRole.setUserRoleCode(rs.getString("USER_ROLE_CODE"));
			userRole.setUserRoleName(rs.getString("USER_ROLE_NAME"));
			userRole.setStatus(rs.getInt("STATUS"));
			userRole.setCreatedBy(rs.getInt("CREATED_BY"));
			userRole.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
			userRole.setCreatedOn(rs.getString("CREATED_ON"));
			userRole.setModifiedBy(rs.getInt("MODIFIED_BY"));
			userRole.setModifiedByLoginId(rs.getString("MODIFIED_BY_LOGIN_ID"));
			userRole.setModifiedOn(rs.getString("MODIFIED_ON"));
			userRole.setCheckedBy(rs.getInt("CHECKED_BY"));
			userRole.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
			userRole.setCheckedOn(rs.getString("CHECKED_ON"));
			userRole.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));
			userRole.setApprovedFlag(rs.getInt("APPROVED_FLAG"));
			userRole.setRefUserRoleKey(rs.getInt("REF_USER_ROLE_KEY"));
			
			return userRole;
		}
	}


	@Override
	public DBResponse manageUserRole(UserRoleDetails userRole, Integer loggedInKey, String action) {
		DBResponse resp = null;
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_USER_ROLE_MASTER_MANAGE);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("IUSER_ROLE_KEY", OracleTypes.NUMBER),
					new SqlParameter("IUSER_ROLE_CODE", OracleTypes.VARCHAR),
					new SqlParameter("IUSER_ROLE_NAME", OracleTypes.VARCHAR),
					new SqlParameter("ISTATUS", OracleTypes.NUMBER),
					new SqlParameter("IAPP_REJ_REMARKS", OracleTypes.VARCHAR),
					new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
					new SqlParameter("IREF_USER_ROLE_KEY", OracleTypes.NUMBER),
					new SqlParameter("IRECORD_STATUS", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
					);
			
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", loggedInKey)
					.addValue("IUSER_ROLE_KEY", userRole.getUserRoleKey())
					.addValue("IUSER_ROLE_CODE", userRole.getUserRoleCode())
					.addValue("IUSER_ROLE_NAME", userRole.getUserRoleName())
					.addValue("ISTATUS", userRole.getStatus())
					.addValue("IAPP_REJ_REMARKS", userRole.getAppRejRemarks())
					.addValue("IAPPROVED_FLAG", userRole.getApprovedFlag())
					.addValue("IREF_USER_ROLE_KEY", userRole.getRefUserRoleKey())
					.addValue("IRECORD_STATUS", action)
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if (out != null) {
				BigDecimal result = (BigDecimal) out.get("O_RESULT");
				resp = new DBResponse(result.intValue(), (String) out.get("O_MESSAGE"), result);

				if (resp.getStatus() == 1) {
					return resp;
				}
			}
		}
		catch (Exception e) {
			logger.error("manageUserRole("+action+"): " + userRole, e);
			throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching UserRole List"));
		}
		
		if(resp == null) {
			resp = new DBResponse(-1, null);
		}
		
		return resp;
	}


	@Override
	public DBResponse verifyUserRoleRecords(Integer loggedInKey, String userRoleKeys, String remarks, String recordStatus) {
		DBResponse resp = null;
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_USER_ROLE_MASTER_CHECK);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("IUSER_ROLE_KEYS", OracleTypes.VARCHAR),
					new SqlParameter("IAPP_REJ_REMARKS", OracleTypes.VARCHAR),
					new SqlParameter("IRECORD_STATUS", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
					);
	
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", loggedInKey)
					.addValue("IUSER_ROLE_KEYS", ("").equalsIgnoreCase(userRoleKeys) ? null : userRoleKeys)
					.addValue("IAPP_REJ_REMARKS", ("").equalsIgnoreCase(remarks) ? null : remarks)
					.addValue("IRECORD_STATUS", ("").equalsIgnoreCase(recordStatus) ? null : recordStatus)
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				resp = new DBResponse((BigDecimal)out.get("O_RESULT"), (String) out.get("O_MESSAGE"));
			} 
		}
		catch (Exception e) {
			logger.error("manageUserRole("+recordStatus+"): " + userRoleKeys + " -> " + remarks, e);
			resp = new DBResponse(-1, "Error While Verifing UserRole Records");
		}
		
		if(resp == null) {
			resp = new DBResponse(-1, "No Records Found.");
		}
		
		return resp;
	}
	
	
	@Override
	public String exportUserRoleCSV(UserRoleSearch userRoleSearch) {
	    SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_USER_ROLE_MST_EXP);

	    simpleJdbcCall.declareParameters(
	            new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	            new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
	            new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlParameter("IUSER_ROLE_CODE", OracleTypes.VARCHAR),
	            new SqlParameter("IUSER_ROLE_NAME", OracleTypes.VARCHAR),
	            new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	            new SqlParameter("ISTATUS", OracleTypes.INTEGER),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("ILOGGEDIN_KEY", userRoleSearch.getLoggedInKey())
	            .addValue("IORDER_BY_COLUMN", userRoleSearch.getOrderByColumn())
	            .addValue("IORDER_ASC_DESC", userRoleSearch.getOrderAscDesc())
	            .addValue("IUSER_ROLE_CODE", userRoleSearch.getUserRoleCode())
	            .addValue("IUSER_ROLE_NAME", userRoleSearch.getUserRoleName())
	            .addValue("IAPPROVED_FLAG", userRoleSearch.getApprovedFlag())
	            .addValue("ISTATUS", userRoleSearch.getStatus());

	    Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	    if (out != null) {
	        @SuppressWarnings("unchecked")
	        ArrayList<Map<String, Object>> resultSet = (ArrayList<Map<String, Object>>) out.get("IO_CURSOR");

	        if (resultSet != null && !resultSet.isEmpty()) {
	            String csvContent = exportData.generateCSV(resultSet);

	            String base64Csv = exportData.encodeBase64(csvContent);

	            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	            String filename = "UserRoleMaster_" + timestamp + ".csv";

	            return filename + "|" + base64Csv;
	        } else {
	            return "No data found";
	        }
	    } else {
	        return "Error during CSV generation";
	    }
	}

	
	@Override
	public ExportResponse exportUserRoleExcel(UserRoleSearch userRoleSearch) throws IOException {
	    SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_USER_ROLE_MST_EXP);

	    simpleJdbcCall.declareParameters(
	            new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	            new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
	            new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlParameter("IUSER_ROLE_CODE", OracleTypes.VARCHAR),
	            new SqlParameter("IUSER_ROLE_NAME", OracleTypes.VARCHAR),
	            new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	            new SqlParameter("ISTATUS", OracleTypes.INTEGER),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("ILOGGEDIN_KEY", userRoleSearch.getLoggedInKey())
	            .addValue("IORDER_BY_COLUMN", userRoleSearch.getOrderByColumn())
	            .addValue("IORDER_ASC_DESC", userRoleSearch.getOrderAscDesc())
	            .addValue("IUSER_ROLE_CODE", userRoleSearch.getUserRoleCode())
	            .addValue("IUSER_ROLE_NAME", userRoleSearch.getUserRoleName())
	            .addValue("IAPPROVED_FLAG", userRoleSearch.getApprovedFlag())
	            .addValue("ISTATUS", userRoleSearch.getStatus());

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

	        String filename = "UserRoleMaster_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
	        String base64Data = exportData.generateExcelBase64(jsonData, columnOrder);

	        return new ExportResponse(base64Data, filename);
	    }

	    return new ExportResponse(null, null);
	}
	
	@Override
	public UserRoleDetails getUserRoleDetailsById(Integer userRoleKey, Integer userId) {
	    try {
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_USER_ROLE_MST_DET);

	        simpleJdbcCall.declareParameters(
	                new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	                new SqlParameter("IUSER_ROLE_KEY", OracleTypes.NUMBER),
	                new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new UserRoleDetailsViewRowMapper())
	        );

	        SqlParameterSource paramSource = new MapSqlParameterSource()
	                .addValue("ILOGGEDIN_KEY", userId)
	                .addValue("IUSER_ROLE_KEY", userRoleKey);

	        Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	        if (out != null && out.get("IO_CURSOR") != null) {
	            @SuppressWarnings("unchecked")
	            List<UserRoleDetails> userRoleList = (List<UserRoleDetails>) out.get("IO_CURSOR");
	            if (!userRoleList.isEmpty()) {
	                return userRoleList.get(0);  
	            } else {
	                throw new CommonDataException(HttpStatus.NOT_FOUND, new DBResponse(-1, "User role not found for ID: " + userRoleKey));
	            }
	        } else {
	            throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "No data returned from procedure."));
	        }
	    } catch (Exception e) {
	        logger.error("Error in getUserRoleDetailsById(userRoleKey=" + userRoleKey + ", loggedInUserId=" + userId + "): ", e);
	        throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error fetching user role details"));
	    }
	}

	class UserRoleDetailsViewRowMapper implements RowMapper<UserRoleDetails> {

	    @Override
	    public UserRoleDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
	        UserRoleDetails userRole = new UserRoleDetails();
	        userRole.setUserRoleKey(rs.getInt("USER_ROLE_KEY"));
	        userRole.setUserRoleCode(rs.getString("USER_ROLE_CODE"));
	        userRole.setUserRoleName(rs.getString("USER_ROLE_NAME"));
	        userRole.setStatus(rs.getInt("STATUS"));
	        userRole.setCreatedBy(rs.getInt("CREATED_BY"));
	        userRole.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
	        userRole.setCreatedOn(rs.getString("CREATED_ON"));
	        userRole.setModifiedBy(rs.getInt("MODIFIED_BY"));
	        userRole.setModifiedByLoginId(rs.getString("MODIFIED_BY_LOGIN_ID"));
	        userRole.setModifiedOn(rs.getString("MODIFIED_ON"));
	        userRole.setCheckedBy(rs.getInt("CHECKED_BY"));
	        userRole.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
	        userRole.setCheckedOn(rs.getString("CHECKED_ON"));
	        userRole.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));
	        userRole.setApprovedFlag(rs.getInt("APPROVED_FLAG"));
	        userRole.setRefUserRoleKey(rs.getInt("REF_USER_ROLE_KEY"));

	        return userRole;
	    }
	}

	
	
}
