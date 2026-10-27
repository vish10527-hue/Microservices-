package in.ameya.dao.impl.transaction;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
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

import in.ameya.dao.transaction.TransactionDao;
import in.ameya.model.CommonDataException;
import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.transaction.Transaction;
import in.ameya.model.transaction.TransactionSearch;
import in.ameya.schedulers.vo.fileSeries.ActivityMsgVo;
import in.ameya.schedulers.vo.fileSeries.CredenceMirrorVo;
import in.ameya.schedulers.vo.fileSeries.FittCsvVo;
import in.ameya.schedulers.vo.fileSeries.FottCsvVo;
import in.ameya.schedulers.vo.fileSeries.OtherTxnsCsvVo;
import in.ameya.schedulers.vo.mtSeries.Type950;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.AESEncryptDecryptUtil;
import in.ameya.util.DBConstants;
import in.ameya.util.ExportData;
import oracle.jdbc.OracleTypes;

@Repository
public class TransactionDaoImpl extends JdbcDaoSupport implements TransactionDao {

	private static final Logger logger = LogManager.getLogger(TransactionDaoImpl.class);
	
	@Autowired
	ExportData exportData;	
	
	@Autowired
	@Qualifier("MainDatabase")
	protected DataSource dataSource;
	
	@Autowired
	AESEncryptDecryptUtil aesEncryptDecryptUtil;
	
	@Override
	public String exportTransactionCSV(Transaction transactionObj) throws IOException {
	    SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKG_TRANSACTION_ONE_STP_FETCH_UPLOADED_MSG_EXP);

