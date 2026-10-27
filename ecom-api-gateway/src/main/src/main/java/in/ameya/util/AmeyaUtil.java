package in.ameya.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AmeyaUtil {
	
	
	public static final int MAX_PAGE_SIZE = 50;
	
	public static final String DATE_FORMAT_1 = "dd-MMM-yyyy HH:mm:ss";
	
    private static final Logger log = LoggerFactory.getLogger(AmeyaUtil.class);

	public static String getMessageTypeCode(String InputString)
	{
		String messageTypeCode = "1";
		String Tag = "{2:";
		
		try {
			messageTypeCode = InputString.substring(InputString.indexOf(Tag) + 4).substring(0, 3);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e, "AmeyaUtil-getMessageTypeCode", "");
		}
		return messageTypeCode;
	}
	
	
	public static List<File> getAllFilesinList(File filePath, List<File> listOfFiles)
	{
		try
		{
			File[] fList = filePath.listFiles();
		    if(fList != null)
		    {
		        for (File file : fList) {      
		            if (file.isFile()) {
		            	listOfFiles.add(file);
		            } else if (file.isDirectory()) {
		            	getAllFilesinList(file, listOfFiles);
		            }
		        }
		    }		
		}
		catch(Exception e)
		{
			log.error("AmeyaUtil.getAllFilesinList");
		}
		return listOfFiles;
	}
	
	public static String getFileExactPath(String fileNameWithPath, String destinationPath, String msgType){
		try
		{
			if(destinationPath.contains("<msg_type>"))
			{
				destinationPath = destinationPath.replace("<msg_type>", msgType);
			}
			
			File newFile = new File(fileNameWithPath);
			if(destinationPath.contains("<today_date>"))
			{
				String format = "yyyy-MM-dd";
		        DateFormat dateFormatter = new SimpleDateFormat(format);
		        Date date = new Date();
				String strDate = dateFormatter.format(date);
				//destinationPath = destinationPath.replace("<today_date>", "\\"+strDate);
				destinationPath = destinationPath.replace("<today_date>", File.separator+strDate);
				File theDir = new File(destinationPath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}
				
				//destinationPath = destinationPath+"\\";
				destinationPath = destinationPath+File.separator;
			}	
			
			
			destinationPath = destinationPath+newFile.getName();
			
			return destinationPath;
			
		}
		catch(Exception e)
		{
			log.error(e.getMessage(), e, "AmeyaUtil.moveFileToRespectedDir", "");
			return "";
		}
	}
	public static String moveFileToRespectedDir(String fileNameWithPath, String destinationPath, String msgType) {
		try {
			destinationPath = getFileExactPath(fileNameWithPath,destinationPath,msgType);
			Files.move(Paths.get(fileNameWithPath), Paths.get(destinationPath)); 
			return destinationPath;
		} catch(Exception e) {
			log.error(e.getMessage(), e, "AmeyaUtil.moveFileToRespectedDir", "");
		}
		
		return null;
	}
	
	public static boolean isBlank(String value) {
		return (value == null || value.replaceAll("\\s", "").isEmpty());
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
					if(("#").equalsIgnoreCase(inputVal))						
						returnVal = "";
					else
						returnVal = inputVal;
				}
			}
		}catch(Exception e)
		{
			returnVal = "";
		}
		
		return returnVal;
	}
	
	public static final void showError(HttpServletResponse response, String errorMsg)
	{
		PrintWriter writer;
		try {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			writer = response.getWriter();
			writer.println(errorMsg);
			writer.flush();
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}		
	}
	
	
	public static String getSHA(String message) throws Exception {
		MessageDigest digest;
		byte[] hash;
		digest = MessageDigest.getInstance("SHA-256");
		hash = digest.digest(
				message.getBytes(StandardCharsets.UTF_8)
			);
		StringBuilder hexString = new StringBuilder();
	    for (byte b : hash) {
	      hexString.append(String.format("%02x", b));
	    }
		return hexString.toString();
	}
	
	public static int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	
	private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR" };

	public static String getClientIpAddress(HttpServletRequest request) {
		for (String header : HEADERS_TO_TRY) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				if(ip.contains(",")) ip = ip.split(",")[0];
				return ip;
			}
		}

		return request.getRemoteAddr();
	}
	
	public static String formatMXString(String formattedString) {
		String formattedStringNew =  formattedString.replaceAll("\"(.*?)\"", "<b>\"$1\"</b>");
		return formattedStringNew;
	}
}
