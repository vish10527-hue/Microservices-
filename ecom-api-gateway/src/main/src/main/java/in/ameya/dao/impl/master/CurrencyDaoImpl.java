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

import in.ameya.dao.master.CurrencyDao;
import in.ameya.model.CommonDataException;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.CurrencyDetails;
import in.ameya.model.master.CurrencySearch;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.Constants;
import in.ameya.util.DBConstants;
import in.ameya.util.ExportData;
import oracle.jdbc.OracleTypes;

@Repository
@PropertySource(value="classpath:filePath.properties")
public class CurrencyDaoImpl extends JdbcDaoSupport implements CurrencyDao {

	private static final Logger logger = LogManager.getLogger(CurrencyDao.class);
	
	@Autowired
	ExportData exportData;	
		
	@Autowired
	@Qualifier("MainDatabase")
	protected DataSource dataSource;
	
	@SuppressWarnings("unchecked")
	@Override
	public DBResponse getCurrencyList(CurrencySearch currencySearch) {
		
		AtomicInteger totalCount = new AtomicInteger(0);
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_CURRENCIES_MST_LIST);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("IPAGE_NO", OracleTypes.INTEGER),
					new SqlParameter("IPAGE_SIZE", OracleTypes.INTEGER),
					new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
					new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
					new SqlParameter("ICURR_CODE", OracleTypes.VARCHAR),
					new SqlParameter("ICURR_DESCRIPTION", OracleTypes.VARCHAR),
					new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
					new SqlParameter("ISTATUS", OracleTypes.INTEGER),
					new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new CurrencyDetailsRowMapper(totalCount))
					);
			
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", currencySearch.getLoggedInKey())
					.addValue("IPAGE_NO", currencySearch.getPageNo())
					.addValue("IPAGE_SIZE", currencySearch.getPageSize())
					.addValue("IORDER_BY_COLUMN", currencySearch.getOrderByColumn())
					.addValue("IORDER_ASC_DESC", currencySearch.getOrderAscDesc())
					.addValue("ICURR_CODE", Constants.getParameter(currencySearch.getCurrencyCode()))
					.addValue("ICURR_DESCRIPTION",Constants.getParameter(currencySearch.getCurrencyDescription()))
					.addValue("IAPPROVED_FLAG", currencySearch.getApprovedFlag())
					.addValue("ISTATUS", currencySearch.getStatus())
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				currencySearch.currencies = (ArrayList<CurrencyDetails>) out.get("IO_CURSOR");
				currencySearch.count = totalCount.intValue();
				return new DBResponse(1);
			} else {
				return new DBResponse(-1, "No result from query.");
			}
		}
		catch (Exception e) {
			logger.error("getCurrencyList("+ currencySearch +"): " + totalCount, e);
			throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching Currency List"));
		}
	}

	
	class CurrencyDetailsRowMapper implements RowMapper<CurrencyDetails> {
		
		private AtomicInteger count;
		public CurrencyDetailsRowMapper(AtomicInteger count) {
			this.count = count;
		}

		@Override
		public CurrencyDetails mapRow(ResultSet rs, int rowNumber) throws SQLException {
			this.count.accumulateAndGet(rs.getInt("TOT_COUNT"), Math::max);
			CurrencyDetails currency = new CurrencyDetails();
			
			currency.setCurrencyKey(rs.getInt("CURR_KEY"));
			currency.setCurrencyCode(rs.getString("CURR_CODE"));
			currency.setCurrencyDescription(rs.getString("CURR_DESCRIPTION"));
			currency.setStatus(rs.getInt("STATUS"));
			currency.setCreatedBy(rs.getInt("CREATED_BY"));
			currency.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
			currency.setCreatedOn(rs.getString("CREATED_ON"));
			currency.setModifiedBy(rs.getInt("MODIFIED_BY"));
			currency.setModifiedByLoginId(rs.getString("MODIFIED_BY_LOGIN_ID"));
			currency.setModifiedOn(rs.getString("MODIFIED_ON"));
			currency.setCheckedBy(rs.getInt("CHECKED_BY"));
			currency.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
			currency.setCheckedOn(rs.getString("CHECKED_ON"));
			currency.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));
			currency.setApprovedFlag(rs.getInt("APPROVED_FLAG"));
			currency.setRefCurrencyKey(rs.getInt("REF_CURR_KEY"));
			
			return currency;
		}
	}


	@Override
	public DBResponse manageCurrency(CurrencyDetails currency, Integer loggedInKey, String action) {
		DBResponse resp = null;
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_CURRENCY_MASTER_MANAGE);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("ICURR_KEY", OracleTypes.NUMBER),
					new SqlParameter("ICURR_CODE", OracleTypes.VARCHAR),
					new SqlParameter("ICURR_DESCRIPTION", OracleTypes.VARCHAR),
					new SqlParameter("ISTATUS", OracleTypes.NUMBER),
					new SqlParameter("IAPP_REJ_REMARKS", OracleTypes.VARCHAR),
					new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
					new SqlParameter("IREF_CURR_KEY", OracleTypes.NUMBER),
					new SqlParameter("IRECORD_STATUS", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
					);
			
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", loggedInKey)
					.addValue("ICURR_KEY", currency.getCurrencyKey())
					.addValue("ICURR_CODE", currency.getCurrencyCode())
					.addValue("ICURR_DESCRIPTION", currency.getCurrencyDescription())
					.addValue("ISTATUS", currency.getStatus())
					.addValue("IAPP_REJ_REMARKS", currency.getAppRejRemarks())
					.addValue("IAPPROVED_FLAG", currency.getApprovedFlag())
					.addValue("IREF_CURR_KEY", currency.getRefCurrencyKey())
					.addValue("IRECORD_STATUS", action)
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if (out != null) {
				BigDecimal result = (BigDecimal) out.get("O_RESULT");
				resp = new DBResponse(result.intValue(), (String) out.get("O_MESSAGE"), currency);
				if (resp.getStatus() == 1) {
					return resp;
				}
			}
		}
		catch (Exception e) {
			logger.error("manageCurrency("+action+"): " + currency, e);
			throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching Currency List"));
		}
		
		if(resp == null) {
			resp = new DBResponse(-1, null);
		}
		return resp;
	}


	@Override
	public DBResponse verifyCurrencyRecords(Integer loggedInKey, String currencyKeys, String remarks, String recordStatus) {
		DBResponse resp = null;
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_CURRENCY_MASTER_CHECK);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("ICURR_KEYS", OracleTypes.VARCHAR),
					new SqlParameter("IAPP_REJ_REMARKS", OracleTypes.VARCHAR),
					new SqlParameter("IRECORD_STATUS", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
					);
	
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", loggedInKey)
					.addValue("ICURR_KEYS", currencyKeys)
					.addValue("IAPP_REJ_REMARKS", remarks)
					.addValue("IRECORD_STATUS", recordStatus)
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				resp = new DBResponse((BigDecimal) out.get("O_RESULT"), (String) out.get("O_MESSAGE"));
			} 
		}
		catch (Exception e) {
			logger.error("manageCurrency("+recordStatus+"): " + currencyKeys + " -> " + remarks, e);
			resp = new DBResponse(-1, "Error While Verifing Currency Records");
		}
		
		if(resp == null) {
			resp = new DBResponse(-1, "No Records Found.");
		}
		return resp;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String exportCurrencyCSV(CurrencySearch currencySearch) {
	    SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_CURRENCIES_MST_EXP);

	    simpleJdbcCall.declareParameters(
	            new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	            new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
	            new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlParameter("ICURR_CODE", OracleTypes.VARCHAR),
	            new SqlParameter("ICURR_DESCRIPTION", OracleTypes.VARCHAR),
	            new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	            new SqlParameter("ISTATUS", OracleTypes.INTEGER),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("ILOGGEDIN_KEY", currencySearch.getLoggedInKey())
	            .addValue("IORDER_BY_COLUMN", currencySearch.getOrderByColumn())
	            .addValue("IORDER_ASC_DESC", currencySearch.getOrderAscDesc())
	            .addValue("ICURR_CODE", currencySearch.getCurrencyCode())
	            .addValue("ICURR_DESCRIPTION", currencySearch.getCurrencyDescription())
	            .addValue("IAPPROVED_FLAG", currencySearch.getApprovedFlag())
	            .addValue("ISTATUS", currencySearch.getStatus());

	    Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	    if (out != null) {
	        ArrayList<Map<String, Object>> resultSet = (ArrayList<Map<String, Object>>) out.get("IO_CURSOR");

	        if (resultSet != null && !resultSet.isEmpty()) {
	            String csvContent = exportData.generateCSV(resultSet);

	            String base64Csv = exportData.encodeBase64(csvContent);

	            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	            String filename = "CurrencyMaster_" + timestamp + ".csv";

	            return filename + "|" + base64Csv;
	        } else {
	            return "No data found";
	        }
	    } else {
	        return "Error during CSV generation";
	    }
	}

	
	@Override
	public ExportResponse exportCurrencyExcel(CurrencySearch currencySearch) throws IOException {
	    SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_CURRENCIES_MST_EXP);

	    simpleJdbcCall.declareParameters(
	            new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	            new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
	            new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlParameter("ICURR_CODE", OracleTypes.VARCHAR),
	            new SqlParameter("ICURR_DESCRIPTION", OracleTypes.VARCHAR),
	            new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	            new SqlParameter("ISTATUS", OracleTypes.INTEGER),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("ILOGGEDIN_KEY", currencySearch.getLoggedInKey())
	            .addValue("IORDER_BY_COLUMN", currencySearch.getOrderByColumn())
	            .addValue("IORDER_ASC_DESC", currencySearch.getOrderAscDesc())
	            .addValue("ICURR_CODE", currencySearch.getCurrencyCode())
	            .addValue("ICURR_DESCRIPTION", currencySearch.getCurrencyDescription())
	            .addValue("IAPPROVED_FLAG", currencySearch.getApprovedFlag())
	            .addValue("ISTATUS", currencySearch.getStatus());

	    Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	    @SuppressWarnings("unchecked")
	    ArrayList<Map<String, Object>> resultSet = (ArrayList<Map<String, Object>>) out.get("IO_CURSOR");

	    if (resultSet != null && !resultSet.isEmpty()) {
	        // Capture column order from the first row
	        List<String> columnOrder = new ArrayList<>(resultSet.get(0).keySet());

	        ArrayList<JSONObject> jsonData = new ArrayList<>();
	        for (Map<String, Object> row : resultSet) {
	            JSONObject jsonObject = new JSONObject(row);
	            jsonData.add(jsonObject);
	        }

	        // Pass columnOrder explicitly to maintain the order
	        String filename = "CurrencyMaster_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
	        String base64Data = exportData.generateExcelBase64(jsonData, columnOrder);

	        return new ExportResponse(base64Data, filename);
	    }

	    return new ExportResponse(null, null);
	}



	@SuppressWarnings("unchecked")
	@Override
	public CurrencyDetails getCurrencyDetailsById(Integer currencyId, Integer userId) {
	    try {
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_CURRENCIES_MST_DET);

	        simpleJdbcCall.declareParameters(
	                new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	                new SqlParameter("ICURR_KEY", OracleTypes.NUMBER),
	                new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new CurrencyDetailsViewRowMapper())
	        );

	        SqlParameterSource paramSource = new MapSqlParameterSource()
	                .addValue("ILOGGEDIN_KEY", userId)
	                .addValue("ICURR_KEY", currencyId);

	        Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	        if (out != null && out.get("IO_CURSOR") != null) {
	            List<CurrencyDetails> currencyList = (List<CurrencyDetails>) out.get("IO_CURSOR");
	            if (!currencyList.isEmpty()) {
	                return currencyList.get(0);
	            } else {
	                throw new CommonDataException(HttpStatus.NOT_FOUND, new DBResponse(-1, "Currency not found for ID: " + currencyId));
	            }
	        } else {
	            throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "No data returned from procedure."));
	        }
	    } catch (Exception e) {
	        logger.error("Error in getCurrencyDetailsById(currencyId=" + currencyId + ", userId=" + userId + "): ", e);
	        throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error fetching currency details"));
	    }
	}
	
	class CurrencyDetailsViewRowMapper implements RowMapper<CurrencyDetails> {

	    @Override
	    public CurrencyDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
	        CurrencyDetails currency = new CurrencyDetails();

	        currency.setCurrencyKey(rs.getInt("CURR_KEY"));
	        currency.setCurrencyCode(rs.getString("CURR_CODE"));
	        currency.setCurrencyDescription(rs.getString("CURR_DESCRIPTION"));
	        currency.setStatus(rs.getInt("STATUS"));
	        currency.setCreatedBy(rs.getInt("CREATED_BY"));
	        currency.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
	        currency.setCreatedOn(rs.getString("CREATED_ON"));
	        currency.setModifiedBy(rs.getInt("MODIFIED_BY"));
	        currency.setModifiedByLoginId(rs.getString("MODIFIED_BY_LOGIN_ID"));
	        currency.setModifiedOn(rs.getString("MODIFIED_ON"));
	        currency.setCheckedBy(rs.getInt("CHECKED_BY"));
	        currency.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
	        currency.setCheckedOn(rs.getString("CHECKED_ON"));
	        currency.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));
	        currency.setApprovedFlag(rs.getInt("APPROVED_FLAG"));
	        currency.setRefCurrencyKey(rs.getInt("REF_CURR_KEY"));

	        return currency;
	    }
	}


}
