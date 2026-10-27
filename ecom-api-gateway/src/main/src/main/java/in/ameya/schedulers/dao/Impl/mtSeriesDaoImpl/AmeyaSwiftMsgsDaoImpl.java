package in.ameya.schedulers.dao.Impl.mtSeriesDaoImpl;

import java.sql.Types;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import in.ameya.schedulers.dao.mtSeriesDao.AmeyaSwiftMsgsDao;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.DBConstants;
import in.ameya.util.LoggerUtil;


@PropertySource(value="classpath:messages.properties")
@PropertySource(value="classpath:application.properties")
@Repository
public class AmeyaSwiftMsgsDaoImpl extends JdbcDaoSupport implements AmeyaSwiftMsgsDao{
	
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private Environment env;
	
	
	@Override
	public int createJob(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag)
	{
	    int intRetVal = -1;

	    try
	    {
	        SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_UPL_CREATE_JOB);
	        simpleJdbcCall.declareParameters(
	                new SqlParameter("IMSG_TYPE", Types.VARCHAR),
	                new SqlParameter("IFILE_NAME", Types.VARCHAR),
	                new SqlParameter("ISOURCE_SYSTEM", Types.INTEGER),
	                new SqlParameter("IFILE_PROCESS_DATE_TIME", Types.VARCHAR),
	                new SqlParameter("IMUR_NUMBER", Types.VARCHAR),
	                new SqlParameter("IIN_OUT_FLAG", Types.INTEGER),
	                new SqlOutParameter("O_UPLOAD_KEY", Types.INTEGER));

	        MapSqlParameterSource params = new MapSqlParameterSource()
	                .addValue("IMSG_TYPE", msgType)
	                .addValue("IFILE_NAME", strFileName)
	                .addValue("ISOURCE_SYSTEM", sourceSystem)
	                .addValue("IFILE_PROCESS_DATE_TIME", fileModDateTime)
	                .addValue("IMUR_NUMBER", murNumber)
	                .addValue("IIN_OUT_FLAG", inOutFlag);

	        // turn off processing parameter metadata information
	        simpleJdbcCall.withoutProcedureColumnMetaDataAccess();

	        Map<String, Object> results = simpleJdbcCall.execute(params);

	        if (results != null && results.containsKey("O_UPLOAD_KEY"))
	        {
	            intRetVal = ((Number) results.get("O_UPLOAD_KEY")).intValue();
	        }
	    }
	    catch(Exception e)
	    {
	        intRetVal = -1;
	        e.printStackTrace();
	        loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaSwiftMsgsDaoImpl-createJob", "");
	        throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
	    }
	    return intRetVal;
	}