	    simpleJdbcCall.declareParameters(
                new SqlParameter("IMSG_TYPE_KEY", OracleTypes.NUMBER),
                new SqlParameter("IFROM_DATE", OracleTypes.VARCHAR),
                new SqlParameter("ITO_DATE", OracleTypes.VARCHAR),
                new SqlParameter("IFILE_NAME", OracleTypes.VARCHAR),
                new SqlParameter("ISTATUS", OracleTypes.NUMBER),
                new SqlParameter("ILOGIN_USER_KEY", OracleTypes.NUMBER),
                new SqlParameter("I_ORDER_BY_COLUMN", OracleTypes.VARCHAR),
                new SqlParameter("I_ORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue("IMSG_TYPE_KEY", checkEmpty(transactionObj.getMsgTypeKey()))
                .addValue("IFROM_DATE", checkEmpty(transactionObj.getFromDate()))
                .addValue("ITO_DATE", checkEmpty(transactionObj.getToDate()))
                .addValue("IFILE_NAME", checkEmpty(transactionObj.getFileName()))
                .addValue("ISTATUS", checkEmpty(transactionObj.getStatus()))
                .addValue("ILOGIN_USER_KEY", checkEmpty(transactionObj.getLoggedInKey()))
		        .addValue("I_ORDER_BY_COLUMN", checkEmpty(transactionObj.getOrderByColumn()))
		        .addValue("I_ORDER_ASC_DESC", checkEmpty(transactionObj.getOrderAscDesc()));

	    Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	    if (out != null) {
	        @SuppressWarnings("unchecked")
	        ArrayList<Map<String, Object>> resultSet = (ArrayList<Map<String, Object>>) out.get("IO_CURSOR");

	        if (resultSet != null && !resultSet.isEmpty()) {
	            String csvContent = exportData.generateCSV(resultSet);
	            String base64Csv = exportData.encodeBase64(csvContent);

	            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	            String filename = "InwardOutwardMessages_" + timestamp + ".csv";

	            return filename + "|" + base64Csv;
	        } else {
	            return "No data found";
	        }
	    } else {
	        return "Error during CSV generation";
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	public DBResponse getUploadedMsgDataList(Transaction transactionObj) {
	    try {
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKG_TRANSACTION_ONE_STP_FETCH_UPLOADED_MSG_LIST);

	        simpleJdbcCall
	            .withoutProcedureColumnMetaDataAccess()
	            .declareParameters(
	                new SqlParameter("IPAGE_NO", OracleTypes.NUMBER),
	                new SqlParameter("IPAGE_SIZE", OracleTypes.NUMBER),
	                new SqlParameter("IMSG_TYPE_KEY", OracleTypes.NUMBER),
	                new SqlParameter("IFROM_DATE", OracleTypes.VARCHAR),
	                new SqlParameter("ITO_DATE", OracleTypes.VARCHAR),
	                new SqlParameter("IFILE_NAME", OracleTypes.VARCHAR),
	                new SqlParameter("ISTATUS", OracleTypes.NUMBER),
	              //  new SqlParameter("IIN_OUT_FLAG", OracleTypes.NUMBER),
	                new SqlParameter("ILOGIN_USER_KEY", OracleTypes.NUMBER),
	                new SqlParameter("I_ORDER_BY_COLUMN", OracleTypes.VARCHAR),
	                new SqlParameter("I_ORDER_ASC_DESC", OracleTypes.VARCHAR),
	                new SqlOutParameter("O_CURSOR", OracleTypes.CURSOR, new UploadedMsgDataListRowMapper())
	            );

	        SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("IPAGE_NO", checkEmpty(transactionObj.getPageNo()))
	            .addValue("IPAGE_SIZE", checkEmpty(transactionObj.getPageSize()))
	            .addValue("IMSG_TYPE_KEY", checkEmpty(transactionObj.getMsgTypeKey()))
	            .addValue("IFROM_DATE", checkEmpty(transactionObj.getFromValueDate()))
	            .addValue("ITO_DATE", checkEmpty(transactionObj.getToValueDate()))
	            .addValue("IFILE_NAME", checkEmpty(transactionObj.getFileName()))
	            .addValue("ISTATUS", checkEmpty(transactionObj.getStatus()))
	           // .addValue("IIN_OUT_FLAG", "".equalsIgnoreCase(transactionObj.getInOutFlag()) ? null:transactionObj.getInOutFlag())
	            .addValue("ILOGIN_USER_KEY", checkEmpty(transactionObj.getLoggedInKey()))
	            .addValue("I_ORDER_BY_COLUMN", checkEmpty(transactionObj.getOrderByColumn()))
	            .addValue("I_ORDER_ASC_DESC", checkEmpty(transactionObj.getOrderAscDesc()));

	        Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	        if (out != null) {
	            ArrayList<TransactionSearch> results = (ArrayList<TransactionSearch>) out.get("O_CURSOR");
	            if(results!=null && results.size()>0) {
		            transactionObj.setTransactionSearch(results);	            
		            // Set count based on the size of results
		            transactionObj.setCount(results.get(0).getCount());
	            }
	            return new DBResponse(1);
	        } else {
	            return new DBResponse(-1, "No result from query.");
	        }

	    } catch (Exception e) {
	        logger.error("getUploadedMsgDataList(" + transactionObj + ")", e);
	        throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching Transaction List"));
	    }
	}

	class UploadedMsgDataListRowMapper implements RowMapper<TransactionSearch> {
	    @Override
	    public TransactionSearch mapRow(ResultSet rs, int rowNumber) throws SQLException {
	        TransactionSearch details = new TransactionSearch();

	        details.setUploadKey(rs.getString("UPLOAD_KEY"));
	        details.setMsgTypeKey(rs.getString("MSG_TYPE_KEY"));
	        details.setMsgDesc(rs.getString("MSG_DESC"));
	       // details.setMsgCodeMX(rs.getString("MSG_CODE_MX"));
	        details.setFileName(rs.getString("FILE_NAME"));
	        details.setStatus(rs.getString("STATUS"));
	        details.setNoRecordsSaved(rs.getString("NO_RECORDS_SAVED"));
	        details.setNoRecordsRejected(rs.getString("NO_RECORDS_REJECTED"));
	        details.setNoRecordsTotal(rs.getString("NO_RECORDS_TOTAL"));
	        details.setCreatedOn(rs.getString("CREATED_ON"));
	        details.setCreatedOnOrderBy(rs.getString("CREATED_ON_ORDER_BY"));
	        details.setCount(rs.getInt("TOTAL_RECORDS"));	 
	        return details;
	    }
	}


	// RowMapper for msgTypeKey 1 & 2 - Type950
	public class Type950RowMapper implements RowMapper<Object> {
	    @Override
	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	        Type950 type950 = new Type950();
	        type950.setUploadKey(rs.getString("UPLOAD_KEY"));
	        type950.setSenderCode(rs.getString("SENDER_CODE"));
	        type950.setReceiverBIC(rs.getString("REC_BIC"));
	        type950.setTRFNo(rs.getString("TRAN_REF_NO"));
	        type950.setIntRefNo(rs.getString("INT_REF_NO"));
	        type950.setValueDate(rs.getString("VALUE_DATE"));
	        type950.setCurrCode(rs.getString("CURR_CODE"));
	        type950.setMsgAmount(rs.getString("MSG_AMOUNT"));
	        type950.setTranType(rs.getString("TRAN_TYPE"));
	        type950.setBankDetails(rs.getString("BANK_DETAILS"));
	        type950.setField86Data(rs.getString("FIELD_86_DATA"));
	        type950.setStrAccountID(rs.getString("ACCOUNT_ID"));
	        type950.setEntryDate(rs.getString("ENTRY_DATE"));
	        type950.setTransTypeIdentifiCode(rs.getString("TRANS_TYPE_IDENTIFI_CODE"));
	        type950.setCreatedOn(rs.getString("CREATED_ON"));
	        return type950;
	    }
	}

	// RowMapper for msgTypeKey 3 - CredenceMirrorVo
	public class CredenceMirrorRowMapper implements RowMapper<Object> {
	    @Override
	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	        CredenceMirrorVo credenceMirror = new CredenceMirrorVo();
	        credenceMirror.setUploadKey(rs.getString("UPLOAD_KEY"));
	        credenceMirror.setSrNo(rs.getString("SR_NO"));
	        credenceMirror.setSettlementRefNo(rs.getString("SETTLEMENT_REF_NO"));
	        credenceMirror.setPostingDate(rs.getString("POSTING_DATE"));
	        credenceMirror.setValueDate(rs.getString("VALUE_DATE"));
	        credenceMirror.setCurrencyCode(rs.getString("CURR_CODE"));
	        credenceMirror.setNostroBalance(rs.getString("NOSTRO_BALANCE"));
	        credenceMirror.setNostroDrCr(rs.getString("NOSTRO_DRCR"));
	        credenceMirror.setLcyBalance(rs.getString("LCY_BALANCE"));
	        credenceMirror.setLcyBalanceDrCr(rs.getString("LCY_BAL_DRCR"));
	        credenceMirror.setTradeType(rs.getString("TRADE_TYPE"));
	        credenceMirror.setBankName(rs.getString("BANK_NAME"));
	        credenceMirror.setAccountNo(rs.getString("ACCOUNT_NO"));
	        credenceMirror.setProduct(rs.getString("PRODUCT"));
	        credenceMirror.setDealerTicketNo(rs.getString("DEALER_TICKET_NO"));
	        credenceMirror.setDealer(rs.getString("DEALER"));
	        credenceMirror.setCounterParty(rs.getString("COUNTER_PARTY"));
	        credenceMirror.setNostroRefdate(rs.getString("NOSTRO_REFDATE"));
	        credenceMirror.setNostroRefNo(rs.getString("NOSTRO_REF_NO"));
	        credenceMirror.setDealType(rs.getString("DEAL_TYPE"));
	        credenceMirror.setBranchName(rs.getString("BRANCH_NAME"));
	        credenceMirror.setTransactionType(rs.getString("TRANSACTION_TYPE"));
	        credenceMirror.setSettlementType(rs.getString("SETTLEMENT_TYPE"));
	        credenceMirror.setTradeDate(rs.getString("TRADE_DATE"));
	        credenceMirror.setQuantity(rs.getString("QUANTITY"));
	        credenceMirror.setRate(rs.getString("RATE"));
	        credenceMirror.setAmount(rs.getString("AMOUNT"));
	        credenceMirror.setPaymentMode(rs.getString("PAYMENT_MODE"));
	        credenceMirror.setCountry(rs.getString("COUNTRY"));
	        credenceMirror.setDealOwner(rs.getString("DEAL_OWNER"));
	        credenceMirror.setBranchRefNo(rs.getString("BRANCH_REF_NO"));
	        credenceMirror.setInvestment(rs.getString("INVESTMENT"));
	        credenceMirror.setDealSourceRefNo(rs.getString("DEAL_SOURCE_REF_NO"));
	        credenceMirror.setSettlementFlag(rs.getString("SETTLEMENT_FLAG"));
	        credenceMirror.setRefNo(rs.getString("REF_NO"));
	        credenceMirror.setLegNo(rs.getString("LEG_NO"));
	        credenceMirror.setBankCode(rs.getString("BANK_CODE"));
	        credenceMirror.setReversalStatus(rs.getString("REVERSAL_STATUS"));
	        credenceMirror.setLcyRate(rs.getString("LCY_RATE"));
	        credenceMirror.setUsdRate(rs.getString("USD_RATE"));
	        credenceMirror.setUsdBalance(rs.getString("USD_BALANCE"));
	        credenceMirror.setUsdBalDrcr(rs.getString("USD_BAL_DRCR"));
	        credenceMirror.setRemarks(rs.getString("REMARKS"));
	        credenceMirror.setNostroRefdate1(rs.getString("NOSTRO_REFDATE_1"));
	        credenceMirror.setNostroRefNo1(rs.getString("NOSTRO_REF_NO_1"));
	        credenceMirror.setCreatedOn(rs.getString("CREATED_ON"));
	        return credenceMirror;
	    }
	}

