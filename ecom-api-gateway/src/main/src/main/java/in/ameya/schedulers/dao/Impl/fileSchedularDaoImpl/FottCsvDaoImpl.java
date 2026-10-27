package in.ameya.schedulers.dao.Impl.fileSchedularDaoImpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import in.ameya.schedulers.dao.fileSchedularDao.FottCsvDao;
import in.ameya.schedulers.vo.fileSeries.FittCsvVo;
import in.ameya.schedulers.vo.fileSeries.FottCsvVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.Constants;
import in.ameya.util.DBConstants;
import oracle.jdbc.OracleTypes;

@Repository
@PropertySource(value = "classpath:filePath.properties")
@PropertySource(value = "classpath:query.properties")
public class FottCsvDaoImpl extends JdbcDaoSupport implements FottCsvDao {

	@Autowired
	@Qualifier("MainDatabase")
	protected DataSource dataSource;

	@Autowired
	@Qualifier("MainDatabaseTemplate")
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;

	private static final Logger logger = LogManager.getLogger(FottCsvDao.class);

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
			logger.error(e.getMessage(), e, "FottCsvDaoImpl-createJob", "");
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return uploadKey;
	}

	@Override
    public List<FottCsvVo> readFile(File file) throws Exception {
        List<FottCsvVo> transactionList = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(file.getAbsolutePath()))) {
            List<String> csvLines = lines.collect(Collectors.toList());
            for (int i = 1; i < csvLines.size(); i++) {
             //   String[] record = csvLines.get(i).split(",");
            	String line = csvLines.get(i).replace("\"", "");
            	String[] record = line.split(",");
                if (record.length == 0) continue;
                FottCsvVo transaction = new FottCsvVo(
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
                	    record[18]
                	);
                transactionList.add(transaction);
            }
            logger.info("FOTT CSV read successfully. Records = " + transactionList.size());
        } catch (Exception e) {
        	logger.error("Error reading FOTT CSV file" + file.getName(), e);
            throw e;
        }
        return transactionList;
    }

	private void processBatch(List<FottCsvVo> batchTransactions, String fileName, int rowNumCounter,
			Integer uploadKey) {

		String insertQuery = env.getProperty("fott.insert.query");

		try (Connection conn = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement(insertQuery)) {

			int index = 0;
			conn.setAutoCommit(false);
			for (FottCsvVo transaction : batchTransactions) {
				
				ps.setLong(1, uploadKey);
				ps.setLong(2, rowNumCounter + index);
				ps.setString(3, null);
				ps.setString(4, null);

				ps.setString(5, trimOrNull(transaction.getProductCode()));
				ps.setString(6, trimOrNull(transaction.getSenderBic()));
				ps.setString(7, trimOrNull(transaction.getReceiverBic()));
				ps.setString(8, trimOrNull(transaction.getBookDate()));				                
				ps.setString(9, trimOrNull(transaction.getSenderReference()));
				ps.setString(10, trimOrNull(transaction.getTransactionTypeCode()));
				ps.setString(11, trimOrNull(transaction.getValueDate()));
				ps.setString(12,trimOrNull(transaction.getCurrencyCode()));
				ps.setString(13, null);
				ps.setString(14, trimOrNull(transaction.getTransactionAmount()));
				ps.setString(15, trimOrNull(transaction.getInstructedCurrencyCode()));
				ps.setString(16, null);
				ps.setString(17, trimOrNull(transaction.getInstructedAmount()));
				ps.setString(18,trimOrNull(transaction.getExchangeRate()));				
				ps.setString(19, trimOrNull(transaction.getOrderingCustomer2()));
				ps.setString(20, trimOrNull(transaction.getOrderingInstitution()));
				ps.setString(21, trimOrNull(transaction.getOrderingInstitution2()));
				ps.setString(22, trimOrNull(transaction.getBranchReference()));
				ps.setString(23, trimOrNull(transaction.getReceiver1()));
				ps.setString(24, trimOrNull(transaction.getAccountWithInstitution()));
				ps.setString(25, transaction.getBeneficiaryCustomer());
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
    public String saveTransactionsInBatches(List<FottCsvVo> transactions, String fileName, Integer uploadKey) {
        try {
            int totalRecords = transactions.size();
            int processedRecords = 0;
            int rowNumCounter = 1;
            while (processedRecords < totalRecords) {
                int endIndex = Math.min(processedRecords + Constants.FOTT_BATCH_SIZE, totalRecords);
                List<FottCsvVo> batchTransactions = transactions.subList(processedRecords, endIndex);
                processBatch(batchTransactions, fileName, rowNumCounter, uploadKey);
                processedRecords = endIndex;
                rowNumCounter += Constants.FOTT_BATCH_SIZE;
                logger.info("Processed batch: {" +processedRecords+"} of {"+totalRecords+"} records");
            }
            logger.info("saveTransactionsInBatches() : FOTT Data Inserted Successfully");
            return null;
        } catch (Exception e) {
            logger.error("saveTransactionsInBatches() : Exception", e);
            return e.getMessage();
        }
    }

	@Override
	public void validateUploadFile(Integer uploadKey) {
		try {
			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_UPL_PROCESS_FOTT_OUT_DATA);
			simpleJdbcCall.declareParameters(new SqlParameter("I_UPLOAD_KEY", OracleTypes.NUMBER));

			MapSqlParameterSource paramSource = new MapSqlParameterSource().addValue("I_UPLOAD_KEY", uploadKey);

			simpleJdbcCall.execute(paramSource);

			logger.info(
					"validateUploadFile() : FOTT File validation completed Successfully with Upload Key: " + uploadKey);
		} catch (Exception e) {
			logger.error("validateUploadFile() : Exception while Validating FITT File: ", e);
		}
	}
	
	
	//helper Method
	private String trimOrNull(String value) {
        return value != null ? value.trim() : null;
    }
    
}