//	@Override
//	public int createJobUnknownMsg(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, String fileData) 
//	{
//		int intRetVal = -1;
//		
//		try
//		{
//			String strInsertStatementArr[] = fileData.split("@_@");
//			
//			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_UPL_CREATE_JOB_UNKNOWN_MSG);
//			simpleJdbcCall.declareParameters(
//					new SqlParameter("@SOURCE_SYSTEM", Types.TINYINT),
//					new SqlParameter("@IFILE_NAME", Types.VARCHAR),					
//					new SqlParameter("@FILE_PROCESS_DATE_TIME", Types.TIMESTAMP),
//					new SqlParameter("@IMUR_NUMBER", Types.VARCHAR),
//					new SqlParameter("@IMSG_TYPE", Types.VARCHAR),
//					new SqlParameter("@IIN_OUT_FLAG", Types.TINYINT),
//					new SqlParameter("@IACK_NACK_FLAG", Types.TINYINT),
//					new SqlParameter("@ISENDER_CODE", Types.VARCHAR),
//					new SqlParameter("@IREC_BIC", Types.VARCHAR),
//					new SqlParameter("@ICOMPLETE_MSG", Types.VARCHAR));
//	
//			MapSqlParameterSource params = new MapSqlParameterSource()
//					.addValue("@IMSG_TYPE", msgType)
//					.addValue("@IFILE_NAME", strFileName)
//					.addValue("@SOURCE_SYSTEM", sourceSystem)
//					.addValue("@FILE_PROCESS_DATE_TIME", fileModDateTime)
//					.addValue("@IMUR_NUMBER", murNumber)
//					.addValue("@IMSG_TYPE", strInsertStatementArr[0])
//					.addValue("@IIN_OUT_FLAG", strInsertStatementArr[1])
//					.addValue("@IACK_NACK_FLAG", strInsertStatementArr[2])
//					.addValue("@ISENDER_CODE", strInsertStatementArr[3])
//					.addValue("@IREC_BIC", strInsertStatementArr[4])
//					.addValue("@ICOMPLETE_MSG", strInsertStatementArr[5]);
//			
//			simpleJdbcCall.returningResultSet("createJob", new RowMapper<CreateJobVo>() {
//				
//				@Override
//				public CreateJobVo mapRow(ResultSet rs, int rowNum) throws SQLException {
//					CreateJobVo createJobVo = new CreateJobVo();
//					int intRetVal = rs.getInt(1);	
//					createJobVo.setIntRetVal(intRetVal);
//					return createJobVo;
//				}
//			});
//			
//			// turn off processing parameter metadata information 
//			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
//			
//			Map<String, Object> results = simpleJdbcCall.execute(params);
//			
//			if (results != null) 
//			{	
//				@SuppressWarnings("unchecked")
//				List<CreateJobVo> createJobList = (List<CreateJobVo>) results.get("createJob");
//				if(createJobList != null && createJobList.size() > 0)
//				{
//					CreateJobVo createJobVo = createJobList.get(0);
//					intRetVal = createJobVo.getIntRetVal();
//				}
//			}
//		}
//		catch(Exception e)
//		{
//			intRetVal = -1;
//			e.printStackTrace();
//			loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaSwiftMsgsDaoImpl-createJobUnknownMsg", "");
//			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
//		}
//		return intRetVal;
//	}
	
	
	@Override
	public int executeInsertStatement(String insertStatement) 
	{
		int intRetVal = -1;
		
		try
		{
			this.jdbcTemplateMain.execute(insertStatement);
			intRetVal = 1;
		}
		catch(Exception e)
		{
			intRetVal = -1;
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaSwiftMsgsDaoImpl-executeInsertStatement", "");
			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
		}
		return intRetVal;
	}
		
	
