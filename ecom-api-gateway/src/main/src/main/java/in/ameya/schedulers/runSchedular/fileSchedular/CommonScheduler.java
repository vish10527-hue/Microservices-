package in.ameya.schedulers.runSchedular.fileSchedular;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.ameya.schedulers.runSchedular.mtSeriesSchedular.SWIFTRunScheduler;

@Component
public class CommonScheduler {

    @Autowired
    private FittCsvScheduler fittCsvScheduler;

    @Autowired
    private FottCsvScheduler fottCsvScheduler;

    @Autowired
    private OtherTxnsCsvScheduler otherTxnsCsvScheduler;

    @Autowired
    private CredenceMirrorExcelScheduler credenceMirrorExcelScheduler;
    
    @Autowired
    private SWIFTRunScheduler swiftRunScheduler;
    
    // Runs every 5 minutes
//    @Scheduled(cron = "0 */5 * * * ?")
    public void runAllJobs() {

    	fittCsvScheduler.processTransactionFiles();
    	fottCsvScheduler.processTransactionFiles();
    	otherTxnsCsvScheduler.processTransactionFiles();
    	credenceMirrorExcelScheduler.processTransactionFiles();
    	swiftRunScheduler.SwiftRunScheduler();
    }
}