	// RowMapper for msgTypeKey 4 - SwiftMT103Vo
	public class FittCsvVoRowMapper implements RowMapper<Object> {
	    @Override
	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	FittCsvVo fittCsv = new FittCsvVo();
	       fittCsv.setUploadKey(rs.getString("UPLOAD_KEY"));
	       fittCsv.setProductCode(rs.getString("PRODUCT_CODE"));
	       fittCsv.setSenderBic(rs.getString("SENDER_BIC"));
	       fittCsv.setReceiverBic(rs.getString("RECEIVER_BIC"));
	       fittCsv.setBookDate(rs.getString("BOOK_DATE"));
	       fittCsv.setSenderReference(rs.getString("SENDER_REFERENCE"));
	       fittCsv.setRelatedReference(rs.getString("RELATED_REFERENCE"));
	       fittCsv.setTransactionTypeCode(rs.getString("TRANSACTION_TYPE_CODE"));
	       fittCsv.setValueDate(rs.getString("VALUE_DATE"));
	       fittCsv.setCurrencyCode(rs.getString("CURR_CODE"));
	       fittCsv.setTxnAmount(rs.getString("TXN_AMT"));
	       fittCsv.setExchangeRate(rs.getString("EXCH_RATE"));
	       fittCsv.setOrderingCustomer(rs.getString("ORDERING_CUSTOMER"));
	       fittCsv.setBeneficiaryCustomer(rs.getString("BENEFICIARY_CUSTOMER"));
	       fittCsv.setBranchReference(rs.getString("BRANCH_REFERENCE"));
	       fittCsv.setCreatedOn(rs.getString("CREATED_ON"));
	        return fittCsv;
	    }
	}

	// RowMapper for msgTypeKey 5 - FittCsvVo
	public class FottCsvVoRowMapper implements RowMapper<Object> {
	    @Override
	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	FottCsvVo fott = new FottCsvVo();
	        fott.setUploadKey(rs.getString("UPLOAD_KEY"));
	        fott.setProductCode(rs.getString("PRODUCT_CODE"));
	        fott.setSenderBic(rs.getString("SENDER_BIC"));
	        fott.setReceiver(rs.getString("RECEIVER"));
	        fott.setBookDate(rs.getString("BOOK_DATE"));
	        fott.setSenderReference(rs.getString("SENDERS_REFERENCE"));
	        fott.setTransactionTypeCode(rs.getString("TRANSACTION_TYPE_CODE"));
	        fott.setValueDate(rs.getString("VALUE_DATE"));
	        fott.setCurrencyCode(rs.getString("CURR_CODE"));
	        fott.setTransactionAmount(rs.getString("TRANSACTION_AMOUNT"));
	        fott.setInstructedCurrencyCode(rs.getString("INST_CURR_CODE"));
	        fott.setInstructedAmount(rs.getString("INSTRUCTED_AMOUNT"));
	        fott.setExchangeRate(rs.getString("EXCH_RATE"));
	        fott.setOrderingCustomer2(rs.getString("ORDERING_CUSTOMER2"));
	        fott.setOrderingInstitution(rs.getString("ORDERING_INSTITUTION"));
	        fott.setOrderingInstitution2(rs.getString("ORDERING_INSTITUTION2"));
	        fott.setBranchReference(rs.getString("BRANCH_REFERENCE"));
	        fott.setReceiver1(rs.getString("RECEIVER_1"));
	        fott.setAccountWithInstitution(rs.getString("ACCOUNT_WITH_INSTITUTION"));
	        fott.setBeneficiaryCustomer(rs.getString("BENEFICIARY_CUSTOMER"));
	        fott.setCreatedOn(rs.getString("CREATED_ON"));
	        return fott;
	    }
	}

	// RowMapper for msgTypeKey 6 - FottOutwardVo
	public class OtherTxnsRowMapper implements RowMapper<Object> {
	    @Override
	    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	OtherTxnsCsvVo othTxn = new OtherTxnsCsvVo();
	        othTxn.setUploadKey(rs.getString("UPLOAD_KEY"));
	        othTxn.setTransactionRefNo(rs.getString("TRN_REF_NO"));
	        othTxn.setTransactionDate(rs.getString("TRN_DT"));
	        othTxn.setAccountNo(rs.getString("AC_NO"));
	        othTxn.setCurrencyCode(rs.getString("AC_CURR_CODE"));
	        othTxn.setDrCrFlag(rs.getString("DRCR_IND"));
	        othTxn.setFycAmount(rs.getString("FCY_AMOUNT"));
	        othTxn.setExchangeRate(rs.getString("EXCH_RATE"));
	        othTxn.setLycAmount(rs.getString("LCY_AMOUNT"));
	        othTxn.setModule(rs.getString("MODULE"));
	        othTxn.setBranchReference(rs.getString("BRANCH_REFERENCE"));
	        othTxn.setCreatedOn(rs.getString("CREATED_ON"));
	        return othTxn;
	    }
	}
	
	private RowMapper<Object> getRowMapperForMsgTypeKey(String msgTypeKey) {
	    if (msgTypeKey == null) {
	        return null;
	    }
	    
	    switch (msgTypeKey) {
	        case "1":
	        case "2":
	            return new Type950RowMapper();
	        case "3":
	            return new CredenceMirrorRowMapper();
	        case "4":
	            return new FittCsvVoRowMapper();
	        case "5":
	            return new FottCsvVoRowMapper();
	        case "6":
	            return new OtherTxnsRowMapper();
	        default:
	            logger.warn("Invalid msgTypeKey: {}. Returning null.", msgTypeKey);
	            return null;
	    }
	}
	
	@Override
	public DBResponse getReceivedMsgView(String msgKey, String msgTypeKey) {
	    try {
	        // Get the appropriate row mapper based on msgTypeKey
	        RowMapper<Object> rowMapper = getRowMapperForMsgTypeKey(msgTypeKey);
	        
	        if (rowMapper == null) {
	            logger.warn("No valid row mapper found for msgTypeKey: {}.", msgTypeKey);
	            return new DBResponse(0, "Invalid message type key", null);
	        }
	        
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKG_TRANSACTION_ONE_STP_FETCH_RECEIVED_MSG_VIEW);
	        
	        simpleJdbcCall.declareParameters(
			        new SqlParameter("IMSG_KEY", OracleTypes.NUMBER),
			        new SqlOutParameter("O_CURSOR", OracleTypes.REF_CURSOR, rowMapper)
			    );

			    SqlParameterSource paramSource = new MapSqlParameterSource()
			    	.addValue("IMSG_KEY", msgKey);
			    
	        Map<String, Object> out = simpleJdbcCall.execute(paramSource);
	        
	        if (out != null && out.get("O_CURSOR") != null) {
	            @SuppressWarnings("unchecked")
	            List<Object> transactions = (List<Object>) out.get("O_CURSOR");
	            
	            if (!transactions.isEmpty()) {
	                Object data = transactions.get(0);
	                return new DBResponse(1, "Success", data);
	            } else {
	                logger.info("No records found for msgType: {}, msgTypeKey: {}", msgKey, msgTypeKey);
	                return new DBResponse(0, "No records found", null);
	            }
	        } else {
	            return new DBResponse(-1, "No data returned from procedure", null);
	        }
	    } catch (Exception e) {
	        logger.error("Error in getReceivedMsgView for msgType: {}, msgTypeKey: {}", 
	        		msgKey, msgTypeKey, e);
	        return new DBResponse(-1, "Error fetching transaction details: " + e.getMessage(), null);
	    }
	}
	class UploadedMsgDataDetailsRowMapper implements RowMapper<Transaction> {

	    @Override
	    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {

	        Transaction txn = new Transaction();

	        // Existing mappings (kept intact)
	        txn.setSenderCode(rs.getString("SENDER_CODE"));
	        txn.setRowNumber(rs.getString("ROW_NUMBER"));
	        txn.setReasonForRejection(rs.getString("REASON_FOR_REJECTION"));
	        txn.setTranRefNo(rs.getString("TRAN_REF_NO"));
	        txn.setValueDate(rs.getString("VALUE_DATE"));
	        txn.setCurrCode(rs.getString("CURR_CODE"));
	        txn.setMsgAmount(rs.getString("MSG_AMOUNT"));
	        txn.setRecBic(rs.getString("REC_BIC"));
	        txn.setCompleteMsg(rs.getString("COMPLETE_MSG"));
	        txn.setFormatedMsg(rs.getString("FORMATED_MSG"));
	        txn.setMtMxFlag(rs.getString("MT_MX_FLAG"));
	        txn.setUploadKey(rs.getString("UPLOAD_KEY"));
	        txn.setInternalRefNo(rs.getString("INT_REF_NO"));
	        txn.setTranType(rs.getString("TRAN_TYPE"));
	        txn.setBankDetails(rs.getString("BANK_DETAILS"));
	        txn.setField86(rs.getString("FIELD_86_DATA"));
	        txn.setAccountId(rs.getString("ACCOUNT_ID"));
	        txn.setEntryDate(rs.getString("ENTRY_DATE"));
	        txn.setTransTypeIdentificationCode(rs.getString("TRANS_TYPE_IDENTIFI_CODE"));
	        txn.setCreatedOn(rs.getString("CREATED_ON"));

	        return txn;
	    }
	}

	@Override
	public String exportUploadedMsgDataCSV(Transaction transactionObj) throws IOException {
	    SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKG_TRANSACTION_ONE_STP_FETCH_UPL_MSG_VIEW_EXP);

	    simpleJdbcCall.declareParameters(
		        new SqlParameter("IUPLOAD_KEY", OracleTypes.NUMBER),
		        new SqlOutParameter("O_CURSOR", OracleTypes.REF_CURSOR)
		    );

		    SqlParameterSource paramSource = new MapSqlParameterSource()
		    	.addValue("IUPLOAD_KEY", checkEmpty(transactionObj.getUploadKey()));

	    Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	    if (out != null) {
	        @SuppressWarnings("unchecked")
	        ArrayList<Map<String, Object>> resultSet = (ArrayList<Map<String, Object>>) out.get("O_CURSOR");

	        if (resultSet != null && !resultSet.isEmpty()) {
	            String csvContent = exportData.generateCSV(resultSet);
	            String base64Csv = exportData.encodeBase64(csvContent);

	            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	            String filename = "TransactionSearch_" + timestamp + ".csv";

	            return filename + "|" + base64Csv;
	        } else {
	            return "No data found";
	        }
	    } else {
	        return "Error during CSV generation";
	    }
	}

	@Override
	public DBResponse getReceivedMsgList(Transaction transactionObj) {
		try {
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKG_TRANSACTION_ONE_STP_FETCH_RECEIVED_MSG_LIST);

	        simpleJdbcCall
	            .withoutProcedureColumnMetaDataAccess()
	            .declareParameters(
	                new SqlParameter("IPAGE_NO", OracleTypes.NUMBER),
	                new SqlParameter("IPAGE_SIZE", OracleTypes.NUMBER),
	                new SqlParameter("IMSG_TYPE_KEY", OracleTypes.NUMBER),
	                new SqlParameter("INOSTRO_KEY", OracleTypes.NUMBER),
	                new SqlParameter("IFROM_DATE", OracleTypes.VARCHAR),
	                new SqlParameter("ITO_DATE", OracleTypes.VARCHAR),
	                new SqlParameter("IMATCHED_STATUS", OracleTypes.NUMBER),
	                new SqlParameter("ICURR_KEY", OracleTypes.VARCHAR),
	                new SqlParameter("IFROM_AMOUNT", OracleTypes.NUMERIC),
	                new SqlParameter("ITO_AMOUNT", OracleTypes.NUMERIC),
	                new SqlParameter("ITRAN_REF_NO", OracleTypes.VARCHAR),
	                new SqlParameter("ILOGIN_USER_KEY", OracleTypes.NUMBER),
	                new SqlParameter("I_ORDER_BY_COLUMN", OracleTypes.VARCHAR),
	                new SqlParameter("I_ORDER_ASC_DESC", OracleTypes.VARCHAR),
	                new SqlOutParameter("O_CURSOR", OracleTypes.CURSOR, new ReceivedMsgListRowMapper())
	            );

	        SqlParameterSource paramSource = new MapSqlParameterSource()
	        	    .addValue("IPAGE_NO", checkEmpty(transactionObj.getPageNo()))
	        	    .addValue("IPAGE_SIZE", checkEmpty(transactionObj.getPageSize()))
	        	    .addValue("IMSG_TYPE_KEY", checkEmpty(transactionObj.getMsgTypeKey()))
	        	    .addValue("INOSTRO_KEY", checkEmpty(transactionObj.getNostroKey()))
	        	    .addValue("IFROM_DATE", checkEmpty(transactionObj.getFromValueDate()))
	        	    .addValue("ITO_DATE", checkEmpty(transactionObj.getToValueDate()))
	        	    .addValue("IMATCHED_STATUS", checkEmpty(transactionObj.getMatchedStatus()))
	        	    .addValue("ICURR_KEY", checkEmpty(transactionObj.getCurrencyKey()))
	        	    .addValue("IFROM_AMOUNT", checkEmpty(transactionObj.getFromAmount()))
	        	    .addValue("ITO_AMOUNT", checkEmpty(transactionObj.getToAmount()))
	        	    .addValue("ITRAN_REF_NO", checkEmpty(transactionObj.getTranRefNo()))
	        	    .addValue("ILOGIN_USER_KEY", checkEmpty(transactionObj.getLoggedInKey()))
	        	    .addValue("I_ORDER_BY_COLUMN", checkEmpty(transactionObj.getOrderByColumn()))
	        	    .addValue("I_ORDER_ASC_DESC", checkEmpty(transactionObj.getOrderAscDesc()));

	        Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	        if (out != null) {
	            ArrayList<TransactionSearch> results = (ArrayList<TransactionSearch>) out.get("O_CURSOR");
	            if(results!=null && results.size()>0) {
		            transactionObj.setTransactionSearch(results);	            
		            // Set count based on the size of results
		            transactionObj.setCount(results.get(0).getCount());
	            }
	            return new DBResponse(1);
	        } else {
	            return new DBResponse(-1, "No result from query.");
	        }

	    } catch (Exception e) {
	        logger.error("getUploadedMsgDataList(" + transactionObj + ")", e);
	        throw new CommonDataException(HttpStatus.INTERNAL_SERVER_ERROR, new DBResponse(-1, "Error While Fetching Transaction List"));
	    }
	}

	private static final class ReceivedMsgListRowMapper implements RowMapper<TransactionSearch> {
	    
	    @Override
	    public TransactionSearch mapRow(ResultSet rs, int rowNum) throws SQLException {
	        TransactionSearch transactionSearch = new TransactionSearch();
	        
	        try {
	            // Map all columns from the result set to TransactionSearch object
	            transactionSearch.setUploadKey(rs.getString("UPLOAD_KEY"));
	            transactionSearch.setMessageKey(rs.getString("MSG_KEY"));
	            transactionSearch.setMsgTypeKey(rs.getString("MSG_TYPE_KEY"));
	            transactionSearch.setNostroName(rs.getString("NOSTRO_NAME"));
	            transactionSearch.setMsgDesc(rs.getString("MSG_DESC"));
	            transactionSearch.setTranRefNo(rs.getString("TRAN_REF_NO"));
	            transactionSearch.setRelRefNo(rs.getString("REL_REF_NO"));
	            transactionSearch.setCurrKey(rs.getString("CURR_KEY"));
	            transactionSearch.setCurrCode(rs.getString("CURR_CODE"));
	            transactionSearch.setValueDate(rs.getString("VALUE_DATE"));
	            transactionSearch.setMsgAmount(rs.getString("MSG_AMOUNT"));
	            transactionSearch.setSenderCode(rs.getString("SENDER_CODE"));
	            transactionSearch.setReceiverBic(rs.getString("RECEIVER_BIC"));
	            transactionSearch.setCreatedOn(rs.getString("CREATED_ON"));
	            transactionSearch.setMatchedStatusDesc(rs.getString("MATCHED_STATUS_DESC"));
	            transactionSearch.setUtilisedAmount(rs.getString("UTILISED_AMOUNT"));
	            transactionSearch.setCount(rs.getInt("TOTAL_RECORDS"));
	            
	        } catch (SQLException e) {
	            throw new SQLException("Error mapping ReceivedMsgList row", e);
	        }
	        
	        return transactionSearch;
	    }
	}


	@Override
	public String exportReceivedTransactionCSV(Transaction transactionObj) {
		SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKG_TRANSACTION_ONE_STP_FETCH_RECEIVED_MSG_EXP);

	    simpleJdbcCall.declareParameters(
                new SqlParameter("IMSG_TYPE_KEY", OracleTypes.NUMBER),
                new SqlParameter("INOSTRO_KEY", OracleTypes.NUMBER),
                new SqlParameter("IFROM_DATE", OracleTypes.VARCHAR),
                new SqlParameter("ITO_DATE", OracleTypes.VARCHAR),
                new SqlParameter("IMATCHED_STATUS", OracleTypes.NUMBER),
                new SqlParameter("ICURR_KEY", OracleTypes.VARCHAR),
                new SqlParameter("IFROM_AMOUNT", OracleTypes.NUMBER),
                new SqlParameter("ITO_AMOUNT", OracleTypes.NUMBER),
                new SqlParameter("ITRAN_REF_NO", OracleTypes.VARCHAR),
                new SqlParameter("ILOGIN_USER_KEY", OracleTypes.NUMBER),
                new SqlParameter("I_ORDER_BY_COLUMN", OracleTypes.VARCHAR),
                new SqlParameter("I_ORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("IMSG_TYPE_KEY", checkEmpty(transactionObj.getMsgTypeKey()))
	            .addValue("INOSTRO_KEY", checkEmpty(transactionObj.getNostroKey()))
	            .addValue("IFROM_DATE", checkEmpty(transactionObj.getFromValueDate()))
	            .addValue("ITO_DATE", checkEmpty(transactionObj.getToValueDate()))
	            .addValue("IMATCHED_STATUS", checkEmpty(transactionObj.getMatchedStatus()))
	            .addValue("ICURR_KEY", checkEmpty(transactionObj.getCurrencyKey()))
	            .addValue("IFROM_AMOUNT", checkEmpty(transactionObj.getFromAmount()))
	            .addValue("ITO_AMOUNT", checkEmpty(transactionObj.getToAmount()))
	            .addValue("ITRAN_REF_NO", checkEmpty(transactionObj.getTranRefNo()))
	            .addValue("ILOGIN_USER_KEY", checkEmpty(transactionObj.getLoggedInKey()))
	            .addValue("I_ORDER_BY_COLUMN", checkEmpty(transactionObj.getOrderByColumn()))
	            .addValue("I_ORDER_ASC_DESC", checkEmpty(transactionObj.getOrderAscDesc()));

	    Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	    if (out != null) {
	        @SuppressWarnings("unchecked")
	        ArrayList<Map<String, Object>> resultSet = (ArrayList<Map<String, Object>>) out.get("IO_CURSOR");

	        if (resultSet != null && !resultSet.isEmpty()) {
	            String csvContent = exportData.generateCSV(resultSet);
	            String base64Csv = exportData.encodeBase64(csvContent);

	            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	            String filename = "InwardOutwardMessages_" + timestamp + ".csv";

	            return filename + "|" + base64Csv;
	        } else {
	            return "No data found";
	        }
	    } else {
	        return "Error during CSV generation";
	    }
	}

	@Override
	public ExportResponse exportReceivedTransactionExcel(Transaction transactionObj) throws IOException {
		SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKG_TRANSACTION_ONE_STP_FETCH_RECEIVED_MSG_EXP);

	    simpleJdbcCall.declareParameters(
                new SqlParameter("IMSG_TYPE_KEY", OracleTypes.NUMBER),
                new SqlParameter("INOSTRO_KEY", OracleTypes.NUMBER),
                new SqlParameter("IFROM_DATE", OracleTypes.VARCHAR),
                new SqlParameter("ITO_DATE", OracleTypes.VARCHAR),
                new SqlParameter("IMATCHED_STATUS", OracleTypes.NUMBER),
                new SqlParameter("ICURR_KEY", OracleTypes.VARCHAR),
                new SqlParameter("IFROM_AMOUNT", OracleTypes.NUMBER),
                new SqlParameter("ITO_AMOUNT", OracleTypes.NUMBER),
                new SqlParameter("ITRAN_REF_NO", OracleTypes.VARCHAR),
                new SqlParameter("ILOGIN_USER_KEY", OracleTypes.NUMBER),
                new SqlParameter("I_ORDER_BY_COLUMN", OracleTypes.VARCHAR),
                new SqlParameter("I_ORDER_ASC_DESC", OracleTypes.VARCHAR),
	            new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR)
	    );

	    SqlParameterSource paramSource = new MapSqlParameterSource()
	            .addValue("IMSG_TYPE_KEY", checkEmpty(transactionObj.getMsgTypeKey()))
	            .addValue("INOSTRO_KEY", checkEmpty(transactionObj.getNostroKey()))
	            .addValue("IFROM_DATE", checkEmpty(transactionObj.getFromValueDate()))
	            .addValue("ITO_DATE", checkEmpty(transactionObj.getToValueDate()))
	            .addValue("IMATCHED_STATUS", checkEmpty(transactionObj.getMatchedStatus()))
	            .addValue("ICURR_KEY", checkEmpty(transactionObj.getCurrencyKey()))
	            .addValue("IFROM_AMOUNT", checkEmpty(transactionObj.getFromAmount()))
	            .addValue("ITO_AMOUNT", checkEmpty(transactionObj.getToAmount()))
	            .addValue("ITRAN_REF_NO", checkEmpty(transactionObj.getTranRefNo()))
	            .addValue("ILOGIN_USER_KEY", checkEmpty(transactionObj.getLoggedInKey()))
	            .addValue("I_ORDER_BY_COLUMN", checkEmpty(transactionObj.getOrderByColumn()))
	            .addValue("I_ORDER_ASC_DESC", checkEmpty(transactionObj.getOrderAscDesc()));

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

	        String filename = "InwardOutwardMessages_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
	        String base64Data = exportData.generateExcelBase64(jsonData, columnOrder);

	        return new ExportResponse(base64Data, filename);
	    }

	    return new ExportResponse(null, null);
	}

	@Override
	public DBResponse getActivityDetail(Integer msgKey) {
		try {
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKG_TRANSACTION_ONE_STP_FETCH_REC_MSG_ACT_DET);

	        simpleJdbcCall.declareParameters(
	                new SqlParameter("IMSG_KEY", OracleTypes.NUMBER),
	                new SqlOutParameter("O_CURSOR", OracleTypes.CURSOR, new ActivityMsgListRowMapper())
		    );

		    SqlParameterSource paramSource = new MapSqlParameterSource()
		            .addValue("IMSG_KEY", msgKey);

	        Map<String, Object> out = simpleJdbcCall.execute(paramSource);

	        if (out != null && out.get("O_CURSOR") != null) {
	            @SuppressWarnings("unchecked")
	            ArrayList<ActivityMsgVo> results = (ArrayList<ActivityMsgVo>) out.get("O_CURSOR");
	            
	            if (results != null && !results.isEmpty()) {
	                return new DBResponse(1, "Success", results);
	            } else {
	                return new DBResponse(0, "No records found", null);
	            }
	        } else {
	            return new DBResponse(-1, "No result from query", null);
	        }

	    } catch (Exception e) {
	        logger.error("getActivityDetail(msgKey: {})", msgKey, e);
	        return new DBResponse(-1, "Error while fetching activity details: " + e.getMessage(), null);
	    }
	}
	
	public class ActivityMsgListRowMapper implements RowMapper<ActivityMsgVo> {
	    
	    @Override
	    public ActivityMsgVo mapRow(ResultSet rs, int rowNum) throws SQLException {
	        ActivityMsgVo vo = new ActivityMsgVo();
	        
	        vo.setActivityKey(rs.getInt("ACTIVITY_KEY"));
	        vo.setSystemRemark(rs.getString("SYSTEM_REMARK"));
	        vo.setUserRemark(rs.getString("USER_REMARK"));
	        vo.setActivityOn(rs.getString("ACTIVITY_ON"));
	        vo.setActivityTime(rs.getString("ACTIVITY_TIME"));
	        vo.setCreatedOn(rs.getString("CREATED_ON"));
	        vo.setCreatedTime(rs.getString("CREATED_TIME"));
	        vo.setMatchedStatus(rs.getString("MATCHED_STATUS"));
	        vo.setTranRefNo(rs.getString("TRAN_REF_NO"));
	        vo.setMsgAmount(rs.getString("MSG_AMOUNT"));  // Formatted string
	        vo.setActivityDoneBy(rs.getString("ACTIVITY_DONE_BY"));
	        
	        return vo;
	    }
	}
	
	public String checkEmpty(String input) {
		return ("").equalsIgnoreCase(input) ? null : input;
	}
}
