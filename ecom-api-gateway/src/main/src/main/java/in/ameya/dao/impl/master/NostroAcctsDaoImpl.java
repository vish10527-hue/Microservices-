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

import in.ameya.dao.master.NostroAcctsDao;
import in.ameya.model.CommonDataException;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.NostroAcctsDetails;
import in.ameya.model.master.NostroAcctsSearch;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.Constants;
import in.ameya.util.DBConstants;
import in.ameya.util.ExportData;
import oracle.jdbc.OracleTypes;


@Repository
@PropertySource(value="classpath:filePath.properties")
public class NostroAcctsDaoImpl extends JdbcDaoSupport implements NostroAcctsDao {

	private static final Logger logger = LogManager.getLogger(NostroAcctsDao.class);
	
	@Autowired
	ExportData exportData;	
		
	@Autowired
	@Qualifier("MainDatabase")
	protected DataSource dataSource;
	
	@SuppressWarnings("unchecked")
	@Override
	public DBResponse getNostroAcctsList(NostroAcctsSearch nostroAcctsSearch) {
		
		AtomicInteger totalCount = new AtomicInteger(0);
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_NOSTRO_ACC_MST_LIST);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("IPAGE_NO", OracleTypes.INTEGER),
					new SqlParameter("IPAGE_SIZE", OracleTypes.INTEGER),
					new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
					new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
					new SqlParameter("INOSTRO_ACCT_NO", OracleTypes.VARCHAR),
					new SqlParameter("INOSTRO_NAME", OracleTypes.VARCHAR),
					new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
					new SqlParameter("ISTATUS", OracleTypes.INTEGER),
					new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new NostroAcctsDetailsRowMapper(totalCount))
					);
			
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", nostroAcctsSearch.getLoggedInKey())
					.addValue("IPAGE_NO", nostroAcctsSearch.getPageNo())
					.addValue("IPAGE_SIZE", nostroAcctsSearch.getPageSize())
					.addValue("IORDER_BY_COLUMN", Constants.getParameter(nostroAcctsSearch.getOrderByColumn()))
					.addValue("IORDER_ASC_DESC", Constants.getParameter(nostroAcctsSearch.getOrderAscDesc()))
					.addValue("INOSTRO_ACCT_NO", Constants.getParameter(nostroAcctsSearch.getNostroAcctNo()))
					.addValue("INOSTRO_NAME",nostroAcctsSearch.getNostroAcctName())
					.addValue("IAPPROVED_FLAG", nostroAcctsSearch.getApprovedFlag())
					.addValue("ISTATUS", nostroAcctsSearch.getStatus())
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				nostroAcctsSearch.nostroAccts = (ArrayList<NostroAcctsDetails>) out.get("IO_CURSOR");
				nostroAcctsSearch.count = totalCount.intValue();
				return new DBResponse(1);
			} else {
				return new DBResponse(-1, "No result from query.");
			}
		}
		catch (Exception e) {
			logger.error("getNostroAcctsList("+ nostroAcctsSearch +"): " + totalCount, e);
			throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching NostroAccts List"));
		}
	}

	
	class NostroAcctsDetailsRowMapper implements RowMapper<NostroAcctsDetails> {
		
		private AtomicInteger count;
		public NostroAcctsDetailsRowMapper(AtomicInteger count) {
			this.count = count;
		}

		@Override
		public NostroAcctsDetails mapRow(ResultSet rs, int rowNumber) throws SQLException {
			this.count.accumulateAndGet(rs.getInt("TOT_COUNT"), Math::max);
			NostroAcctsDetails nostroAccts = new NostroAcctsDetails();
			nostroAccts.setNostroAcctsKey(rs.getInt("NOSTRO_KEY"));
			nostroAccts.setNostroAcctsNo(rs.getString("NOSTRO_ACCT_NO"));
			nostroAccts.setNostroName(rs.getString("NOSTRO_NAME"));
			nostroAccts.setBankCode(rs.getString("BANK_CODE"));
			nostroAccts.setCurrKey(rs.getString("CURR_KEY"));
			nostroAccts.setCurrCode(rs.getString("CURR_CODE"));
			nostroAccts.setSenderBic(rs.getString("SENDER_BIC"));
			nostroAccts.setReceiverBic(rs.getString("RECEIVER_BIC"));
			nostroAccts.setStatus(rs.getInt("STATUS"));
			nostroAccts.setCreatedBy(rs.getInt("CREATED_BY"));
			nostroAccts.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
			nostroAccts.setCreatedOn(rs.getString("CREATED_ON"));
			nostroAccts.setModifiedBy(rs.getInt("MODIFIED_BY"));
			nostroAccts.setModifiedByLoginId(rs.getString("MODIFIED_BY_LOGIN_ID"));
			nostroAccts.setModifiedOn(rs.getString("MODIFIED_ON"));
			nostroAccts.setCheckedBy(rs.getInt("CHECKED_BY"));
			nostroAccts.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
			nostroAccts.setCheckedOn(rs.getString("CHECKED_ON"));
			nostroAccts.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));
			nostroAccts.setApprovedFlag(rs.getInt("APPROVED_FLAG"));
			nostroAccts.setRefNostroAcctsKey(rs.getInt("REF_NOSTRO_KEY"));
			nostroAccts.setOutFileType(rs.getInt("OUT_FILE_TYPE"));
			nostroAccts.setFinNostroMirror(rs.getString("FIN_NOSTRO_MIRROR"));
			return nostroAccts;
		}
	}


	@Override
	public DBResponse manageNostroAccts(NostroAcctsDetails nostroAccts, Integer loggedInKey, String action) {
		DBResponse resp = null;
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_NOSTRO_ACCT_MASTER_MANAGE);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("INOSTRO_KEY", OracleTypes.NUMBER),
					new SqlParameter("INOSTRO_ACCT_NO", OracleTypes.VARCHAR),
					new SqlParameter("INOSTRO_NAME", OracleTypes.VARCHAR),
					new SqlParameter("IBANK_CODE", OracleTypes.VARCHAR),
					new SqlParameter("ICURR_KEY", OracleTypes.VARCHAR),
					new SqlParameter("ISENDER_BIC", OracleTypes.VARCHAR),
					new SqlParameter("IRECEIVER_BIC", OracleTypes.VARCHAR),
					new SqlParameter("ISTATUS", OracleTypes.NUMBER),
					new SqlParameter("IAPP_REJ_REMARKS", OracleTypes.VARCHAR),
					new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
					new SqlParameter("IREF_NOSTRO_KEY", OracleTypes.NUMBER),
					new SqlParameter("IRECORD_STATUS", OracleTypes.VARCHAR),
					new SqlParameter("IOUT_FILE_TYPE", OracleTypes.NUMBER),
					new SqlParameter("IFIN_NOSTRO_MIRROR", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
					);
			
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", loggedInKey)
					.addValue("INOSTRO_KEY", nostroAccts.getNostroAcctsKey())
					.addValue("INOSTRO_ACCT_NO", nostroAccts.getNostroAcctsNo())
					.addValue("INOSTRO_NAME", nostroAccts.getNostroName())
					.addValue("IBANK_CODE", nostroAccts.getBankCode())
					.addValue("ICURR_KEY", nostroAccts.getCurrKey())
					.addValue("ISENDER_BIC", nostroAccts.getSenderBic())
					.addValue("IRECEIVER_BIC", nostroAccts.getReceiverBic())
					.addValue("ISTATUS", nostroAccts.getStatus())
					.addValue("IAPP_REJ_REMARKS", nostroAccts.getAppRejRemarks())
					.addValue("IAPPROVED_FLAG", nostroAccts.getApprovedFlag())
					.addValue("IREF_NOSTRO_KEY", nostroAccts.getRefNostroAcctsKey())
					.addValue("IOUT_FILE_TYPE", nostroAccts.getOutFileType())
					.addValue("IFIN_NOSTRO_MIRROR", nostroAccts.getFinNostroMirror())
					.addValue("IRECORD_STATUS", action)
					;
			
					Map<String, Object> out = simpleJdbcCall.execute(paramSource);
					if (out != null) {
						BigDecimal result = (BigDecimal) out.get("O_RESULT");
						resp = new DBResponse(result.intValue(), (String) out.get("O_MESSAGE"),nostroAccts);

						if (resp.getStatus() == 1) {
							return resp;
						}
					}
		}
		catch (Exception e) {
			logger.error("manageNostroAccts("+action+"): " + nostroAccts, e);
			throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching NostroAccts List"));
		}
		if(resp == null) {
			resp = new DBResponse(-1,null);
		}
		return resp;
	}


	@Override
	public DBResponse verifyNostroAcctsRecords(Integer loggedInKey, String nostroAcctsKeys, String remarks, String recordStatus) {
		DBResponse resp = null;
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_NOSTRO_ACCT_MASTER_CHECK);
			
			simpleJdbcCall.declareParameters(					
					new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
					new SqlParameter("INOSTRO_KEYS", OracleTypes.VARCHAR),
					new SqlParameter("IAPP_REJ_REMARKS", OracleTypes.VARCHAR),
					new SqlParameter("IRECORD_STATUS", OracleTypes.VARCHAR),
					new SqlOutParameter("O_RESULT", OracleTypes.NUMBER),
					new SqlOutParameter("O_MESSAGE", OracleTypes.VARCHAR)
					);
	
			SqlParameterSource paramSource = new MapSqlParameterSource()
					.addValue("ILOGGEDIN_KEY", loggedInKey)
					.addValue("INOSTRO_KEYS", nostroAcctsKeys)
					.addValue("IAPP_REJ_REMARKS", remarks)
					.addValue("IRECORD_STATUS", recordStatus)
					;
			
			Map<String, Object> out = simpleJdbcCall.execute(paramSource);
			if(out != null) {
				resp = new DBResponse((BigDecimal) out.get("O_RESULT"), (String) out.get("O_MESSAGE"));
			} 
		}
		catch (Exception e) {
			logger.error("manageNostroAccts("+recordStatus+"): " + nostroAcctsKeys + " -> " + remarks, e);
			resp = new DBResponse(-1, "Error While Verifing NostroAccts Records");
		}
		
		if(resp == null) {
			resp = new DBResponse(-1, "No Records Found.");
		}
		
		return resp;
	}
	
	
	@Override
	public String exportNostroAcctsCSV(NostroAcctsSearch nostroAcctsSearch) throws IOException {
	    SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_NOSTRO_ACC_MST_EXP);

	    simpleJdbcCall.declareParameters(
	            new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	            new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
	            new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlParameter("INOSTRO_ACCT_NO", OracleTypes.VARCHAR),
	            new SqlParameter("INOSTRO_NAME", OracleTypes.VARCHAR),
	            new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	            new SqlParameter("ISTATUS", OracleTypes.INTEGER),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("ILOGGEDIN_KEY", nostroAcctsSearch.getLoggedInKey())
	            .addValue("IORDER_BY_COLUMN", nostroAcctsSearch.getOrderByColumn())
	            .addValue("IORDER_ASC_DESC", nostroAcctsSearch.getOrderAscDesc())
	            .addValue("INOSTRO_ACCT_NO", nostroAcctsSearch.getNostroAcctNo())
	            .addValue("INOSTRO_NAME", nostroAcctsSearch.getNostroAcctName())
	            .addValue("IAPPROVED_FLAG", nostroAcctsSearch.getApprovedFlag())
	            .addValue("ISTATUS", nostroAcctsSearch.getStatus());

	    Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	    if (out != null) {
	        @SuppressWarnings("unchecked")
	        ArrayList<Map<String, Object>> resultSet = (ArrayList<Map<String, Object>>) out.get("IO_CURSOR");

	        if (resultSet != null && !resultSet.isEmpty()) {
	            String csvContent = exportData.generateCSV(resultSet);
	            String base64Csv = exportData.encodeBase64(csvContent);

	            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	            String filename = "NostroAcctsMaster_" + timestamp + ".csv";

	            return filename + "|" + base64Csv;
	        } else {
	            return "No data found";
	        }
	    } else {
	        return "Error during CSV generation";
	    }
	}


	@Override
	public ExportResponse exportNostroAcctsExcel(NostroAcctsSearch nostroAcctsSearch) throws IOException {
	    SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_NOSTRO_ACC_MST_EXP);

	    simpleJdbcCall.declareParameters(
	            new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	            new SqlParameter("IORDER_BY_COLUMN", OracleTypes.VARCHAR),
	            new SqlParameter("IORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlParameter("INOSTRO_ACCT_NO", OracleTypes.VARCHAR),
	            new SqlParameter("INOSTRO_NAME", OracleTypes.VARCHAR),
	            new SqlParameter("IAPPROVED_FLAG", OracleTypes.NUMBER),
	            new SqlParameter("ISTATUS", OracleTypes.INTEGER),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("ILOGGEDIN_KEY", nostroAcctsSearch.getLoggedInKey())
	            .addValue("IORDER_BY_COLUMN", nostroAcctsSearch.getOrderByColumn())
	            .addValue("IORDER_ASC_DESC", nostroAcctsSearch.getOrderAscDesc())
	            .addValue("INOSTRO_ACCT_NO", nostroAcctsSearch.getNostroAcctNo())
	            .addValue("INOSTRO_NAME", nostroAcctsSearch.getNostroAcctName())
	            .addValue("IAPPROVED_FLAG", nostroAcctsSearch.getApprovedFlag())
	            .addValue("ISTATUS", nostroAcctsSearch.getStatus());

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

	        String filename = "NostroAcctsMaster_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
	        String base64Data = exportData.generateExcelBase64(jsonData, columnOrder);

	        return new ExportResponse(base64Data, filename);
	    }

	    return new ExportResponse(null, null);
	}



	@SuppressWarnings("unchecked")
	@Override
	public NostroAcctsDetails getNostroAcctsDetailsById(Integer nostroAcctsKey, Integer userId) {
	    try {
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_SEL_NOSTRO_ACC_MST_DET);

	        simpleJdbcCall.declareParameters(
	                new SqlParameter("ILOGGEDIN_KEY", OracleTypes.NUMBER),
	                new SqlParameter("INOSTRO_KEY", OracleTypes.NUMBER),
	                new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new NostroAcctsDetailsViewRowMapper())
	        );

	        SqlParameterSource paramSource = new MapSqlParameterSource()
	                .addValue("ILOGGEDIN_KEY", userId)
	                .addValue("INOSTRO_KEY", nostroAcctsKey);

	        Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	        if (out != null && out.get("IO_CURSOR") != null) {
	            List<NostroAcctsDetails> nostroAcctsList = (List<NostroAcctsDetails>) out.get("IO_CURSOR");
	            if (!nostroAcctsList.isEmpty()) {
	                return nostroAcctsList.get(0);
	            } else {
	                throw new CommonDataException(HttpStatus.NOT_FOUND, new DBResponse(-1, "NostroAccts not found for ID: " + nostroAcctsKey));
	            }
	        } else {
	            throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "No data returned from procedure."));
	        }
	    } catch (Exception e) {
	        logger.error("Error in getNostroAcctsDetailsById(nostroAcctsKey=" + nostroAcctsKey + ", userId=" + userId + "): ", e);
	        throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error fetching nostroAccts details"));
	    }
	}
	
	class NostroAcctsDetailsViewRowMapper implements RowMapper<NostroAcctsDetails> {

	    @Override
	    public NostroAcctsDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
	        NostroAcctsDetails nostroAccts = new NostroAcctsDetails();
	        nostroAccts.setNostroAcctsKey(rs.getInt("NOSTRO_KEY"));
			nostroAccts.setNostroAcctsNo(rs.getString("NOSTRO_ACCT_NO"));
			nostroAccts.setNostroName(rs.getString("NOSTRO_NAME"));
			nostroAccts.setBankCode(rs.getString("BANK_CODE"));
			nostroAccts.setCurrKey(rs.getString("CURR_KEY"));
			nostroAccts.setCurrCode(rs.getString("CURR_CODE"));
			nostroAccts.setSenderBic(rs.getString("SENDER_BIC"));
			nostroAccts.setReceiverBic(rs.getString("RECEIVER_BIC"));
	        nostroAccts.setCreatedBy(rs.getInt("CREATED_BY"));
	        nostroAccts.setCreatedByLoginId(rs.getString("CREATED_BY_LOGIN_ID"));
	        nostroAccts.setCreatedOn(rs.getString("CREATED_ON"));
	        nostroAccts.setModifiedBy(rs.getInt("MODIFIED_BY"));
	        nostroAccts.setModifiedByLoginId(rs.getString("MODIFIED_BY_LOGIN_ID"));
	        nostroAccts.setModifiedOn(rs.getString("MODIFIED_ON"));
	        nostroAccts.setCheckedBy(rs.getInt("CHECKED_BY"));
	        nostroAccts.setCheckedByLoginId(rs.getString("CHECKED_BY_LOGIN_ID"));
	        nostroAccts.setCheckedOn(rs.getString("CHECKED_ON"));
	        nostroAccts.setAppRejRemarks(rs.getString("APP_REJ_REMARKS"));
	        nostroAccts.setApprovedFlag(rs.getInt("APPROVED_FLAG"));
	        nostroAccts.setRefNostroAcctsKey(rs.getInt("REF_NOSTRO_KEY"));
	        nostroAccts.setFinNostroMirror(rs.getString("FIN_NOSTRO_MIRROR"));
	        return nostroAccts;
	    }
	}



}
