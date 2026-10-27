package in.ameya.util;

import java.sql.SQLException;
import java.util.HashMap;


public class Constants {

	public static HashMap<String, String> userSession = new HashMap<String, String>();
	
	public static final String AES_ENCRYPT_DECRYPT_SALT = "1743011251094159195099140010066165";
	public static final String DELIMITER = "#_\\*";
	public static final String SEPARATOR = "#_*";
	public static final String PIPE_SEPARATOR = "\\|";
	public static final String SLASH_SEPARATOR = "/";
	public static final String NOTPROVIDED = "NOTPROVIDED";

	public static final int FAILED_REASON_LENGTH = 200;
	public static final String PACS008= "PACS008";
	public static final String PACS009="PACS009";
	public static final String mxSaperator = "$";
	public static final String RECORD_INSERT_STATUS = "0";
	public static final String RECORD_UPDATE_STATUS = "1";
	public static final String RECORD_DELETE_STATUS = "-1";
	public static final String RECORD_APPROVE_STATUS = "1";
	public static final String RECORD_REJECT_STATUS = "2";
	
	public static final String STATUS_ACTIVE = "1";
	public static final String STATUS_DEACTIVE = "0";
	
	public static final int MAX_PAGE_SIZE = 10;
	
	public static final String RECORD_INSERT = "I";
	public static final String RECORD_UPDATE = "U";
	public static final String RECORD_DISCARD = "C";
	public static final String RECORD_DELETE = "D";
	public static final String RECORD_APPROVE = "A";
	public static final String RECORD_REJECT = "R";
	public static final String RECORD_VIEW = "V";
				
	public static final String EXCEPTION_OCCURED = "EXCEPTION_OCCURED";
	
	public static final String DATE_FORMAT_1 = "dd-MMM-yyyy HH:mm:ss";
	public static final String DATE_FORMAT_2 = "dd/MM/yyyy HH:mm:ss";
	
	public static final int DHANLAXMI_RECORD_TYPE_KEY = 1;

	
	public static final String USER_TYPE_ADMIN_MAKER = "2";
	public static final String USER_TYPE_ADMIN_CHECKER = "3";
	public static final String USER_TYPE_TRAN_MAKER = "4";
	public static final String USER_TYPE_TRAN_CHECKER = "5";
	public static final String USER_TYPE_TRAN_APPROVER = "6";
	public static final String USER_TYPE_TRAN_RCU = "7";
	public static final String USER_TYPE_AUDITOR = "8";
	
	public static final String LOOKUP_TYPE_ZERO = "0";
	public static final String LOOKUP_USER_TYPE = "1";
	public static final String LOOKUP_ENTITY_TYPE = "50";
	public static final String LOOKUP_DOCUMENT_TYPE = "51";
	public static final String LOOKUP_DOC_TMP_DOC_TYPE = "63";
	public static final String LOOKUP_ENTITY_CATEGORY = "52";
	public static final String LOOKUP_ADDRESS_TYPE = "53";
	public static final String LOOKUP_ADDRESS_STATUS = "54";
	public static final String LOOKUP_RELATION_WITH_PARTNER = "55";
	public static final String LOOKUP_ACTIVITY_MASTER = "56";
	public static final String LOOKUP_DEVIATION_CATEGORY = "57";
	public static final String LOOKUP_TEMPLATE_TYPE = "59";
	public static final String LOOKUP_TERMINATION_REASON = "60";
	public static final String LOOKUP_FILE_EXTENSION = "58";
	public static final String LOOKUP_DEVIATION_SUB_CATEGORY = "61";
	public static final String LOOKUP_ACCOUNT_TYPE = "62";
	public static final String LOOKUP_STAFF_PROFILE = "64";
	public static final String LOOKUP_EMAIL_TEMPLATE = "65";
	public static final String LOOKUP_TERMINATION_NOTICE = "1001";
	public static final String LOOKUP_TERMINATION_ACCEPTANCE = "1002";
	public static final String LOOKUP_VENDOR_STAFF_DOCUMENT = "1003";
	public static final String LOOKUP_VENDOR_STAFF_TEMPLATE = "1011";
	
