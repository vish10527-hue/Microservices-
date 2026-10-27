package in.ameya.schedulers.service.fileSchedularService;

import java.io.File;
import java.util.List;

import in.ameya.schedulers.vo.fileSeries.FottCsvVo;

public interface FottCsvService {

	public Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag);

	public String saveTransactionsInBatches(List<FottCsvVo> transactionList,String fileName, Integer uploadKey);

	public List<FottCsvVo> readFile(File file) throws Exception ;

	public void validateUploadFile(Integer uploadKey); 
 
}