//	@Override
//	public int startAutomatchingMatchingProcess() 
//	{
//		int intRetVal = 1;
//		
//		try
//		{
//			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_UPL_AUTO_MATCHING);
//			simpleJdbcCall.declareParameters(
//					new SqlParameter("@IUSER_ID", Types.INTEGER));
//	
//			MapSqlParameterSource params = new MapSqlParameterSource()
//					.addValue("@IUSER_ID", "1");
//			
//			simpleJdbcCall.returningResultSet("autoMatching", new RowMapper<CreateJobVo>() {
//				
//				@Override
//				public CreateJobVo mapRow(ResultSet rs, int rowNum) throws SQLException {
//					CreateJobVo createJobVo = new CreateJobVo();
//					int intRetVal = rs.getInt(1);	
//					createJobVo.setIntRetVal(intRetVal);
//					return createJobVo;
//				}
//			});
//			
//			// turn off processing parameter metadata information 
//			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
//			
//			Map<String, Object> results = simpleJdbcCall.execute(params);
//			
//			if (results != null) 
//			{	
//				@SuppressWarnings("unchecked")
//				List<CreateJobVo> createJobList = (List<CreateJobVo>) results.get("autoMatching");
//				if(createJobList != null && createJobList.size() > 0)
//				{
//					CreateJobVo createJobVo = createJobList.get(0);
//					intRetVal = createJobVo.getIntRetVal();
//				}
//			}
//		}
//		catch(Exception e)
//		{
//			intRetVal = -1;
//			e.printStackTrace();
//			loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaSwiftMsgsDaoImpl-startAutomatchingMatchingProcess", "");
//			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
//		}
//		return intRetVal;
//	}
//
//	
//	@Override
//	public int startMsgReUnReconciledSMSProcess() 
//	{
//		int intRetVal = 1;
//		
//		try
//		{
//			
//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
//			LocalDateTime fromDate = LocalDateTime.now().minusMinutes(60);
//			LocalDateTime toDate = LocalDateTime.now();
//			
//			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ALL_OUT_MSG_DATA_COUNT);
//			simpleJdbcCall.declareParameters(
//					new SqlParameter("@I_FROM_DATE", Types.DATE),
//					new SqlParameter("@I_TO_DATE", Types.DATE),
//					new SqlParameter("@BRANCH_CODE", Types.VARCHAR),
//					new SqlParameter("@IN_OUT_FLAG", Types.TINYINT),
//					new SqlParameter("@I_IS_TREASURY_USER", Types.CHAR),
//					new SqlParameter("@I_LIST_EXPORT", Types.VARCHAR));
//	
//			MapSqlParameterSource params = new MapSqlParameterSource()
//					.addValue("@I_FROM_DATE", dtf.format(fromDate))
//					.addValue("@I_TO_DATE", dtf.format(toDate))
//					.addValue("@BRANCH_CODE", env.getProperty("branch.code.treasury.branch"))
//					.addValue("@IN_OUT_FLAG", Constants.OUTWARD_FLAG)
//					.addValue("@I_IS_TREASURY_USER", "T")
//					.addValue("@I_LIST_EXPORT", "M");
//			
//			simpleJdbcCall.returningResultSet("startMsgReUnReconciledSMSProcess", new RowMapper<CreateJobVo>() {
//				
//				@Override
//				public CreateJobVo mapRow(ResultSet rs, int rowNum) throws SQLException {
//					CreateJobVo createJobVo = new CreateJobVo();
//					int intRetVal = rs.getInt(1);	
//					createJobVo.setIntRetVal(intRetVal);
//					return createJobVo;
//				}
//			});
//			
//			// turn off processing parameter metadata information 
//			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
//			
//			Map<String, Object> results = simpleJdbcCall.execute(params);
//			
//			if (results != null) 
//			{	
//				@SuppressWarnings("unchecked")
//				List<CreateJobVo> createJobList = (List<CreateJobVo>) results.get("startMsgReUnReconciledSMSProcess");
//				if(createJobList != null && createJobList.size() > 0)
//				{
//					CreateJobVo createJobVo = createJobList.get(0);
//					intRetVal = createJobVo.getIntRetVal();
//				}
//			}
//		}
//		catch(Exception e)
//		{
//			intRetVal = -1;
//			e.printStackTrace();
//			loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaSwiftMsgsDaoImpl-startMsgReUnReconciledSMSProcess", "");
//			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
//		}
//		return intRetVal;
//	}
//	
//	@Override
//	public int startMsgReUnReconciledEMAILProcess() 
//	{
//		int intRetVal = 1;
//		
//		try
//		{
//			SimpleJdbcCall simpleJdbcCallRe = getMainDBProcedureCall(DBConstants.STP_EMAIL_GENERATE_RECONCILED_DATA);
//						
//			simpleJdbcCallRe.returningResultSet("startMsgReReconciledEMAILProcess", new RowMapper<CreateJobVo>() {
//				
//				@Override
//				public CreateJobVo mapRow(ResultSet rs, int rowNum) throws SQLException {
//					CreateJobVo createJobVo = new CreateJobVo();
//					int intRetVal = rs.getInt(1);	
//					createJobVo.setIntRetVal(intRetVal);
//					return createJobVo;
//				}
//			});
//			
//			// turn off processing parameter metadata information 
//			simpleJdbcCallRe.withoutProcedureColumnMetaDataAccess();
//			
//			Map<String, Object> resultsRe = simpleJdbcCallRe.execute();
//			
//			if (resultsRe != null) 
//			{	
//				@SuppressWarnings("unchecked")
//				List<CreateJobVo> createJobList = (List<CreateJobVo>) resultsRe.get("startMsgReReconciledEMAILProcess");
//				if(createJobList != null && createJobList.size() > 0)
//				{
//					CreateJobVo createJobVo = createJobList.get(0);
//					intRetVal = createJobVo.getIntRetVal();
//				}
//			}
//			
//			// second procedure call
//			SimpleJdbcCall simpleJdbcCallUnRe = getMainDBProcedureCall(DBConstants.STP_EMAIL_GENERATE_UNRECONCILED_DATA);
//			
//			simpleJdbcCallUnRe.returningResultSet("startMsgUnReconciledEMAILProcess", new RowMapper<CreateJobVo>() {
//				
//				@Override
//				public CreateJobVo mapRow(ResultSet rs, int rowNum) throws SQLException {
//					CreateJobVo createJobVo = new CreateJobVo();
//					int intRetVal = rs.getInt(1);	
//					createJobVo.setIntRetVal(intRetVal);
//					return createJobVo;
//				}
//			});
//			
//			// turn off processing parameter metadata information 
//			simpleJdbcCallUnRe.withoutProcedureColumnMetaDataAccess();
//			
//			Map<String, Object> resultsUnRe = simpleJdbcCallUnRe.execute();
//			
//			if (resultsUnRe != null) 
//			{	
//				@SuppressWarnings("unchecked")
//				List<CreateJobVo> createJobList = (List<CreateJobVo>) resultsUnRe.get("startMsgUnReconciledEMAILProcess");
//				if(createJobList != null && createJobList.size() > 0)
//				{
//					CreateJobVo createJobVo = createJobList.get(0);
//					intRetVal = createJobVo.getIntRetVal();
//				}
//			}
//			
//		}
//		catch(Exception e)
//		{
//			intRetVal = -1;
//			e.printStackTrace();
//			loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaSwiftMsgsDaoImpl-startMsgReUnReconciledEMAILProcess", "");
//			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
//		}
//		return intRetVal;
//	}
//	
//	public class CreateJobVo
//	{
//		
//		private int intRetVal;
//
//		public int getIntRetVal() {
//			return intRetVal;
//		}
//
//		public void setIntRetVal(int intRetVal) {
//			this.intRetVal = intRetVal;
//		}
//		
//	}
//	
//	
//	@Override
//	public void createSwiftOutwardFileForConcliare() {
//		
//		try
//		{
//			String outFileLocation = env.getProperty("conciiare.csv.swift.outward.file.location");
//			String tempFileLocation = env.getProperty("conciiare.csv.swift.temp.file.location");
//			
//			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_CREATE_SWIFT_OUTWARD_CSV);
//			simpleJdbcCall.declareParameters();
//	
//			MapSqlParameterSource params = new MapSqlParameterSource();
//			
//			simpleJdbcCall.returningResultSet("createSwiftOutwardFileForConcliare", new RowMapper<String>() {
//				
//				@Override
//				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
//					CSVWriter writer = null;
//					String downloadStringNew = null;
//					try
//					{
//						downloadStringNew = tempFileLocation + "SWIFT_OUTWARD_DATA_" + DateUtil.formatDate(new Date(), "ddMMyyyyHHmmss") + ".csv";
//						writer = new CSVWriter(new FileWriter(downloadStringNew), ',', CSVWriter.NO_ESCAPE_CHARACTER, System.getProperty("line.separator"));
//						writer.writeAll(rs, false);
//						writer.close();
//						
//						File source = new File(downloadStringNew);
//						File destination = new File(outFileLocation+source.getName());
//						FileUtils.copyFile(source, destination);						
//					}
//					catch(Exception e)
//					{
//						e.printStackTrace();
//						loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaSwiftMsgsDaoImpl-createSwiftOutwardFileForConcliare", "");
//					}
//					return downloadStringNew;
//				}
//			});
//			
//			// turn off processing parameter metadata information 
//			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
//			
//			simpleJdbcCall.execute(params);
//			
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaSwiftMsgsDaoImpl-createSwiftOutwardFileForConcliare", "");
//			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
//		}
//	}
//	
//	
//	@Override
//	public void createSwiftInOutCreditsFileForConcliare() {
//		
//		try
//		{
//			String outFileLocation = env.getProperty("conciiare.csv.swift.in.out.credits.file.location");
//			String tempFileLocation = env.getProperty("conciiare.csv.swift.temp.file.location");
//			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_CREATE_SWIFT_INOUT_CREDITS_CSV);
//			simpleJdbcCall.declareParameters();
//	
//			MapSqlParameterSource params = new MapSqlParameterSource();
//			
//			simpleJdbcCall.returningResultSet("createSwiftInOutCreditsFileForConcliare", new RowMapper<String>() {
//				
//				@Override
//				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
//					CSVWriter writer = null;
//					String downloadStringNew = null;
//					try
//					{
//						downloadStringNew = tempFileLocation + "SWIFT_CREDITS_INOUT_DATA_" + DateUtil.formatDate(new Date(), "ddMMyyyyHHmmss") + ".csv";
//						writer = new CSVWriter(new FileWriter(downloadStringNew), ',', CSVWriter.NO_ESCAPE_CHARACTER, System.getProperty("line.separator"));
//						writer.writeAll(rs, false);
//						writer.close();
//						
//						File source = new File(downloadStringNew);
//						File destination = new File(outFileLocation+source.getName());
//						FileUtils.copyFile(source, destination);	
//					}
//					catch(Exception e)
//					{
//						e.printStackTrace();
//						loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaSwiftMsgsDaoImpl-createSwiftOutwardFileForConcliare", "");
//					}
//					return downloadStringNew;
//				}
//			});
//			
//			// turn off processing parameter metadata information 
//			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
//			
//			simpleJdbcCall.execute(params);
//			
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaSwiftMsgsDaoImpl-createSwiftInOutCreditsFileForConcliare", "");
//			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
//		}
//	}
//
//	@Override
//	public int sentEmailSMSCommonProcess() {
//		
//		int i = 0;		
//		ArrayList<SentEmailSMSVo> pendingEmailList = null;
//		ArrayList<SentEmailSMSVo> pendingSMSList = null;
//		
//		try
//		{			
//			pendingEmailList = fetchPendingEmailSMSList("E");
//			if(pendingEmailList != null)
//			{
//				if(pendingEmailList.size() > 0)
//				{
//					Iterator<SentEmailSMSVo> emailIterator = pendingEmailList.iterator();
//					while(emailIterator.hasNext()) 
//					{
//						SentEmailSMSVo sentEmailSMSVo = emailIterator.next();
//						
//						String attchmentArray[] = null;
//						if(sentEmailSMSVo.getAttachmentFilePaths() != null && !("").equalsIgnoreCase(sentEmailSMSVo.getAttachmentFilePaths()))
//						{
//							attchmentArray = sentEmailSMSVo.getAttachmentFilePaths().split(",");
//						}						
//						
//						i = sendMail.send(sentEmailSMSVo.getFromList(), sentEmailSMSVo.getToList().split(","), 
//								sentEmailSMSVo.getCcList().split(","), sentEmailSMSVo.getBccList().split(","), 
//								sentEmailSMSVo.getEmailSubject(), sentEmailSMSVo.getEmailBody(), attchmentArray);
//						if(i > 0)
//						{
//							i = updateEmailSMSStatus(sentEmailSMSVo, "E");
//						}
//					}
//				}
//			}
//			else
//			{
//				i = -1;
//			}
//			
//			pendingSMSList = fetchPendingEmailSMSList("S");
//			if(pendingSMSList != null)
//			{
//				if(pendingSMSList.size() > 0)
//				{
//					Iterator<SentEmailSMSVo> emailIterator = pendingSMSList.iterator();
//					while(emailIterator.hasNext()) 
//					{
//						SentEmailSMSVo SentEmailSMSVo = emailIterator.next();
//						i = sendSMS.send(SentEmailSMSVo.getMobileNumber(), SentEmailSMSVo.getSmsBody());
//						if(i > 0)
//						{
//							i = updateEmailSMSStatus(SentEmailSMSVo, "S");
//						}
//					}
//				}
//			}
//			else
//			{
//				i = -1;
//			}
//		}
//		catch (Exception e)
//		{
//			i = -1;
//			loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaSwiftMsgsDaoImpl-sentEmailSMSCommonProcess", "");
//			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
//		}		
//		return i;
//	}
//	
//	@SuppressWarnings("unchecked")
//	private ArrayList<SentEmailSMSVo> fetchPendingEmailSMSList(String emailSmsType) {
//		
//		ArrayList<SentEmailSMSVo> pendingEmailSMSList = new ArrayList<SentEmailSMSVo>();
//	
//		try
//		{
//			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_FETCH_ARM_SMS_EMAIL_DATA);
//			simpleJdbcCall.declareParameters(
//					new SqlParameter("@IEMAILSMSTYPE", Types.VARCHAR));
//	
//			MapSqlParameterSource params = new MapSqlParameterSource()
//					.addValue("@IEMAILSMSTYPE", emailSmsType);
//			
//			simpleJdbcCall.returningResultSet("fetchPendingEmailSMSList", new RowMapper<SentEmailSMSVo>() {
//				
//				@Override
//				public SentEmailSMSVo mapRow(ResultSet rs, int rowNum) throws SQLException {
//						SentEmailSMSVo sentEmailSMSVo = new SentEmailSMSVo();
//						
//						sentEmailSMSVo.setEmailSmsId(rs.getString("ID"));
//						sentEmailSMSVo.setEmailSmsConfigId(rs.getString("ARM_SMS_EMAIL_CONFIG_ID"));
//						sentEmailSMSVo.setMsgId(rs.getString("MSG_ID"));
//						sentEmailSMSVo.setTranRefNo(rs.getString("TRAN_REF_NO"));
//						sentEmailSMSVo.setAmount(rs.getString("AMOUNT"));
//						sentEmailSMSVo.setValueDate(rs.getString("VALUE_DATE"));
//						sentEmailSMSVo.setMobileNumber(rs.getString("SMS_MOBILE_NO"));
//						sentEmailSMSVo.setSmsBody(rs.getString("SMS_BODY"));
//						sentEmailSMSVo.setEmailSubject(rs.getString("EMAIL_SUBJECT"));
//						sentEmailSMSVo.setEmailBody(rs.getString("EMAIL_BODY"));
//						sentEmailSMSVo.setFromList(rs.getString("FROM_LIST") == null ? "" : rs.getString("FROM_LIST"));
//						sentEmailSMSVo.setToList(rs.getString("TO_LIST") == null ? "" : rs.getString("TO_LIST"));
//						sentEmailSMSVo.setCcList(rs.getString("CC_LIST") == null ? "" : rs.getString("CC_LIST"));
//						sentEmailSMSVo.setTableName(rs.getString("TABLE_NAME"));
//						sentEmailSMSVo.setAttachmentFilePaths(rs.getString("ATTACHMENT_FILE_PATH") == null ? "" : rs.getString("ATTACHMENT_FILE_PATH"));
//						sentEmailSMSVo.setBccList("");
//						
//						return sentEmailSMSVo;					
//				}
//			});
//			
//			// turn off processing parameter metadata information 
//			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
//			
//			Map<String, Object> results = simpleJdbcCall.execute(params);
//			
//			if (results != null) 
//			{			
//				pendingEmailSMSList = (ArrayList<SentEmailSMSVo>) results.get("fetchPendingEmailSMSList");				
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaSwiftMsgsDaoImpl-fetchPendingEmailSMSList", "");
//			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
//		}
//		return pendingEmailSMSList;
//	}
//	
//
//	private int updateEmailSMSStatus(SentEmailSMSVo sentEmailSMSVo, String emailSmsType) 
//	{
//		int intRetVal = -1;
//		
//		try
//		{
//			SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.STP_ARM_SMS_EMAIL_DATA_UPD_STATUS);
//			simpleJdbcCall.declareParameters(
//					new SqlParameter("@ISMSEMAILID", Types.INTEGER),
//					new SqlParameter("@IEMAILSMSTYPE", Types.VARCHAR),
//					new SqlParameter("@TABLE_NAME", Types.VARCHAR));
//	
//			MapSqlParameterSource params = new MapSqlParameterSource()
//					.addValue("@ISMSEMAILID", sentEmailSMSVo.getEmailSmsId())
//					.addValue("@IEMAILSMSTYPE", emailSmsType)
//					.addValue("@TABLE_NAME", sentEmailSMSVo.getTableName());
//			
//			simpleJdbcCall.returningResultSet("updateEmailSMSStatus", new RowMapper<SentEmailSMSVo>() {
//				
//				@Override
//				public SentEmailSMSVo mapRow(ResultSet rs, int rowNum) throws SQLException {
//					SentEmailSMSVo sentEmailSMSVo = new SentEmailSMSVo();
//					int intRetVal = rs.getInt(1);	
//					sentEmailSMSVo.setMsgId(""+intRetVal);;
//					return sentEmailSMSVo;
//				}
//			});
//			
//			// turn off processing parameter metadata information 
//			simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
//			
//			Map<String, Object> results = simpleJdbcCall.execute(params);
//			
//			if (results != null) 
//			{	
//				@SuppressWarnings("unchecked")
//				List<SentEmailSMSVo> SentEmailSMSVoList = (List<SentEmailSMSVo>) results.get("updateEmailSMSStatus");
//				if(SentEmailSMSVoList != null && SentEmailSMSVoList.size() > 0)
//				{
//					SentEmailSMSVo sentEmailSMSVo1 = SentEmailSMSVoList.get(0);
//					String msgId = sentEmailSMSVo1.getMsgId();
//					if(msgId != null && !("").equalsIgnoreCase(msgId))
//						intRetVal = Integer.parseInt(sentEmailSMSVo1.getMsgId());
//				}
//			}
//		}
//		catch(Exception e)
//		{
//			intRetVal = -1;
//			e.printStackTrace();
//			loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaSwiftMsgsDaoImpl-updateEmailSMSStatus", "");
//			throw new RuntimeException(env.getProperty("app.common.error.invalid.request"));
//		}
//		return intRetVal;
//	}

}