	public static final String ENTITY_SUBSIDARY = "2011";
	public static final String ENTITY_NEW_GROUP = "2010";
	public static final String ENTITY_STANDALONE = "2012";
	public static final String ENTITY_PROPRIETORSHIP = "2008";
	
	public static final String LOCATION_TYPE_COUNTRY = "0";
	public static final String LOCATION_TYPE_STATE = "1";
	public static final String LOCATION_TYPE_CITY = "2";
	public static final String LOCATION_TYPE_LOCATION = "3";
	
	public static final String TERMINATION_TYPE_NORMAL_EXIT = "1";
	public static final String TERMINATION_TYPE_FORCED_TERM = "2";
	
	public static final String DECISION_PURGE = "0";
	public static final String DECISION_APPROVE = "1";
	public static final String DECISION_REJECT = "2";
	public static final String DECISION_FORWARD = "3";
	public static final String DECISION_RECOMMEND = "4";
	public static final String DECISION_SCREENED = "5";
	public static final String DECISION_SAMPLED_POSITIVE = "6";
	public static final String DECISION_SAMPLED_NEGATIVE = "7";
	public static final String DECISION_CLOSURE = "8";
	public static final String DECISION_RECOMMEND_TO_RCU = "9";
	public static final String DECISION_RECOMMEND_TO_CHECKER = "10";
	
	public static final String RESULT_COMPLIAINT = "1";
	public static final String RESULT_NON_COMPLIAINT = "2";
	
	public static final String ONBOARDING = "1";
	public static final String MODIFICATION = "2";
	public static final String PREBOARDED = "3";
	public static final String TERMINATION = "4";
	public static final String ACTIVATE_DE_EMPALMENT = "5";
	public static final String ONBOARD = "7";
	public static final String STAFF_RENEWAL = "3";
	
	public static final String PARTNER_ONBOARD_CLOSURE_DOCUMENT = "1010";
	public static final String VENDOR_EMPANEl_PENDING_WITH_MAKER_CLOSURE = "21";
	public static final String CHECKER_REJECTED_PENDING_WITH_MAKER = "6";
	public static final String APPROVER_REJECTED_PENDING_WITH_MAKER = "7";
	public static final String STAFF_CHECKER_REJECTED_PENDING_WITH_MAKER = "2";
	public static final String PREBOARDED_CLOSURE_PENDING_WITH_CHECKER = "11";
	public static final String PREBOARDED_CLOSURE_PENDING_WITH_MAKER = "10";
	public static final String VENDOR_EMPANELLED = "12";
	public static final String VENDOR_TERMINATED = "15";
	public static final String VENDOR_DISCARD = "16";
	
	public static final String STATUS_RENEW_PENDING_MAKER = "11";
	public static final String RENEWAL_PENDING_CHECKER = "6";
	public static final String RENEWAL_REJECTED_CHECKER = "7";
	public static final String EXIT_PENDING_CHECKER = "8";
	public static final String EXIT_REJECTED_CHECKER = "9";
	public static final String STATUS_EXIT = "10";
	public static final String TRN_STATUS_TYPE = "2";
	
	public static final String UPL_TRN_VPOB_OWN_MAN_DET = "1";
	public static final String UPL_TRN_VPOB_BG_VERIFILE_EXT = "2";
	public static final String UPL_TRN_VPOB_BG_ENTDOC_EXT = "3";
	public static final String UPL_TRN_VPOB_TERMI_DOC_EXT = "4";
	public static final String UPL_TRN_VPOB_CLOSURE_DOC_EXT = "5";
	public static final String UPL_TRN_STAFF_DOC_EXT = "6";
	public static final String UPL_TRN_STAFF_TEMPLATE_DOC_EXT = "7";
	
