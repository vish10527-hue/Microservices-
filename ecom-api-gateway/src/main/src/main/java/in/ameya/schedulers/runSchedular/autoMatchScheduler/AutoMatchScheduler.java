package in.ameya.schedulers.runSchedular.autoMatchScheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import in.ameya.schedulers.service.autoMatchService.AutoMatchService;

@Component
public class AutoMatchScheduler {

    private static final Logger logger =LogManager.getLogger(AutoMatchScheduler.class);

    @Autowired
    private AutoMatchService autoMatchService;

    @Scheduled(cron = "0 */5 * * * ?")
    public void autoMatchJob() {

        Integer systemUserKey = 1;

        logger.info("Auto Match Scheduler started");

        try {
            autoMatchService.executeAutoMatch(systemUserKey);
            logger.info("Auto Match Scheduler completed");
        } catch (Exception e) {
            logger.error("Auto Match Scheduler failed", e);
        }
    }
}
