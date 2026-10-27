package in.ameya.schedulers.dao.Impl.fileSchedularDaoImpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import in.ameya.schedulers.dao.fileSchedularDao.CredenceMirrorDao;
import in.ameya.schedulers.vo.fileSeries.CredenceMirrorVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.Constants;
import in.ameya.util.DBConstants;
import oracle.jdbc.OracleTypes;

@Repository
@PropertySource(value = "classpath:filePath.properties")
@PropertySource(value = "classpath:query.properties")
public class CredenceMirrorDaoImpl extends JdbcDaoSupport implements CredenceMirrorDao {

	@Autowired
	@Qualifier("MainDatabase")
	protected DataSource dataSource;

	@Autowired
	@Qualifier("MainDatabaseTemplate")
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;

	private static final Logger logger = LogManager.getLogger(CredenceMirrorDaoImpl.class);

	@Override
    public Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime,
			String murNumber, int inOutFlag) {
		Integer uploadKey = null;
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_UPL_CREATE_JOB);
			simpleJdbcCall.declareParameters(new SqlParameter("IMSG_TYPE", Types.VARCHAR),
					new SqlParameter("IFILE_NAME", Types.VARCHAR), new SqlParameter("ISOURCE_SYSTEM", Types.INTEGER),
					new SqlParameter("IFILE_PROCESS_DATE_TIME", Types.VARCHAR),
					new SqlParameter("IMUR_NUMBER", Types.VARCHAR), new SqlParameter("IIN_OUT_FLAG", Types.INTEGER),
					new SqlOutParameter("O_UPLOAD_KEY", Types.INTEGER));

			MapSqlParameterSource params = new MapSqlParameterSource().addValue("IMSG_TYPE", msgType)
					.addValue("IFILE_NAME", strFileName).addValue("ISOURCE_SYSTEM", sourceSystem)
					.addValue("IFILE_PROCESS_DATE_TIME", fileModDateTime).addValue("IMUR_NUMBER", murNumber)
					.addValue("IIN_OUT_FLAG", inOutFlag);

			// turn off processing parameter metadata information
			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();

			Map<String, Object> results = simpleJdbcCall.execute(params);

			if (results != null && results.containsKey("O_UPLOAD_KEY")) {
				uploadKey = ((Number) results.get("O_UPLOAD_KEY")).intValue();
			}
		} catch (Exception e) {
			uploadKey = -1;
			e.printStackTrace();
			logger.error(e.getMessage(), e, "AmeyaSwiftMsgsDaoImpl-createJob", "");
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return uploadKey;
	}

	@Override
    public List<CredenceMirrorVo> readFile(File file) throws Exception {
        List<CredenceMirrorVo> transactionList = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(file.getAbsolutePath()))) {
            List<String> csvLines = lines.collect(Collectors.toList());
            for (int i = 1; i < csvLines.size(); i++) {
             //   String[] record = csvLines.get(i).split(",");
            	String line = csvLines.get(i).replace("\"", "");
            	String[] record = line.split(",");
                if (record.length == 0) continue;
                CredenceMirrorVo transaction = new CredenceMirrorVo(
                		record[0], 
						record[1], 
						record[2], 
						record[3], 
						record[4],
						record[5], 
						record[6], 
						record[7], 
						record[8], 
						record[9], 
						record[10], 
						record[11], 
						record[12],
						record[13], 
						record[14], 
						record[15], 
						record[16], 
						record[17], 
						record[18], 
						record[19], 
						record[20],
						record[21], 
						record[22], 
						record[23], 
						record[24], 
						record[25], 
						record[26], 
						record[27], 
						record[28],
						record[29],
						record[30], 
						record[31], 
						record[32], 
						record[33], 
						record[34], 
						record[35], 
						record[36],
						record[37], 
						record[38], 
						record[39], 
						record[40], 
						record[41], 
						record[42], 
						record[43]
                	);
                transactionList.add(transaction);
            }
            logger.info("Credence Mirror CSV read successfully. Records = " + transactionList.size());
        } catch (Exception e) {
        	logger.error("Error reading Credence Mirror CSV file" + file.getName(), e);
            throw e;
        }
        return transactionList;
    }

	private void processBatch(List<CredenceMirrorVo> batchTransactions, String fileName, int rowNumCounter,
			Integer uploadKey) {

		String insertQuery = env.getProperty("credence.mirror.insert.query");

		try (Connection conn = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement(insertQuery)) {

			int index = 0;
			conn.setAutoCommit(false);
			for (CredenceMirrorVo transaction : batchTransactions) {

				ps.setLong(1, uploadKey); // UPLOAD_KEY
				ps.setLong(2, rowNumCounter + index); // ROW_NUMBER
				ps.setString(3, null); // REASON_FOR_REJECTION
				ps.setLong(4, 16); // NOSTRO_KEY

				ps.setString(5, trimOrNull(transaction.getSrNo()));
				ps.setString(6, trimOrNull(transaction.getSettlementRefNo()));
				ps.setString(7, normalizeDate(transaction.getPostingDate()));
				ps.setString(8, normalizeDate(transaction.getValueDate()));
				ps.setString(9, trimOrNull(transaction.getCurrencyCode()));
				ps.setString(10, null);
				ps.setString(11, trimOrNull(transaction.getNostroBalance()));
				ps.setString(12, trimOrNull(transaction.getNostroDrCr()));
				ps.setString(13, trimOrNull(transaction.getLcyBalance()));
				ps.setString(14, trimOrNull(transaction.getLcyBalanceDrCr()));
				ps.setString(15, trimOrNull(transaction.getTradeType()));
				ps.setString(16, trimOrNull(transaction.getBankName()));
				ps.setString(17, trimOrNull(transaction.getAccountNo()));
				ps.setString(18, trimOrNull(transaction.getProduct()));
				ps.setString(19, trimOrNull(transaction.getDealerTicketNo()));
				ps.setString(20, trimOrNull(transaction.getDealer()));
				ps.setString(21, trimOrNull(transaction.getCounterParty()));
				ps.setString(22, normalizeDate(transaction.getNostroRefDate()));
				ps.setString(23, trimOrNull(transaction.getNostroRefNo()));
				ps.setString(24, trimOrNull(transaction.getDealType()));
				ps.setString(25, trimOrNull(transaction.getBranchName()));
				ps.setString(26, trimOrNull(transaction.getTransactionType()));
				ps.setString(27, trimOrNull(transaction.getSettlementType()));
				ps.setString(28, normalizeDate(transaction.getTradeDate()));
				ps.setString(29, trimOrNull(transaction.getQuantity()));
				ps.setString(30, trimOrNull(transaction.getRate()));
				ps.setString(31, trimOrNull(transaction.getAmount()));
				ps.setString(32, trimOrNull(transaction.getPaymentMode()));
				ps.setString(33, trimOrNull(transaction.getCountry()));
				ps.setString(34, trimOrNull(transaction.getDealOwner()));
				ps.setString(35, trimOrNull(transaction.getBranchRefNo()));
				ps.setString(36, trimOrNull(transaction.getInvestment()));
				ps.setString(37, trimOrNull(transaction.getDealSourceRefNo()));
				ps.setString(38, trimOrNull(transaction.getSettlementFlag()));
				ps.setString(39, trimOrNull(transaction.getRefNo()));
				ps.setString(40, trimOrNull(transaction.getLegNo()));
				ps.setString(41, trimOrNull(transaction.getBankCode()));
				ps.setString(42, trimOrNull(transaction.getReversalStatus()));
				ps.setString(43, trimOrNull(transaction.getLcyRate()));
				ps.setString(44, trimOrNull(transaction.getUsdRate()));
				ps.setString(45, trimOrNull(transaction.getUsdBalance()));
				ps.setString(46, trimOrNull(transaction.getUsdBalDrCr()));
				ps.setString(47, trimOrNull(transaction.getRemarks()));
				ps.setString(48, normalizeDate(transaction.getNostroRefDate1()));
				ps.setString(49, trimOrNull(transaction.getNostroRefNo1()));
				ps.executeUpdate();
				index++;
			}
			conn.commit();
			// logger.info("Inserted {} records successfully", batchTransactions.size());
		} catch (Exception e) {
			logger.error("processBatch() : Exception while inserting records", e);
			throw new RuntimeException(e); // ❗ do NOT swallow
		}
	}

	@Override
	public String saveTransactionsInBatches(List<CredenceMirrorVo> transactions, String fileName, Integer uploadKey) {
		try {
			int totalRecords = transactions.size();
			int processedRecords = 0;
			int rowNumCounter = 1;
			while (processedRecords < totalRecords) {
				int endIndex = Math.min(processedRecords + Constants.CREDENCE_MIRROR_BATCH_SIZE, totalRecords);
				List<CredenceMirrorVo> batchTransactions = transactions.subList(processedRecords, endIndex);
				processBatch(batchTransactions, fileName, rowNumCounter, uploadKey);
				processedRecords = endIndex;
				rowNumCounter += Constants.CREDENCE_MIRROR_BATCH_SIZE;
				logger.info("Processed batch: {" + processedRecords + "} of {" + totalRecords + "} records");
			}
			logger.info("saveTransactionsInBatches() : Credence Mirror Data Inserted Successfully");
			return null;
		} catch (Exception e) {
			logger.error("saveTransactionsInBatches() : Exception", e);
			return e.getMessage();
		}
	}

	@Override
	public void validateUploadFile(Integer uploadKey) {
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_UPL_PROCESS_MIRROR_DATA);
			simpleJdbcCall.declareParameters(new SqlParameter("I_UPLOAD_KEY", OracleTypes.NUMBER)
			);

			MapSqlParameterSource paramSource = new MapSqlParameterSource().addValue("I_UPLOAD_KEY", uploadKey);
			simpleJdbcCall.execute(paramSource);

			logger.info(
					"validateUploadFile() : Credence Mirror File validation completed Successfully with Upload Key: " + uploadKey);
		} catch (Exception e) {
			logger.error("validateUploadFile() : Exception while Validating Credence Mirror File: ", e);
		}
	}

	private String trimOrNull(String value) {
		return value != null ? value.trim() : null;
	}
	
	private String normalizeDate(String date) {
	    if (date == null || date.trim().isEmpty()) return null;
	    return LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
	                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

}
