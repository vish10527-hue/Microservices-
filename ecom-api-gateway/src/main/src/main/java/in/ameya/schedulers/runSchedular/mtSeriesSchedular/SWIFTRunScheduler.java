package in.ameya.schedulers.runSchedular.mtSeriesSchedular;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import in.ameya.schedulers.schedulersUtil.ameyaSwiftMsgsParser9.RunParser9;
import in.ameya.schedulers.schedulersUtil.util.SchedulerUtil;
import in.ameya.schedulers.vo.mtSeries.MsgType9;
import in.ameya.util.LoggerUtil;



@Component
@PropertySource("classpath:/filePath.properties")
public class SWIFTRunScheduler {

	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
    public  Environment env;
	
	@Autowired
	RunParser9 runParser9;
	
	
	
	@Scheduled(fixedRate=5000, initialDelay = 5000)
	public void SwiftRunScheduler()
	{
		//String SCHEDULER_STARTED_PATH = env.getProperty("schdular.parent.file.path");
		String PROCESSING_QUEUE_PATH = env.getProperty("schdular.parent.file.path")+env.getProperty("swift.file.directory")+env.getProperty("InputDirectory");
		String BACKUP_PATH = env.getProperty("schdular.parent.file.path")+env.getProperty("swift.file.directory")+env.getProperty("BackupDirectory");
		
		//File fileToCreate = new File(SCHEDULER_STARTED_PATH+"SwiftRunScheduler.txt");
		
		try 
		{
			/*if(fileToCreate.exists()){
				loggerUtil.logInfo("***** SwiftRunScheduler Process Already Running *****");
			}
			else
			{
				try {
					fileToCreate.createNewFile();
				} catch (IOException e) 
				{
					loggerUtil.logErrorInsert(e.getMessage(), e, "SwiftRunScheduler 1: SwiftRunScheduler", "");
				}*/ 
				
				//runParser1.RespawnThread(MsgType1.MT101);
				runParser9.RespawnThread(SchedulerUtil.SOURCE_SWIFT, MsgType9.MT950, PROCESSING_QUEUE_PATH, BACKUP_PATH, env.getProperty("MT950DirectoryName"));
				runParser9.RespawnThread(SchedulerUtil.SOURCE_SWIFT, MsgType9.MT940, PROCESSING_QUEUE_PATH, BACKUP_PATH, env.getProperty("MT940DirectoryName"));
			//}				
		}
		catch (Exception e) 
		{
			loggerUtil.logErrorInsert(e.getMessage(), e, "SwiftRunScheduler 2: SwiftRunScheduler", "");
		} 
		/*finally{
			if(fileToCreate.exists()){
				fileToCreate.delete();
			}
		}*/
	}
	
}
