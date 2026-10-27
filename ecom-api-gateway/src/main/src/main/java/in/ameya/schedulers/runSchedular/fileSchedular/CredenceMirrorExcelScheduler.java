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
import in.ameya.schedulers.service.fileSchedularService.CredenceMirrorExcelService;
import in.ameya.schedulers.vo.fileSeries.CredenceMirrorExcelVo;

@Component
@PropertySource("classpath:/filePath.properties")
public class CredenceMirrorExcelScheduler {

	private static final Logger logger = LogManager.getLogger(CredenceMirrorExcelScheduler.class);

	@Autowired
	private Environment env;

	@Value("${credenceMirror.file.path}")
	private String transactionPath;

	@Value("${credenceMirror.processed.path}")
	private String processedPath;

	@Autowired
	private CredenceMirrorExcelService credenceMirrorExcelService;

	@Autowired
	private SchedulerUtil schedulerUtil;

	@Scheduled(fixedRate = 30000)
	public void processTransactionFiles() {

		File folder = new File(transactionPath);

		File[] files = folder
				.listFiles((dir, name) -> name.toLowerCase().endsWith(".xls") || name.toLowerCase().endsWith(".xlsx"));

		if (files == null || files.length == 0) {
			logger.info("No Excel files to process in directory {}", transactionPath);
			return;
		}

		int sourceSystem = SchedulerUtil.SOURCE_SWIFT;

		for (File file : files) {
			try {
				logger.info("Processing Credence Mirror Excel file {}", file.getName());

				String murNumber = "";
				String fileModDateTime = "";

				String fileType = schedulerUtil.getFileTypeFromFileName(file);
				String dateMURStr = schedulerUtil.getMURNumberFileDate(file.getAbsoluteFile(), sourceSystem);

				if (dateMURStr != null && dateMURStr.contains("#")) {
					String[] parts = dateMURStr.split("#");
					if (parts.length > 0)
						murNumber = parts[0];
					if (parts.length > 1)
						fileModDateTime = parts[1];
				}

				Integer uploadKey = credenceMirrorExcelService.isFileUploaded(fileType, file.getName(), sourceSystem,fileModDateTime, murNumber, 1);

				if (uploadKey > 0) {

					// Read Excel file
					List<CredenceMirrorExcelVo> transactionList = credenceMirrorExcelService.readExcel(file);

					// Save transactions
					String errorMsg = credenceMirrorExcelService.saveTransactionsInBatches(transactionList,file.getName(), uploadKey);

					if (errorMsg == null) {
						errorMsg = env.getProperty("records.inserted.success");
					}

					// Move file to processed
					Files.move(file.toPath(), Paths.get(processedPath, file.getName()));

					logger.info("processTransactionFiles({}) : Excel file processed successfully", file.getName());

					// Validation
					credenceMirrorExcelService.validateUploadFile(uploadKey);
				} else {
					logger.warn("processTransactionFiles({}) : Skipping file due to missing uploadKey", file.getName());
				}

			} catch (Exception e) {
				logger.error("processTransactionFiles({})", file.getName(), e);
			}
		}
	}
}
