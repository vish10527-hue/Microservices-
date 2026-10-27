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

import in.ameya.dao.master.BranchDao;
import in.ameya.model.CommonDataException;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.BranchDetails;
import in.ameya.model.master.BranchSearch;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.Constants;
import in.ameya.util.DBConstants;
import in.ameya.util.ExportData;
import oracle.jdbc.OracleTypes;

@Repository
@PropertySource(value="classpath:filePath.properties")
public class BranchDaoImpl extends JdbcDaoSupport implements BranchDao {

	private static final Logger logger = LogManager.getLogger(BranchDao.class);
	
	@Autowired
	ExportData exportData;	
		
	@Autowired
	@Qualifier("MainDatabase")
	protected DataSource dataSource;
	
	@SuppressWarnings("unchecked")
	@Override
	public DBResponse getBranchList(BranchSearch BranchSearch) {
		
		AtomicInteger totalCount = new AtomicInteger(0);
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_BRANCHES_MST_LIST);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("IPAGE_NO", OracleTypes.INTEGER),
					new SqlParameter("IPAGE_SIZE", OracleTypes.INTEGER),
					new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
					new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
					new SqlParameter("IBRANCH_CODE", OracleTypes.VARCHAR),
					new SqlParameter("IBRANCH_DESCRIPTION", OracleTypes.VARCHAR),
					new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
					new SqlParameter("ISTATUS", OracleTypes.INTEGER),
					new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new BranchDetailsRowMapper(totalCount))
					);
			
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", BranchSearch.getLoggedInKey())
					.addValue("IPAGE_NO", BranchSearch.getPageNo())
					.addValue("IPAGE_SIZE", BranchSearch.getPageSize())
					.addValue("IORDER_BY_COLUMN", Constants.getParameter(BranchSearch.getOrderByColumn()))
					.addValue("IORDER_ASC_DESC", Constants.getParameter(BranchSearch.getOrderAscDesc()))
					.addValue("IBRANCH_CODE", Constants.getParameter(BranchSearch.getBranchCode()))
					.addValue("IBRANCH_DESCRIPTION",BranchSearch.getBranchDescription())
					.addValue("IAPPROVED_FLAG", BranchSearch.getApprovedFlag())
					.addValue("ISTATUS", BranchSearch.getStatus())
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				BranchSearch.branches = (ArrayList<BranchDetails>) out.get("IO_CURSOR");
				BranchSearch.count = totalCount.intValue();
				return new DBResponse(1);
			} else {
				return new DBResponse(-1, "No result from query.");
			}
		}
		catch (Exception e) {
			logger.error("getBranchList("+ BranchSearch +"): " + totalCount, e);
			throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching Branch List"));
		}
	}

	
	class BranchDetailsRowMapper implements RowMapper<BranchDetails> {
		
		private AtomicInteger count;
		public BranchDetailsRowMapper(AtomicInteger count) {
			this.count = count;
		}

		@Override
		public BranchDetails mapRow(ResultSet rs, int rowNumber) throws SQLException {
			this.count.accumulateAndGet(rs.getInt("TOT_COUNT"), Math::max);
			BranchDetails branch = new BranchDetails();
			branch.setBranchKey(rs.getInt("BRANCH_KEY"));
			branch.setBranchCode(rs.getString("BRANCH_CODE"));
			branch.setBranchDescription(rs.getString("BRANCH_DESCRIPTION"));
			branch.setStatus(rs.getInt("STATUS"));
			branch.setCreatedBy(rs.getInt("CREATED_BY"));
			branch.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
			branch.setCreatedOn(rs.getString("CREATED_ON"));
			branch.setModifiedBy(rs.getInt("MODIFIED_BY"));
			branch.setModifiedByLoginId(rs.getString("MODIFIED_BY_LOGIN_ID"));
			branch.setModifiedOn(rs.getString("MODIFIED_ON"));
			branch.setCheckedBy(rs.getInt("CHECKED_BY"));
			branch.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
			branch.setCheckedOn(rs.getString("CHECKED_ON"));
			branch.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));
			branch.setApprovedFlag(rs.getInt("APPROVED_FLAG"));
			branch.setRefBranchKey(rs.getInt("REF_BRANCH_KEY"));
			return branch;
		}
	}


	@Override
	public DBResponse manageBranch(BranchDetails branch, Integer loggedInKey, String action) {
		DBResponse resp = null;
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_BRANCH_MASTER_MANAGE);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("IBRANCH_KEY", OracleTypes.NUMBER),
					new SqlParameter("IBRANCH_CODE", OracleTypes.VARCHAR),
					new SqlParameter("IBRANCH_DESCRIPTION", OracleTypes.VARCHAR),
					new SqlParameter("ISTATUS", OracleTypes.NUMBER),
					new SqlParameter("IAPP_REJ_REMARKS", OracleTypes.VARCHAR),
					new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
					new SqlParameter("IREF_BRANCH_KEY", OracleTypes.NUMBER),
					new SqlParameter("IRECORD_STATUS", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
					);
			
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", loggedInKey)
					.addValue("IBRANCH_KEY", branch.getBranchKey())
					.addValue("IBRANCH_CODE", branch.getBranchCode())
					.addValue("IBRANCH_DESCRIPTION", branch.getBranchDescription())
					.addValue("ISTATUS", branch.getStatus())
					.addValue("IAPP_REJ_REMARKS", branch.getAppRejRemarks())
					.addValue("IAPPROVED_FLAG", branch.getApprovedFlag())
					.addValue("IREF_BRANCH_KEY", branch.getRefBranchKey())
					.addValue("IRECORD_STATUS", action)
					;
			
					Map<String, Object> out = simpleJdbcCall.execute(paramSource);
					if (out != null) {
						BigDecimal result = (BigDecimal) out.get("O_RESULT");
						resp = new DBResponse(result.intValue(), (String) out.get("O_MESSAGE"),branch);

						if (resp.getStatus() == 1) {
							return resp;
						}
					}
		}
		catch (Exception e) {
			logger.error("manageBranch("+action+"): " + branch, e);
			throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching Branch List"));
		}
		if(resp == null) {
			resp = new DBResponse(-1,null);
		}
		return resp;
	}


	@Override
	public DBResponse verifyBranchRecords(Integer loggedInKey, String branchKeys, String remarks, String recordStatus) {
		DBResponse resp = null;
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_BRANCH_MASTER_CHECK);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("IBRANCH_KEYS", OracleTypes.VARCHAR),
					new SqlParameter("IAPP_REJ_REMARKS", OracleTypes.VARCHAR),
					new SqlParameter("IRECORD_STATUS", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
					);
	
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", loggedInKey)
					.addValue("IBRANCH_KEYS", branchKeys)
					.addValue("IAPP_REJ_REMARKS", remarks)
					.addValue("IRECORD_STATUS", recordStatus)
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				resp = new DBResponse((BigDecimal) out.get("O_RESULT"), (String) out.get("O_MESSAGE"));
			} 
		}
		catch (Exception e) {
			logger.error("manageBranch("+recordStatus+"): " + branchKeys + " -> " + remarks, e);
			resp = new DBResponse(-1, "Error While Verifing Branch Records");
		}
		
		if(resp == null) {
			resp = new DBResponse(-1, "No Records Found.");
		}
		
		return resp;
	}
	
	
	@Override
	public String exportBranchCSV(BranchSearch branchSearch) throws IOException {
	    SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_BRANCHES_MST_EXP);

	    simpleJdbcCall.declareParameters(
	            new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	            new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
	            new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlParameter("IBRANCH_CODE", OracleTypes.VARCHAR),
	            new SqlParameter("IBRANCH_DESCRIPTION", OracleTypes.VARCHAR),
	            new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	            new SqlParameter("ISTATUS", OracleTypes.INTEGER),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("ILOGGEDIN_KEY", branchSearch.getLoggedInKey())
	            .addValue("IORDER_BY_COLUMN", branchSearch.getOrderByColumn())
	            .addValue("IORDER_ASC_DESC", branchSearch.getOrderAscDesc())
	            .addValue("IBRANCH_CODE", branchSearch.getBranchCode())
	            .addValue("IBRANCH_DESCRIPTION", branchSearch.getBranchDescription())
	            .addValue("IAPPROVED_FLAG", branchSearch.getApprovedFlag())
	            .addValue("ISTATUS", branchSearch.getStatus());

	    Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	    if (out != null) {
	        @SuppressWarnings("unchecked")
	        ArrayList<Map<String, Object>> resultSet = (ArrayList<Map<String, Object>>) out.get("IO_CURSOR");

	        if (resultSet != null && !resultSet.isEmpty()) {
	            String csvContent = exportData.generateCSV(resultSet);
	            String base64Csv = exportData.encodeBase64(csvContent);

	            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	            String filename = "BranchMaster_" + timestamp + ".csv";

	            return filename + "|" + base64Csv;
	        } else {
	            return "No data found";
	        }
	    } else {
	        return "Error during CSV generation";
	    }
	}


	@Override
	public ExportResponse exportBranchExcel(BranchSearch branchSearch) throws IOException {
	    SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_BRANCHES_MST_EXP);

	    simpleJdbcCall.declareParameters(
	            new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	            new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
	            new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlParameter("IBRANCH_CODE", OracleTypes.VARCHAR),
	            new SqlParameter("IBRANCH_DESCRIPTION", OracleTypes.VARCHAR),
	            new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	            new SqlParameter("ISTATUS", OracleTypes.INTEGER),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("ILOGGEDIN_KEY", branchSearch.getLoggedInKey())
	            .addValue("IORDER_BY_COLUMN", branchSearch.getOrderByColumn())
	            .addValue("IORDER_ASC_DESC", branchSearch.getOrderAscDesc())
	            .addValue("IBRANCH_CODE", branchSearch.getBranchCode())
	            .addValue("IBRANCH_DESCRIPTION", branchSearch.getBranchDescription())
	            .addValue("IAPPROVED_FLAG", branchSearch.getApprovedFlag())
	            .addValue("ISTATUS", branchSearch.getStatus());

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

	        String filename = "BranchMaster_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
	        String base64Data = exportData.generateExcelBase64(jsonData, columnOrder);

	        return new ExportResponse(base64Data, filename);
	    }

	    return new ExportResponse(null, null);
	}



	@SuppressWarnings("unchecked")
	@Override
	public BranchDetails getBranchDetailsById(Integer branchKey, Integer userId) {
	    try {
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_BRANCHES_MST_DET);

	        simpleJdbcCall.declareParameters(
	                new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	                new SqlParameter("IBRANCH_KEY", OracleTypes.NUMBER),
	                new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new BranchDetailsViewRowMapper())
	        );

	        SqlParameterSource paramSource = new MapSqlParameterSource()
	                .addValue("ILOGGEDIN_KEY", userId)
	                .addValue("IBRANCH_KEY", branchKey);

	        Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	        if (out != null && out.get("IO_CURSOR") != null) {
	            List<BranchDetails> branchList = (List<BranchDetails>) out.get("IO_CURSOR");
	            if (!branchList.isEmpty()) {
	                return branchList.get(0);
	            } else {
	                throw new CommonDataException(HttpStatus.NOT_FOUND, new DBResponse(-1, "Branch not found for ID: " + branchKey));
	            }
	        } else {
	            throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "No data returned from procedure."));
	        }
	    } catch (Exception e) {
	        logger.error("Error in getBranchDetailsById(branchKey=" + branchKey + ", userId=" + userId + "): ", e);
	        throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error fetching branch details"));
	    }
	}
	
	class BranchDetailsViewRowMapper implements RowMapper<BranchDetails> {

	    @Override
	    public BranchDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
	        BranchDetails branch = new BranchDetails();
	        branch.setBranchKey(rs.getInt("BRANCH_KEY"));
	        branch.setBranchCode(rs.getString("BRANCH_CODE"));
	        branch.setBranchDescription(rs.getString("BRANCH_DESCRIPTION"));
	        branch.setStatus(rs.getInt("STATUS"));
	        branch.setCreatedBy(rs.getInt("CREATED_BY"));
	        branch.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
	        branch.setCreatedOn(rs.getString("CREATED_ON"));
	        branch.setModifiedBy(rs.getInt("MODIFIED_BY"));
	        branch.setModifiedByLoginId(rs.getString("MODIFIED_BY_LOGIN_ID"));
	        branch.setModifiedOn(rs.getString("MODIFIED_ON"));
	        branch.setCheckedBy(rs.getInt("CHECKED_BY"));
	        branch.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
	        branch.setCheckedOn(rs.getString("CHECKED_ON"));
	        branch.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));
	        branch.setApprovedFlag(rs.getInt("APPROVED_FLAG"));
	        branch.setRefBranchKey(rs.getInt("REF_BRANCH_KEY"));
	        return branch;
	    }
	}



}
