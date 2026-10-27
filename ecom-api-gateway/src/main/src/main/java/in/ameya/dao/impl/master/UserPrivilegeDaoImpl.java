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
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import in.ameya.dao.master.BranchDao;
import in.ameya.dao.master.UserPrivilegeDao;
import in.ameya.model.CommonDataException;
import in.ameya.model.DBResponse;
import in.ameya.model.master.UserPrivilegeDetails;
import in.ameya.model.master.UserPrivilegeSearch;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.DBConstants;
import in.ameya.util.ExportData;
import oracle.jdbc.OracleTypes;

@Repository
@PropertySource(value="classpath:filePath.properties")
public class UserPrivilegeDaoImpl extends JdbcDaoSupport implements UserPrivilegeDao {

	private static final Logger logger = LogManager.getLogger(BranchDao.class);

	@Autowired
	@Qualifier("MainDatabase")
	protected DataSource dataSource;
	
	@Autowired
	ExportData exportData;
	
	@SuppressWarnings("unchecked")
	@Override
	public DBResponse getUserPrivilegeList(UserPrivilegeSearch userPrivilegeSearch) {
	    AtomicInteger totalCount = new AtomicInteger(0);
	    try {
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_ROLE_PRIV_MAP_LIST);

	        simpleJdbcCall.declareParameters(
	                new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	                new SqlParameter("IPAGE_NO", OracleTypes.INTEGER),
	                new SqlParameter("IPAGE_SIZE", OracleTypes.INTEGER),
	                new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
	                new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
	                new SqlParameter("IUSER_ROLE_CODE", OracleTypes.VARCHAR),
	                new SqlParameter("IUSER_ROLE_NAME", OracleTypes.VARCHAR),
	                new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	                new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new UserPrivilegeDetailsRowMapper(totalCount))
	        );

	        SqlParameterSource paramSource = new MapSqlParameterSource()
	                .addValue("ILOGGEDIN_KEY", userPrivilegeSearch.getLoggedInKey())
	                .addValue("IPAGE_NO", userPrivilegeSearch.getPageNo())
	                .addValue("IPAGE_SIZE", userPrivilegeSearch.getPageSize())
	                .addValue("IORDER_BY_COLUMN", userPrivilegeSearch.getOrderByColumn())
	                .addValue("IORDER_ASC_DESC", userPrivilegeSearch.getOrderAscDesc())
	                .addValue("IUSER_ROLE_CODE", userPrivilegeSearch.getUserRoleCode())
	                .addValue("IUSER_ROLE_NAME", userPrivilegeSearch.getUserRoleName())
	                .addValue("IAPPROVED_FLAG", userPrivilegeSearch.getApprovedFlag());

