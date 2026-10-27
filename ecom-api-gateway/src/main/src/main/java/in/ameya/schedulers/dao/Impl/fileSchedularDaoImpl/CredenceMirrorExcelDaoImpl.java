package in.ameya.schedulers.dao.Impl.fileSchedularDaoImpl;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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

import in.ameya.schedulers.dao.fileSchedularDao.CredenceMirrorExcelDao;
import in.ameya.schedulers.vo.fileSeries.CredenceMirrorExcelVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.Constants;
import in.ameya.util.DBConstants;
import oracle.jdbc.OracleTypes;

@Repository
@PropertySource(value = "classpath:filePath.properties")
@PropertySource(value = "classpath:query.properties")
public class CredenceMirrorExcelDaoImpl extends JdbcDaoSupport implements CredenceMirrorExcelDao {

	private static final Logger logger = LogManager.getLogger(CredenceMirrorExcelDaoImpl.class);

	@Autowired
	@Qualifier("MainDatabase")
	protected DataSource dataSource;

	@Autowired
	@Qualifier("MainDatabaseTemplate")
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment env;

	private static final DataFormatter FORMATTER = new DataFormatter();

	
	
	@Override
	public Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime,String murNumber, int inOutFlag) {

		Integer uploadKey = null;
		try {
			SimpleJdbcCall call = getMainDBProcedureCall(DBConstants.STP_UPL_CREATE_JOB);

			call.declareParameters(new SqlParameter("IMSG_TYPE", Types.VARCHAR),
					new SqlParameter("IFILE_NAME", Types.VARCHAR), new SqlParameter("ISOURCE_SYSTEM", Types.INTEGER),
					new SqlParameter("IFILE_PROCESS_DATE_TIME", Types.VARCHAR),
					new SqlParameter("IMUR_NUMBER", Types.VARCHAR), new SqlParameter("IIN_OUT_FLAG", Types.INTEGER),
					new SqlOutParameter("O_UPLOAD_KEY", Types.INTEGER));

			call.withoutProcedureColumnMetaDataAccess();

			Map<String, Object> out = call.execute(new MapSqlParameterSource().addValue("IMSG_TYPE", msgType)
					.addValue("IFILE_NAME", strFileName).addValue("ISOURCE_SYSTEM", sourceSystem)
					.addValue("IFILE_PROCESS_DATE_TIME", fileModDateTime).addValue("IMUR_NUMBER", murNumber)
					.addValue("IIN_OUT_FLAG", inOutFlag));

			if (out != null && out.containsKey("O_UPLOAD_KEY")) {
				uploadKey = ((Number) out.get("O_UPLOAD_KEY")).intValue();
			}

		} catch (Exception e) {
			logger.error("Error creating upload job", e);
			return -1;
		}
		return uploadKey;
	}

	@Override
	public List<CredenceMirrorExcelVo> readExcel(File file) throws Exception {

		List<CredenceMirrorExcelVo> list = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(file); Workbook workbook = WorkbookFactory.create(fis)) {

			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.iterator();

			rows.next(); // skip header row

			while (rows.hasNext()) {
				Row r = rows.next();

				CredenceMirrorExcelVo vo = new CredenceMirrorExcelVo(
						cell(r, 0), 
						cell(r, 1), 
						cell(r, 2), 
						cell(r, 3), 
						cell(r, 4),
						cell(r, 5), 
						cell(r, 6), 
						cell(r, 7), 
						cell(r, 8), 
						cell(r, 9), 
						cell(r, 10), 
						readAsPlainString(r.getCell(11)),
						cell(r, 12), 
						cell(r, 13), 
						cell(r, 14), 
						cell(r, 15), 
						cell(r,16),
						cell(r, 17), 
						cell(r, 18),
						cell(r, 19), 
						cell(r, 20), 
//						readAsPlainString(r.getCell(21)), 
						cell(r, 21), 
						cell(r, 22), 
						cell(r, 23), 
						cell(r, 24), 
						cell(r, 25),
						cell(r, 26), 
						cell(r, 27), 
						cell(r, 28), 
//						readAsPlainString(r.getCell(29)), 
						cell(r, 29), 

						cell(r, 30), 
						cell(r, 31), 
						cell(r, 32),
						cell(r, 33), 
						cell(r, 34), 
						cell(r, 35), 
						cell(r, 36), 
						cell(r, 37), 
						cell(r, 38), 
						cell(r, 39),
						cell(r, 40), 
						cell(r, 41), 
						cell(r, 42), 
//						readAsPlainString(r.getCell(43)));
						cell(r, 43));

				list.add(vo);
			}
			logger.info("Credence Mirror Excel read successfully. Records = {}", list.size());
		}
		return list;
	}

	private void processBatch(List<CredenceMirrorExcelVo> batch, String fileName, int rowNumCounter, Integer uploadKey) {

		String query = env.getProperty("credence.mirror.insert.query");

		try (Connection conn = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = conn.prepareStatement(query)) {

			conn.setAutoCommit(false);
			int index = 0;

			for (CredenceMirrorExcelVo t : batch) {

				ps.setLong(1, uploadKey);
				ps.setLong(2, rowNumCounter + index);
				ps.setString(3, null);
				ps.setString(4, null);

				ps.setString(5, trim(t.getSrNo()));
				ps.setString(6, trim(t.getSettlementRefNo()));
				ps.setString(7, normalizeDate(t.getPostingDate()));
				ps.setString(8, normalizeDate(t.getValueDate()));
				ps.setString(9, trim(t.getCurrencyCode()));
				ps.setString(10, null);
				ps.setString(11, trim(t.getNostroBalance()));
				ps.setString(12, trim(t.getNostroDrCr()));
				ps.setString(13, trim(t.getLcyBalance()));				
				ps.setString(14, trim(t.getLcyBalanceDrCr()));
				ps.setString(15, trim(t.getTradeType()));
				ps.setString(16, trim(t.getBankName()));
				ps.setString(17, trim(t.getAccountNo()));
				ps.setString(18, trim(t.getProduct()));
				ps.setString(19, trim(t.getDealerTicketNo()));
				ps.setString(20, trim(t.getDealer()));
				ps.setString(21, trim(t.getCounterParty()));
				ps.setString(22, normalizeDate(t.getNostroRefDate()));
				ps.setString(23, trim(t.getNostroRefNo()));
				ps.setString(24, trim(t.getDealType()));
				ps.setString(25, trim(t.getBranchName()));
				ps.setString(26, trim(t.getTransactionType()));
				ps.setString(27, trim(t.getSettlementType()));
				ps.setString(28, normalizeDate(t.getTradeDate()));
				ps.setString(29, trim(t.getQuantity()));
				ps.setString(30, trim(t.getRate()));
				ps.setString(31, trim(t.getAmount()));
				ps.setString(32, trim(t.getPaymentMode()));
				ps.setString(33, trim(t.getCountry()));
				ps.setString(34, trim(t.getDealOwner()));
				ps.setString(35, trim(t.getBranchRefNo()));
				ps.setString(36, trim(t.getInvestment()));
				ps.setString(37, trim(t.getDealSourceRefNo()));
				ps.setString(38, trim(t.getSettlementFlag()));
				ps.setString(39, trim(t.getRefNo()));
				ps.setString(40, trim(t.getLegNo()));
				ps.setString(41, trim(t.getBankCode()));
				ps.setString(42, trim(t.getReversalStatus()));
				ps.setString(43, trim(t.getLcyRate()));
				ps.setString(44, trim(t.getUsdRate()));
				ps.setString(45, trim(t.getUsdBalance()));
				ps.setString(46, trim(t.getUsdBalDrCr()));
				ps.setString(47, trim(t.getRemarks()));
				ps.setString(48, normalizeDate(t.getNostroRefDate1()));
				ps.setString(49, trim(t.getNostroRefNo1()));

				ps.executeUpdate();
				index++;
			}

			conn.commit();
		} catch (Exception e) {
			logger.error("Error inserting Credence Mirror Excel batch", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public String saveTransactionsInBatches(List<CredenceMirrorExcelVo> list, String fileName, Integer uploadKey) {

		int processed = 0;
		int rowNum = 1;

		while (processed < list.size()) {
			int end = Math.min(processed + Constants.CREDENCE_MIRROR_BATCH_SIZE, list.size());
			processBatch(list.subList(processed, end), fileName, rowNum, uploadKey);
			processed = end;
			rowNum += Constants.CREDENCE_MIRROR_BATCH_SIZE;
		}
		return null;
	}

	@Override
    public void validateUploadFile(Integer uploadKey) {
        try {
        	SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_UPL_PROCESS_MIRROR_DATA);
            simpleJdbcCall.declareParameters(
                    new SqlParameter("I_UPLOAD_KEY", OracleTypes.NUMBER)
            );

            MapSqlParameterSource paramSource = new MapSqlParameterSource()
                    .addValue("I_UPLOAD_KEY", uploadKey);

            simpleJdbcCall.execute(paramSource);

            logger.info("validateUploadFile() : Credence Mirror Excel File validation completed Successfully with Upload Key: " + uploadKey);
        } catch (Exception e) {
        	logger.error("validateUploadFile() : Exception while Validating Credence Mirror Excel File File: ", e);
    }
}
	//Helper Method
	private String cell(Row row, int index) {

	    Cell cell = row.getCell(index, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
	    if (cell == null) return null;

	    switch (cell.getCellType()) {

	        case STRING:return cell.getStringCellValue().trim();
	        case NUMERIC:
	            if (DateUtil.isCellDateFormatted(cell)) {
	                return cell.getLocalDateTimeCellValue().toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	            }
	            DataFormatter formatter = new DataFormatter();
	            String value = formatter.formatCellValue(cell).trim().replaceAll(",","").replaceAll("- 0", "-0");
	            
	            Double e1Val = cell.getNumericCellValue();
				BigDecimal bd = new BigDecimal(e1Val.toString());
				//columnValue = NumberToTextConverter.toText(e1Val);//bd.toString();
				//bd = bd.setScale(4, BigDecimal.ROUND_CEILING);
				if(e1Val.toString().contains("E"))
				{
					value = bd.toString();
					long val = bd.longValue();
					value = String.valueOf(val);
				}
	            
	            return value;

	        case BOOLEAN:return String.valueOf(cell.getBooleanCellValue());
	        case FORMULA: return cell.getCellFormula();

	        default: return null;
	    }
	}


	private String trim(String s) {
		return (s == null || s.isEmpty()) ? null : s.trim();
	}

	private String normalizeDate(String date) {
	    if (date == null || date.isBlank()) return null;
	    return date;
	}
	
	private String readAsPlainString(Cell cell) {
	    if (cell == null) return null;

	    switch (cell.getCellType()) {
	        case STRING: return cell.getStringCellValue().trim();
	        case NUMERIC: return new java.math.BigDecimal(cell.getNumericCellValue()).toPlainString();
	        case FORMULA: return cell.getCellFormula();
	        default: return null;
	    }
	}


}
