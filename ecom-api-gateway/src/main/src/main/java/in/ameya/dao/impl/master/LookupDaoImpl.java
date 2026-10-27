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

import in.ameya.dao.master.LookupDao;
import in.ameya.model.CommonDataException;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.LookupDetails;
import in.ameya.model.master.LookupSearch;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.DBConstants;
import in.ameya.util.ExportData;
import oracle.jdbc.OracleTypes;

@Repository
@PropertySource(value="classpath:filePath.properties")
public class LookupDaoImpl extends JdbcDaoSupport implements LookupDao {

	private static final Logger logger = LogManager.getLogger(LookupDao.class);
	
	@Autowired
	ExportData exportData;	
		
	@Autowired
	@Qualifier("MainDatabase")
	protected DataSource dataSource;
	
	@SuppressWarnings("unchecked")
	@Override
	public DBResponse getLookupList(LookupSearch lookupSearch) {

	    AtomicInteger totalCount = new AtomicInteger(0);
	    try {
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_LOOKUPS_MST_LIST);

	        simpleJdbcCall.declareParameters(
	                new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	                new SqlParameter("IPAGE_NO", OracleTypes.INTEGER),
	                new SqlParameter("IPAGE_SIZE", OracleTypes.INTEGER),
	                new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
	                new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
	                new SqlParameter("ILOOKUP_TYPE", OracleTypes.NUMBER),
	                new SqlParameter("ILOOKUP_CODE", OracleTypes.VARCHAR),
	                new SqlParameter("ILOOKUP_DESCRIPTION", OracleTypes.VARCHAR),
	                new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	                new SqlParameter("ISTATUS", OracleTypes.INTEGER),
	                new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new LookupDetailsRowMapper(totalCount))
	        );

	        SqlParameterSource paramSource = new MapSqlParameterSource()
	                .addValue("ILOGGEDIN_KEY", lookupSearch.getLoggedInKey())
	                .addValue("IPAGE_NO", lookupSearch.getPageNo())
	                .addValue("IPAGE_SIZE", lookupSearch.getPageSize())
	                .addValue("IORDER_BY_COLUMN", lookupSearch.getOrderByColumn())
	                .addValue("IORDER_ASC_DESC", lookupSearch.getOrderAscDesc())
	                .addValue("ILOOKUP_TYPE", lookupSearch.getLookupType())
	                .addValue("ILOOKUP_CODE", lookupSearch.getLookupCode())
	                .addValue("ILOOKUP_DESCRIPTION", lookupSearch.getLookupDescription())
	                .addValue("IAPPROVED_FLAG", lookupSearch.getApprovedFlag())
	                .addValue("ISTATUS", lookupSearch.getStatus());

