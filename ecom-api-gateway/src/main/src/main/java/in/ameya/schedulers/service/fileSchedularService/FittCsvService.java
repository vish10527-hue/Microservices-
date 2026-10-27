package in.ameya.schedulers.service.fileSchedularService;

import java.io.File;
import java.util.List;

import in.ameya.schedulers.vo.fileSeries.FittCsvVo;

public interface FittCsvService {

	public Integer isFileUploaded(String msgType, String strFileName, int sourceSystem, String fileModDateTime, String murNumber, int inOutFlag);

	public String saveTransactionsInBatches(List<FittCsvVo> transactionList,String fileName, Integer uploadKey);

	public List<FittCsvVo> readFile(File file) throws Exception ;

	public void validateUploadFile(Integer uploadKey); 
 
}