	public static final String SYS_PARAM_LDAP_SYS_USERNAME = "8";
	public static final String SYS_PARAM_LDAP_SYS_PASSWORD = "9";
	
	public static final String OTP_SERVICE_URL = "17";
	public static final String OTP_SERVICE_AUTH_USERNAME = "18";
	public static final String OTP_SERVICE_AUTH_PASSWORD = "19";
	public static final String OTP_SERVICE_AUTH_DOMAIN = "20";
	public static final String OTP_SERVICE_X_IBM_CLIENT_ID = "21";
	public static final String OTP_SERVICE_X_IBM_CLIENT_SECRET = "22";
	public static final String OTP_SERVICE_ENABLED_FLAG = "23";
	public static final String OTP_SERVICE_JKS_NAME_1 = "24";
	public static final String OTP_SERVICE_JKS_PASSWD_1 = "25";
	public static final String BUREAUQUERY_WSLINK = "5";
	
	//public static final String ARM_MT101_MSG_TYPE = "12";
	public static final int  INWARD_FLAG = 1;
	public static final int  OUTWARD_FLAG = 2;
	
	public static final String USER_BRANCH = "B";
		
	
	public static final String ARM_MT103_MSG_TYPE = "1";
	public static final String ARM_MT195_MSG_TYPE = "2";
	public static final String ARM_MT196_MSG_TYPE = "3";
	public static final String ARM_MT199_MSG_TYPE = "4";
	public static final String ARM_MT202_MSG_TYPE = "5";
	public static final String ARM_MT400_MSG_TYPE = "6";
	public static final String ARM_MT410_MSG_TYPE = "33";
	public static final String ARM_MT412_MSG_TYPE = "34";
	public static final String ARM_MT416_MSG_TYPE = "40";

	public static final String ARM_MT756_MSG_TYPE = "7";
	public static final String ARM_MT910_MSG_TYPE = "8";
	public static final String ARM_MT940_MSG_TYPE = "9";
	public static final String ARM_MT950_MSG_TYPE = "10";
	public static final String ARM_MT200_MSG_TYPE = "13";
	public static final String ARM_MT110_MSG_TYPE = "19";
	public static final String ARM_MT111_MSG_TYPE = "20";
	public static final String ARM_MT192_MSG_TYPE = "21";
	
	public static final String ARM_MT103_INR_MSG_TYPE = "11";
	public static final String ARM_MT101_MSG_TYPE = "12";
	public static final String MT101_MANDATE_NEFT = "13";
	public static final String ARM_MT740_MSG_TYPE = "14";
	public static final String ARM_MT742_MSG_TYPE = "15";
	public static final String ARM_MT747_MSG_TYPE = "16";
	public static final String ARM_MT300_MSG_TYPE = "17";
	public static final String ARM_MT320_MSG_TYPE = "18";
	
	public static final String ARM_MT292_MSG_TYPE = "41";
	public static final String ARM_MT295_MSG_TYPE = "42";
	public static final String ARM_MT299_MSG_TYPE = "43";
	public static final String ARM_MT392_MSG_TYPE = "44";
	public static final String ARM_MT395_MSG_TYPE = "45";
	public static final String ARM_MT399_MSG_TYPE = "46";
	
	public static final String ARM_MT499_MSG_TYPE = "36";

	public static final String ARM_MT710_MSG_TYPE = "28";
	public static final String ARM_MT720_MSG_TYPE = "29";
	public static final String ARM_MT734_MSG_TYPE = "27";
	public static final String ARM_MT750_MSG_TYPE = "30";
	public static final String ARM_MT752_MSG_TYPE = "31";
	public static final String ARM_MT754_MSG_TYPE = "32";
	public static final String ARM_MT767_MSG_TYPE = "37";
	public static final String ARM_MT792_MSG_TYPE = "38";
	public static final String ARM_MT795_MSG_TYPE = "39";
	public static final String ARM_MT760_MSG_TYPE = "35";
	public static final String ARM_MT700_MSG_TYPE = "22";
	public static final String ARM_MT707_MSG_TYPE = "23";
	//need to add
	public static final String ARM_MT730_MSG_TYPE = "26";
	public static final String ARM_MT799_MSG_TYPE = "24";
	