	        Map<String, Object> out = simpleJdbcCall.execute(paramSource);
	        if (out != null && out.get("IO_CURSOR") != null) {
	            lookupSearch.lookups = (ArrayList<LookupDetails>) out.get("IO_CURSOR");
	            lookupSearch.count = totalCount.intValue();
	            return new DBResponse(1);
	        } else {
	            return new DBResponse(-1, "No result from query.");
	        }
	    } catch (Exception e) {
	        logger.error("getLookupList(" + lookupSearch + "): " + totalCount, e);
	        throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching Lookup List"));
	    }
	}

	class LookupDetailsRowMapper implements RowMapper<LookupDetails> {

	    private AtomicInteger count;

	    public LookupDetailsRowMapper(AtomicInteger count) {
	        this.count = count;
	    }

	    @Override
	    public LookupDetails mapRow(ResultSet rs, int rowNumber) throws SQLException {
	        this.count.accumulateAndGet(rs.getInt("TOT_COUNT"), Math::max);

	        LookupDetails lookup = new LookupDetails();
	        lookup.setLookupType(rs.getInt("LOOKUP_TYPE"));
	        lookup.setLookupKey(rs.getInt("LOOKUP_KEY"));
	        lookup.setLookupCode(rs.getString("LOOKUP_CODE"));
	        lookup.setLookupDescription(rs.getString("LOOKUP_DESCRIPTION"));
	        lookup.setStatus(rs.getInt("STATUS"));
	        lookup.setCreatedBy(rs.getInt("CREATED_BY"));
	        lookup.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
	        lookup.setCreatedOn(rs.getString("CREATED_ON"));
	        lookup.setModifiedBy(rs.getInt("MODIFIED_BY"));
	        lookup.setModifiedByLoginId(rs.getString("MODIFIED_BY_LOGIN_ID"));
	        lookup.setModifiedOn(rs.getString("MODIFIED_ON"));
	        lookup.setCheckedBy(rs.getInt("CHECKED_BY"));
	        lookup.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
	        lookup.setCheckedOn(rs.getString("CHECKED_ON"));
	        lookup.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));
	        lookup.setApprovedFlag(rs.getInt("APPROVED_FLAG"));
	        lookup.setRefLookupKey(rs.getInt("REF_LOOKUP_KEY"));    
	        lookup.setLookupTypeDesc(rs.getString("LOOKUP_TYPE_DESC"));
	        return lookup;
	    }
	}


	@Override
	public DBResponse manageLookup(LookupDetails lookup, Integer loggedInKey, String action) {
		DBResponse resp = null;
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_LOOKUP_MASTER_MANAGE);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("ILOOKUP_KEY", OracleTypes.NUMBER),
					new SqlParameter("ILOOKUP_TYPE", OracleTypes.NUMBER),
					new SqlParameter("ILOOKUP_CODE", OracleTypes.VARCHAR),
					new SqlParameter("ILOOKUP_DESCRIPTION", OracleTypes.VARCHAR),
					new SqlParameter("IRESERVED_FLAG", OracleTypes.NUMBER),
					new SqlParameter("ISTATUS", OracleTypes.NUMBER),
					new SqlParameter("IAPP_REJ_REMARKS", OracleTypes.VARCHAR),
					new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
					new SqlParameter("IREF_LOOKUP_KEY", OracleTypes.NUMBER),
					new SqlParameter("IRECORD_STATUS", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
					);
			
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", loggedInKey)
					.addValue("ILOOKUP_KEY", lookup.getLookupKey())
					.addValue("ILOOKUP_TYPE", lookup.getLookupType())
					.addValue("ILOOKUP_CODE", lookup.getLookupCode())
					.addValue("ILOOKUP_DESCRIPTION", lookup.getLookupDescription())
					.addValue("IRESERVED_FLAG", lookup.getReservedFlag())
					.addValue("ISTATUS", lookup.getStatus())
					.addValue("IAPP_REJ_REMARKS", lookup.getAppRejRemarks())
					.addValue("IAPPROVED_FLAG", lookup.getApprovedFlag())
					.addValue("IREF_LOOKUP_KEY", lookup.getRefLookupKey())
					.addValue("IRECORD_STATUS", action)
					;
			
					Map<String, Object> out = simpleJdbcCall.execute(paramSource);
					if (out != null) {
						BigDecimal result = (BigDecimal) out.get("O_RESULT");
						resp = new DBResponse(result.intValue(), (String) out.get("O_MESSAGE"), lookup);

						if (resp.getStatus() == 1) {
							return resp;
						}

					}
		}
		catch (Exception e) {
			logger.error("manageLookup("+action+"): " + lookup, e);
			throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching Lookup List"));
		}
		
		if(resp == null) {
			resp = new DBResponse(-1, null);
		}
		
		return resp;
	}


	@Override
	public DBResponse verifyLookupRecords(Integer loggedInKey, String lookupKeys, String remarks, String recordStatus) {
		DBResponse resp = null;
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_LOOKUP_MASTER_CHECK);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("ILOOKUP_KEYS", OracleTypes.VARCHAR),
					new SqlParameter("IAPP_REJ_REMARKS", OracleTypes.VARCHAR),
					new SqlParameter("IRECORD_STATUS", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
					);
	
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", loggedInKey)
					.addValue("ILOOKUP_KEYS", lookupKeys)
					.addValue("IAPP_REJ_REMARKS", remarks)
					.addValue("IRECORD_STATUS", recordStatus)
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				resp = new DBResponse((BigDecimal) out.get("O_RESULT"), (String) out.get("O_MESSAGE"));
			} 
		}
		catch (Exception e) {
			logger.error("manageLookup("+recordStatus+"): " + lookupKeys + " -> " + remarks, e);
			resp = new DBResponse(-1, "Error While Verifing Lookup Records");
		}
		
		if(resp == null) {
			resp = new DBResponse(-1, "No Records Found.");
		}
		
		return resp;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String exportLookupCSV(LookupSearch lookupSearch) {
	    SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_LOOKUP_MST_EXP);

	    simpleJdbcCall.declareParameters(                    
	            new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	            new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
	            new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlParameter("ILOOKUP_TYPE", OracleTypes.NUMBER),
	            new SqlParameter("ILOOKUP_CODE", OracleTypes.VARCHAR),
	            new SqlParameter("ILOOKUP_DESCRIPTION", OracleTypes.VARCHAR),
	            new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	            new SqlParameter("ISTATUS", OracleTypes.INTEGER),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("ILOGGEDIN_KEY", lookupSearch.getLoggedInKey())
	            .addValue("IORDER_BY_COLUMN", lookupSearch.getOrderByColumn())
	            .addValue("IORDER_ASC_DESC", lookupSearch.getOrderAscDesc())
	            .addValue("ILOOKUP_TYPE", lookupSearch.getLookupType())
	            .addValue("ILOOKUP_CODE", lookupSearch.getLookupCode())
	            .addValue("ILOOKUP_DESCRIPTION", lookupSearch.getLookupDescription())
	            .addValue("IAPPROVED_FLAG", lookupSearch.getApprovedFlag())
	            .addValue("ISTATUS", lookupSearch.getStatus());

	    Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	    if (out != null) {
	        ArrayList<Map<String, Object>> resultSet = (ArrayList<Map<String, Object>>) out.get("IO_CURSOR");

	        if (resultSet != null && !resultSet.isEmpty()) {
	            String csvContent = exportData.generateCSV(resultSet);

	            String base64Csv = exportData.encodeBase64(csvContent);

	            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	            String filename = "LookupMaster_" + timestamp + ".csv";

	            return filename + "|" + base64Csv;
	        } else {
	            return "No data found";
	        }
	    } else {
	        return "Error during CSV generation";
	    }
	}


	
	@Override
	public ExportResponse exportLookupExcel(LookupSearch lookupSearch) throws IOException {
	    SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_LOOKUP_MST_EXP);

	    simpleJdbcCall.declareParameters(
	            new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	            new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
	            new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlParameter("ILOOKUP_TYPE", OracleTypes.NUMBER),
	            new SqlParameter("ILOOKUP_CODE", OracleTypes.VARCHAR),
	            new SqlParameter("ILOOKUP_DESCRIPTION", OracleTypes.VARCHAR),
	            new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	            new SqlParameter("ISTATUS", OracleTypes.INTEGER),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("ILOGGEDIN_KEY", lookupSearch.getLoggedInKey())
	            .addValue("IORDER_BY_COLUMN", lookupSearch.getOrderByColumn())
	            .addValue("IORDER_ASC_DESC", lookupSearch.getOrderAscDesc())
	            .addValue("ILOOKUP_TYPE", lookupSearch.getLookupType())
	            .addValue("ILOOKUP_CODE", lookupSearch.getLookupCode())
	            .addValue("ILOOKUP_DESCRIPTION", lookupSearch.getLookupDescription())
	            .addValue("IAPPROVED_FLAG", lookupSearch.getApprovedFlag())
	            .addValue("ISTATUS", lookupSearch.getStatus());

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

	        String filename = "LookupMaster_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
	        String base64Data = exportData.generateExcelBase64(jsonData, columnOrder);

	        return new ExportResponse(base64Data, filename);
	    }

	    return new ExportResponse(null, null);
	}


	@SuppressWarnings("unchecked")
	@Override
	public LookupDetails getLookupDetailsById(Integer lookupId, Integer userId) {
	    try {
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_LOOKUPS_MST_DET);

	        simpleJdbcCall.declareParameters(
	                new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	                new SqlParameter("ILOOKUP_KEY", OracleTypes.NUMBER),
	                new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new LookupDetailsViewRowMapper())
	        );

	        SqlParameterSource paramSource = new MapSqlParameterSource()
	                .addValue("ILOGGEDIN_KEY", userId)
	                .addValue("ILOOKUP_KEY", lookupId);

	        Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	        if (out != null && out.get("IO_CURSOR") != null) {
	            List<LookupDetails> lookupList = (List<LookupDetails>) out.get("IO_CURSOR");
	            if (!lookupList.isEmpty()) {
	                return lookupList.get(0);
	            } else {
	                throw new CommonDataException(HttpStatus.NOT_FOUND, new DBResponse(-1, "Lookup not found for ID: " + lookupId));
	            }
	        } else {
	            throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "No data returned from procedure."));
	        }
	    } catch (Exception e) {
	        logger.error("Error in getLookupDetailsById(lookupId=" + lookupId + ", userId=" + userId + "): ", e);
	        throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error fetching lookup details"));
	    }
	}
	
	class LookupDetailsViewRowMapper implements RowMapper<LookupDetails> {

	    @Override
	    public LookupDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
	        LookupDetails lookup = new LookupDetails();
	        lookup.setLookupKey(rs.getInt("LOOKUP_KEY"));
	        lookup.setLookupCode(rs.getString("LOOKUP_CODE"));
	        lookup.setLookupDescription(rs.getString("LOOKUP_DESCRIPTION"));
	        lookup.setStatus(rs.getInt("STATUS"));
	        lookup.setCreatedBy(rs.getInt("CREATED_BY"));
	        lookup.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
	        lookup.setCreatedOn(rs.getString("CREATED_ON"));
	        lookup.setModifiedBy(rs.getInt("MODIFIED_BY"));
	        lookup.setModifiedByLoginId(rs.getString("MODIFIED_BY_LOGIN_ID"));
	        lookup.setModifiedOn(rs.getString("MODIFIED_ON"));
	        lookup.setCheckedBy(rs.getInt("CHECKED_BY"));
	        lookup.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
	        lookup.setCheckedOn(rs.getString("CHECKED_ON"));
	        lookup.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));
	        lookup.setApprovedFlag(rs.getInt("APPROVED_FLAG"));
	        lookup.setRefLookupKey(rs.getInt("REF_LOOKUP_KEY"));

	        return lookup;
	    }
	}


}