	        Map<String, Object> out = simpleJdbcCall.execute(paramSource);
	        if (out != null) {
	        	userPrivilegeSearch.userPrivileges = (ArrayList<UserPrivilegeDetails>) out.get("IO_CURSOR");
	            userPrivilegeSearch.count=totalCount.intValue();
	            return new DBResponse(1);
	        } else {
	            return new DBResponse(-1, "No result from query.");
	        }
	    } catch (Exception e) {
	        logger.error("getUserPrivilegeList(" + userPrivilegeSearch + "): " + totalCount, e);
	        throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR,
	                new DBResponse(-1, "Error While Fetching User Privilege List"));
	    }
	}

	class UserPrivilegeDetailsRowMapper implements RowMapper<UserPrivilegeDetails> {

	    private AtomicInteger count;

	    public UserPrivilegeDetailsRowMapper(AtomicInteger count) {
	        this.count = count;
	    }

	    @Override
	    public UserPrivilegeDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
	        this.count.accumulateAndGet(rs.getInt("TOT_COUNT"), Math::max);
	        
	        UserPrivilegeDetails userPrivilege = new UserPrivilegeDetails();
	        
	        userPrivilege.setUserRoleKey(rs.getString("USER_ROLE_KEY"));
	        userPrivilege.setUserRoleCode(rs.getString("USER_ROLE_CODE"));
	        userPrivilege.setUserRoleName(rs.getString("USER_ROLE_NAME"));
	        userPrivilege.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
	        userPrivilege.setCreatedOn(rs.getString("CREATED_ON"));
	        userPrivilege.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
	        userPrivilege.setCheckedOn(rs.getString("CHECKED_ON"));
	        userPrivilege.setApprovedFlag(rs.getString("APPROVED_FLAG") == null ? null : rs.getInt("APPROVED_FLAG"));

	        return userPrivilege;
	    }
	}
	
	@Override
	public DBResponse verifyUserPrivilegeRecords(Integer loggedInKey, String userPrivilegeKeys, String remarks, String recordStatus) {
	    DBResponse resp = null;
	    try {
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ROLE_PRIVILEGES_MAP_CHECK);

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
	                .addValue("IUSER_ROLE_KEYS", userPrivilegeKeys)
	                .addValue("IAPP_REJ_REMARKS", remarks)
	                .addValue("IRECORD_STATUS", recordStatus);

	        Map<String, Object> out = simpleJdbcCall.execute(paramSource);
	        
	        if (out != null) {
	            resp = new DBResponse((BigDecimal) out.get("O_RESULT"), (String) out.get("O_MESSAGE"));
	        }
	    } catch (Exception e) {
	        logger.error("verifyUserPrivilegeRecords(" + recordStatus + "): " + userPrivilegeKeys + " -> " + remarks, e);
	        resp = new DBResponse(-1, "Error While Verifying User Privilege Records");
	    }

	    if (resp == null) {
	        resp = new DBResponse(-1, "No Records Found.");
	    }

	    return resp;
	}

	@Override
	public DBResponse manageUserPrivilege(UserPrivilegeDetails userPrivilege, Integer loggedInKey, String recordUpdate) {
	    DBResponse resp = null;
	    try {
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ROLE_PRIVILEGES_MAP_MANAGE);

	        simpleJdbcCall.declareParameters(
	                new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	                new SqlParameter("IUSER_ROLE_KEY", OracleTypes.NUMBER),
	                new SqlParameter("IPRIVILEGE_KEYS", OracleTypes.VARCHAR),
	                new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
	                new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
	        );

	        SqlParameterSource paramSource = new MapSqlParameterSource()
	                .addValue("ILOGGEDIN_KEY", loggedInKey)
	                .addValue("IUSER_ROLE_KEY", userPrivilege.getUserRoleKey())
	                .addValue("IPRIVILEGE_KEYS", userPrivilege.getPrivilegeKey());
	        Map<String, Object> out = simpleJdbcCall.execute(paramSource);
	        
			if (out != null) {
				BigDecimal result = (BigDecimal) out.get("O_RESULT");
				resp = new DBResponse(result.intValue(), (String) out.get("O_MESSAGE"), userPrivilege);

				if (resp.getStatus() == 1) {
					return resp;
				}
			}
	    } catch (Exception e) {
	        logger.error("manageUserPrivilege(" + recordUpdate + "): " + userPrivilege, e);
	        throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Managing User Privileges"));
	    }

	    if (resp == null) {
	        resp = new DBResponse(-1, null);
	    }
	    return resp;
	}

	@Override
	public List<UserPrivilegeDetails> getUserPrivilegeDetailsById(String userRoleKey, Integer approvedFlg, Integer userId) {
	    try {
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_ROLE_PRIV_MAP_DET);

	        simpleJdbcCall.declareParameters(
	            new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	            new SqlParameter("IUSER_ROLE_KEY", OracleTypes.VARCHAR),
	            new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new UserPrivilegeDetailsViewRowMapper())
	        );

	        SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("ILOGGEDIN_KEY", userId)
	            .addValue("IUSER_ROLE_KEY", userRoleKey)
	            .addValue("IAPPROVED_FLAG", approvedFlg);

	        Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	        if (out != null && out.get("IO_CURSOR") != null) {
	            @SuppressWarnings("unchecked")
	            List<UserPrivilegeDetails> userPrivilegeList = (List<UserPrivilegeDetails>) out.get("IO_CURSOR");

	            if (!userPrivilegeList.isEmpty()) {
	                // Organize privileges hierarchically using privilegeMap
	                Map<Integer, UserPrivilegeDetails> privilegeMap = new HashMap<>();

	                for (UserPrivilegeDetails privilege : userPrivilegeList) {
	                    if (privilege.getParentKey() == 0) {
	                        privilegeMap.put(privilege.getMenuKey(), privilege);
	                    }
	                }

	                for (UserPrivilegeDetails privilege : userPrivilegeList) {
	                    if (privilege.getParentKey() != 0) {
	                        UserPrivilegeDetails parentPrivilege = privilegeMap.get(privilege.getParentKey());
	                        if (parentPrivilege != null) {
	                            if (parentPrivilege.getChildPrivileges() == null) {
	                                parentPrivilege.setChildPrivileges(new ArrayList<>());
	                            }
	                            parentPrivilege.getChildPrivileges().add(privilege);
	                        }
	                    }
	                }

	                // Return the organized privileges as a list
	                return new ArrayList<>(privilegeMap.values());
	            } else {
	                throw new CommonDataException(
	                    HttpStatus.NOT_FOUND,
	                    new DBResponse(-1, "User privilege not found for ID: " + userId)
	                );
	            }
	        } else {
	            throw new CommonDataException(
	                HttpStatus.INTERNAL_SERVER_ERROR,
	                new DBResponse(-1, "No data returned from procedure.")
	            );
	        }
	    } catch (Exception e) {
	        throw new CommonDataException(
	            HttpStatus.INTERNAL_SERVER_ERROR,
	            new DBResponse(-1, "Error fetching user privilege details")
	        );
	    }
	}

	class UserPrivilegeDetailsViewRowMapper implements RowMapper<UserPrivilegeDetails> {
	    @Override
	    public UserPrivilegeDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
	        UserPrivilegeDetails userPrivilege = new UserPrivilegeDetails();

	        userPrivilege.setPrivilegeKey(rs.getString("PRIVILEGE_KEY"));
	        userPrivilege.setUserRoleKey(rs.getString("USER_ROLE_KEY"));
	        userPrivilege.setPrivilegeName(rs.getString("PRIVILEGE_NAME"));
	        userPrivilege.setParentKey(rs.getInt("PARENT_KEY"));
	        userPrivilege.setMenuKey(rs.getInt("MENU_KEY"));
	        //userPrivilege.setParentPrivilegeKey(rs.getInt("PARENT_PRIVILEGE_KEY"));
	        userPrivilege.setApprovedFlag(rs.getInt("APPROVED_FLAG"));
	        userPrivilege.setCreatedBy(rs.getInt("CREATED_BY"));
	        userPrivilege.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
	        userPrivilege.setCreatedOn(rs.getString("CREATED_ON"));
	        userPrivilege.setModifiedBy(rs.getInt("MODIFIED_BY"));
	        userPrivilege.setModifiedByLoginId(rs.getString("MODIFIED_BY_LOGIN_ID"));
	        userPrivilege.setModifiedOn(rs.getString("MODIFIED_ON"));
	        userPrivilege.setCheckedBy(rs.getInt("CHECKED_BY"));
	        userPrivilege.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
	        userPrivilege.setCheckedOn(rs.getString("CHECKED_ON"));
	        userPrivilege.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));

	        return userPrivilege;
	    }
	}
	
	
	@Override
	public List<UserPrivilegeDetails> attachUserPrivilege(UserPrivilegeSearch userPrivilegeSearch, Integer userId) {
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_ROLE_PRIV_MAP_DET);

			simpleJdbcCall.declareParameters(new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("IUSER_ROLE_KEY", OracleTypes.VARCHAR),
					new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
					new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new UserPrivilegeAttachRowMapper()));

			SqlParameterSource paramSource = new MapSqlParameterSource().addValue("ILOGGEDIN_KEY", userId)
					.addValue("IUSER_ROLE_KEY", userPrivilegeSearch.getUserRoleKey())
					.addValue("IAPPROVED_FLAG", userPrivilegeSearch.getApprovedFlag());

			Map<String, Object> out = simpleJdbcCall.execute(paramSource);

			if (out != null && out.get("IO_CURSOR") != null) {

				@SuppressWarnings("unchecked")
				List<UserPrivilegeDetails> privileges = (List<UserPrivilegeDetails>) out.get("IO_CURSOR");

				if (!privileges.isEmpty()) {
					Map<Integer, UserPrivilegeDetails> privilegeMap = new HashMap<>();

					for (UserPrivilegeDetails privilege : privileges) {
						if (privilege.getParentKey() == 0) {
							privilegeMap.put(privilege.getMenuKey(), privilege);
						}
					}

					for (UserPrivilegeDetails privilege : privileges) {
						if (privilege.getParentKey() != 0) {
							UserPrivilegeDetails parentPrivilege = privilegeMap.get(privilege.getParentKey());
							if (parentPrivilege != null) {
								if (parentPrivilege.getChildPrivileges() == null) {
									parentPrivilege.setChildPrivileges(new ArrayList<>());
								}
								parentPrivilege.getChildPrivileges().add(privilege);
							}
						}
					}

					return new ArrayList<>(privilegeMap.values());
				} else {
					throw new CommonDataException(HttpStatus.NOT_FOUND,
							new DBResponse(-1, "No privileges found for the specified role."));
				}
			} else {
				throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR,
						new DBResponse(-1, "No data returned from procedure."));
			}
		} catch (Exception e) {
			logger.error("Error in attachUserPrivilege(userRoleKey=" + userPrivilegeSearch.getUserRoleKey()
					+ ", userId=" + userId + "): ", e);
			throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR,
					new DBResponse(-1, "Error while fetching user privileges"));
		}
	}
	
	class UserPrivilegeAttachRowMapper implements RowMapper<UserPrivilegeDetails> {

	    @Override
	    public UserPrivilegeDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
	        UserPrivilegeDetails userPrivilege = new UserPrivilegeDetails();

	        userPrivilege.setPrivilegeKey(rs.getString("PRIVILEGE_KEY"));
	        userPrivilege.setUserRoleKey(rs.getString("USER_ROLE_KEY"));
	        userPrivilege.setPrivilegeName(rs.getString("PRIVILEGE_NAME"));
	        userPrivilege.setParentKey(rs.getInt("PARENT_KEY"));
	        userPrivilege.setMenuKey(rs.getInt("MENU_KEY"));
	        userPrivilege.setParentPrivilegeKey(rs.getInt("PARENT_PRIVIEGE_KEY"));
	        userPrivilege.setCreatedBy(rs.getInt("CREATED_BY"));
	        userPrivilege.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
	        userPrivilege.setCreatedOn(rs.getString("CREATED_ON"));
	        userPrivilege.setModifiedBy(rs.getInt("MODIFIED_BY"));
	        userPrivilege.setModifiedByLoginId(rs.getString("MODIFIED_BY_LOGIN_ID"));
	        userPrivilege.setModifiedOn(rs.getString("MODIFIED_ON"));
	        userPrivilege.setCheckedBy(rs.getInt("CHECKED_BY"));
	        userPrivilege.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
	        userPrivilege.setCheckedOn(rs.getString("CHECKED_ON"));
	        userPrivilege.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));
	        userPrivilege.setApprovedFlag(rs.getInt("APPROVED_FLAG"));

	        return userPrivilege;
	    }
	}
	
}