	public static final String ARM_MT999_MSG_TYPE = "25";
	
	public static final String ARM_MT210_MSG_TYPE = "47";
	public static final String ARM_MT305_MSG_TYPE = "48";
	public static final String ARM_MT341_MSG_TYPE = "49";
	public static final String ARM_MT360_MSG_TYPE = "50";
	public static final String ARM_MT362_MSG_TYPE = "51";
	
	public static final String ROLE_TYPE_NONE = "4";
	public static final String ROLE_TYPE_MAKER = "5";
	public static final String ROLE_TYPE_CHECKER = "6";
	public static final String ROLE_TYPE_BOTH = "7";
	public static final String ROLE_TYPE_SUP_CHECKER = "8";
	
	public static final String BRANCH_TYPE_BCAT = "B";
	public static final String BRANCH_TYPE_CCAT = "C";
	public static final String YES = "1";
	public static final String NO = "0";
	
	//Email SMS Flag
	public static final String EMAIL_FLAG = "E";
	public static final String SMS_FLAG = "S";
	public static final String EMAIL_SMS_BOTH = "B";
	
	public static final int  SOURCE_SWIFT = 0;
	public static final int  SOURCE_FLEXCUBE = 1;
	public static final int  SOURCE_CALYPSO = 2;
	
	public static final int  ACKED_FLAG = 0;
	public static final int  NACKED_FLAG = 1;
	
	//Scheduler
	public static final int FITT_RECORD_TYPE_KEY = 1;
	public static final int FOTT_RECORD_TYPE_KEY =2;
	public static final int OTHER_TXNS_RECORD_TYPE_KEY=3;
	public static final int CREDENCE_MIRROR_RECORD_TYPE_KEY=4;
	
	public static final int FITT_BATCH_SIZE = 100;
	public static final int FOTT_BATCH_SIZE = 100;
	public static final int OTHER_TXNS_BATCH_SIZE=100;
	public static final int CREDENCE_MIRROR_BATCH_SIZE=100;

	
	public static String GetOracleErrorMsg(SQLException ex)
	{
		String strErrorMsg = ex.getMessage();
		strErrorMsg = strErrorMsg.split("\n")[0]; 
		if(strErrorMsg.indexOf("ORA-20000") > -1 || strErrorMsg.indexOf("ORA-20001") > -1)
			strErrorMsg = strErrorMsg.substring(strErrorMsg.indexOf("ORA-2000") + 10).trim();
		return strErrorMsg;
	}
	
	public static final String getSplitValue(String inputVal, String seperatorStr, int index){
		String returnVal = "";
		try
		{
			if(inputVal != null && !("").equalsIgnoreCase(inputVal))
			{
				if(inputVal.split(seperatorStr).length > 1)
				{
					returnVal = inputVal.split(seperatorStr)[index];
				}
				else
				{
					returnVal = inputVal;
				}
			}
		}catch(Exception e)
		{
			returnVal = "";
		}
		
		return returnVal;
	}
	
	public static String replaceSpecialCharWithNewLine(String inputStr)
	{
		String outputStr = "";
		try
		{
			if(inputStr != null)
			{
				outputStr = inputStr.replace("#_*", "<br>");
			}
		
		} catch (Exception e) {
	    	//loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaConstants-replaceSpecialCharWithNewLine", "");
	    }
		return outputStr;		
	}
	
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	    	strNum = strNum.replaceAll(",", "");
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public static String getParameter(String data) {
		if("".equalsIgnoreCase(data)) {
			return null;
		}
		return data;
	}
	
}
