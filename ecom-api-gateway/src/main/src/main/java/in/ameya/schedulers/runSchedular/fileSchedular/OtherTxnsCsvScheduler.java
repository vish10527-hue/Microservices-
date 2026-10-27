package in.ameya.schedulers.runSchedular.fileSchedular;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import in.ameya.schedulers.schedulersUtil.util.SchedulerUtil;
import in.ameya.schedulers.service.fileSchedularService.OtherTxnsCsvService;
import in.ameya.schedulers.vo.fileSeries.OtherTxnsCsvVo;

@Component
@PropertySource("classpath:/filePath.properties")
public class OtherTxnsCsvScheduler {
    private static final Logger logger = LogManager.getLogger(FottCsvScheduler.class);

    @Autowired
    private Environment env;
    
    @Value("${other.file.path}")
    private  String transactionPath;
    
    @Value("${other.processed.path}")
    private String processedPath;
    
    @Autowired
    private OtherTxnsCsvService OtherTxnsCsvService;

    @Autowired
	SchedulerUtil schedulerUtil;
    
//    @Scheduled(cron = "0 11 15 * * ?")  //millisecond 
    @Scheduled(fixedRate = 30000)
   public void processTransactionFiles() {
    	
        File folder = new File(transactionPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

        if (files == null || files.length == 0) {
            logger.info("No files to process in directory", transactionPath);
            return;
        }
        int sourceSystem = SchedulerUtil.SOURCE_SWIFT;
        for (File file : files) {
            try {
                logger.info("Processing OTHER TXNS file", file.getName());
                String murNumber = "";
                String fileModDateTime = "";
                String fileType = schedulerUtil.getFileTypeFromFileName(file);
                String dateMURStr = schedulerUtil.getMURNumberFileDate(file.getAbsoluteFile(), sourceSystem);
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
				//int inOutFlag = schedulerUtil.getInOutFlag(file.getAbsoluteFile());
                Integer uploadKey = OtherTxnsCsvService.isFileUploaded(fileType,file.getName(),sourceSystem,fileModDateTime,murNumber,1); //inout flag 1
               
                if (uploadKey > 0) {
                    // Check uploaded file contents
                     List<OtherTxnsCsvVo> transactionList = OtherTxnsCsvService.readFile(file);

                    // Save transactions in batches
                    String errorMsg=OtherTxnsCsvService.saveTransactionsInBatches(transactionList, file.getName(), uploadKey);
                    int status=0;
                    if(errorMsg == null) {
                    	errorMsg = env.getProperty("records.inserted.success");
                    }else {
                    	status=2;
                    }

                    // Move processed file
                    Files.move(file.toPath(), Paths.get(processedPath, file.getName()));
                    logger.info("processTransactionFiles("+file.getName()+"): Successfully processed and moved file");

                    // Validation process
                    OtherTxnsCsvService.validateUploadFile(uploadKey);
                    logger.info("Transaction file processing completed.");

                } else {
                	logger.warn("processTransactionFiles("+file.getName()+"): Skipping file due to missing uploadKey");
                }
            } catch (Exception e) {
            	logger.error("processTransactionFiles("+file.getName()+")", e);
            }
        }
    }
}
