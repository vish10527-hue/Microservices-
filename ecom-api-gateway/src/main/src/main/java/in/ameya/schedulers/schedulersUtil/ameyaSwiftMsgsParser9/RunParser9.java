package in.ameya.schedulers.schedulersUtil.ameyaSwiftMsgsParser9;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import in.ameya.schedulers.schedulersUtil.util.SchedulerUtil;
import in.ameya.schedulers.service.mtSeriesService.AmeyaSwiftMsgsService;
import in.ameya.schedulers.vo.mtSeries.MsgType9;
import in.ameya.util.LoggerUtil;


@Component
@PropertySource("classpath:/filePath.properties")
public class RunParser9 
{

	
	@Autowired
	SwiftParser9 swiftParser9;
	
	@Autowired
    public  Environment env;
		
	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	SchedulerUtil schedulerUtil;
	
	@Autowired
	AmeyaSwiftMsgsService ameyaSwiftMsgsService;
	
	public boolean RespawnThread(int sourceSystem, MsgType9 swiftMsgType, String PROCESSING_QUEUE_PATH, String BACKUP_PATH, String msgDir)
	{		
		String strInputDirectory = PROCESSING_QUEUE_PATH + msgDir + "\\";
		String strBackupDirectory = BACKUP_PATH  + msgDir  + "\\";
		
		String format = "yyyy-MM-dd";
        DateFormat dateFormatter = new SimpleDateFormat(format);
        Date date = new Date();
		String strDate = dateFormatter.format(date);
		strBackupDirectory = strBackupDirectory + strDate;
		File theDir = new File(strBackupDirectory);
		if (!theDir.exists()){
		    theDir.mkdirs();
		}
		
		String tempBackupDirectory = BACKUP_PATH  + msgDir  + "\\";
		String exceptionPath = tempBackupDirectory.replace(msgDir, "EXCEPTION");
		exceptionPath = exceptionPath + strDate;
		File theExceptionDir = new File(exceptionPath);
		if (!theExceptionDir.exists()){
			theExceptionDir.mkdirs();
		}			
		
		strBackupDirectory = strBackupDirectory +"\\";
		exceptionPath = exceptionPath +"\\";
		
		File[] fls = new File(strInputDirectory).listFiles();
		if(fls.length > 0)
		{
			int errorCode = -1;
			int intRecordID = -1;
			try
			{			
				String fileModDateTime = "";
				String murNumber = "";
				
				String dateMURStr = schedulerUtil.getMURNumberFileDate(fls[0], sourceSystem);
				if(!("").equalsIgnoreCase(dateMURStr))
				{
					if(dateMURStr.contains("#"))
					{
						if(dateMURStr.contains("#"))
						{
							int length = dateMURStr.length();
							if(length >= 1)
								murNumber = dateMURStr.split("#")[0];
							if(length >= 2)
								fileModDateTime = dateMURStr.split("#")[1];						
						}					
					}
				}
				int inOutFlag = schedulerUtil.getInOutFlag(fls[0]);
				intRecordID = CreateJob(swiftMsgType.toString(), fls[0].getName(), sourceSystem, fileModDateTime, murNumber,inOutFlag);
				if(intRecordID < 0)
				{
					System.out.println("intRecordID==>"+errorCode);
					schedulerUtil.copyFileToRespectedDir(strInputDirectory + fls[0].getName(), exceptionPath + fls[0].getName());
				}
				else
				{						
					System.out.println("intRecordID==>"+intRecordID);
					processMessage(swiftParser9, swiftMsgType, intRecordID, strInputDirectory + fls[0].getName(), 
							strBackupDirectory, exceptionPath);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "RunParser9 1: RespawnThread", "");
			}
		}
		else
			return false;
		return true;
	}
		
		
	private int CreateJob(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag)
	{
		int intRetVal = -1;
		try 
		{
			intRetVal = ameyaSwiftMsgsService.createJob(msgType, strFileName, sourceSystem, fileModDateTime, murNumber, inOutFlag);			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "RunParser9 1: CreateJob 1", "");			
		} 
		return intRetVal;		
	}

	
	 public void processMessage(SwiftParser9 swiftParser9, MsgType9 swiftMsgType, int intRecordID, String strInputDirectoryWithFileName, 
				String strBackupDirectory, String exceptionPath)
	 {
		try
		{
			if(intRecordID > 0)
			{
				int errorCode = swiftParser9.processInOutFiles(intRecordID, swiftMsgType, strInputDirectoryWithFileName);	
				if(errorCode > 0)
				{
					File source = new File(strInputDirectoryWithFileName);
					File dest = new File(strBackupDirectory + source.getName());
					try {
					    FileUtils.copyFile(source, dest);
					    if(source.exists())
					    {
					    	source.delete();
					    }
					} catch (IOException e) {
					    e.printStackTrace();
					}
				}
				else
				{
					SimpleDateFormat dtF = new SimpleDateFormat("ddMMyyHHmmss");
					File source = new File(strInputDirectoryWithFileName);
					File dest = new File(exceptionPath + source.getName());
					try {
						
						if(dest.exists())
						{
							dest = new File(exceptionPath + source.getName()+ dtF.format(new java.util.Date()));
						}						
					    FileUtils.copyFile(source, dest);
					    if(source.exists())
					    {
					    	source.delete();
					    }
					} catch (IOException e) {
					    e.printStackTrace();
					}
				}								
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "RunParser9 1: run 2", "");
		}
		finally
		{
			File source = new File(strInputDirectoryWithFileName);
			File dest = new File(exceptionPath + source.getName());
			SimpleDateFormat dtF = new SimpleDateFormat("ddMMyyHHmmss");
			
			try
			{
				if(source.exists())
				{
					if(dest.exists())
					{
						dest = new File(exceptionPath + source.getName()+ dtF.format(new java.util.Date()));
					}						
				    FileUtils.copyFile(source, dest);
				    if(source.exists())
				    {
				    	source.delete();
				    }
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				loggerUtil.logErrorInsert(e.getMessage(), e, "RunParser9: run 3", "");
			}
		}		
	}

}

